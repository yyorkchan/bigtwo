/**
 *  Single class is a subclass of Hand which model a single hand
 *  and implement the method in Hand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class Single extends Hand{
    // Public constructor of Single
    public Single(CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    
    /** 
     * Check whether it is a valid single hand
     * @return boolean
     */
    public boolean isValid(){
        if (!this.isEmpty()){
            if (this.size() == 1){
                return true;
            }
        }
        return false;
    }

    
    /** 
     * Return type of hand
     * @return String
     */
    public String getType(){
        return "Single";
    }
}
