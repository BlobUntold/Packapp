package com.example.pack.ui.ai;

import com.example.pack.data.models.GearItem;
import com.example.pack.data.models.Backpack;
import com.example.pack.data.models.Trip;

import java.util.List;

public class AIPromptBuilder {

    public static String build(Trip trip, List<GearItem> gear, List<Backpack> packs) {

        StringBuilder sb = new StringBuilder();

        sb.append("You are an expert outdoor gear guide.\n");
        sb.append("Given a trip and available gear, generate:\n");
        sb.append("- Best backpack choice\n");
        sb.append("- Essential gear for the trip\n");
        sb.append("- Optional gear\n");
        sb.append("- Warnings (damaged gear, unsafe sleeping bag temp, weather mismatches)\n");
        sb.append("\n");

        sb.append("TRIP:\n");
        sb.append("Environment: ").append(trip.environment).append("\n");
        sb.append("Weather: ").append(trip.weather).append("\n");
        sb.append("Min Temp: ").append(trip.minTemp).append("\n");
        sb.append("Max Temp: ").append(trip.maxTemp).append("\n");
        sb.append("Distance: ").append(trip.distanceKm).append("\n");
        sb.append("Elevation: ").append(trip.elevationGain).append("\n");
        sb.append("Group Size: ").append(trip.groupSize).append("\n");
        sb.append("Nights: ").append(trip.nights).append("\n");
        sb.append("\n");

        sb.append("USER GEAR:\n");
        for (GearItem g : gear) {
            sb.append("- ")
                    .append(g.name)
                    .append(" | type: ").append(g.gearType)
                    .append(" | cond: ").append(g.condition)
                    .append(" | notes: ").append(g.notes)
                    .append(" | cold: ").append(g.goodForCold)
                    .append(" | rain: ").append(g.goodForRain)
                    .append(" | snow: ").append(g.goodForSnow)
                    .append(" | wind: ").append(g.goodForWind)
                    .append(" | comfortTemp: ").append(g.comfortTemp)
                    .append(" | extremeTemp: ").append(g.extremeTemp)
                    .append(" | capacity: ").append(g.capacity)
                    .append("\n");
        }

        sb.append("\n");
        sb.append("Backpacks:\n");
        for (Backpack b : packs) {
            sb.append("- ")
                    .append(b.name)
                    .append(" (maxVolume: ").append(b.maxVolume)
                    .append(", maxWeight: ").append(b.maxWeight)
                    .append(")\n");
        }

        sb.append("\n");
        sb.append("Return a JSON object with the following schema:\n");
        sb.append("{\n")
                .append("  \"backpack\": \"string\",\n")
                .append("  \"essential\": [\"string\"],\n")
                .append("  \"optional\": [\"string\"],\n")
                .append("  \"warnings\": [\"string\"]\n")
                .append("}\n");


        return sb.toString();
    }
}
