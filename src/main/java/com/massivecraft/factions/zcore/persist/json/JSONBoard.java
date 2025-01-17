package com.massivecraft.factions.zcore.persist.json;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.zcore.persist.MemoryBoard;
import com.massivecraft.factions.zcore.util.DiscUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class JSONBoard extends MemoryBoard {
    private static final transient File file = new File(FactionsPlugin.getInstance().getDataFolder() + File.separator + "data" + File.separator, "board.json");

    // -------------------------------------------- //
    // Persistance
    // -------------------------------------------- //

    @Override
    public void convertFrom(MemoryBoard old) {
        this.flocationIds = old.flocationIds;
        forceSave();
        Board.instance = this;
    }

    public Map<String, Map<String, String>> dumpAsSaveFormat() {
        Map<String, Map<String, String>> worldCoordIds = new HashMap<>();

        String worldName, coords;
        String id;

        for (Entry<FLocation, String> entry : flocationIds.entrySet()) {
            worldName = entry.getKey().getWorldName();
            coords = entry.getKey().getCoordString();
            id = entry.getValue();
            if (!worldCoordIds.containsKey(worldName))
                worldCoordIds.put(worldName, new TreeMap<>());
            worldCoordIds.get(worldName).put(coords, id);
        }
        return worldCoordIds;
    }

    public void loadFromSaveFormat(Map<String, Map<String, String>> worldCoordIds) {
        flocationIds.clear();

        String worldName;
        String[] coords;
        int x, z;
        String factionId;

        for (Entry<String, Map<String, String>> entry : worldCoordIds.entrySet()) {
            worldName = entry.getKey();
            for (Entry<String, String> entry2 : entry.getValue().entrySet()) {
                coords = entry2.getKey().trim().split("[,\\s]+");
                x = Integer.parseInt(coords[0]);
                z = Integer.parseInt(coords[1]);
                factionId = entry2.getValue();
                flocationIds.put(new FLocation(worldName, x, z), factionId);
            }
        }
    }

    public void forceSave() {
        forceSave(true);
    }

    public void forceSave(boolean sync) {
        DiscUtil.writeCatch(file, FactionsPlugin.getInstance().gson.toJson(dumpAsSaveFormat()), sync);
    }

    public boolean load() {
        FactionsPlugin.getInstance().log("Attempting to load board modules from disk.");

        if (!file.exists()) {
            FactionsPlugin.getInstance().log("There was no board found that I could load. Generating new file.");
            FactionsPlugin.getInstance().log("ALERT: This process might take a while.");
            forceSave();
            return true;
        }

        try {
            Type type = new TypeToken<Map<String, Map<String, String>>>() {
            }.getType();
            Map<String, Map<String, String>> worldCoordIds = FactionsPlugin.getInstance().gson.fromJson(DiscUtil.read(file), type);
            loadFromSaveFormat(worldCoordIds);
            FactionsPlugin.getInstance().log("Initiated " + flocationIds.size() + " board locations from our disk.");
        } catch (Exception e) {
            e.printStackTrace();
            FactionsPlugin.getInstance().log("(WARNING/FATAL) Failed to load the Faction Board. Please report this stacktracke to thmihnea!");
            return false;
        }

        return true;
    }


}
