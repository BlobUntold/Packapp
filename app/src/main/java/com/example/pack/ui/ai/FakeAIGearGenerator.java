package com.example.pack.ui.ai;

import com.example.pack.data.models.GearItem;

import java.util.ArrayList;
import java.util.List;

public class FakeAIGearGenerator {

    public static AIPackingResult generate(String prompt, List<GearItem> gear) {

        List<String> essential = new ArrayList<>();
        List<String> optional = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Fake logic: mark damaged gear as warnings
        for (GearItem g : gear) {
            if (g.condition != null && g.condition.toLowerCase().contains("damaged")) {
                warnings.add(g.name + " is damaged.");
            }
            essential.add(g.name);
        }

        return new AIPackingResult(
                "Default Backpack",
                essential,
                optional,
                warnings
        );
    }
}