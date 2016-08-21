//Arlan UY 2015-09385
package uy;
import java.io.*;
import java.util.*;

/**
* implements the characteristics specific to a suit pile
* @author  Arlan Uy
* @version 1.8.0_60
*/
public class SuitPile extends LinkedStack implements SuitPileAndPileInterface{
    private char suit_pile;
    private SuitPilePanel suit_pile_panel;

    public SuitPile (char suit_pile) {
        this.suit_pile = suit_pile;
        kind = "Suit Pile";
    }

    public void setSuitPilePanel(SuitPilePanel suit_pile_panel) {
    this.suit_pile_panel = suit_pile_panel;
    suit_pile_panel.setSuitPile(this);
}

    public void setSuitPile(char suit_pile) {
        this.suit_pile = suit_pile;
    }

    public char getSuitPile() {
        return suit_pile;
    }

    public String toString(LinkedStack x) {
        String xpeek = "";
        if (x.peek().getSuit() == 'v') {
            xpeek = "non-existent";
        }

        else {
            xpeek = x.peek().toString();
        }
        return String.format("Its topmost card is %s  and the number of cards left is %d and its kind is a %s and its pile suit is %c\n",
        xpeek, number_of_cards, kind, ((SuitPile)x).getSuitPile());
    }

    public boolean checkForWin() {
        int count_cards = 0;
        for (Node i = top; i != null; i = i.LINK) {
            count_cards++;
        }
        if (count_cards == 13) {
            return true;
        }

        else {
            return false;
        }
    }

    public void push(Card x) {
        Node newN = new Node();
        newN.INFO = x;
        newN.LINK = top;
        if (top != null) {
            top.INFO.setFaceUp(false);
        }
        top = newN;
        number_of_cards++;
        suit_pile_panel.addCard(x.getCardField());
        x.setFaceUp(true);
    }

    public boolean checkPush(Card x, Card temp) {
        boolean set_face_up = (x.getFaceUp() == true);
        boolean null_suit = (temp.getSuit() != 'v');
        boolean num_check = (x.getRank() == temp.getRank() + 1);
        boolean suit_check = (x.getSuit() == temp.getSuit());
        if ((set_face_up == true)) {
            if ((temp.getSuit() == 'v') && x.getRank() == 1) {
                return true;
            }

            else if ((null_suit == true) && (num_check == true) && (suit_check == true)) {
                return true;
            }

            else {
                return false;
            }
        }

        else {
            return false;
        }
    }

    public Card pop(boolean suppress_error, boolean peek_mode) {
        Card x = new Card();
        if (top == null) {
            if (suppress_error == false) {
                underflow(this);
            }

        }

        else {
              Node alpha  = top;
              x = alpha.INFO;
              top = alpha.LINK;
              if (top != null) {
                top.INFO.setFaceUp(true);
              }
              suit_pile_panel.removeCard(x.getCardField());
              number_of_cards--;
              return x;

            }
        return x;
      }

      public Card peek() {
          boolean peek_mode = true;
          boolean suppress_error = true;
          Card x = pop(suppress_error, peek_mode);
          if (x.getSuit() != 'v')
            push(x);
          return x;
      }

}
