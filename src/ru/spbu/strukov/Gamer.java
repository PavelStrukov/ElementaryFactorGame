package ru.spbu.strukov;

import java.util.Locale;

/**
 * Created on 21.11.2018 15:05:04
 *
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
class Gamer extends NumberOwner {

    private final int ownNumber;
    Coalition coalition;
    double rate;
    double pie;
    double offerPie;

    /**
     * Contructor for gamer
     *
     * @param i his own number and serial number
     * @param game
     */
    Gamer(int i, Game game) {
        ownNumber = i;
        calculateIncome();
    }

    /**
     * Calculates income
     */
    private void calculateIncome() {
        Game.calculateIncome(this);
    }

    /**
     * для вывода
     *
     * @return string
     */
    @Override
    public String toString() {
        return "[" + ownNumber + "_" + String.format(Locale.UK, "%5.2f", (double) pie) + "(" + coalition.name + ")" + "]";
    }

    /**
     * Takes own number
     *
     * @return own number
     */
    @Override
    public int getNumber() {
        return ownNumber;
    }

    /**
     * Gets income
     *
     * @return income
     */
    @Override
    double getProfit() {
        return pie;
    }

    /**
     * clears offers
     */
    @Override
    void clearOffer() {
        offer = null;
        offerPie = 0;
    }

}
