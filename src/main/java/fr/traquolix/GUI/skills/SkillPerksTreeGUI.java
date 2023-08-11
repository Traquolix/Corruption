package fr.traquolix.GUI.skills;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.skills.Skill;
import net.minestom.server.inventory.InventoryType;

public class SkillPerksTreeGUI extends AbstractGUI {

    // TODO Chaque skill a son arbre de compétences / caractéristiques qu'on peut améliorer avec des points qu'on gagne en montant en niveaux ?
    //  Mais j'aimerais bien que chaque individuelle caractéristique soit upgradable. Reste à trouver comment I guess ?
    //  Genre monter niveau 50 ou 100 certaines caractéristiques indiquerait qu'on a besoin de la masse de skill points ?
    //  On peut imaginer qu'on utilise un broyeur de ressource, qui les consomme pour les transformer en micro-points qui sont utilisables dans les caractéristiques pour les upgrader eux mêmes.
    //  C'est pas si mal, mais ça viens avec l'inconvénient de consommer la ressource pour augmenter ses caractéristiques. Aka, faire le choix entre un item et une amélioration générale et indéterminée.
    //  Ou alors c'est pas basé sur la consommation, mais la récolte. Chaque récolte d'un item te donne un certain nombre de points, et après, tu peux choisir de les dépenser dans les caractéristiques que tu veux.
    //  On fait le choix entre des items et des caractéristiques. Craft / vendre versus amélioration de soi.
    //  Mais on peut aussi reverse la conversation, éventuellement. Pouvoir output ses points en des ressources, une sorte de transmutation.
    Identifier identifier = new Identifier("gui", "skill_perks_tree");
 // TODO On peut dire que chaque perk dans un tier est un mini arbre de compétence.
    public SkillPerksTreeGUI(Skill skill) {
        super(InventoryType.CHEST_6_ROW, skill.name() + " Perks Tree");
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }
}
