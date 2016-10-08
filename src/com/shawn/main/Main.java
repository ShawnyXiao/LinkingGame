package com.shawn.main;

import com.shawn.ui.LinkingGameFrame;

import javax.swing.*;
import java.awt.*;

/**
 * This is a linking game.
 *
 * @author Shawn（肖小粤）
 * @version 2.1
 */
public class Main
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                LinkingGameFrame frame = new LinkingGameFrame();
                frame.setTitle("Linking Game");
                frame.setIconImage(new ImageIcon(getClass().getResource("/com/shawn/image/linkingGame.png")).getImage());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}