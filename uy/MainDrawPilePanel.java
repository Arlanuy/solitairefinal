package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Implements the characteristics of the  main draw pile
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class MainDrawPilePanel extends JPanel {

    private final static int CARDHEIGHT = 120;
    private final static int CARDWIDTH = 100;
    private static final Dimension SIZE = new Dimension(CARDWIDTH, CARDHEIGHT);
    private static final int LOCX = 150;
    //Because SuitPilesField that uses BoxLayout does not follow any setBounds method and thus having a y-gap-start of 75 pixels,
    //we must just adjust this accordingly. But I can't directly order this layout to place the cards at a certain y-coordinate and so we just follow the default y-gap-start of SuitPilesField
    private static final int LOCY = 75;
    private DrawPile main_draw_pile;
    private SubDrawPilePanel sub_draw_pile_panel;
    private static CardField[] main_draw_pile_cards = new CardField[52];
    private Rectangle rectangle_covered;
    private static int num_cards = 0;
    private JLayeredPane cards_layout;
    private LayeredPaneLayout layered_layout;
    private CardField background_pic;

    public MainDrawPilePanel() {
        cards_layout = new JLayeredPane();
        layered_layout = new LayeredPaneLayout(this, 1);
        setLayout(new BorderLayout());
        cards_layout.setLayout(layered_layout);
        setOpaque(false);
        add(cards_layout);
        //cards_layout.setOpaque(true);
        cards_layout.setBackground(Color.WHITE);
        //It essential to use this method when you use a BorderLayout on the JContainer where this JComponent is located because if not this JPanel won't even appear in the program
        setBounds(LOCX, LOCY, CARDWIDTH, CARDHEIGHT);
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        background_pic = new CardField("jb.png", true);
        background_pic.removeMouseListeners();
        MouseAdaptForCardInMainDrawPile refill_listener = new MouseAdaptForCardInMainDrawPile(background_pic);
        cards_layout.add(background_pic);
        background_pic.setLocation(LOCX, LOCY);
    }

    public void setSubDrawPilePanel(SubDrawPilePanel sub_draw_pile_panel) {
        this.sub_draw_pile_panel = sub_draw_pile_panel;
    }


    public void setMainDrawPile(DrawPile main_draw_pile) {
        this.main_draw_pile = main_draw_pile;
    }

    public void addCard(CardField pushed_card) {
        main_draw_pile_cards[num_cards] = pushed_card;
        cards_layout.add(pushed_card, JLayeredPane.DRAG_LAYER, 0);
        pushed_card.removeMouseListeners();
        MouseAdaptForCardInMainDrawPile main_draw_pile_card_listener = new MouseAdaptForCardInMainDrawPile(pushed_card);
        pushed_card.setLocation(LOCX, LOCY);
        validate();
        repaint();
        num_cards++;
    }

    public void removeCard(CardField removed_card) {
        cards_layout.remove(removed_card);
        removed_card.removeMouseListeners();
        //System.out.println("added a card  mouselistener on " + removed_card.getCardLogic());
        MouseAdaptForCard card_listener = new MouseAdaptForCard(removed_card);
        revalidate();
        repaint();
        num_cards--;
    }

    public CardField getTopCard() {
        try {
            return main_draw_pile_cards[num_cards - 1];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Cards in main draw pile panel are already gone");
            return null;
        }

    }

    public void draw(Graphics g) {
        background_pic.drawCard(g);
        for (int i = 0; i < num_cards; i++) {
            main_draw_pile_cards[i].drawCard(g);
        }
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void printContent() {
        System.out.println("content of main draw pile panel are: ");
        for (int i = 0; i < num_cards; i++) {
            System.out.print(main_draw_pile_cards[i].getCardLogic() + " | ");
        }
        System.out.println();
    }

}
