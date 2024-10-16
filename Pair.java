/**
 *  Pair class is a subclass of Hand which model a pair hand
 *  and implement the method in Hand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class Pair extends Hand{
    public Pair(CardGamePlayer player, CardList cards){
        super(player, cards);
    }
    
    /** 
     * Check whether the hand is a valid pair or not
     * @return boolean
     */
    public boolean isValid(){
        if (!this.isEmpty()){
            if (this.size() == 2){
                if (this.getCard(0).getRank() == this.getCard(1).getRank()){
                    return true;
                }
            } 
        }
        return false;
    }
    
    /** 
     * Return the type of hand
     * @return String
     */
    public String getType(){
        return "Pair";
    }
    
}
