/**
 *  Triple class is a subclass of Hand which model a triple hand
 *  and implement the method in Hand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class Triple extends Hand{
    public Triple(CardGamePlayer player, CardList cards){
        super(player, cards);
    }
    
    /** 
     * Check whether the hand is a valid triple or not
     * @return boolean
     */
    public boolean isValid(){
        if (!this.isEmpty()){
            if (this.size() == 3){
                if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()){
                    return true;
                }
            } 
        }
        return false;
    }
    
    /** 
     * Return type of hand
     * @return String
     */
    public String getType(){
        return "Triple";
    }
    
}
