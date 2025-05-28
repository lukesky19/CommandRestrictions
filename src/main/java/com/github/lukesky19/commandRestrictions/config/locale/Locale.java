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

import com.github.lukesky19.skylib.libs.configurate.objectmapping.ConfigSerializable;

/**
 * This class represents the plugin's locale messages.
 * @param configVersion The version of the file.
 * @param prefix The plugin's prefix.
 * @param reload The message sent when the plugin is reloaded.
 * @param invalidSettings The message sent when the plugin's settings is invalid.
 * @param invalidRegex The message sent when a configured regex is invalid.
 * @param blockedCommandPlayerMessage The message sent to the player when their command was blocked.
 * @param blockedCommandConsoleMessage The message sent to console when a command was blocked due to blocking any match.
 * @param blockedTextConsoleMessage The message sent to console when a command was blocked due to a partial match.
 */
@ConfigSerializable
public record Locale(
        String configVersion,
        String prefix,
        String reload,
        String invalidSettings,
        String invalidRegex,
        String blockedCommandPlayerMessage,
        String blockedCommandConsoleMessage,
        String blockedTextConsoleMessage) {
}
