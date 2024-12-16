![Logo](docs/logo.png)

[![Licence](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/license/mit)
[![Modrinth](https://img.shields.io/modrinth/game-versions/easy-homes?style=flat)](https://modrinth.com/mod/easy-homes)
[![Build status](https://img.shields.io/github/actions/workflow/status/ivkond-mc/easy-homes/gradle-publish.yml)](https://github.com/ivkond-mc/easy-homes/actions/workflows/gradle-publish.yml)
[![Java 21](https://img.shields.io/badge/java-21%2B-blue)](https://adoptium.net/temurin/releases/?version=21)

### About

In Minecraft, navigating vast worlds can often be time-consuming and frustrating, especially when managing multiple
locations. **Easy Homes** eliminates this frustration by providing a streamlined way to save and revisit key areas.
Whether
you're mining in a remote cave, working on a distant build, or farming resources in far-off biomes, you can teleport
between locations instantly, saving time and effort.

This mod is perfect for players who value efficiency, organization, and the freedom to explore without losing their way.
It’s also highly useful in multiplayer settings, where players often manage shared bases or individual builds scattered
across the map.

With **Easy Homes**, you’ll spend less time traveling and more time enjoying the creative and adventurous aspects of
Minecraft. It’s the ultimate quality-of-life upgrade for any player!

### Commands

| Command                | Description                                                                                                                                                              |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/home [<name>]`       | Instantly return to your saved homes. If no name is specified, you'll be teleported to your default home.                                                                |
| `/homes`               | Keep track of all your saved locations. Lists all your homes by name, dimension and coordinates.                                                                         |
| `/sethome [<name>]`    | Mark current location as a home. Name it for easy identification or leave it blank to set a default home. If name equals `-` - home name will be generated automatically |
| `/renhome <old> <new>` | Change existing home name from `<old>` to `<new>`.                                                                                                                       |
| `/delhome <name>`      | Removes the saved home with the specified name.                                                                                                                          |

### Key Binds

| Key | Description                                                              |
|-----|--------------------------------------------------------------------------|
| `H` | Bind for `/home` command. Teleports to default home.                     |
| `J` | Bind for `/sethome -` command. Save current position with generated name |

### Configuration

#### Properties

| Property   | Default | Description                        |
|------------|---------|------------------------------------|
| `cooldown` | 5       | Teleportation cooldown in seconds. |
| `maxHomes` | 5       | How many homes may have user.      |

#### Server

Place `easy_homes.json` in your server `config` folder with following content:

```json
{
  "cooldown": 5,
  "maxHomes": 5
}
```

#### Client

> Fabric users need to install **Mod Menu
** ([modrinth](https://modrinth.com/mod/modmenu), [curseforge](https://www.curseforge.com/minecraft/mc-mods/fabric-api))

To configure mod properties you can use your mod loader configuration screen.
