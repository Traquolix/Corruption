package fr.traquolix.entity.npc.npc;

import fr.traquolix.GUI.rumorsGUI.rumors.DwarfRumors;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;

import java.util.List;

public class Dwarf extends NPCEntity  {


    public static final Identifier identifier = new Identifier(getGroup(), "1");
    public Dwarf(EntityType entityType) {
        super(entityType);
        initGui();
    }

    @Override
    public void initSkin() {
        super.skin = new PlayerSkin(
                "eyJ0aW1lc3RhbXAiOjE1ODYyNTE5ODQ5NDgsInByb2ZpbGVJZCI6IjczODJkZGZiZTQ4NTQ1NWM4MjVmOTAwZjg4ZmQzMmY4IiwicHJvZmlsZU5hbWUiOiJZYU9PUCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGYwNzkwZDk3NThiMTRjZTVmNDE3ODE2YzAxNTcyODc0NWE1ZDhhNDMwNDhhOWQwYTQxZDBlMTY4ZWQyN2Q2MCJ9fX0=",
                "Mr4wG/tAjFLtzCwOlO2zQcGADCX4goyWvGAFE7Ld794geQDgTxiY7zSl3xVmLaor0GAdHJOzZGm6fobKC8JA6OIoZTrcWBsW0kVUMiSRP+zQHfW/KQGiHAcAsKNEGnQnhl5yYPF5h/mrPDOpnww5m6BuF18nW4D9OPXSrnaDDqvY5WnUGKlMPPg+Q332OUzO8W1JW66gztpBDpbOEUQHWZqOO0Bs7ZZ5SGfBfDFu3Zvf140ZKvoMaYQA4RHwH5b5gbmEaStqR+NgFF5YYT9hC3JCxjjDJ1b9nnuqUQZezy2gp0uPWqc9ACpYJrhVryBbuYZeOkHUMCdyLe06ecRZeFVSEGPvHGhYswSUHSFBJINmcGm+3D372V2nRNWNOVbnnh1YM6vQOxZ/TlTOeRZMnLEabY3oM3dw0Uu/JcKA+tlXbmIMhR0qmGN0/7cU4z230uWGbh1VWsrlVIZ7/XOgYnpt0gDgCPz3ppcS01IXKCHfLoOeLew/cWGeoH1/2ODpSDx2Uw6fcP8/7Y5qjRQzRPjcgvfsXSrECagoNsmavAdgbJK6yopB40Z4PimluFAtC+OH5V/3mAJgyju5uMCJ6HojSQDxcMPLuNiCbiXsTuumvFmeZ4TCcs6ZcsoW8guXIdJ6Jc/FWUODVx303y38qUhsXSihcLXFlv+k315xryc=");
    }

    @Override
    public void initUsername() {
        super.username = "Leo the Dwarf";
        // TODO Being able to define the name of the NPC depending on the mercenary who is talking to him ?
        //  To reflect their relationship, or how they see him, while keeping the same name ? Might be a bit confusing.
    }

    @Override
    public void initDefaultDialogues() {
        // TODO Should have default dialogues depending on the mercenary who is talking to him. Maybe we could take into account the fact that he got his quest resolved previously ? I do not know. Might need a revamp if that's the case.
        defaultDialogues.addDialogue(0,
                List.of(
                        Component.text("Finally, some company. It's been a long time ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("since all my companions left the mines.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                10);
        defaultDialogues.addDialogue(0,
                List.of(
                    Component.text("It's been 5 years since I have not seen", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("the sun. But that blizzard won't stop.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)

                ),
                10);
        defaultDialogues.addDialogue(0,
                List.of(
                        Component.text("I'm wasted on cross-country. We dwarves", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("are natural sprinters. Very dangerous over", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("short distances.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)

                ),
                10);
        defaultDialogues.addDialogue(0,
                List.of(
                Component.text("I miss you Aleana. Where did you end up ?", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                2);
        defaultDialogues.addDialogue(0,
                List.of(
                Component.text("You human don't know what a Dwarf is capable of !", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                10);
        defaultDialogues.addDialogue(0,
                List.of(
                Component.text("Come back to me if you find anything interesting.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                10);
    }

    @Override
    public void initGui() {
        gui = new DwarfRumors();
    }

    @Override
    public void initId() {
        id = "1";
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void initName() {
        name = Component.text("Leo the Dwarf").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.DARK_GREEN);
    }

    @Override
    public void onInteract(CPlayer player) {

        gui.open(player);
        /*
        if (!quest.step(player)) {
            player.sendMessage(getDefaultMessage());
        }

         */
    }
}
