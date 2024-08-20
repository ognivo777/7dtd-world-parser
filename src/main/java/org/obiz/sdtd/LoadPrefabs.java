package org.obiz.sdtd;

import org.obiz.sdtd.game.GameTts;

import java.io.IOException;

public class LoadPrefabs {
    public static void main(String[] args) throws IOException {
//        new GamePrefab(Path.of(Context.getInstance().getSteamUtils().getPrefabDirPoi()));
//        String prefab = "bridge_concrete1";
//        String prefab = "funeral_home_01";
//        String prefab = "house_old_modular_04";
//        String prefab = "mp_waste_bldg_04_white";
//        String prefab = "junkyard_lg_01";
//        String prefab = "rwg_tile_intersection";
//        String prefab = "lot_industrial_13";
//        new GameTts(Context.getInstance().getSteamUtils().getPrefabDirPoi() + prefab);
//        String prefab = "lot_industrial_13";
//        Tools.showImage(new GameTts(prefab).draw3d());
        String prefab = "rwg_tile_commercial_cap";
//        Tools.showImage(new GameTts(prefab).drawTopView());
        Tools.showImage(new GameTts(prefab).draw3d());
    }
}