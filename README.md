<h1 align="center">TrueEssentials</h1>
<p align="center"><i>A feature-rich, lightweight alternative to EssentialsX for PaperMC servers.</i></p>

> [!CAUTION]
> This project has been archived and is no longer receiving updates. Feel free to fork the project and customize as you want!

## Features
- **Core utility commands**: gamemode shortcuts, flight toggle, ping, sudo, ender chest, disposal bin, teleport requests, and inventory viewing — the essentials, without the vanilla-overriding bloat.
- **Homes**: players can set, list, teleport to, and delete personal homes, with permission-based home limits.
- **Spawn**: a configurable server spawn point with teleport delay, cancel-on-move/damage, and sound options.
- **Moderation**: warn, ban, unban, and vanish players, with escalating ban durations and a punishment history (`/modlogs`).
- **Command hider/spoofer**: hide selected commands (or only allow a specific list) from players via the command manager extension.
- **Join experience**: customizable splash screen (title/subtitle/sound) plus custom join, first-join, and quit messages.
- **Chat**: custom chat format, with legacy `&` color codes and full MiniMessage support throughout.
- **Placeholders**: PlaceholderAPI support, with simple built-in placeholders (`%player_name%`, `%player_displayname%`) as a fallback.

## Commands
| Command | Aliases | Description |
| --- | --- | --- |
| `/trueessentials <text>` | `/te`, `/truee` | Base plugin command. |
| `/inventorysee <player>` | `/invsee`, `/inv` | View another player's inventory. |
| `/enderchest [player]` | `/ec` | Open your ender chest or another player's. |
| `/flight [player]` | `/fly` | Toggle flight for yourself or another player. |
| `/kickall` | | Kick all online players, except those exempt. |
| `/ping [player]` | | View your ping or another player's. |
| `/sudo <player> <command>` | | Force another player to run a command. |
| `/gamemode <mode> [player]` | | Change your or another player's game mode. |
| `/gmc [player]` | | Shortcut for `/gamemode creative`. |
| `/gms [player]` | | Shortcut for `/gamemode survival`. |
| `/gma [player]` | | Shortcut for `/gamemode adventure`. |
| `/gmsp [player]` | | Shortcut for `/gamemode spectator`. |
| `/trash` | `/disposal` | Opens a disposal menu. |
| `/tpask <player>` | `/tpa` | Request to teleport to another player. |
| `/tpaccept` | | Accept a pending teleport request. |
| `/clearchat` | `/cc` | Clears chat for all players, except those exempt. |
| `/spawn` | | Teleport to the server spawn point. |
| `/setspawn` | | Set the server spawn point to your location. |
| `/home <name>` | | Teleport to one of your homes. |
| `/sethome <name>` | | Create a home at your current location. |
| `/delhome <name>` | | Delete one of your homes. |
| `/homes` | | List all of your homes. |
| `/ban <player> <reason>` | | Ban a player from the server. |
| `/unban <player>` | | Unban a player. |
| `/warn <player> <reason>` | | Warn a player. |
| `/modlogs <player>` | | View a player's punishment history. |
| `/vanish` | | Toggle vanish for yourself. |

## Installation
1. Download the latest build of TrueEssentials, or build it yourself:
```bash
git clone https://github.com/Nebulations/TrueEssentials.git
cd TrueEssentials
mvn clean package
```
2. Drop the resulting jar from `target/` into your server's `plugins` folder.
3. Start (or restart) your server to generate the default configuration files.
4. Edit `plugins/TrueEssentials/config.yml` to enable or disable extensions and adjust messages to fit your server.
5. Most extension changes apply with `/trueessentials reload` — a full restart is only needed for tab-completion of extension commands to update.

## Requirements
- Java 17
- A Paper (or Paper-fork) server running 1.19.4+
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (optional, for the full placeholder experience — falls back to built-in placeholders if absent)

## Configuration
All settings live in `plugins/TrueEssentials/config.yml`, including:
- Global settings (prefix, primary/secondary/error colors, PlaceholderAPI toggle)
- Per-extension toggles: `splashscreen`, `join-quit-messages`, `chat-format`, `spawn`, `homes`, `command-manager`, `moderation`
- Home limits per permission group, moderation reasons/ban durations, and hidden-command lists

## Metrics
TrueEssentials collects anonymous usage statistics (server and player counts) via [bStats](https://bstats.org/plugin/bukkit/TrueEssentials). You can opt out in `plugins/bStats/config.yml`.

## Support
- [Open an issue](https://github.com/Nebulations/TrueEssentials/issues) for bugs or feature requests
- Join the [Discord server](https://discord.gg/krpVvqGPSp) for more direct support

## License
Licensed under [GPL-3.0-or-later](https://www.gnu.org/licenses/gpl-3.0.en.html).