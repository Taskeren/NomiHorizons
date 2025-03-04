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