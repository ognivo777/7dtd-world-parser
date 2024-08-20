package org.obiz.sdtd.game;

import org.obiz.sdtd.Context;
import org.obiz.sdtd.XmlTools;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePrefabFactory {
    private static Map<String, GamePrefab> cache = new ConcurrentHashMap<>();
    public static GamePrefab get(String prefabName) {
        return cache.computeIfAbsent(prefabName, name -> {
            String prefabPath = Context.getInstance().getPrefabBaseDir(name) + "\\" + name + ".xml";
//            System.out.println("prefabPath = " + prefabPath);
            try {
                Document doc = XmlTools.buildDom(Path.of(prefabPath));
                Node  zero = doc.createTextNode("0");
                return new GamePrefab(name,
                        XmlTools.findNodes("//property[@name='YOffset']/@value", doc).findFirst().orElse(zero).getNodeValue(),
                        XmlTools.findNodes("//property[@name='RotationToFaceNorth']/@value", doc).findFirst().orElse(zero).getNodeValue(),
                        XmlTools.findNodes("//property[@name='PrefabSize']/@value", doc).findFirst().orElse(zero).getNodeValue()
                );
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
