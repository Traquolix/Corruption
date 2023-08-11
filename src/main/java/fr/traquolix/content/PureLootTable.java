package fr.traquolix.content;

import net.minestom.server.item.Material;

public enum PureLootTable {
// TODO Add check for enchantment + item type of the breaking item
    SNOW_LAYER("minecraft:snow", "minecraft:snowball", 1, 4),
    DEEPSLATE("minecraft:deepslate", "minecraft:cobbled_deepslate", 1, 1),
    ICE("minecraft:ice", "minecraft:ice", 0, 0);


    private final String block;
    private final String item;
    private final int min;
    private final int max;

    PureLootTable(String block, String item, int min, int max) {
        this.block = block;
        this.item = item;
        this.min = min;
        this.max = max;
    }

    public static String getResult(String value) {
        for (PureLootTable pureLootTable : PureLootTable.values()) {
            if (pureLootTable.block.equals(value)) {
                return pureLootTable.item;
            }
        }
        return value;
    }

    public static int getMaximum(String name) {
        for (PureLootTable pureLootTable : PureLootTable.values()) {
            if (pureLootTable.block.equals(name)) {
                return pureLootTable.max+1;
            }
        }
        return 2;
    }

    public static int getMinimum(String name) {
        for (PureLootTable pureLootTable : PureLootTable.values()) {
            if (pureLootTable.block.equals(name)) {
                return pureLootTable.min;
            }
        }
        return 1;
    }
}
