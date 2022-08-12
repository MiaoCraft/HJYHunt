package net.mcxk.hjyhunt;

import lombok.Getter;
import net.mcxk.hjyhunt.commands.HJYHuntCommand;
import net.mcxk.hjyhunt.commands.TabComplete;
import net.mcxk.hjyhunt.game.ConstantCommand;
import net.mcxk.hjyhunt.game.Game;
import net.mcxk.hjyhunt.listener.ChatListener;
import net.mcxk.hjyhunt.watcher.CountDownWatcher;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;

public final class HJYHunt extends JavaPlugin {
    public static String messageHead;
    public static String pluginName;
    public static String pluginVersion;
    @Getter
    private static HJYHunt instance;
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
    }


    @Override
    public void onEnable() {
        // 插件启动逻辑
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        instance = this;
        game = new Game();
        countDownWatcher = new CountDownWatcher();
        final PluginCommand HJYHuntCommand = this.getCommand(ConstantCommand.HJY_HUNT);
        if (Objects.nonNull(HJYHuntCommand)) {
            HJYHuntCommand.setExecutor(new HJYHuntCommand());
            HJYHuntCommand.setTabCompleter(new TabComplete());
        }
        Plugin pluginAdvancedReplay = Bukkit.getPluginManager().getPlugin("AdvancedReplay");
        if (pluginAdvancedReplay != null) {
            getLogger().info("检测到AdvancedReplay插件，回放功能已启用！");
        }
        game.switchWorldRuleForReady(false);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.hjyhunt.listener.PlayerServerListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.hjyhunt.listener.PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.hjyhunt.listener.PlayerCompassListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.hjyhunt.listener.ProgressDetectingListener(), this);
        Bukkit.getPluginManager().registerEvents(new net.mcxk.hjyhunt.listener.GameWinnerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // 插件关闭逻辑
        final FileConfiguration config = getConfig();
        if (!config.getBoolean("LevelSeed")) {
            return;
        }
        final String serverPath = System.getProperty("user.dir");
        File propertiesFile = new File(serverPath + "/server.properties");
        final File file = new File(serverPath + "/plugins/HJYHunt/seeds.txt");
        try (final InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(propertiesFile.toPath()), StandardCharsets.UTF_8);
             final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(propertiesFile.toPath()), StandardCharsets.UTF_8);
             final BufferedReader seedReader = new BufferedReader(new FileReader(file))
        ) {
            int seedNum = config.getInt("LevelSeedNum");
            if (!file.exists()) {
                return;
            }
            getLogger().info(file.getAbsolutePath());
            String seed = "";
            for (int i = 0; i <= seedNum; i++) {
                if (i == seedNum) {
                    seed = seedReader.readLine();
                } else {
                    seedReader.readLine();
                }
            }
            if (StringUtils.isEmpty(seed)) {
                seed = "";
            }
            config.set("LevelSeedNum", seedNum + 1);
            config.save(serverPath + "/plugins/" + getInstance().getDescription().getName() + "/config.yml");
            Properties server = new Properties();
            server.load(inputStreamReader);
            getLogger().log(Level.SEVERE, "读取到新的种子：{}", seed);
            server.setProperty("level-seed", seed);
            server.store(outputStreamWriter, "propeties,write:level-seed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
