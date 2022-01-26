package org.dhwpcs.infinitum.arguments;

import io.github.initauther97.arguments.IArgument;
import io.github.initauther97.arguments.env.Environment;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.util.UUID;
import java.util.regex.Pattern;

public class ArgumentPlayer implements IArgument<OfflinePlayer> {

    public static final IArgument<OfflinePlayer> INSTANCE = new ArgumentPlayer();

    private final Pattern UID_PATTERN =
            Pattern.compile("^[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}$");

    @Override
    public Class<OfflinePlayer> getArgumentType() {
        return OfflinePlayer.class;
    }

    @Override
    public OfflinePlayer parse(String arg, Environment env) {
        Server server = env.get("bukkit").getAs(Server.class);
        OfflinePlayer[] players = server.getOfflinePlayers();
        if(UID_PATTERN.matcher(arg).matches()) {
            for(OfflinePlayer player : players) {
                if(player.hasPlayedBefore() && player.getUniqueId().equals(UUID.fromString(arg))) {
                    return player;
                }
            }
        } else if(env.has("player_fuzzy_matching") &&
                env.get("player_fuzzy_matching").getAs(boolean.class)){
            for(OfflinePlayer player : players) {
                if(player.hasPlayedBefore() && player.getName().contains(arg)) {
                    return player;
                }
            }
        }
        return null;
    }

    @Override
    public boolean checkCompatible(Environment env) {
        return env.has("bukkit");
    }
}
