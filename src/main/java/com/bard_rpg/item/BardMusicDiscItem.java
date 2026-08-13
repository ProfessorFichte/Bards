package com.bard_rpg.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class BardMusicDiscItem extends MusicDiscItem {
    public static final String ARTIST = "KaktusDoesMusic";

    private final String title;

    public BardMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds, String title) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
        this.title = title;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(title + " - " + ARTIST).formatted(Formatting.GRAY));
    }
}
