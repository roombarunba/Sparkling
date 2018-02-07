package com.example.nttr.sparkling;

/**
 * Created by nttr on 2018/02/06.
 */

public class Data {
    public String place;
    public int score;
    public int sort_score;

    public Data(String place, int score) {
        this.place = place;
        this.score = score;
        this.sort_score = 0 - score;
    }

}
