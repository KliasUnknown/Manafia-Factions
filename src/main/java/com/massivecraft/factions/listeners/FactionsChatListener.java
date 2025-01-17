package com.massivecraft.factions.listeners;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UnknownFormatConversionException;
import java.util.logging.Level;

public class FactionsChatListener implements Listener {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */

    // this is for handling slashless command usage and faction/alliance chat, set at lowest priority so Factions gets to them first
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerEarlyChat(AsyncPlayerChatEvent event) {
        Player talkingPlayer = event.getPlayer();
        String msg = event.getMessage();
        FPlayer me = FPlayers.getInstance().getByPlayer(talkingPlayer);
        ChatMode chat = me.getChatMode();
        // Is the player entering a password for a warp?
        if (me.isEnteringPassword()) {
            event.setCancelled(true);
            me.sendMessage(ChatColor.DARK_GRAY + event.getMessage().replaceAll("(?s).", "*"));
            if (me.getFaction().isWarpPassword(me.getEnteringWarp(), event.getMessage())) {
                doWarmup(me.getEnteringWarp(), me);
            } else {
                // Invalid Password
                me.msg(TL.COMMAND_FWARP_INVALID_PASSWORD);
            }
            me.setEnteringPassword(false, "");
            return;
        }
        switch (chat) {
            case MOD:
                Faction myFaction = me.getFaction();

                String message = String.format(Conf.modChatFormat, ChatColor.stripColor(me.getNameAndTag()), msg);

                if (me.getRole().isAtLeast(Role.MODERATOR))
                    // Iterates only through the factions' members so we enhance performance.
                    for (FPlayer fplayer : myFaction.getFPlayers()) {
                        if (fplayer.getRole().isAtLeast(Role.MODERATOR))
                            fplayer.sendMessage(message);
                        else if (fplayer.isSpyingChat() && me != fplayer)
                            fplayer.sendMessage("[MCspy]: " + message);
                    }
                else {
                    // Just in case player gets demoted while in faction chat.
                    me.msg(TL.COMMAND_CHAT_MOD_ONLY);
                    event.setCancelled(true);
                    me.setChatMode(ChatMode.FACTION);
                    return;
                }
                Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("Mod Chat: " + message));

                event.setCancelled(true);
                break;
            case FACTION:
                Faction myFaction1 = me.getFaction();

                String message1 = String.format(Conf.factionChatFormat, me.describeTo(myFaction1), msg);
                myFaction1.sendMessage(message1);

                Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("FactionChat " + myFaction1.getTag() + ": " + message1));

                //Send to any players who are spying chat
                for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers())
                    if (fplayer.isSpyingChat() && fplayer.getFaction() != myFaction1 && me != fplayer)
                        fplayer.sendMessage("[FCspy] " + myFaction1.getTag() + ": " + message1);
                event.setCancelled(true);
                break;
            case ALLIANCE:
                Faction myFaction2 = me.getFaction();

                String message2 = String.format(Conf.allianceChatFormat, ChatColor.stripColor(me.getNameAndTag()), msg);

                //Send message to our own faction
                myFaction2.sendMessage(message2);

                //Send to all our allies
                for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers())
                    if (myFaction2.getRelationTo(fplayer) == Relation.ALLY && !fplayer.isIgnoreAllianceChat())
                        fplayer.sendMessage(message2);
                    else if (fplayer.isSpyingChat() && me != fplayer)
                        fplayer.sendMessage("[ACspy]: " + message2);
                Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("AllianceChat: " + message2));
                event.setCancelled(true);
                break;
            case TRUCE:
                Faction myFaction3 = me.getFaction();

                String message3 = String.format(Conf.truceChatFormat, ChatColor.stripColor(me.getNameAndTag()), msg);

                //Send message to our own faction
                myFaction3.sendMessage(message3);

                //Send to all our truces
                for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers())
                    if (myFaction3.getRelationTo(fplayer) == Relation.TRUCE)
                        fplayer.sendMessage(message3);
                    else if (fplayer.isSpyingChat() && fplayer != me)
                        fplayer.sendMessage("[TCspy]: " + message3);
                Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("TruceChat: " + message3));
                event.setCancelled(true);
                break;
        }
    }

    private void doWarmup(final String warp, final FPlayer fme) {
        WarmUpUtil.process(fme, WarmUpUtil.Warmup.WARP, TL.WARMUPS_NOTIFY_TELEPORT, warp, () -> {
            Player player = Bukkit.getPlayer(fme.getPlayer().getUniqueId());
            if (player != null) {
                player.teleport(fme.getFaction().getWarp(warp).getLocation());
                fme.msg(TL.COMMAND_FWARP_WARPED, warp);
            }
        }, FactionsPlugin.getInstance().getConfig().getLong("warmups.f-warp", 10));
    }

    // this is for handling insertion of the player's faction tag, set at highest priority to give other plugins a chance to modify chat first
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Are we to insert the Faction tag into the format?
        // If we are not to insert it - we are done.
        if (!Conf.chatTagEnabled || Conf.chatTagHandledByAnotherPlugin)
            return;

        Player talkingPlayer = event.getPlayer();
        String msg = event.getMessage();
        String eventFormat = event.getFormat();
        FPlayer me = FPlayers.getInstance().getByPlayer(talkingPlayer);
        int InsertIndex;

        if (!Conf.chatTagReplaceString.isEmpty() && eventFormat.contains(Conf.chatTagReplaceString)) {
            // we're using the "replace" method of inserting the faction tags
            if (eventFormat.contains("[FACTION_TITLE]"))
                eventFormat = eventFormat.replace("[FACTION_TITLE]", me.getTitle());

            InsertIndex = eventFormat.indexOf(Conf.chatTagReplaceString);
            eventFormat = eventFormat.replace(Conf.chatTagReplaceString, "");
            Conf.chatTagPadAfter = false;
            Conf.chatTagPadBefore = false;
        } else if (!Conf.chatTagInsertAfterString.isEmpty() && eventFormat.contains(Conf.chatTagInsertAfterString))
            // we're using the "insert after string" method
            InsertIndex = eventFormat.indexOf(Conf.chatTagInsertAfterString) + Conf.chatTagInsertAfterString.length();
        else if (!Conf.chatTagInsertBeforeString.isEmpty() && eventFormat.contains(Conf.chatTagInsertBeforeString))
            // we're using the "insert before string" method
            InsertIndex = eventFormat.indexOf(Conf.chatTagInsertBeforeString);
        else {
            // we'll fall back to using the index place method
            InsertIndex = Conf.chatTagInsertIndex;
            if (InsertIndex > eventFormat.length())
                return;
        }

        String formatStart = eventFormat.substring(0, InsertIndex) + ((Conf.chatTagPadBefore && !me.getChatTag().isEmpty()) ? " " : "");
        String formatEnd = ((Conf.chatTagPadAfter && !me.getChatTag().isEmpty()) ? " " : "") + eventFormat.substring(InsertIndex);

        String nonColoredMsgFormat = formatStart + me.getChatTag().trim() + formatEnd;

        // Relation Colored?
        if (Conf.chatTagRelationColored) {
            for (Player listeningPlayer : event.getRecipients()) {
                FPlayer you = FPlayers.getInstance().getByPlayer(listeningPlayer);
                String yourFormat = formatStart + me.getChatTag(you).trim() + formatEnd;
                try {
                    listeningPlayer.sendMessage(String.format(yourFormat, talkingPlayer.getDisplayName(), msg));
                } catch (UnknownFormatConversionException ex) {
                    Conf.chatTagInsertIndex = 0;
                    FactionsPlugin.getInstance().log(Level.SEVERE, "Critical error in chat message formatting!");
                    FactionsPlugin.getInstance().log(Level.SEVERE, "NOTE: This has been automatically fixed right now by setting chatTagInsertIndex to 0.");
                    FactionsPlugin.getInstance().log(Level.SEVERE, "For a more proper fix, please read this regarding chat configuration: http://massivecraft.com/plugins/factions/config#Chat_configuration");
                    return;
                }
            }
            event.getRecipients().clear();
        }
        event.setFormat(nonColoredMsgFormat);
    }

}