package assignment2;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Mini Tester for Assignment 2 (JUnit 5).
 * Basic structural checks + 30 open test cases covering all methods.
 */
public class A2MiniTest {

    private static String deckToString(Deck deck) {
        if (deck.head == null) return "";
        StringBuilder sb = new StringBuilder();
        Deck.Card curr = deck.head;
        do {
            sb.append(curr.toString()).append(" ");
            curr = curr.next;
        } while (curr != deck.head);
        return sb.toString();
    }

    // =====================================================================
    //  Basic Structural Checks
    // =====================================================================

    @Test
    public void test_ClassNamesExist() {
        String[] classNames = {"assignment2.Deck", "assignment2.SolitaireCipher"};
        for (String name : classNames) {
            try {
                Class.forName(name);
            } catch (ClassNotFoundException e) {
                fail("Expected class " + name + " not found.");
            }
        }
    }

    @Test
    public void test_DeckInnerClasses() {
        // Card, PlayingCard, Joker should be inner classes of Deck
        String[] innerNames = {"assignment2.Deck$Card", "assignment2.Deck$PlayingCard", "assignment2.Deck$Joker"};
        for (String name : innerNames) {
            try {
                Class.forName(name);
            } catch (ClassNotFoundException e) {
                fail("Expected inner class " + name + " not found. " +
                     "Make sure Card, PlayingCard, and Joker are inner classes of Deck.");
            }
        }
        // Card should be abstract
        try {
            Class<?> cardClass = Class.forName("assignment2.Deck$Card");
            assertTrue(Modifier.isAbstract(cardClass.getModifiers()),
                    "Deck.Card should be declared abstract.");
        } catch (ClassNotFoundException e) {
            fail("Deck$Card not found.");
        }
    }

    @Test
    public void test_DeckMethodNames() {
        String[] expectedMethods = {
            "addCard", "shuffle", "locateJoker", "moveCard",
            "tripleCut", "countCut", "lookUpCard", "generateNextKeystreamValue"
        };
        Class<?> deckClass;
        try {
            deckClass = Class.forName("assignment2.Deck");
        } catch (ClassNotFoundException e) {
            fail("Deck class not found.");
            return;
        }
        Method[] methods = deckClass.getDeclaredMethods();
        HashSet<String> methodNames = new HashSet<>();
        for (Method m : methods) methodNames.add(m.getName());
        for (String name : expectedMethods) {
            assertTrue(methodNames.contains(name),
                    "Deck class is missing expected method: " + name);
        }
    }

    @Test
    public void test_SolitaireCipherMethodNames() {
        String[] expectedMethods = {"getKeystream", "encode", "decode"};
        Class<?> cipherClass;
        try {
            cipherClass = Class.forName("assignment2.SolitaireCipher");
        } catch (ClassNotFoundException e) {
            fail("SolitaireCipher class not found.");
            return;
        }
        Method[] methods = cipherClass.getDeclaredMethods();
        HashSet<String> methodNames = new HashSet<>();
        for (Method m : methods) methodNames.add(m.getName());
        for (String name : expectedMethods) {
            assertTrue(methodNames.contains(name),
                    "SolitaireCipher class is missing expected method: " + name);
        }
    }

    // =====================================================================
    //  Proficiency (20 tests)
    // =====================================================================

    // -------------------- addCard --------------------

    @Test
    public void AddCard_SingleCard() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
        deck.addCard(c1);

        assertTrue(c1.prev == c1 && c1.next == c1,
                "A single card's next and prev should both point to itself.");
    }

    @Test
    public void AddCard_CheckHead() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); // 8C
        deck.addCard(c1);
        deck.addCard(c8);

        assertSame(c1, deck.head,
                "The head should be the first card added. Expected " + c1 + " but got " + deck.head);
    }

    @Test
    public void AddCard_Circular() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); // 8C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(c8);

        assertTrue(c1.prev == c8 && c8.next == c1,
                "Circular references are not correctly set up. " +
                "The last card's next should point to head, and head's prev to last card.");
    }

    @Test
    public void AddCard_AllRef() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);

        boolean c1ok = c1.next == c2 && c1.prev == c3;
        boolean c2ok = c2.next == c3 && c2.prev == c1;
        boolean c3ok = c3.next == c1 && c3.prev == c2;

        assertTrue(c1ok && c2ok && c3ok,
                "The next and prev references are not set correctly for a 3-card deck.");
    }

    @Test
    public void AddCard_NumOfCards() {
        Deck deck = new Deck();
        deck.addCard(deck.new PlayingCard(Deck.suitsInOrder[0], 1));
        deck.addCard(deck.new PlayingCard(Deck.suitsInOrder[0], 2));
        deck.addCard(deck.new PlayingCard(Deck.suitsInOrder[1], 11));
        deck.addCard(deck.new PlayingCard(Deck.suitsInOrder[0], 3));

        assertEquals(4, deck.numOfCards,
                "numOfCards should be 4 after adding 4 cards, but got " + deck.numOfCards);
    }

    // -------------------- deepCopy --------------------

    @Test
    public void DeepCopy_CheckRefs() {
        HashSet<Deck.Card> cardSet = new HashSet<>();
        Deck deck = new Deck();
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 1));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 3));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 5));
        cardSet.add(deck.new Joker("black"));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[1], 2));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[2], 4));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[3], 6));

        for (Deck.Card c : cardSet) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck);
        Deck.Card cur = copy.head;
        for (int i = 0; i < cardSet.size(); i++) {
            assertFalse(cardSet.contains(cur), "Deep copy must create new card objects, not reuse originals.");
            cur = cur.next;
        }
    }

    @Test
    public void DeepCopy_CircularNext() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck);
        Deck.Card cur = copy.head;

        for (int i = 0; i < cards.length; i++) {
            assertNotNull(cur, "Either head or a next pointer is null.");
            assertEquals(cards[i].getClass(), cur.getClass(),
                    "Card at position " + i + " has wrong type: expected " +
                    cards[i].getClass().getSimpleName() + " but got " + cur.getClass().getSimpleName());

            if (cur instanceof Deck.PlayingCard) {
                assertEquals(cards[i].getValue(), cur.getValue(),
                        "Card at position " + i + " has wrong value: expected " +
                        cards[i].getValue() + " but got " + cur.getValue());
            } else {
                String expected = ((Deck.Joker) cards[i]).getColor();
                String actual = ((Deck.Joker) cur).getColor();
                assertEquals(expected, actual,
                        "Joker at position " + i + " has wrong color: expected " + expected + " but got " + actual);
            }
            cur = cur.next;
        }
        assertSame(copy.head, cur, "The last card's next does not point back to head.");
    }

    // -------------------- locateJoker --------------------

    @Test
    public void LocateJoker_Red() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Deck.Card expected = deck.new Joker("red");

        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(expected);

        Deck.Card received = deck.locateJoker("red");
        assertSame(expected, received,
                "Expected the red Joker reference but got " + received);
    }

    @Test
    public void LocateJoker_Black() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card rj = deck.new Joker("red");
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Deck.Card expected = deck.new Joker("black");

        deck.addCard(c1);
        deck.addCard(rj);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(expected);

        Deck.Card received = deck.locateJoker("black");
        assertSame(expected, received,
                "Expected the black Joker reference but got " + received);
    }

    // -------------------- lookUpCard --------------------

    @Test
    public void LookUpCard_Simple() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC (value 1)
        Deck.Card expected = deck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C

        deck.addCard(c1);
        deck.addCard(expected);
        deck.addCard(c3);

        Deck.Card received = deck.lookUpCard();
        assertSame(expected, received,
                "Head is AC (value 1), so lookUpCard should return the 1st card after head. " +
                "Expected " + expected + " but got " + received);
    }

    @Test
    public void LookUpCard_JokerNull() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); // 8C (value 8)
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[2], 3);
        Deck.Card c4 = deck.new PlayingCard(Deck.suitsInOrder[2], 4);
        Deck.Card c5 = deck.new PlayingCard(Deck.suitsInOrder[2], 5);
        Deck.Card c6 = deck.new PlayingCard(Deck.suitsInOrder[2], 6);
        Deck.Card c7 = deck.new PlayingCard(Deck.suitsInOrder[2], 7);
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[2], 8);
        Deck.Card joker = deck.new Joker("red");

        deck.addCard(c1); deck.addCard(c2); deck.addCard(c3); deck.addCard(c4);
        deck.addCard(c5); deck.addCard(c6); deck.addCard(c7); deck.addCard(c8);
        deck.addCard(joker);

        Deck.Card received = deck.lookUpCard();
        assertNull(received,
                "lookUpCard should return null when the looked-up card is a Joker.");
    }

    // -------------------- moveCard --------------------

    @Test
    public void MoveCard_CheckNext1() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) deck.addCard(c);

        // Move 5C (index 2) down 3 positions
        Deck.Card[] expected = {cards[0], cards[1], cards[3], cards[4], cards[5], cards[2], cards[6]};
        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], cur, "At index " + i + ": expected " + expected[i] + " but got " + cur);
            cur = cur.next;
        }
    }

    @Test
    public void MoveCard_CheckNext2() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) deck.addCard(c);

        // Move BJ (index 3) down 4 positions — wraps around
        Deck.Card[] expected = {cards[0], cards[3], cards[1], cards[2], cards[4], cards[5], cards[6]};
        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], cur, "At index " + i + ": expected " + expected[i] + " but got " + cur);
            cur = cur.next;
        }
    }

    // -------------------- shuffle --------------------

    @Test
    public void Shuffle_Empty() {
        Deck deck = new Deck();
        deck.shuffle();
        assertNull(deck.head, "Shuffling an empty deck should leave head as null.");
    }

    @Test
    public void Shuffle_SingleCard() {
        Deck deck = new Deck();
        Deck.Card c = deck.new Joker("red");
        deck.addCard(c);
        deck.shuffle();

        assertTrue(deck.head.getValue() == c.getValue() &&
                c.next == c && c.prev == c,
                "A single-card deck should remain unchanged after shuffle.");
    }

    @Test
    public void Shuffle_Example() {
        Deck deck = new Deck();
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Deck.Card[] arrDeck = new Deck.Card[12];
        for (int i = 0; i < 10; i++) {
            int suit = i / 5;
            int rank = i % 5 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            arrDeck[i] = card;
            deck.addCard(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        arrDeck[10] = rj;
        arrDeck[11] = bj;
        deck.addCard(rj);
        deck.addCard(bj);

        Deck.gen.setSeed(10);
        deck.shuffle();

        // Expected: 3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D
        int[] shuffledIndex = {2, 7, 5, 4, 11, 1, 6, 8, 0, 10, 3, 9};

        Deck.Card cur = deck.head;
        for (int i = 0; i < 12; i++) {
            Deck.Card exp = arrDeck[shuffledIndex[i]];
            assertEquals(exp.getValue(), cur.getValue(),
                    "Shuffle with seed 10: card at index " + i + " should be " + exp + " but got " + cur);
            cur = cur.next;
        }
    }

    // -------------------- countCut --------------------

    @Test
    public void CountCut_Basic() {
        Deck deck = new Deck(5, 2);
        String expected = "AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ ";
        deck.countCut();
        assertEquals(expected, deckToString(deck),
                "countCut on Deck(5,2) failed.\nExpected: " + expected + "\nActual: " + deckToString(deck));
    }

    // -------------------- tripleCut --------------------

    @Test
    public void TripleCut_Standard() {
        Deck deck = new Deck();
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        Deck.Card c4 = deck.new PlayingCard(Deck.suitsInOrder[0], 4);
        Deck.Card c5 = deck.new PlayingCard(Deck.suitsInOrder[0], 5);
        Deck.Card c6 = deck.new PlayingCard(Deck.suitsInOrder[0], 6);
        // Deck: AC 2C RJ 3C 4C BJ 5C 6C
        deck.addCard(c1); deck.addCard(c2); deck.addCard(rj);
        deck.addCard(c3); deck.addCard(c4); deck.addCard(bj);
        deck.addCard(c5); deck.addCard(c6);
        // Expected: 5C 6C RJ 3C 4C BJ AC 2C
        deck.tripleCut(rj, bj);

        Deck.Card[] expected = {c5, c6, rj, c3, c4, bj, c1, c2};
        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], cur, "At index " + i + ": expected " + expected[i] + " but got " + cur);
            cur = cur.next;
        }
        assertSame(deck.head, cur, "Deck not circular after tripleCut.");
    }

    // -------------------- encode / decode --------------------

    @Test
    public void Encode_BasicMessage() {
        SolitaireCipher cipher = new SolitaireCipher(new Deck(13, 4));
        String result = cipher.encode("Is that you, Bob?");
        assertEquals("MPDFISXGUKWI", result,
                "Expected MPDFISXGUKWI but got " + result);
    }

    @Test
    public void Decode_BasicMessage() {
        SolitaireCipher cipher = new SolitaireCipher(new Deck(13, 4));
        String result = cipher.decode("MPDFISXGUKWI");
        assertEquals("ISTHATYOUBOB", result,
                "Expected ISTHATYOUBOB but got " + result);
    }

    // =====================================================================
    //  Approaching Mastery (8 tests)
    // =====================================================================

    // -------------------- deepCopy --------------------

    @Test
    public void DeepCopy_CircularPrev() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) deck.addCard(c);

        Deck copy = new Deck(deck);
        Deck.Card cur = copy.head;

        for (int j = 0; j < cards.length; j++) {
            int i = Math.floorMod(-j, cards.length); // i goes 0, n-1, n-2, ..., 1
            assertNotNull(cur, "Either head or a prev pointer is null.");

            assertEquals(cards[i].getClass(), cur.getClass(),
                    "Card at prev-position " + j + " has wrong type.");

            if (cur instanceof Deck.PlayingCard) {
                assertEquals(cards[i].getValue(), cur.getValue(),
                        "Card at prev-position " + j + " has wrong value: expected " +
                        cards[i].getValue() + " but got " + cur.getValue());
            } else {
                String expColor = ((Deck.Joker) cards[i]).getColor();
                String actColor = ((Deck.Joker) cur).getColor();
                assertEquals(expColor, actColor,
                        "Joker at prev-position " + j + " has wrong color: expected " + expColor + " but got " + actColor);
            }
            cur = cur.prev;
        }
        assertSame(copy.head, cur, "The last card's prev does not point back to head.");
    }

    // -------------------- locateJoker --------------------

    @Test
    public void LocateJoker_BothPresent() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card expected = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);

        deck.addCard(c1);
        deck.addCard(expected);
        deck.addCard(bj);
        deck.addCard(c2);
        deck.addCard(c3);

        Deck.Card received = deck.locateJoker("red");
        assertSame(expected, received,
                "With both jokers present, locateJoker(\"red\") should return the red Joker, not the black one.");
    }

    // -------------------- lookUpCard --------------------

    @Test
    public void LookUpCard_LargerDeck() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 6); // 6C (value 6)
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[1], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[2], 3);
        Deck.Card c4 = deck.new PlayingCard(Deck.suitsInOrder[2], 4);
        Deck.Card c5 = deck.new PlayingCard(Deck.suitsInOrder[2], 5);
        Deck.Card c6 = deck.new PlayingCard(Deck.suitsInOrder[2], 6);
        Deck.Card expected = deck.new PlayingCard(Deck.suitsInOrder[2], 7); // 7H — the 6th card after head
        Deck.Card c7 = deck.new PlayingCard(Deck.suitsInOrder[2], 8);
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[2], 9);

        deck.addCard(c1); deck.addCard(c2); deck.addCard(c3); deck.addCard(c4);
        deck.addCard(c5); deck.addCard(c6); deck.addCard(expected);
        deck.addCard(c7); deck.addCard(c8);

        Deck.Card received = deck.lookUpCard();
        assertSame(expected, received,
                "Head is 6C (value 6), so lookUpCard should return the 6th card after head (7H). " +
                "Got " + received);
    }

    // -------------------- moveCard --------------------

    @Test
    public void MoveCard_CheckPrev() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) deck.addCard(c);

        // Move 5C (index 2) down 3 positions — same move as CheckNext1, but verify prev pointers
        Deck.Card[] expected = {cards[0], cards[1], cards[3], cards[4], cards[5], cards[2], cards[6]};
        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length);
            assertEquals(expected[i], cur, "Prev traversal at step " + j + ": expected " + expected[i] + " but got " + cur);
            cur = cur.prev;
        }
    }

    // -------------------- tripleCut --------------------

    @Test
    public void TripleCut_JokerAtHead() {
        Deck deck = new Deck();
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        // Deck: RJ AC BJ 2C 3C
        deck.addCard(rj); deck.addCard(c1); deck.addCard(bj); deck.addCard(c2); deck.addCard(c3);
        // Expected: 2C 3C RJ AC BJ
        deck.tripleCut(rj, bj);

        Deck.Card[] expected = {c2, c3, rj, c1, bj};
        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], cur, "At index " + i + ": expected " + expected[i] + " but got " + cur);
            cur = cur.next;
        }
    }

    @Test
    public void TripleCut_JokerAtTail() {
        Deck deck = new Deck();
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1);
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2);
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3);
        // Deck: AC 2C RJ 3C BJ
        deck.addCard(c1); deck.addCard(c2); deck.addCard(rj); deck.addCard(c3); deck.addCard(bj);
        // Expected: RJ 3C BJ AC 2C
        deck.tripleCut(rj, bj);

        Deck.Card[] expected = {rj, c3, bj, c1, c2};
        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], cur, "At index " + i + ": expected " + expected[i] + " but got " + cur);
            cur = cur.next;
        }
    }

    // -------------------- generateNextKeystreamValue --------------------

    @Test
    public void GenKeystream_Deck13_4_First4() {
        Deck deck = new Deck(13, 4);
        int[] expected = {4, 49, 10, 24};
        for (int i = 0; i < expected.length; i++) {
            int received = deck.generateNextKeystreamValue();
            assertEquals(expected[i], received,
                    "Keystream value " + (i + 1) + " for Deck(13,4): expected " + expected[i] + " but got " + received);
        }
    }

    // -------------------- encode --------------------

    @Test
    public void Encode_MixedCase() {
        SolitaireCipher cipher = new SolitaireCipher(new Deck(13, 4));
        String result = cipher.encode("very VERY secret message!");
        assertEquals("ZBBWDDQQSNKYZHTCVRCOF", result,
                "Expected ZBBWDDQQSNKYZHTCVRCOF but got " + result);
    }

    // =====================================================================
    //  Mastery (2 tests)
    // =====================================================================

    // -------------------- generateNextKeystreamValue --------------------

    @Test
    public void GenKeystream_Deck1_1_AllOnes() {
        // Smallest possible deck: AC RJ BJ (3 cards). Every keystream value should be 1.
        Deck deck = new Deck(1, 1);
        for (int i = 0; i < 10; i++) {
            int val = deck.generateNextKeystreamValue();
            assertEquals(1, val,
                    "Deck(1,1) should always produce 1 but got " + val + " at iteration " + (i + 1));
        }
    }

    // -------------------- encode/decode round-trip --------------------

    @Test
    public void EncodeDecode_RoundTrip() {
        String msg = "HELLOWORLDTHISISATEST";
        SolitaireCipher encCipher = new SolitaireCipher(new Deck(13, 4));
        String ciphertext = encCipher.encode(msg);
        SolitaireCipher decCipher = new SolitaireCipher(new Deck(13, 4));
        String plaintext = decCipher.decode(ciphertext);
        assertEquals(msg, plaintext,
                "Encoding then decoding should recover the original message. Got " + plaintext);
    }
}
