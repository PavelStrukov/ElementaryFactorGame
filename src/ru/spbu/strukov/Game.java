package ru.spbu.strukov;

import java.util.ArrayList;

/**
 * Main class of Elementary Factor Game.
 *
 * Created on 21.11.2018 14:55:07
 *
 * @author Alexander Mikhailovich Kovshov, Pavel Vladislavovich Strukov
 */
public class Game {

    static boolean canGamersMakeOffers = true;
    static boolean ADD_GAMER = true;
    static boolean OUT_GAMER = false;

    /**
     * coalitions and gamers contains ALL coalitions and gamers of the game
     * payoff is used in {@link eliminateGamerFromCoalition} and later
     */
    ArrayList<Coalition> coalitions;
    ArrayList<Gamer> gamers;
    PayOff payoff;

    /**
     * Method CalculateIncome Counts an Income for Gamer or Coalition
     *
     * @param gamerOrCoalition
     */
    static void calculateIncome(NumberOwner gamerOrCoalition) {
        gamerOrCoalition.setIncome(sumOfElementaryFactors(gamerOrCoalition.getNumber()));
    }

    /**
     * Metod sumOfElementaryFactors calculates an amount of
     *
     * @param number elementary factors
     * @return amount
     */
    static int sumOfElementaryFactors(int number) {
        ArrayList<Integer> elFactors = elementaryFactors(number);
        number = 0;
        for (Integer elFactor : elFactors) {
            number += elFactor;
        }
        return number;
    }

    /**
     * Method elementaryFactors Finds elementary factors of
     *
     * @param number
     * @return ArrayList
     */
    static ArrayList<Integer> elementaryFactors(int number) {
        ArrayList<Integer> ret = new ArrayList<>();
        int rest = number;
        boolean repeat;
        for (;;) {
            repeat = false;
            for (int i = 2; i <= Math.sqrt(rest); i++) {
                if (rest % i == 0) {
                    ret.add(i);
                    rest /= i;
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                ret.add(rest);
                break;
            }
        }
        return ret;
    }

    public class NumberProfit {

        int coalitionNumber;
        double pie;

        NumberProfit() {
            coalitionNumber = 0;
            pie = 0;
        }
    }

    /**
     * Constructor that creates object with separate coalitions.
     *
     * @param n
     * @param payoff
     */
    Game(int n, PayOff payoff) {
        this(n, payoff, false);
    }

    /**
     * Constructor that creates object with large coalition or separate
     * coalitions.
     *
     * @param n
     * @param payoff
     * @param large If true --- large coalition will be created, else --- each
     * gqmer in separate coalition.
     */
    Game(int n, PayOff payoff, boolean large) {
        this.payoff = payoff;
        coalitions = new ArrayList<>(n);
        gamers = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            gamers.add(new Gamer(i, this));
        }
        createInitialCoalitions(large);
    }

    /**
     * Creates initial coalitions at the begining of the game. Looks for best
     * possible coalition for each gamer.
     *
     * @param large is responsible for creating coalition of ALL gamers and
     * coalitions. If true creates large coalition of all gamers. If false
     * creates separate coalition for each gamer.
     */
    private void createInitialCoalitions(boolean large) {
        if (!large) {
            for (Gamer gamer : gamers) {
                ArrayList g = new ArrayList(1);
                g.add(gamer);
                createAndRregisterNewCoalition(g);
            }
        } else {
            createAndRregisterNewCoalition(new ArrayList(gamers));
        }
    }

    /**
     * Constuctor creates game without param large
     *
     * @param payoff
     * @param ns array of gamers
     */
    Game(PayOff payoff, int... ns) {
        this(payoff, false, ns);
    }

    /**
     * Constructor creates game
     *
     * @param payoff
     * @param large
     * @param ns array of gamers
     */
    Game(PayOff payoff, boolean large, int... ns) {
        this.payoff = payoff;
        coalitions = new ArrayList<>(ns.length);
        gamers = new ArrayList<>(ns.length);
        for (int i = 0; i < ns.length; i++) {
            gamers.add(new Gamer(ns[i], this));
        }
        createInitialCoalitions(large);
    }

    /**
     * Creates and registers new coalition from
     *
     * @param gamerList (type: ArrayList) adds to new coalition
     */
    private void createAndRregisterNewCoalition(ArrayList gamerList) {
        Coalition coalition = Coalition.makeCoalition(gamerList);
        calculateIncome(coalition);
        payoff.calculateGamersPie(coalition);
        coalitions.add(coalition);
    }

    /**
     * Creates and registers new coalition from creates new ArrayList from a
     * fiew gamerArray
     *
     * @param gamerArray (type: Array) adds to new coalition
     */
    private void createAndRregisterNewCoalition(Gamer... gamerArray) {
        ArrayList<Gamer> gamerList = new ArrayList<>(gamerArray.length);
        for (int i = 0; i < gamerArray.length; i++) {
            gamerList.add(gamerArray[i]);
        }
        createAndRregisterNewCoalition(gamerList);
    }

    void play() {
        for (int i = 0; i < 20; i++) {
            clearOffers();
            makeOffers();
            //вывод
            for (Gamer gamer : gamers) {
                System.out.println(gamer + "->" + gamer.offer);
            }
            for (Coalition coalition : coalitions) {
                System.out.println(coalition + "->" + coalition.offer);
            }
            processOffers();
            //вывод
            int sumCoal = 0;
            for (Coalition coalition : coalitions) {
                sumCoal += coalition.income;
            }
            int sumGamr = 0;
            for (Gamer gamer : gamers) {
                sumGamr += gamer.income;
            }
            for (Coalition coalition : coalitions) {
                coalition.lastChange++;
            }
            System.out.println(" ------------------------------------------------------- " + i + " # " + sumCoal + " ~ " + sumGamr);
        }
        System.out.println("===============================  Game over ==============================");
    }

    /**
     * This method clears offers for everyone
     */
    void clearOffers() {
        for (Coalition coalition : coalitions) {
            coalition.clearOffer();
        }
        for (Gamer gamer : gamers) {
            gamer.clearOffer();
        }
    }

    /**
     * This method gives offers
     */
    void makeOffers() {
        //Gamers make offers.
        if (canGamersMakeOffers) {
            for (Gamer gamer : gamers) {
                if (gamer.coalition.gamers.size() == 1) {
                    continue;
                }
                if (gamer.coalition.lastChange <= 1) {
                    continue;
                }
                for (Coalition coalition : coalitions) {
                    if (coalition == gamer.coalition) {
                        if (gamer.pie > gamer.income) {
                            continue;
                        }
                        if (gamer.coalition.lastChange <= 1) {
                            continue;
                        }
                        gamer.offer = coalition;
                        gamer.offerPie = gamer.income;
                    } else {
                        if (coalition.lastChange > 1) {
                            boolean przn = true;
                            for (Gamer gamer1 : coalition.gamers) {
                                if (gamer1.offer != null) {
                                    przn = false;
                                }
                            }
                            if (przn == true) {
                                Coalition plusCoalition = checkForOffer(coalition, gamer, ADD_GAMER);
                                if (plusCoalition.offerAvIncome <= gamer.pie) {
                                    continue;
                                }
                                if (plusCoalition.offerAvIncome <= gamer.offerPie) {
                                    continue;
                                }
                                gamer.offer = coalition;
                                gamer.offerPie = plusCoalition.offerAvIncome;
                            }
                        }
                    }
                }
                for (Gamer otherGamer : gamers) {
                    if (gamer.coalition.gamers.size() == 1) {
                        continue;
                    }
                    if (gamer == otherGamer) {
                        continue;
                    }
                    if (otherGamer.coalition == gamer.coalition
                            && gamer.coalition.gamers.size() == 2) {
                        continue;
                    }
                    Coalition gamerPlusGamer = checkForOffer(gamer, otherGamer);
                    if (gamerPlusGamer.offerAvIncome <= gamer.pie) {
                        continue;
                    }
                    if (gamerPlusGamer.offerAvIncome <= gamer.offerPie) {
                        continue;
                    }
                    gamer.offer = otherGamer;
                    gamer.offerPie = gamerPlusGamer.offerAvIncome;
                }
            }
        }
        for (Coalition coalition : coalitions) {
            for (Gamer gamer : gamers) {
                if (gamer.coalition.gamers.size() == 1) {
                    continue;
                }
                if (coalition.gamers.contains(gamer)) {
                    Coalition minusCoalition = checkForOffer(coalition, gamer, OUT_GAMER);
                    if (minusCoalition.offerAvIncome < (double) coalition.income / coalition.gamers.size()) {
                        continue;
                    }
                    if (minusCoalition.offerAvIncome <= coalition.offerAvIncome) {
                        continue;
                    }
                    coalition.offer = gamer;
                    coalition.offerAvIncome = minusCoalition.offerAvIncome;
                } else {
                    Coalition plusCoalition = checkForOffer(coalition, gamer, ADD_GAMER);
                    if (plusCoalition.offerAvIncome <= (double) coalition.income / coalition.gamers.size()) {
                        continue;
                    }
                    if (plusCoalition.offerAvIncome <= gamer.pie) {
                        continue;
                    }
                    coalition.offer = gamer;
                    coalition.offerAvIncome = plusCoalition.offerAvIncome;
                }
            }
            for (Coalition otherCoalition : coalitions) {
                if (otherCoalition == coalition) {
                    continue;
                }
                ArrayList<Gamer> unitedGamers = new ArrayList<>(coalition.gamers);
                unitedGamers.addAll(otherCoalition.gamers);
                Coalition unitedTemporaryCoalition = Coalition.makeTemporaryCoalition(unitedGamers);
                calculateIncome(unitedTemporaryCoalition);
                unitedTemporaryCoalition.offerAvIncome
                        = (double) unitedTemporaryCoalition.income / unitedTemporaryCoalition.gamers.size();
                if (unitedTemporaryCoalition.offerAvIncome < coalition.offerAvIncome) {
                    continue;
                }
                if (unitedTemporaryCoalition.offerAvIncome
                        <= ((double) coalition.income / coalition.gamers.size())) {
                    continue;
                }
                if (unitedTemporaryCoalition.offerAvIncome < otherCoalition.offerAvIncome) {
                    continue;
                }
                if (unitedTemporaryCoalition.offerAvIncome
                        <= ((double) otherCoalition.income / otherCoalition.gamers.size())) {
                    continue;
                }
                if (unitedTemporaryCoalition.offerAvIncome == coalition.offerAvIncome) {
                    try {
                        double avIncome = coalition.offer instanceof Coalition
                                ? coalition.offer.income
                                / ((Coalition) coalition.offer).gamers.size()
                                : coalition.offer instanceof Gamer
                                        ? ((Gamer) coalition.offer).pie : 0;
                        if ((double) otherCoalition.income / otherCoalition.gamers.size()
                                > avIncome) {
                            continue;
                        }
                    } catch (ArithmeticException e) {
                        e.printStackTrace(System.err);
                        System.out.println("BAD Coalition! " + coalition.offer.toString());
                    }
                }
                coalition.offer = otherCoalition;
                coalition.offerAvIncome = unitedTemporaryCoalition.offerAvIncome;
            }
        }
    }

    /**
     * Creates temporary coalition to check possible offer. Temporary coalition
     * can be created by joining a gamer to an initial coalition or by
     * elimination a gamer from an initial coalition.
     *
     * @param coalition Initial coalition.
     * @param gamer Gamer to join or eliminate.
     * @param addGamer If true, gamer will be joined, else gamer will be
     * eliminated.
     * @return Temporary coalition.
     */
    private Coalition checkForOffer(Coalition coalition, Gamer gamer, boolean addGamer) {
        ArrayList<Gamer> changedGamers = new ArrayList<>(coalition.gamers);
        if (addGamer) {
            changedGamers.add(gamer);
        } else {
            changedGamers.remove(gamer);
        }
        Coalition changedTemporaryCoalition = Coalition.makeTemporaryCoalition(changedGamers);
        return checkForOffer(changedTemporaryCoalition);
    }

    private Coalition checkForOffer(Gamer gamer1, Gamer gamer2) {
        Coalition pairCoalition = Coalition.makeTemporaryCoalition(gamer1, gamer2);
        return checkForOffer(pairCoalition);
    }

    private Coalition checkForOffer(Coalition coalition) {
        calculateIncome(coalition);
        coalition.offerAvIncome
                = (double) coalition.income / coalition.gamers.size();
        return coalition;
    }

    /**
     * This method is the process of accepting or cancelling offers
     */
    private void processOffers() {
        for (Gamer gamer : gamers) {
            if (gamer.offer == null) {
                continue;
            }
            if (gamer.offer.offer == gamer) {
                if (gamer.offer instanceof Coalition
                        && ((Coalition) gamer.offer).gamers.contains(gamer)) {
                    separateGamer(gamer);
                    continue;
                }
                if (gamer.offer instanceof Gamer) {
                    joinGamerToGamer(gamer, (Gamer) gamer.offer);
                } else {
                    joinGamerToCoalition(gamer, (Coalition) gamer.offer);
                }
            }
        }
        for (int i = 0; i < coalitions.size(); i++) {
            Coalition coalition = coalitions.get(i);
            if (coalition.offer == null) {
                continue;
            }
            if (coalition.offer instanceof Gamer
                    && coalition.gamers.contains((Gamer) coalition.offer)) {
                separateGamer((Gamer) coalition.offer);
                continue;
            } else if (coalition.offer.offer == coalition) {
                if (coalition.offer instanceof Gamer) {
                    joinGamerToCoalition((Gamer) coalition.offer, coalition);
                } else {
                    joinCoalitionToCoalition(coalition, (Coalition) coalition.offer);
                }
            }
        }
    }

    /**
     * Gamers decide to make coalition with each other
     *
     * @param gamer current gamer
     * @param offer an offer for current gamer
     */
    private void joinGamerToGamer(Gamer gamer, Gamer offer) {
        if (gamer.coalition.gamers.size() == 1) {
            joinGamerToCoalition(offer, gamer.coalition);
            return;
        }
        if (offer.coalition.gamers.size() == 1) {
            joinGamerToCoalition(gamer, offer.coalition);
            return;
        }
        eliminateGamerFromCoalition(gamer);
        eliminateGamerFromCoalition(offer);
        createAndRregisterNewCoalition(gamer, offer);
    }

    /**
     * Убирает игрока из этой коалиции.
     *
     * @param gamer Исключаемый игрок.
     */
    private void eliminateGamerFromCoalition(Gamer gamer) {
        gamer.coalition.lastChange = 0;
        gamer.clearOffer();
        gamer.coalition.clearOffer();
        gamer.coalition.gamers.remove(gamer);
        calculateIncome(gamer.coalition);
        payoff.calculateGamersPie(gamer.coalition);
    }

    /**
     * Delets gamer from his own personal coalition
     *
     * @param gamer
     */
    private void separateGamer(Gamer gamer) {
        eliminateGamerFromCoalition(gamer);
        createAndRregisterNewCoalition(gamer);
    }

    /**
     * Adds gamer to coalition
     *
     * @param gamer wants to join
     * @param offer wants to accept gamer
     */
    private void joinGamerToCoalition(Gamer gamer, Coalition offer) {
        if (gamer.coalition.gamers.size() == 1) {
            joinCoalitionToCoalition(offer, gamer.coalition);
        }
        eliminateGamerFromCoalition(gamer);
        gamer.coalition = offer;
        offer.lastChange = 0;
        gamer.clearOffer();
        offer.clearOffer();
        offer.gamers.add(gamer);
        calculateIncome(offer);
        payoff.calculateGamersPie(offer);
    }

    /**
     * Two coalitions decided to be together
     *
     * @param namer
     * @param offer
     */
    private void joinCoalitionToCoalition(Coalition namer, Coalition offer) {
        boolean change = false;
        if (namer.income < offer.income) {
            change = true;
        } else if (namer.income == offer.income && namer.offerAvIncome < offer.offerAvIncome) {
            change = true;
        } else if (namer.name > offer.name) {
            change = true;
        }
        if (change) {
            Coalition t = namer;
            namer = offer;
            offer = t;
        }
        coalitions.remove(offer);
        namer.gamers.addAll(offer.gamers);
        for (Gamer offerGamer : offer.gamers) {
            offerGamer.coalition = namer;
        }
        namer.clearOffer();
        namer.lastChange = 0;
        calculateIncome(namer);
        payoff.calculateGamersPie(namer);

    }

    public static void main(String[] args) {
//        Game game = new Game(16, new OutProportionalPayOff(), false);
//        Game game = new Game(16, new SingleProportionalPayOff(), false);
//        Game game = new Game(3, new OutProportionalPayOff());
//        Game game = new Game(new OutProportionalPayOff(), false, 1, 5, 8, 9, 10, 12, 27);
        Game game = new Game(new OutProportionalPayOff(), true, 1, 5, 8, 9, 10, 12, 27);
//        Game game = new Game(new OutProportionalPayOff(), false, 8, 9, 10, 5);
//        Game game = new Game(new SingleProportionalPayOff(), 10, 13);
//        Game game = new Game(new SingleProportionalPayOff(), 8, 5);
//        Game game = new Game(new SingleProportionalPayOff(), false, 4,6,8,21,32);
//        Game game = new Game(new EqualExcessPayOff(), true, 4,6,8,21,32);
//          Game game = new Game(new EqualExcessPayOff(), false, 98,100,120,125, 105,108,112,114);
//          Game game = new Game(new SingleProportionalPayOff(), true, 98,100,120,125, 105,108,112,114);
//          Game game = new Game(new SingleProportionalPayOff(), false, 98,100,120,125, 105,108,112,114);
        game.play();
        String out = "";
        int sumCoal = 0;
        for (Coalition coalition : game.coalitions) {
            out += coalition + "\r\n";
            sumCoal += coalition.income;
        }
        System.out.println(out);
        int sumGamr = 0;
        for (Gamer gamer : game.gamers) {
            sumGamr += gamer.income;
        }
        System.out.println("______Summary coalitions income ~ Summary single gamers income _______  " + sumCoal + " ~ " + sumGamr + " \r\n");

    }
}