package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PilePanel extends JPanel {
    private final static int CARDHEIGHT = 120;
    private final static int CARDWIDTH = 100;
    private final static int CARDDIST = 30;
    //BOTTOMMARGIN = 230
    private static final int HEIGHT = 560;//SolitaireGUI.frame_height - BOTTOMMARGIN - DrawPileAndSuitPilesField.HEIGHT;
    //private static final int XCONTAINERGAP = 600;
    //private static final int YCONTAINERGAP = 370;
    private static final Dimension SIZE = new Dimension(CARDWIDTH, HEIGHT);
    // the adjustment that were made in the form of marginerrors in a card's released position to still let the card be dragged irrespective of the wrong location  where the mouse is pressed at
    private static final int MARGINERROR = 40;
    private static final int TWICEMARGINERROR = 80;
    private int loc_X;
    private int loc_Y;
    private int top_removed_card_index = -1;
    //we assume not exactly 13 pile cards because of the face down cards
    private  CardField[] pile_cards = new CardField[20];
    private CardField[] cards_dragged;
    private Pile pile;
    private Rectangle rectangle_covered;
    private int num_cards = 0;
    private JLayeredPane cards_layout;
    private LayeredPaneLayout layered_layout;
    private CardField background_pic;

    public PilePanel() {
        cards_layout = new JLayeredPane();
        layered_layout = new LayeredPaneLayout(this, 2);
        cards_layout.setLayout(layered_layout);
        setOpaque(false);
        //setOpaque(true);
        //cards_layout.setOpaque(true);
        setLayout(new BorderLayout());
        setSize(SIZE);
        add(cards_layout);
        cards_layout.setBackground(Color.BLACK);
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        background_pic = new CardField("jr.png", true);
        background_pic.removeMouseListeners();
        cards_layout.add(background_pic);
    }

    public Rectangle getRectangleCovered() {
        return rectangle_covered;
    }

    public int getNumCards() {
        return num_cards;
    }

    public void printContent() {
        System.out.println("The cards in pp are ");
        for (int i = 0; i < num_cards; i++) {
            System.out.println(pile_cards[i].getCardLogic());
        }
    }

    public void setPileCard(int loc_X, int loc_Y) {
        this.loc_X = loc_X;
        this.loc_Y = loc_Y;
        rectangle_covered = new Rectangle(loc_X - MARGINERROR, loc_Y - MARGINERROR,  CARDWIDTH + TWICEMARGINERROR, HEIGHT + TWICEMARGINERROR);
        background_pic.setLocation(loc_X, loc_Y);
    }

    public int searchCard(CardField card_seek) {
        //System.out.println("num cards at search card method is " + num_cards);
        for (int i = 0; i < num_cards; i++) {
            if (pile_cards[i] == card_seek) {
                return i;
            }
            //System.out.println("While searching " + getXOnScreen() + " " + getYOnScreen());
            //pile_cards[i].getCardName();
        }
        return -1;
    }

    public void setDraggedCards(CardField top_removed_card) {
        top_removed_card_index = searchCard(top_removed_card);
        if ((top_removed_card_index != -1) && (num_cards != 1)) {
            cards_dragged = new CardField[num_cards - top_removed_card_index];
            for (int i = top_removed_card_index; i < num_cards; i++) {
                cards_dragged[i - top_removed_card_index] = pile_cards[i];
            }
            SolitaireGUI.setDragPileCards(cards_dragged);
            //System.out.println("nadrag silang lahat");
        }


        else {
            //System.out.println("hindi nadrag silang lahat");
        }
    }

    public void addCard(CardField pile_card) {
        pile_cards[num_cards] = pile_card;
        //adds the card to the topmost part of the pile
        pile_card.setLocation(loc_X, loc_Y + (num_cards * CARDDIST));
        //System.out.println("added " + pile_card + "at pile panel # " + pile.getPileNumber());
        cards_layout.add(pile_card, JLayeredPane.DRAG_LAYER, 0);;
        validate();
        repaint();
        num_cards++;
    }

    public void removeCard(CardField removed_card) {
        cards_layout.remove(removed_card);
        //System.out.println("removed " + removed_card.getCardLogic());
        revalidate();
        repaint();
        SolitaireGUI.redisplay();
        num_cards--;
        //System.out.println("num_cards after removing is  : " + num_cards);
    }

    public void pushIntoPileLogic(CardField pushed_card) {
        pile.push(pushed_card.getCardLogic());
    }


    public Card popFromPileLogic() {
        boolean suppress_error = false, peek_mode = false;
        Card x = pile.pop(suppress_error, peek_mode);
        return x;
    }


    public void setPile(Pile pile) {
        this.pile = pile;
    }

    public int getXOnScreen() {
        return loc_X;
    }

    public int getYOnScreen() {
        return loc_Y;
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void draw(Graphics g) {
        background_pic.drawCard(g);
        for (int i = 0; (i < num_cards); i++) {
            pile_cards[i].drawCard(g);
        }
    }
}
