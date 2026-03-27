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

### 21.4.5
* Fixed homes corruption on sever crash
