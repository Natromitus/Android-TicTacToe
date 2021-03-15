package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game extends AppCompatActivity
{
    private TextView m_tvStatusBar;
    private LinearLayout m_llBoardContainer;
    private Button m_btnPrevMove;
    private Button m_btnNextMove;
    private Button m_btnRestart;
    private Button m_btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        m_tvStatusBar = (TextView)findViewById(R.id.tv_statusBar);
        m_llBoardContainer = (LinearLayout)findViewById(R.id.llay_boardContainer);
        m_btnPrevMove = (Button)findViewById(R.id.btn_prevMove);
        m_btnNextMove = (Button)findViewById(R.id.btn_nextMove);
        m_btnRestart = (Button)findViewById(R.id.btn_restart);
        m_btnHome = (Button)findViewById(R.id.btn_home);

        m_btnPrevMove.setOnClickListener(new BtnPrevMoveHandler());
        m_btnNextMove.setOnClickListener(new BtnNextMoveHandler());
        m_btnRestart.setOnClickListener(new BtnRestartHandler());
        m_btnHome.setOnClickListener(new BtnHomeHandler());

        GameManager.getInstance().setStatusBar(m_tvStatusBar);
        startGame();
    }

    private void startGame()
    {
        GameManager.getInstance().restart();
        int nMapSize = GameManager.getInstance().getGameMode().getMapSize();
        Board board = Board.create(m_llBoardContainer, this, nMapSize);
        GameManager.getInstance().addTurn(board);
    }

    private class BtnPrevMoveHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            GameManager.getInstance().curMoveChange(-1);
        }
    }

    private class BtnNextMoveHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            GameManager.getInstance().curMoveChange(1);
        }
    }

    private class BtnRestartHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            GameManager.getInstance().restart();
            startGame();
        }
    }

    private class BtnHomeHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    }
}