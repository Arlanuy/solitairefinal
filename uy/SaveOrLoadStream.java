package uy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.EOFException;


public class SaveOrLoadStream {
    private FileOutputStream fos;
    private FileInputStream fis;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String previous_checker_string = "";
    private String checker_string;
    private int skip_to_index;

    public SaveOrLoadStream() {
        try {
            fos = new FileOutputStream("save.bin");
            fis = new FileInputStream("save.bin");
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        dos = new DataOutputStream(fos);
        dis = new DataInputStream(fis);
    }

    public SaveOrLoadStream(FileOutputStream fos) {
        this.fos = fos;
        //dos = new DataOutputStream(fos); this will work as well but the uncommented below performs much faster
        dos = new DataOutputStream((OutputStream)new BufferedOutputStream(fos));
    }

    public SaveOrLoadStream(FileInputStream fis) {
        this.fis = fis;
        //dis = new DataInputStream(fis); this will work as well but the uncommented below performs much faster
        dis = new DataInputStream((InputStream)new BufferedInputStream(fis));
    }

    public Card readCardQualities() throws IOException {
        Card card = null;
        try {
            boolean face_up = Boolean.parseBoolean(checker_string);
               if((!checker_string.equals("true")) && (!checker_string.equals("false"))) {
                    while (!checker_string.equals("Finale")) {
                        //System.out.println("Old checker string is "+ checker_string);
                        previous_checker_string = checker_string;
                        checker_string = dis.readUTF();
                        //System.out.println("New checker string is "+ checker_string);
                        if ((checker_string.equals("true")) || (checker_string.equals("false"))) {
                            face_up = Boolean.parseBoolean(checker_string);
                            break;
                        }
                    }
                }

                if (!checker_string.equals("Finale")) {
                    System.out.println("checker string is: " + checker_string);
                    char color = dis.readUTF().charAt(0);
                    char suit = dis.readUTF().charAt(0);
                    int rank = Integer.parseInt(dis.readUTF());
                    String card_field_image_name = dis.readUTF();
                    //System.out.println("read fu: " + face_up +" color: " + color + " suit " + suit + " rank " + rank + " img_filename " + card_field_image_name);
                    CardField card_field = new CardField(card_field_image_name);
                    checker_string = dis.readUTF();
                    card = new Card(face_up, color, suit, rank, card_field);
                    card_field.setCardLogic(card);
                }


        } catch (EOFException e) {
            System.out.println("EOF Exception");
        }

        return card;
    }

    public void writeCardQualities(Card card) throws IOException {
        dos.writeUTF(Boolean.toString(card.getFaceUp()));
        dos.writeUTF(Character.toString(card.getColor()));
        dos.writeUTF(Character.toString(card.getSuit()));
        dos.writeUTF(Integer.toString(card.getRank()));
        String img_filename = card.getCardField().toString();
        //System.out.println("write fu: " + card.getFaceUp() +" color: " + card.getColor() + " suit " + card.getSuit() +" rank " +card.getRank() +"img filename " + img_filename);;
        dos.writeUTF(img_filename); //we can use this, instead of using card.getCardField().toString method because they both do the same thing
    }

    public void printArray(Card[] temp_cards) {
        System.out.println("The cards in the array are ");
        for (Card x: temp_cards) {
            System.out.println(x);
        }
        System.out.println("End");
    }

    public void save(DrawPile main_draw_pile, DrawPile[] draw_piles, LinkedStack[] grouped_piles, boolean check_win) throws IOException  {
        boolean suppress_error = false, peek_mode = true;
        for (int i = 0; i < draw_piles.length; i++) {
            Card[] temp_cards = draw_piles[i].copyCardsIntoArray(suppress_error, peek_mode);
            //printArray(temp_cards);
            //System.out.println("DrawPiles Saved on " + (i + 1) + " are: ");
            for (int j = temp_cards.length - 1; j >= 0; j--) {
                writeCardQualities(temp_cards[j]);
                System.out.println(temp_cards[j] + " | ");
            }
            draw_piles[i].pushDrawnCards(temp_cards);
            dos.writeUTF("Draw Pile Break " + i);
        }

        for (int i = 0; i < grouped_piles.length; i++) {
            //System.out.println("GroupedPiles Saved on " + (i + 1) + " are: ");
            Card[] temp_cards = grouped_piles[i].copyCardsIntoArray(suppress_error, peek_mode);
            for (int j = temp_cards.length - 1; j >= 0; j--) {
                writeCardQualities(temp_cards[j]);
            }
            grouped_piles[i].pushDrawnCards(temp_cards);
            dos.writeUTF("Grouped Piles Break " + i);
        }

        dos.writeUTF("Finale");
        dos.flush();
        dos.close();
        fos.close();
    }

    public boolean compareIsEqualToLastStacks(int index_stack, String checker_string) {
        if (index_stack == 13) {
            System.out.println("passed through here");
            return false;
        }

        else {
            String breaks[] = new String[14];
            int i;
            for (i = 0; i < 2; i++) {
                breaks[i] = String.format("Draw Pile Break " + i);
            }

            for (i = 2; i < 13; i++) {
                breaks[i] = String.format("Grouped Piles Break " + (i - 2));
            }

            breaks[i] = "Finale";

            for (int j = index_stack + 1; j <= 13; j++) {
                if (breaks[j].equals(checker_string)) {
                    skip_to_index = j;
                    //System.out.println("returned true at " + j);
                    return true;
                }
            }

            return false;

        }
    }

    public SolitaireGameCards load(SolitaireGameCards prepared_game) throws IOException {
        LinkedStack main_draw_pile = prepared_game.getMainDrawPile();
        DrawPile sub_draw_pile = prepared_game.getSubDrawPile();
        LinkedStack[] piles = prepared_game.getPiles();
        LinkedStack[] suit_piles = prepared_game.getSuitPiles();
        String restriction_string;
        Card card = null;
        checker_string = dis.readUTF();
        if ((!checker_string.equals("Draw Pile Break 0")) && (compareIsEqualToLastStacks(0, previous_checker_string) == false)) {
            restriction_string = "Draw Pile Break 0";
            card = readCardQualities();
            while (!previous_checker_string.equals(restriction_string)  && (compareIsEqualToLastStacks(0, previous_checker_string) == false)) {
                //System.out.println("pushed card on main_draw_pile is " + card);
                //System.out.println("Previous checker string is " + previous_checker_string);
                main_draw_pile.push(card);
                //System.out.println("test " + (compareIsEqualToLastStacks(0, previous_checker_string) == false));
                card = readCardQualities();
            }
        }
        System.out.println("Gogo lang");

        if ((!previous_checker_string.equals("Draw Pile Break 1")) && (compareIsEqualToLastStacks(1, previous_checker_string) == false)) {
            restriction_string = "Draw Pile Break 1";
            ((SubDrawPile)sub_draw_pile).resetFillEmpty();
            while (!previous_checker_string.equals(restriction_string)  && (compareIsEqualToLastStacks(0, previous_checker_string) == false)) {
                //System.out.println("pushed card on sub_draw_pile is " + card);
                //System.out.println("Previous checker string is " + previous_checker_string);
                ((SubDrawPile)sub_draw_pile).pushAlsoToDrawnCards(card);
                //sub_draw_pile.printContent();
                //((SubDrawPile)sub_draw_pile).printDrawnCards();
                card = readCardQualities();
            }
        }

        for (int i = 2; i < 2 + piles.length; i++) {
            restriction_string = String.format("Grouped Piles Break " + (i - 2));
            if ((!previous_checker_string.equals(restriction_string)) && (compareIsEqualToLastStacks(i, previous_checker_string) == false)) {
                while (!previous_checker_string.equals(restriction_string)  && (compareIsEqualToLastStacks(i, previous_checker_string) == false)) {
                    if (card != null) {
                        //System.out.println("Pushed in pile " + (i - 1) + " is " + card);
                         piles[i - 2].push(card);
                    }
                   //System.out.println("Previous checker string is " + previous_checker_string + "restriction_string: "+ restriction_string);
                   card = readCardQualities();
                }
            }

            else {
                if (skip_to_index > 8) {
                    break;
                }

                else if (i != skip_to_index){
                    //we decremented it by one because it will be incremented by one before entering this loop again because of the update condition
                    i =  skip_to_index - 1;
                }
            }
            //System.out.println("Previous checker string is " + previous_checker_string + "restriction_string: "+ restriction_string);
        }

        for (int i = 9; i < 9 + suit_piles.length; i++) {
            restriction_string = String.format("Grouped Piles Break " + (i - 2));
            //System.out.println("Previous checker string is " + previous_checker_string + " and restriction string is " + restriction_string);
            if (!previous_checker_string.equals(restriction_string) || compareIsEqualToLastStacks(i, previous_checker_string) == false) {
                while (!previous_checker_string.equals(restriction_string)  && (compareIsEqualToLastStacks(i, previous_checker_string) == false)) {
                    if (card != null) {
                        suit_piles[i - 9].push(card);
                        //System.out.println("Pushed in saveorloadstream suitpile " + card + " on " + (i - 9));
                    }
                    card = readCardQualities();
                }
            }

            else {
                if (skip_to_index == 13) {
                    //System.out.println("previous_checker_string: " + previous_checker_string);
                    break;
                }

                else {
                    //we decremented it by one because it will be incremented by one before entering this loop again because of the update condition
                    i =  skip_to_index - 1;
                }
            }
            //System.out.println("i: " + i + " sti : " + skip_to_index);
       }
        //System.out.println("fill empty: " + ((SubDrawPile)sub_draw_pile).getFillEmpty());
        SolitaireGameCards game_cards = new SolitaireGameCards(main_draw_pile, sub_draw_pile, piles, suit_piles);
        //System.out.println("SubDrawPile printdrawncards: ");
        //((SubDrawPile)sub_draw_pile).printDrawnCards();
         dis.close();
         fis.close();
         return game_cards;
    }
}
