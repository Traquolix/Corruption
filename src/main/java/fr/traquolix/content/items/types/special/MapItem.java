package fr.traquolix.content.items.types.special;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.Rarity;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.items.helpers.RotatingMapRenderer;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.MapMeta;
import net.minestom.server.map.framebuffers.Graphics2DFramebuffer;
import net.minestom.server.network.packet.server.play.MapDataPacket;

import java.time.Instant;

public class MapItem extends AbstractItem {

    // 1. Corrected identifier
    public static Identifier identifier = new Identifier("item", "map");
    private final int MAP_ID = 0;

    private final RotatingMapRenderer mapRenderer = new RotatingMapRenderer();


    // 2. Provide a constructor
    public MapItem() {
        super(identifier,
                Component.text("Map Item"),
                ItemType.ITEM, // Assuming you have an enum value called SPECIAL in ItemType
                Material.FILLED_MAP,
                Rarity.COMMON, // Assuming you want a default rarity of COMMON for MapItem
                "This is a special map item.",
                Instant.now(),
                Component.text("Server"), // Default creator
                false, // Not reforgeable
                Component.text("Use this to view the map."),
                0, // No cooldown
                0); // No mana cost
    }

    @Override
    public void initRequirements() {
        // No requirements for this item
    }

    @Override
    public void initBonuses() {
        // No stat bonuses for this item
    }

    @Override
    public void use(CPlayer cplayer, AbstractItem item) {

        // Update the renderer to capture the current world view and rotate it.
        mapRenderer.renderToMap(cplayer.getPlayer(), cplayer.getPosition());

        // Now, use the framebuffer from the renderer to send the updated map view.
        Graphics2DFramebuffer framebuffer = mapRenderer.getFramebuffer();
        final MapDataPacket packet = framebuffer.preparePacket(MAP_ID);

        cplayer.getPlayer().sendPacket(packet);
    }


    @Override
    public ItemStack buildItemStack() {

        ItemStack itemStack = super.buildItemStack();

        return itemStack.withMeta(MapMeta.class, meta -> meta.mapId(MAP_ID)); // Applies the metadata to the item stack.

    }


}

