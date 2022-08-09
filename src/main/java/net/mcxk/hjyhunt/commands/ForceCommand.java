package net.mcxk.hjyhunt.commands;

import net.mcxk.hjyhunt.HJYHunt;
import net.mcxk.hjyhunt.game.ConstantCommand;
import net.mcxk.hjyhunt.game.Game;
import net.mcxk.hjyhunt.game.GameStatus;
import net.mcxk.hjyhunt.game.PlayerRole;
import net.mcxk.hjyhunt.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/9
 * @apiNote
 */
public class ForceCommand {
    public static boolean forceCommand(CommandSender sender, String[] args, Game game){
        if (!sender.hasPermission(net.mcxk.hjyhunt.game.ConstantCommand.MINE_HUNT_ADMIN)) {
            return false;
        }
        // 不安全命令 完全没做检查，确认你会用再执行
        // 墨守吐槽：挺安全的起码我没用出啥问题，有空我改改2333
        if ((net.mcxk.hjyhunt.game.ConstantCommand.HUNTER.equalsIgnoreCase(args[0]) || net.mcxk.hjyhunt.game.ConstantCommand.RUNNER.equalsIgnoreCase(args[0]))) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            game.getInGamePlayers().add(player);
            if (net.mcxk.hjyhunt.game.ConstantCommand.HUNTER.equalsIgnoreCase(args[0])) {
                game.getRoleMapping().put(player, net.mcxk.hjyhunt.game.PlayerRole.HUNTER);
            } else {
                game.getRoleMapping().put(player, net.mcxk.hjyhunt.game.PlayerRole.RUNNER);
            }
            player.setGameMode(GameMode.SURVIVAL);
            Bukkit.broadcastMessage("玩家 " + sender.getName() + " 强制加入了游戏！ 身份：" + args[0]);
            return true;
        }
        if (net.mcxk.hjyhunt.game.ConstantCommand.RESET_COUNTDOWN.equalsIgnoreCase(args[0]) && game.getStatus() == net.mcxk.hjyhunt.game.GameStatus.WAITING_PLAYERS) {
            HJYHunt.getInstance().getCountDownWatcher().resetCountdown();
            return true;
        }
        if (net.mcxk.hjyhunt.game.ConstantCommand.PLAYERS.equalsIgnoreCase(args[0]) && game.getStatus() == net.mcxk.hjyhunt.game.GameStatus.GAME_STARTED) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + ">猎人AND逃亡者<");
            Bukkit.broadcastMessage(ChatColor.RED + "猎人: " + net.mcxk.hjyhunt.util.Util.list2String(net.mcxk.hjyhunt.HJYHunt.getInstance().getGame().getPlayersAsRole(net.mcxk.hjyhunt.game.PlayerRole.HUNTER).stream().map(Player::getName).collect(Collectors.toList())));
            Bukkit.broadcastMessage(ChatColor.GREEN + "逃亡者: " + Util.list2String(net.mcxk.hjyhunt.HJYHunt.getInstance().getGame().getPlayersAsRole(PlayerRole.RUNNER).stream().map(Player::getName).collect(Collectors.toList())));
            return true;
        }
        if (ConstantCommand.FORCE_START.equalsIgnoreCase(args[0]) && game.getStatus() == GameStatus.WAITING_PLAYERS) {
            if (game.getInGamePlayers().size() < 2) {
                sender.sendMessage("错误：至少有2名玩家才可以强制开始游戏 1名玩家你玩个锤子");
                return true;
            } else {
                game.start();
            }
            return true;
        }
        return false;
    }
}
