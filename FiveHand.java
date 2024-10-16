/**
 * The FiveHand class is a subclass of Hand which model a hand of 5 cards
 * It gives template of the beats method for any hand with 5 cards
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public abstract class FiveHand extends Hand{
    // Public constructor of FiveHand
    public FiveHand(CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    
    /** 
     * Method that check whether this hand beats a certain hand
     * @param hand
     * @return boolean
     */
    public boolean beats(Hand hand){
        // Return false if it is not valid or not the same size
        if (!this.isValid() || !hand.isValid() || (this.size() != hand.size())){
            return false;
        }
        BigTwoCard ThisTopCard = (BigTwoCard) this.getTopCard();
        BigTwoCard TopCard = (BigTwoCard) hand.getTopCard();

        // Return true if same type and both are valid and this beats the sepcified hand
        if (this.size() == hand.size() & this.getType() == hand.getType() & ThisTopCard.compareTo(TopCard) == 1 & this.isValid() & hand.isValid()){
            return true;
        }

        // String array that store the rankings of hand with 5 cards
        String[] FiveCards = new String[]{"Straight", "Flush", "Full House", "Quad", "Straight Flush"};

        // compare if it is not same type
        if (this.size() == hand.size() & this.size() == 5 & this.isValid() & hand.isValid()){
            int ThisBeat = -1, HandBeat = -1;
            for (int i = 0; i < FiveCards.length; i++){
                if (FiveCards[i] == this.getType()){
                    ThisBeat = i;
                }
                if (FiveCards[i] == hand.getType()){
                    HandBeat = i;
                }
            }
            if (ThisBeat > HandBeat){
                return true;
            }
        }
        return false;
    }
    public abstract boolean isValid(); //abstract method for check valid hand
    public abstract String getType(); //abstract method for getting the Type of hand

}
