package com.logisticscraft.logisticsloader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LogisticsLoader {
    private final static String FILE_NAME;
    private final static String DOWNLOAD_URL;

    static {
        Properties properties = new Properties();
        String fileName = null;
        String downloadUrl = null;
        try {
            properties.load(LogisticsLoader.class.getResourceAsStream("/logistics_download.properties"));
            String repository = properties.getProperty("repository");
            String groupId = properties.getProperty("groupId").replaceAll("\\.", "/");
            String artifactId = properties.getProperty("artifactId").replaceAll("\\.", "/");
            String version = properties.getProperty("version");
            fileName = artifactId + "-" + version + ".jar";
            downloadUrl = repository + groupId + "/" + artifactId + "/" + version + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        FILE_NAME = fileName;
        DOWNLOAD_URL = downloadUrl;
    }

    public static void load() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled("LogisticsAPI")) {
            return;
        }
        install();
    }

    private static void install() {
        File pluginFolder = new File("plugins");
        File outputFile = new File(pluginFolder, FILE_NAME);
        if (!outputFile.isFile()) {
            try {
                URL website = new URL(DOWNLOAD_URL);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            PluginManager pluginManager = Bukkit.getPluginManager();
            Plugin plugin = pluginManager.loadPlugin(outputFile);
            pluginManager.enablePlugin(plugin);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            e.printStackTrace();
        }
    }
}
