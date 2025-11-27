package com.example.crystal;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "Crystal Extractor Helper",
    description = "Highlights the Crystal extractor after a mote harvest until you interact with it.",
    tags = { "crystal", "extractor" }
)
public class CrystalExtractorPlugin extends Plugin {

    private static final String TRIGGER_MESSAGE =
        "Your crystal extractor has harvested a crystal mote.";
    private static final String OBJECT_NAME = "Crystal extractor";

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CrystalExtractorOverlay overlay;

    @Inject
    private CrystalExtractorConfig config;

    private GameObject target;
    private boolean highlightActive;

    @Provides
    CrystalExtractorConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CrystalExtractorConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
        highlightActive = false;
        target = null;
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        highlightActive = false;
        target = null;
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (
            event.getType() != ChatMessageType.GAMEMESSAGE &&
            event.getType() != ChatMessageType.SPAM
        ) {
            return;
        }

        String message = net.runelite.client.util.Text.removeTags(
            event.getMessage()
        );
        if (message.equals(TRIGGER_MESSAGE)) {
            highlightActive = true;
            target = findNearestExtractor();
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        if (!highlightActive || target != null) {
            return;
        }
        target = findNearestExtractor();
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        String targetName = net.runelite.client.util.Text.removeTags(
            event.getMenuTarget()
        );
        if (
            highlightActive &&
            targetName.equalsIgnoreCase(OBJECT_NAME) &&
            event.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION
        ) {
            clearHighlight();
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        if (target != null && event.getGameObject() == target) {
            clearHighlight();
        }
    }

    public GameObject getTarget() {
        return target;
    }

    public boolean isHighlightActive() {
        return highlightActive;
    }

    private void clearHighlight() {
        highlightActive = false;
        target = null;
    }

    private GameObject findNearestExtractor() {
        if (client.getLocalPlayer() == null) {
            return null;
        }

        WorldPoint playerLoc = client.getLocalPlayer().getWorldLocation();
        GameObject closest = null;
        int bestDist = Integer.MAX_VALUE;

        for (int z = 0; z < 4; z++) {
            Tile[][] plane = client.getScene().getTiles()[z];
            for (int x = 0; x < plane.length; x++) {
                for (int y = 0; y < plane[x].length; y++) {
                    Tile tile = plane[x][y];
                    if (tile == null) {
                        continue;
                    }

                    for (GameObject obj : tile.getGameObjects()) {
                        if (obj == null) {
                            continue;
                        }

                        ObjectComposition composition =
                            client.getObjectDefinition(obj.getId());
                        if (composition == null) {
                            continue;
                        }

                        String name = composition.getName();
                        if (
                            name == null || !name.equalsIgnoreCase(OBJECT_NAME)
                        ) {
                            continue;
                        }

                        int dist = playerLoc.distanceTo(obj.getWorldLocation());
                        if (dist < bestDist) {
                            bestDist = dist;
                            closest = obj;
                        }
                    }
                }
            }
        }
        return closest;
    }
}
