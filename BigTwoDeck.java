/**
 * The BigTwoDeck is a subclass of Deck and is to model the deck
 * used in BigTwo card game.
 * @author Chan Yuk Yin (UID: 3035786574)
 */

public class BigTwoDeck extends Deck{
	// Public overrided method that initialize the deck with BigTwoCard
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}
}
