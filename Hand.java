/**
 * The Hand class is a abstract subclass of the CardList and is used to model a hand of card
 * with private varaible that stores players who play this hand and methods that model
 * a valid Hand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */
public abstract class Hand extends CardList{
    // public constructor of Hand
    public Hand(CardGamePlayer player, CardList cards){
        this.player = player;
        for (int i = 0; i < cards.size(); i++){
            this.addCard(cards.getCard(i));
        }
    }

    // private varaible that store the player
    private CardGamePlayer player;

    
    /** 
     * Getter of player
     * @return CardGamePlayer
     */
    public CardGamePlayer getPlayer(){
        return player;
    }

    
    /** 
     * Method that return the TopCard in this hand
     * @return Card
     */
    public Card getTopCard(){
        BigTwoCard TopCard = (BigTwoCard) getCard(0);
        for (int i = 0; i < this.size(); i++){
            if (TopCard.compareTo(getCard(i)) == -1){
                TopCard = (BigTwoCard) getCard(i);
            }
        }
        return TopCard;
    }

    
    /** 
     * Method that check whether this hand beats a certain hand
     * @param hand
     * @return boolean
     */
    public boolean beats(Hand hand){
        if (!this.isValid() || !hand.isValid() || (this.size() != hand.size())){
            return false;
        }
        BigTwoCard ThisTopCard = (BigTwoCard) this.getTopCard();
        BigTwoCard TopCard = (BigTwoCard) hand.getTopCard();
        if (this.size() == hand.size() & this.getType() == hand.getType() & ThisTopCard.compareTo(TopCard) == 1 & this.isValid() & hand.isValid()){
            return true;
        }
        return false;
    }

    public abstract boolean isValid(); //abstract method for check valid hand
    public abstract String getType(); //abstract method for getting the Type of hand

}
