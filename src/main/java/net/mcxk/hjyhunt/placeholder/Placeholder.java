package net.mcxk.hjyhunt.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.mcxk.hjyhunt.HJYHunt;
import net.mcxk.hjyhunt.game.PlayerRole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Placeholder extends PlaceholderExpansion {

    private final HJYHunt plugin;

    public Placeholder(HJYHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "Minehunt";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        Optional<net.mcxk.hjyhunt.game.PlayerRole> role = HJYHunt.getInstance().getGame().getPlayerRole(player);
        if ("rule".equals(identifier) && role.isPresent()) {
            if (role.get() == net.mcxk.hjyhunt.game.PlayerRole.HUNTER) {
                return (ChatColor.RED + plugin.getConfig().getString("HunterName"));
            }
            if (role.get() == net.mcxk.hjyhunt.game.PlayerRole.RUNNER) {
                return (ChatColor.GREEN + plugin.getConfig().getString("RunnerName"));
            }
            if (role.get() == PlayerRole.WAITING) {
                return (ChatColor.GRAY + plugin.getConfig().getString("WaitingName"));
            }
            return (ChatColor.GRAY + plugin.getConfig().getString("ObserverName"));
        }
        return null;
    }
}



