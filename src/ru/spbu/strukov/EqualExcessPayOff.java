package ru.spbu.strukov;

/**
 * Делёж при котором игроки получают долю, равную сумме их возможного одиночного выигрыша
 * и равной для всех игроков коалиционной премии. 
 * Если выигрыш коалиции меньше суммарных одиночных выигрышей игроков, 
 * то премия будет отрицательной.
 * 
 * Created on 20.03.2019 12:35:33
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
public class EqualExcessPayOff implements PayOff {

    @Override
    public void calculateGamersPie(Coalition coalition) {
        double excess = 0;
        for (Gamer gamer : coalition.gamers) {
            excess += gamer.income;                                             //Суммарный выигрыш игроков при игре в одиночной коалиции.
        }
        excess -= coalition.income;                                             //Разность выигрыша коалиции и суммарного выигрыша.
        excess /= -coalition.gamers.size();                                     //Доля разности на игрока.
        for (Gamer gamer : coalition.gamers) {
            gamer.pie = gamer.income + excess;                                  //Доля игрока в выигрыше коалиции.
        }        
    }

}
