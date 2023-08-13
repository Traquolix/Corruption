package fr.traquolix.mercenaries;

import fr.traquolix.content.generalities.identifiers.Identifier;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;

@Getter
public abstract class AbstractMercenary implements Cloneable {
    protected Identifier identifier;
    protected AbstractMercenary() {
        initIdentifier();
        MercenaryRegistry.getInstance().register(this.identifier, this);
    }

    // TODO Nemesis system avec les camps de faction ?
    public abstract void initIdentifier();

    Ability specialAbility;

    public abstract PlayerSkin getSkin();

    public abstract Component getDisplayName();

    @Override
    public AbstractMercenary clone() {
        try {
            return (AbstractMercenary) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
