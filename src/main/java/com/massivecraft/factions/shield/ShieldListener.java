package com.massivecraft.factions.shield;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ShieldListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(block.getLocation()));
            return faction != null && faction.isShieldRunning();
        });
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(block.getLocation()));
            return faction != null && faction.isShieldRunning();
        });
    }




    /*
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(block.getLocation()));
            return faction != null && faction.isShieldRunning();
        });
    }

     */
}



