package uy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFileChooser;
import java.io.File;

public class MenuPanels extends JMenuBar {

   String[ ] gameControlItems = new String[ ] { "New Game", "Save Game", "Load Game"};

   public MenuPanels(SolitaireGUI game) {

      JMenu gameControlsMenu = new JMenu("Game Controls");
      JMenu userManualMenu = new JMenu("User Manual");
      ActionListener printListener = new ActionListener(  ) {
         public void actionPerformed(ActionEvent event) {
           String command = event.getActionCommand();
           System.out.println(command);
           if (command.equals("New Game")) {
             SolitaireDemo.newGame();
           }

           else if (command.equals("Save Game")) {
             SolitaireDemo.saveGame();
           }

           else if (command.equals("Load Game")) {
                SolitaireDemo.loadGame();//selectedFile
           }

           else if (command.equals("Read Manual")) {
               ManualReader manual_reader = new  ManualReader();
           }
         }
      };

    // Assemble the File menus and User Manual menus with keyboard accelerators.
      for (int i=0; i < gameControlItems.length; i++) {
         JMenuItem item = new JMenuItem(gameControlItems[i]);
         item.addActionListener(printListener);
         gameControlsMenu.add(item);
      }


      JMenuItem manual = new JMenuItem("Read Manual");
      manual.addActionListener(printListener);
      userManualMenu.add(manual);

      gameControlsMenu.setToolTipText("You can either append a .bin to the file or not. Both will work");
      // Finally, add all the menus to the menu bar.
      add(gameControlsMenu);
      add(userManualMenu);
    }
}
