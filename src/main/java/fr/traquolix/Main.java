package fr.traquolix;

import fr.traquolix.commands.*;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.CloudBlock;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.types.armor.helmets.FrostHelmetItem;
import fr.traquolix.content.items.types.misc.*;
import fr.traquolix.events.*;
import fr.traquolix.content.blocks.misc.IndestructibleBedrockBlock;
import fr.traquolix.content.blocks.misc.IndestructibleCoalBlock;
import fr.traquolix.content.blocks.misc.SandOfTimeBlock;
import fr.traquolix.content.blocks.ores.BloodstoneOreBlock;
import fr.traquolix.content.items.types.pickaxes.DwarvenPickaxe;
import fr.traquolix.content.items.types.swords.EndSword;
import fr.traquolix.locations.cave.generator.CaveGenerator;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.mojang.MojangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

/**
 * The main class of the Corruption Minecraft server.
 * This class initializes the server, registers items, blocks, events, and commands, and starts the server on a specified port.
 */
@Getter
public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

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

        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Set the ChunkGenerator
        CaveGenerator caveGenerator = new CaveGenerator(instanceContainer);
        instanceContainer.setGenerator(caveGenerator);
        caveGenerator.populate();

        // Set render distance
        System.setProperty("minestom.chunk-view-distance", "16");

        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 320, 0));
        });

        // Use Mojang uuid
        MojangAuth.init();

        // Set the skin provider
        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = PlayerSkin.fromUuid(String.valueOf(event.getPlayer().getUuid()));
            event.setSkin(skin);
        });

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
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
