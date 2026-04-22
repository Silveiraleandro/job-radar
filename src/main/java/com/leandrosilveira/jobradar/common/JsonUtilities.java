package com.leandrosilveira.jobradar.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtilities {

    public static String getFileAsString(String path) {
        try (InputStream inputStream = JsonUtilities.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new JobRadarException(String.format(Constants.Error.FILE_NOT_FOUND, path));
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new JobRadarException(
                    String.format(Constants.Error.COULD_NOT_READ_FILE, path), e);
        }
    }
}
