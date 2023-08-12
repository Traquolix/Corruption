package fr.traquolix.entity.npc;

import fr.traquolix.time.DialogueTimeline;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static fr.traquolix.Main.logger;

public class DialogueSelector {

    private final DialogueTimeline dialogues;
    private int totalWeight;
    private Random random;
    private final Dialogue defaultDialogue = new Dialogue(new ArrayList<>(List.of(Component.text("I have nothing to tell you right now.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))), 1);
    private Dialogue lastDialogue = null;
    public DialogueSelector() {
        dialogues = new DialogueTimeline();
        random = new Random();
    }

    public void addDialogue(int i, List<TextComponent> line, int weight) {
        dialogues.add(i, new Dialogue(line, weight));
        totalWeight += weight;
    }
 // TODO Faire en sorte qu'on ait des mystères à résoudre, type enquêtes, mais que les meilleurs moyens pour les résoudre est simplement d'être là au bon moment. Donc on déduit surtout le temps et l'endroit de la scène pour pouvoir y aller à la prochaine boucle.
    //   TODO Inspiration d'obra dinn. Remplir soi même les informations qu'on récolte ? En tout cas, certaines. Mais quand même noter qu'on est dans un jeu multi, et que les gens vont communiquer sur les réponses.
    //  TODO Il faut voir comment ça peut être fait.
    //   Le fait de devoir établir 3 résultats corrects pour valider la suite évite de trop deviner et on doit vraiment connaître les réponses.
    //   Chaque réponse peut être trouvée d'un tas de façons de différentes, pour éviter que les gen soient trop bloqués.
    //   Une bonne façon d'éviter qu'on puisse partager l'information en multi est de le faire aléatoirement, tout simplement ? Chacun avec sa seed I guess ? Je sais pas exactement.
    //   On peut également faire en sorte que les actions d'un joueur dans la boucle pour une quête crée une autre quête pour tous les autres ? Genre s'il choisit de tuer un certain NPC, il faut enquêter sur sa disparition etc. Y'a une récompense à tuer le NPC, mais tu as aussi un malus qui dure toute la timeloop, pendant que les autres peuvent enquêter sur ton identité pour pouvoir réclamer la quête du detective.
    //   Oui, ça me semble pas trop mal comme idée, de faire des quêtes qui se crée en fonction des actions d'autres joueurs.
    //   Les actions que tu as ont des conséquences énormes sur le jeu, mais c'est pas si grave parce que prochaine time loop = reset.
    //   Les autres joueurs pourraient avoir un impact aussi.
    //   Voir pour développer un système de quête un peu plus intéressant, notamment avec des branches ? (On peut déjà en faire, mais toutes les branches sont accessibles... Quoique, on peut créer un requirement ou il faut ne pas avoir fait telle quête pour pouvoir faire telle quête, mais c'est pas très propre, et ça veut dire qu'on verra les autres branches dans les quêtes disponibles, c'est pas ouf.
    //
    //  TODO Il faudrait cacher les quêtes qu'on peut pas faire peut être ? Et simplement se baser sur quelques informations ici et là, la progression du joueur naturelle, pour partir du principe qu'il arrivera à un moment ou il parlera au NPC avec les bons requirements pour faire apparaître les autres quêtes ? Mouaip, pourquoi pas.
    //   Mais ça veut aussi dire que pour l'écran de knowledge sur un NPC, toutes les branches seront décomptées comme des quêtes à part. Ce qui est pas si mal en soi, ça part du principe que t'as pas exploré toutes les branches, et qu'il y a encore du contenu. J'aime bien cette idée.
    //   Faire en sorte que le joueur sache qu'il y a encore du contenu, mais qu'il a simplement pas forcément trouvé comment le débloquer pourrait être pas mal. Yep yep. Mais ne pas afficher les quêtes déjà terminées. Et avoir la possibilité d'observer la timeline d'un NPC, pour pouvoir constater quelles quêtes on a découvert / fini à quel moment, et éventuellement montrer qu'il y a des quêtes inconnues de disponible dans cet écran là ? Je ne sais pas trop comment aborder ça pour le moment, faut que j'y réfléchisse

    // TODO Les quêtes peuvent impacter le monde, mais il faut pas que les quetes donnent des récompenses trop élevées. Ou alors il doit y en avoir BEAUCOUP qui impactent l'environnement, pour pas qu'on se sente lésé pendant une timeloop je suppose. Elles doivent avoir peu de conséquences techniques, mais peuvent avoir des conséquences en terme d'histoire.
    //  Il faut voir si l'impact d'une quête ne risque pas de dérégler d'autres quêtes autour par contre, et ne pas bloquer des accès. Si on fait en sorte qu'un accès se ferme si *quelqu'un* fait la quête, well, quelqu'un fera toujours la quête, et l'accès sera toujours bloqué... Faut voir.
    public List<TextComponent> getRandomDialogue(int i) {
        if (dialogues.getSize(i) == 1) {
            logger.warn("There is only one dialogue for this NPC, it should not. Make sure you have at least 2 dialogues if you add one to a time step / NPC.");
            return List.of(Component.text("..."));
        }
        Dialogue selectedDialogue = null;
        do {
            int value = random.nextInt(totalWeight);
            int weightSum = 0;
            if (dialogues.get(i) != null) {
                for (Dialogue dialogue : dialogues.get(i)) {
                    weightSum += dialogue.weight;
                    if (weightSum >= value) {
                        selectedDialogue = dialogue;
                        break;  // We found our dialogue, break out of the loop
                    }
                }
                if (selectedDialogue == null) {
                    selectedDialogue = defaultDialogue;
                }
            } else {
                selectedDialogue = defaultDialogue;
            }
        } while (selectedDialogue == lastDialogue); // Keep looping until a different dialogue is selected

        lastDialogue = selectedDialogue;
        return selectedDialogue.line;
    }

}
