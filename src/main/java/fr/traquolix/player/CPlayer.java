package fr.traquolix.player;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.types.misc.AirItem;
import fr.traquolix.content.items.types.misc.MainMenuItem;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.rewards.PersonalRewardRegistry;
import fr.traquolix.skills.AbstractSkill;
import fr.traquolix.skills.Skill;
import fr.traquolix.skills.farming.PureFarmingGainRegistry;
import fr.traquolix.skills.mining.PureMiningGainRegistry;
import fr.traquolix.stats.AbstractStat;
import fr.traquolix.stats.Stat;
import fr.traquolix.utils.Utils;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.notifications.Notification;
import net.minestom.server.advancements.notifications.NotificationCenter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static fr.traquolix.Main.logger;

/**
 * Custom player wrapper class to manage player-related data and functionality in the game.
 */
@Getter
public class CPlayer {

    final Equipment equipment = new Equipment();
    final Player player;
    final ConcurrentMap<Skill, AbstractSkill> skills = new ConcurrentHashMap<>();
    final ConcurrentMap<Stat, AbstractStat> stats = new ConcurrentHashMap<>();
    // TODO : Quand on ouvre un dialogue avec un NPC, toutes les quêtes qu'il a de disponible sont ajoutées aux quêtes découvertes.
    // TODO : Seules les quêtes découvertes sont affichées au rewardStash, le reste est masqué, mais généré quand même.
    final ConcurrentLinkedQueue<Integer> discoveredQuests = new ConcurrentLinkedQueue<>();
    final ConcurrentMap<Integer, Integer> currentQuests = new ConcurrentHashMap<>();
    final ConcurrentLinkedQueue<Integer> completedQuests = new ConcurrentLinkedQueue<>();

    double currentMana = 0;
    final int manaRegenSpeed = 500; // In milliseconds
    final ConcurrentMap<Stat, AbstractStat> bonusStats = new ConcurrentHashMap<>();
    Task manaRegenTask;
    PersonalRewardRegistry personalRewardRegistry = new PersonalRewardRegistry();
    ConcurrentMap<Flag, Boolean> configFlags = new ConcurrentHashMap<>();

    /**
     * Constructor to create a new CPlayer instance for the given Minestom Player entity.
     *
     * @param player The Minestom Player entity to associate with the CPlayer.
     */
    public CPlayer(Player player) {
        this.player = player;

        // Register the player with the PlayerRegistry
        PlayerRegistry.getInstance().registerPlayer(player.getUuid(), this);
        logger.info("[REGISTRY] - " + player.getUsername());


        //TODO Test values
        configFlags.put(Flag.REWARD_STASH_SHOW_CLAIMED, false);
        configFlags.put(Flag.RUMORS_SHOW_COMPLETED, true);

        // Add Main menu
        player.getInventory().setItemStack(8, ItemRegistry.getInstance().getItem(MainMenuItem.identifier).buildItemStack());

        // Load skills, stats, and set default values
        loadSkills();
        loadBaseStats();
        loadBonusStats();
        setDefaultStats();
        loadRewardRegistry();
    }

    private void loadRewardRegistry() {
        QuestRegistry.getInstance().getItemMap().forEach((id, quest) -> {
            personalRewardRegistry.registerAllRewards(id, quest.getRewards());
        });
        // Other sources of rewards...
    }

    /**
     * Set default values for base stats.
     */
    private void setDefaultStats() {
        setBaseStatValue(Stat.HEALTH, 100);
        setBaseStatValue(Stat.DAMAGE, 5);
        setBaseStatValue(Stat.MANA, 100);
        setBaseStatValue(Stat.DEFENSE, 0);

        currentMana = getCurrentStatValue(Stat.MANA);
    }

    /**
     * Load bonus stats with initial values.
     */
    private void loadBonusStats() {
        bonusStats.put(Stat.HEALTH, Stat.HEALTH.createNewInstance());
        bonusStats.put(Stat.DAMAGE, Stat.DAMAGE.createNewInstance());
        bonusStats.put(Stat.MANA, Stat.MANA.createNewInstance());
        bonusStats.put(Stat.DEFENSE, Stat.DEFENSE.createNewInstance());
    }

    /**
     * Load base stats with initial values.
     */
    private void loadBaseStats() {
        stats.put(Stat.HEALTH, Stat.HEALTH.createNewInstance());
        stats.put(Stat.DAMAGE, Stat.DAMAGE.createNewInstance());
        stats.put(Stat.MANA, Stat.MANA.createNewInstance());
        stats.put(Stat.DEFENSE, Stat.DEFENSE.createNewInstance());
    }

    /**
     * Load skills and create instances for each.
     */
    private void loadSkills() {
        skills.put(Skill.MINING, Skill.MINING.getSkillSupplier().get());
        skills.put(Skill.FARMING, Skill.FARMING.getSkillSupplier().get());
    }

    /**
     * Gain experience in a specific skill and possibly level up.
     *
     * @param skill  The skill in which to gain experience.
     * @param amount The amount of experience points to gain.
     */
    public void gainExperience(Skill skill, double amount) {
        if (skills.get(skill).gainExperience(amount)) {
            int currentLevel = getLevel(skill);
            int previousLevel = currentLevel-1;

            Notification notification = new Notification(Component.text(Utils.capitalizeFirstLetter(skill.name()), NamedTextColor.DARK_AQUA).append(Component.text(" " + Utils.toRomanNumeral(previousLevel) + " -> ", NamedTextColor.DARK_GRAY)).append(Component.text(Utils.toRomanNumeral(currentLevel))), FrameType.GOAL, skill.getRepresentation());
            NotificationCenter.send(notification, player);
            if (currentLevel % 25 == 0) player.playSound(Sound.sound(Key.key("ui.toast.challenge_complete"), Sound.Source.MASTER, 1f, 0f));
            else player.playSound(Sound.sound(Key.key("ui.toast.challenge_complete"), Sound.Source.MASTER, 1f, 1.5f));
        }
    }


    /**
     * Get the experience points of a specific skill.
     *
     * @param skill The skill for which to get the experience points.
     * @return The experience points of the specified skill.
     */
    public double getExperience(Skill skill) {
        return skills.get(skill).getExperience();
    }

    /**
     * Get the level of a specific skill.
     *
     * @param skill The skill for which to get the level.
     * @return The level of the specified skill.
     */
    public int getLevel(Skill skill) {
        return skills.get(skill).getLevel();
    }

    /**
     * Get the current value of a specific stat.
     *
     * @param stat The stat for which to get the current value.
     * @return The current value of the specified stat.
     */
    public double getCurrentStatValue(Stat stat) {
        return stats.get(stat).getValue();
    }

    /**
     * Set the base value of a specific stat.
     *
     * @param stat  The stat for which to set the base value.
     * @param value The value to set as the base value for the specified stat.
     */
    public void setBaseStatValue(Stat stat, double value) {
        stats.get(stat).setValue(value);
    }

    /**
     * Add a value to the base value of a specific stat.
     *
     * @param stat  The stat for which to add the value.
     * @param value The value to add to the base value of the specified stat.
     */
    public void addBaseStatValue(Stat stat, double value) {
        stats.get(stat).setValue(stats.get(stat).getValue() + value);
    }

    /**
     * Remove a value from the base value of a specific stat.
     *
     * @param stat  The stat for which to remove the value.
     * @param value The value to remove from the base value of the specified stat.
     */
    public void removeBaseStatValue(Stat stat, double value) {
        stats.get(stat).setValue(stats.get(stat).getValue() - value);
    }

    /**
     * Get the bonus stat instance associated with a specific stat.
     *
     * @param stat The stat for which to get the bonus stat.
     * @return The bonus stat instance associated with the specified stat.
     */
    public AbstractStat getBonusStat(Stat stat) {
        return bonusStats.get(stat);
    }

    /**
     * Check if the player has completed a specific quest.json.
     *
     * @param questId The identifier of the quest.json to check completion.
     * @return true if the player has completed the quest.json, false otherwise.
     */
    public boolean hasCompletedQuest(int questId) {
        return completedQuests.contains(questId);
    }

    /**
     * Reset all bonus stats to their default values (0).
     */
    public void resetBonusStats() {
        bonusStats.forEach((identifier, stat) -> stat.setValue(0));
    }

    /**
     * Add a value to the bonus value of a specific stat.
     *
     * @param stat  The stat for which to add the value.
     * @param value The value to add to the bonus value of the specified stat.
     */
    public void addBonusStatValue(Stat stat, Integer value) {
        bonusStats.get(stat).setValue(bonusStats.get(stat).getValue() + value);
    }

    /**
     * Get a map containing the current values of all stats, including base stats and bonus stats.
     *
     * @return A map containing the current values of all stats.
     */
    public Map<Stat, Integer> getCurrentStats() {
        Map<Stat, Integer> currentStats = new HashMap<>();
        for (Stat stat : Stat.values()) {
            currentStats.put(stat, (int) (stats.get(stat).getValue() + bonusStats.get(stat).getValue()));
        }
        return currentStats;
    }

    /**
     * Get the item in the player's main hand.
     *
     * @return The item in the player's main hand as an ItemStack.
     */
    public ItemStack getItemInMainHand() {
        return player.getInventory().getItemInMainHand();
    }

    /**
     * Start mana regeneration for the player.
     */
    public void startManaRegen() {
        // Stop any existing mana recovery task before starting a new one.
        if (manaRegenTask != null) {
            manaRegenTask.cancel();
        }

        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        // Submit a new task for mana regeneration
        manaRegenTask = scheduler.submitTask(() -> {
            if (currentMana < stats.get(Stat.MANA).getValue()) {
                currentMana++;
                player.sendMessage(Component.text(currentMana + " / " + stats.get(Stat.MANA).getValue()));
                return TaskSchedule.millis(manaRegenSpeed); // Schedule next regeneration after 500 ms.
            } else {
                return TaskSchedule.stop(); // Stop the task when mana has been fully regenerated.
            }
        });
    }

    /**
     * Consume mana for the player.
     *
     * @param manaCost The amount of mana to consume.
     * @return true if the player had enough mana and it was consumed, false otherwise.
     */
    public boolean consumeMana(int manaCost) {
        if (currentMana < manaCost) {
            return false;
        }
        currentMana -= manaCost;

        // Start the regeneration task after consuming mana.
        startManaRegen();
        return true;
    }

    /**
     * Break a block and gain experience based on the block type (mining or farming).
     *
     * @param block The block to break.
     * @return true if the block was successfully broken and experience gained, false otherwise.
     */
    public boolean brokeBlock(Block block) {
        if (!canUseItem()) return false;
        if (block.hasTag(Identifier.getNaturalTag())) {
            if (block.getTag(Identifier.getNaturalTag()).equals(false)) {
                return true;
            }
        }

        // Check the block type and gain experience accordingly
        for (PureMiningGainRegistry value : PureMiningGainRegistry.values()) {
            if (Objects.equals(value.getName(), block.name())) {
                gainExperience(Skill.MINING, value.getXp());
            }
        }
        for (PureFarmingGainRegistry value : PureFarmingGainRegistry.values()) {
            if (Objects.equals(value.getName(), block.name())) {
                gainExperience(Skill.FARMING, value.getXp());
            }
        }
        Random random = new Random();
        getPlayer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 0.3f, random.nextFloat(1.85f, 2f)));
        return true;
    }

    /**
     * Determines if the player can use the item currently held in their main hand.
     *
     * @return {@code true} if the player meets all the requirements to use the item, otherwise {@code false}.
     */
    private boolean canUseItem() {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        // If there's no item in the main hand or the item is 'air', player can use it
        if (itemInMainHand.isAir()) return true;

        // Ensure the item has the required tag
        if (!itemInMainHand.hasTag(Identifier.getGlobalTag())) return false;

        String identifierString = itemInMainHand.getTag(Identifier.getGlobalTag());
        Identifier identifier = new Identifier(identifierString);
        AbstractItem item = ItemRegistry.getInstance().getItem(identifier);

        // If the item doesn't exist in the registry, it can't be used
        if (item == null) return false;

        // Ensure all requirements are met for the player to use the item
        return item.getRequirements().stream().allMatch(requirement -> requirement.isMet(this));
    }

    public PlayerConnection getPlayerConnection() {
        return player.getPlayerConnection();
    }

    public void sendMessage(TextComponent text) {
        player.sendMessage(text);
    }

    public String getUsername() {
        return player.getUsername();
    }

    public void giveItem(ItemStack itemStack) {
        player.getInventory().addItemStack(itemStack);
    }

    public Pos getPosition() {
        return player.getPosition();
    }

    public void addItemInMainHandToEquipment(AbstractItem item) {
        equipment.setItemInMainHand(item);
    }
    public void addItemInOffHandToEquipment(AbstractItem item) {
        equipment.setItemInOffHand(item);
    }

    public void addArmor(int slot, AbstractItem item) {
        equipment.set(slot, item);
    }
    public void removeArmor(int slot) {
        equipment.set(slot, ItemRegistry.getInstance().getItem(AirItem.identifier));
    }

    public void refreshBonuses() {
        resetBonusStats();
        equipment.getAllItems().forEach(item -> item.getBonuses().forEach((stat, value) -> addBonusStatValue(stat, value)));
    }

    public void resetEquipment() {
        equipment.reset();
    }

    public void removeItemInMainHandFromEquipment() {
        equipment.setItemInMainHand(ItemRegistry.getInstance().getItem(AirItem.identifier));
    }

    public void removeItemInOffHandFromEquipment() {
        equipment.setItemInOffHand(ItemRegistry.getInstance().getItem(AirItem.identifier));
    }

    public void refreshEquipment() {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (!itemInMainHand.isAir()) {
            String identifier = itemInMainHand.getTag(Identifier.getGlobalTag());
            equipment.setItemInMainHand(ItemRegistry.getInstance().getItem(new Identifier(identifier)));
        }

        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        if (!itemInOffHand.isAir()) {
            String identifier = itemInOffHand.getTag(Identifier.getGlobalTag());
            equipment.setItemInOffHand(ItemRegistry.getInstance().getItem(new Identifier(identifier)));
        }

        ItemStack helmet = player.getInventory().getHelmet();
        if (!helmet.isAir()) {
            String identifier = helmet.getTag(Identifier.getGlobalTag());
            equipment.set(41, ItemRegistry.getInstance().getItem(new Identifier(identifier)));
        }

        ItemStack chestplate = player.getInventory().getChestplate();
        if (!chestplate.isAir()) {
            String identifier = chestplate.getTag(Identifier.getGlobalTag());
            equipment.set(42, ItemRegistry.getInstance().getItem(new Identifier(identifier)));
        }

        ItemStack leggings = player.getInventory().getLeggings();
        if (!leggings.isAir()) {
            String identifier = leggings.getTag(Identifier.getGlobalTag());
            equipment.set(43, ItemRegistry.getInstance().getItem(new Identifier(identifier)));
        }

        ItemStack boots = player.getInventory().getBoots();
        if (!boots.isAir()) {
            String identifier = boots.getTag(Identifier.getGlobalTag());
            equipment.set(44, ItemRegistry.getInstance().getItem(new Identifier(identifier)));
        }

        refreshBonuses();
    }

    public void addItemStack(AbstractItem item) {
        player.getInventory().addItemStack(item.buildItemStack());
    }

    public void addCurrentQuests(int id, int step) {
        currentQuests.put(id, step);
    }

    public void removeCurrentQuests(AbstractQuest abstractQuest) {
        currentQuests.remove(abstractQuest.getId());
    }

    public void addCompletedQuests(AbstractQuest abstractQuest) {
        completedQuests.add(abstractQuest.getId());
    }

    public @NotNull UUID getUuid() {
        return player.getUuid();
    }

    public void openGui(Inventory inventory) {
        player.openInventory(inventory);
    }

    public void closeGui() {
        player.closeInventory();
    }

    public void toggle(Flag flag) {
        configFlags.put(flag, !configFlags.get(flag));
    }

    public boolean hasFlag(Flag flag) {
        return configFlags.get(flag);
    }

    public void resetPerks(Skill skill, int page) {
        System.out.println("IMPLEMENTE MOI PD");
    }
}
