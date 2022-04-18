package org.dhwpcs.infinitum.chat.tag.arguments;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.dhwpcs.infinitum.chat.ChatContext;
import org.dhwpcs.infinitum.chat.tag.TagInstance;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;
import org.dhwpcs.infinitum.chat.tag.parse.argument.TagContext;

import java.util.UUID;

public class ArgumentPlayer implements Argument<OfflinePlayer> {

    public static ArgumentPlayer player() {
        return new ArgumentPlayer();
    }

    public static OfflinePlayer getPlayer(String id, TagContext tag) throws TagFailedException {
        return tag.getArgument(id, OfflinePlayer.class);
    }

    @Override
    public Class<OfflinePlayer> getValueClass() {
        return OfflinePlayer.class;
    }

    @Override
    public OfflinePlayer parse(Object input, ChatContext ctx) {
        if(input instanceof TagInstance<?> instance) {
            if(!OfflinePlayer.class.isAssignableFrom(instance.tag().type())) {
                return null;
            } else {
                return (OfflinePlayer) instance.result();
            }
        } else if(input instanceof String str) {
            UUID id;
            try {
                id = UUID.fromString(str);
            } catch (IllegalArgumentException e) {
                id = Bukkit.getPlayerUniqueId(str);
            }
            if(id == null) {
                return null;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(str));
            if(!player.hasPlayedBefore()) {
                return null;
            }
            return player;
        }
        return null;
    }
}
