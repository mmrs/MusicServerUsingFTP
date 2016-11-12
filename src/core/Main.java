/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import gui.MainFrame;
import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Moshiur
 */
public class Main {
    
    FTPFile musicFiles[];
    
    public static void main(String[] args) throws IOException {
   
           try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                /*if ("Metal".equals(info.getName())) {
                 javax.swing.UIManager.setLookAndFeel(info.getClassName());
                 break;
                 }*/
                Properties props = new Properties();
                props.put("logoString", "my company");
                HiFiLookAndFeel.setCurrentTheme(props);
                //  UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
          //        UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                // UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                  UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                //  UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
             //     UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
                //   UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
                //   UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                //  UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
               // UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
             //      UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
                 //  UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");

            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {

        }
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
        frame.setResizable(false);
    }
   
}
