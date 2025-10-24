package io.github.mcengine.extension.library.essential.example;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.api.essential.extension.library.IMCEngineEssentialLibrary;

import io.github.mcengine.extension.library.essential.example.command.EssentialLibraryCommand;
import io.github.mcengine.extension.library.essential.example.listener.EssentialLibraryListener;
import io.github.mcengine.extension.library.essential.example.tabcompleter.EssentialAPITabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Main class for the Essential Library example module.
 * <p>
 * Registers the {@code /essentiallibraryexample} command and related event listeners.
 */
public class ExampleEssentialLibrary implements IMCEngineEssentialLibrary {

    /**
     * Custom extension logger for this module, with contextual labeling.
     */
    private MCEngineExtensionLogger logger;

    /**
     * Initializes the Essential Library example module.
     * Called automatically by the MCEngine core plugin.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        // Initialize contextual logger once and keep it for later use.
        this.logger = new MCEngineExtensionLogger(plugin, "Library", "EssentialExampleLibrary");

        try {
            // Register event listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new EssentialLibraryListener(plugin, this.logger), plugin);

            // Reflectively access Bukkit's CommandMap
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define the /essentiallibraryexample command
            Command essentialLibraryExampleCommand = new Command("essentiallibraryexample") {

                /**
                 * Handles command execution for /essentiallibraryexample.
                 */
                private final EssentialLibraryCommand handler = new EssentialLibraryCommand();

                /**
                 * Handles tab-completion for /essentiallibraryexample.
                 */
                private final EssentialAPITabCompleter completer = new EssentialAPITabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            essentialLibraryExampleCommand.setDescription("Essential Library example command.");
            essentialLibraryExampleCommand.setUsage("/essentiallibraryexample");

            // Dynamically register the /essentiallibraryexample command
            commandMap.register(plugin.getName().toLowerCase(), essentialLibraryExampleCommand);

            this.logger.info("Enabled successfully.");
        } catch (Exception e) {
            this.logger.warning("Failed to initialize ExampleEssentialLibrary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when the Essential Library example module is disabled/unloaded.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onDisload(Plugin plugin) {
        if (this.logger != null) {
            this.logger.info("Disabled.");
        }
    }

    /**
     * Sets the unique ID for this module.
     *
     * @param id the assigned identifier (ignored; a fixed ID is used for consistency)
     */
    @Override
    public void setId(String id) {
        MCEngineCoreApi.setId("mcengine-essential-library-example");
    }
}
