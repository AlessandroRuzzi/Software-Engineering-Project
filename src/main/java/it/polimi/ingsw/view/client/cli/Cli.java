package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardLoader;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientGameController;

import javax.print.DocFlavor;
import java.util.*;
import java.util.zip.CheckedOutputStream;

import static it.polimi.ingsw.utils.ConstantsContainer.*;

public class Cli extends ClientGameController {

    private int port = 4700;
    private String address = "127.0.0.1";
    private String nickName;
    private int numberOfPlayers;

    private Map<String, Card> deck = CardLoader.loadCards();

    private static final String TITLE = "\n\u001B[31m" +
            "             ___       ___  ___          ___    _____   ___      ___               _____  ___   ___                  |_|  |_|\n" +
            " \\   \\/   / |    |    |    |   | |\\  /| |         |    |   |    |       /\\   |\\  |   |   |   | |   | | |\\  | |     ___________\n" +
            "  \\  /\\  /  |--  |    |    |   | | \\/ | |--       |    |   |    |---|  /--\\  | \\ |   |   |   | |___| | | \\ | |     |   _|_   |\n" +
            "   \\/  \\/   |___ |___ |___ |___| |    | |___      |    |___|     ___| /    \\ |  \\|   |   |___| |  \\  | |  \\| |      |_______|\n\n\u001B[0m";


    public void start() {
        String keyboard;

        clearShell();
        print(TITLE);
        login(false);

        print("INSERT THE PORT NUMBER (default as 4700): ");
        keyboard = input();
        if(!keyboard.equals(""))
            setPort(Integer.parseInt(keyboard));

        print("INSERT THE IP ADDRESS (default as 127.0.0.1 - localhost): ");
        keyboard = input();
        if(!keyboard.equals(""))
            setAddress(keyboard);


        try {
            openConnection(getNickName(), getNumberOfPlayers(), getAddress(), getPort());
        }catch (Exception e) {
            System.err.print("\n\nFAILED TO OPENING CONNECTION\n\n");
        }

        lobby(false);

        startGame();

    }

    public void lobby(boolean isOnUpdateLobby) {
        clearShell();
        print("WAITING LOBBY\n");
        int waitingPlayers;

        if(!isOnUpdateLobby) {
            waitingPlayers = getNumberOfPlayers() - 1;
            printPlayers(waitingPlayers, false);
            checkBackCommand();
        }
        else {
            waitingPlayers = getNumberOfPlayers() - getPlayers().size();
            printPlayers(waitingPlayers,true);
            checkBackCommand();
        }
    }

    public void login(boolean lobbyCall) {

        setNickName();
        setNumberOfPlayers();

        if(lobbyCall)
            newGame(getNickName(), getNumberOfPlayers());
    }

    public List<String> challengerChooseCards() {
        List<String> chosenCards = new ArrayList<>();
        String keyboard;

        for(String s: deck.keySet())
            System.out.println(Color.ANSI_YELLOW + s.toUpperCase() + Color.RESET);


        keyboard = input().toLowerCase();
        String[] cards = splitter(keyboard);

        cards = printPower(cards, keyboard);

        for(int i=0; i<cards.length; i++) {
            Card card = deck.get(cards[i]);
            if(card == null) {
                print("WRONG CARD NAME. PLEASE, REINSERT NEW CARD NAME: ");
                keyboard = input().toLowerCase();
                cards = splitter(keyboard);
            }
        }

        clearShell();
        print("THE DECK OF THIS GAME IS COMPOSED BY: ");
        for (String card : cards) {
            print(card.toUpperCase() + " ", Color.ANSI_YELLOW);
        }

        Collections.addAll(chosenCards, cards);
        return chosenCards;
    }

    //-------------------------------

    //---------- SETTER & GETTER ----------

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName() {
        String nickName;

        print("\nINSERT YOUR NICKNAME: ");
        nickName = input();
        while(nickName.length() < MIN_LENGHT_NICK || nickName.length() > MAX_LENGHT_NICK) {
            print("\nINVALID NICKNAME LENGHT. PLEASE, REINSERT YOUR NICKNAME: ");
            nickName = input();
        }

        this.nickName = nickName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers() {
        String keyboard;

        print("INSERT THE NUMBER OF PLAYERS: ");
        keyboard = input();

        while (!keyboard.equals("2") && !keyboard.equals("3")) {
            print("\nINVALID NUMBER OF PLAYERS. PLEASE, REINSERT THE NUMBER OF PLAYERS: ");
            keyboard = input();
        }

        this.numberOfPlayers = Integer.parseInt(keyboard);
    }

    //------------------------------

    //---------- USEFUL FUNCTIONS ----------

    public static void print(String string) {
        System.out.print(Color.ANSI_RED + string + Color.RESET);
    }

    public static void print(String string, Color color) {
        System.out.print(color + string + Color.RESET);
    }

    public static String input() {
        String keyboard;
        Scanner input = new Scanner(System.in);
        keyboard = input.nextLine();
        return keyboard;
    }

    public static String[] splitter(String keyboard) {
        return keyboard.split("\\s");
    }

    public void printPlayers(int waitingPlayers, boolean isOnUpdateLobby) {
        print("WAITING FOR " + waitingPlayers + " PLAYERS\nPLAYERS ACTUALLY IN THE LOBBY:\n");

        if(isOnUpdateLobby) {
            for (Player p : getPlayers())
                print(">>> " + p.getNickname() + "\n");
        }
        else {
            print(">>> " + getNickName() + "\n");
        }
    }

    public void checkBackCommand() {
        print("INSERT \"BACK\" TO TURN BACK IN THE LOGIN WINDOW: ");
        String keyboard = input();

        if(keyboard.equalsIgnoreCase("BACK")) {
            onBackCommand();
            login(true);
        }
    }

    public String[] printPower(String[] cards, String keyboard) {
        while(cards.length == 1)
        {
            Card card = deck.get(cards[0]);
            if(card != null) {
                print("THIS IS THE POWER OF ");
                print(keyboard.toUpperCase(), Color.ANSI_YELLOW);
                print(":\n");
                if (keyboard.equalsIgnoreCase("ATHENA") || keyboard.equalsIgnoreCase("HERA"))
                    print("OPPONENT'S TURN:", Color.ANSI_BLUE);
                else if (keyboard.equalsIgnoreCase("HYPNUS"))
                    print("START OF OPPONENT'S TURN:", Color.ANSI_BLUE);
                else
                    print(deck.get(keyboard).getType().toString() + ":", Color.ANSI_BLUE);
                print(deck.get(keyboard).getDescription() + "\n\n");
            }
            else
                print("WRONG CARD NAME. PLEASE, REINSERT NEW CARD NAME: ");

            keyboard = input().toLowerCase();
            cards = splitter(keyboard);
        }
        if(cards.length != getPlayers().size()) {
            print("WRONG NUMBER OF CARDS. PLEASE, REINSERT " + getPlayers().size() + " CARDS: ");
            keyboard = input().toLowerCase();
            cards = splitter(keyboard);
        }

        return cards;
    }

    public Integer[] getCoordinates() {
        String[] split = splitter(input());

        split = controlCoordinates(split);

        return new Integer[] {Integer.parseInt(split[0]), Integer.parseInt(split[1])};

    }

    public String[] controlCoordinates(String[] split) {
        while(split.length != 2) {
            print("WRONG NUMBER OF PARAMETERS!\nPLEASE, REINSERT COORDINATES (from 0 up to 4): ");
            split = splitter(input());
        }

        while(!split[0].equals("0") && !split[0].equals("1") && !split[0].equals("2") && !split[0].equals("3") && !split[0].equals("4") && !split[1].equals("0") && !split[1].equals("1") && !split[1].equals("2") && !split[1].equals("3") && !split[1].equals("4")) {
            print("WRONG VALUE!\nPLEASE, REINSERT COORDINATES (from 0 up to 4): ");
            split = splitter(input());
        }

        return split;
    }

    public void clearShell() {
        Color.clearConsole();
    }

    //------------------------------

    //---------- OVERRIDING FUNCTIONS ----------

    @Override
    public void updateLobbyPlayer() {
        lobby(true);
    }

    @Override
    public void nickUsed() {
        clearShell();
        print("NICKNAME ALREADY USED. PLEASE, REINSERT A NEW NICKNAME:");
        setNickName();
    }

    @Override
    public void startGame() {
        clearShell();
        print("GAME IS GOING TO START. PLEASE WAIT WHILE IS LOADING\n\n");

        //INIZIO GIOCO (?)

    }

    @Override
    public void challengerChoice(String challengerNick, boolean isYourPlayer) {
        clearShell();
        if(isYourPlayer) {
            print("YOU HAVE BEEN CHOSEN AS CHALLENGER!\nPLEASE, CHOOSE " + getPlayers().size() + " CARDS ");
            print("(insert ONLY ONE card to see its power)", Color.ANSI_BLUE);
            print(":\n");

            List<String> cards = challengerChooseCards();

            challengerResponse(challengerNick, cards);

        }
        else {
            print("PLAYER " + challengerNick + " IS CHOOSING CARDS");
        }
    }

    @Override
    public void cardChoice(String challengerNick, boolean isYourPlayer) {
        clearShell();
        if(isYourPlayer) {
            print("AVAILABLE CARDS:\n");
            for(String s: getAvailableCards())
                print(s + "\n", Color.ANSI_YELLOW);
            print("\nINSERT THE NAME OF THE CARD YOU WANT TO CHOOSE: ");
            String choose = input();

            //FARE CONTROLLO SCELTA CORRETTA

            cardChoiceResponse(choose);
        }
        else {
            print("PLAYER " + challengerNick + "IS CHOOSING HIS POWER");
        }

    }

    @Override
    public void placeWorker(String challengerNick, boolean isYourPlayer) {
        clearShell();

        if(isYourPlayer) {
            print("INSERT COORDINATES (from 0 up to 4) OF THE TILE IN WHICH YOU WANT TO PLACE FIRST WORKER: ");
            Integer[] tile1 = getCoordinates();

            print("INSERT COORDINATES (from 0 up to 4) OF THE TILE IN WHICH YOU WANT TO PLACE SECOND WORKER: ");
            Integer[] tile2 = getCoordinates();

            cliPlaceWorkersResponse(tile1, tile2);
        }
        else
            print("PLAYER " + challengerNick + " IS PLACING HIS WORKERS\n");
    }

    @Override
    public void updatePlacedWorkers(List<Square> squares) {

    }

    @Override
    public void updateBoard() {

    }

    @Override
    public void notifyWin() {

    }

    @Override
    public void addConstraint() {

    }

    @Override
    public void onLobbyDisconnection() {
        clearShell();
        print("YOU ARE GOING TO BE DISCONNECTED FROM THE LOBBY. DO YOU WANT TO BE RECONNECTED (type REC) OR DO YOU WANT TO CLOSE THE APPLICATION (type CLOSE)?");
        String keyboard = input().toUpperCase();

        while (!keyboard.equals("REC") && !keyboard.equals("CLOSE")) {
            print("\nPLEASE REINSERT \"REC\" TO BE RECONNECTED TO THE LOBBY, \"CLOSE\" TO CLOSE THE APP:");
            keyboard = input().toUpperCase();
        }

        switch (keyboard) {
            case "REC":
                break;
            case "CLOSE":
                break;
            default:
                System.err.print("\n\nERROR IN DISCONNECTION CHOICE\n\n");
        }

    }

    @Override
    public void onPingDisconnection() {

    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void errorMessage() {

    }

    @Override
    public void startTurn() {

    }
}
