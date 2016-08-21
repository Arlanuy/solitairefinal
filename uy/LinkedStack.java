//Arlan UY 2015-09385
package uy;
import java.io.*;
import java.util.*;

/**
* Implements the characteristics common to all stacks
* Note: In this program, the words "Stack" and "Pile" are used  interchangeably
* @author  Arlan Uy
* @version 1.8.0_60
*/


public abstract class LinkedStack{
    protected Node top = null;
    protected int number_of_cards = 0;
    protected int saved_state_number_of_cards;
    protected String kind;

    public int getNumberOfCards() {
        return number_of_cards;
    }

    public String getKind() {
        return kind;
    }

    public void setNumberCards(int number_of_cards) {
        this.number_of_cards = number_of_cards;
    }
    /**
    * By using a loop, this method moves a node's reference to its LINK field and then the one linked to the said LINK field and so on
    * @param num_node  term coined to be the index at a given node
    * @return j  returns the node at the given index
    */
    public Node getIndexNode(int num_node) {
        Node j = top;
        for (int i = 1; i != num_node; i++) {
            j = j.LINK;
        }
        return j;
    }

    public void printContent() {
        if (top == null) {
          System.out.println("The stack is empty");
        }

        else {
            int number = 0;
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

    }

    /**
    * Pushes a card to the top of a subclass' stack namely those of the stacks
    * of DrawPile, Pile, SuitPile
    * @param x  the card to be pushed to the stack
    */
    public abstract void push(Card x);

    /**
    * Prints information regarding the LinkedStack x
    * @param x  Linked stack under string representation
    * @return   returns the string representation of a linked stack
    */

     public abstract String toString(LinkedStack x);

     /**
     * Pops a node from the top of a subclass' stack namely those of the stacks
     * of DrawPile, Pile, SuitPile
     * @param suppress_error  suppresses the error thrown by an exception when true
     * @param peek_mode  prints error when false because of the exception embedded on the peek function
     * @return  the Card object in the  INFO field of the node popped
     */

    public abstract Card pop(boolean suppress_error, boolean peek_mode);

    /**
    * Pushes the drawn cards which can only be either one or three cards
    * @param drawn_cards an array of Cards that is used by the classes of PilesField and SuitPilesField
    * in the SaveOrloadStream class after being grouped into the variable grouped_piles
    * to allow only one or three cards to be currently in the game display
    * and not all the cards in the SubDrawPilePanel
    */
    public void pushDrawnCards(Card[] drawn_cards) {
       for (int i = drawn_cards.length - 1; i >= 0; i--) {
           if (drawn_cards[i] != null) {
               push(drawn_cards[i]);
            }
       }
   }

    public Card[] copyCardsIntoArray() {
        boolean suppress_error = true, peek_mode = false;
        Card[] temp_cards = new Card[number_of_cards];
        //protected variable number_of_cards decrements by one whenever a card is popped and so we get its initial value only
        int saved_state_number_of_cards = number_of_cards;
        //System.out.println("Saved state number card is " + saved_state_number_of_cards);
        for(int i = 0; i < saved_state_number_of_cards; i++) {
            temp_cards[i] = pop(suppress_error, peek_mode);
            //System.out.println("Temp card is " + temp_cards[i]);
        }
        return temp_cards;
    }

    public Card[] copyCardsIntoArray(boolean suppress_error, boolean peek_mode) {
        Card[] temp_cards = new Card[number_of_cards];
        //protected variable number_of_cards decrements by one whenever a card is popped and so we get its initial value only
        int saved_state_number_of_cards = number_of_cards;
        //System.out.println("saved state  number card is " + saved_state_number_of_cards);
        for(int i = 0; i < saved_state_number_of_cards; i++) {
            temp_cards[i] = pop(suppress_error, peek_mode);
            //System.out.println("Temp card is " + temp_cards[i]);
        }
        return temp_cards;
    }


      public void underflow(LinkedStack x) {
        System.out.println("There are no more cards left in the current pile");
        try {
            throw new StackUnderFlowException(x);
        }

        catch (StackUnderFlowException e){
            System.out.println("Invalid move");
        }
      }

      public boolean isEmpty() {
        return (top == null);
      }

      /**
      * Peeks through the top card of a pile but if the pile is empty then it just returns
      * a card denoting null or the term "doesn't exist" which is checked in the course of execution accordingly
      * @return  returns a reference to the Card Object at the top of a file
      */


      public Card peek() throws NullPointerException{
          boolean peek_mode = true;
          boolean suppress_error = true;
          Card x = pop(suppress_error, peek_mode);
          if (x.getSuit() != 'v') {
            push(x);
          }

          return x;
      }


}
