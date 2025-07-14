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
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import com.rylinaux.plugman.api.PlugManAPI;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
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
        if(!checkSkyLibVersion()) return;

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

    /**
     * Checks if the Server has the proper SkyLib version.
     * @return true if it does, false if not.
     */
    private boolean checkSkyLibVersion() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        Plugin skyLib = pluginManager.getPlugin("SkyLib");
        if (skyLib != null) {
            String version = skyLib.getPluginMeta().getVersion();
            String[] splitVersion = version.split("\\.");
            int second = Integer.parseInt(splitVersion[1]);

            if(second >= 3) {
                return true;
            }
        }

        this.getComponentLogger().error(AdventureUtil.serialize("SkyLib Version 1.3.0.0 or newer is required to run this plugin."));
        this.getServer().getPluginManager().disablePlugin(this);
        return false;
    }
}
