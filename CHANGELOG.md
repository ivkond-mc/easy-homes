### 21.4.0
* Initial release

### 21.4.1
* Added Xaero's maps mods integration

### 21.4.2
* Cooldown and homes limit are disabled in creative mode  

### 21.4.3
* Reworked translation
* Added `/back` command
* Binded `/back` command to `Shift` + `H` keystroke

### 21.4.4
* Added warning message if player is in the vehicle 

### 21.4.6
* Fixed potential crashes (NPE) when player data is not loaded
* Fixed empty home names passing validation
* Added home name length limit (64 characters)
* Fixed commands always reporting success even on failure
* Atomic file writes to prevent data corruption on crash
* Reduced disk writes during teleportation
* Fixed typo in onServerStarted method name

### 21.11.0
* Port to Minecraft 1.21.11
* Renamed ResourceLocation to Identifier (Mojang deobfuscation prep)
* Renamed ResourceKey.location() to ResourceKey.identifier()
* Updated Architectury Loom to 1.13, all dependencies updated

### 26.1.2
* Port to Minecraft 26.1.2
* Updated Fabric Loom, ModDevGradle/NeoForm, and Gradle to 9.4
* Updated Fabric API, NeoForge, MidnightLib, ModMenu dependencies
* Adapted Fabric networking, key mapping, and Xaero's Minimap APIs

### 21.9.0
* Port to Minecraft 1.21.9 (covers 1.21.9-1.21.10)
* Adapted KeyMapping.Category API change (ResourceLocation-based)
* Adapted NeoForge FML refactor: removed Bus enum from @EventBusSubscriber, updated FMLEnvironment API

### 21.4.7
* Port to Minecraft 1.21.5 (covers 1.21.4-1.21.8)
* Updated Architectury Loom to 1.11, Gradle to 8.11
* Updated Fabric API, NeoForge, MidnightLib, ModMenu dependencies
* Narrowed NeoForge version range to [21.4,)

### 21.4.5
* Fixed homes corruption on sever crash
