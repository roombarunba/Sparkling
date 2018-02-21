package com.example.nttr.sparkling;

/**
 * Created by nttr on 2018/02/06.
 */

public class GPSData {
    public double ido;
    public double keido;
//    public int m_score;
    public int m_sort_score;
    public long time;

    public GPSData(double ido, double keido,  int m_score, long time) {
        this.ido = ido;
        this.keido = keido;
//        this.score = m_score;
        this.m_sort_score = 0 - m_score;
        this.time = time;
    }

}
