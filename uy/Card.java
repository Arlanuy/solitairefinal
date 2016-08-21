//Arlan UY 2015-09385
package uy;
import java.io.*;
import java.util.*;

/**
* Implements the characteristics of a card
* @author  Arlan Uy
* @version 1.8.0_60
*/

public class Card{
    private boolean face_up;
    private char color;
    private char suit;
    private int rank;
    private int deck_number;
    private CardField card_field;

    public Card() {
        this.color = 'v';
        this.suit = 'v';
        this.rank = 0;
        this.deck_number = 0;
    }

    public Card(boolean face_up, char color, char suit, int rank, CardField card_field) {
        this.face_up = face_up;
        this.color = color;
        this.suit = suit;
        this.rank = rank;
        this.card_field = card_field;
    }

    public void setDeckNumber (int deck_number) {
        this.deck_number = deck_number;
    }

    public int getDeckNumber() {
        return deck_number;
    }

    public CardField getCardField() {
        return card_field;
    }

    public boolean getFaceUp() {
        return face_up;
    }
    public void setFaceUp(boolean face_up) {
        this.face_up = face_up;
    }

    public char getColor(){
        return color;
    }

    public void setColor(char color){
        this.color = color;
    }

    public char getSuit(){
        return suit;
    }

    public void setSuit(char suit){
        this.suit = suit;
    }

    public int getRank(){
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String toString() {
        int rank = getRank();
        String rank_string = "";
        if (rank == 13)
            rank_string = "K";
        else if (rank == 12)
            rank_string = "Q";
        else if (rank == 11)
            rank_string = "J";
        else if (rank == 1)
            rank_string = "A";
        else
            rank_string = String.format("" + rank);
        return rank_string + " " +getSuit();
    }
}
