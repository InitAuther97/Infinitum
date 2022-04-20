package io.github.initauther97.nugget.adventure.util;

import net.kyori.adventure.text.*;

public class ComponentUtils {
    public static String extractContent(TextComponent component) {
        StringBuilder sb = new StringBuilder();
        sb.append(component.content());
        for(Component child : component.children()) {
            if(child instanceof TextComponent text) {
                sb.append(extractContent(text));
            } else sb.append(componentInfo(child));
        }
        return sb.toString();
    }

    public static String componentInfo(Component component) {
        if(component instanceof TextComponent text) {
            return String.format("Text{content=%s}", text.content());
        } else if(component instanceof TranslatableComponent translatable) {
            return String.format("Translatable{key=%s,args=%s}", translatable.key(), translatable.args());
        } else if(component instanceof ScoreComponent score) {
            return String.format("Score{name=%s,objective=%s}", score.name(), score.objective());
        } else if(component instanceof StorageNBTComponent storage) {
            return String.format("StorageNBT{key=%s,nbt=%s}", storage.storage(), storage.nbtPath());
        } else if(component instanceof SelectorComponent selector) {
            return String.format("Selector{pattern=%s}", selector.pattern());
        } else if(component instanceof BlockNBTComponent block) {
            return String.format("BlockNBT{pos=%s,nbt=%s}", block.pos(), block.nbtPath());
        } else if(component instanceof EntityNBTComponent entity) {
            return String.format("EntityNBT{selector=%s,nbt=%s}", entity.selector(), entity.nbtPath());
        } else return component.toString();
    }
}
