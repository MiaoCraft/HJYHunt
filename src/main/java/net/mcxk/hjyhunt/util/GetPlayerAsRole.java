package net.mcxk.hjyhunt.util;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.mcxk.hjyhunt.game.PlayerRole;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Crsuh2er0
 * &#064;date 2022/8/12
 * @apiNote
 */
public class GetPlayerAsRole {
    @Getter
    @Setter
    private static Map<Player, PlayerRole> roleMapping = Maps.newConcurrentMap();

    private GetPlayerAsRole() {
    }

    public static List<Player> getPlayersAsRole(PlayerRole role) {
        return roleMapping.entrySet().stream().filter(playerPlayerRoleEntry -> playerPlayerRoleEntry.getValue() == role).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
