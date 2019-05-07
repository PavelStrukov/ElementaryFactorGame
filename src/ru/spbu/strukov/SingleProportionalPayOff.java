package ru.spbu.strukov;

/**
 * Делёж, при котором выигрыш коалиции распределяется пропорционально выигрышу
 * каждого игрока в его единичной коалиции.
 *
 * Created on 22.11.2018 2:53:08
 *
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
class SingleProportionalPayOff implements PayOff {

    @Override
    public void calculateGamersPie(Coalition coalition) {
        for (Gamer gamer : coalition.gamers) {
            gamer.rate = gamer.income;
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
