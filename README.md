# Create Infinite Speed 0.2.0 — Minecraft 1.20.1

## Target
- Minecraft Java Edition 1.20.1
- Forge 47.4.23
- Create 6.0.8

Create 6.0.8 is the current 1.20.1 Create release listed by CurseForge and Create's developer wiki.

## Included in this prototype
- 可変速ギアボックス
  - Intended range: 1–256 RPM
  - Redstone input supported
  - Lever/button can be used through normal redstone
- 無限可変速ギアボックス
  - Intended range: 1–practically unlimited RPM
  - Redstone input supported
  - If 2 or more are simultaneously active in the prototype's high-speed stage, a burst occurs
  - Burst radius: 50 blocks
  - Terrain destruction: disabled
  - Living-entity damage: 10,000
- Custom 32x32 textures based on the brass/copper + warning-red design
- No Mixins and no dependency on any weapon mod

## Important prototype limitation
The blocks are currently the gameplay/visual prototype. The actual Create kinetic-network RPM transformer and GUI are intentionally left as the next development step. The project is structured around Create 6.0.8 so that the kinetic implementation can be added against the correct 1.20.1 API.

## TaCZ
This prototype does not add weapon-specific integration. It is designed to remain a separate Create addon without directly modifying or depending on weapon systems, which minimizes cross-mod coupling.

## Build
Use a Java 17 JDK and a Forge 1.20.1 MDK with Gradle 8.x. Run `gradlew build`.
