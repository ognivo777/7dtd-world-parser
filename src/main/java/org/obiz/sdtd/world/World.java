package org.obiz.sdtd.world;

import org.w3c.dom.Node;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.obiz.sdtd.XmlTools.findNodes;

public class World {
    public static final String DTM_RAW = "\\dtm.raw"; //World heights
    Path baseDir;
    private File heightsFile;
    private int mapSize;
    private List<WorldPrefab> prefabs = new ArrayList<>();

    public World(Path baseDir) {
        this.baseDir = baseDir;
    }

    public World(String baseDir) {
        this(Path.of(baseDir));
    }

    public void read() {
        String dtmFileName = baseDir + DTM_RAW;
        heightsFile = new File(dtmFileName);
        long fileLength = heightsFile.length();
//        log("dtm.raw fileLength = " + fileLength);
        mapSize = (int) Math.round(Math.sqrt(fileLength / 2.));
//        log("Detected mapSize: " + mapSize);

//Load prefabs
        /*
<?xml version="1.0" encoding="UTF-8"?>
<prefabs>
  <decoration type="model" name="rwg_tile_rural_cap" position="-2896,30,-1546" rotation="0" />
  <decoration type="model" name="army_camp_04" position="3180,37,-1167" rotation="2" />
</prefabs>
        */

        findNodes("/prefabs/decoration", baseDir.resolve("prefabs.xml")).forEach(node ->
                prefabs.add(
                    new WorldPrefab(this,
                            getAttr(node, "type"),
                            getAttr(node, "name"),
                            getAttr(node, "position"),
                            getAttr(node, "rotation")
                    )));
    }

    private String getAttr(Node node, String attr) {
        return node.getAttributes().getNamedItem(attr).getNodeValue();
    }

    public int getMapSize() {
        return mapSize;
    }

    public List<WorldPrefab> getPrefabs() {
        return prefabs;
    }
}
