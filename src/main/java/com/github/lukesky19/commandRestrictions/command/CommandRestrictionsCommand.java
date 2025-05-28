/*
    CommandRestrictions is a plugin that restricts specific commands or partial commands using regex.
    Copyright (C) 2025  lukeskywlker19

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.github.lukesky19.commandRestrictions.command;

import com.github.lukesky19.commandRestrictions.CommandRestrictions;
import com.github.lukesky19.commandRestrictions.config.locale.Locale;
import com.github.lukesky19.commandRestrictions.config.locale.LocaleManager;
import com.github.lukesky19.skylib.format.FormatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.entity.Player;

/**
 * This class creates the plugin command to be registered using the Lifecycle API.
 */
public class CommandRestrictionsCommand {
    private final CommandRestrictions commandRestrictions;
    private final LocaleManager localeManager;

    /**
     * Constructor
     * @param commandRestrictions The plugin's class.
     * @param localeManager A {@link LocaleManager} instance.
     */
    public CommandRestrictionsCommand(CommandRestrictions commandRestrictions, LocaleManager localeManager) {
        this.commandRestrictions = commandRestrictions;
        this.localeManager = localeManager;
    }

    /**
     * This class creates the command to be registered using the Lifecycle API.
     * @return A {@link LiteralCommandNode} of {@link CommandSourceStack}.
     */
    public LiteralCommandNode<CommandSourceStack> createCommand() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("commandrestrictions")
                .requires(ctx -> ctx.getSender().hasPermission("commandrestrictions.commands.commandrestrictions"));

        builder.then(Commands.literal("reload")
            .requires(ctx -> ctx.getSender().hasPermission("commandrestrictions.commands.commandrestrictions.reload"))
            .executes(ctx -> {
                ComponentLogger logger = commandRestrictions.getComponentLogger();
                Locale locale = localeManager.getLocale();

                commandRestrictions.reload();

                if(ctx.getSource().getSender() instanceof Player player) {
                    player.sendMessage(FormatUtil.format(locale.prefix() + locale.reload()));
                } else {
                    logger.info(FormatUtil.format(locale.reload()));
                }

                return 1;
            })
        );

        return builder.build();
    }
}
