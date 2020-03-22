package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Map.Building;
import it.polimi.ingsw.model.Map.Directions;
import it.polimi.ingsw.model.Map.GameMap;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Player.Worker;

import java.util.ArrayList;

public class Card {

    private String name;
    private String description;
    private boolean isPlayableIn3;
    private CardType type;
    private CardSubType subType;

    public Card (String name, String description, boolean isPlayableIn3, CardType type, CardSubType subType)
    {
        this.name = name;
        this.description = description;
        this.isPlayableIn3 = isPlayableIn3;
        this.type = type;
        this.subType = subType;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public boolean isPlayableIn3() {
        return isPlayableIn3;
    }

    public CardType getType() {
        return type;
    }

    public CardSubType getSubType() {
        return subType;
    }

    public ArrayList<Directions> findWorkerMove(GameMap gameMap, Worker worker) {
        return gameMap.reachableSquares(worker);
    }

    public Response executeWorkerMove(GameMap gameMap, Directions directions, Player player) {
        gameMap.moveWorkerTo(player, directions);
        return  Response.MOVED;
    }

    public ArrayList<Directions> findPossibleBuild(GameMap gameMap, Worker worker) {
        return gameMap.reachableSquares(worker);
    }

    public Response executeBuild(GameMap gameMap, Building building, Directions directions, Worker worker) {
        if(gameMap.buildInSquare(worker, directions, building))
            return Response.BUILD;
        else
            return Response.NOTBUILD;
    }

    public boolean checkVictory(GameMap gameMap, Worker worker) {
        return worker.getBoardPosition().getBuildingLevel() == 3 && worker.getPreviousBoardPosition().getBuildingLevel() == 2;
    }

    public ArrayList<Directions> eliminateInvalidMove(GameMap gameMap,Worker worker, ArrayList<Directions> directionsArrayList) {
        return directionsArrayList;
    }

    public ArrayList<Directions> canMove(GameMap gameMap, Worker worker) {
        return null;
    }
    public boolean isValidVictory() {
        return true;
    }

    public void assignConstraint(ArrayList<Player> playerArrayList,Player currentPlayer){

        for(Player player: playerArrayList){
            if(!player.equals(currentPlayer))
                player.setConstraint(currentPlayer.getPower());
        }

    }

    @Override
    public String toString() {
        String result = "Card Name -> " + name + "\nCard Description -> " + description;
        if(isPlayableIn3)
            result += "\nPlayable with 3 Player -> Yes\n";
        else
            result += "\nPlayable with 3 Player -> No\n";
        return result;
    }
}
