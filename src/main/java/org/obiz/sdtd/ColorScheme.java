package org.obiz.sdtd;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ColorScheme {
    private static final Color concrete = Color.decode("#a0a098");
    private static final Color asphalt = Color.decode("#696963");
    private static final Color metal = Color.decode("#98979b");
    public static final Color terrain = Color.decode("#62754d");
    private static final Color wood = Color.decode("#90753c");
    private static final Color brick = Color.decode("#935543");
    private static final Color cobblestone = Color.decode("#6f7265");
    private static final Color other = Color.decode("#5C5C5C");

    private static Map<String, Color> cache = new ConcurrentHashMap<>();
    public static Color colorForBlockNew(String blockName) {
        if(blockName==null)
            return other;
        return cache.computeIfAbsent(blockName, s -> {
            if(s.toLowerCase().contains("concrete")) {
                return concrete;
            } else if(s.toLowerCase().contains("asphalt")) {
                return asphalt;
            } else if(s.toLowerCase().contains("metal")) {
                return metal;
            } else if(s.toLowerCase().contains("terrain")) {
                return terrain;
            } else if(s.toLowerCase().contains("wood")) {
                return wood;
            } else if(s.toLowerCase().contains("brick")) {
                return brick;
            } else if(s.toLowerCase().contains("cobblestone")) {
                return cobblestone;
//                                } else if(blockName.toLowerCase().contains("")) {
//                                    return Color.decode("");
//                                } else if(blockName.toLowerCase().contains("")) {
//                                    return Color.decode("");
//                                } else if(blockName.toLowerCase().contains("")) {
//                                    return Color.decode("");
//                                } else if(blockName.toLowerCase().contains("")) {
//                                    return Color.decode("");
            } else {
                return other;
            }
        });



    }

}
