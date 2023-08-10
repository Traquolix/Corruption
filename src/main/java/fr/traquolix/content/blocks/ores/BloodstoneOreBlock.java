package fr.traquolix.content.blocks.ores;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.types.misc.BloodstoneOreItem;
import fr.traquolix.content.items.types.pickaxes.DwarvenPickaxe;
import fr.traquolix.content.generalities.requirements.ItemInMainHandRequirement;
import fr.traquolix.player.CPlayer;
import fr.traquolix.skills.Skill;
import net.minestom.server.instance.block.Block;

/**
 * The BloodstoneOreBlock class represents a block of Bloodstone Ore in the game world.
 */
public class BloodstoneOreBlock extends AbstractBlock {

    /**
     * The unique identifier of the Bloodstone Ore block.
     */
    final public static Identifier identifier = new Identifier("block", "bloodstone_ore");

    /**
     * The item that the block drops when broken.
     */
    final public AbstractItem drop = ItemRegistry.getInstance().getItem(BloodstoneOreItem.identifier);

    /**
     * Constructs a BloodstoneOreBlock.
     */
    public BloodstoneOreBlock() {
        super(identifier, Block.RED_STAINED_GLASS);
    }

    /**
     * Initializes the requirements for breaking the Bloodstone Ore block.
     * The block requires a Dwarven Pickaxe to be held in the player's main hand to be broken.
     */
    @Override
    public void initRequirements() {
        requirements.add(new ItemInMainHandRequirement(ItemRegistry.getInstance().getItem(DwarvenPickaxe.identifier)));
    }

    /**
     * Called when the Bloodstone Ore block is broken by a player.
     * The player gains mining experience and receives the drop item in their inventory.
     *
     * @param player The player who broke the block.
     */
    @Override
    public void broke(CPlayer player) {
        player.getPlayer().getInventory().addItemStack(
                drop.buildItemStack()
        );
    }

    @Override
    public void brokeNatural(CPlayer player) {
        player.getPlayer().getInventory().addItemStack(
                drop.buildItemStack()
        );
        player.gainExperience(Skill.MINING, 10);
    }

}
