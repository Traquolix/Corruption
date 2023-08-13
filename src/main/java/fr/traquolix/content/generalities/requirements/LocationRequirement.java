package fr.traquolix.content.generalities.requirements;

import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;

public class LocationRequirement implements Requirement {

    Point point1;
    Point point2;
    String locationName;
    public LocationRequirement(String locationName, Point point1, Point point2) {
        this.locationName = locationName;
        this.point1 = point1;
        this.point2 = point2;
    }
    @Override
    public boolean isMet(CPlayer player) {
        if (player.getPlayer().getPosition().x() > point1.x() && player.getPlayer().getPosition().x() < point2.x()) {
            if (player.getPlayer().getPosition().y() > point1.y() && player.getPlayer().getPosition().y() < point2.y()) {
                return player.getPlayer().getPosition().z() > point1.z() && player.getPlayer().getPosition().z() < point2.z();
            }
        }
        return false;
    }
    @Override
    public TextComponent getText() {
        return Component.text("→ Find your way to " + locationName)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD);
    }
}
