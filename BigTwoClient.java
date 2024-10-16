import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * This class is used for modeling client for the Big Two card game.
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */
public class BigTwoClient implements NetworkGame{
    //public contructor
    public BigTwoClient(BigTwo game, BigTwoGUI gui){
        this.game = game;
        this.gui = gui;
        this.playerID = game.getNumOfPlayers();
        this.playerName = (String) JOptionPane.showInputDialog("Name: ");
        if (this.playerName == null){
            this.playerName = "Player " + playerID;
        }
        connect();

    }

    private BigTwo game;
    private BigTwoGUI gui;
    private Socket sock;
    private ObjectOutputStream oos;
    private int playerID;
    private String playerName;
    private String serverIP;
    private int serverPort;


    
    /** 
     * Getter of PlayerID
     * @return int
     */
    @Override
    public int getPlayerID() {
        return this.playerID;
    }

    
    /** 
     * Setter of playerID
     * @param playerID
     */
    @Override
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    
    /** 
     * Getter of PlayerName
     * @return String
     */
    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    
    /** 
     * Setter of PlayerName
     * @param playerName
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        
    }

    
    /** 
     * Getter of ServerIP
     * @return String
     */
    @Override
    public String getServerIP() {
        return this.serverIP;
    }

    
    /** 
     * Setter of ServerIP
     * @param serverIP
     */
    @Override
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
        
    }
    
    
    /** 
     * Getter of ServerPort
     * @return int
     */
    @Override
    public int getServerPort() {
        return this.serverPort;
    }

    
    /** 
     * Setter of ServerPort
     * @param serverPort
     */
    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Connect sock to the BigTwoServer
     */
    @Override
    public void connect() {
        setServerIP("127.0.0.1");
        setServerPort(2396);

        try {
            sock = new Socket(serverIP, serverPort);
            oos = new ObjectOutputStream(sock.getOutputStream());
            Runnable threadJob = new ServerHandler();
            Thread myThread = new Thread(threadJob);
            myThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }  
    }

    
    /** 
     * Parse msg from BigTwoServer
     * @param message
     */
    @Override
    public void parseMessage(GameMessage message) {
		switch (message.getType()) {
            // update the players information on this client
            case CardGameMessage.PLAYER_LIST:
			    setPlayerID(message.getPlayerID());
                gui.setActivePlayer(message.getPlayerID());
                String[] names = (String[]) message.getData();
                for (int i = 0; i < names.length ; i++){
                    if (names[i]!=null){
                        game.getPlayerList().get(i).setName(names[i]);
                    }
                }
                sendMessage(new CardGameMessage(CardGameMessage.JOIN, this.playerID, getPlayerName()));
			    break;

            // update new player
            case CardGameMessage.JOIN:
                gui.printMsg("Welcome! Our new player : "+ (String) message.getData());
                game.getPlayerList().get(message.getPlayerID()).setName((String) message.getData());
                if (((String)message.getData()).equals(playerName)){
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }

                break;
            
            // If the game is Full
            case CardGameMessage.FULL:
                gui.printMsg("The game is full :/ Cannont join the game now.");
                break;
            
            // If Someone Quit
            case CardGameMessage.QUIT:
                gui.printMsg("Player "+message.getPlayerID()+" has left :/");
                game.getPlayerList().get(message.getPlayerID()).setName("");
                if (!game.endOfGame()){
                    gui.disable();
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                break;
            
            // If someone is ready
            case CardGameMessage.READY:
                gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName()+" is READY!!");
                break;
            
            // start if the game can start
            case CardGameMessage.START:
                gui.reset();
                game.getHandsOnTable().clear();
                game.start((Deck) message.getData());
                gui.enable();
                break;

            // Make move
            case CardGameMessage.MOVE:
                game.checkMove(message.getPlayerID(), (int[]) message.getData());
                // Print message for ending the game
                if (game.endOfGame()){
                    String msg = "Game ends\n";
                    for (int i = 0; i < 4; i++){
                        if (game.getPlayerList().get(i).getNumOfCards() != 0){
                            msg += "Player "+i+" has "+game.getPlayerList().get(i).getNumOfCards()+" cards in hand.\n";
                        }
                        else{
                            msg += "Player "+i+" wins the game.\n";
                        }
                    }
                    JOptionPane.showMessageDialog(this.gui.getFrame(), msg);

                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                    
                }
                break;
            
            // texting
            case CardGameMessage.MSG:
                gui.printChat((String) message.getData());
                break;
        }
        gui.repaint();

    }

    
    /** 
     * Send Msg to BigTwoServer
     * @param message
     */
    @Override
    public synchronized void sendMessage(GameMessage message) {
        try {
            oos.writeObject(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Inner class that implements the run method
     * a runnable for multi-threading
     */
    class ServerHandler implements Runnable{
        public void run() {
            CardGameMessage msg = null;
            try{
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                while ((msg = (CardGameMessage) ois.readObject())!=null){
                    parseMessage(msg);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            gui.repaint();
        }
    }



}
