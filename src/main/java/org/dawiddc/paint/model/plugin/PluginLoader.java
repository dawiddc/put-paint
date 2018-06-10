package org.dawiddc.paint.model.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private static Class<?> loadClass(File pluginsDirectory, String configFileName) throws IOException, ClassNotFoundException {
        final JarFile jarFile = new JarFile(pluginsDirectory);
        final JarEntry jarEntry = jarFile.getJarEntry(configFileName);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarEntry)));
        final Map<String, String> data = new HashMap<>();

        String in;
        while ((in = bufferedReader.readLine()) != null) {
            if (in.isEmpty() || in.startsWith("#"))
                continue;
            final String[] split = in.split(" ");
            data.put(split[0], split[1]);
        }

        jarFile.close();

        return Class.forName(data.get("Main"), true, new URLClassLoader(new URL[]{pluginsDirectory.toURI().toURL()}));
    }

    public static Class<?>[] loadPlugins(String pluginsDirectory, String configFileName) throws IOException, ClassNotFoundException {
        return loadPlugins(new File(pluginsDirectory), configFileName);
    }

    private static Class<?>[] loadPlugins(File dir, String config) throws IOException, ClassNotFoundException {
        final File[] files = dir.listFiles();
        final Class<?>[] classes = new Class<?>[Objects.requireNonNull(files).length];
        for (int i = 0; i < files.length; i++) {
            classes[i] = loadClass(files[i], config);
        }

        return classes;
    }

    public static Plugin[] initAsPlugin(Class<?>[] classes) throws InstantiationException, IllegalAccessException {
        final Plugin[] plugins = new Plugin[classes.length];
        for (int i = 0; i < classes.length; i++) {
            plugins[i] = initAsPlugin(classes[i]);
        }

        return plugins;
    }

    private static Plugin initAsPlugin(Class<?> group) throws InstantiationException, IllegalAccessException {
        return (Plugin) group.newInstance();
    }
}
