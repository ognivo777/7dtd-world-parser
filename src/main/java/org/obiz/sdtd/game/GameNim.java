package org.obiz.sdtd.game;

import com.google.common.io.LittleEndianDataInputStream;
import org.obiz.sdtd.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class GameNim {
    private HashMap<Integer, String> blockNames = new HashMap<>();

    public GameNim(String name) {
        String path = Context.getInstance().getPrefabBaseDir(name) + "\\" + name + ".blocks.nim";
        if(Files.exists(Path.of(path))) {
            try (LittleEndianDataInputStream dis = new LittleEndianDataInputStream(new FileInputStream(path))) {
                dis.skipBytes(8);

                while (dis.available() > 4) {
                    int id = dis.readInt();
//                System.out.println("id = " + id);
                    int len = dis.readUnsignedByte();
//                System.out.println("len = " + len);
                    if(dis.available() >= len ) {
                        String blockName = new String(dis.readNBytes(len), StandardCharsets.UTF_8);
        //                System.out.println("name = " + name);
                        blockNames.put(id, blockName);
                    }
                }

            } catch (IOException e) {
                System.out.println(name);
                throw new RuntimeException(e);

            }
        }
    }

    public String blockNameById(Integer id) {
        return blockNames.get(id);
    }
}
