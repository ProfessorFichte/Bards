package com.bards.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/// 1.20.1 has no `jukebox_song` data registry: the song is baked into the item itself
/// (`MusicDiscItem(comparatorOutput, sound, settings, lengthInSeconds)`), which is what the
/// `data/bards_rpg/jukebox_song/*.json` files carried on 1.21.
public class BardMusicDiscItem extends MusicDiscItem {
    public static final String ARTIST = "KaktusDoesMusic";

    private final String title;
    private final String descriptionKey;

    public BardMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds,
                             String title, String descriptionKey) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
        this.title = title;
        this.descriptionKey = descriptionKey;
    }

    /// Keeps the shipped `jukebox_song.bards_rpg.<name>` translation keys in use: on 1.20.1 the jukebox
    /// "Now playing" overlay renders `getDescription()`, and vanilla's default would look for
    /// `item.bards_rpg.<name>.desc`, which this mod never had.
    @Override
    public MutableText getDescription() {
        return Text.translatable(descriptionKey);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(title + " - " + ARTIST).formatted(Formatting.GRAY));
    }
}
