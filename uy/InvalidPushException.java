package uy;
import java.io.*;

/**
* Handles the occurence of an  invalid move due to a wrong push of card/s from
* one pile into another
* The kind  of pile that implements  this exception consists of DrawPile, Pile and SuitPile classes
* @author  Arlan Uy
* @version 1.8.0_60
*/
public class InvalidPushException extends Exception {
    public InvalidPushException(Card y, LinkedStack x) {
            System.out.println("Invalid move");
            String yrep = "", xrep = "";
            if (y.getSuit() == 'v') {
                yrep = "non-existent";
            }

            else {
                yrep = y.toString();
            }

            System.out.println("The card you want to move is " + y.toString() + " while the details about the pile to where you had pushed are that "
             + x.toString(x));
        }

}
