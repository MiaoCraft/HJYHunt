package net.mcxk.hjyhunt.util;

import net.mcxk.hjyhunt.HJYHunt;
import net.mcxk.hjyhunt.game.Game;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/12
 * @apiNote
 */
public class GameEnd {
    static Game game = new Game();

    private GameEnd() {
    }

    public static void startEnd() {
        if (!game.isAutoKick()) {
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.isEmpty() && player.isOnline()) {
                // 主动踢出玩家
                player.kickPlayer("游戏结束，后台正在重置地图，预计需要60秒！");
            }
        });

        AtomicReference<String> seed = new AtomicReference<>("");
        final String serverPath = System.getProperty("user.dir");
        File propertiesFile = new File(serverPath + "/server.properties");
        Properties server = new Properties();
        switch (HJYHunt.seedFrom) {
            case 0:
                Bukkit.shutdown();
                return;
            case 1:
                HJYHunt.getInstance().getLogger().info("开始读取种子...");
                final File file = new File(serverPath + "/plugins/HJYHunt/seeds.txt");
                try (final InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(propertiesFile.toPath()), StandardCharsets.UTF_8);
                     final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(propertiesFile.toPath()), StandardCharsets.UTF_8);
                     final BufferedReader seedReader = new BufferedReader(new FileReader(file))
                ) {
                    int seedNum = HJYHunt.config.getInt("LevelSeedNum");
                    if (!file.exists()) {
                        Bukkit.shutdown();
                        return;
                    }
                    for (int i = 0; i <= seedNum; i++) {
                        if (i == seedNum) {
                            seed.set(seedReader.readLine());
                        } else {
                            seedReader.readLine();
                        }
                    }
                    if (StringUtils.isEmpty(seed.get())) {
                        HJYHunt.getInstance().getLogger().info("种子行数配置错误！将使用随机种子！");
                        Bukkit.shutdown();
                        return;
                    }
                    HJYHunt.config.set("LevelSeedNum", seedNum + 1);
                    HJYHunt.config.save(serverPath + "/plugins/" + HJYHunt.getInstance().getDescription().getName() + "/config.yml");
                    server.load(inputStreamReader);
                    HJYHunt.getInstance().getLogger().log(Level.SEVERE, "读取到新的种子：{}", seed);
                    server.setProperty("level-seed", seed.get());
                    server.store(outputStreamWriter, "propeties,write:level-seed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bukkit.shutdown();
                return;
            default:
                HJYHunt.getInstance().getLogger().info("种子获取方式配置错误！将使用随机种子！");
                Bukkit.shutdown();
        }
    }
}
