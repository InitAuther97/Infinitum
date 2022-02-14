package org.dhwpcs.infinitum.chat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.papermc.paper.chat.ChatRenderer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.entity.Player;

import org.dhwpcs.infinitum.chat.brigadier.ChatSource;
import org.dhwpcs.infinitum.chat.brigadier.FeatureDispatcher;
import org.dhwpcs.infinitum.chat.data.MsgHistory;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRendererInf implements ChatRenderer {

    private MsgHistory hist;
    private final Pattern MARK_CHAT_FEATURE = Pattern.compile("\\\\{0}\\$\\{(.*)\\}");
    private final Pattern AT_ALIAS = Pattern.compile("\\\\{0}@(.*)\b");
    private final String WHOLE_ESCAPE_MARK = "!#";
    private final List<Runnable> afterActions = new LinkedList<>();

    public ChatRendererInf(MsgHistory history) {
        this.hist = history;
    }

    @Override
    public Component render(Player source, Component nameOfSource, Component message, Audience receiver) {
        TextComponent name = TextComponent.ofChildren(nameOfSource)
                .clickEvent(ClickEvent.copyToClipboard(String.format("#{@:%s}", nameOfSource)));
        message.clickEvent(ClickEvent.copyToClipboard(String.format("#{reply:%s}", nameOfSource)));
        if(message instanceof TextComponent component) {
            return parse(source, component);
        } else return message;
    }

    Component parse(Player source, TextComponent component) {
        String content = component.content();
        if(content.startsWith(WHOLE_ESCAPE_MARK)) {
            return Component.text(content.substring(2)).style(component.style());
        }
        Matcher matcher = MARK_CHAT_FEATURE.matcher(content);
        MatchResult[] results = matcher.results().toArray(MatchResult[]::new);
        ChatSource src = new ChatSource(VanillaCommandWrapper.getListener(source), results, hist, afterActions);
        for(int i = 0; i < results.length; i++) {
            try {
                FeatureDispatcher.INSTANCE.execute(results[i].group(), src);
                FeatureDispatcher.move(src);
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
        FeatureDispatcher.flip(src);
        Component[] allComponents = new Component[2 * results.length + 1];
        allComponents[0] = Component.text(content.substring(0, results[0].start()));
        for(int i = 0; i + 1 < results.length; i++) {
            allComponents[i*2] = Component.text(content.substring(results[i].end(), results[i+1].start()));
        }
        for(int i = 0; i < results.length; i++) {
            allComponents[i*2+1] = src.getReplacement();
            FeatureDispatcher.move(src);
        }
        return TextComponent.ofChildren(allComponents).style(component.style());
    }

    public Runnable afterTask() {
        return () -> {
            afterActions.forEach(Runnable::run);
            afterActions.clear();
        };
    }
}
