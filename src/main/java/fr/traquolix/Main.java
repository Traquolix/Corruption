package fr.traquolix;

import fr.traquolix.commands.*;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.CloudBlock;
import fr.traquolix.content.blocks.misc.IndestructibleBedrockBlock;
import fr.traquolix.content.blocks.misc.IndestructibleCoalBlock;
import fr.traquolix.content.blocks.misc.SandOfTimeBlock;
import fr.traquolix.content.blocks.ores.BloodstoneOreBlock;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.types.armor.helmets.FrostHelmetItem;
import fr.traquolix.content.items.types.misc.*;
import fr.traquolix.content.items.types.pickaxes.DwarvenPickaxe;
import fr.traquolix.content.items.types.swords.EndSword;
import fr.traquolix.entity.npc.npc.TutorialGuy;
import fr.traquolix.events.*;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.quests.tutorial.Tutorial2Quest;
import fr.traquolix.quests.tutorial.TutorialQuest;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the Corruption Minecraft server.
 * This class initializes the server, registers items, blocks, events, and commands, and starts the server on a specified port.
 */
@Getter
public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static InstanceContainer instance;
    /**
     * Default constructor.
     */
    public Main() {}

    /**
     * The main method to start the Corruption Minecraft server.
     * Initializes the server, registers items, blocks, events, and commands, and starts the server on a specified port.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();


        // Register items, blocks, events, and commands
        registerItems();
        registerBlocks();
        registerEvents(globalEventHandler);
        registerCommands();
        registerQuests();

        // Create the instance
        instance = instanceManager.createInstanceContainer();
        // Set the ChunkGenerator

        //CaveGenerator caveGenerator = new CaveGenerator(instance);
        //instance.setGenerator(caveGenerator);
        //caveGenerator.populate();

        instance.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.STONE));

        // Set render distance
        System.setProperty("minestom.chunk-view-distance", "16");

        // Use Mojang uuid
        MojangAuth.init();

        // Set the skin provider
        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = PlayerSkin.fromUuid(String.valueOf(event.getPlayer().getUuid()));
            event.setSkin(skin);
        });

        TutorialGuy tutorialGuy = new TutorialGuy(EntityType.VILLAGER);
        tutorialGuy.spawn(instance, new Pos(0, 40, 0));


        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
    }

    private static void registerQuests() {
        new TutorialQuest(0);
        new Tutorial2Quest(1);

        logger.info("[Registry] - " + QuestRegistry.getInstance().getSize() + " quests registered.");

    }

    /**
     * Registers custom commands for the Corruption server.
     */
    private static void registerCommands() {
        MinecraftServer.getCommandManager().register(new GetItemCommand());
        MinecraftServer.getCommandManager().register(new GetStatsCommand());
        MinecraftServer.getCommandManager().register(new EffectCommand());
        MinecraftServer.getCommandManager().register(new GetXPCommand());
        MinecraftServer.getCommandManager().register(new DwarfCabinCommand());
        MinecraftServer.getCommandManager().register(new FrenzyReceptacleCommand());
        MinecraftServer.getCommandManager().register(new ObservatoryCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        MinecraftServer.getCommandManager().register(new GetEquipmentCommand());
        MinecraftServer.getCommandManager().register(new QuestStepCommand());
        MinecraftServer.getCommandManager().register(new SetQuestCommand());
        logger.info("[Registry] - " + MinecraftServer.getCommandManager().getCommands().size() + " commands registered.");
    }

    /**
     * Registers custom event handlers for the Corruption server.
     *
     * @param globalEventHandler The global event handler to register event listeners.
     */
    private static void registerEvents(GlobalEventHandler globalEventHandler) {

        new EntityChangeGearEvent(globalEventHandler);
        new PlayerLoginRegisterEvent(globalEventHandler);
        new PlayerUseGearEvent(globalEventHandler);
        new PlayerCustomBlockBreakEvent(globalEventHandler);
        new PlayerPlaceCustomBlockEvent(globalEventHandler);
        new PlayerInteractWithNPCsEvent(globalEventHandler);

    }

    /**
     * Registers custom items for the Corruption server.
     */
    private static void registerItems() {

        // -- Special
        //new MapItem(); // Temporary disabled because if eats all the RAM. We will see later.

        // -- Pickaxes
        new DwarvenPickaxe();

        // -- Swords
        new EndSword();

        // -- Misc
        new StoneBlockItem();
        new CloudBlockItem();
        new AirItem();

        // -- Minerals
        new BloodstoneItem();
        new BloodstoneOreItem();
        new CelestiteItem();

        // -- Armor
        // - Helmets
        new FrostHelmetItem();

        logger.info("[Registry] - " + ItemRegistry.getInstance().getSize() + " items registered.");
    }

    /**
     * Registers custom blocks for the Corruption server.
     */
    private static void registerBlocks() {
        // Ores
        new BloodstoneOreBlock();

        // Misc
        new IndestructibleBedrockBlock();
        new IndestructibleCoalBlock();
        new SandOfTimeBlock();
        new CloudBlock();

        logger.info("[Registry] - " + BlockRegistry.getInstance().getSize() + " blocks registered.");
    }
}
