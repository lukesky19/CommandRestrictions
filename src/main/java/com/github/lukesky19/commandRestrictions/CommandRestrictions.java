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
package com.github.lukesky19.commandRestrictions;

import com.github.lukesky19.commandRestrictions.command.CommandRestrictionsCommand;
import com.github.lukesky19.commandRestrictions.config.locale.LocaleManager;
import com.github.lukesky19.commandRestrictions.config.settings.SettingsManager;
import com.github.lukesky19.commandRestrictions.listener.PlayerCommandPreProcessListener;
import com.rylinaux.plugman.api.PlugManAPI;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * The main plugin class.
 */
public class CommandRestrictions extends JavaPlugin {
    private SettingsManager settingsManager;
    private LocaleManager localeManager;

    /**
     * Called when the plugin is enabled and initializes required classes and data.
     */
    @Override
    public void onEnable() {
        // Prevent unloading this plugin using plugman for security reasons.
        PlugManAPI.iDoNotWantToBeUnOrReloaded(this.getName());

        // Create class instances
        settingsManager = new SettingsManager(this);
        localeManager = new LocaleManager(this, settingsManager);
        CommandRestrictionsCommand commandRestrictionsCommand = new CommandRestrictionsCommand(this, localeManager);

        // Register the plugin's command
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                commands ->
                        commands.registrar().register(commandRestrictionsCommand.createCommand(),
                                "Command to manage and use the CommandRestrictions plugin.",
                                List.of("security")));

        // Register listeners
        this.getServer().getPluginManager().registerEvents(new PlayerCommandPreProcessListener(this, settingsManager, localeManager), this);

        // Reload plugin data.
        reload();
    }

    /**
     * Reloads plugin data.
     */
    public void reload() {
        settingsManager.reload();
        localeManager.reload();
    }
}
