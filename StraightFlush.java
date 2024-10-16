import java.util.*;

/**
 *  Quad class is a subclass of FiveHand which model a quad hand
 *  and implement the method in FiveHand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */
public class StraightFlush extends FiveHand{
    // Public constructor of straightflush
    public StraightFlush(CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    
    /** 
     * Check whether the hand is a valid straight flush
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
                if (this.size() == 5 && this.getCard(0).getSuit() == this.getCard(1).getSuit() & this.getCard(1).getSuit() == this.getCard(2).getSuit() & this.getCard(2).getSuit() == this.getCard(3).getSuit() & this.getCard(3).getSuit() == this.getCard(4).getSuit()){
                    return true;
                }
            }
        }
        return false;
    }

    
    /** 
     * Return the type of the hand
     * @return String
     */
    public String getType(){
        return "Straight Flush";
    }

    
}
