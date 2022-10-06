package com.nqnewlin.demo.models;

import com.github.oscar0812.pokeapi.models.pokemon.Pokemon;
import com.github.oscar0812.pokeapi.utils.Client;
import com.github.oscar0812.pokeapi.utils.Information;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonDataService {

    List<PokemonData> pokemonDataList = new ArrayList<>();

    String URL = "https://pokeapi.co/api/v2/pokemon-species/";

    public List<PokemonData> getPokemon(int id, boolean notForm) {
        if (notForm) { pokemonDataList.clear(); }
        Pokemon pokemon = Client.getPokemonById(id);

        PokemonData pokemonData = new PokemonData(id,
                pokemon.getName().substring(0,1).toUpperCase() + pokemon.getName().substring(1),
                pokemon.getSprites().getFrontDefault(),
                pokemon.getSpecies().getEvolutionChain().getId(),
                pokemon.getSpecies().getIsBaby(),
                pokemon.getSpecies().getHasGenderDifferences(),
                pokemon.getSpecies().getFormsSwitchable(),
                pokemon.getSpecies().getGeneration().getMainRegion().getName(),
                pokemon.getSpecies().getGenderRate());


        pokemonData.setTypes(getTypes(pokemon));
        pokemonData.setForm(!notForm);
        getMythicalLegendary(pokemonData, id);

        pokemonDataList.add(pokemonData);

        /** TESTING catch non-switchable forms **/
        List<?> forms = pokemon.getSpecies().getVarieties();
        if (!pokemonData.isHasForms() && forms.size() > 1) {
            pokemonData.setHasForms(true);
        }

        if (pokemonData.isHasForms() && notForm) {
            getForms(pokemon, pokemonData);
        } else {
            pokemonData.setHasForms(false);
        }

        return pokemonDataList;
    }

    public void getForms(Pokemon pokemon, PokemonData pokemonData) {
        List<?> varieties = pokemon.getSpecies().getVarieties();
        List<Integer> forms = new ArrayList<>();


        for (var variety : varieties) {
            JsonObject json = new Gson().fromJson(variety.toString(), JsonObject.class);
            JsonObject pokemonJson = new Gson().fromJson(json.get("pokemon"), JsonObject.class);

            String pokemonFormUrl = pokemonJson.get("url").getAsString();
            String[] url = pokemonFormUrl.split("/");
            int id = Integer.parseInt(url[url.length - 1]);
            if (pokemon.getId() != id) {
                forms.add(id);
                pokemonData.setFormIds(id);

            }
        }
        for (int form : forms) {
            getPokemon(form, false);
        }
    }

    public List<String> getTypes(Pokemon pokemon) {
        List<String> pokemonTypes = new ArrayList<>();
        List<?> types = pokemon.getTypes();

        for (var type: types) {
            JsonObject json = new Gson().fromJson(type.toString(), JsonObject.class);
            JsonObject typeJson = new Gson().fromJson(json.get("type").toString(), JsonObject.class);
            String typeName = typeJson.get("name").getAsString();

            pokemonTypes.add(typeName);
        }
        pokemon.getTypes();

        return pokemonTypes;
    }

    public void getMythicalLegendary(PokemonData pokemonData, int id) {

        try {
            if (!pokemonData.isForm()) {
                JsonObject mon = new Gson().fromJson(Information.fromInternet(URL + id), JsonObject.class);

                pokemonData.setLegendary(mon.get("is_legendary").getAsBoolean());
                pokemonData.setMythical(mon.get("is_mythical").getAsBoolean());
            }

        } catch (Exception e) { System.out.println("Error get Legendary: " + e.getMessage()); }
    }

    public byte[] getImage(int id) throws IOException {
        Pokemon pokemon = Client.getPokemonById(id);

        URL url = new URL(pokemon.getSprites().getFrontDefault());
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();

        return response;
    }

    public void saveImages(int start, int end) throws Exception {
        for (int i = start; i <= end; i++) {
            String imageName = String.format("./images/%s.jpg", i);
            try {
                byte[] image = getImage(i);
                FileOutputStream fos = new FileOutputStream(imageName);
                fos.write(image);
                fos.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }
    }

    public void saveSingleImage(PokemonData pokemonData) throws Exception {
        URL imageUrl = null;
        try {
            imageUrl = new URL(pokemonData.getImageUrl());
        } catch (Exception e) {
            System.out.println(pokemonData.getImageUrl());
            throw new Exception("Error reading string url");
        }
        InputStream in = new BufferedInputStream(imageUrl.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        String number = String.format("%05d", pokemonData.getDexNumber());

        String imageName = String.format("./images/%s.jpg", number);
        FileOutputStream fos = new FileOutputStream(imageName);
        fos.write(response);
        fos.close();
        System.out.println("Image " + number + " saved.");
    }

    public void testForms(int id) {
        Pokemon pokemon = Client.getPokemonById(id);
        List<?> varieties = pokemon.getSpecies().getVarieties();

        System.out.print(pokemon.getName() + ": " + varieties.size() + " ");
        System.out.println(pokemon.getSpecies().getFormsSwitchable());

    }
}
