package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private MainActivity m_activity;

    private Button m_btnMakeGame;
    private Button m_btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_activity = this;

        m_btnMakeGame = (Button)findViewById(R.id.btn_makeGame);
        m_btnSettings = (Button)findViewById(R.id.btn_settings);

        m_btnMakeGame.setOnClickListener(new BtnMakeGameHandler());
        m_btnSettings.setOnClickListener(new BtnSettingsHandler());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameManager.getInstance().calculateBitmapSizes(displayMetrics.widthPixels, this);
    }

    private class BtnMakeGameHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(m_activity, Game.class);
            startActivity(intent);
        }
    }

    private class BtnSettingsHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(m_activity, Settings.class);
            startActivity(intent);
        }
    }
}