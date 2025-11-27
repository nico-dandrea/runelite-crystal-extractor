package com.crystal;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.GameObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class CrystalExtractorOverlay extends Overlay {

    private final CrystalExtractorPlugin plugin;
    private final CrystalExtractorConfig config;
    private final ModelOutlineRenderer outlineRenderer;

    @Inject
    CrystalExtractorOverlay(
        CrystalExtractorPlugin plugin,
        CrystalExtractorConfig config,
        ModelOutlineRenderer outlineRenderer
    ) {
        this.plugin = plugin;
        this.config = config;
        this.outlineRenderer = outlineRenderer;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isHighlightActive()) {
            return null;
        }

        GameObject target = plugin.getTarget();
        if (target == null) {
            return null;
        }

        outlineRenderer.drawOutline(
            target,
            config.outlineWidth(),
            config.outlineColor(),
            config.outlineFeather()
        );

        return null;
    }
}
