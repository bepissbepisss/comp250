package assignment2;

import java.util.Random;

public class Deck {
	public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
	public static Random gen = new Random();

	public int numOfCards; // contains the total number of cards in the deck
	public Card head; // contains a pointer to the card on the top of the deck

	public void main() {
		int num = 4;
		int suits = 1;
		Deck d = new Deck(num,suits);
		Card c = d.head;
		System.out.println("Value of " + c + " is " + c.getValue());
		int i = num*suits+2;
		while (i >0) {
			i--;
			System.out.println(c);
			c = c.next;
		}
		System.out.println("---");
		//d.moveCard(new Joker("black"),1);
		System.out.println("d.head.prev is " + d.head.prev);
		d.head = d.head.prev;
		d.moveCard(new Joker("red"),5);
		c = d.head;
		i = num*suits+2;
		while (i >0) {
			i--;
			System.out.println(c);
			c = c.next;
		}
		System.out.println("---");
		d.tripleCut(d.locateJoker("black"),d.locateJoker("red"));
		c = d.head;
		i = num*suits+2;
		while (i >0) {
			i--;
			System.out.println(c);
			c = c.next;
		}




//		System.out.println("----");
//		Card b = new PlayingCard("C",2);
//		d.moveCard(b,3);
//		i = num*suits+2;
//		while (i >0) {
//			i--;
//			System.out.println(c);
//			c = c.next;
//		}
		/*
		System.out.println("---");

		Deck copy = new Deck(d);
		c = copy.head;
		int i = num+5;
		Card B = new PlayingCard("D",1);
		copy.addCard(B);
		while (i!=0) {
			i--;
			System.out.println(c);
			//if (c.toString().equals("BJ")) break;
			c = c.next;
		}
		System.out.println("There are " + copy.numOfCards + " cards");

		 */
	}

	/* 
	 * TODO: Initializes a Deck object using the inputs provided
	 */
	public Deck(int numOfCardsPerSuit, int numOfSuits) {
		if (numOfCardsPerSuit > 13 || numOfCardsPerSuit < 1) {
			throw new IllegalArgumentException("Bad deck size");
		}
		if (numOfSuits < 1 || numOfSuits > 4) {
			throw new IllegalArgumentException("Bad suit number");
		}
		numOfCards = numOfCardsPerSuit * numOfSuits +2;

		Card prevCard = null;
		Card nextCard = null;
		String[] suits = {"C", "D", "H", "S"};
		for (int i=0; i<numOfSuits; i++) {
			for (int j=0; j<numOfCardsPerSuit;j++) {
				nextCard = new PlayingCard(suits[i], j+1);
				if (prevCard!=null) {
					prevCard.next = nextCard;
					nextCard.prev = prevCard;
				}
				if (i == 0 && j==0) {
					head = nextCard;
				}
				prevCard=nextCard;
			}
		}
		Card rJoker = new Joker("red");
		Card bJoker = new Joker("black");


		assert nextCard != null;
		nextCard.next = rJoker;

		rJoker.prev = nextCard;
		rJoker.next = bJoker;
		bJoker.prev=rJoker;
		bJoker.next = head;
		head.prev = bJoker;
	}

	/*
	 * TODO: Implements a copy constructor for Deck using Card.getCopy().
	 * This method runs in O(n), where n is the number of cards in d.
	 */
	public Deck(Deck d) {
		Card deckCard = d.head;
		if (d.head == null) {
			numOfCards = 0;
			head = null;
			return;
		}
		Card prevCard = null;
		Card nextCard = deckCard.getCopy();
		head = nextCard;

		String firstCard = nextCard.toString();

		int num = 1;

		boolean firstTime = true;
		boolean firstIteration = true;
		while (true) {

			num++;
			if (!firstIteration) {
				nextCard = deckCard.getCopy();
			}
			firstIteration = false;
			//System.out.println("Copying card: " + nextCard);
			if (prevCard != null) {
				//System.out.println("PrevCard is good, linking");
				prevCard.next = nextCard;
				//System.out.println(prevCard + " -> " + nextCard);
				nextCard.prev = prevCard;
			}
			if (nextCard.toString().equals(firstCard)) {
				if (firstTime) {
					//System.out.println("Found an ace of clubs, first time");
					firstTime = false;
				} else {
					//System.out.println("Found an ace of clubs, breaking");
					prevCard.next = head;
					head.prev = prevCard;
					//System.out.println(prevCard + " -> " + head);
					numOfCards = num-2;
					break;
				}
			}

			prevCard = nextCard;
			deckCard = deckCard.next;



		}
	}

	/*
	 * For testing purposes we need a default constructor.
	 */
	public Deck() {}

	/*
	 * TODO: Adds the specified card at the bottom of the deck. This
	 * method runs in $O(1)$.
	 */
	public void addCard(Card c) {
		if (head == null) {
			numOfCards = 1;
			head = c;
			head.next = head;
			head.prev = head;
		} else {
			Card lastCard = head.prev;

			head.prev = c;
			lastCard.next = c;

			c.next = head;
			c.prev = lastCard;

			numOfCards++;
		}
	}

	/*
	 * TODO: Shuffles the deck using the algorithm described in the pdf.
	 * This method runs in O(n) and uses O(n) space, where n is the total
	 * number of cards in the deck.
	 */
	public void shuffle() {
		if (numOfCards == 0) return;
		Card[] cards = new Card[numOfCards];
		Card c = head;
		for(int i =0; i<numOfCards;i++){
			cards[i] =c;
			c = c.next;
		}
		//shuffle
		for(int i = numOfCards-1; i>=1; i--) {
			int j = gen.nextInt(i+1);
			Card temp = cards[j].getCopy();
			cards[j] = cards[i].getCopy();
			cards[i] = temp.getCopy();
		}

		//rebuild linked list
		Card prevCard = null;
		Card nextCard = null;
		for(int i =0; i<numOfCards-1; i++) {
			cards[i].next = cards[i+1];
			cards[i+1].prev = cards[i];
		}
		cards[numOfCards-1].next = cards[0];
		cards[0].prev = cards[numOfCards-1];
		head = cards[0];
	}

	/*
	 * TODO: Returns a reference to the joker with the specified color in
	 * the deck. This method runs in O(n), where n is the total number of
	 * cards in the deck.
	 */
	public Joker locateJoker(String color) {
		if (numOfCards ==0) return null;
		Card j = new Joker(color);
		Card c = head;
		String firstCard = c.toString();
		if (c.toString().equals(j.toString())) return (Joker) c;
		c = c.next;
		while (!c.toString().equals(firstCard)) {
			if (c.toString().equals(j.toString())) return (Joker) c;
			c = c.next;
		}

		return null;
	}

	/*
	 * TODO: Moved the specified Card, p positions down the deck. You can
	 * assume that the input Card does belong to the deck (hence the deck is
	 * not empty). This method runs in O(p).
	 */
	public void moveCard(Card c, int p) {
		// find card
		Card b = head;
		while(!c.toString().equals(b.toString())) {
			b = b.next;
		}
		Card B = b;
		Card A;
		Card C;
		Card D;
		//System.out.println("Found b: " + b);
		for (int i = 0; i<p; i++){
			A = B.prev;
			C = B.next;
			D = C.next;
			//System.out.println("Series went from " + A + " " + B + " " + C +" "+ D);

			A.next = C;
			C.prev = A;
			C.next = B;
			B.prev = C;
			B.next = D;
			D.prev = B;
			//System.out.println("Series went to:  " + B.prev.prev + " " + B.prev + " " + B +" "+ B.next);
			//System.out.println("------");
		}

	}

	/*
	 * TODO: Performs a triple cut on the deck using the two input cards. You
	 * can assume that the input cards belong to the deck and the first one is
	 * nearest to the top of the deck. This method runs in O(1)
	 */
	public void tripleCut(Card firstCard, Card secondCard) {
		Card A = firstCard;
		Card B = secondCard;
		Card C = null;
		Card D = null;
		Card H = null;
		Card E = null;
		Card lastCard = head.prev;

		if (secondCard == lastCard && firstCard == head) return;

		if (firstCard != head) {
			H = head;
			C = A.prev;
		}
		if (secondCard != lastCard) {
			D = B.next;
			E = head.prev;
		}

		// update second joker
		if (firstCard!=head) {
			B.next = H;
			H.prev = B;
		} else {
			B.next = D;
			D.prev = B;
		}

		//System.out.println("Card " + B + " points to " + B.next);

		// update first joker
		if (secondCard != lastCard) {
			A.prev = E;
			E.next = A;
		}  else {
			A.prev = C;
			C.next = A;
		}
		//System.out.println("Card " + E + " points to " + E.next);

		// update head
		if (firstCard!=head && secondCard != lastCard) {
			D.prev = C;
			C.next = D;
			head = D;
		} else if (secondCard == lastCard) {
			head = A;
		} else if (firstCard == head) {
			head = D;
		}






	}

	/*
	 * TODO: Performs a count cut on the deck. Note that if the value of the
	 * bottom card is equal to a multiple of the number of cards in the deck,
	 * then the method should not do anything. This method runs in O(n).
	 */
	public void countCut() {
		/**** ADD CODE HERE ****/
	}

	/*
	 * TODO: Returns the card that can be found by looking at the value of the
	 * card on the top of the deck, and counting down that many cards. If the
	 * card found is a Joker, then the method returns null, otherwise it returns
	 * the Card found. This method runs in O(n).
	 */
	public Card lookUpCard() {
		/**** ADD CODE HERE ****/
		return null;
	}

	/*
	 * TODO: Uses the Solitaire algorithm to generate one value for the keystream
	 * using this deck. This method runs in O(n).
	 */
	public int generateNextKeystreamValue() {
		/**** ADD CODE HERE ****/
		return 0;
	}


	public abstract class Card { 
		public Card next;
		public Card prev;

		public abstract Card getCopy();
		public abstract int getValue();

	}

	public class PlayingCard extends Card {
		public String suit;
		public int rank;

		public PlayingCard(String s, int r) {
			this.suit = s.toLowerCase();
			this.rank = r;
		}

		public String toString() {
			String info = "";
			if (this.rank == 1) {
				//info += "Ace";
				info += "A";
			} else if (this.rank > 10) {
				String[] cards = {"Jack", "Queen", "King"};
				//info += cards[this.rank - 11];
				info += cards[this.rank - 11].charAt(0);
			} else {
				info += this.rank;
			}
			//info += " of " + this.suit;
			info = (info + this.suit.charAt(0)).toUpperCase();
			return info;
		}

		public PlayingCard getCopy() {
			return new PlayingCard(this.suit, this.rank);   
		}

		public int getValue() {
			int i;
			for (i = 0; i < suitsInOrder.length; i++) {
				if (this.suit.equals(suitsInOrder[i]))
					break;
			}

			return this.rank + 13*i;
		}

	}

	public class Joker extends Card{
		public String redOrBlack;

		public Joker(String c) {
			if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
				throw new IllegalArgumentException("Jokers can only be red or black"); 

			this.redOrBlack = c.toLowerCase();
		}

		public String toString() {
			//return this.redOrBlack + " Joker";
			return (this.redOrBlack.charAt(0) + "J").toUpperCase();
		}

		public Joker getCopy() {
			return new Joker(this.redOrBlack);
		}

		public int getValue() {
			return numOfCards - 1;
		}

		public String getColor() {
			return this.redOrBlack;
		}
	}

}
