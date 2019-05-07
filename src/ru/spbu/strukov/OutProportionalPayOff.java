package ru.spbu.strukov;

import java.util.ArrayList;

/**
 * Дележ, пропорциональный вкладу каждого игрока в коалицию, а именно,
 * пропорционально потери в выигрыше коалиции при выходе игрока из неё. При
 * таком дележе выигрыш отдельного игрока может быть отрицательным, если его
 * выход из коалиции увеличит её выигрыш.
 *
 * Created on 02.12.2018 7:03:11
 *
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
class OutProportionalPayOff implements PayOff {

    @Override
    public void calculateGamersPie(Coalition coalition) {
        for (Gamer gamer : coalition.gamers) {
            ArrayList<Gamer> minusGamers = new ArrayList<>(coalition.gamers);
            minusGamers.remove(gamer);
            Coalition minusGamerCoalition = Coalition.makeTemporaryCoalition(minusGamers);
            Game.calculateIncome(minusGamerCoalition);
            gamer.rate = coalition.income - minusGamerCoalition.income;
        }
        double sumRate = 0;
        for (Gamer gamer : coalition.gamers) {
            sumRate += gamer.rate;
        }
        for (Gamer gamer : coalition.gamers) {
            gamer.pie = (double) coalition.income * gamer.rate / sumRate;
        }
    }

}