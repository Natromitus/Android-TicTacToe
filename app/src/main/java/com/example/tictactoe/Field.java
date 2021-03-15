package com.example.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class Field extends androidx.appcompat.widget.AppCompatImageButton implements View.OnClickListener
{
    public enum FieldState
    {
        eNone,
        ePlayer1,
        ePlayer2
    }

    private Board m_board;
    private int m_nX;
    private int m_nY;
    private FieldState m_fieldState = FieldState.eNone;

    public Board getBoard()
    {
        return m_board;
    }

    public int getCordX()
    {
        return m_nX;
    }

    public int getCordY()
    {
        return m_nY;
    }

    public FieldState getState()
    {
        return m_fieldState;
    }

    public void setState(FieldState state)
    {
        m_fieldState = state;

        switch(m_fieldState)
        {
            case eNone:
                setImageBitmap(GameManager.getInstance().getEmptyBitmap());
                break;
            case ePlayer1:
                setImageBitmap(GameManager.getInstance().getPlayerOneBitmap());
                break;
            case ePlayer2:
                setImageBitmap(GameManager.getInstance().getPlayerTwoBitmap());
                break;
        }
    }

    public Field(Context context, Board board, int nX, int nY)
    {
        super(context);

        m_board = board;
        m_nX = nX;
        m_nY = nY;

        setOnClickListener(this::onClick);

        setScaleType(ScaleType.CENTER_CROP);
        setBackgroundColor(Color.WHITE);
        setState(FieldState.eNone);
    }

    @Override
    public void onClick(View v)
    {
        if(GameManager.getInstance().getResult() != GameManager.MatchResult.eNone)
            return;

        // Check if we are on last move, if so then go to last move
        if(!GameManager.getInstance().isCurMoveLast())
        {
            GameManager.getInstance().goToLastMove();
            return;
        }

        // Field is taken
        if(m_fieldState != FieldState.eNone)
            return;

        GameManager.getInstance().makeMove(this);
    }
}
