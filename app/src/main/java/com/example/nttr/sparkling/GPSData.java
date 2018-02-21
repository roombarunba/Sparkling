package com.example.nttr.sparkling;

/**
 * Created by nttr on 2018/02/06.
 */

public class GPSData {
    public double ido;
    public double keido;
//    public int score;
    public int sort_score;
    public long time;

    public GPSData(double ido, double keido,  int score, long time) {
        this.ido = ido;
        this.keido = keido;
//        this.score = score;
        this.sort_score = 0 - score;
        this.time = time;
    }

}
