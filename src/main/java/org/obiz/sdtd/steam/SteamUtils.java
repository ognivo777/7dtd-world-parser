package org.obiz.sdtd.steam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SteamUtils {
    private String steamInstallDir;
    private Optional<String> generatedWorldsDir;
    private String predefinedWorldsDir;
    private String gameDir;
    private String gameConfigDir;
    private final String PREFAB_DIR = "\\Data\\Prefabs\\";
    private final String CONFIG_DIR = "\\Data\\Config\\";
    private final String PREFAB_POI_DIR = "\\Data\\Prefabs\\POIs\\";
    private final String PREFAB_RWG_DIR = "\\Data\\Prefabs\\RWGTiles\\";
    private String prefabDir;
    private String prefabDirPoi;
    private String prefabDirRGW;
    private final String PREDEFINED_WORLDS = "\\Data\\Worlds\\";

    public SteamUtils() {
        steamInstallDir = "C:\\Program Files (x86)\\Steam";
    }

    public void init() throws IOException {

        Path libFilders = Path.of(steamInstallDir + "\\SteamApps\\libraryfolders.vdf");
        List<String> steamConfigStrings = Files.lines(libFilders).collect(Collectors.toList());
        String currPath = ".";
        for (String steamConfigString : steamConfigStrings) {
            if (steamConfigString.contains("\"path\"")) {
                currPath = steamConfigString.trim().split("\\s+")[1];
                currPath = currPath.substring(1, currPath.length() - 1);
            } else if (steamConfigString.contains("\"251570\"")) { // it's steam game id of 7dtd
                break;
            }
        }
        gameDir = currPath + "\\steamapps\\common\\7 Days To Die";
        predefinedWorldsDir = gameDir + PREDEFINED_WORLDS;
        gameConfigDir = gameDir + CONFIG_DIR;
        prefabDir = gameDir + PREFAB_DIR;
        prefabDirPoi = gameDir + PREFAB_POI_DIR;
        prefabDirRGW = gameDir + PREFAB_RWG_DIR;
        String generatedWorldsDirPath = System.getenv("USERPROFILE") + "\\AppData\\Roaming\\7DaysToDie\\GeneratedWorlds";
        if(Files.exists(Path.of(generatedWorldsDirPath))) {
            generatedWorldsDir = Optional.of(generatedWorldsDirPath);
        } else {
            generatedWorldsDir = Optional.empty();
        }
    }

    public String getSteamInstallDir() {
        return steamInstallDir;
    }

    public Optional<String> getGeneratedWorldsDir() {
        return generatedWorldsDir;
    }

    public String getPredefinedWorldsDir() {
        return predefinedWorldsDir;
    }

    public String getGameDir() {
        return gameDir;
    }

    public String getGameConfigDir() {
        return gameConfigDir;
    }

    public String getPrefabDir() {
        return prefabDir;
    }

    public String getPrefabDirPoi() {
        return prefabDirPoi;
    }

    public String getPrefabDirRGW() {
        return prefabDirRGW;
    }
}
