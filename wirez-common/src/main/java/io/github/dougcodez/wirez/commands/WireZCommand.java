package io.github.dougcodez.wirez.commands;

import io.github.dougcodez.wirez.commands.sub.SubCommand;
import io.github.dougcodez.wirez.commands.sub.SubCommandRegistry;
import io.github.dougcodez.wirez.commands.user.CommandUser;
import io.github.dougcodez.wirez.files.types.lang.Lang;
import io.github.dougcodez.wirez.files.types.settings.WireZSettingsFile;

import java.util.List;
import java.util.Map;

public interface WireZCommand {

    default boolean onCommand(CommandUser user, String[] args) {
        if (!user.hasPermission("wirez.help")) {
            user.sendMessage(Lang.PREFIX.toConfigString() + Lang.NO_PERMISSION.toConfigString());
            return true;
        }

        Map<String,SubCommand> subCommandMap = SubCommandRegistry.getSubCommandMap();
        if (args.length > 0) {
            for (SubCommand subCommand : subCommandMap.values()) {
                if (args[0].equalsIgnoreCase(subCommand.getSubCommandName())) {
                    subCommand.perform(user, args, getSettingsFile());
                }
            }
        } else {
            for (SubCommand subCommand : subCommandMap.values()) {
                user.sendMessage(Lang.PREFIX.toConfigString() + subCommand.getSubCommandSyntax() + " - " + subCommand.getSubCommandDescription());
            }
        }

        return true;
    }

    WireZSettingsFile getSettingsFile();
}

