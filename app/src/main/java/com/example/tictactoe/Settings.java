package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import com.google.android.material.slider.Slider;

public class Settings extends AppCompatActivity
{
    private Button m_btnSubmit;
    private Slider m_slMapSize, m_slConnect;
    private Context m_context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        m_context = this;

        m_btnSubmit = (Button)findViewById(R.id.btn_submit);
        m_slMapSize = (Slider)findViewById(R.id.sl_mapSize);
        m_slConnect = (Slider)findViewById(R.id.sl_connect);

        m_slMapSize.setValue(GameManager.getInstance().getGameMode().getMapSize());
        m_slConnect.setValue(GameManager.getInstance().getGameMode().getConnect());

        m_btnSubmit.setOnClickListener(new BtnSubmitHandler());
    }

    private class BtnSubmitHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            int nMapSize = (int)m_slMapSize.getValue();
            int nConnect = (int)m_slConnect.getValue();

            GameManager.getInstance().setGameMode(new GameMode(nMapSize, nConnect));
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            GameManager.getInstance().calculateBitmapSizes(displayMetrics.widthPixels, m_context);

            finish();
        }
    }
}