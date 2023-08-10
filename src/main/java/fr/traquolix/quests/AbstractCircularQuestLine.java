package fr.traquolix.quests;

import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


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
@Getter
public abstract class AbstractCircularQuestLine {
    ConcurrentLinkedQueue<AbstractQuest> quests = new ConcurrentLinkedQueue<>();
    protected Component questLineName;
    AbstractQuest lastQuest;
    boolean flaggedMessage = false;
    protected AbstractCircularQuestLine() {
        initQuests();
        initName();
    }

    public abstract void initQuests();
    public abstract void initName();

    public boolean step(CPlayer player, AbstractQuest quest) {
        if (quest.getCurrentStep()-1 == quest.getSteps().size()) {
            quest.finished = true;
            quest.getRewards().forEach(questReward -> {
                questReward.applyToPlayer(player);
            });
            player.removeCurrentQuests(quest);
            player.addCompletedQuests(quest);
            player.sendMessage(Component.text("You finished the quest " + quest.getName()));
            return true;
        } else {
            return quest.step(player);
        }
    }

    public boolean start(CPlayer cPlayer, int id) {
        for (AbstractQuest quest : quests) {
            if (quest.getId() == id) {
                return quest.start(cPlayer);
            }
        }
        cPlayer.sendMessage(Component.text("This quest doesn't exist"));
        return false;
    }
}
