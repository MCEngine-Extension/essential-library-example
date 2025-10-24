package io.github.mcengine.extension.library.essential.example.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tab-completer for the {@code /essentiallibraryexample} command.
 */
public class EssentialAPITabCompleter implements TabCompleter {

    /**
     * Provides tab-completion suggestions for {@code /essentiallibraryexample}.
     *
     * @param sender  The source of the command.
     * @param command The command object.
     * @param alias   The alias used to execute the command.
     * @param args    The current command arguments.
     * @return A list of tab-completion suggestions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("start", "stop", "status");
        }
        return Collections.emptyList();
    }
}
