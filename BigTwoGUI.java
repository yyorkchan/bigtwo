import java.util.ArrayList;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;


/**
 * This class is used for modeling a graphical user interface for the Big Two card game.
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */
public class BigTwoGUI implements CardGameUI{

	private final static int MAX_CARD_NUM = 13; // max. no. of cards each player holds
	private BigTwo game = null; // a BigTwo object
	private ArrayList<CardGamePlayer> playerList; // the list of players
	private int activePlayer = -1; // the index of the active player
	private boolean[] selected = new boolean[MAX_CARD_NUM]; // selected cards

	private JFrame frame; // main window of the application

	private JPanel bigTwoPanel; // west panel for showing the cards of each player
	private boolean disableMouseListen = false;

	private JPanel[] playerPanels = new JPanel[4];

	private JPanel bottomPanel; // Sourth panel with text input place and button
	private JPanel bottomleftPanel;
	private JPanel bottomrightPanel;
	private JButton playButton; // a "Play" button for the active player to play the selected card
	private JButton passButton; // a "Pass" button for the active player to pass the current round
	private JTextField chatInput; // a text field for players to input chat message
	private JLabel msgtext; // a text of "Message: "

	private JPanel rightPanel; // Panel of the east
	private JTextArea msgArea; // a text area for showing the current game status
	private JTextArea chatArea; // a text are for showing chat message sent by the player
	private JScrollPane scroll1; // scrollpane for msgArea
	private JScrollPane scroll2; // scrollpane for chatArea


	

	/**
	 * Creates and returns an instance of the BigTwoUI class.
	 * 
	 * @param game a BigTwo object associated with this UI
	 */
	public BigTwoGUI(BigTwo game){
		this.game = game;
		selected = new boolean[13];
		playerList = new ArrayList<CardGamePlayer>();
		this.playerList = game.getPlayerList();
		this.initialize();
	}

	/**
	 * Sets the index of the active player.
	 * 
	 * @param activePlayer the index of the active player (i.e., the player who can
	 *                     make a move)
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= playerList.size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}

	//====================================================
	/**
	 * initialize the GUI.
	 */
	public void initialize() {
		// Frame
		frame = new JFrame("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Top
		JMenuBar menuBar = new JMenuBar(); // menuBar
		JMenu gameM = new JMenu("Game"); // Game bar button
		JMenuItem connect = new JMenuItem("Connect"); // selection of game button
		JMenuItem quit = new JMenuItem("Quit"); // selection of the game button

		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());

		gameM.add(connect);
		gameM.add(quit);
		menuBar.add(gameM);
		

		// Bottom
		bottomPanel = new JPanel();
		bottomleftPanel = new JPanel(); // Panel for play and pass button
		bottomrightPanel = new JPanel(); // Panel for chat Input

		chatInput = new JTextField(20); 
		chatInput.addActionListener(new TextInputListener());

		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		msgtext = new JLabel("                                                                  Message: ");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		bottomleftPanel.add(playButton);
		bottomleftPanel.add(passButton);
		bottomrightPanel.add(msgtext);
		bottomrightPanel.add(chatInput);
		// Set the Background color to gray
		bottomPanel.setBackground(Color.gray);
		bottomleftPanel.setBackground(Color.gray);
		bottomrightPanel.setBackground(Color.gray);
		bottomPanel.add(bottomleftPanel);
		bottomPanel.add(bottomrightPanel);

		// Right
		rightPanel = new JPanel();
		msgArea = new JTextArea();
		chatArea = new JTextArea();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		msgArea.setEditable(false); // set textArea non-editable
		chatArea.setEditable(false); // set textArea non-editable
		// make it scrollable
		scroll1 = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2 = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// set size
		scroll1.setPreferredSize(new Dimension(450,375));
		scroll2.setPreferredSize(new Dimension(450,375));
		// set background
		scroll1.setBackground(Color.CYAN);
		scroll2.setBackground(Color.magenta);
		rightPanel.add(scroll1);
		rightPanel.add(scroll2);

		// left
		bigTwoPanel = new BigTwoPanel();
		// set size
		bigTwoPanel.setPreferredSize(new Dimension(550 , 375));
		// set background color
		bigTwoPanel.setBackground(Color.PINK);


		// add them to frame
		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.add(bigTwoPanel, BorderLayout.WEST);
		frame.add(rightPanel, BorderLayout.EAST);

		frame.setSize(new Dimension(1000, 750));
		frame.setResizable(false);
		frame.setVisible(true);
	}
	//====================================================

	/**
	 * Repaint the GUI
	 */
	public synchronized void repaint(){
		resetSelected();
		frame.repaint();
	}
	/**
	 * Prints the specified string to the GUI.
	 * 
	 * @param msg the string to be printed to the GUI
	 */
	public void printMsg(String msg) {
		msgArea.append(msg+"\n");
		this.msgArea.setCaretPosition(msgArea.getDocument().getLength());
	}

	
	/** 
	 * Print chat msg
	 * @param msg
	 */
	public void printChat(String msg){
		chatArea.append(msg + "\n");
		this.chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}

	/**
	 * Clears the message area of the GUI.
	 */
	public void clearMsgArea() {
		this.msgArea.setText(null);
	}

	/**
	 * Resets the GUI.
	 */
	public void reset() {
		clearMsgArea();
		repaint();
		enable();
	}

	/**
	 * Enables user interactions.
	 */
	public void enable() {
		this.playButton.setEnabled(true);
		this.passButton.setEnabled(true);
		this.chatInput.setEnabled(true);
		//disableMouseListen = false;
	}

	/**
	 * Disables user interactions.
	 */
	public void disable() {
		this.playButton.setEnabled(false);
		this.passButton.setEnabled(false);
		this.chatInput.setEnabled(false);
		//disableMouseListen = true;
	}

	/**
	 * Prompts active player to select cards and make his/her move.
	 */
	public void promptActivePlayer() {
		if (game.endOfGame()==false && game.getClient()!=null){
            if (game.getClient().getPlayerID() == activePlayer){
                printMsg("Your turn");
            }
            else{
                printMsg("Player " + this.game.getCurrentPlayerIdx() + "'s turn");
            }
        }
	}
	
	/**
	 * Returns an array of indices of the cards selected through the UI.
	 * 
	 * @return an array of indices of the cards selected, or null if no valid cards
	 *         have been selected
	 */
	public int[] getSelected() {
		int count = 0;
		for (int i = 0; i < selected.length; i++){
			if (selected[i]){
				count += 1;
			}
		}
		if (count == 0){
			return null;
		}
		int[] chosen = new int[count];
		int temp = 0;
		while (temp < count){
			for (int i = 0; i < selected.length; i++){
				if(selected[i]){
					chosen[temp] = i;
					temp += 1;
				}
			}
		}
		return chosen;

	}

	
	/** 
	 * Getter of the frame
	 * @return JFrame
	 */
	public JFrame getFrame(){
		return this.frame;
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	private void resetSelected() {
		for (int j = 0; j < selected.length; j++) {
			selected[j] = false;
		}
	}


	/**
	 * Inner class that implements the actionlistener for the play button
	 * which calls the makemove() function with the selected cards
	 */
	class PlayButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (getSelected() == null){
				printMsg("Select Your cards!!!");
				promptActivePlayer();
			}
			else if(game.getClient().getPlayerID() == activePlayer){
				game.makeMove(activePlayer, getSelected());
				resetSelected();
			}
			else{
				printMsg("Not your turn!!!!!");
				resetSelected();
			}
		}
	}

	/**
	 * Inner class that implements the actionlistener for the pass button
	 * which calls the makemove() function with the selected cards
	 */
	class PassButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(game.getClient().getPlayerID() == activePlayer){
				game.makeMove(activePlayer, null);
				resetSelected();
			}
			else{
				printMsg("Not your turn!!!!!");
				resetSelected();
			}
		}
	}

	/**
	 * Inner class that implements the actionlistener for the chat area
	 * which prints chat input to chat area
	 */
	class TextInputListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, chatInput.getText()));
			chatInput.setText(null);
		}
	}

	/**
	 * Inner class that implements the actionlistener for the connect button in the menubar
	 * which connect to the game server
	 */
	class ConnectMenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			game.connect();
		}
	}


	/**
	 * Inner class that implements the actionlistener for the quit button in the menu bar
	 * which exit the frame
	 */
	class QuitMenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	}

	/**
	 * Inner class for the BigTwoPanel that extends the Jpanel and implements 
	 * the mouse listener.
	 * It create the bigTwoPanel with cards player name and icons 
	 * and allows plaayer to play on it 
	 */
	class BigTwoPanel extends JPanel implements MouseListener{

		// constructor of the BugTwoPanel
		public BigTwoPanel(){
            this.addMouseListener(this);
        }

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			//setting the player icon
			Image image0 = new ImageIcon("images/player0.jpg").getImage();
			Image image1 = new ImageIcon("images/player1.jpg").getImage();
			Image image2 = new ImageIcon("images/player2.jpg").getImage();
			Image image3 = new ImageIcon("images/player3.jpg").getImage();

			g2.drawImage(image0, 5, 40, 90, 70, this);
			g2.drawImage(image1, 5, 170, 90, 70, this);
			g2.drawImage(image2, 5, 302, 90, 70, this);
			g2.drawImage(image3, 5, 434, 90, 70, this);

			// make an image array to store cards.jpg
			Image[][] cards = new Image[4][13];
			Image backofcard = new ImageIcon("images/back.jpg").getImage();
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 13; j++){
					cards[i][j] = new ImageIcon("images/" + j + "" + i +".jpg").getImage();
				}
			}

			
			// draw cards image
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++){
					int rank = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
					int suit = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
					if(game.getClient() != null){
						if (i == game.getClient().getPlayerID()){ // print face of cards if its active player
							if (!selected[j]){
								g2.drawImage(cards[suit][rank], 120 + j*27, 25 + 132*i, 60, 100, this);
							}
							else{ // lift the cards if selected
								g2.drawImage(cards[suit][rank], 120 + j*27, 5 + 132*i, 60, 100, this);
							}
						}
						else{ // printing the back of the cards
							g2.drawImage(backofcard, 120 + j*27, 25 + 132*i, 60, 100, this);
						}
					}

				}
			}

			// print the last hand on the table
			if (!game.getHandsOnTable().isEmpty()){
				Hand lastHand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
                for (int i = 0; i < lastHand.size(); i++){
                    int suit = lastHand.getCard(i).getSuit();
                    int rank = lastHand.getCard(i).getRank();
                    g.drawImage(cards[suit][rank], 10 + i*70, 553 , 60, 100, this);
                }
			}


			//printing the player names
			String youLabel = "You";

			//Print You if activeplayer else print player names
			//Printing the lines to divide each player panel 
			if (game.getClient() != null){
				for (int i = 0; i < playerPanels.length; i++){
					if (i == game.getClient().getPlayerID()){
						  g2.drawString(youLabel, 0,10 + 132*i);
						g2.drawLine(0, 132*i - 2, 750, 132*i - 2);
					}
					else{
						  g2.drawString(game.getPlayerList().get(i).getName(), 0,10 + 132*i);
						g2.drawLine(0, 132*i - 2, 750, 132*i - 2);
					}
				}
			}


			// Print last hand played by which player
			g2.drawLine(0, 526, 750, 526);
			if (!game.getHandsOnTable().isEmpty()){
				String lasthandplayer = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName();
				String tableLabel = "Played by "+ lasthandplayer;
				g2.drawString(tableLabel, 0, 538);
			}
			repaint();
		}

		// included due to abstract, not required
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		// included due to abstract, not required
		public void mousePressed(MouseEvent e) {		
		}

		@Override
		// select card if released
		public void mouseReleased(MouseEvent e) {
			if(!disableMouseListen){
				int numofCards = game.getPlayerList().get(game.getClient().getPlayerID()).getNumOfCards();
				int playeridx = game.getClient().getPlayerID();
				// If it is not the last card
				for (int i = 0; i < numofCards - 1; i++){
					//check y-axis
					if (e.getY() >= 25 + 132 * playeridx && e.getY() <= 125 + 132 * playeridx){
						//check x-axis
						if (e.getX() >= 120 + i * 27 && e.getX() < 147 + i * 27){
							if (selected[i] == false){
								selected[i] = true;
							}
							else{
								selected[i] = false;
							}
						}
					}
				}
				// if it is the last card
	
				// check y-axis
				if (e.getY() >= 25 + 132 * playeridx && e.getY() <= 125 + 132 * playeridx){
					// check x-axis
					if (e.getX() >= 120 + (numofCards -1) * 27 && e.getX() < 120 + (numofCards - 1) * 27 + 60){
						if (selected[numofCards - 1] == false){
							selected[numofCards -1] = true;
						}
						else{
							selected[numofCards - 1] = false;
						}
					}
				}
				this.repaint();
			}	
		}

		@Override
		// included due to abstract, not required
		public void mouseEntered(MouseEvent e) {			
		}

		@Override
		// included due to abstract, not required
		public void mouseExited(MouseEvent e) {			
		}
	}
}
