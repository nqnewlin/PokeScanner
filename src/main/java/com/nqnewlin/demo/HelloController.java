package com.nqnewlin.demo;

import com.github.oscar0812.pokeapi.models.pokemon.Pokemon;
import com.github.oscar0812.pokeapi.utils.Client;
import com.github.oscar0812.pokeapi.utils.Information;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nqnewlin.demo.models.PokemonData;
import com.nqnewlin.demo.models.PokemonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class HelloController {

    @Autowired
    PokemonDataService dataService;

    @Autowired
    CsvService csvService;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot";
    }

    @GetMapping("/pokemon")
    public ResponseEntity<?> poke(@RequestHeader(value = "start")String start,
                                  @RequestHeader(value = "end")String end,
                                  @RequestHeader(value = "save", required = false, defaultValue = "false")String save,
                                  @RequestHeader(value = "csv", required = false, defaultValue = "false")String csv) {
        int startVal = Integer.parseInt(start);
        int endVal = Integer.parseInt(end);

        ArrayList<String> response = new ArrayList<>();

        ArrayList<PokemonData> pokemonDatas = new ArrayList<>();


        for (int i = startVal; i <= endVal; i++) {

            for (PokemonData data : dataService.getPokemon(i, true)) {
                pokemonDatas.add(data);
            }

        }
        int total = pokemonDatas.size();

        List<PokemonData> error = new ArrayList<>();
        if (save.equals("true")) {
            for (PokemonData data : pokemonDatas) {
                //System.out.println(data.toJson());
                try {
                    dataService.saveSingleImage(data);
                } catch (Exception e) {
                    Logger log = Logger.getLogger(PokemonDataService.class.getName());
                    log.log(Level.SEVERE, e.getMessage());
                    error.add(data);
                    //return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            response.add(total + " images saved");
            csvService.saveError(error);
        }
        if (csv.equals("true")) {
            csvService.saveCsv(pokemonDatas);
            response.add(total + " pokemon saved to csv");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable("id")int id) throws IOException {
        System.out.println("Id: " + id);
        byte[] image = dataService.getImage(id);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping(path = "/pokemon/images")
    public ResponseEntity<?> pokeImage(@RequestHeader(value = "start")int start,
                                  @RequestHeader(value = "end")int end) {
        int total = end - start + 1;
        try {
            dataService.saveImages(start, end);
            return new ResponseEntity<>(total + " images saved", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "/testing")
    public String testing(@RequestHeader ("start")int start,
                          @RequestHeader ("end")int end) {
        for (int i = start; i <= end; i++) {
            dataService.testForms(i);
        }

        return "done";
    }


}
