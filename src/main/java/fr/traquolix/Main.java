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
import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.npc.Dwarf;
import fr.traquolix.events.*;
import fr.traquolix.locations.cave.generator.CaveGenerator;
import fr.traquolix.mercenaries.JackTheRipper.JackTheRipper;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.quests.dwarf.*;
import fr.traquolix.quests.missions.ExploreAPlanet;
import fr.traquolix.time.TimeManager;
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
import net.minestom.server.network.packet.server.play.TeamsPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the Corruption Minecraft server.
 * This class initializes the server, registers items, blocks, events, and commands, and starts the server on a specified port.
 */
@Getter
public class Main {


    //TODO Bon début, mais utiliser un coffre pour afficher les différentes quêtes et leurs progressions en
    // fonction du NPC sur lequel on clique avec l'item en question serait mieux.
    // Oublier l'utilisation du tchat, c'est vraiment trop la galère + pas pratique + utilisé par les joueurs.
    // Faire des menus en gros. Avec un papier pour chaque quête, et l'étape à laquelle on est qui s'affiche direct
    // (+ un petit résumé, voir une possibilité de voir l'avancement si on clique dessus avec clic droit,
    // et clic gauche pour intéragir. Éventuellement on peut aussi dire que cliquer / hover sur la tête du personnage
    // permet de lui parler et d'avoir d'autres informations supplémentaires / complémentaires. A voir comment j'implémente ça pour que ce soit intéressant. Peut être faire circuler du texte au fur et à mesure qu'on clique, dans une boucle d'informations.
    // Une sorte d'impression de rumeurs, de commentaires, comme un vrai NPC avec plusieurs sous dialogues quoi.
    // Voir monde solo 3 pour exemple de ce que je veux faire en terme d'interface. Y'a moyen que ce soit intéressant d'un point de vue conversationnel / praticité.
    // Si t'as pas les requirements pour la quête, elle s'affiche même pas, ou alors s'affiche dans un autre menu, avec un message ?
    // Ou alors on peut faire un système de quêtes cachées, qui s'affichent que si on a les requirements, et qui sont pas dans le menu de base. Et on peut découvrir les requirements en discutant avec le NPC, en lisant le lore, et discutant autour etc.
    // On peut aussi s'inspirer des missions dans les mines sur hypixel kyblock qui ont un menu similaire

    //TODO Faire une statistique de compréhension ? Pour déchiffrer la langue au fur et à mesure ? Utiliser le carnet comme dans Tunic ?
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
 // TODO Faire un sytème de charme / et ou / talisman
    public static InstanceContainer instance;
    /**
     * Default constructor.
     */
    public Main() {}
 // TODO Faudra faire une documentation correcte et nettoyer le bordel dans les classes.
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

        // Create the instance
        instance = instanceManager.createInstanceContainer();

        // Init teams
        InitTeams();
        TimeManager timeManager = new TimeManager(instance);

        // Register items, blocks, events, and commands
        registerItems();
        registerBlocks();
        registerEvents(globalEventHandler);
        registerCommands();
        registerMercenaries();

        //Dwarf dwarf = (Dwarf) EntityRegistry.getInstance().getEntity(Dwarf.identifier);
        //dwarf.spawn(instance, new Pos(0, 40, 0));


        // Set the ChunkGenerator

        CaveGenerator caveGenerator = new CaveGenerator(instance);
        instance.setGenerator(caveGenerator);
        caveGenerator.populate();


        // Set render distance
        System.setProperty("minestom.chunk-view-distance", "16");

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

    private static void registerMercenaries() {
        new JackTheRipper();
    }

    private static void InitTeams() {
        //TODO Trouver un moyen pour qu'on puisse définir la distance de visibilité du nom. Pas envie qu'on puisse le voir à 700 blocs de là.
        MinecraftServer.getTeamManager()
                .createBuilder("players")
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .nameTagVisibility(TeamsPacket.NameTagVisibility.ALWAYS)
                .build();

        MinecraftServer.getTeamManager()
                .createBuilder("npc")
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .nameTagVisibility(TeamsPacket.NameTagVisibility.ALWAYS)
                .build();

        MinecraftServer.getTeamManager()
                .createBuilder("mobs")
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .nameTagVisibility(TeamsPacket.NameTagVisibility.ALWAYS)
                .build();
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
        MinecraftServer.getCommandManager().register(new RewardStashCommand());
        MinecraftServer.getCommandManager().register(new TimeStepCommand());
        MinecraftServer.getCommandManager().register(new TellTimeCommand());
        MinecraftServer.getCommandManager().register(new IncarnateCommand());
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
        new PlayerInventoryPreClickEvent(globalEventHandler);

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
        new MainMenuItem();

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
