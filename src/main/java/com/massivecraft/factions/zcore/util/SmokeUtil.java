package com.massivecraft.factions.zcore.util;

import org.bukkit.Effect;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Random;

// http://mc.kev009.com/Protocol
// -----------------------------
// Smoke Directions 
// -----------------------------
// Direction ID    Direction
//            0    South - East
//            1    South
//            2    South - West
//            3    East
//            4    (Up or middle ?)
//            5    West
//            6    North - East
//            7    North
//            8    North - West
//-----------------------------

public class SmokeUtil {

    public static Random random = new Random();

    // -------------------------------------------- //
    // Spawn once
    // -------------------------------------------- //

    // Simple Cloud ========
    public static void spawnCloudSimple(Location location) {
        for (int i = 0; i <= 8; i++)
            spawnSingle(location, i);
    }

    public static void spawnCloudRandom(Collection<Location> locations, float thickness) {
        for (Location location : locations)
            spawnCloudRandom(location, thickness);
    }

    public static void spawnSingleRandom(Location location) {
        spawnSingle(location, random.nextInt(9));
    }

    // Random Cloud ========
    public static void spawnCloudRandom(Location location, float thickness) {
        int singles = (int) Math.floor(thickness * 9);
        for (int i = 0; i < singles; i++)
            spawnSingleRandom(location.clone());
    }

    // Single ========
    public static void spawnSingle(Location location, int direction) {
        if (location == null) return;
        location.getWorld().playEffect(location.clone(), Effect.SMOKE, direction);
    }

    // -------------------------------------------- //
    // Attach continuous effects to or locations
    // -------------------------------------------- //

    // TODO

}
