//Arlan UY 2015-09385
package uy;
import java.io.*;
import java.util.*;

/**
* Implements the characteristics of the  draw pile
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class DrawPile extends LinkedStack implements PileAndDrawPileInterface {
    private int pile_number;
    protected Card drawn_card;
    private MainDrawPilePanel main_draw_pile_panel;
    private boolean add_cardfield = false;
    //The following numbers are useful for debugging
    //#0 pile pertains to the one that had just undergone the deckNumberSetter method in SolitaireDemo class
    //while #1, #2, #3 pertains to pile not yet shuffled, pile shuffled, sub_draw_pile

    public DrawPile(int pile_number) {
        this.pile_number = pile_number;
        kind = "Draw Pile";
    }

    public DrawPile(int pile_number, boolean add_cardfield) {
        this.pile_number = pile_number;
        kind = "Draw Pile";
        this.add_cardfield = add_cardfield;
    }

    public void setMainDrawPilePanel(MainDrawPilePanel main_draw_pile_panel) {
        this.main_draw_pile_panel = main_draw_pile_panel;
        main_draw_pile_panel.setMainDrawPile(this);
    }

    public MainDrawPilePanel getMainDrawPilePanel() {
        return main_draw_pile_panel;
    }

    public void push(Card x) {
        Node newN = new Node();
        newN.INFO = x;
        x.setFaceUp(false);
        newN.LINK = top;
        number_of_cards++;
        top = newN;
        drawn_card = top.INFO;
        if (add_cardfield == true) {
            CardField x_field = x.getCardField();
            main_draw_pile_panel.addCard(x_field);
        }
    }

    public void setPileNumber(int pile_number) {
        this.pile_number = pile_number;
    }

    public int getPileNumber() {
        return pile_number;
    }

    public Card getDrawnCard() {
        return drawn_card;
    }

    public void setDrawnCard() {
        try {
             Card x = peek();
             drawn_card = x;
        }
        catch(NullPointerException e) {
            System.out.println("This catch block in drawpile's setdrawncard method is ok to be empty");
        }
    }
    /**
    * Pushes the drawn cards which can only be either one or three cards
    * @param drawn_cards an array of Cards that is used by the classes of MainDrawPile or SubDrawPile in the SaveOrLoadStream class after being grouped into the draw_piles variable
    * For the case of SubDrawPile this variable is used to allow only one or three cards to be currently in the game display.
    * The execution of which is in the second if condition of the drawMainOrSubDrawPileCards method of the SubDrawPile class
    */
    @Override
   public void pushDrawnCards(Card[] drawn_cards) {
       //The drawn cards below are arranged from the first card you can use\n up to the last
      for (int i = drawn_cards.length - 1; i >= 0; i--) {
          if (drawn_cards[i] != null) {
              //If this method pushDrawnCards was called in the SubDrawPile class then the push method there will be the one executed by this line
              //but if not and instead the pushDrawnCards is called inside this class then the push method here will be executed instead
              push(drawn_cards[i]);
              //System.out.println("The card pushed is " + drawn_cards[i]);
           }
      }
      setDrawnCard();
  }

  @Override
   public Card[] copyCardsIntoArray() {
       boolean suppress_error = true, peek_mode = false;
       Card[] temp_cards = new Card[number_of_cards];
       //protected variable number_of_cards decrements by one whenever a card is popped and so we get its initial value only
       int saved_state_number_of_cards = number_of_cards;
       for(int i = 0; i < saved_state_number_of_cards; i++) {
           temp_cards[i] = pop(suppress_error, peek_mode);
       }
       return temp_cards;
    }

    @Override
    public Card[] copyCardsIntoArray(boolean suppress_error, boolean peek_mode) {
        Card[] temp_cards = new Card[number_of_cards];
        //protected variable number_of_cards decrements by one whenever a card is popped and so we get its initial value only
        int saved_state_number_of_cards = number_of_cards;
        for(int i = 0; i < saved_state_number_of_cards; i++) {
            temp_cards[i] = pop(suppress_error, peek_mode);
            //System.out.println("popped card from drawpile  is " + temp_cards[i]);
        }
        return temp_cards;
    }

   public void drawCard() {
       boolean suppress_error = true, peek_mode = false;
        Card x = peek();
       if (x.getSuit() != 'v') {
             drawn_card = pop(suppress_error, peek_mode);
            //System.out.println("drawn card in " + pile_number + " is " + drawn_card.toString());
            setDrawnCard();
       }
   }

   //overrides the toString method in the  Object Class
    public String toString(LinkedStack x) {
        String xpeek = "";
        if (x.peek().getSuit() == 'v') {
            xpeek = "non-existent";
        }

        else {
            xpeek = x.peek().toString();
        }
        return String.format("Its topmost card is %s  and the number of cards left is %d and its kind is a %s and its pile number is %d \n",
        xpeek, number_of_cards, kind, ((PileAndDrawPileInterface)x).getPileNumber());
    }

    public Card pop(boolean suppress_error, boolean peek_mode) {
        Card x = new Card();
        if (top == null) {
            if (suppress_error == false && peek_mode == false) {
                underflow(this);
                System.out.println("Entered pop and top is null");
            }
        }

        else {
              Node alpha  = top;
              x = alpha.INFO;
              top = alpha.LINK;
              setDrawnCard();
              number_of_cards--;
              if (add_cardfield == true) {
                  CardField x_field = x.getCardField();
                  main_draw_pile_panel.removeCard(x_field);
              }
              return x;

            }
        return x;
      }


    /**
    * Pops a random card out from the stack associated with a deck_number that is chosen by random
    * this method is not used by sub_draw_pile
    * @param num_card   erm coined to be the deck_number of a card that will be popped from the stack
    * @param suppress_error  suppresses the error thrown by an exception when true
    * @return j  returns the node at the given index
    */

      public Card popCardNumber(int num_card, boolean suppress_error) {
          Card x = new Card();
          if (top == null) {
              if (suppress_error == false) {
                  underflow(this);
              }
          }

          else {
              Node temp = top;
               for (int i = 1; i <= number_of_cards; i++) {
                   if (temp != null) {
                       if (temp.INFO.getDeckNumber() == num_card) {
                           Node alpha  = top;
                           x = alpha.INFO;
                           top = alpha.LINK;
                           number_of_cards--;
                           break;
                       }
                       else if ((temp.LINK != null) && (temp.LINK.INFO.getDeckNumber() == num_card)) {
                           Node alpha  = temp.LINK;
                           x = alpha.INFO;
                           temp.LINK = alpha.LINK;
                           number_of_cards--;
                           break;
                       }

                       else if ((temp.LINK == null) && (temp.INFO.getDeckNumber() == num_card)) {
                           x = temp.INFO;
                           temp = null;
                           number_of_cards--;
                           break;
                       }

                       else {
                           temp = temp.LINK;
                       }
                   }



                 }

             }
        return x;
    }

    public void printContent() {
        if (top == null) {
          System.out.println("The stack is empty");
        }

        else {
            int number = 0;
            System.out.println("the content of drawpile is: ");
            for (Node i = top; i != null; i = i.LINK) {
                number = i.INFO.getRank();
                if (i.INFO.getFaceUp() == false) {
                     System.out.print("Hidden |" );
                }

                else {
                    if (number == 13)
                        System.out.print("K");
                    else if (number == 12)
                        System.out.print("Q");
                    else if (number == 11)
                        System.out.print("J");
                    else if (number == 1)
                        System.out.print("A");
                    else
                        System.out.print(number);
                    System.out.print(" "+ i.INFO.getSuit() +" | ");
                }

            }
            System.out.println();
        }
        System.out.println("the content of maindrawpilepanel is: ");
        main_draw_pile_panel.printContent();

    }

  }
