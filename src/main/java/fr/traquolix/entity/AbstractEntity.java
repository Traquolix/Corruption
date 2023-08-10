package fr.traquolix.entity;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.InstanceContainer;


@Getter
public abstract class AbstractEntity {
    Entity entity;
    protected String id;
    protected Component name;
    protected Identifier identifier;
    protected EntityType type;

    protected AbstractEntity(EntityType type) {
        initId();
        initName();
        initIdentifier();
        this.type = type;
        entity = new EntityCreature(type);
        entity.setTag(Identifier.getGlobalTag(), identifier.toString());
        entity.setCustomName(name);

        EntityRegistry.getInstance().registerEntity(this);
    }

    public abstract void initId();

    public abstract void initIdentifier();

    public abstract void initName();

    public abstract void onInteract(CPlayer player);

    public void spawn(InstanceContainer instance, Point point) {
        entity.setInstance(instance, point);
    }
}
