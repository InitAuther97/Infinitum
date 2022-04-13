package io.github.initauther97.nugget.file.type.compressed;

import io.github.initauther97.nugget.file.FileManager;
import io.github.initauther97.nugget.file.type.FileType;
import io.github.initauther97.nugget.file.type.RegularFileType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class CompressedFileType extends RegularFileType<FileManager> {

    public static final FileType<FileManager> ZIP = new CompressedFileType() {

        public final int header = 0x504B0304;

        @Override
        public boolean isType(FileManager manager, Path pth) throws IOException {
            if(super.isType(manager, pth)) {
                InputStream is = Files.newInputStream(pth);
                return header == ((is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read());
            }
            return false;
        }

        @Override
        protected Path createFsRoot(Path pth) throws IOException {
            FileSystem fs = FileSystems.newFileSystem(pth);
            return fs.getPath("");
        }
    };

    @Override
    public FileManager onVisit(FileManager manager, Path pth) throws IOException {
        return new FileManager(manager, createFsRoot(pth), false);
    }

    protected abstract Path createFsRoot(Path pth) throws IOException;

}
