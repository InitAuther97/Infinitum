package org.dhwpcs.infinitum.chat;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.util.Tuple;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.chat.adventure.Translatable;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;
import org.dhwpcs.infinitum.Infinitum;

import java.util.*;

public class ChatRendererInf implements ChatRenderer {

    private final Infinitum infinitum;
    private final ChatGlobal global;
    private final List<Runnable> afterActions = new LinkedList<>();
    private final Multimap<String, Tuple<UUID, Translatable>> cache = MultimapBuilder.hashKeys().hashSetValues().build();

    public ChatRendererInf(Infinitum infinitum, ChatGlobal global) {
        this.infinitum = infinitum;
        this.global = global;
    }

    @Override
    public Component render(Player source, Component nameOfSource, Component message, Audience receiver) {
        Component name = nameOfSource
                .clickEvent(ClickEvent.copyToClipboard(String.format("{@:%s}", nameOfSource)));
        message.clickEvent(ClickEvent.copyToClipboard(String.format("{reply:%s}", nameOfSource)));
        System.out.println(message);
        if(message instanceof TextComponent component) {
            ChatContext ctx = new ChatContext(source, component, infinitum, global);
            TextComponent parsed;
            if(component.content().startsWith(global.getWholeEscape())){
                parsed = component.content(component.content().substring(2));
            } else {
                Translatable tr;
                try {
                    tr = global.getParser().parse(ctx);
                    cache.put(component.content(), new Tuple<>(source.getUniqueId(), tr));
                } catch (TagFailedException e) {
                    e.printStackTrace();
                    if(receiver instanceof CommandSender p) {
                        return Component.translatable("chat.type.text", name, infinitum.getI18n().format("chat.", p, message));
                    } else return Component.translatable("chat.type.text", name, infinitum.getI18n().format("chat", SupportedLang.EN_US, message));
                }

                parsed = tr.translate(receiver instanceof CommandSender ? infinitum.getI18n().getLanguage((CommandSender) receiver) : SupportedLang.EN_US);
                afterActions.addAll(ctx.afterTasks);
            }
            global.getHistory().appendMessage(new Message.Builder()
                    .uid(source.getUniqueId())
                    .timestamp(new Date().getTime())
                    .tick(Bukkit.getCurrentTick())
                    .message(component)
            );
            return Component.translatable("chat.type.text", name, parsed);
        } else return message;
    }

    public Runnable afterTask() {
        return () -> {
            afterActions.forEach(Runnable::run);
            afterActions.clear();
        };
    }
}
