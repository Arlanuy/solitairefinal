package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SuitPilePanel extends JPanel {

    private final static int CARDHEIGHT = 120;
    private final static int CARDWIDTH = 100;
    private static final int HEIGHT = 160;
    //the gap between the leftmost side of the screen and the leftmost side of the parent of this container namely, SuitPilesField
    public final static int CONTAINERGAP = 600;
    public final static int STARTGAP = 50;
    private static final Dimension SIZE = new Dimension(CARDWIDTH, CARDHEIGHT);
    // the adjustments that were made in the form of marginerrors in a card's released position to still let the card be dragged irrespective of the wrong location  where the mouse is pressed at
    private static final int MARGINERROR = 40;
    private static final int TWICEMARGINERROR = 80;
    private int loc_X;
    private int loc_Y;
    //private  CardField[] pile_field = new CardField[num_cards];
    private CardField[] suit_pile_cards = new CardField[13];
    private SuitPile suit_pile;
    private int num_cards = 0;
    private Rectangle rectangle_covered;
    private JLayeredPane cards_layout = new JLayeredPane();
    private LayeredPaneLayout layered_layout;
    private CardField background_pic;

    public SuitPilePanel() {
        setOpaque(false);
        layered_layout = new LayeredPaneLayout(this, 1);
        cards_layout.setLayout(layered_layout);
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
        Component[] gogo = cards_layout.getComponents();
    }

    public Rectangle getRectangleCovered() {
        return rectangle_covered;
    }

    public void setSuitPileCard(int loc_X, int loc_Y) {
        this.loc_X = loc_X;
        this.loc_Y = loc_Y;
        rectangle_covered = new Rectangle(loc_X - MARGINERROR, loc_Y - MARGINERROR,  CARDWIDTH + TWICEMARGINERROR, CARDHEIGHT + TWICEMARGINERROR);
        background_pic.setLocation(loc_X, loc_Y);
    }

    public void addCard(CardField suit_pile_card) {
        suit_pile_cards[num_cards] = suit_pile_card;
        //adds the card to the topmost part of the pile
        cards_layout.add(suit_pile_card, JLayeredPane.DRAG_LAYER, 0);
        suit_pile_card.setLocation(loc_X, loc_Y);
        validate();
        repaint();
        num_cards++;
    }

    public void removeCard(CardField removed_card) {
        cards_layout.remove(removed_card);
        revalidate();
        repaint();
        num_cards--;
    }

    public void setSuitPile(SuitPile suit_pile) {
        this.suit_pile = suit_pile;
    }

    public int getXOnScreen() {
        return loc_X;
    }

    public int getYOnScreen() {
        return loc_Y;
    }

    public void draw(Graphics g) {
        background_pic.drawCard(g);
        for (int i = 0; (i < num_cards); i++) {
            suit_pile_cards[i].drawCard(g);
        }
    }
}
