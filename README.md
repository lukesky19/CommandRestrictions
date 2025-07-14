# CommandRestrictions
## Description
* A simple plugin to block specific in-game commands using regex.

## Required Dependencies
* [SkyLib](https://github.com/lukesky19/SkyLib)

## Commands
- /commandrestrictions - Command to manage the plugin.
    - Alias:
        - /security
- /commandrestrictions reload - Reloads the plugin.

## Permisisons
- `commandrestrictions.commands.commandrestrictions` - The permission to access the /commandrestrictions base command.
- `commandrestrictions.commands.commandrestrictions` - The permission to access /commandrestrictions reload.

## Issues, Bugs, or Suggestions
* Please create a new [GitHub Issue](https://github.com/lukesky19/CommandRestrictions/issues) with your issue, bug, or suggestion.
* If an issue or bug, please post any relevant logs containing errors related to CommandRestrictions and your configuration files.
* I will attempt to solve any issues or implement features to the best of my ability.

## FAQ
Q: What versions does this plugin support?

A: 1.21.4, 1.21.5, 1.21.6, and 1.21.7.

Q: Are there any plans to support any other versions?

A: I will always do my best to support the latest versions of the game. I will sometimes support other versions until I no longer use them.

Q: Does this work on Spigot? Paper? (Insert other server software here)?

A: I only support Paper, but this will likely also work on forks of Paper (untested). There are no plans to support any other server software (i.e., Spigot or Folia).

## For Server Admins/Owners
* Download the plugin [SkyLib](https://github.com/lukesky19/SkyLib/releases).
* Download the plugin from the releases tab and add it to your server.

## Building
* Go to [SkyLib](https://github.com/lukesky19/SkyLib) and follow the "For Developers" instructions.
* Then run:
  ```./gradlew build```

## Why AGPL3?
I wanted a license that will keep my code open source. I believe in open source software and in-case this project goes unmaintained by me, I want it to live on through the work of others. And I want that work to remain open source to prevent a time when a fork can never be continued (i.e., closed-sourced and abandoned).
