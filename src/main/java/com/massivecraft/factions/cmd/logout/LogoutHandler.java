package com.massivecraft.factions.cmd.logout;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogoutHandler {

    public static Map<String, LogoutHandler> factionDatas = new HashMap<>();
    private final Map<UUID, Long> logoutCooldown = new HashMap<>();
    private final String name;

    public LogoutHandler(String name) {
        this.name = name;
        factionDatas.put(name, this);
    }

    public static LogoutHandler getByName(String name) {
        LogoutHandler logoutHandler = factionDatas.get(name);
        return logoutHandler == null ? new LogoutHandler(name) : factionDatas.get(name);
    }

    public void applyLogoutCooldown(Player player) {
        logoutCooldown.put(player.getUniqueId(), System.currentTimeMillis() + (30 * 1000));

        Bukkit.getScheduler().runTaskLater(FactionsPlugin.getInstance(), () -> {
            if (isLogoutActive(player)) {
                player.setMetadata("Logout", new FixedMetadataValue(FactionsPlugin.getInstance(), true));
                player.kickPlayer(String.valueOf(TL.COMMAND_LOGOUT_KICK_MESSAGE));
                cancelLogout(player);
            }
        }, Conf.logoutCooldown * 20L);
    }

    public boolean isLogoutActive(Player player) {
        return logoutCooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis() < logoutCooldown.get(player.getUniqueId());
    }

    public void cancelLogout(Player player) {
        logoutCooldown.remove(player.getUniqueId());
    }

    public String getName() {
        return name;
    }
}
