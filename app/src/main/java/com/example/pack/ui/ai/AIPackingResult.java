package com.example.pack.ui.ai;

import java.util.List;

public class AIPackingResult {
    public String backpackName;
    public List<String> essential;
    public List<String> optional;
    public static List<String> warnings;

    public AIPackingResult(String bp, List<String> e, List<String> o, List<String> w){
        backpackName = bp;
        essential = e;
        optional = o;
        warnings = w;
    }
}