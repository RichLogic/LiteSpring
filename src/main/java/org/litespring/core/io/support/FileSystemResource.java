package org.litespring.core.io.support;

import org.litespring.core.io.Resource;
import org.litespring.util.Assert;

import java.io.*;

public class FileSystemResource implements Resource {

    private String path;
    private File file;

    public FileSystemResource(String path) {
        Assert.notNull(path, "路径不能为空");
        this.path = path;
        this.file = new File(path);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public String getDescription() {
        return path;
    }

}
