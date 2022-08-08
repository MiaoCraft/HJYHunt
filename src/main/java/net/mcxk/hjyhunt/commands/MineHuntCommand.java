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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

/**
 * @author LingMuQingYu
 * @since 2022/5/8 22:22
 */
public class MineHuntCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }
        final HJYHunt HJYHunt = net.mcxk.hjyhunt.HJYHunt.getInstance();
        final Game game = HJYHunt.getGame();

        // 禁止删除本行版权声明
        // 墨守吐槽：如果有人想在我这搞分支就顺着往下写就好了~
        if (net.mcxk.hjyhunt.game.ConstantCommand.COPYRIGHT.equalsIgnoreCase(args[0])) {
            sender.sendMessage("Copyright - Minecraft of gamerteam. 版权所有.");
            sender.sendMessage("Fork by MossCG 这是墨守的分支版本~");
            sender.sendMessage("Fork by LingMuQingYu 这是凌慕轻语的分支版本~");
            sender.sendMessage("Fork by Crsuh2er0 这是泠辰的分支版本~");
            return true;
        }
        // 只有在游戏未开始时才会去处理是否准备
        if (net.mcxk.hjyhunt.game.GameStatus.WAITING_PLAYERS.equals(game.getStatus())
                && net.mcxk.hjyhunt.game.ConstantCommand.WANT.equalsIgnoreCase(args[0]) && args.length == 2) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            final String arg = args[1];
            final net.mcxk.hjyhunt.game.PlayerRole playerRole = game.getIntentionRoleMapping().get(player);
            // 判断由观战转为游戏玩家时，如果游戏人数已经达到最大游戏人数，不能变更游戏模式
            if (net.mcxk.hjyhunt.game.PlayerRole.WAITING.equals(playerRole) &&
                    !net.mcxk.hjyhunt.game.ConstantCommand.WAITING.equals(args[1]) &&
                    game.getInGamePlayers().size() >= game.getMaxPlayers()) {
                player.sendMessage("当前游戏已满人，只能选择观战队列！");
                return true;
            }
            switch (arg) {
                case net.mcxk.hjyhunt.game.ConstantCommand.HUNTER:
                    game.getIntentionRoleMapping().put(player, net.mcxk.hjyhunt.game.PlayerRole.HUNTER);
                    game.getPlayerPrepare().put(player, false);
                    game.getInGamePlayers().add(player);
                    player.sendMessage("您选择了猎人！");
                    break;
                case net.mcxk.hjyhunt.game.ConstantCommand.RUNNER:
                    game.getIntentionRoleMapping().put(player, net.mcxk.hjyhunt.game.PlayerRole.RUNNER);
                    game.getPlayerPrepare().put(player, false);
                    game.getInGamePlayers().add(player);
                    player.sendMessage("您选择了逃亡者，当意愿逃亡者人数过多时，将从中随机抽取部分逃亡者！");
                    break;
                case net.mcxk.hjyhunt.game.ConstantCommand.WAITING:
                    game.getIntentionRoleMapping().put(player, net.mcxk.hjyhunt.game.PlayerRole.WAITING);
                    game.getPlayerPrepare().remove(player);
                    game.getInGamePlayers().remove(player);
                    player.sendMessage("您选择了观战，您将不会参与到游戏中！");
                    break;
                default:
                    return false;
            }
            return true;
        }
        // 只有在游戏未开始时才会去处理是否准备
        if (net.mcxk.hjyhunt.game.GameStatus.WAITING_PLAYERS.equals(game.getStatus())
                && net.mcxk.hjyhunt.game.ConstantCommand.PREPARE.equalsIgnoreCase(args[0]) && args.length == 2) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            // 观战模式玩家不能准备游戏
            final net.mcxk.hjyhunt.game.PlayerRole playerRole = game.getIntentionRoleMapping().get(player);
            if (net.mcxk.hjyhunt.game.PlayerRole.WAITING.equals(playerRole)) {
                return true;
            }
            switch (args[1]) {
                case net.mcxk.hjyhunt.game.ConstantCommand.TRUE:
                    // 玩家准备就绪
                    game.getPlayerPrepare().put(player, true);
                    player.sendMessage("已准备就绪！");
                    player.setGlowing(false);
                    break;
                case net.mcxk.hjyhunt.game.ConstantCommand.FALSE:
                    // 玩家取准备
                    game.getPlayerPrepare().put(player, false);
                    player.sendMessage("已取消准备！");
                    player.setGlowing(true);
                    break;
                default:
                    return false;
            }
            return true;
        }


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
            HJYHunt.getCountDownWatcher().resetCountdown();
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
