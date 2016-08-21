package uy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PilesField extends JPanel  implements FieldInterface {
    private static final int WIDTH  = 1370;//SolitaireGUI.frame_width - PilesPanelHolder.CONTAINERGAP;
    //BOTTOMMARGIN = 230;
    private static final int HEIGHT = 560;//SolitaireGUI.frame_height -BOTTOMMARGIN - DrawPileAndSuitPilesField.HEIGHT;
    private static final int MINHEIGHT = 200;
    private final static int CARDWIDTH = 100;
    private final static int CARDHEIGHT = 120;
    private final static int CARDDIST = 30;
    private static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
    private static final int TOPGAP = 50;
    private static final Dimension INSIDESIZE = new Dimension(WIDTH, HEIGHT - TOPGAP);
    private static final int LOCX = 550;
    private static final int LOCY = 320; // DrawPileSuitPilesField.HEIGHT + DrawCanvas.MIDGAP
    private static final int STARTGAP = 550;
    private static final int XGAP = 80;
    private static final int XSTARTGAP = 50;
    private static final int YGAP = 50;
    private static Pile piles[];
    private static PilePanel piles_panel[];
    private int num_cards_dragged = 0;
    private BoxLayout layout;
    private BoxLayout layout2;
    private Rectangle rectangle_covered;
    private JPanel pile_cards_container;
    private static String display_win;

    public PilesField() {
        layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);
        setOpaque(false);
        //setOpaque(true);
        setBackground(Color.BLUE);
        //System.out.println("dpsp height " + DrawPileAndSuitPilesField.HEIGHT);
        //System.out.println("PPH at " + PilesPanelHolder.CONTAINERGAP);
        rectangle_covered = new Rectangle(LOCX, LOCY, WIDTH, HEIGHT);
        pile_cards_container = new JPanel();
        pile_cards_container.setOpaque(false);
        layout2 = new BoxLayout(pile_cards_container, BoxLayout.LINE_AXIS);
        pile_cards_container.setLayout(layout2);
        pile_cards_container.setMaximumSize(getInsideContainerPreferredSize());
        pile_cards_container.setMinimumSize(getInsideContainerPreferredSize());
        pile_cards_container.setPreferredSize(getInsideContainerPreferredSize());
        //pile_cards_container.setOpaque(true);
        //we don't need to write here below anymore the method setPreferredSize because it is already done for this program when the getPreferredSize method is overriden here.
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }

    public void setDisplayWin(String display_win) {
        this.display_win = display_win;
    }

    public void setPilesAndPilesPanel(Pile[] piles, PilePanel[] piles_panel) {
        this.piles = piles;
        this.piles_panel = piles_panel;
    }

    public Rectangle getRectangleCovered() {
        return rectangle_covered;
    }

    /**
    * By using a loop, this method assign each pile to its corresponding piles_panel
    * @param piles  the logic used by a pile represented in an array
    * @param piles_panel  the representation of a pile in the GUI
    */

    public void assignPiles(LinkedStack piles[], PilePanel piles_panel[]) {
        this.piles_panel = piles_panel;
        this.piles = (Pile[])piles;
        for (int i = 0; i < piles.length; i++) {
            piles_panel[i] = new PilePanel();
            ((Pile)piles[i]).setPilePanel(piles_panel[i]);
        }
        createPilesPanel();
    }

    public void createPilesPanel() {
        pile_cards_container.add(Box.createRigidArea(new Dimension(XSTARTGAP, HEIGHT)));
        for (int i = 0; i < piles_panel.length; i++) {
            pile_cards_container.add(piles_panel[i]);
            pile_cards_container.add(Box.createRigidArea(new Dimension(XGAP, HEIGHT)));
        }
        add(Box.createRigidArea(new Dimension(WIDTH, YGAP)));
        add(pile_cards_container);
    }

    public String getName() {
        return "PilesField";
    }

    public void setPilesCards() {
        for (int i =  0; i < piles_panel.length ; i++) {
            piles_panel[i].setPileCard((XSTARTGAP + LOCX + (XGAP + CARDWIDTH) * i), LOCY +  + TOPGAP);
        }
    }

    public int containsCardInChild(int location_x, int location_y) {
        int index_child = -1;
        //System.out.println("Location to be contained is " + location_x + " " + location_y);
        for (int i = 0; i < piles_panel.length; i++) {
            //System.out.println("Piles panel is at X: " + piles_panel[i].getXOnScreen() + " AND AT Y: " + piles_panel[i].getYOnScreen());
            //System.out.println("Rectangle on SCREEN is at " + rectangle_covered.getXOnScreen() + " " + rectangle_covered.getYOnScreen());
            Rectangle rect = piles_panel[i].getRectangleCovered();
            //System.out.println("Rect error i" + i +" at location " + rect.getX() + " " + rect.getY() + " with dimension " + rect.getWidth() + " " + rect.getHeight());
            if (rect.contains(location_x, location_y)) {
                index_child = i;
                break;
            }
        }
        return index_child;
    }

    public  void setCardLocationInChild(int index_child, CardField pushed_card) {
        piles_panel[index_child].pushIntoPileLogic(pushed_card);
    }

    public Card removeCardLocationInChild(int index_child) {
        //System.out.println("index child " + index_child +"pushed_card" + pushed_card);
        //System.out.println("this is suit panel " + piles_panel[index_child]);
        Card removed_card = piles_panel[index_child].popFromPileLogic();
        return removed_card;
    }

    public void createDragPileCards(CardField selected_card, int pile_panel_index) {
        piles_panel[pile_panel_index].setDraggedCards(selected_card);
    }

    public Dimension getPreferredSize() {
        return SIZE;
    }

    public Dimension getInsideContainerPreferredSize() {
        return INSIDESIZE;
    }

    public void draw(Graphics g) {
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        g.setColor(Color.red);
        g.drawString(display_win, 100, LOCY * 2);

        for (int i = 0; i < piles_panel.length; i++) {
            piles_panel[i].draw(g);
        }
    }
}
