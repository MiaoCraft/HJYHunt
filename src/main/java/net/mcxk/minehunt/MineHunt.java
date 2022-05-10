package net.mcxk.minehunt;

import lombok.Getter;
import net.mcxk.minehunt.commands.MineHuntCommand;
import net.mcxk.minehunt.game.ConstantCommand;
import net.mcxk.minehunt.game.Game;
import net.mcxk.minehunt.listener.*;
import net.mcxk.minehunt.placeholder.Placeholder;
import net.mcxk.minehunt.watcher.CountDownWatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MineHunt extends JavaPlugin {
    @Getter
    private static MineHunt instance;
    @Getter
    private Game game;

    @Getter
    private CountDownWatcher countDownWatcher;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        instance = this;
        game = new Game();
        countDownWatcher = new CountDownWatcher();
        final PluginCommand minehuntCommand = this.getCommand(ConstantCommand.MINE_HUNT);
        if (Objects.nonNull(minehuntCommand)) {
            minehuntCommand.setExecutor(new MineHuntCommand());
        }
        Plugin pluginPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (pluginPlaceholderApi != null) {
            System.out.println("检测到PlaceHolderAPI插件，变量功能已启用！");
            new Placeholder(this).register();
        }
        Plugin pluginAdvancedReplay = Bukkit.getPluginManager().getPlugin("AdvancedReplay");
        if (pluginAdvancedReplay != null) {
            System.out.println("检测到AdvancedReplay插件，回放功能已启用！");
        }
        game.switchWorldRuleForReady(false);
        Bukkit.getPluginManager().registerEvents(new PlayerServerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCompassListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProgressDetectingListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameWinnerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
