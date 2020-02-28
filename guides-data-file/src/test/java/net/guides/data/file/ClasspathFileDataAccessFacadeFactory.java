package net.guides.data.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ClasspathFileDataAccessFacadeFactory extends FileDataAccessFacadeFactory {
    public ClasspathFileDataAccessFacadeFactory(Properties properties) {
        super(properties);
    }

    @Override
    BufferedReader getReader(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(path)));
    }
}
