package com.example.tictactoe;

import android.app.ActionBar;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Board implements Cloneable
{
    private Game m_gameActivity;
    private LinearLayout m_boardContainer;

    private int m_nMapSize;
    private GridLayout m_boardView;
    private Field[][] m_arFields;

    public LinearLayout getBoardContainer()
    {
        return m_boardContainer;
    }
    public int getMapSize()
    {
        return m_nMapSize;
    }
    public Field[][] getFields()
    {
        return m_arFields;
    }

    public void setBoardVisibility(boolean bIsVisible)
    {
        if(bIsVisible)
            m_boardView.setVisibility(View.VISIBLE);
        else
            m_boardView.setVisibility(View.GONE);
    }

    private Board()
    {
    }

    public static Board create(LinearLayout boardContainer, Game gameActivity, int nMapSize)
    {
        Board board = new Board();
        board.m_gameActivity = gameActivity;
        board.m_boardContainer = boardContainer;
        board.m_nMapSize = nMapSize;
        board.m_arFields = new Field[nMapSize][nMapSize];

        // Creating View for this board
        board.m_boardView = new GridLayout(gameActivity);
        LinearLayout.LayoutParams boardContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        boardContainerParams.setMargins(20, 20, 20, 20);
        board.m_boardView.setLayoutParams(boardContainerParams);
        board.m_boardView.setBackgroundColor(Color.GRAY);
        board.m_boardView.setRowCount(nMapSize);
        board.m_boardView.setColumnCount(nMapSize);
        boardContainer.addView(board.m_boardView);

        for(int y = 0; y < board.m_nMapSize; ++y)
        {
            for(int x = 0; x < board.m_nMapSize; ++x)
            {
                Field field = new Field(gameActivity, board, x, y);
                GridLayout.LayoutParams btnParams = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL,1f),
                        GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
                btnParams.height = 0;
                btnParams.width = 0;
                btnParams.setMargins(
                        x == 0 ? 20 : 10,
                        y == 0 ? 20 : 10,
                        x == board.m_nMapSize - 1 ? 20 : 10,
                        y == board.m_nMapSize - 1 ? 20 : 10);
                field.setLayoutParams(btnParams);
                board.m_boardView.addView(field);

                board.m_arFields[x][y] = field;
            }
        }

        return board;
    }

    public Object clone() throws CloneNotSupportedException
    {
        Board board = (Board)super.clone();
        board.m_arFields = new Field[m_nMapSize][m_nMapSize];

        // We have to copy board manually since we can't use clone method
        board.m_boardView = new GridLayout(m_gameActivity);
        LinearLayout.LayoutParams boardContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        boardContainerParams.setMargins(20, 20, 20, 20);
        board.m_boardView.setLayoutParams(boardContainerParams);
        board.m_boardView.setBackgroundColor(Color.GRAY);
        board.m_boardView.setRowCount(m_nMapSize);
        board.m_boardView.setColumnCount(m_nMapSize);
        m_boardContainer.addView(board.m_boardView);

        for(int y = 0; y < board.m_nMapSize; ++y)
        {
            for(int x = 0; x < board.m_nMapSize; ++x)
            {
                Field field = new Field(m_gameActivity, board, x, y);
                field.setState(m_arFields[x][y].getState());

                GridLayout.LayoutParams btnParams = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL,1f),
                        GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
                btnParams.height = 0;
                btnParams.width = 0;
                btnParams.setMargins(
                        x == 0 ? 20 : 10,
                        y == 0 ? 20 : 10,
                        x == board.m_nMapSize - 1 ? 20 : 10,
                        y == board.m_nMapSize - 1 ? 20 : 10);
                field.setLayoutParams(btnParams);
                board.m_boardView.addView(field);

                board.m_arFields[x][y] = field;
            }
        }

        return board;
    }
}
