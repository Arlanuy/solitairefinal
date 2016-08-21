package uy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SolitaireGUI extends JFrame{
    public static int frame_width;
    public static int frame_height;
    public final static int CARDWIDTH = 100;
    public final static int CARDHEIGHT = 120;
    public static int menu_height;
    public  final static int Y_ADJUST = 100;
    private MenuPanels menu_panels;
   // Declare an instance of the drawing canvas,
   // which is an inner class called DrawCanvas extending javax.swing.JPanel.
   private static DrawCanvas canvas;
   private static CardField selected_main_card; //= new CardField("0-K.png");
   private DrawPileHolderPanel draw_pile_holder_panel;
   private MainDrawPilePanel main_draw_pile_panel;
   private SubDrawPilePanel sub_draw_pile_panel;
   private DrawPileAndSuitPilesField drawpile_and_suitpiles;
   private SuitPilesField suit_piles_field;
   private PilesField piles_field;
   private static FieldInterface field_interfaces[] = new FieldInterface[2];
   private static CardField[] pile_cards_dragged;
    private final static int DRAGGEDPILECARDDIST = 30;
    private static Container content_pane;

      public SolitaireGUI(MainDrawPilePanel main_draw_pile_panel, SubDrawPilePanel sub_draw_pile_panel, SuitPilesField suit_piles_field, PilesField piles_field) {
        this.main_draw_pile_panel = main_draw_pile_panel;
        this.sub_draw_pile_panel = sub_draw_pile_panel;
        this.suit_piles_field = suit_piles_field;
        this.piles_field = piles_field;
        Container cp = getContentPane();
        menu_panels = new MenuPanels(this);
        setJMenuBar(menu_panels);
        menu_height = menu_panels.getHeight();
        //System.out.println("menu height in gui is " + menu_height);
        cp.setLayout(new FlowLayout());
        setName("SolitaireGUI");
        setTitle("SolitaireGUI");
        Font myFont1 = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        Font myFont2 = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
        frame_width = getWidth();
        frame_height = getHeight();
        System.out.println("width is " + getWidth() + " height is " + getHeight());
        canvas = new DrawCanvas();    // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(frame_width, frame_height));
        cp.add(canvas);
        setResizable(false);
        pack(); //We don't have to set anymore drawcanvas' preferred size because of this line. The program automatically arranges this container's component/s based on the number of component/s this container have
        setVisible(true);
    }

    public static void changeDisplayWinContent(String display_win) {
        canvas.changeDisplayWinContent(display_win);
    }

    public static void setMainDrawPilePanel(MainDrawPilePanel main_draw_pile_panel) {
        canvas.setMainDrawPilePanel(main_draw_pile_panel);
    }

  public static int getCardWidth() {
      return frame_width;
  }

  public static int getCardHeight() {
      return frame_width;
  }

  public static void redisplay() {
      canvas.repaint();
  }

  public static void setSelectedCard(CardField selected_card) {
      selected_main_card = selected_card;
    }

    public static CardField getSelectedCard() {
        return selected_main_card;
    }

    public static void addMainToSubDrawPile() {
        canvas.addMainToSubDrawPile();
    }

    public static boolean validatePosition(int source_x, int source_y, int destination_x, int destination_y) {
        return canvas.validatePosition(source_x, source_y, destination_x, destination_y);
    }

    public static void createDragPileCards(int pile_panel_locX, int pile_panel_locY) {
        canvas.createDragPileCards(pile_panel_locX, pile_panel_locY);
    }

    public static void setDragPileCards(CardField[] cards_dragged) {
        canvas.setDragPileCards(cards_dragged);
    }

    public static void dragPileCards(int loc_X, int loc_Y) {
        canvas.dragPileCards(loc_X, loc_Y);
    }

  /**
    * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
    */
   private class DrawCanvas extends JPanel {
       private int selected_cardX;
       private int selected_cardY;
       private final static int MIDGAP = 50;
       private BoxLayout layout;
      // Override paintComponent to perform your own painting
      public DrawCanvas() {
         layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
         setLayout(layout);
         setBackground(Color.GREEN);
         draw_pile_holder_panel = new DrawPileHolderPanel();
          draw_pile_holder_panel.setMainDrawPilePanel(main_draw_pile_panel);
          draw_pile_holder_panel.setSubDrawPilePanel(sub_draw_pile_panel);
         drawpile_and_suitpiles = new DrawPileAndSuitPilesField();
         piles_field.setPilesCards();
         suit_piles_field.setSuitPilesCards();
         drawpile_and_suitpiles.setDrawPileHolderPanel(draw_pile_holder_panel);
         drawpile_and_suitpiles.setSuitPilesField(suit_piles_field);
        main_draw_pile_panel.setSubDrawPilePanel(sub_draw_pile_panel);
        PilesPanelHolder piles_panel_holder = new PilesPanelHolder();
        piles_panel_holder.setPilesField(piles_field);
        add(drawpile_and_suitpiles);//, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(frame_width, MIDGAP)));
        add(piles_panel_holder);//, BorderLayout.SOUTH);
        field_interfaces[0] = suit_piles_field;
        field_interfaces[1] = piles_field;
        //System.out.println("Dimension of drawcanvas is " + getWidth() + " " + getHeight());
    }

    public void changeDisplayWinContent(String display_win) {
        piles_field.setDisplayWin(display_win);
        repaint();
    }

    public void setMainDrawPilePanel(MainDrawPilePanel draw_pile_panel) {
        main_draw_pile_panel = draw_pile_panel;
    }

    public void addMainToSubDrawPile() {
        //The push is being called here first instead of the pop in order for the method following pushToSubDrawPileLogic
        //namely, checkRefillSubOrMainDrawPile to check first if the main_draw_pile or sub_draw_pile is empty
        //so that if such is the case then it will refill them accordingly with the cards in the TempDrawPile
        sub_draw_pile_panel.pushToSubDrawPileLogic();
    }

    public boolean validatePosition(int source_x, int source_y, int destination_x, int destination_y) {
        boolean setted_card = false;
         int index_destination_child = -1, index_destination_field = -1;
         if (sub_draw_pile_panel.getRectangleCovered().contains(source_x, source_y) == true) {
             for (int i = 0; i < field_interfaces.length; i++) {
                    //System.out.println("GOGO LABAN LANG");
                     Rectangle rectangle_covered = field_interfaces[i].getRectangleCovered();
                     if (rectangle_covered.contains(destination_x, destination_y) == true) {
                         //System.out.println("Yes success siya");
                         index_destination_field = i;
                     }

                     else {
                         //System.out.println("try mo lang ng itry");
                     }
                     if ((index_destination_child == -1) && (index_destination_field != - 1)) {
                        //System.out.println("contained ba ng DEST " + rectangle_covered.contains(destination_x, destination_y));
                          index_destination_child = field_interfaces[i].containsCardInChild(destination_x, destination_y);
                     }
                }
             //System.out.println("index src field sub draw pile "  + " index dest field " + index_destination_field);
             if (index_destination_child == -1) {
                 setted_card = false;
                 //System.out.println("Nagfalse siya SA 1");
             }

             else {
                 if (field_interfaces[index_destination_field].getName().equals("SuitPilesField")) {
                     setted_card = SolitaireDemo.doSubMenu1Choice(index_destination_child);
                 }

                 else {
                     setted_card = SolitaireDemo.doSubMenu2Choice(index_destination_child);
                 }
                 //System.out.println("Nagtrue siya sa source na subdrawpile" + " at dest na " + index_destination_child);
             }
         }

         else {
             int index_source_child = -1, index_source_field = -1;
             for (int i = 0; (i < field_interfaces.length) ; i++) {

                Rectangle rectangle_covered = field_interfaces[i].getRectangleCovered();
                if (rectangle_covered.contains(destination_x, destination_y) == true) {
                    index_destination_field = i;
                    //System.out.println("nag-index dest field siya sa " + i);
                }

                if (rectangle_covered.contains(source_x, source_y) == true) {
                    index_source_field = i;
                    //System.out.println("nag-index source field siya sa " + i);
                }
                if ((index_destination_child == -1) && (index_destination_field != - 1)) {
                    index_destination_child = field_interfaces[i].containsCardInChild(destination_x, destination_y);
                }

                if ((index_source_child == -1) && (index_source_field != - 1)) {
                    //System.out.println("contains ba ng i SOURCE " + rectangle_covered.contains(source_x, source_y));
                    index_source_child = field_interfaces[i].containsCardInChild(source_x, source_y);
               }
                //System.out.println("index_dest " + index_destination_child + "index source " + index_source_child);
            }
            //System.out.println("index src field " + index_source_field + " index dest field " + index_destination_field);
            if ((index_destination_child == -1) || (index_source_child == -1)) {
                setted_card = false;
                //System.out.println("Nagfalse siya SA 2" + index_destination_child + " " + index_source_child);
            }

            else {
                boolean add_top_card_first = false;
                if (index_source_field == 1) {
                    if ((pile_cards_dragged != null) && (pile_cards_dragged.length != 1)) {
                        if (index_destination_field == 1) {
                            if (index_source_child == index_destination_child) {
                                setted_card = false;
                                transferBackPilesCardPosition(source_x, source_y);
                            }

                            else {
                                setted_card = true;
                                add_top_card_first = true;
                            }
                        }
                        else{
                            setted_card = false;
                            transferBackPilesCardPosition(source_x, source_y);
                        }
                    }

                    else {
                        setted_card = true;
                        //System.out.println("iisang card ung mamomove galing sa piles");
                    }

                }

                else {
                    setted_card = true;
                }


                if (setted_card == true) {
                    if (field_interfaces[index_destination_field].getName().equals("SuitPilesField") && field_interfaces[index_source_field].getName().equals("PilesField")) {
                        setted_card = SolitaireDemo.doSubMenu3Choice(index_source_child, index_destination_child);
                    }

                    else if (field_interfaces[index_destination_field].getName().equals("PilesField") && field_interfaces[index_source_field].getName().equals("PilesField")) {
                        setted_card = SolitaireDemo.doSubMenu4Choice(index_source_child, index_destination_child);
                    }

                    else if (field_interfaces[index_destination_field].getName().equals("PilesField") && field_interfaces[index_source_field].getName().equals("SuitPilesField")) {
                        setted_card = SolitaireDemo.doSubMenu5Choice(index_source_child, index_destination_child);
                    }
                }
                if (add_top_card_first == true) {
                    transferPilesCardPosition(index_source_child, index_destination_child);
                }

                //System.out.println("Nagtrue siya sa source na " + index_source_child + " at dest na " + index_destination_child);
            }
        }
        //System.out.println("Ito ung boolean setted card " + setted_card);
        pile_cards_dragged = null;
        selected_main_card = null;
        return setted_card;
    }


     public void transferBackPilesCardPosition(int source_x, int source_y) {
         for (int i = 1; i < pile_cards_dragged.length; i++) {
            pile_cards_dragged[i].setLocation(source_x, source_y + (DRAGGEDPILECARDDIST * i));
            //System.out.println("Pabalik na pagpasa ng pile card na ");
            //pile_cards_dragged[i].getCardName();
         }
     }

     public void transferPilesCardPosition(int index_source_child, int index_destination_child) {
         for (int i = 1; i < pile_cards_dragged.length; i++) {
            piles_field.removeCardLocationInChild(index_source_child);
            piles_field.setCardLocationInChild(index_destination_child, pile_cards_dragged[i]);
            //System.out.println("Nagtrue siya sa pagpasa ng pile card na ");
            //pile_cards_dragged[i].getCardName();
         }
     }

     public void createDragPileCards(int pile_panel_locX, int pile_panel_locY) {
        // System.out.println("pile_panel_locX " + pile_panel_locX + " pile_panel_locY " + pile_panel_locY);
         int pile_panel_index = piles_field.containsCardInChild(pile_panel_locX, pile_panel_locY);
         if ((selected_main_card != null) && (pile_panel_index != -1)) {
             piles_field.createDragPileCards(selected_main_card, pile_panel_index);
            // System.out.println("nagdaan sa createdragpilecards method sa GUI");
         }

         else {
            //System.out.println("hindi nagdaan sa createdragpilecards method smc" + selected_main_card.getCardLogic() +"pile panel ind " + pile_panel_index);
         }

     }

     public void setDragPileCards(CardField[] cards_dragged) {
         pile_cards_dragged = cards_dragged;
    }

     public void dragPileCards(int loc_X, int loc_Y) {
         if ((selected_main_card != null) && (pile_cards_dragged != null)) {
              for (int i = 1; i < pile_cards_dragged.length; i++) {
                  pile_cards_dragged[i].setLocation(loc_X, loc_Y + (i * DRAGGEDPILECARDDIST));
                  if (pile_cards_dragged[i] == null) {
                      break;
                  }
              }
              //System.out.println("nagdaan sa canvasdragpile method");
          }

          else {
             //System.out.println("nagnull dragpilecards method at ito ay either selected_main_card o pile_cards_dragged ");
             //selected_main_card.getCardName();
          }
    }

      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);     // paint parent's background
         setBackground(Color.GREEN);  // set background color for this JPanel
         draw_pile_holder_panel.draw(g);
         suit_piles_field.draw(g);
         piles_field.draw(g);
        if (selected_main_card != null) {
           selected_main_card.drawCard(g);
        }

        if (pile_cards_dragged != null) {
            for (int i = 1; i < pile_cards_dragged.length; i++) {
               pile_cards_dragged[i].drawCard(g);
           }
       }
      }
   }
 }
