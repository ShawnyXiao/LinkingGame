package com.shawn.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class LinkingGameButton extends JToggleButton
{
    public LinkingGameButton(String string)
    {
        super(string);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        // set the rendering hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        // set the border to null
        this.setBorder(null);

        // fill the background
        this.setRolloverEnabled(true);

        ButtonModel model = this.getModel();
        Color color;

        if (!model.isSelected())
        {
            if (model.isArmed())
            {
                color = new Color(255, 255, 255);
            }
            else if (model.isRollover())
            {
                color = new Color(0, 240, 255);
            }
            else
            {
                color = new Color(0, 210, 255);
            }
        }
        else
        {
            if (model.isArmed())
            {
                color = new Color(0, 210, 255);
            }
            else if (model.isRollover())
            {
                color = new Color(0, 240, 255);
            }
            else
            {
                color = new Color(255, 255, 255);
            }
        }

        g2.setPaint(color);
        g2.fill(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));

        // draw the string
        Font font = new Font("微软雅黑", Font.BOLD, 40);
        g2.setFont(font);

        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(this.getText(), context);

        double x = (JButton.RIGHT == this.getHorizontalAlignment()) ?
                (this.getWidth() - bounds.getWidth() - 10) :
                ((this.getWidth() - bounds.getWidth()) / 2);
        double y = (this.getHeight() - bounds.getHeight()) / 2;

        double ascent = -bounds.getY();
        double baseY = y + ascent;

        if (!model.isSelected())
        {
            if (model.isArmed())
            {
                color = new Color(0, 210, 255);
            }
            else
            {
                color = new Color(255, 255, 255);
            }
        }
        else
        {
            if (model.isArmed())
            {
                color = new Color(255, 255, 255);
            }
            else
            {
                color = new Color(0, 210, 255);
            }
        }

        g2.setPaint(color);
        g2.drawString(this.getText(), (int) x, (int) baseY);
    }
}