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

    @Override
    public MutableText getDescription() {
        return Text.translatable(descriptionKey);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(title + " - " + ARTIST).formatted(Formatting.GRAY));
    }
}
