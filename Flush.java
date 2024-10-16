/**
 *  Flush class is a subclass of FiveHand which model a flush hand
 *  and implement the method in FiveHand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class Flush extends FiveHand{
    public Flush(CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    
    /** 
     * Overrided beats method that compare hand in Flush way
     * @param hand
     * @return boolean
     */
    public boolean beats(Hand hand){
        // return false if it is not valid
        if (!this.isValid() || !hand.isValid() || (this.size() != hand.size())){
            return false;
        }
        int ThisSuit = this.getCard(0).getSuit(), HandSuit = hand.getCard(0).getSuit();
        int check = 0; //checker of beats 0: false 1: true
        BigTwoCard ThisTopCard = (BigTwoCard) this.getTopCard();
        BigTwoCard TopCard = (BigTwoCard) hand.getTopCard();
        // compare hands if they have the same type
        if (ThisSuit > HandSuit || (ThisSuit == HandSuit & ThisTopCard.compareTo(TopCard) == 1)){
            check = 1;
        }
        if (this.size() == hand.size() & this.getType() == hand.getType() & check == 1 & this.isValid() & hand.isValid()){
            return true;
        }

        // String array that store the rankings of hands with 5 cards
        String[] FiveCards = new String[]{"Straight", "Flush", "Full House", "Quad", "Straight Flush"};

        // compare hands with 5 cards which have different type 
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

    
    /** 
     * Check whether this hand is a  valid flush
     * @return boolean
     */
    public boolean isValid(){
        if (!this.isEmpty()){
            if (this.size() == 5 && this.getCard(0).getSuit() == this.getCard(1).getSuit() & this.getCard(1).getSuit() == this.getCard(2).getSuit() & this.getCard(2).getSuit() == this.getCard(3).getSuit() & this.getCard(3).getSuit() == this.getCard(4).getSuit()){
                return true;
            }
        }
        return false;
    }

    
    /** 
     * Return type of this hand
     * @return String
     */
    public String getType(){
        return "Flush";
    }
}
