# 3.0.0+1.20.1

> ### ⚠️ Read this before updating
>
> This release is a **major technical overhaul and is not backwards compatible.**
>
> - **Requires the matching Spell Engine and More RPG Library releases.** This version will not run on
>   Spell Engine **0.9.x**, and mods built against 0.9.x will not work alongside it.
> - **Update the whole set together.** Spell Engine, More RPG Library and every RPG Series mod must be on
>   matching versions. Mixing in an older add-on will break at startup or misbehave in play.
> - **Spell books must be re-obtained.** Spell books from an older world no longer carry valid
>   spell data. Re-craft them, or re-bind their spells at the Spell Binding Table.
>
> **Back up your world before updating.**

- Thanks to Daedelus for the PR!
- Ported to Minecraft 1.20.1 (Fabric + Forge 47). NeoForge is replaced by Forge on this line; the same
  Forge jar also loads on NeoForge 1.20.1.
- There is only one Bard Spell Book now, not two like on the older Bard 1.20.1 Version.
- Requires the matching 1.20.1 releases of More RPG Library (2.7.2), Spell Engine (1.10.5),
  Spell Power (1.6.0), Armor Model API (1.0.0), Ranged Weapon API (2.3.4) and Structure Pool API (1.2.1).
- The harp crossbow keeps its extra volley: Ranged Weapon API has a Forge build on this line, so the
  ranged content is available on both loaders.
- Every registry write goes through Forge's `RegisterEvent` window, so the mod also boots on Forge 47.0-47.3
  and on NeoForge 1.20.1, which never unlock the vanilla registries.
- The Storyteller armor is now always registered, so a server without Armory RPGs starts (its set bonus
  used to fail to load and abort the startup). Crafting it still requires Armory RPGs.

### Accepted 1.20.1 limitations

- 1.20.1 has no `jukebox_song` registry, so each Bard music disc carries its song, length and comparator
  output on the item itself instead of in a datapack file. The discs play and read exactly as before, but
  the ten `jukebox_song` JSON files are gone and can no longer be edited from a datapack.
- Hymn of the Golden Light no longer applies its `generic.max_absorption` bonus - that attribute does not
  exist before 1.20.5. The effect still grants the same absorption hearts, so the visible behaviour is
  unchanged.
- The harp crossbow's extra arrows do not inherit the weapon's enchantments (1.20.1 has no API for it), and
  a mob wielding one fires the vanilla single bolt. The main bolt is unaffected.
- Two block types in the village pubs do not exist on 1.20.1 and are substituted: tuff bricks become stone
  bricks, and waxed exposed copper grates become exposed copper. The pubs are structurally intact; the
  grates simply are not see-through.
- RPG armor is tagged `#minecraft:trimmable_armor` instead of the 1.21-only per-slot armor tags, and
  `#minecraft:enchantable/durability` is not shipped (1.20.1 uses the vanilla enchantment targets instead).
  Trims and Unbreaking work as before.
