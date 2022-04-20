package io.github.initauther97.nugget.adventure.test;

import io.github.initauther97.nugget.adventure.AdventureWrapper;
import io.github.initauther97.nugget.adventure.SupportedLang;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestAdventure {
    public static void main(String[] args) throws URISyntaxException {
        AdventureWrapper wrapper = new AdventureWrapper(Paths.get(TestAdventure.class.getResource("/assets/infpaper/text").toURI()));
        System.out.println(wrapper.get("command.vote.info"));
        System.out.println(wrapper.format("command.vote.info", SupportedLang.EN_US, "", "", "", ""));
    }
}
