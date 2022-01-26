package org.dhwpcs.infinitum.chat.feature.inst;

import io.github.initauther97.arguments.ArgumentList;
import net.kyori.adventure.text.Component;
import org.dhwpcs.infinitum.chat.feature.ChatFeature;

import java.util.List;

public class FeatureReply implements ChatFeature {
    @Override
    public String name() {
        return "Reply";
    }

    @Override
    public String id() {
        return "reply";
    }

    @Override
    public List<String> aliases() {
        return List.of("rep");
    }

    @Override
    public ArgumentList getArguments() {
        return new ArgumentList();
    }

    @Override
    public Component compile(Object[] args) {
        return null;
    }
}
