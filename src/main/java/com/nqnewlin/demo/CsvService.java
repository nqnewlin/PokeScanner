package com.nqnewlin.demo;

import com.google.gson.JsonObject;
import com.nqnewlin.demo.models.PokemonData;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
    private String[] header = {"Number", "Name", "Evolution_Chain", "isBaby", "hasGenderDifference",
        "genderRate", "hasForms", "forms", "isForm", "Types", "isLegendary", "isMythical", "region"};
    public void saveCsv(List<PokemonData> pokemonData) {
        File file = new File("./Pokemon.csv");
        List<String[]> allData = new ArrayList<>();

        try {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output);

            System.out.println("Size: " + pokemonData.size());

            for (PokemonData data : pokemonData) {
                List<String> info = new ArrayList<>();

                info.add(String.valueOf(data.getDexNumber()));
                info.add(data.getName());
                info.add(String.valueOf(data.getEvolutionChain()));
                info.add(String.valueOf(data.isBaby()));
                info.add(String.valueOf(data.isHasGenderDifference()));
                info.add((String.valueOf(data.getGenderRate())));
                info.add(String.valueOf(data.isHasForms()));
                if (data.isHasForms()) {
                    info.add(data.getFormIds().toString());
                } else info.add("");
                info.add(String.valueOf(data.isForm()));
                info.add(data.getTypes().toString());
                info.add(String.valueOf(data.isLegendary()));
                info.add(String.valueOf(data.isMythical()));
                info.add(data.getRegion());

                allData.add(info.toArray(String[]::new));
            }


            writer.writeNext(header);
            writer.writeAll(allData);

            writer.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void saveError(List<PokemonData> pokemonData) {
        File file = new File("./PokemonNoImage.csv");
        List<String[]> allData = new ArrayList<>();

        try {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output);

            System.out.println("Size: " + pokemonData.size());

            for (PokemonData data : pokemonData) {
                List<String> info = new ArrayList<>();

                info.add(String.valueOf(data.getDexNumber()));
                info.add(data.getName());
                info.add(String.valueOf(data.getEvolutionChain()));
                info.add(String.valueOf(data.isBaby()));
                info.add(String.valueOf(data.isHasGenderDifference()));
                info.add((String.valueOf(data.getGenderRate())));
                info.add(String.valueOf(data.isHasForms()));
                if (data.isHasForms()) {
                    info.add(data.getFormIds().toString());
                } else info.add("");
                info.add(String.valueOf(data.isForm()));
                info.add(data.getTypes().toString());
                info.add(String.valueOf(data.isLegendary()));
                info.add(String.valueOf(data.isMythical()));
                info.add(data.getRegion());

                allData.add(info.toArray(String[]::new));
            }


            writer.writeNext(header);
            writer.writeAll(allData);

            writer.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
