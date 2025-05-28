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
package com.github.lukesky19.commandRestrictions.config.locale;

import com.github.lukesky19.commandRestrictions.CommandRestrictions;
import com.github.lukesky19.commandRestrictions.config.settings.Settings;
import com.github.lukesky19.commandRestrictions.config.settings.SettingsManager;
import com.github.lukesky19.skylib.config.ConfigurationUtility;
import com.github.lukesky19.skylib.format.FormatUtil;
import com.github.lukesky19.skylib.libs.configurate.ConfigurateException;
import com.github.lukesky19.skylib.libs.configurate.yaml.YamlConfigurationLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

/**
 * This class manages the plugin's locale.
 */
public class LocaleManager {
    private final CommandRestrictions commandRestrictions;
    private final SettingsManager settingsManager;
    private Locale locale;
    private final Locale DEFAULT_LOCALE = new Locale(
            "1.0.0.0",
            "<dark_red><bold>Security</bold></dark_red> <gray><bold>▪</bold></gray> ",
            "<aqua>The plugin has been reloaded.</aqua>",
            "<red>Unable to compare command ran due to invalid plugin settings.</red>",
            "<red>Unable to check command against regex due to a null regex configured.</red>",
            "<red>The command you sent can only be ran through console.</red>",
            "<red>Blocked a command due to regex match:</red> <white><command></white>",
            "<red>Blocked a command containing blocked text:</red> <white><command></white>");

    /**
     * Gets the configured locale or the default locale.
     * @return A {@link Locale} object.
     */
    @NotNull
    public Locale getLocale() {
        if(locale == null) return DEFAULT_LOCALE;

        return locale;
    }

    /**
     * Constructor
     * @param commandRestrictions The plugin's class
     * @param settingsManager A {@link SettingsManager} instance.
     */
    public LocaleManager(
            @NotNull CommandRestrictions commandRestrictions,
            @NotNull SettingsManager settingsManager) {
        this.commandRestrictions = commandRestrictions;
        this.settingsManager = settingsManager;
    }

    /**
     * A method to reload the plugin's locale config.
     */
    public void reload() {
        ComponentLogger logger = commandRestrictions.getComponentLogger();
        locale = null;

        // Save any default locale files if it doesn't exist on the disk.
        copyDefaultLocales();

        // Only load locale if plugin settings is valid.
        Settings settings = settingsManager.getSettings();
        if(settings == null || settings.locale() == null) {
            logger.error(FormatUtil.format("<red>Failed to load plugin's locale due to invalid plugin settings.</red>"));
            return;
        }

        // Get the configured locale to use
        String localeString = settings.locale();
        Path path = Path.of(commandRestrictions.getDataFolder() + File.separator + "locale" + File.separator + (localeString + ".yml"));

        // Attempt to load the configured locale.
        YamlConfigurationLoader loader = ConfigurationUtility.getYamlConfigurationLoader(path);
        try {
            locale = loader.load().get(Locale.class);
        } catch (ConfigurateException exception) {
            throw new RuntimeException(exception);
        }

        // Validate the configured locale that was loaded.
        validateLocale();
    }

    /**
     * Copies the default locale files that come bundled with the plugin, if they do not exist at least.
     */
    private void copyDefaultLocales() {
        Path path = Path.of(commandRestrictions.getDataFolder() + File.separator + "locale" + File.separator + "en_US.yml");
        if (!path.toFile().exists()) {
            commandRestrictions.saveResource("locale" + File.separator + "en_US.yml", false);
        }
    }

    /**
     * Validates the plugin's locale.
     */
    private void validateLocale() {
        ComponentLogger logger = commandRestrictions.getComponentLogger();
        if(locale == null) return;

        if(locale.prefix() == null) {
            logger.warn(FormatUtil.format("No prefix configured in locale file."));
            locale = DEFAULT_LOCALE;
            return;
        }

        if(locale.invalidSettings() == null) {
            logger.warn(FormatUtil.format("No invalid settings message configured in locale file."));
            locale = DEFAULT_LOCALE;
            return;
        }

        if(locale.invalidRegex() == null) {
            logger.warn(FormatUtil.format("No invalid regex message configured in locale file."));
            locale = DEFAULT_LOCALE;
            return;
        }

        if(locale.blockedCommandPlayerMessage() == null) {
            logger.warn(FormatUtil.format("No blocked player message configured in locale file."));
            locale = DEFAULT_LOCALE;
            return;
        }

        if(locale.blockedCommandConsoleMessage() == null) {
            logger.warn(FormatUtil.format("No blocked command console message configured in locale file."));
            locale = DEFAULT_LOCALE;
            return;
        }

        if(locale.blockedTextConsoleMessage() == null) {
            logger.warn(FormatUtil.format("No blocked text console message configured in locale file."));
            locale = DEFAULT_LOCALE;
        }
    }
}
