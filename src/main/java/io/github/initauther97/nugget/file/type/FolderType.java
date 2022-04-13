package io.github.initauther97.nugget.file.type;

import io.github.initauther97.nugget.file.FileManager;
import io.github.initauther97.nugget.file.VisitOperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public abstract class FolderType<T> implements FileType<T> {

    public static final FolderType<Path> IDENTITY = new FolderType<>() {
        @Override
        public Path onVisit(FileManager manager, Path pth) {
            return pth;
        }
    };

    public static final FolderType<FileManager> MANAGER = new FolderType<>() {
        @Override
        public FileManager onVisit(FileManager manager, Path pth) {
            return new FileManager(manager, pth, true);
        }
    };

    public static final FolderType<Stream<Path>> WALK = new FolderType<>() {
        @Override
        public Stream<Path> onVisit(FileManager manager, Path pth) throws IOException {
            return Files.walk(pth);
        }
    };

    @Override
    public VisitOperation createIfNotExist(FileManager manager, Path pth) throws IOException {
        Files.createDirectory(pth);
        Files.createFile(pth);
        return VisitOperation.CONTINUE;
    }


    @Override
    public boolean isType(FileManager manager, Path pth) throws IOException {
        return Files.isDirectory(pth);
    }
}
