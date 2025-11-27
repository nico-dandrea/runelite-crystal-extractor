package com.example.crystal;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("crystalextractor")
public interface CrystalExtractorConfig extends Config
{
    @ConfigItem(
        keyName = "outlineColor",
        name = "Outline color",
        description = "Color used to outline the Crystal extractor"
    )
    default Color outlineColor()
    {
        return Color.CYAN;
    }

    @ConfigItem(
        keyName = "outlineWidth",
        name = "Outline width",
        description = "Stroke width for the outline"
    )
    default int outlineWidth()
    {
        return 4;
    }

    @ConfigItem(
        keyName = "outlineFeather",
        name = "Outline feather",
        description = "Feather (glow) strength for the outline"
    )
    default int outlineFeather()
    {
        return 2;
    }
}
