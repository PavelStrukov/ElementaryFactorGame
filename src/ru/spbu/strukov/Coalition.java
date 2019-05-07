package ru.spbu.strukov;

import java.util.ArrayList;

/**
 * Created on 21.11.2018 15:02:05
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
class Coalition extends NumberOwner{
    
    private static int count = 0;
    
    private static final boolean REAL_COALITION = true;
    private static final boolean TEMP_COALITION = false;
    
    public ArrayList<Gamer> gamers;
    public final int name;
    double offerAvIncome;
    
    public int lastChange = 0;
    
    public static Coalition makeTemporaryCoalition(Gamer... gamers) {
        return new Coalition(TEMP_COALITION, gamers);
    }
    
    public static Coalition makeTemporaryCoalition(ArrayList<Gamer> gamers) {
        return new Coalition(TEMP_COALITION, gamers);
    }
    
    public static Coalition makeCoalition(Gamer... gamers) {
        return new Coalition(REAL_COALITION, gamers);
    }
    
    public static Coalition makeCoalition(ArrayList<Gamer> gamers) {
        return new Coalition(REAL_COALITION, gamers);
    }
    
    private Coalition(boolean realCoalition, Gamer... gamers) {
        this.gamers = new ArrayList<>(gamers.length);
        for (Gamer gamer : gamers) {
            this.gamers.add(gamer);
        }
        name = realCoalition ? gamersAssign() : -1;
    }
    
    private Coalition(boolean realCoalition, ArrayList<Gamer> gamers) {
        this.gamers = gamers;
        name = realCoalition ? gamersAssign() : -1;
    }
    
    /**
     * This method creates links on this coalition for gamers in current coalition
     */
    private int gamersAssign() {
        for (Gamer gamer : gamers) {
            gamer.coalition = this;
        }
        return ++count;
    }
    
    @Override
    public String toString(){
        String out = "";
        int sum = 0;
        for (Gamer gamer : gamers) {
            out += gamer.toString();
            sum += gamer.getNumber();
        }
        out = "{" + name + ":" +  sum + "_" +  income + ";" + out + " <" + lastChange + ">}";
        return out;
    }

    /**
     * counts an own number of coalition
     * @return own number
     */
    @Override
    public int getNumber() {
        int sum = 0;
        for (Gamer gamer : gamers) {
            sum += gamer.getNumber();
        }
        return sum;
    }

    /**
     * To take an icome
     * @return a value of income
     */
    @Override
    double getProfit() {
        return income;
    }

    /**
     * Clears offers
     */
    @Override
    void clearOffer() {
        offer = null;
        offerAvIncome = 0;
    }
        
    public static Coalition makeTemporaryCoalitionByItsOrderNumber(int n, ArrayList<Gamer> allGamers) {
        ArrayList<Gamer> gamers = new ArrayList<>();
        for (int i = 0, k = 1; i < allGamers.size(); i++, k <<= 1) {
            if ((n & k) != 0) {
                gamers.add(allGamers.get(i));
            }
        }
        return gamers.isEmpty() ? null : makeTemporaryCoalition(gamers);
    }

}
