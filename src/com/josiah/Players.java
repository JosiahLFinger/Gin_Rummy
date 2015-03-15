package com.josiah;

import java.util.ArrayList;

/**
 * Created by Yanirash on 3/5/2015.
 */
public class Players {
    private String name;
    private ArrayList<String> hand;
    private int score = 0;

    public Players(String name) {
        this.name = name;
        this.score = score;
        this.hand = new ArrayList<String>();

    }


    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }
    public ArrayList<String> getHand() {
        return hand;
    }
    public void clearHand() {
        this.hand.clear();
    }

    public void addCard(String value) {

        hand.add(value);
    }
    public void removeCard(String value) {
        hand.remove(value);
    }



    public void displayHand(Players player) {
        if (this.name != "Bob") {
            System.out.println("Your hand: \n");
            System.out.print("[ ");
            for (int y = 0; y < this.hand.size(); y++) {
                System.out.print(+(y + 1) + ". " + this.hand.get(y) + " | ");
            }
            System.out.println(" ]");
            System.out.println();
        } else {

            System.out.println(this.name + "'s hand: \n");
            for (int y = 0; y < this.hand.size(); y++) {
                System.out.print("[] ");
            }
            System.out.print("   " + this.hand.size() + " cards");
            System.out.println("\n");
        }
    }
}

