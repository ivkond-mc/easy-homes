![Logo](docs/logo.png)

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

| Command                | Alias             | Description                                                                                                                                                              |
|------------------------|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/home [<name>]`       | `/ho [<name>]`    | Instantly return to your saved homes. If no name is specified, you'll be teleported to your default home.                                                                |
| `/homes`               | `/hl`             | Keep track of all your saved locations. Lists all your homes by name, dimension and coordinates.                                                                         |
| `/sethome [<name>]`    | `/sh [<name>]`    | Mark current location as a home. Name it for easy identification or leave it blank to set a default home. If name equals `-` - home name will be generated automatically |
| `/renhome <old> <new>` | `/rh <old> <new>` | Change existing home name from `<old>` to `<new>`.                                                                                                                       |
| `/delhome <name>`      | `/dh <name>`      | Removes the saved home with the specified name.                                                                                                                          |

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