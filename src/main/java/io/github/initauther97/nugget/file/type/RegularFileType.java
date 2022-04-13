package io.github.initauther97.nugget.file.type;

import io.github.initauther97.nugget.file.AccessPermission;
import io.github.initauther97.nugget.file.FileManager;
import io.github.initauther97.nugget.file.VisitOperation;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class RegularFileType<T> implements FileType<T> {

    public static final RegularFileType<InputStream> INPUT_STREAM = new RegularFileType<>() {
        @Override
        public InputStream onVisit(FileManager manager, Path pth) throws IOException {
            return Files.newInputStream(pth);
        }
    };

    public static final RegularFileType<BufferedReader> INPUT_READER = new RegularFileType<>() {
        @Override
        public BufferedReader onVisit(FileManager manager, Path pth) throws IOException {
            return Files.newBufferedReader(pth);
        }
    };

    public static final RegularFileType<OutputStream> OUTPUT_STREAM = new RegularFileType<>() {
        @Override
        public OutputStream onVisit(FileManager manager, Path pth) throws IOException {
            return Files.newOutputStream(pth);
        }
    };

    public static final RegularFileType<BufferedWriter> OUTPUT_WRITER = new RegularFileType<>() {
        @Override
        public BufferedWriter onVisit(FileManager manager, Path pth) throws IOException {
            return Files.newBufferedWriter(pth);
        }
    };

    public static final RegularFileType<FileChannel> FILE_CHANNEL = new RegularFileType<>() {
        @Override
        public boolean canVisit(FileManager manager, Path pth) {
            return manager.checkPermission(pth) != AccessPermission.DENIED;
        }

        @Override
        public FileChannel onVisit(FileManager manager, Path pth) throws IOException {
            return FileChannel.open(pth);
        }
    };

    public static final RegularFileType<SeekableByteChannel> BYTE_CHANNEL = new RegularFileType<>() {
        @Override
        public SeekableByteChannel onVisit(FileManager manager, Path pth) throws IOException {
            return Files.newByteChannel(pth);
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
        return Files.isRegularFile(pth);
    }
}
