package org.obiz.sdtd.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GamePrefab {
    private String name;
    private int YOffset;
    private int RotationToFaceNorth;
    private int PrefabSizeX;
    private int PrefabSizeZ;
    private int PrefabSizeY;

    private GameTts blocks;
    private BufferedImage topView;

    public GamePrefab(String name, String YOffset, String rotationToFaceNorth, String prefabSize) {
        this.name = name;
        this.YOffset = Integer.parseInt(YOffset);
        this.RotationToFaceNorth = Integer.parseInt(rotationToFaceNorth);
        List<Integer> sizes = Arrays.stream(prefabSize.split(",\\s*")).map(Integer::parseInt).collect(Collectors.toList());
        this.PrefabSizeX = sizes.get(0);
        this.PrefabSizeZ = sizes.get(1);
        this.PrefabSizeY = sizes.get(2);
    }

    public BufferedImage createTopViewImage() {
        if(topView!=null) {
            return topView;
        }
//        topView = new BufferedImage(PrefabSizeX, PrefabSizeY, BufferedImage.TYPE_INT_RGB);
//        Graphics g = topView.getGraphics();
//        g.drawRect(0,0, PrefabSizeX-1, PrefabSizeY-1);
//        g.drawLine(0,0, PrefabSizeX, PrefabSizeY);
//        g.drawLine(0,PrefabSizeY, PrefabSizeX, 0);
//        g.drawOval(0,0, PrefabSizeX, PrefabSizeY);
        //TODO отрисовать префаб

        GameTts gameTts = new GameTts(name);
        topView = gameTts.drawTopView();
        return topView;
    }

    public String getName() {
        return name;
    }

    public int getYOffset() {
        return YOffset;
    }

    public int getRotationToFaceNorth() {
        return RotationToFaceNorth;
    }

    public int getPrefabSizeX() {
        return PrefabSizeX;
    }

    public int getPrefabSizeZ() {
        return PrefabSizeZ;
    }

    public int getPrefabSizeY() {
        return PrefabSizeY;
    }

    @Override
    public String toString() {
        return "GamePrefab{" +
                "name='" + name + '\'' +
                ", YOffset=" + YOffset +
                ", RotationToFaceNorth=" + RotationToFaceNorth +
                ", PrefabSizeX=" + PrefabSizeX +
                ", PrefabSizeZ=" + PrefabSizeZ +
                ", PrefabSizeY=" + PrefabSizeY +
                '}';
    }
}
