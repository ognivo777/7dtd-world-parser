package org.obiz.sdtd.game;

import com.google.common.io.LittleEndianDataInputStream;
import org.obiz.sdtd.ColorScheme;
import org.obiz.sdtd.Context;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameTts {
    private final int[][] topNotAirBlocks;
    private GameNim nim;
    private int[][][] blockIds;
    private String ttsPath;
    private String name;

    /* координаты блоков
      0_________ X _________
      |\                    \
      | \                    \
      |  Z                    \
      Y   \                    \
      |    \____________________\
      |    |                    |
      |___ |                    |
      \    |                    |
       \   |                    |
        \  |                    |
         \ |                    |
          \|                    |
           |____________________|
    */
    class Header {
        String fileType; //char[4]
        int version; //uint32
        int sizeX; //ushort
        int sizeY; //ushort

        int sizeZ; //ushort
    }

    private Header header;

    public GameTts(String name) {
        this.name = name;

        nim = new GameNim(name);
        ttsPath = Context.getInstance().getPrefabBaseDir(name) + "\\" + name + ".tts";
        System.out.println("ttsPath = " + ttsPath);
        try (LittleEndianDataInputStream dis = new LittleEndianDataInputStream(new FileInputStream(ttsPath))) {
            header = new Header();
            header.fileType = new String(dis.readNBytes(4), StandardCharsets.UTF_8);
            header.version = dis.readUnsignedShort();
            dis.skipBytes(2);
            header.sizeX = dis.readUnsignedShort();
            header.sizeY = dis.readUnsignedShort();
            header.sizeZ = dis.readUnsignedShort();
            System.out.println("header.version = " + header.version);
            System.out.println("header.sizeX = " + header.sizeX);
            System.out.println("header.sizeY = " + header.sizeY);
            System.out.println("header.sizeZ = " + header.sizeZ);

            blockIds = new int[header.sizeZ][header.sizeY][header.sizeX];
            Map<Integer, Integer> countBlocks2 = new HashMap<>();
            topNotAirBlocks = new int[header.sizeZ][header.sizeX];
            int topNotAirBlocksY[][] = new int[header.sizeZ][header.sizeX];

            for (int i = 0; i < header.sizeZ; i++) {
                for (int j = 0; j < header.sizeY; j++) {
                    for (int k = 0; k < header.sizeX; k++) {
                        int b = dis.readUnsignedShort() & 32767;
                        blockIds[i][j][k] = b;
                        String blockName = nim.blockNameById(b);
                        if(blockName ==null) {
                            System.out.println("not found (" + j + ";" + i + "):  " + b);
                        } else if (!blockName.equals("air")) {
                            if(topNotAirBlocksY[i][k]<j) {
                                topNotAirBlocksY[i][k]=j;
                                topNotAirBlocks[i][k]=b;
                            }
                        }
                        countBlocks2.merge(b, 1, Integer::sum);
                        dis.skipBytes(2);
                    }
                }
            }

            System.out.println("=============================");

            countBlocks2.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
                System.out.println(e.getKey() + ":" + e.getValue() + "\t -> " + nim.blockNameById(e.getKey()));
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage draw3d() {
        float pK = .30f;
        int picXsize = Math.round(header.sizeX + header.sizeZ*pK)+1;
        int picYsize = Math.round(header.sizeY + header.sizeZ*pK)+1;

        BufferedImage map = new BufferedImage(picXsize, picYsize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = map.createGraphics();

        for (int i = 0; i < header.sizeZ; i++) {
            for (int j = 0; j < header.sizeY; j++) {
                for (int k = 0; k < header.sizeX; k++) {
                    int b = blockIds[i][header.sizeY-j-1][k];
                    String blockName = nim.blockNameById(b);
                    if(!blockName.equals("air") ) {
                        g.setColor(ColorScheme.colorForBlockNew(blockName));
                        g.drawRect(Math.round(k+i* pK),Math.round(j+i* pK),1,1);
                    }
                }
            }
        }
        return map;
    }

    public BufferedImage drawTopView() {
        BufferedImage map = new BufferedImage(header.sizeX, header.sizeZ,  BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = map.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        for (int i = 0; i < topNotAirBlocks.length; i++) { //Z
            int[] topNotAirBlock = topNotAirBlocks[i];
            for (int j = 0; j < topNotAirBlock.length; j++) { //X
                int b = topNotAirBlock[j];
                String blockName = nim.blockNameById(b);
                Color color = ColorScheme.colorForBlockNew(blockName);
                if(!color.equals(ColorScheme.terrain)) {
                    map.setRGB(j, header.sizeZ - i - 1, color.getRGB());
                }
            }
        }
        return map;
    }

    public String getName() {
        return name;
    }

}
