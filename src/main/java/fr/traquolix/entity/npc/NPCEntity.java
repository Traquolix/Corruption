package fr.traquolix.entity.npc;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestEntityRegistry;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class NPCEntity extends AbstractEntity {
// TODO Introduire le système de timeline (en fonction de quand on va le voir, il propose d'autres quêtes).
// TODO Créer un event qu'on peut appeler régulièrement pour "step" le temps.
    protected String username;
    protected AbstractGUI gui;
    @Getter
    protected PlayerSkin skin;
    @Getter
    protected DialogueSelector defaultDialogues = new DialogueSelector(); // And their percentage of apparition.
    protected NPCEntity(EntityType type) {
        super(type);
        initDefaultDialogues();
        initUsername();
        initSkin();
        MinecraftServer.getTeamManager().getTeam("npc").addMember(username);
        QuestEntityRegistry.getInstance().registerEntity(this);
    }

    public abstract void initSkin();

    public abstract void initUsername();

    public void initEntity(CPlayer player) {
        updateNewViewer(player);
    }

    private void updateNewViewer(CPlayer player) {

        String skinTexture = skin.textures();
        String skinSignature = skin.signature();


        var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();
        if (skinTexture != null && skinSignature != null) {
            properties.add(new PlayerInfoUpdatePacket.Property("textures", skinTexture, skinSignature));
        }
        var entry = new PlayerInfoUpdatePacket.Entry(getEntity().getUuid(), username, properties, false,
                0, GameMode.SURVIVAL, null, null);
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));

        // Spawn the player entity
        getEntity().updateNewViewer(player.getPlayer());

        // Enable skin layers
        player.sendPackets(new EntityMetaDataPacket(getEntity().getEntityId(), Map.of(17, Metadata.Byte((byte) 127))));
        getEntity().setInvisible(false);
    }

    public abstract void initDefaultDialogues();
    public abstract void initGui();
    public abstract void onInteract(CPlayer player);
    public static String getGroup() {
        return "npc";
    }
}
