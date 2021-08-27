package com.massivecraft.factions.struct;

import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.command.CommandSender;

public enum Permission {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */


    MANAGE_SAFE_ZONE("managesafezone"),
    MANAGE_WAR_ZONE("managewarzone"),
    OWNERSHIP_BYPASS("ownershipbypass"),
    ADMIN("admin"),
    ALTS("alts"),
    ADMIN_ANY("admin.any"),
    AHOME("ahome"),
    ANNOUNCE("announce"),
    AUDIT("audit"),
    AUTOCLAIM("autoclaim"),
    AUTO_LEAVE_BYPASS("autoleavebypass"),
    BAN("ban"),
    BOOSTER_ADD("booster.add"),
    BOOSTER_REMOVE("booster.remove"),
    BOOSTER_GUI("boosters"),
    BYPASS("bypass"),
    CHAT("chat"),
    CHATSPY("chatspy"),
    CHECK("check"),
    CLAIM("claim"),
    CLAIMAT("claimat"),
    CLAIM_FILL("claimfill"),
    CLAIM_LINE("claim.line"),
    CLAIM_RADIUS("claim.radius"),
    CONFIG("config"),
    CONVERT("convert"),
    CONVERTCONFIG("convertconfig"),
    CREATE("create"),
    CORNER("corner"),
    DEBUG("debug"),
    DEFAULTRANK("defaultrank"),
    DEINVITE("deinvite"),
    DELHOME("delhome"),
    DESCRIPTION("description"),
    DISBAND("disband"),
    DISBAND_ANY("disband.any"),
    DRAIN("drain"),
    FLY_FLY("fly"),
    FLY_WILDERNESS("fly.wilderness"),
    FLY_SAFEZONE("fly.safezone"),
    FLY_WARZONE("fly.warzone"),
    FLY_ENEMY("fly.enemy"),
    FLY_ALLY("fly.ally"),
    FLY_TRUCE("fly.truce"),
    FLY_NEUTRAL("fly.neutral"),
    FOCUS("focus"),
    FRIENDLYFIRE("friendlyfire"),
    GLOBALCHAT("globalchat"),
    GRACE("grace"),
    GRACETOGGLE("gracetoggle"),
    HELP("help"),
    HOME("home"),
    INVITE("invite"),
    INVSEE("invsee"),
    JOIN("join"),
    JOIN_ANY("join.any"),
    JOIN_OTHERS("join.others"),
    KICK("kick"),
    KICK_ANY("kick.any"),
    LEAVE("leave"),
    LIST("list"),
    LOCK("lock"),
    LOCKSPAWNERS("lockspawners"),
    LOGOUT("logout"),
    LOOKUP("lookup"),
    MAP("map"),
    MAPHEIGHT("mapheight"),
    MOD("mod"),
    NOTIFICATIONS("notifications"),
    COLEADER("coleader"),
    MOD_ANY("mod.any"),
    COLEADER_ANY("coleader.any"),
    MISSIONS("missions"),
    MODIFY_POWER("modifypower"),
    MONEY_BALANCE("money.balance"),
    MONEY_BALANCE_ANY("money.balance.any"),
    MONEY_DEPOSIT("money.deposit"),
    MONEY_WITHDRAW("money.withdraw"),
    MONEY_WITHDRAW_ANY("money.withdraw.any"),
    MONEY_F2F("money.f2f"),
    MONEY_F2P("money.f2p"),
    MONEY_P2F("money.p2f"),
    MONITOR_LOGINS("monitorlogins"),
    NEAR("near"),
    NO_BOOM("noboom"),
    OPEN("open"),
    OWNER("owner"),
    OWNERLIST("ownerlist"),
    RESERVE("reserve"),
    SET_TNT("settnt"),
    SET_GUILD("setguild"),
    SET_PEACEFUL("setpeaceful"),
    SET_PERMANENT("setpermanent"),
    SET_PERMANENTPOWER("setpermanentpower"),
    SHIELD("shield"),
    SHOW_INVITES("showinvites"),
    PAYPAL("paypal"),
    PAYPALSET("setpaypal"),
    PERMISSIONS("permissions"),
    POINTS("points"),
    POWERBOOST("powerboost"),
    POWER("power"),
    POWER_ANY("power.any"),
    PROMOTE("promote"),
    RELATION("relation"),
    RELOAD("reload"),
    ROSTER("roster"),
    SAVE("save"),
    SPAM("spam"),
    SETHOME("sethome"),
    SETHOME_ANY("sethome.any"),
    SETPOWER("setpower"),
    SETSTRIKES("setstrikes"),
    SHOW("show"),
    SPAWNER_CHUNKS("spawnerchunks"),
    STATUS("status"),
    STEALTH("stealth"),
    STUCK("stuck"),
    TAG("tag"),
    TOGGLE_TITLES("toggletitles"),
    TNT("tnt"),
    TITLE("title"),
    TITLE_COLOR("title.color"),
    TOGGLE_ALLIANCE_CHAT("togglealliancechat"),
    UNCLAIM("unclaim"),
    UNCLAIM_ALL("unclaimall"),
    VERSION("version"),
    SCOREBOARD("scoreboard"),
    SEECHUNK("seechunk"),
    SETWARP("setwarp"),
    SHOP("shop"),
    TOP("top"),
    VIEWCHEST("viewchest"),
    ADDPOINTS("addpoints"),
    REMOVEPOINTS("removepoints"),
    SETPOINTS("setpoints"),
    VAULT("vault"),
    GETVAULT("getvault"),
    SETMAXVAULTS("setmaxvaults"),
    RULES("rules"),
    CHECKPOINT("checkpoint"),
    UPGRADES("upgrades"),
    BANNER("banner"),
    TPBANNER("tpbanner"),
    KILLHOLOS("killholos"),
    INSPECT("inspect"),
    TNTFILL("tntfill"),
    COORD("coords"),
    SHOWCLAIMS("showclaims"),
    WARP("warp"),
    WILD("wild"),
    CHEST("chest");

    public final String node;

    Permission(final String node) {
        this.node = "factions." + node;
    }

    public boolean has(CommandSender sender, boolean informSenderIfNot) {
        return FactionsPlugin.getInstance().perm.has(sender, this.node, informSenderIfNot);
    }

    public boolean has(CommandSender sender) {
        return has(sender, false);
    }
}