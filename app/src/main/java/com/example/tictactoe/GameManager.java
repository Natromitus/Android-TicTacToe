package com.example.tictactoe;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.service.autofill.FieldClassification;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameManager
{
    // Singleton
    private static GameManager m_singleton = null;
    public static GameManager getInstance()
    {
        if(m_singleton == null)
            m_singleton = new GameManager();

        return m_singleton;
    }

    // Fields
    private final int[][][] m_arDirectionOffsets = new int[][][]
    {
        {
            // Vertical
            {0, -1},    // Up
            {0, 1}      // Down
        },
        {   // Horizontal
            {-1, 0},    // Left
            {1, 0}      // Right
        },
        {   // Diagonal TL - BR
            {-1, -1},    // To top-left
            {1, 1}      // To bottom-right
        },
        {   // Diagonal BL - TR
            {1, -1},    // To bottom-left
            {-1, 1}      // To top-right
        }
    };

    public enum MatchResult
    {
        eNone,
        ePlayer1Win,
        ePlayer2Win,
        eDraw
    }

    private GameMode m_gameMode = new GameMode(3, 3);
    private Bitmap m_bmpEmpty;
    private Bitmap m_bmpPlayerOne;
    private Bitmap m_bmpPlayerTwo;

    private TextView m_tvStatusBar;
    private MatchResult m_result = MatchResult.eNone;
    private Board m_curBoard;
    private ArrayList<Board> m_arTurns = new ArrayList<Board>();
    private boolean m_bIsPlayerOneActive = true;

    // Public Properties
    public GameMode getGameMode()
    {
        return m_gameMode;
    }

    public void setGameMode(GameMode gameMode)
    {
        m_gameMode = gameMode;
    }

    public Bitmap getEmptyBitmap()
    {
        return m_bmpEmpty;
    }

    public Bitmap getPlayerOneBitmap()
    {
        return m_bmpPlayerOne;
    }

    public Bitmap getPlayerTwoBitmap()
    {
        return m_bmpPlayerTwo;
    }

    public void setStatusBar(TextView tvStatusBar)
    {
        m_tvStatusBar = tvStatusBar;
    }

    public MatchResult getResult()
    {
        return m_result;
    }

    public ArrayList<Board> getTurns()
    {
        return m_arTurns;
    }

    public boolean getIsPlayerOneActive()
    {
        return m_bIsPlayerOneActive;
    }

    public void curMoveChange(int nOffset)
    {
        int nCurIndex = m_arTurns.indexOf(m_curBoard);
        int nNewIndex = nCurIndex + nOffset;
        if(nNewIndex < 0 || nNewIndex > m_arTurns.size() - 1)
            return;

        setCurBoard(m_arTurns.get(nNewIndex));
    }

    public boolean isCurMoveLast()
    {
        return m_arTurns.get(m_arTurns.size() - 1).equals(m_curBoard);
    }

    public void goToLastMove()
    {
        setCurBoard(m_arTurns.get(m_arTurns.size() - 1));
    }

    // Private Properties
    private void setResult(MatchResult result)
    {
        m_result = result;
        switch(m_result)
        {
            case ePlayer1Win:
                m_tvStatusBar.setText(R.string.tv_firstPlayerWin);
                break;
            case ePlayer2Win:
                m_tvStatusBar.setText(R.string.tv_secondPlayerWin);
                break;
            case eDraw:
                m_tvStatusBar.setText(R.string.tv_draw);
                break;
        }
    }

    private void setCurBoard(Board board)
    {
        m_curBoard = board;
        for (Board turn : m_arTurns)
            turn.setBoardVisibility(false);

        board.setBoardVisibility(true);
    }

    private void setIsPlayerOneActive(boolean bIsActive)
    {
        m_bIsPlayerOneActive = bIsActive;
        if(m_bIsPlayerOneActive)
            m_tvStatusBar.setText(R.string.tv_firstPlayerMove);
        else
            m_tvStatusBar.setText(R.string.tv_secondPlayerMove);
    }

    private GameManager()
    {
    }

    // Functions
    public void calculateBitmapSizes(int nDisplayWidth, Context context)
    {
        // Display Width - Margins - Gaps between fields
        int nBoardWidth = nDisplayWidth - (2 * 20) - ((getGameMode().getMapSize() + 1) * 20);
        int nFieldSize = nBoardWidth / getGameMode().getMapSize();

        m_bmpEmpty = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_empty);
        m_bmpEmpty = Bitmap.createScaledBitmap(m_bmpEmpty, nFieldSize, nFieldSize, true);
        m_bmpPlayerOne = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_player_one);
        m_bmpPlayerOne = Bitmap.createScaledBitmap(m_bmpPlayerOne, nFieldSize, nFieldSize, true);
        m_bmpPlayerTwo = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_player_two);
        m_bmpPlayerTwo = Bitmap.createScaledBitmap(m_bmpPlayerTwo, nFieldSize, nFieldSize, true);
    }

    public void restart()
    {
        if(m_arTurns.size() != 0)
            m_arTurns.get(0).getBoardContainer().removeAllViews();

        m_arTurns = new ArrayList<Board>();
        setResult(MatchResult.eNone);
        setIsPlayerOneActive(true);
    }

    public void makeMove(Field field)
    {
        Board board = null;
        try
        {
            board = (Board)field.getBoard().clone();
        }
        catch(CloneNotSupportedException e)
        {
        }

        Field[][] fields = board.getFields();
        int nFieldX = field.getCordX();
        int nFieldY = field.getCordY();

        if(m_bIsPlayerOneActive)
            fields[nFieldX][nFieldY].setState(Field.FieldState.ePlayer1);
        else
            fields[nFieldX][nFieldY].setState(Field.FieldState.ePlayer2);

        MatchResult result = checkWin(board, fields[nFieldX][nFieldY]);
        addTurn(board);

        if(result != MatchResult.eNone)
        {
            setResult(result);
            return;
        }

        if(checkDraw(board))
        {
            setResult(MatchResult.eDraw);
            return;
        }

        setIsPlayerOneActive(!m_bIsPlayerOneActive);
    }

    public void addTurn(Board board)
    {
        m_arTurns.add(board);
        setCurBoard(m_arTurns.get(m_arTurns.size() - 1));
    }

    private MatchResult checkWin(Board board, Field lastMove)
    {
        int nConnected;
        Field checkField = null;
        for (int[][] direction : m_arDirectionOffsets)
        {
            nConnected = 1;
            for (int[] offsets : direction)
            {
                if (lastMove.getCordX() + offsets[0] < 0 ||
                    lastMove.getCordY() + offsets[1] < 0 ||
                    lastMove.getCordX() + offsets[0] > board.getMapSize() - 1 ||
                    lastMove.getCordY() + offsets[1] > board.getMapSize() - 1)
                {
                    continue;
                }

                checkField = board.getFields()[lastMove.getCordX() + offsets[0]][lastMove.getCordY() + offsets[1]];
                while (checkField.getState() == lastMove.getState())
                {
                    if (++nConnected >= m_gameMode.getConnect())
                        return m_bIsPlayerOneActive ? MatchResult.ePlayer1Win : MatchResult.ePlayer2Win;

                    if (checkField.getCordX() + offsets[0] < 0 ||
                        checkField.getCordY() + offsets[1] < 0 ||
                        checkField.getCordX() + offsets[0] > board.getMapSize() - 1 ||
                        checkField.getCordY() + offsets[1] > board.getMapSize() - 1)
                    {
                        break;
                    }

                    checkField = board.getFields()[checkField.getCordX() + offsets[0]][checkField.getCordY() + offsets[1]];
                }
            }
        }
        
        return MatchResult.eNone;
    }

    private boolean checkDraw(Board board)
    {
        for(Field[] row : board.getFields())
        {
            for(Field item : row)
            {
                if(item.getState() == Field.FieldState.eNone)
                    return false;
            }
        }

        return true;
    }
}