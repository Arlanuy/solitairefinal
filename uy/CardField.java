package uy;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import javax.imageio.*;
import java.io.*;

/**
* Implements the image of a card in  the GUI
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class CardField extends JLabel{
    private BufferedImage card_image;
    private BufferedImage card_image_face1;
    private BufferedImage card_image_face2;
    private int location_x;
    private int location_y;
    private String img_filename = "";
    private MouseAdapter card_listener;
    public final static int CARDWIDTH = 100;
    public final static int CARDSTARTHEIGHT = 120;
    public static int CARDHEIGHT = CARDSTARTHEIGHT;
    private final static Dimension SIZE = new Dimension(CARDWIDTH, CARDHEIGHT);
    private Card card_logic;
    private int suit;

    public CardField(String img_filename) {
        //the same as "this.setMaximumSize(SIZE);""
        setMaximumSize(SIZE);
        setMinimumSize(SIZE);
        setBounds(location_x, location_y, CARDWIDTH, CARDHEIGHT);
        this.img_filename = img_filename;
        setOpaque(false);
        //setOpaque(true);
        setBackground(Color.RED);
        card_listener = new MouseAdaptForCard(this);
        try {
            //takes the card image from  the package graphics
            card_image_face1 = ImageIO.read(getClass().getResourceAsStream("/uy/card_images/" + "B.png"));
            card_image_face2 = ImageIO.read(getClass().getResourceAsStream("/uy/card_images/" + img_filename));
        }
        catch (IOException e) {
            //not handled
            System.out.println("IOException");
        }
    }

    public CardField(String img_filename, boolean is_background_pic) {
        //the same as "this.setMaximumSize(SIZE);""
        setMaximumSize(SIZE);
        setMinimumSize(SIZE);
        setBounds(location_x, location_y, CARDWIDTH, CARDHEIGHT);
        this.img_filename = img_filename;
        setOpaque(false);
        //setOpaque(true);
        setBackground(Color.RED);
        card_listener = new MouseAdaptForCard(this);
        try {
            //takes the card image from  the package graphics
            card_image = ImageIO.read(getClass().getResourceAsStream("/uy/card_images/" + img_filename));
        }
        catch (IOException e) {
            //not handled
            System.out.println("IOException");
        }
    }

    public void setCardListener (MouseAdapter card_listener) {
        this.card_listener = card_listener;
    }

    public void setCardLogic(Card card_logic) {
        this.card_logic = card_logic;
    }

    public Card getCardLogic() {
        return card_logic;
    }

    public void removeMouseListeners() {
        removeMouseMotionListener(card_listener);
        removeMouseListener(card_listener);
    }


    public void drawCard(Graphics g) {
        //System.out.println(card_image + " locx " + location_x + " locy " + location_y);
        if (card_logic != null && card_logic.getFaceUp() == false) {
            card_image = card_image_face1;
        }

        else if (card_logic != null && card_logic.getFaceUp() == true) {
            card_image = card_image_face2;
        }
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.WHITE);
        g.clearRect(location_x, location_y, CARDWIDTH, CARDHEIGHT);
        g2d.drawImage(card_image, location_x, location_y, CARDWIDTH, CARDHEIGHT, null);
    }


    public void setLocation(int location_x, int location_y) {
        this.location_x = location_x;
        this.location_y = location_y;
    }

     public int getXOnScreen() {
         return location_x;
     }

     public int getYOnScreen() {
         return location_y;
     }

     public Dimension getPreferredSize() {
         return new Dimension(CARDWIDTH, CARDHEIGHT);
     }

     public void getCardName() {
         System.out.println("The card is " + img_filename);
     }

     @Override
     public String toString() {
        return img_filename;
    }
}
