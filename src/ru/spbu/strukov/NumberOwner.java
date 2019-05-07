package ru.spbu.strukov;

/**
 * Common class for gamers and coalitions Contains common fields and methods for
 * them Created on 28.11.2018 15:02:32
 *
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
abstract class NumberOwner {

    int income;
    NumberOwner offer;

    abstract int getNumber();

    void setIncome(int income) {
        this.income = income;
    }

    NumberOwner getOffer() {
        return offer;
    }

    abstract double getProfit();

    abstract void clearOffer();

    public NumberOwner() {
    }

}
