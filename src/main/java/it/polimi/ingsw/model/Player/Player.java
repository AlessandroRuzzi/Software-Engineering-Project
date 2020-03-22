package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Cards.Card;
import it.polimi.ingsw.model.Cards.CardSubType;
import it.polimi.ingsw.model.Cards.CardType;
import it.polimi.ingsw.model.Cards.Response;
import it.polimi.ingsw.model.Map.Building;
import it.polimi.ingsw.model.Map.Directions;
import it.polimi.ingsw.model.Map.GameMap;

import javax.swing.*;
import java.util.ArrayList;

public class Player {

    private String nickname;
    private Card power;
    private TurnStatus turnStatus;
    private ArrayList<Card> constraint;
    private ArrayList<Worker> workers;
    private Worker currentWorker;
    private Worker unmovedWorker;

    public Player (String nickname, TurnStatus turnStatus){

        workers = new ArrayList<>();
        constraint = new ArrayList<>();
        this.nickname = nickname;
        this.turnStatus = turnStatus;

        workers.add(new Worker(WorkerName.WORKER1));
        workers.add(new Worker(WorkerName.WORKER2));
    }

    public String getNickname() { return nickname;}

    public Card getPower(){ return power;}

    public void setPower(Card power){
        if (power == null)
            throw new NullPointerException("power == null");

        this.power = power;
    }

    public TurnStatus getTurnStatus() { return turnStatus;}

    public void setTurnStatus(TurnStatus turnStatus) {
        if (turnStatus == null)
            throw new NullPointerException("turnStatus == null");

        this.turnStatus = turnStatus;
    }

    public ArrayList<Card> getConstraint() { return constraint;}

    public void setConstraint(Card constraint) {
        if (constraint == null)
            throw new NullPointerException("constraint == null");

        this.constraint.add(constraint);
    }

    public void removeConstraint(Card constraint){
        if (constraint == null)
            throw new NullPointerException("constraint == null");

        this.constraint.remove(constraint);
    }

    public ArrayList<Worker> getWorkers() { return workers;}


    public void setCurrentWorker(Worker currentWorker) {
        if (currentWorker == null)
            throw new NullPointerException("currentWorker == null");

        this.currentWorker = currentWorker;
    }

    public Worker getCurrentWorker() { return currentWorker;}

    public void setUnmovedWorker(Worker unmovedWorker) {
        if (unmovedWorker == null)
            throw new NullPointerException("unmovedWorker == null");

        this.unmovedWorker = unmovedWorker;
    }

    public Worker getUnmovedWorker() { return unmovedWorker;}

    //
    //function to transform the string with the worker in the WorkerName enumeration
    //

    public Worker getWorkerFromString (String worker){
        if (worker == null)
            throw new NullPointerException("worker == null");

        WorkerName name = WorkerName.parseInput(worker);
        for (Worker work : workers)
            if(work.getName().equals(name))
                return work;
        throw new IllegalArgumentException("Wrong name");
    }

    public boolean selectCurrentWorker(GameMap gameMap, String worker){
        if (gameMap == null || worker == null)
            throw new NullPointerException("gameMap or worker == null");

        Worker worker1 = getWorkerFromString(worker);
        if (!checkIfCanMove(gameMap, worker1)){
            return false;
        }
        setCurrentWorker(worker1);
        return true;
    }

    public boolean checkIfCanMove(GameMap gameMap, Worker worker){
        if (gameMap == null || worker == null)
            throw new NullPointerException("gameMap or worker == null");

        ArrayList<Directions> direction = findWorkerMove(gameMap, worker);
        if(direction.size() > 0){
            for(Card card : constraint){
                if (!checkConstraint(gameMap, worker, card, direction))
                    return  false;
            }
        }
        else return false;

        return true;
    }

    private boolean checkConstraint (GameMap gameMap, Worker worker, Card card, ArrayList<Directions> direction){
        if(card.getType().equals(CardType.YOURMOVE) && !card.getSubType().equals(CardSubType.NORMAL)){
            return card.eliminateInvalidMove(gameMap, worker, direction).size() > 0;
        }
        else if(card.getType().equals(CardType.YOURTURN) && !card.getSubType().equals(CardSubType.NORMAL)) {
            return card.canMove(gameMap, worker).size() > 0;
        }
        return  true;
    }

    public boolean checkIfLoose(GameMap gameMap){
        if (gameMap == null)
            throw new NullPointerException("gameMap or worker == null");

        for (Worker work : workers){
            checkIfCanMove(gameMap, work);
        }
        return true;
    }

    public ArrayList<Directions> findWorkerMove(GameMap gameMap, Worker worker){ return power.findWorkerMove(gameMap, worker);}

    public Response executeWorkerMove(GameMap gameMap, Directions direction, Player player){ return power.executeWorkerMove(gameMap, direction, player);}

    public ArrayList<Directions> findPossibleBuild(GameMap gameMap, Worker worker){ return power.findPossibleBuild(gameMap, worker);}

    public Response executeBuild(GameMap gameMap, Building building, Directions direction){
        if (gameMap == null || building == null || direction == null)
            throw new NullPointerException("gameMap or building or direction == null");

        return power.executeBuild(gameMap, building, direction, this.currentWorker);
    }

    public boolean checkVictory(GameMap gameMap, Worker worker){
        if (gameMap == null || worker == null)
            throw new NullPointerException("gameMap or worker == null");

        return  power.checkVictory(gameMap, worker);
    }




}
