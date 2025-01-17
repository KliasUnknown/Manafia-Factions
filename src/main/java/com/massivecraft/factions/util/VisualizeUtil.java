package com.massivecraft.factions.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class VisualizeUtil {

    protected static Map<UUID, Set<Location>> playerLocations = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static void addLocation(Player player, Location location, Material type, byte data) {
        getPlayerLocations(player).add(location);
        player.sendBlockChange(location, type, data);
    }

    public static Set<Location> getPlayerLocations(Player player) {
        return getPlayerLocations(player.getUniqueId());
    }

    public static Set<Location> getPlayerLocations(UUID uuid) {
        Set<Location> ret = playerLocations.computeIfAbsent(uuid, k -> new HashSet<>());
        return ret;
    }

    @SuppressWarnings("deprecation")
    public static void addLocation(Player player, Location location, Material material) {
        getPlayerLocations(player).add(location);
        player.sendBlockChange(location, material, (byte) 0);
    }

    @SuppressWarnings("deprecation")
    public static void clear(Player player) {
        Set<Location> locations = getPlayerLocations(player);
        if (locations == null) {
            return;
        }
        for (Location location : locations) {
            Block block = location.getWorld().getBlockAt(location);
            player.sendBlockChange(location, block.getType(), block.getData());
        }
        locations.clear();
    }

}
