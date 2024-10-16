import java.util.*;

/**
 *  Straight class is a subclass of FiveHand which model a straight hand
 *  and implement the method in FiveHand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class Straight extends FiveHand{
    public Straight(CardGamePlayer player, CardList cards){
        super(player, cards);
    }
    
    /** 
     * Check whether the hand is  a valid straight or not
     * @return boolean
     */
    public boolean isValid(){
        if (!this.isEmpty()){
            if (this.size()== 5){
                int[] rank = new int[5];
                int temp;
                for (int i = 0; i < 5; i ++){
                    temp = this.getCard(i).getRank();
                    if (temp < 2){
                        temp += 13;
                    }
                    rank[i] = temp;
                }
                Arrays.sort(rank);
                for (int i = 0; i < 4; i++){
                    if (rank[i+1] - rank[i] != 1){
                        return false;
                    }
                }
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
        return "Straight";
    }
    
}
