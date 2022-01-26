package org.dhwpcs.infinitum.test;

import io.github.initauther97.adventure.AdventureWrapped;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Map;

public class AdventureTest {
    public static void main(String[] args) throws URISyntaxException, IOException {
        URI theTarget = AdventureTest.class.getClassLoader()
                .getResource("assets/infpaper/text").toURI();
        FileSystem fs = FileSystems.newFileSystem(theTarget, Map.of());
        AdventureWrapped wrapper = new AdventureWrapped(fs.getPath("assets", "infpaper", "text"));
        System.out.println(wrapper.i18n);
    }
}
