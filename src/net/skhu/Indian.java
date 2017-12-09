package net.skhu;

import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.MainFrame;

public class Indian{

   private JFrame frame;


   private Game game = new Game();

   private Button button;
   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               Indian window = new Indian();

            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the application.
    */
   public Indian() {
      initialize();
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {
      MainFrame frame = new MainFrame(game);
      frame.setBounds(750, 250, 500, 500);

      JPanel panel = frame.mainView();
      frame.getContentPane().add(panel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);


   }




}