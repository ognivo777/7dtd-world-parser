package org.obiz.sdtd.world;

import org.obiz.sdtd.game.GamePrefab;
import org.obiz.sdtd.game.GamePrefabFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorldPrefab {
    private String type;
    private String name;
    private String position;
    private int positionX;
    private int positionY;
    private int positionZ;
    private int rotation;
    private GamePrefab gamePrefab;
    private BufferedImage image;

    public WorldPrefab(World world, String type, String name, String position, String rotation) {
        this.type = type;
        this.name = name;
        this.position = position;
        this.rotation = Integer.parseInt(rotation);
        List<Integer> positionsList = Arrays.stream(position.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toList());
        positionX = positionsList.get(0) + world.getMapSize()/2;
//        positionX = world.getMapSize()/2 - positionsList.get(0);
        positionZ = positionsList.get(1);
        // H - (y + H/2) = H - y - H/2 = H/2 - y
        positionY = world.getMapSize()/2 - positionsList.get(2);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getRotation() {
        return rotation;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getPositionZ() {
        return positionZ;
    }

    public BufferedImage createTile() {
        gamePrefab = GamePrefabFactory.get(this.getName());
        image = gamePrefab.createTopViewImage();
        return image;
    }

    public synchronized void drawTo(Graphics2D g) {
        g.drawImage(gamePrefab.createTopViewImage(), null, getPositionX(), getPositionY());
    }
}
