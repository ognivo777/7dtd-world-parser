package org.obiz.sdtd;

import org.obiz.sdtd.steam.SteamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private static Context instance;
    private static Map<String, String> prefabs;

    private SteamUtils steamUtils;
    private Context() {
        steamUtils = new SteamUtils();
        prefabs = new HashMap<>();
        try {
            steamUtils.init();
            Files.walk(Path.of(steamUtils.getPrefabDir()))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".xml"))
                    .forEach(path -> {
                        String toLowerCaseName = path.getFileName().toString().toLowerCase();
                        prefabs.put(toLowerCaseName.substring(0, toLowerCaseName.length()-4), path.getParent().toString());
                    });
            System.out.println(prefabs.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Context getInstance() {
        if (instance==null) {
            instance = new Context();
        }
        return instance;
    }

    public String getPrefabBaseDir(String name) {
        return prefabs.get(name.toLowerCase());
    }

    public SteamUtils getSteamUtils() {
        return steamUtils;
    }
}
