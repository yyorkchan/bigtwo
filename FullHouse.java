/**
 *  Fullhouse class is a subclass of FiveHand which model a fullhouse hand
 *  and implement the method in FiveHand
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */


public class FullHouse extends FiveHand{
    // public constructor of Fullhouse
    public FullHouse(CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    
    /** 
     * Check whether the hand is a valid fullhouse
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
                if (count1 == 2 & count2 == 3){
                    return true;
                }
                if (count1 == 3 & count2 == 2){
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * Return the topcard
     * @return Card
     */
    public Card getTopCard(){
        this.sort();
        if (this.getCard(0).getRank()==this.getCard(2).getRank()){
            return this.getCard(2);
        }
        else if (this.getCard(2).getRank()==this.getCard(4).getRank()){
            return this.getCard(4);
        }
        return null;
    }

    
    /** 
     * Return type of hand
     * @return String
     */
    public String getType(){
        return "Full House";
    }
    
}
