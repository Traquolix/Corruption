package fr.traquolix.content.items.helpers;

import net.minestom.server.color.Color;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.metadata.MapMeta;

import java.util.List;

public class MapMetaBuilder {

    private int mapId = 0; // Default
    private int mapScaleDirection = 0; // Default
    private Color mapColor = new Color(255, 0, 0); // Default, change as needed
    private List<MapMeta.Decoration> decorations = List.of(); // Empty by default

    public MapMetaBuilder mapId(int mapId) {
        this.mapId = mapId;
        return this;
    }

    public MapMetaBuilder mapScaleDirection(int direction) {
        this.mapScaleDirection = direction;
        return this;
    }

    public MapMetaBuilder mapColor(Color color) {
        this.mapColor = color;
        return this;
    }

    public MapMetaBuilder decorations(List<MapMeta.Decoration> decorations) {
        this.decorations = decorations;
        return this;
    }

    public MapMeta build() {
        MapMeta.Builder metaBuilder = new MapMeta.Builder();

        metaBuilder.mapId(this.mapId)
                .mapScaleDirection(this.mapScaleDirection)
                .mapColor(this.mapColor)
                .decorations(this.decorations);

        return new MapMeta(metaBuilder.tagHandler());
    }

    public ItemStack applyTo(ItemStack itemStack) {
        return itemStack.withMeta(metaBuilder -> {
            if (metaBuilder instanceof MapMeta.Builder mapMetaBuilder) {
                mapMetaBuilder.mapId(this.mapId)
                        .mapScaleDirection(this.mapScaleDirection)
                        .mapColor(this.mapColor)
                        .decorations(this.decorations);
            }
        });
    }
}
