package com.logisticscraft.logisticsloader;

import lombok.experimental.UtilityClass;
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

@UtilityClass
public class LogisticsLoader {

    public static boolean install() throws LogisticInstallException {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled("LogisticsApi")) {
            return false;
        }

        // Read the download url from the resource file
        URL downloadUrl;
        File outputFile;
        try {
            Properties properties = new Properties();
            properties.load(LogisticsLoader.class.getResourceAsStream("/logistics_download.properties"));
            String groupId = properties.getProperty("groupId");
            String artifactId = properties.getProperty("artifactId");
            String version = properties.getProperty("version");
            String url = properties.getProperty("url")
                    .replace("%groupId%", groupId)
                    .replace("%artifactId%", artifactId)
                    .replace("%version%", version);
            downloadUrl = new URL(url);
            outputFile = new File("plugins/" + artifactId + "-" + version + ".jar");
        } catch (IOException e) {
            throw new LogisticInstallException(e);
        }

        if (outputFile.isFile()) {
            return false;
        }

        // Download the file
        try {
            ReadableByteChannel readableByteChannel;
            readableByteChannel = Channels.newChannel(downloadUrl.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new LogisticInstallException(e);
        }

        try {
            Plugin plugin = pluginManager.loadPlugin(outputFile);
            pluginManager.enablePlugin(plugin);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            throw new LogisticInstallException(e);
        }

        return true;
    }
}
