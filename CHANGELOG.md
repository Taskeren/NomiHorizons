# Changelog

[KeepAChangelog Convention](https://keepachangelog.com/en/1.1.0/)

## [1.0.0] - 2025-2-17

### Added

- Initialize Nomi Horizons
- GregTech Fluid Tank support for Extra Utilities 2 Drums

## [1.1.0] - 2025-2-18

### Added

- Shrink the Bounding Box size of Extra Utilities 2 Drums
- Shrink the Bounding Box size of Actually Additions Crates

## [1.2.0] - 2025-2-18

### Added

- Fix invalid GregTech fluid name shown when right-clicking Extra Utilities 2 Drums.
  Now it correctly shows the right text instead of the missing translation key.
  Eg: `fluid.sulfuric_acid` -> `Sulfuric Acid`

## [1.3.0] - 2025-2-18

### Added

- Modify the Capacity of GregTech Cells to 144L to better handle fluids.
  This option can be toggled off in Mixin Configurations, see below.
  Mixin Config: `setCellCapacityTo144`.
- Added Mixin Configuration to allow users to choose which features are enabled.
  Only features that may cause balancing issues or at experimental stage will be added to this config,
  and all of them are enabled by default.

## [1.4.0] - 2025-2-24

### Added

- Added support for Interactive Fluid Slots in Super/Quantum Tanks GUI.
  Now, you can directly fill/drain fluids by clicking the fluid slot while holding fluid containers.
  Mixin Config: `useNonPhantomFluidTankWidgets`.

## [1.4.1] - 2025-2-24

### Added

- Added support for Interactive Fluid Slots in Input/Output Hatches GUI.
  For details, see above.

## [1.4.2] - 2025-2-25

### Fixed

- Fixed a bug causing Duplicating Extra Utilities 2 Drums.

## [1.5.0] - 2025-3-3

### Added

- Added Elevator-kind blocks in GregTech Cleanroom structures.
  Currently supported elevators are Elevators from Elevator Mod and Travel Anchor from Ender IO.

## [1.5.1] - 2025-3-4

### Modify

- Added Elevator block previews in Cleanroom structures.

## [1.6.0] - 2025-3-7

### Added

- Added Cleanroom Pardon, which allows running recipes that require (Sterile) Cleanroom in Multiblocks without said
  required cleanroom environment.
  You can configure which ones are pardoned in in-game config without restarting the game.
  Mixin Config: `useCleanroomPardon`.

## [1.7.0] - 2025-3-10

### Added

- Added Creative Spray Can, which you can select the wanted color by right-clicking.
  Currently, no crafting recipe is provided, and it needs suggestion.

## [1.7.1] - 2025-3-16

### Modify

- Added Mixin Configuration Auto-Upgrade.
  It will now add new options to the JSON file when the version is updated.
  And if the file is corrupted, it will generate a new file with all values by default.

### Added

- Added One Stack Only, which forces each type of ItemStack can only consume 1 slot in GregTech Machines (or other things uses GTItemStackHandler.)
  This will prevent 1 type of item takes up more than 1 slot in a machine, in a passive product line.
  Mixin Config: `useOneStackOnly`.

## [1.7.2] - 2025-3-29

### Re-Implementation

- Removed the One Stack Only Mixins, and moved its functionality to One Stack Filter item, as a cover to machines.
  Also fixed a bug where stacks with only different amount are considered as different stacks.

## [1.8.0] - 2025-3-10 [EXPERIMENTAL]

### Added

- Added Advanced Recipe Logic, where the machines won't be stuck at the first recipe when there is something blocking it,
  like there is not enough space to dump outputs.
  Mixin Config: `useBetterRecipe`. Can be disabled in in-game configuration, **and is DISABLED by default.**
- (Technical) As a part of new things above, `RecipeMap`s are now implementing `INewHorizonsRecipeMap`, which provides
  more methods for querying recipes.

## [1.8.1] - 2025-3-16 [EXPERIMENTAL]

### Added

- Refactored Configuration system, allowing players to reload the configurations with command `/nomi-horizons reload`.
  This is pretty useful when you are running a server and adjusting configurations, where you don't have to restart the server
  every time when you have something changed.
  *This feature MAYBE also added to 1.7.X branch, please make sure to check it in CHANGELOG.*

### Fixed

- Fixed a bug where machines with dynamically searched recipes (like MultiSmelter, which will read the Furnace recipes runtime),
  are not working correctly, when the Advanced Recipe Logic is on.