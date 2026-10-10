![Logo](docs/logo.png)

[![Licence](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/license/mit)
[![Java](https://img.shields.io/badge/java-21%20%7C%2025-blue)](https://adoptium.net/temurin/releases/)
[![Minecraft](https://img.shields.io/badge/minecraft-1.18.2%20to%2026.3-blue)](https://www.minecraft.net/)
[![Build status](https://img.shields.io/github/actions/workflow/status/ivkond-mc/easy-homes/gradle-publish.yml?branch=main)](https://github.com/ivkond-mc/easy-homes/actions/workflows/gradle-publish.yml?branch=main)

### 🏠 About

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

### 🖥️ Commands

* ```
  /home [<name>]
  ```
  Instantly return to your saved homes. If no name is specified, you'll be teleported to your default home.
* ```
  /homes
  ```
  Keep track of all your saved locations. Lists all your homes by name, dimension and coordinates.
* ```
  /back
  ```
  Instantly return to previously visited home. 
* ```
  /sethome [<name>]
  ```
  Mark current location as a home. Name it for easy identification or leave it blank to set a default home.
  If name equals `-` - home name will be generated automatically.
* ```
  /renhome <old> <new>
  ```
  Change existing home name from `<old>` to `<new>`.
* ```
  /delhome <name>
  ```
  Removes the saved home with the specified name.

### ⌨️ Key Binds

| Key                             | Description                                                              |
|---------------------------------|--------------------------------------------------------------------------|
| <kbd>H</kbd>                    | Bind for `/home` command. Teleports to default home.                     |
| <kbd>Shift</kbd> + <kbd>H</kbd> | Bind for `/back` command. Teleports to previously visited home.          |
| <kbd>J</kbd>                    | Bind for `/sethome -` command. Save current position with generated name |

### 🔧 Configuration

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

## 🛠️ Development

The mod targets multiple Minecraft versions (1.18.2 – 26.3) for Fabric and NeoForge from a single branch
with [Stonecutter](https://stonecutter.kikugie.dev). Version differences live in the shared sources as
`//?` chiseled conditions and in `stonecutter.properties.toml` (per-version dependency versions).

Gradle itself runs on Java 25 (provisioned automatically via `gradle/gradle-daemon-jvm.properties`);
older Minecraft nodes are compiled with Java 21 toolchains automatically.

* `./gradlew build` — build the active version node
* `./gradlew chiseledBuild` — build every version × loader combination
* `./gradlew publishAll` — build and upload every node to Modrinth/CurseForge
* Version switching tasks are listed under the `Stonecutter tasks` task group (`./gradlew tasks`),
  or use the [Stonecutter plugin](https://plugins.jetbrains.com/plugin/26040-stonecutter) for IntelliJ

To add a new Minecraft version: add it to the version list in `settings.gradle` and a matching section
in `stonecutter.properties.toml`.

## 📢 Feedback
Feel free to reach out in our Discord if you encounter any bugs, have questions, or would like to share suggestions for improving the mod: [https://discord.gg/CJFGMTB6kN](https://discord.gg/CJFGMTB6kN)
