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
package com.github.lukesky19.commandRestrictions.listener;

import com.github.lukesky19.commandRestrictions.CommandRestrictions;
import com.github.lukesky19.commandRestrictions.config.locale.Locale;
import com.github.lukesky19.commandRestrictions.config.locale.LocaleManager;
import com.github.lukesky19.commandRestrictions.config.settings.Settings;
import com.github.lukesky19.commandRestrictions.config.settings.SettingsManager;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class listens to commands sent by players and uses the configured regex and blocked text to restrict commands sent in-game.
 */
public class PlayerCommandPreProcessListener implements Listener {
    private final CommandRestrictions commandRestrictions;
    private final SettingsManager settingsManager;
    private final LocaleManager localeManager;

    public PlayerCommandPreProcessListener(
            @NotNull CommandRestrictions commandRestrictions,
            @NotNull SettingsManager settingsManager,
            @NotNull LocaleManager localeManager) {
        this.commandRestrictions = commandRestrictions;
        this.settingsManager = settingsManager;
        this.localeManager = localeManager;
    }

    /**
     * Listens to when a player sends a command and uses the configured regex and blocked text to restrict commands sent in-game.
     * @param playerCommandPreprocessEvent A {@link PlayerCommandPreprocessEvent}.
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCommandSent(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        ComponentLogger logger = commandRestrictions.getComponentLogger();
        @Nullable Settings settings = settingsManager.getSettings();
        @NotNull Locale locale = localeManager.getLocale();

        // If the plugin settings is invalid, display and error and return.
        if(settings == null) {
            logger.error(AdventureUtil.serialize(locale.invalidSettings()));
            return;
        }

        // Get the Player and command sent.
        Player player = playerCommandPreprocessEvent.getPlayer();
        String command = playerCommandPreprocessEvent.getMessage();

        // Create the command placeholder for console log messages.
        List<TagResolver.Single> placeholders = List.of(Placeholder.parsed("command", command));

        // Loop through configured regex and process any matches.
        settings.entries().forEach(entry -> {
            Pattern regex = entry.regex();

            // Check if regex is non-null.
            if(regex != null) {
                Matcher matcher = regex.matcher(command);

                // While the matcher has a match, process the match based on the configuration.
                while(matcher.find()) {
                    if(settings.debug()) {
                        logger.info(AdventureUtil.serialize("Match found for regex: " + regex));
                    }

                    // If any match should be blocked, cancel the event here.
                    // Also send the player a message and log the incident to console.
                    if(entry.blockAllMatches()) {
                        if(settings.debug()) {
                            logger.info(AdventureUtil.serialize("Blocking entire command for regex: " + regex));
                        }

                        player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.blockedCommandPlayerMessage()));
                        logger.warn(AdventureUtil.serialize(locale.blockedCommandConsoleMessage(), placeholders));
                        playerCommandPreprocessEvent.setCancelled(true);
                        return;
                    } else {
                        // Loop through the groups and check if the group contains any blocked text.
                        for(int i = 0; i <= matcher.groupCount(); i++) {
                            String group = matcher.group(i);

                            if(settings.debug()) {
                                logger.info(AdventureUtil.serialize("Processing group number " + i + " with text: " + group));
                            }

                            if(entry.blockedText().contains(group)) {
                                if(settings.debug()) {
                                    logger.info(AdventureUtil.serialize("Group number " + i + " contained blocked text. Blocked text: " + group));
                                }

                                player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.blockedCommandPlayerMessage()));
                                logger.warn(AdventureUtil.serialize(locale.blockedTextConsoleMessage(), placeholders));
                                playerCommandPreprocessEvent.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            } else {
                // Display and error if no regex is configured.
                logger.error(AdventureUtil.serialize(locale.invalidRegex()));
            }
        });
    }
}
