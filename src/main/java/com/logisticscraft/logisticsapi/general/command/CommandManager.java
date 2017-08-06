package com.logisticscraft.logisticsapi.general.command;

import com.logisticscraft.logisticsapi.LogisticsApiPlugin;
import com.logisticscraft.logisticsapi.util.console.Tracer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class CommandManager {
    private static Map<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>() {{
        put("logisticsapi", new LogisticsApiCommand());
    }};

    public static void registerCommands() {
        LogisticsApiPlugin plugin = LogisticsApiPlugin.getInstance();

        for (Map.Entry<String, CommandExecutor> commandExecutorEntry : commands.entrySet()) {
            Tracer.msg("  Registering command: " + commandExecutorEntry.getKey());
            PluginCommand command = plugin.getCommand(commandExecutorEntry.getKey());
            if (command != null) {
                Tracer.msg("  Command found, registering Executor.");
                command.setExecutor(commandExecutorEntry.getValue());
            } else Tracer.warn("  Command not found!");
        }
    }
}
