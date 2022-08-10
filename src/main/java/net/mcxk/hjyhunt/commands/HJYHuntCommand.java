package net.mcxk.hjyhunt.commands;

import net.mcxk.hjyhunt.HJYHunt;
import net.mcxk.hjyhunt.game.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author LingMuQingYu, Crsuh2er0
 * @since 2022/8/10 21:53
 */
public class HJYHuntCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }
        final HJYHunt HJYHunt = net.mcxk.hjyhunt.HJYHunt.getInstance();
        final Game game = HJYHunt.getGame();

        if (AboutCommand.about(sender, args)) {
            return true;
        }
        // 只有在游戏未开始时才会去处理是否准备
        if (net.mcxk.hjyhunt.game.GameStatus.WAITING_PLAYERS.equals(game.getStatus())
                && net.mcxk.hjyhunt.game.ConstantCommand.WANT.equalsIgnoreCase(args[0])
                && args.length == 2) {
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
                    // 玩家取消准备
                    game.getPlayerPrepare().put(player, false);
                    player.sendMessage("已取消准备！");
                    player.setGlowing(true);
                    break;
                default:
                    return false;
            }
            return true;
        }
        return ForceCommand.forceCommand(sender, args, game);
    }
}
