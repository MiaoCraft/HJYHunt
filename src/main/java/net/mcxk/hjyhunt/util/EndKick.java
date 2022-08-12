package net.mcxk.hjyhunt.util;

import net.mcxk.hjyhunt.game.Game;
import org.bukkit.Bukkit;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/12
 * @apiNote
 */
public class EndKick {
    static Game game = new Game();

    private EndKick() {
    }

    public static void kickAllPlayer() {
        if (!game.isAutoKick()) {
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.isEmpty() && player.isOnline()) {
                // 主动踢出玩家
                player.kickPlayer("游戏结束，后台正在重置地图，预计需要30秒！");
            }
        });
    }
}
