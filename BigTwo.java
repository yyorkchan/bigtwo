import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface and is used to model
 * the BigTwo Card Game with private variable storing the basic information
 * of the BigTwo game and method that runs the game.
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class BigTwo implements CardGame{

    private BigTwoClient client; //client of the game
    private int numOfPlayers; // number of player
    private Deck deck; // deck of card
    private ArrayList<CardGamePlayer> playerList; // list of player
    private ArrayList<Hand> handsOnTable; // list of hand played on the table
    private int currentPlayerIdx; // current player index
    private BigTwoGUI gui; // BigTwogui that provide the user interface
    private int pass = 0; // counting the number passed in this round

    // Constructor of BigTwo
    public BigTwo(){
        playerList = new ArrayList<CardGamePlayer>();
        handsOnTable = new ArrayList<Hand>();
        this.numOfPlayers = 4;
        for (int i = 0; i < numOfPlayers; i++){
            CardGamePlayer player = new CardGamePlayer();
            playerList.add(player);
        }
        gui = new BigTwoGUI(this);
    }

    
    /** 
     * Getter of numOfPlayers
     * @return int
     */
    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }

    
    /** 
     *  Getter of deck
     * @return Deck
     */
    public Deck getDeck(){
        return this.deck;
    }

    
    /** 
     *  Getter of playerlist
     * @return ArrayList<CardGamePlayer>
     */
    public ArrayList<CardGamePlayer> getPlayerList(){
        return this.playerList;
    }

    
    /** 
     *  Getter of handsOnTable
     * @return ArrayList<Hand>
     */
    public ArrayList<Hand> getHandsOnTable(){
        return this.handsOnTable;
    }

    
    /** 
     *  Getter of currentPlayerIdx
     * @return int
     */
    public int getCurrentPlayerIdx(){
        return this.currentPlayerIdx;
    }

    
    /** 
     * Public Boolen method for the status of end game or not
     * @return boolean
     */
    public boolean endOfGame(){
        for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i).getCardsInHand().isEmpty()){
                return true;
            }
        }
        return false;
    }

    
    /** 
     * Public method that start the BigTwo game
     * @param deck
     */
    public void start(Deck deck){
        // remove all cards from the players and table
        for (int i = 0; i < playerList.size(); i++){
            playerList.get(i).removeAllCards();
        }
        this.handsOnTable = new ArrayList<Hand>();
        // distribute cards to player
        for (int i = 0; i < deck.size(); i++){
            playerList.get(i%4).addCard(deck.getCard(i));
        }
        // make player who hold Three of Diamonds as the starting player
        for (int i = 0; i < playerList.size(); i++){
            playerList.get(i).sortCardsInHand();
            if (playerList.get(i).getCardsInHand().contains(new Card(0,2))){
                this.currentPlayerIdx = i;
                this.gui.setActivePlayer(i);
            }
        }
        this.gui.promptActivePlayer();
        this.gui.repaint();
/** 
        while (!endOfGame()){
            this.gui.repaint();
            this.gui.promptActivePlayer();
        }*/


    }

    
    /** 
     * priavate method for check valid move
     * 1: valid, 2: invalid input 3: pass
     * @param playerIdx
     * @param cardIdx
     * @return int
     */
    private int validMove(int playerIdx, int[] cardIdx){
        // check pass condition
        if (cardIdx == null){
            if (this.pass < 3 & !(handsOnTable.isEmpty())){
                return 3;
            }
            return 2;
        }
        // check whether the input of function is within range
        if (cardIdx.length > 5){
            return 2;
        }
        if (playerIdx < 0 || playerIdx > 3){
            return 2;
        }

        // check whether the player input is exist in player hand
        CardGamePlayer player = playerList.get(playerIdx);
        for (int i = 0; i < cardIdx.length; i++){
            if (cardIdx[i] >= player.getNumOfCards() || cardIdx[i] < 0){
                return 2;
            }
        }

        CardList PlayerHand = new CardList();
        PlayerHand = player.play(cardIdx);
        Hand hand = composeHand(player, PlayerHand);
        // return 2 if no valid hand
        if (hand == null){
            return 2;
        }

        // Check for first hand condition
        if (handsOnTable.isEmpty()){
            if (hand.isValid() & hand.contains(new Card(0,2))){
                return 1;
            }
            return 2;
        }

        // Check for hand beats or not
        else if(pass < 3 & hand.beats(handsOnTable.get(handsOnTable.size()-1))){
            return 1;
        }

        // If all other players pass
        else if (this.pass == 3 & hand.isValid()){
            return 1;
        }
        return 2;
    }

    
    /** 
     * Public method for making a valid move with index specifiied by the player
     * @param playerIdx
     * @param cardIdx
     */
    public void makeMove(int playerIdx, int[] cardIdx){
        getClient().sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
    }

    
    /** 
     * Print message when the player does not make a valid move or pass
     * @param playerIdx
     * @param cardIdx
     */
    public void checkMove(int playerIdx, int[] cardIdx){
        int check = validMove(playerIdx, cardIdx);
        // ask again for a valid move if it is not
        if (check == 2){
            gui.printMsg("Not a legal move!!!");
            this.gui.promptActivePlayer();
            gui.repaint();
        }
        // pass if the player pass
        else if (check == 3){
            gui.printMsg("{Pass}");
            gui.printMsg("");
            this.pass += 1;
            this.currentPlayerIdx = (playerIdx + 1) % 4;
            this.gui.setActivePlayer(this.currentPlayerIdx);
            this.gui.promptActivePlayer();
            gui.repaint();
        }
        // make the valid move if the player input correctly
        else if (check == 1){
            this.pass = 0;
            CardGamePlayer player = playerList.get(playerIdx);
            CardList PlayerHand = new CardList();
            PlayerHand = player.play(cardIdx);
            Hand hand = composeHand(player, PlayerHand);
            for (int i = 0; i < PlayerHand.size(); i++){
                playerList.get(playerIdx).getCardsInHand().removeCard(PlayerHand.getCard(i));
            }
            this.handsOnTable.add(hand);
            gui.printMsg("{"+hand.getType()+"} "+ hand);
            gui.printMsg("");
            this.currentPlayerIdx = (playerIdx + 1) % 4;
            this.gui.setActivePlayer(currentPlayerIdx);
            gui.promptActivePlayer();
            gui.repaint();
        }
        // Print message for ending the game
        if (endOfGame()){
            this.gui.printMsg("Game ends");
            for (int i = 0; i < 4; i++){
                if (playerList.get(i).getNumOfCards() != 0){
                    this.gui.printMsg("Player "+i+" has "+playerList.get(i).getNumOfCards()+" cards in hand.");
                }
                else{
                    this.gui.printMsg("Player "+i+" wins the game.");
                }
            }
            this.gui.disable();
        }
    }

    
    /** 
     * For checking the hand type is valid or not
     * Returning the hand type or null if it is not valid
     * @param player
     * @param cards
     * @return Hand
     */
    Hand composeHand(CardGamePlayer player, CardList cards){
        Single single = new Single(player, cards);
        Pair pair = new Pair(player, cards);
        Triple triple = new Triple(player, cards);
        Straight straight = new Straight(player, cards);
        Flush flush = new Flush(player, cards);
        FullHouse fullhouse = new FullHouse(player, cards);
        Quad quad = new Quad(player, cards);
        StraightFlush straightFlush = new StraightFlush(player, cards);

        if (straightFlush.isValid()){
            return straightFlush;
        }
        else if (single.isValid()){
            return single;
        }
        else if (pair.isValid()){
            return pair;
        }
        else if (triple.isValid()){
            return triple;
        }
        else if (straight.isValid()){
            return straight;
        }
        else if (flush.isValid()){
            return flush;
        }
        else if (fullhouse.isValid()){
            return fullhouse;
        }
        else if (quad.isValid()){
            return quad;
        }

        return null;
    }

    // Make connection with game server
    public void connect(){
        this.client = new BigTwoClient(this, this.gui);
    }

    //getter of client
    public BigTwoClient getClient(){
        return this.client;
    }

    /** 
     * main function for running the game
     * @param args
     */
    public static void main(String[] args){
        BigTwo game = new BigTwo();
        BigTwoDeck deck = new BigTwoDeck();
        deck.shuffle();
        game.start(deck);
    }
}
