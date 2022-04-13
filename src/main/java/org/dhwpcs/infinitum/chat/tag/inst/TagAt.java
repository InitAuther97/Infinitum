package org.dhwpcs.infinitum.chat.tag.inst;

import net.kyori.adventure.sound.Sound;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.chat.adventure.Translatable;
import org.dhwpcs.infinitum.chat.tag.ChatContext;
import org.dhwpcs.infinitum.chat.tag.ChatTag;
import org.dhwpcs.infinitum.chat.tag.arguments.ArgumentPlayer;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;
import org.dhwpcs.infinitum.chat.tag.parse.argument.ArgumentTable;
import org.dhwpcs.infinitum.chat.tag.parse.argument.TagContext;

import java.util.List;

public class TagAt implements ChatTag<OfflinePlayer> {

    @Override
    public String name() {
        return "At";
    }

    @Override
    public String id() {
        return "at";
    }

    @Override
    public List<String> aliases() {
        return List.of("@");
    }

    @Override
    public ArgumentTable arguments() throws TagFailedException {
        return ArgumentTable.table()
                .with("id", ArgumentPlayer.player());
    }

    @Override
    public Class<OfflinePlayer> type() {
        return OfflinePlayer.class;
    }

    @Override
    public OfflinePlayer parse(TagContext tag, ChatContext chat) throws TagFailedException {
        return ArgumentPlayer.getPlayer("id", tag);
    }

    @Override
    public Translatable parseComponent(OfflinePlayer result, ChatContext ctx) {
        ctx.addAfterTask(() -> {
            if(result.isOnline()) {
                Player entity = result.getPlayer();
                entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 1.0f, 0.0f));
            }
        });
        return Translatable.wrapNugget(ctx.getInfinitum().getI18n().get("chat.at."+(result.isOnline() ? "online" : "offline")), result.getName());
    }
}
