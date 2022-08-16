package net.mcxk.minihunt;

import lombok.Getter;
import net.mcxk.minihunt.commands.MiniHuntCommand;
import net.mcxk.minihunt.commands.TabComplete;
import net.mcxk.minihunt.game.ConstantCommand;
import net.mcxk.minihunt.game.Game;
import net.mcxk.minihunt.listener.ChatListener;
import net.mcxk.minihunt.watcher.CountDownWatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MiniHunt extends JavaPlugin {
    public static String messageHead;
    public static String pluginName;
    public static String pluginVersion;
    public static byte seedFrom;
    public static FileConfiguration config;
    @Getter
    private static MiniHunt instance;
    @Getter
    private Game game;
    @Getter
    private CountDownWatcher countDownWatcher;

    @Override
    public void onLoad() {
        instance = this;
        pluginName = instance.getName();
        pluginVersion = instance.getDescription().getVersion();
        messageHead = String.format("[%s%s%s] ", ChatColor.AQUA, pluginName, ChatColor.WHITE);
        seedFrom = (byte) instance.getConfig().getInt("seedFrom");
        config = getConfig();
    }


    @Override
    public void onEnable() {
        // 插件启动逻辑
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        instance = this;
        game = new Game();
        countDownWatcher = new CountDownWatcher();
        final PluginCommand MiniHuntCommand = this.getCommand(ConstantCommand.PLUGIN_NAME);
        if (Objects.nonNull(MiniHuntCommand)) {
            MiniHuntCommand.setExecutor(new MiniHuntCommand());
            MiniHuntCommand.setTabCompleter(new TabComplete());
        }
        Plugin pluginAdvancedReplay = Bukkit.getPluginManager().getPlugin("AdvancedReplay");
        if (pluginAdvancedReplay != null) {
            getLogger().info("检测到AdvancedReplay插件，回放功能已启用！");
        }
        game.switchWorldRuleForReady(false);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.minihunt.listener.PlayerServerListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.minihunt.listener.PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.minihunt.listener.PlayerCompassListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.minihunt.listener.ProgressDetectingListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.minihunt.listener.GameWinnerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // 插件关闭逻辑

    }
}