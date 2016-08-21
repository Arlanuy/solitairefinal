package uy;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Implements the characteristics of the sub_draw_pile's panel which represent the object associated with the GUI
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class SubDrawPilePanel extends JPanel{

    private final static int CARDHEIGHT = 120;
    public final static int CARDWIDTH = 100;
    private static final Dimension SIZE = new Dimension(CARDWIDTH, CARDHEIGHT);
    private static final int LOCX = 350;
    private static int card_locX = LOCX;
    private static final int LOCY = 75;
    private static final int GAP = 50;
    private static final int MARGINERROR = 40;
    private static final int TWICEMARGINERROR = 80;
    private SubDrawPile sub_draw_pile;
    private Rectangle rectangle_covered;
    private static int num_cards = 0;
    private CardField[] sub_draw_pile_cards = new CardField[24];
    private CardField background_pic;
    private boolean background_pic_flag = true;
    private CardField pushed_card;
    private JLayeredPane cards_layout = new JLayeredPane();
    private LayeredPaneLayout layered_layout;
    private int sub_draw_pile_num_cards;
    private Card[] drawn_cards;


    public SubDrawPilePanel() {
        setLayout(new BorderLayout());
        //setSize(SIZE);
        //setLocation(LOCX, LOCY);
        //It essential to use this method when you use a BorderLayout on the JContainer where this JComponent is located because if not this JPanel won't even appear in the program
        setBounds(LOCX, LOCY, CARDWIDTH + 120, CARDHEIGHT); //
        setBackground(Color.YELLOW);
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        setOpaque(false);
        //setOpaque(true);
        rectangle_covered = new Rectangle(LOCX - MARGINERROR, LOCY - MARGINERROR,  CARDWIDTH + TWICEMARGINERROR, CARDHEIGHT + TWICEMARGINERROR);
        //setBackground(Color.WHITE);
    }

    public Rectangle getRectangleCovered() {
        return rectangle_covered;
    }

    public CardField getTopCard() {
        try {
            return sub_draw_pile_cards[num_cards - 1];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Cards in sub draw pile panel are already gone");
            return null;
        }
    }

    public void setSubDrawPileNumCards(int sub_draw_pile_num_cards) {
        this.sub_draw_pile_num_cards = sub_draw_pile_num_cards;
    }

    public void setDrawnCards(Card[] drawn_cards) {
        this.drawn_cards = drawn_cards;
    }

    public void setSubDrawPile(SubDrawPile sub_draw_pile) {
         this.sub_draw_pile = sub_draw_pile;
         int capacity_num_cards = sub_draw_pile.getSubDrawPileNumCards();
         layered_layout = new LayeredPaneLayout(this, capacity_num_cards);
         cards_layout.setLayout(layered_layout);
         add(cards_layout);
         background_pic = new CardField("jr.png", true);
         MouseAdaptForCardInMainDrawPile refill_card = new  MouseAdaptForCardInMainDrawPile(background_pic);
         cards_layout.add(background_pic);
         background_pic.setLocation(card_locX, LOCY);
         if (sub_draw_pile.getSubDrawPileNumCards() == 1) {
             card_locX = LOCX;
         }

         background_pic.removeMouseListeners();
    }

    public void setToDrawBackgroundImageFlag(boolean background_pic_flag) {
        this.background_pic_flag = background_pic_flag;
    }

    public void addCard(CardField pushed_card) {
        this.pushed_card = pushed_card;
        sub_draw_pile_cards[num_cards] = pushed_card;
        cards_layout.add(pushed_card, JLayeredPane.DRAG_LAYER, 0);
        validate();
        repaint();
        num_cards++;
        int index_card = sub_draw_pile.findIndexFromDrawnCards(pushed_card.getCardLogic());
        if (index_card == -1) {
            try {
                throw new IllegalStateException();
            } catch (IllegalStateException e) {
                //sub_draw_pile.printDrawnCards();
                //System.out.println("The card is " + pushed_card.getCardLogic() + " index card is " + index_card);
                e.printStackTrace();
            }

        }

        else {
            pushed_card.setLocation(card_locX + ((sub_draw_pile_num_cards - index_card - 1) * 30), LOCY);
            //System.out.println("naset naman ung lokasyon ni " + pushed_card.getCardLogic() + " sa " + (card_locX + ((sub_draw_pile_num_cards - index_card - 1) * 30)));
        }

        setToDrawBackgroundImageFlag(false);
    }

    public void removeCard(CardField removed_card) {
        cards_layout.remove(removed_card);
        revalidate();
        repaint();
        num_cards--;
        //System.out.println("num_cards after removing in spp is  : " + num_cards);
        if (num_cards == 0) {
            setToDrawBackgroundImageFlag(true);
        }
    }

    public void pushToSubDrawPileLogic() {
        boolean draw_from_main_pile = true;
        sub_draw_pile.checkRefillSubOrMainDrawPile(draw_from_main_pile);
        //sub_draw_pile.printDrawnCards();
    }

    public void draw (Graphics g) {
        if (background_pic_flag == true) {
            background_pic.drawCard(g);
        }
        Card[] drawn_cards = sub_draw_pile.getDrawnCards();
        if (drawn_cards != null) {
            for (int i = drawn_cards.length - 1; i >= 0; i--) {
                if (drawn_cards[i] != null) {
                    drawn_cards[i].getCardField().drawCard(g);
                }
            }
        }

    }

    public void printContent() {
        System.out.println("The cards in spp are: ");
        for (int i = 0; i < num_cards; i++) {
            System.out.println(sub_draw_pile_cards[i].getCardLogic());
        }
    }

    public void printDrawnCards() {
        System.out.println("The cards in subdrawpiledrawncards are ");
        if (drawn_cards != null) {
            for (Card card: drawn_cards) {
                System.out.print(card + " | ");
            }
        }
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }
}
