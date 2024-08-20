package org.obiz.sdtd;

import org.obiz.sdtd.game.GamePrefab;
import org.obiz.sdtd.game.GamePrefabFactory;
import org.obiz.sdtd.world.World;
import org.obiz.sdtd.world.WorldPrefab;

import javax.imageio.ImageIO;
import javax.tools.Tool;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DrawRoads {
    private static final String WORLD_NAME = "PREGEN6k";

    public static void main(String[] args) throws IOException {
//        World world = new World(Context.getInstance().getSteamUtils().getPredefinedWorldsDir() + WORLD_NAME);
        World world = new World("L:\\tmp\\7dtd\\Urirebu Territory");
        world.read();
        System.out.println("world.getMapSize() = " + world.getMapSize());

        List<WorldPrefab> prefabs = world.getPrefabs().stream()
//                .filter(worldPrefab -> worldPrefab.getName().startsWith("rwg_tile_"))
//                .filter(worldPrefab -> worldPrefab.getName().startsWith("air"))
                .collect(Collectors.toList());
        prefabs.forEach(worldPrefab -> System.out.println(worldPrefab.getName() +  ": " + worldPrefab.getPosition()
                        + "(" + worldPrefab.getPositionX() + "," + worldPrefab.getPositionY() + "," + worldPrefab.getRotation() +")"));

        BufferedImage map = new BufferedImage(world.getMapSize(), world.getMapSize(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = map.createGraphics();

        prefabs.parallelStream().forEach(worldPrefab -> {
            GamePrefab gamePrefab = GamePrefabFactory.get(worldPrefab.getName());
            BufferedImage topViewImageOfGamePrefab = gamePrefab.createTopViewImage();
            BufferedImage orientatedPrefab = Tools.rotateClockwise90(topViewImageOfGamePrefab, (4-worldPrefab.getRotation()) % 4);
//            g.drawImage(orientatedPrefab, null, worldPrefab.getPositionX(), (4 - worldPrefab.getPositionY()) % 4);
            g.drawImage(orientatedPrefab, null, worldPrefab.getPositionX(), worldPrefab.getPositionY());
        });

//        prefabs.parallelStream().map(worldPrefab -> {
//            worldPrefab.createTile();
//            return worldPrefab;
//        }).sequential()
//                .forEach(worldPrefab -> worldPrefab.drawTo(g));

        String filename = "result.png";
        File jpegFile = new File(filename);
        ImageIO.write(map, "PNG", jpegFile);
        Desktop.getDesktop().open(jpegFile);
    }

}
