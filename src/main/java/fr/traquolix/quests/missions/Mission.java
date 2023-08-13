package fr.traquolix.quests.missions;

import fr.traquolix.player.CPlayer;

public interface Mission {
    void startAutoActualizationOfTheMission(CPlayer cplayer);

    void stopSelfActualization(CPlayer cplayer);
}
