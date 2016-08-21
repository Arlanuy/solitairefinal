//Arlan UY 2015-09385
package uy;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.lang.Thread;

/**
* This is the entry program of this Solitaire Game
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class SolitaireDemo{
    private static SolitaireGUI game_display;
    private static LinkedStack[] piles = new Pile[7]; // the playing area piles
    private static LinkedStack main_draw_pile = new DrawPile(1); //the main draw pile
    private static DrawPile sub_draw_pile = new SubDrawPile(3);
    // the pile consisting of 3 cards  wherein these are the only cards
    //you can use at a certain moment of playing
    //the temporary pile where the cards from the sub_draw_pile will go after choosing 0 in the selection of the mainMenu0
    private static LinkedStack[] suit_piles = new SuitPile[4]; // the piles needed to be satisfied to achieve
    private static char[] suitschar = new char[4];
    private static MainDrawPilePanel main_draw_pile_panel = new MainDrawPilePanel();
    private static SubDrawPilePanel sub_draw_pile_panel = new SubDrawPilePanel();
    private static SuitPilesField suit_piles_field = new SuitPilesField();
    private static SuitPilePanel suit_piles_panel[] = new SuitPilePanel[4];
    private static PilesField piles_field = new PilesField();
    private static PilePanel piles_panel[] = new PilePanel[7];
    private static DrawPile[] draw_piles = new DrawPile[2];
    private static LinkedStack[] grouped_piles = new LinkedStack[11];
    private static boolean restarted = false, check_win = false, renew_sub_draw_pile = false, prepared_game_already = false;;
    private static SolitaireGameCards prepared_game = new SolitaireGameCards(main_draw_pile, sub_draw_pile, piles, suit_piles);
    private static FileChooserDemo file_chooser = new FileChooserDemo();
    /**
    * Entry point in this class. Gives the overview of the program.
    * @param args command line arguments
    * */

      public static void main(String[] args) {
          if (check_win == false) {
              main_draw_pile = prepared_game.getMainDrawPile();
              sub_draw_pile = prepared_game.getSubDrawPile();
              piles = prepared_game.getPiles();
              suit_piles = prepared_game.getSuitPiles();
              createSuits(suitschar);
              assignStacks(piles, suit_piles);
              ((SubDrawPile)sub_draw_pile).setSubDrawPilePanel(sub_draw_pile_panel);
              piles_field.assignPiles((Pile[])piles, piles_panel);
              suit_piles_field.assignSuitPiles((SuitPile[])suit_piles, suit_piles_panel);
              check_win = prepared_game.getCheckWin();
              prepared_game = new SolitaireGameCards(main_draw_pile, sub_draw_pile, piles, suit_piles);
          }

        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            game_display = new SolitaireGUI(main_draw_pile_panel, sub_draw_pile_panel, suit_piles_field, piles_field);
            prepareGameThread();
            game_display.changeDisplayWinContent("");
          }
        });
      }

      public static synchronized void prepareGameThread() {
        new Thread(new Runnable() {
              public void run() {
                  prepared_game = prepareGame(prepared_game);
                  prepared_game_already = true;
              }
          }).start();
      }

     public static boolean checkWin() {
         for (int i = 0; i < 4; i++) {
             if (((SuitPile)suit_piles[i]).checkForWin() == false) {
                 return false;
             }
         }
         return true;
     }

    public static void checkWinThread() {
             if (suit_piles != null && checkWin() == true) {
                 game_display.changeDisplayWinContent("You win congratulations");
             }

             else {
                 game_display.changeDisplayWinContent("Waiting");
             }

             System.out.println("waited checkwin");
    }

      public static synchronized void emptyStacks() {
          boolean suppress_error = false, peek_mode = false;
          //System.out.println("gone through here at emptystacks method");
          while (main_draw_pile.isEmpty() == false) {
              main_draw_pile.pop(suppress_error, peek_mode);
          }
          int h = 0;
          while (sub_draw_pile.isEmpty() == false) {
             Card x = sub_draw_pile.pop(suppress_error, peek_mode);
             if (h % ((SubDrawPile)sub_draw_pile).getSubDrawPileNumCards() == 0) {
                ((SubDrawPile)sub_draw_pile).resetFillEmpty();
             }
             h++;
             ((SubDrawPile)sub_draw_pile).initializeDrawnCards();
             //System.out.println("popped " + x +" in subdrawpile at the method emptyStacks");
          }

          for (int i = 0; i < piles.length; i++) {
              while (piles[i].isEmpty() == false) {
                  piles[i].pop(suppress_error, peek_mode);
              }
          }

          for (int i = 0; i < suit_piles.length; i++) {
              while (suit_piles[i].isEmpty() == false) {
                  suit_piles[i].pop(suppress_error, peek_mode);
              }
          }
      }

      /**
      * Distributes  the card from the draw_pile to  the piles
      * @param game  the current stacks used by the program, I desperately need this variable because in Java everything is passed by value
      * what I mean by that is that is like for example when main passed the game variable as an argument to this prepareGame method
      * and then suppose in this method the return type is void and I used inside here the statement "game = prepared_game"
      * then soon after the execution of prepareGame ends, leaving the execution back to main then what happens is that back in
      * the main method, game isn't really referenced to variable "prepared_game" but instead to the original variable game instead
      * before the execution of prepareGame. This can be solved by using return statement though, I can only return one variable and hence
      * I grouped the piles as one in the object datatype SolitaireGameCards
      * @return the prepared game */

      public static synchronized SolitaireGameCards prepareGame(SolitaireGameCards game) {
          piles = game.getPiles();
          LinkedStack temp_main_draw_pile = game.getMainDrawPile();
          char color, suit, suit_filename, rank_filename;
          boolean faceup, suppress_error = false, peek_mode = false;
          Card new_card;
          for (int i = 0; i < 4; i++) {
              for (int j = 1; j < 14; j++) {
                  switch (i) {
                      case 0:
                          suit = 'H';
                          suit_filename = '0';
                          break;
                      case 2:
                          suit = 'S';
                          suit_filename = '2';
                          break;
                      case 1:
                          suit = 'D';
                          suit_filename = '1';
                          break;
                      case 3:
                          suit = 'F';
                          suit_filename = '3';
                          break;
                      default:
                          suit = 'v';
                  }
                  if (i < 2) {
                      color = 'R';
                  }

                  else {
                      color = 'B';
                  }
                  if (j == 1) {
                     rank_filename = 'A';
                  }

                  else if (j == 10) {
                      rank_filename = 'T';
                  }

                  else if (j == 11) {
                      rank_filename = 'J';
                  }

                  else if (j == 12) {
                      rank_filename = 'Q';
                  }

                  else if (j == 13) {
                      rank_filename = 'K';
                  }

                  else {
                      rank_filename = ' ';
                  }

                  String img_filename = "";
                   if (rank_filename != ' ') {
                       img_filename = String.format(i + "-" + rank_filename + ".png");
                       //System.out.println("img_filename is " + img_filename);
                       CardField card_field = new CardField(img_filename);
                       new_card = new Card(false, color, suit, j, card_field);
                       card_field.setCardLogic(new_card);
                   }

                   else if (suit != 'v'){
                       img_filename = String.format(i + "-" + j + ".png");
                       CardField card_field = new CardField(img_filename);
                       new_card = new Card(false, color, suit, j, card_field);
                       card_field.setCardLogic(new_card);
                   }

                   else {
                      new_card = new Card();
                   }
                  temp_main_draw_pile.push(new_card);
              }
          }
          temp_main_draw_pile = deckNumberSetter(temp_main_draw_pile);
          int very_new_quant_of_cards = temp_main_draw_pile.getNumberOfCards();
          //System.out.println("latest quant_of_cards: " + very_new_quant_of_cards);
          temp_main_draw_pile = shuffle(temp_main_draw_pile);
          ((SubDrawPile)sub_draw_pile).setMainDrawPile(temp_main_draw_pile);
          int shufled_quant_of_cards = temp_main_draw_pile.getNumberOfCards();
        //   /System.out.println("shuffled quant_of_cards: " + shufled_quant_of_cards);

          for (int i = 1; i < 8; i++) {
              for (int j = 0; j < i; j++) {
                  Card x = temp_main_draw_pile.pop(suppress_error, peek_mode);
                  if (j != i - 1) {
                      x.setFaceUp(false);
                  }
                  piles[i - 1].push(x);
              }
          }
          main_draw_pile = temp_main_draw_pile;
          formStacksInGroup((DrawPile)main_draw_pile, sub_draw_pile, piles, suit_piles);
          SolitaireGameCards prepared_game = new SolitaireGameCards(main_draw_pile, sub_draw_pile, piles, suit_piles);
          //System.out.println("Print content in prepareGame method: having number of cards " + main_draw_pile.getNumberOfCards());
          //main_draw_pile.printContent();
          checkWinThread();
          return prepared_game;
      }

      public static synchronized void newGame() {
          Thread restart_thread = new Thread(new Runnable() {
                public void run() {
                    System.out.println("restarting ongoing");
                    //the emptystacksmethod is done only in order to animate the popping of cards event
                    //the emptying of stacks will be done in the logic part of the code
                    //regardless if the emptyStacks method executes or not because of the withdrawAndDealAgain method
                    //((SubDrawPile)sub_draw_pile).resetFillEmpty();
                    emptyStacks();
                    prepareGameThread();
                }
            });
            restart_thread.start();
      }

      public static synchronized void saveGame () {
          File file = file_chooser.doCommand("save");
          if (file != null) {
              try {
                    //System.out.println("file save " + file.getAbsoluteFile());
                    FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
                    SaveOrLoadStream stream = new SaveOrLoadStream(fos);
                    Thread file_thread = new Thread(new Runnable() {
                          public void run() {
                              try {
                                  //System.out.println("file save ongoing");
                                  stream.save((DrawPile)main_draw_pile, draw_piles, grouped_piles, check_win);
                              }

                              catch(IOException e) {
                                  e.printStackTrace();
                              }

                          }
                      });
                      file_thread.start();
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }
          }
      }

      public static synchronized void loadGame() {
          File file = file_chooser.doCommand("load");
          if (file != null) {
              try {
                     //System.out.println("file load " + file.getAbsoluteFile());
                     FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
                     SaveOrLoadStream stream = new SaveOrLoadStream(fis);
                     Thread file_thread = new Thread(new Runnable() {
                           public void run() {
                               try {
                                   //System.out.println("file load ongoing");
                                   emptyStacks();
                                   prepared_game = stream.load(prepared_game);
                               }

                               catch(IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
                       file_thread.start();
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }
          }
      }

    public static void createSuits(char[] suitschar) {
        suitschar[0] = 'H';
        suitschar[1] = 'S';
        suitschar[2] = 'D';
        suitschar[3] = 'F';
    }

    /**
    * Creates the piles and suit_piles needed by the program and assigns them to grouped_piles
    * @param piles  the piles to where you can put an arrangement of cards (must be arranged from King to Ace with Ace on the front of all cards))
    * @param suit_piles  the piles to where you can put an arrangement of cards that can make you won the game upon
    * filling each of the four piles by 13 cards from the same suit (must be arranged from  Ace to  King with Ace on the front of all cards))
    */

    public static void assignStacks (LinkedStack[] piles, LinkedStack[] suit_piles) {
        for (int i = 0; i < piles.length; i++) {
            grouped_piles[i] = new Pile(i + 1);
            piles[i] = (LinkedStack)grouped_piles[i];
        }

        for (int i = 0; i < suit_piles.length; i++) {
            grouped_piles[i + 7] = new SuitPile(suitschar[i]);
            suit_piles[i] = (LinkedStack)grouped_piles[i + 7];
        }
    }


    /**
    * Assigns each element in the grouped_piles array with the corresponding pile or suit pile
    * @param main_draw_pile the piles to where you can draw the cards
    * @param sub_draw_pile the pile to where your drawn card/s will go after drawing
    * @param piles  the piles to where you can put an arrangement of cards (must be arranged from King to Ace with Ace on the front of all cards)
    * @param suit_piles  the piles to where you can put an arrangement of cards that can make you won the game upon
    * filling each of the four piles by 13 cards from the same suit (must be arranged from  Ace to King with Ace on on the front of all cards)
    */
    public static void formStacksInGroup(DrawPile main_draw_pile, DrawPile sub_draw_pile, LinkedStack[] piles, LinkedStack[] suit_piles) {
        draw_piles[0] = main_draw_pile;
        draw_piles[1] = sub_draw_pile;

        for (int i = 0; i < piles.length; i++) {
            grouped_piles[i] = piles[i];
        }

        for (int i = 0; i < suit_piles.length; i++) {
            grouped_piles[i + 7] = suit_piles[i];
        }
    }

    /**
    * Creates the stacks needed by the program
    * @param draw_piles  contains main_draw_pile and sub_draw_pile at index 0 and 1 respectively
    * @param grouped_piles  the only methods in this interface are checkPush() and getKind()
    */
    public static void separateStacksInPieces (DrawPile[] draw_piles,LinkedStack[] grouped_piles) {
        main_draw_pile = draw_piles[0];
        sub_draw_pile = draw_piles[1];
        for (int i = 0; i < 11; i++) {
            if (i < 7) {
                piles[i] = grouped_piles[i];
            }

            else {
                suit_piles[i - 7] = grouped_piles[i];
            }
        }
    }

    public static void printScreen(LinkedStack drawpile, DrawPile sub_draw_pile, LinkedStack[] piles, LinkedStack[] suit_piles) {
        System.out.println();
        System.out.println("Drawn Cards");
        sub_draw_pile.printContent();
        System.out.println();
        System.out.println("Suit Piles");
        for (int i = 0; i < 4; i++) {
            System.out.print(((SuitPile)suit_piles[i]).getSuitPile() + ".) ");
            suit_piles[i].printContent();
        }
        System.out.println();
        System.out.println("Piles");
        for (int i = 0; i < 7; i++) {
            System.out.print(((Pile)piles[i]).getPileNumber() + ".) ");
            piles[i].printContent();
        }
        System.out.println();
    }

    public static boolean doSubMenu1Choice(int index_destination_child) {
        boolean suppress_error = false, peek_mode = false;
        Card src_card = sub_draw_pile.getDrawnCard();
        Card dest_card = suit_piles[index_destination_child].peek();
        if (src_card == null) {
            try {
                throw new StackUnderFlowException(sub_draw_pile);
            }

            catch (StackUnderFlowException e) {

            }
        }

        else {
            //System.out.println("Executing dosubmenu1choice");
            if (((SuitPile)suit_piles[index_destination_child]).getSuitPile() == src_card.getSuit()) {
                if (((SuitPileAndPileInterface)suit_piles[index_destination_child]).checkPush(src_card, dest_card)) {
                    suit_piles[index_destination_child].push(src_card);
                    ((SubDrawPile)sub_draw_pile).popWithRefill();
                    return true;
                }

                else {
                    try {
                        throw new InvalidPushException(src_card, piles[index_destination_child]);
                    }

                    catch (InvalidPushException e){
                    }
                }
            }
        }
        //System.out.println("indosubmenu1");
        //((SubDrawPile)sub_draw_pile).printDrawnCards();
        return false;
    }

    public static boolean doSubMenu2Choice(int index_destination_child) {
        boolean suppress_error = false, peek_mode = false;
        Card src_card = sub_draw_pile.getDrawnCard();
        Card dest_card = piles[index_destination_child].peek();

        if (src_card == null) {
            try {
                throw new StackUnderFlowException(sub_draw_pile);
            }

            catch (StackUnderFlowException e) {

            }
        }

        else {
            //System.out.println("Executing dosubmenu2choice");
            if (((SuitPileAndPileInterface)piles[index_destination_child]).checkPush(src_card, dest_card)) {
                ((Pile)piles[index_destination_child]).push(src_card);
                ((SubDrawPile)sub_draw_pile).popWithRefill();
                return true;
            }

            else {
                try {
                    throw new InvalidPushException(src_card, piles[index_destination_child]);
                }

                catch (InvalidPushException e){
                }
            }
            //System.out.println("indosubmenu2");
            //((SubDrawPile)sub_draw_pile).printDrawnCards();
        }
        return false;
    }

    public static boolean doSubMenu3Choice(int index_source_child, int index_destination_child) {
        boolean suppress_error = true, peek_mode = false;
        Card src_card = piles[index_source_child].peek();
        //System.out.println("Executing dosubmenuchoice3");
        Card dest_card = suit_piles[index_destination_child].peek();
        if (((SuitPile)suit_piles[index_destination_child]).getSuitPile() == src_card.getSuit()) {
            if (((SuitPileAndPileInterface)suit_piles[index_destination_child]).checkPush(src_card, dest_card)) {
                suit_piles[index_destination_child].push(src_card);
                piles[index_source_child].pop(suppress_error, peek_mode);
                return true;
            }

            else {
                try {
                    throw new InvalidPushException(src_card, suit_piles[index_destination_child]);
                }

                catch (InvalidPushException e){
                }
            }
        }

        else {
            try {
                System.out.println("The suit stack is not suitable to your card: " + src_card.toString());
                throw new InvalidPushException(src_card, suit_piles[index_destination_child]);
            }

            catch (InvalidPushException e){
            }
        }
         return false;
    }

    public static boolean doSubMenu4Choice(int index_source_child, int index_destination_child) {
        boolean suppress_error = true, peek_mode = false;
        Card src_card = SolitaireGUI.getSelectedCard().getCardLogic();
        Card dest_card = piles[index_destination_child].peek();
        //System.out.println("src card is " + src_card + " while dest_card is " + dest_card);
        if (((SuitPileAndPileInterface)piles[index_destination_child]).checkPush(src_card, dest_card)) {
            ((Pile)piles[index_source_child]).pop(suppress_error, peek_mode);
            ((Pile)piles[index_destination_child]).push(src_card);
            return true;
        }
        else {
            try {
                //System.out.println("The chosen destination pile is not is suitable to your card: " + src_card.toString());
                throw new InvalidPushException(src_card, piles[index_destination_child]);
            }

            catch (InvalidPushException e){
            }
        }
        return false;
    }

    public static boolean doSubMenu5Choice (int index_source_child, int index_destination_child) {
        boolean suppress_error = true, peek_mode = false;
        Card src_card = suit_piles[index_source_child].peek();
        Card dest_card = piles[index_destination_child].peek();
        if (((SuitPileAndPileInterface)piles[index_destination_child]).checkPush(src_card, dest_card)) {
            piles[index_destination_child].push(src_card);
             suit_piles[index_source_child].pop(suppress_error, peek_mode);
             return true;
        }

        else {
            try {
                System.out.println("The indicated pile is not suitable to your card: " + src_card.toString());
                throw new InvalidPushException(src_card, piles[index_destination_child]);
            }

            catch (InvalidPushException e){
            }
        }
        return false;
    }

    /**
    * Assigns a deck number for each and every card in the draw_pile and pops the card
    * corresponding to the deck_number entered as one of the method's arguments
    * @param draw_pile  pile where the program draws its cards from
    * @return the new draw_pile having all its cards assigned to a particular deck number */

    public static DrawPile deckNumberSetter(LinkedStack draw_pile) {
        DrawPile new_draw_pile = new DrawPile(0);
        int quant_of_cards = draw_pile.getNumberOfCards();
        //System.out.println("quant_of_cards " + quant_of_cards);
        boolean suppress_error = true, peek_mode = false;
        for (int j = 1; j <= quant_of_cards; j++) {
            Card temp = draw_pile.pop(suppress_error, peek_mode);
            temp.setDeckNumber(j);
            new_draw_pile.push(temp);
        }
        //System.out.println();
        return new_draw_pile;
    }

    public static int randomWithRange(int min, int max)
    {
       int range = (max - min) + 1;
       return (int)(Math.random() * range) + min;
   }

    public static LinkedStack shuffle(LinkedStack draw_pile) {
        boolean suppress_error = false, add_cardfield = true;
        LinkedStack shuffled_draw_pile = new DrawPile(2, add_cardfield);
        int number_of_cards = draw_pile.getNumberOfCards();
        ((DrawPile)shuffled_draw_pile).setMainDrawPilePanel(main_draw_pile_panel);
        for(int i=1; i<=number_of_cards; i++) {
            int cards_left = draw_pile.getNumberOfCards();
            int deck_number = randomWithRange(1, cards_left);
            Card temp = ((DrawPile)draw_pile).popCardNumber(deck_number, suppress_error);
            if (temp.getSuit() != 'v') {
                draw_pile = deckNumberSetter(draw_pile);
                ((DrawPile)shuffled_draw_pile).push(temp);
            }

            else {
                try {
                    throw new StackUnderFlowException(draw_pile);
                }

                catch (StackUnderFlowException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println("at shuffle");
        //main_draw_pile_panel.printContent();
        return shuffled_draw_pile;
    }
}
