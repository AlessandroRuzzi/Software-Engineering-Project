package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Map.Building;
import it.polimi.ingsw.model.Map.Directions;
import it.polimi.ingsw.model.Map.GameMap;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Player.TurnStatus;
import it.polimi.ingsw.model.Player.Worker;
import it.polimi.ingsw.model.Player.WorkerName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PanTest {

    Player player1, player2;
    Card cardPan;
    Worker worker1,worker2;
    GameMap gameMap;
    ArrayList<Directions> directions;

    @BeforeEach
    void setup() {
        player1 = new Player("GoodPlayer", TurnStatus.PREGAME);
        player2 = new Player("BadPlayer", TurnStatus.PREGAME);
        cardPan = CardLoader.loadCards().get("Pan");
        player1.setPower(cardPan);
        worker1 = new Worker(WorkerName.WORKER1);
        worker2 = new Worker(WorkerName.WORKER2);
        gameMap = new GameMap();
        gameMap.getGameMap().get(13).setMovement(player1, player1.getWorkers().get(0));
        player1.getWorkers().get(0).setBoardPosition(gameMap.getGameMap().get(13));
        gameMap.getGameMap().get(4).setMovement(player1, player1.getWorkers().get(1));
        player1.getWorkers().get(1).setBoardPosition(gameMap.getGameMap().get(4));
        gameMap.getGameMap().get(21).setMovement(player2, player2.getWorkers().get(0));
        player2.getWorkers().get(0).setBoardPosition(gameMap.getGameMap().get(21));
        gameMap.getGameMap().get(18).setMovement(player2, player2.getWorkers().get(1));
        player2.getWorkers().get(1).setBoardPosition(gameMap.getGameMap().get(18));
        player1.selectCurrentWorker(gameMap, "worker1");

        gameMap.getGameMap().get(23).setBuilding(Building.LVL1);
        gameMap.getGameMap().get(23).addBuildingLevel();
        gameMap.getGameMap().get(23).setBuilding(Building.LVL2);
        gameMap.getGameMap().get(23).addBuildingLevel();
        gameMap.getGameMap().get(23).setBuilding(Building.LVL3);
        gameMap.getGameMap().get(23).addBuildingLevel();
        gameMap.getGameMap().get(14).setBuilding(Building.LVL1);
        gameMap.getGameMap().get(14).addBuildingLevel();
        gameMap.getGameMap().get(14).setBuilding(Building.LVL2);
        gameMap.getGameMap().get(14).addBuildingLevel();

        directions = player1.findWorkerMove(gameMap, player1.getWorkers().get(0));
    }

    @Test
    void checkVictory() {
        assertThrows(NullPointerException.class , () -> cardPan.checkVictory(null, worker1));
        assertThrows(NullPointerException.class , () -> cardPan.checkVictory(gameMap, null));

        assertEquals(player1.executeWorkerMove(gameMap, Directions.NORD), Response.MOVED);
        assertEquals(cardPan.checkVictory(gameMap, player1.getCurrentWorker()), Response.NOTWIN);
        player1.getCurrentWorker().setBoardPosition(gameMap.getGameMap().get(14));
        assertEquals(player1.executeWorkerMove(gameMap, Directions.EST), Response.MOVED);
        assertEquals(cardPan.checkVictory(gameMap, player1.getCurrentWorker()), Response.WIN);
        assertEquals(player1.executeWorkerMove(gameMap, Directions.OVEST), Response.MOVED);
        assertEquals(cardPan.checkVictory(gameMap, player1.getCurrentWorker()), Response.NOTWIN);
        assertEquals(player1.executeWorkerMove(gameMap, Directions.SUD), Response.MOVED);
        assertEquals(cardPan.checkVictory(gameMap, player1.getCurrentWorker()), Response.WIN);
    }
}