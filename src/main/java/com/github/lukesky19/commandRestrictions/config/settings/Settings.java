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
package com.github.lukesky19.commandRestrictions.config.settings;

import com.github.lukesky19.skylib.libs.configurate.objectmapping.ConfigSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class represents the plugin settings.
 * @param configVersion The version of the file.
 * @param debug Whether to display debug messages or not.
 * @param locale The name of the locale to use.
 * @param entries A list of {@link Entry}.
 */
@ConfigSerializable
public record Settings(
        @Nullable String configVersion,
        boolean debug,
        @Nullable String locale,
        @NotNull List<Entry> entries) {
    /**
     * This entry represents the configuration required to block a command.
     * @param regex The regex to check for.
     * @param blockAllMatches If any match should cancel the event.
     * @param blockedText If blockAllMatches is false, what text in captured groups by the regex should be checked and cancel the event for.
     */
    @ConfigSerializable
    public record Entry(
            @Nullable Pattern regex,
            boolean blockAllMatches,
            @NotNull List<String> blockedText) {
    }
}
