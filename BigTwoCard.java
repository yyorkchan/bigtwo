/**
 * The BigTwoCard class is a subclass of Card and is to model
 * the card of the BigTwo game.
 * 
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class BigTwoCard extends Card{
	// public constructor of the BigTwoCard class
    public BigTwoCard(int suit, int rank){
		super(suit, rank);
    }

	
	/** 
	 * 	Override method that compare two Card in BigTwo way
	 * @param card
	 * @return int
	 */
    public int compareTo(Card card){
		int this_rank = this.rank;
		int card_rank = card.rank;
		if (this.rank < 2){
			this_rank += 13;
		}
		if (card.rank < 2){
			card_rank += 13;
		}
		if (this_rank > card_rank) {
			return 1;
		} else if (this_rank < card_rank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
