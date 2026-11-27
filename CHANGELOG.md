# Changelog

All notable changes to Bard are documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.2.2+1.21.1]

### Changed

- The Loot & Explore themed Rapiers, Lute, Lyre and Harp Crossbow count as Ender Dragon, Wither, Glacial and Elder Guardian weapons for Loot & Explore's smithing advancements
- The Luthier Villager now plays its songs with the Lute & Lyre animations, using separate arms and holding the actual instrument model (Config Option `luthier_song_animation` in tweaks.json)
- The Atlantis Harp Crossbow (Loot & Explore) is no longer in the tier 5 loot tag, it's crafted at the smithing table like the other Loot & Explore variants

### Fixed

- Fix the Luthier Villager not performing its songs
- Fix the smelting and blasting recipes that melt down Bard gear never unlocking in the recipe book
- Sound effects are now mono and 48 kHz, so they come from the right direction and get quieter with distance like vanilla sounds. This includes the songs, which are now heard from the bard's position

## [1.2.1+1.21.1]

### Fixed

- Fix wrong LNE Item Recipes

## [1.2.0+1.21.1]

### Changed

- Adopt Spell Engine 1.10.9 & More RPG Library 2.8.0
- **BREAKING for modpack/datapack developers:** More RPG Library's namespace was renamed `more_rpg_classes` -> `mrpg_lib` (and its advancement root moved to `mrpg_lib:root`). If you have any data files, loot tables, tags or advancements that reference the old `more_rpg_classes:`/`more_rpg_content:` ids from this mod or MRPGL, update them to `mrpg_lib:`
- Updated internal advancement parents, mod dependency declarations (fabric.mod.json, neoforge.mods.toml)
- Bard equipment is now affiliated loot: it drops more often for players carrying the matching spell book

### Fixed

- Fixed Mipmap Warnings for Lyre & Lute Models & Textures

## [1.1.1+1.21.1]

### Added

- Add a Config Option to turn down the volume of the Luthier Villager

### Changed

- Drop Forgified Fabric API (FFAPI) as a required dependency
- Replace AzurelibArmor with ArmorModelAPI

## [1.1.0+1.21.1]

### Added

- Added spell tags for all Spells and specific Lute & Lyre Songs to trigger spell modifiers & passives in the Skill Tree Nodes correctly

### Changed

- Adopt Spell Engine 1.10 - Thanks Daedelus for the PR!

## [1.0.5+1.21.1]

### Changed

- Clean up some missing / unused sound effects
- Song of Celerity, Natures Minne, Vicious Mockery, Wardens Paean & Crescendo received new spell textures

## [1.0.4+1.21.1]

### Added

- added Music Disc's for all Bard Songs that can be found in a Pub in Villages
- Add Merchant Marker Mod Compat for the Luthier Villager

### Changed

- Armory & Arsenal Compat Equipment is now added to a separate Item Group

## [1.0.3.1+1.21.1]

-NEOFORGE ONLY: Remove fixed compatibility version for the dependencies in the "neoforge.mods.toml"

## [1.0.3+1.21.1]

**Balancing & Internal Changes:**

### Changed

- Adapt to Spell Engine 1.9.10+ API Changes
- Encore is now a charged spell, increases its range the longer you cast.

## [1.0.2+1.21.1]

### Fixed

- Fixed GH-Issue #11: Crash due to duplicate sound registration: 'bards_rpg:tale_of_the_dragonslayer_full'

## [1.0.1+1.21.1]

### Fixed

- fixed Bard Structure files being in the wrong folder, resulting in pub's not spawning in villages

## [1.0.0+1.21.1]

### Changed

**Bard's Official 1.21.1 Release Version**
- Improve Sound Effects of Weapon Skills (Bard Song's)
- Fix wrong Ruby Lute Smithing Recipe
- Wardens Paean received a new Status Effect Renderer
- Melody of the Meteor received new projectile models
- Fixed Datagen and Aeternium Recipes
- Fixed GH-Issue #10: Crash due to duplicate sound registration: 'bards_rpg:canticle_of_the_tides'

## [0.1.4+1.21.1]

### Changed

- Attempt to fix the issue with the Bard Sounds being played in the whole world
- lowered the volume of many spell SFX's
- Re-Balance some Spell Power Values for the Bard-Weapons
- Again increase the Attribute Modifier of the Wanderer's Minuet Spell
- Nerfed Healing & Damage spell-power coefficient for the Bard's Weapon Skills
- Buffed the max effect amplifier & amplifier cap spell-power coefficient of Bard Weapon Skills 
- This was done to shift the role of the Bard more towards the enchanter/buffer role
- Wardens Paean is now a instant cast spell

### Fixed

- Fixed wrong Upgrade Crystal for the Storyteller Armor Set
- Fix missing dependency in build gradle for Curseforge
- Fixed the issue for Troubadours Minuet & Astral Shooting spell description, where attribute modifiers showed -% values

## [0.1.3+1.21.1]

### Added

- Added a lot of Sound Effects for the Songs (Created by KaktusDoesMusic <3)

### Changed

- Wanderers Minuet now also buffs crit damage, crit chance was slightly buffed
- Song of Celerity Movement Speed was slightly buffed
- Extra Shot Arrows by Harp Crossbows cant be picked up
- Music Stand Block -> Fixed the hitbox not rotating
- Improved the Inventory GUI Model of the Harp Crossbows
- The Bard Songs (Weapon Skills) are now cast 10 seconds instead of 5 sec

### Fixed

- Fixed some Advancements not working
- Fixed some typos and grammar mistakes in spell descriptions
- Fixed the issue, that you could not re-bind weapon skills for some T5 Bard-Weapons
- Fixed Entertainers Garb Hat Clipping

## [0.1.2+1.21.1]

### Changed

- attempt to fix Server Crash on Neoforge
- Improve the Luthier Villager's behaviour

### Fixed

- Fix some spell translation mutators
- Fix Crescendo's Debuff scaling with Healing Spell power instead of Arcane

## [0.1.1+1.21.1]

### Added

- Added "Ranged Weapon API" as a new dependency!
- Added a new Luthier Villager, that sells Bard Equipment (Thx Florian for the Villager-Texture)
- Added new Structures that get injected in villages "Pub's" (Thx Florian for the Buildings)
- New Weapon Type: The Harp Crossbow

### Changed

- Update to Spell Engine Version 1.9.*
- [IMPORTANT] This mod is still in beta, so sound effects and visuals might be missing
- Nerfed Armies Paeon Damage multiplier
- Improved some Player Animations
- Moved the custom SpellThief Impact to the More RPG Library Mod 
- Heavily Improved the Custom SpellThief Impact for the SpellThief Lute and moved it to the more rpg lib
- 3x new Spells per Tier (Spell Expansion)
- T2 Vicious Mockery > Single Target Debuff, Damage and Taunt
- T3 Wardens Paean > Single Target: 1.Allies or the Caster are protected from the next  „effect_amplifier_amount“ applied harmful effects 2. The Enemies next „effect_amplifier_amount“ beneficial effects get removed
- T4 Crescendo > A piercing projectile that stuns and damages all hit enemies and increases incoming damage
- Common Lutes & Lyres can choose 1 Song of 2x Instrument Signature Songs (Weapon Skills)
- Rare Lutes & Lyres (T5 Weapons > Arsenal & LNE) can 1 Song from their own signature Song (Weapon Skill) + the instrument signature Weapon Songs
- They shoot two extra arrows, and provide arcane and healing spell power
- They're similar to the Archers Rapid Crossbow, pull a little bit slower and also have a 20% damage penalty because of the extra arrows and spell power

### Fixed

- Fixed issues with the Hat-Armor Models conflicting with the player's head model

## [0.1.0+1.21.1]

### Changed

- Bard's Beta!
- **DISCLAIMERS:**
- This does not work with Spell Engine 1.9 or higher. Please use 1.8.19+1.21.1 or lower
- There are some visual bugs with the Armor Set Hats
- Some sound effects might be missing
- Feel free to give feedback on my Discord
