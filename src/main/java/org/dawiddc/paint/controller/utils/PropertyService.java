package org.dawiddc.paint.controller.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyService {

    public Properties loadFromFile(String filePath) {
        InputStream input = null;
        Properties properties = new Properties();
        try {
            input = new FileInputStream(filePath);
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return properties;
    }

}
