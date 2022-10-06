package com.nqnewlin.demo.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PokemonData {
    @Getter @Setter private int dexNumber; //added

    @Getter @Setter private String name; //added

    // -1 == no gender, 0 == only male, 8 == only female
    @Getter @Setter private int genderRate;

    @Getter @Setter private int evolutionChain; //added

    @Getter @Setter private boolean isBaby; //added

    @Getter @Setter private String region; //added

    @Getter @Setter private boolean isMythical; //added

    @Getter @Setter private boolean isLegendary; //added

    @Getter @Setter private String imageUrl; //added

    @Getter @Setter private boolean hasGenderDifference; //added

    @Getter @Setter private boolean hasForms; //added

    @Getter private List<Integer> formIds; //added

    @Getter @Setter private boolean isForm; //add after build

    @Getter @Setter private List<String> types; //added after build



    public PokemonData() {}

    public PokemonData(int dexNumber,
                       String name,
                       String imageUrl,
                       int evolutionChain,
                       boolean isBaby,
                       boolean hasGenderDifference,
                       boolean hasForms,
                       String region,
                       int genderRate) {
        this.dexNumber = dexNumber;
        this.name = name;
        this.imageUrl = imageUrl;
        this.evolutionChain = evolutionChain;
        this.isBaby = isBaby;
        this.hasGenderDifference = hasGenderDifference;
        this.hasForms = hasForms;
        this.formIds = new ArrayList<>();
        this.region = region;
        this.genderRate = genderRate;

    }

    public void setFormIds(int id) {
        this.formIds.add(id);
    }

    @Override
    public String toString() {
        return  "Pokedex Number: " + dexNumber + " " +
                "Name: " + name + " " +
                "Evolution Chain: " + evolutionChain + " " +
                "Baby: " + isBaby + " " +
                "Image URL: " + imageUrl;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("Number", dexNumber);
        json.addProperty("Name", name);
        json.addProperty("Evolution_Chain", evolutionChain);
        json.addProperty("isBaby", isBaby);
        json.addProperty("hasGenderDifference", hasGenderDifference);
        json.addProperty("genderRate", genderRate);
        json.addProperty("hasForms", hasForms);
        if (hasForms) {
            json.addProperty("forms", formIds.toString());
        }
        json.addProperty("types", types.toString());
        json.addProperty("isLegendary", isLegendary);
        json.addProperty("isMythical", isMythical);
        json.addProperty("region", region);

        return json;
    }


}
