package org.obiz.sdtd;

import org.obiz.sdtd.game.GameTts;
import org.obiz.sdtd.steam.SteamUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.obiz.sdtd.XmlTools.*;

public class Parser {
    public static void main(String[] args) throws IOException {

        SteamUtils steamUtils = new SteamUtils();
        steamUtils.init();
//        analyzeRwqTilesXml(steamUtils);
        //analyzeTilesTts(Context.getInstance().getSteamUtils().getPrefabDirRGW());
        analyzeTilesTts(Context.getInstance().getSteamUtils().getPrefabDirPoi());
    }

    private static void analyzeTilesTts(String ttsPath) throws IOException {
        Files.walk(Path.of(ttsPath), 1)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".tts"))
//                .filter(path -> path.getFileName().toString().toLowerCase().startsWith("rwg_tile_"))
                .map(path -> path.getFileName().toString())
                .map(fileName -> fileName.substring(0, fileName.length()-4))
                .peek(path -> {
                    System.out.println("path = " + path);
                })
                .map(GameTts::new)
                .parallel()
                .forEach(gameTts -> {
                    BufferedImage bufferedImage = gameTts.drawTopView();
                    try {
                        Tools.saveImage(bufferedImage, gameTts.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        ;
    }

    private static void analyzeRwqTilesXml(SteamUtils steamUtils) throws IOException {
        //Load blocks definitions
        analyzePrefabAndBlocks(steamUtils);
//        System.exit(0);

        Map<String, Set<String>> props = new HashMap<>();

        String prefabDirPoi = steamUtils.getPrefabDirRGW();

        Files.walk(Path.of(prefabDirPoi), 1)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".xml"))
                .filter(path -> path.getFileName().toString().toLowerCase().startsWith("rwg_tile_"))
                .parallel()
                .forEach(path -> {
                    System.out.println("path = " + path.getFileName());
                    findNodes("//property[@name='PrefabSize']", path)
                            .filter(node -> !node.hasChildNodes()).forEach(node -> {
                                System.out.println(XmlTools.nodeToString(node));
                                String name = getAttr(node, "name");
                                String value = getAttr(node, "value");
                                Set<String> valueSet = new HashSet<>();
                                if (value.contains(",")) {
                                    Collections.addAll(valueSet, value.split(",\\s*"));
                                } else {
                                    valueSet.add(value);
                                }
                                System.out.println(name + ":\t" + value);
                                props.computeIfAbsent(name, s -> valueSet);
                                props.computeIfPresent(name, (s, v) -> {
                                    v.addAll(valueSet);
                                    return v;
                                });
                            });
                });

//            props.entrySet().stream().sorted(Comparator.comparingInt(o -> o.getValue().size())).forEach(e -> {
//                System.out.println(e.getKey() + ":\t" + e.getValue().size());
//            });
//
//            System.out.println("\n\nTags");
//            props.get("Tags").stream().sorted().forEach(s -> System.out.print(s + ";"));
//            System.out.println("\n\nZoning");
//            props.get("Zoning").stream().sorted().forEach(s -> System.out.print(s + ";"));
//            System.out.println("\n\nQuestTags");
//            props.get("QuestTags").stream().sorted().forEach(s -> System.out.print(s + ";"));
//            System.out.println("\n\nThemeTags");
//            props.get("ThemeTags").stream().sorted().forEach(s -> System.out.print(s + ";"));
//            System.out.println("\n\nCondition");
//            props.get("Condition").stream().sorted().forEach(s -> System.out.print(s + ";"));
//            System.out.println("\n\nAllowedTownships");
//            props.get("AllowedTownships").stream().sorted().forEach(s -> System.out.print(s + ";"));
    }

    public static void analyzePrefabAndBlocks(SteamUtils steamUtils) {
        Map<String, Integer> namesCnt = new ConcurrentHashMap<>();
        Map<String, String> blockMaterials = new ConcurrentHashMap<>();
        String gameConfigDir = steamUtils.getGameConfigDir();
        System.out.println("gameConfigDir = " + gameConfigDir);
        Path blocksPath = Path.of(gameConfigDir).resolve("blocks.xml");
//        Path blocksPath = Path.of(gameConfig).resolve("blocks.xml");
        System.out.println("blocks path = " + blocksPath.getFileName());
        findNodes("//block[@name]", blocksPath)
                .parallel()
                .forEach(node -> {
                    String blockName = getAttr(node, "name");
                    String materialName = getAttrOfFirstChild(node, "property[@name='Material']", "value");
                    blockMaterials.put(blockName, materialName);
                    namesCnt.merge(materialName, 1, Integer::sum);
        });
        namesCnt.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println(e.getKey() + ":" + e.getValue()));

        //Load world prefabs
//        Path prefabsPath = Path.of(world).resolve("prefabs.xml");
//        Path prefabsPath = Path.of(steamUtils.getPredefinedWorldsDir() + "Pregen10k1").resolve("prefabs.xml");
//        System.out.println("prefabs path = " + prefabsPath.getFileName());
//        findNodes("//decoration[@name]", prefabsPath).forEach(node -> {
//            String blockName = getAttr(node, "name");
//        });
    }

}
