/**
 *  Quad class is a subclass of FiveHand which model a quad hand
 *  and implement the method in FiveHand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class Quad extends FiveHand{
    // Public constructor of Quad
    public Quad(CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    
    /** 
     * Check whether the hand is a valid quad
     * @return boolean
     */
    public boolean isValid(){
        int temp1, temp2 = -1, count1 = 5, count2 = 0;
        if (!this.isEmpty()){
            if (this.size()== 5){
                temp1 = this.getCard(0).getRank();
                for (int i = 1; i < 5; i++){
                    if (this.getCard(i).getRank() != temp1){
                        if (temp2 == -1){
                            temp2 = this.getCard(i).getRank();
                        }
                        count1 -= 1;
                    }
                    if (this.getCard(i).getRank() == temp2){
                        count2 += 1;
                    }
                }
                if (count1 == 1 & count2 == 4){
                    return true;
                }
                if (count1 == 4 & count2 == 1){
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
        return "Quad";
    }

    /**
     * Return the topcard
     * @return Card
     */
    public Card getTopCard(){
        this.sort();
        if (this.getCard(0).getRank()==this.getCard(3).getRank()){
            return this.getCard(3);
        }
        else if (this.getCard(1).getRank()==this.getCard(4).getRank()){
            return this.getCard(4);
        }
        return null;
    }
}
