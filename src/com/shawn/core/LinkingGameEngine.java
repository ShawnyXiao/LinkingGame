package com.shawn.core;

public class LinkingGameEngine
{
    private int row;
    private int column;

    public LinkingGameEngine(int row, int column)
    {
        this.row = row;
        this.column = column;
    }

    // initialize the map
    public int[][] initializeMap(int[] buttonChar)
    {
        int tempArray[] = new int[this.row * this.column];

        // initialize the tempArray
        for (int i = 0; i < this.row; i++)
        {
            System.arraycopy(buttonChar, 0, tempArray, i * this.row, this.column);
        }

        // randomize the tempArray
        int index, temp;
        for (int i = 0; i < this.row * this.column; i++)
        {
            index = (int) (Math.random() * (this.row * this.column - i)) + i;

            temp = tempArray[index];
            tempArray[index] = tempArray[i];
            tempArray[i] = temp;
        }

        // initialize the tempMap
        int tempMap[][] = new int[this.row + 2][this.column + 2];
        for (int i = 0; i < this.row + 2; i++)
        {
            for (int j = 0; j < this.column + 2; j++)
            {
                if (0 == i || 0 == j || this.row + 1 == i || this.column + 1 == j)
                {
                    tempMap[i][j] = 0;
                }
                else
                {
                    tempMap[i][j] = tempArray[(i - 1) * this.row + (j - 1)];
                }
            }
        }

        return tempMap;

    }

    // judge whether the char in map[firstRow][firstColumn] equals the char in map[secondRow][secondColumn]
    public boolean charMatch(int[][] map, int firstRow, int firstColumn, int secondRow, int secondColumn)
    {
        return map[firstRow][firstColumn] == map[secondRow][secondColumn];
    }

    // judge whether two points can be matched in vertical
    private boolean horizontalMatch(int[][] map, int firstRow, int firstColumn, int secondRow, int secondColumn)
    {
        if (firstRow != secondRow)
        {
            return false;
        }

        int tempColumn;

        if (secondColumn > firstColumn)
        {
            tempColumn = secondColumn - 1;

            while (tempColumn != firstColumn)
            {
                if (0 != map[secondRow][tempColumn])
                {
                    return false;
                }
                tempColumn--;
            }

            return true;
        }
        else if (secondColumn < firstColumn)
        {
            tempColumn = firstColumn - 1;

            while (tempColumn != secondColumn)
            {
                if (0 != map[firstRow][tempColumn])
                {
                    return false;
                }
                tempColumn--;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    // judge whether two points can be matched in horizontal
    private boolean verticalMatch(int[][] map, int firstRow, int firstColumn, int secondRow, int secondColumn)
    {
        if (firstColumn != secondColumn)
        {
            return false;
        }

        int tempRow;

        if (secondRow > firstRow)
        {
            tempRow = secondRow - 1;

            while (tempRow != firstRow)
            {
                if (0 != map[tempRow][secondColumn])
                {
                    return false;
                }
                tempRow--;
            }

            return true;
        }
        else if (secondRow < firstRow)
        {
            tempRow = firstRow - 1;

            while (tempRow != secondRow)
            {
                if (0 != map[tempRow][firstColumn])
                {
                    return false;
                }
                tempRow--;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    // judge whether two points can be matched in a line
    public boolean zeroCornerMatch(int[][] map, int firstRow, int firstColumn, int secondRow, int secondColumn)
    {
        return horizontalMatch(map, firstRow, firstColumn, secondRow, secondColumn)
                || verticalMatch(map, firstRow, firstColumn, secondRow, secondColumn);
    }

    // judge whether two points can be matched in two lines
    public boolean oneCornerMatch(int[][] map, int firstRow, int firstColumn, int secondRow, int secondColumn)
    {
        return map[firstRow][secondColumn] == 0
                && zeroCornerMatch(map, firstRow, firstColumn, firstRow, secondColumn)
                && zeroCornerMatch(map, secondRow, secondColumn, firstRow, secondColumn)
                ||
                map[secondRow][firstColumn] == 0
                        && zeroCornerMatch(map, firstRow, firstColumn, secondRow, firstColumn)
                        && zeroCornerMatch(map, secondRow, secondColumn, secondRow, firstColumn);
    }

    // judge whether two points can be matched in three lines
    public boolean twoCornerMatch(int[][] map, int firstRow, int firstColumn, int secondRow, int secondColumn)
    {
        int tempIndex;

        // north
        tempIndex = firstRow - 1;
        while (0 <= tempIndex && map[tempIndex][firstColumn] == 0)
        {
            if (oneCornerMatch(map, tempIndex, firstColumn, secondRow, secondColumn))
            {
                return true;
            }
            tempIndex--;
        }

        // south
        tempIndex = firstRow + 1;
        while (map.length > tempIndex && map[tempIndex][firstColumn] == 0)
        {
            if (oneCornerMatch(map, tempIndex, firstColumn, secondRow, secondColumn))
            {
                return true;
            }
            tempIndex++;
        }

        // west
        tempIndex = firstColumn - 1;
        while (0 <= tempIndex && map[firstRow][tempIndex] == 0)
        {
            if (oneCornerMatch(map, firstRow, tempIndex, secondRow, secondColumn))
            {
                return true;
            }
            tempIndex--;
        }

        // east
        tempIndex = firstColumn + 1;
        while (map[0].length > tempIndex && map[firstRow][tempIndex] == 0)
        {
            if (oneCornerMatch(map, firstRow, tempIndex, secondRow, secondColumn))
            {
                return true;
            }
            tempIndex++;
        }

        return false;
    }
}
