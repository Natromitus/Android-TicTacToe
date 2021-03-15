package com.example.tictactoe;

public class GameMode
{
    private int m_nMapSize;
    private int m_nConnect;

    public int getMapSize()
    {
        return m_nMapSize;
    }

    public void setMapSize(int nMapSize)
    {
        m_nMapSize = nMapSize;
    }

    public int getConnect()
    {
        return m_nConnect;
    }

    public void setConnect(int nConnect)
    {
        m_nConnect = nConnect;
    }

    public GameMode(int nMapSize, int nConnect)
    {
        m_nMapSize = nMapSize;
        m_nConnect = nConnect;
    }
}
