package com.shawn.ui;

import com.shawn.core.LinkingGameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class LinkingGameFrame extends JFrame
{
    private static final int DEFAULT_WIDTH = 850;
    private static final int DEFAULT_HEIGHT = 700;

    private static final int ROW = 8;
    private static final int COLUMN = 8;

    private int[][] map = new int[ROW + 2][COLUMN + 2];

    private int[] buttonChar = {
            '■', '〄', '③', '囍', '▲', '★', '∞', '〠'
    };

    LinkingGameButton[][] linkingGameButton;
    private OrdinaryButton level, levelDisplay, time, timeDisplay, newGame;

    private int levelNumber = 1, timeNumber = 65;

    private Timer timer;

    private int counterOfMatch = 0;

    private LinkingGameEngine linkingGameEngine = new LinkingGameEngine(ROW, COLUMN);

    public LinkingGameFrame()
    {
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 210, 255));
        this.add(panel);

        // set layout to GridBagLayou
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        // create some buttons
        level = new OrdinaryButton("      Level      ");
        level.setEnabled(false);
        panel.add(level, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

        levelDisplay = new OrdinaryButton(Integer.toString(levelNumber));
        levelDisplay.setEnabled(false);
        panel.add(levelDisplay, new GBC(0, 2).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

        time = new OrdinaryButton("      Time      ");
        time.setEnabled(false);
        panel.add(time, new GBC(11, 1).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

        timeDisplay = new OrdinaryButton(Integer.toString(timeNumber));
        timeDisplay.setEnabled(false);
        panel.add(timeDisplay, new GBC(11, 2).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

        newGame = new OrdinaryButton("      New      ");
        newGame.addActionListener(new NewGameButtonActionListener());
        panel.add(newGame, new GBC(11, 8).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

        // initialize map
        map = linkingGameEngine.initializeMap(buttonChar);


        LinkingGameButtonActionListener actionListener = new LinkingGameButtonActionListener();

        // create button of Linking Game
        linkingGameButton = new LinkingGameButton[ROW + 2][COLUMN + 2];

        for (int i = 0; i < ROW + 2; i++)
        {
            for (int j = 0; j < COLUMN + 2; j++)
            {
                if (0 == i || 0 == j || ROW + 1 == i || COLUMN + 1 == j)
                {
                    linkingGameButton[i][j] = new LinkingGameButton("");
                    linkingGameButton[i][j].setEnabled(false);
                }
                else
                {
                    linkingGameButton[i][j] = new LinkingGameButton(String.valueOf((char) map[i][j]));
                    linkingGameButton[i][j].addActionListener(actionListener);
                    linkingGameButton[i][j].setActionCommand(String.valueOf(i * 10 + j));
                }
                panel.add(linkingGameButton[i][j], new GBC(j + 1, i).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
            }
        }

        // creat timer and start it
        timer = new Timer();
        timer.schedule(new LinkingGameTimerTask(timeNumber), 0, 1000);

    }

    /**
     * a timer task that controls the schedule of game.
     *
     * @author Shawn（肖小粤）
     */
    private class LinkingGameTimerTask extends TimerTask
    {
        private int time;

        LinkingGameTimerTask(int time)
        {
            this.time = time;
        }

        // if time > 0, time will reduce.
        // Otherwise, button of Linking Game will show something.
        @Override
        public void run()
        {
            if (this.time < 0)
            {
                for (int i = 1; i < ROW + 1; i++)
                {
                    for (int j = 1; j < COLUMN + 1; j++)
                    {
                        linkingGameButton[i][j].setText(" ");
                        linkingGameButton[i][j].getModel().setSelected(false);
                        linkingGameButton[i][j].setEnabled(false);
                    }
                }

                // show that u'r lose
                linkingGameButton[4][3].setText("弱");
                linkingGameButton[4][4].setText("逼");
                linkingGameButton[4][5].setText("么");
                linkingGameButton[4][6].setText("？");

                cancel();
            }
            else
            {
                timeDisplay.setText(String.valueOf(this.time));
                this.time--;
            }
        }
    }

    /**
     * an action listener that listens events from buttons of Linking Game.
     *
     * @author Shawn（肖小粤）
     */
    private class LinkingGameButtonActionListener implements ActionListener
    {
        private int lastRow;
        private int lastColumn;

        @Override
        public void actionPerformed(ActionEvent e)
        {
            boolean match;// used for judging whether matched.

            // get row and column from e.getActionCommand()
            int row = Integer.parseInt(e.getActionCommand()) / 10;
            int column = Integer.parseInt(e.getActionCommand()) - row * 10;

            if (0 == this.lastRow && 0 == this.lastColumn)
            {
                this.lastRow = row;
                this.lastColumn = column;
            }
            else if (row == this.lastRow && column == this.lastColumn)
            {
                this.lastRow = 0;
                this.lastColumn = 0;
            }
            else
            {
                if (linkingGameEngine.charMatch(map, this.lastRow, this.lastColumn, row, column))
                {
                    if (linkingGameEngine.zeroCornerMatch(map, this.lastRow, this.lastColumn, row, column))
                    {
                        match = true;
                    }
                    else if (linkingGameEngine.oneCornerMatch(map, this.lastRow, this.lastColumn, row, column))
                    {
                        match = true;
                    }
                    else match = linkingGameEngine.twoCornerMatch(map, this.lastRow, this.lastColumn, row, column);
                }
                else
                {
                    match = false;
                }

                // if matched, char on button will disappear.
                // Otherwise, nothing will happen.
                if (match)
                {
                    map[this.lastRow][this.lastColumn] = 0;
                    map[row][column] = 0;

                    linkingGameButton[this.lastRow][this.lastColumn].getModel().setSelected(false);
                    linkingGameButton[this.lastRow][this.lastColumn].setText(" ");
                    linkingGameButton[this.lastRow][this.lastColumn].setEnabled(false);
                    linkingGameButton[row][column].getModel().setSelected(false);
                    linkingGameButton[row][column].setText(" ");
                    linkingGameButton[row][column].setEnabled(false);

                    this.lastRow = 0;
                    this.lastColumn = 0;

                    counterOfMatch++;
                    if (ROW * COLUMN / 2 == counterOfMatch)
                    {
                        // show that u'r win
                        if (1 == levelNumber)
                        {
                            linkingGameButton[3][3].setText("才");
                            linkingGameButton[3][4].setText("赢");
                            linkingGameButton[3][5].setText("第");
                            linkingGameButton[3][6].setText("一");
                            linkingGameButton[3][7].setText("关");
                            linkingGameButton[4][5].setText("小");
                            linkingGameButton[5][5].setText("渣");
                            linkingGameButton[6][5].setText("渣");
                            linkingGameButton[7][5].setText("！");
                        }
                        else if (2 == levelNumber)
                        {
                            linkingGameButton[3][4].setText("太");
                            linkingGameButton[3][5].setText("弱");
                            linkingGameButton[3][6].setText("了");
                            linkingGameButton[4][5].setText("脑");
                            linkingGameButton[5][5].setText("子");
                            linkingGameButton[6][5].setText("呢");
                            linkingGameButton[7][5].setText("？");
                        }
                        else if (3 == levelNumber)
                        {
                            linkingGameButton[3][4].setText("简");
                            linkingGameButton[3][5].setText("直");
                            linkingGameButton[3][6].setText("菜");
                            linkingGameButton[4][5].setText("3");
                            linkingGameButton[5][5].setText("岁");
                            linkingGameButton[6][5].setText("么");
                            linkingGameButton[7][5].setText("？");
                        }
                        else if (4 == levelNumber)
                        {
                            linkingGameButton[3][4].setText("还");
                            linkingGameButton[3][5].setText("是");
                            linkingGameButton[3][6].setText("弱");
                            linkingGameButton[4][5].setText("行");
                            linkingGameButton[5][5].setText("不");
                            linkingGameButton[6][5].setText("行");
                            linkingGameButton[7][5].setText("？");
                        }
                        else if (5 == levelNumber)
                        {
                            linkingGameButton[3][4].setText("还");
                            linkingGameButton[3][5].setText("行");
                            linkingGameButton[3][6].setText("嘛");
                            linkingGameButton[4][5].setText("小");
                            linkingGameButton[5][5].setText("菜");
                            linkingGameButton[6][5].setText("鸟");
                            linkingGameButton[7][5].setText("。");
                        }
                        else if (6 == levelNumber)
                        {
                            linkingGameButton[4][3].setText("可");
                            linkingGameButton[4][4].setText("以");
                            linkingGameButton[4][5].setText("喔");
                            linkingGameButton[4][6].setText("！");
                            linkingGameButton[5][2].setText("有");
                            linkingGameButton[5][3].setText("点");
                            linkingGameButton[5][4].setText("进");
                            linkingGameButton[5][5].setText("步");
                            linkingGameButton[5][6].setText("了");
                            linkingGameButton[5][7].setText("。");
                        }
                        else if (7 == levelNumber)
                        {
                            linkingGameButton[4][3].setText("厉");
                            linkingGameButton[4][4].setText("害");
                            linkingGameButton[4][5].setText("！");
                            linkingGameButton[4][6].setText("！");
                            linkingGameButton[5][1].setText("和");
                            linkingGameButton[5][2].setText("肖");
                            linkingGameButton[5][3].setText("小");
                            linkingGameButton[5][4].setText("粤");
                            linkingGameButton[5][5].setText("差");
                            linkingGameButton[5][6].setText("不");
                            linkingGameButton[5][7].setText("多");
                            linkingGameButton[5][8].setText("！");
                        }
                        else if (8 == levelNumber)
                        {
                            linkingGameButton[3][3].setText("卧");
                            linkingGameButton[3][4].setText("槽");
                            linkingGameButton[3][5].setText("？");
                            linkingGameButton[3][6].setText("！");
                            linkingGameButton[4][3].setText("超");
                            linkingGameButton[4][4].setText("神");
                            linkingGameButton[4][5].setText("了");
                            linkingGameButton[4][6].setText("！");
                            linkingGameButton[5][1].setText("比");
                            linkingGameButton[5][2].setText("肖");
                            linkingGameButton[5][3].setText("小");
                            linkingGameButton[5][4].setText("粤");
                            linkingGameButton[5][5].setText("还");
                            linkingGameButton[5][6].setText("厉");
                            linkingGameButton[5][7].setText("害");
                            linkingGameButton[5][8].setText("！");
                        }
                        else
                        {
                            linkingGameButton[2][3].setText("简");
                            linkingGameButton[2][4].setText("直");
                            linkingGameButton[2][5].setText("了");
                            linkingGameButton[2][6].setText("！");
                            linkingGameButton[3][4].setText("还");
                            linkingGameButton[4][4].setText("是");
                            linkingGameButton[5][4].setText("人");
                            linkingGameButton[6][4].setText("？");
                            linkingGameButton[7][1].setText("请");
                            linkingGameButton[7][2].setText("收");
                            linkingGameButton[7][3].setText("下");
                            linkingGameButton[7][4].setText("我");
                            linkingGameButton[7][5].setText("的");
                            linkingGameButton[7][6].setText("膝");
                            linkingGameButton[7][7].setText("盖");
                            linkingGameButton[7][8].setText("！");
                        }

                        timer.cancel();

                        newGame.setText("     Next     ");
                    }
                }
                else
                {
                    linkingGameButton[this.lastRow][this.lastColumn].getModel().setSelected(false);

                    this.lastRow = row;
                    this.lastColumn = column;
                }
            }
        }
    }

    /**
     * an action listener that listens events from button of New.
     *
     * @author Shawn（肖小粤）
     */
    private class NewGameButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            map = linkingGameEngine.initializeMap(buttonChar);// shuffle the map

            for (int i = 1; i < ROW + 1; i++)
            {
                for (int j = 1; j < COLUMN + 1; j++)
                {
                    linkingGameButton[i][j].setText(String.valueOf((char) map[i][j]));
                    linkingGameButton[i][j].setEnabled(true);
                }
            }

            counterOfMatch = 0;

            timer.cancel();
            timer = new Timer();

            if (e.getActionCommand().equals("      New      "))
            {
                timer.schedule(new LinkingGameTimerTask(timeNumber), 0, 1000);
            }
            else
            {
                timeNumber -= 5;
                timer.schedule(new LinkingGameTimerTask(timeNumber), 0, 1000);

                levelNumber++;
                levelDisplay.setText(Integer.toString(levelNumber));

                newGame.setText("      New      ");
            }
        }
    }
}
