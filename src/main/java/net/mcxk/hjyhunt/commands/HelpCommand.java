package net.mcxk.hjyhunt.commands;

import net.mcxk.hjyhunt.util.SendMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/11
 * @apiNote
 */
public class HelpCommand {
    private HelpCommand() {
    }

    public static void help(CommandSender sender) {
        SendMessage.sendMessage(String.format("%s命令列表", ChatColor.DARK_GREEN), sender);
        sender.sendMessage(String.format("/hjyhunt about %s查看插件信息", ChatColor.GREEN));
        sender.sendMessage(String.format("/hjyhunt want <runner/helper/waiting> %s选择角色", ChatColor.GREEN));
        sender.sendMessage(String.format("/hjyhunt players %s查看玩家列表", ChatColor.GREEN));
        sender.sendMessage(String.format("/hjyhunt help %s查看此列表", ChatColor.GREEN));
        sender.sendMessage(String.format("/hjyhunt <runner/hunter> %s以选定身份强制加入游戏", ChatColor.GREEN));
        sender.sendMessage(String.format("/hjyhunt rstcd %s重置倒计时", ChatColor.GREEN));
        sender.sendMessage(String.format("/hjyhunt start %s强制开始游戏", ChatColor.GREEN));
    }
}
