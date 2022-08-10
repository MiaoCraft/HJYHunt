package net.mcxk.hjyhunt.commands;

import org.bukkit.command.CommandSender;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/10
 * @apiNote
 */
public class AboutCommand {
    public static boolean about(CommandSender sender, String[] args){
        // 禁止删除本行版权声明
        // 墨守吐槽：如果有人想在我这搞分支就顺着往下写就好了~
        if (net.mcxk.hjyhunt.game.ConstantCommand.ABOUT.equalsIgnoreCase(args[0])) {
            sender.sendMessage("§aHJYHunt §3v" + net.mcxk.hjyhunt.HJYHunt.getInstance().getDescription().getVersion());
            sender.sendMessage("Copyright - Minecraft of gamerteam. 版权所有.");
            sender.sendMessage("使用该插件应遵循§eAGPL-3.0§r协议");
            sender.sendMessage("Fork by MossCG 这是墨守的分支版本~");
            sender.sendMessage("Fork by LingMuQingYu 这是凌慕轻语的分支版本~");
            sender.sendMessage("Fork by §1C§2r§3s§4u§5h§62§7e§8r§90 §r这是§b泠§d辰§r的分支版本~");
            return true;
        }
        return false;
    }
}
