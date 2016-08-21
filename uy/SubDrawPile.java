package uy;

    /**
    * Implements the characteristics of a sub_draw_pile which is the pile
    * where the card immediately taken from the draw pile goes to
    * @author  Arlan Uy
    * @version 1.8.0_60
    */

    public class SubDrawPile extends DrawPile {
    //You can change the value of this variable to either 3 or 1
    private static int sub_draw_pile_num_cards = 3;
    private static int LOCX = 350;
    private static final int LOCY = 75;
    private Card[] drawn_cards;
    private LinkedStack main_draw_pile;
    //private DrawPile temp_draw_pile = new TempDrawPile(4, this);
    private SubDrawPilePanel sub_draw_pile_panel;
    private int fill_empty = 0;
    private boolean emptier;

    public SubDrawPile(int pile_number) {
        super(pile_number);
        drawn_cards = new Card[sub_draw_pile_num_cards];
    }

    public void setMainDrawPile(LinkedStack main_draw_pile) {
        this.main_draw_pile = main_draw_pile;
    }

    public void setSubDrawPilePanel(SubDrawPilePanel sub_draw_pile_panel) {
        this.sub_draw_pile_panel = sub_draw_pile_panel;
        sub_draw_pile_panel.setSubDrawPileNumCards(sub_draw_pile_num_cards);
        sub_draw_pile_panel.setSubDrawPile(this);
    }

    public void setEmptier(boolean emptier) {
        //System.out.println("Just setted emptier to " + emptier);
        this.emptier = emptier;
    }

    public boolean getEmptier() {
        return emptier;
    }

    public LinkedStack getMainDrawPile() {
        return main_draw_pile;
    }

    public Card[] getDrawnCards() {
        return drawn_cards;
    }

    public int getSubDrawPileNumCards() {
        return sub_draw_pile_num_cards;
    }

    public void printDrawnCards() {
        System.out.println("main draw pile content");
        main_draw_pile.printContent();
        System.out.println("subdrawpile content");
        printContent();
        System.out.println("The cards in subdrawpiledrawncards are ");
        for (Card card: drawn_cards) {
            System.out.print(card + " | ");
        }
    }

    public void resetFillEmpty(){
        fill_empty = 0;
    }

    public int getFillEmpty() {
        return fill_empty;
    }

    public void replaceDrawnCard(Card drawn_card) {
        this.drawn_card = drawn_card;
    }

    //overrides the push method from the drawpile
    @Override
    public void push(Card x) {
        Node newN = new Node();
        newN.INFO = x;
        x.setFaceUp(true);
        newN.LINK = top;
        number_of_cards++;
        top = newN;
        drawn_card = top.INFO;
        CardField x_field = x.getCardField();
        sub_draw_pile_panel.addCard(x_field);
    }

    public void pushAlsoToDrawnCards(Card x) {
        if (x != null) {
            Node newN = new Node();
            newN.INFO = x;
            x.setFaceUp(true);
            newN.LINK = top;
            int fill_slot = number_of_cards  %  3;
            drawn_cards[fill_slot] = x;
            number_of_cards++;
            top = newN;
            drawn_card = top.INFO;
            CardField x_field = x.getCardField();
            sub_draw_pile_panel.addCard(x_field);
        }
    }

    public void initializeDrawnCards() {
        drawn_cards = new Card[sub_draw_pile_num_cards];
    }

    //overrides the pop method from the DrawPile class
    @Override
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
              //System.out.println("number_of_cards in pop method  is " + number_of_cards);
              if (emptier == true && peek_mode == false && sub_draw_pile_num_cards == 3) {
                  fill_empty++;
                  //System.out.println("fill empty: " + fill_empty + " and the card is " + drawn_cards[fill_empty - 1]);
                  drawn_cards[fill_empty - 1] = null;
                  if (fill_empty != 3) {
                    replaceDrawnCard(drawn_cards[fill_empty]);
                  }
              }

              else if (sub_draw_pile_num_cards ==  1) {
                  drawn_card = drawn_cards[0];
              }

              else {
                  //System.out.println("Else condition in pop method of subdrawpile ");
              }

              number_of_cards--;
             // System.out.println("The card " + x + " is popped");
              //printContent();
              CardField x_field = x.getCardField();
              sub_draw_pile_panel.removeCard(x_field);
            }
        return x;
      }

      public void popWithRefill() {
         boolean suppress_error = false, peek_mode = false, draw_from_main_pile = false;
         pop(suppress_error, peek_mode);
         checkRefillSubOrMainDrawPile(draw_from_main_pile);
      }

      public Card[] getSubDrawPileDrawnCards() {
          return drawn_cards;
      }

    public int findIndexFromDrawnCards(Card card) {
        int i = 0;
        if (drawn_cards != null) {
            for (Card searcher: drawn_cards) {
                if (searcher != null) {
                    if (searcher.toString().equals(card.toString())) {
                        return i;
                    }

                    else {
                        //System.out.println(searcher + " compared to " +  card);
                    }
                }
                i++;
            }
        }

        else {
            //System.out.println("null drawn cards");
        }
        return -1;
    }

    public void checkRefillSubOrMainDrawPile(boolean draw_from_main_pile) {
       drawMainOrSubDrawPileCards(draw_from_main_pile);
       if (isEmpty() == false) {
           setDrawnCards();
       }
    }

    public boolean isEmptyDrawnCards() {
        for (int i = 0; i < sub_draw_pile_num_cards; i++) {
            if (drawn_cards[i] != null) {
                return false;
            }
        }
        return true;
    }

    public void drawMainOrSubDrawPileCards(boolean draw_from_main_pile) {
        //printDrawnCards();

        if ((main_draw_pile != null) && (main_draw_pile.isEmpty() == true)) {
            //checks if the sub draw pile is empty, this can also be written as this.isEmpty()
            if (isEmpty() == true) {
                System.out.println("There are no  more cards left in both your temp and main draw piles ");
            }

            else if (draw_from_main_pile == true) {
                int num_cards = getNumberOfCards();
                boolean suppress_error = true, peek_mode = false;
                Card[] transfer_cards = new Card[num_cards];
                Card[]transfer_cards2 = new Card[num_cards];
                for (int i = 0; i < num_cards; i++) {
                    if (i % 3 == 0) {
                        resetFillEmpty();
                    }
                    transfer_cards[num_cards - i - 1] = pop(suppress_error, peek_mode);
                }
                //printArray(transfer_cards);

                for (int i = 0; i < num_cards; i++) {
                    transfer_cards2[i] = transfer_cards[num_cards - i - 1];
                    main_draw_pile.push(transfer_cards2[i]);
                    //System.out.println("pushed card from drawmainorsubdrawpilecards method of subdrawpile " + transfer_cards[i]);
                }
            }
        }

        else if ((main_draw_pile != null) && (draw_from_main_pile == true)) {
            //This will be executed if the chosen subdrawpile cards was three
            //the next two cards will be immediately pushed to the subdrawpile
            resetFillEmpty();
            for (int i = 0; i < sub_draw_pile_num_cards; i++) {
                Card x = main_draw_pile.peek();
                if (x != null && x.getSuit() != 'v') {
                    //System.out.println("before top of main pile card is  " +  ((DrawPile)main_draw_pile).getDrawnCard());
                    drawn_cards[i] = ((DrawPile)main_draw_pile).getDrawnCard();
                    ((DrawPile)main_draw_pile).drawCard();
                    //System.out.println("top of main pile card is  " + drawn_cards[i].toString());
                }

                else {
                    //System.out.println("Exhausted cards in main draw pile " + i);
                    drawn_cards[i] = null;
                }
            }
            //System.out.println("entered here at 2");
            //printArray(drawn_cards);
            setEmptier(false);
            pushDrawnCards(drawn_cards);
            setEmptier(true);
        }

        else if ((top != null && isEmptyDrawnCards() == true) || (sub_draw_pile_num_cards == 1)) {
            Node traverser = top;
            for (int i = 0; i < sub_draw_pile_num_cards; i++) {
                if (traverser != null) {
                    //System.out.println("before top of main pile card is  " +  drawn_cards[i]);
                    Card x = traverser.INFO;
                    drawn_cards[i] = x;
                    drawn_cards[i].getCardField().setLocation(LOCX + ((sub_draw_pile_num_cards - i - 1) * 30), LOCY);
                    //System.out.println("after top of main pile card is  " + drawn_cards[i]);
                }

                else {
                    System.out.println("Exhausted cards in main draw pile");
                    break;
                }
                traverser = traverser.LINK;
            }
            //printDrawnCards();
            setEmptier(true);
            setDrawnCard();
            //System.out.println("emptied here");
            resetFillEmpty();
        }

        else {
            //System.out.println("else condition in drawmainorsubdrawpilecards method in subdrawpile");
            //printArray(drawn_cards);
        }
    }

    public void printArray(Card[] temp_cards) {
        System.out.println("The cards in the array are ");
        for (Card x: temp_cards) {
            System.out.println(x);
        }
        System.out.println("End");
    }

    public void setDrawnCards() {
        int j = 0, i = 0;

        if (isEmpty() == false) {
            for (i =  0; i < sub_draw_pile_num_cards; i++) {
                if ((drawn_cards[i] != null) && (drawn_cards[i].getSuit() != 'v')) {
                    j++;
                    if (j > 1) {
                        //removes the mouselisteners behind the top card in order for their listeners to not coincide with one another when the mouse  hovers over them
                        drawn_cards[i].getCardField().removeMouseListeners();
                        //System.out.println("Removed a mouselistener on " + drawn_cards[i]);
                    }
                }

                else {
                    //System.out.println("null on setdrawncards");
                }
                //System.out.println("drawn cards " + i + " is " + drawn_cards[i]);
            }
            //System.out.println("j is " + j + " i is " + i + " num cards " + sub_draw_pile_num_cards);
            if (j != 0 && drawn_cards[sub_draw_pile_num_cards - j] != null) {
                drawn_cards[sub_draw_pile_num_cards - j].getCardField().removeMouseListeners();
                MouseAdaptForCard card_listener = new MouseAdaptForCard(drawn_cards[sub_draw_pile_num_cards - j].getCardField());
                //System.out.println("Added a mouselistener on " + drawn_cards[sub_draw_pile_num_cards - j]);
            }
            //System.out.println("The drawn cards of the sub draw pile in the setDrawnCards method are: ");
            //printDrawnCards();
        }
    }

    //overrides the  printcontent method from the LinkedStack class
    public void printContent() {
        if (top == null){
          System.out.println("The sub draw pile is currently empty");
        }

        else {
            int number = 0;
            System.out.println("The cards in sub draw pile are: ");
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
            System.out.println("the cards in spp are: ");
            sub_draw_pile_panel.printContent();
        }

    }
}
