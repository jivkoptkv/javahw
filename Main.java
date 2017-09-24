package com.company;


import java.util.Scanner;

public class Main  {

    public Main() { }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int peshoAttackDamage = Integer.parseInt(in.nextLine());
        int goshoAttackDamage = Integer.parseInt(in.nextLine());

        Neighbour pesho = new Neighbour("Pesho","Roundhouse kick", peshoAttackDamage,true,100);
        Neighbour gosho = new Neighbour("Gosho","Thunderous fist",goshoAttackDamage,  false,100);

        FightGame game = new FightGame(pesho,gosho);
        
        game.Run();


        Task task = new GameOfNumbers();

        task.execute();
    }

    static class FightGame
    {
        private  final  String WINNER_MESSAGE_TEMPLATE = "%s won in %dth round.";

        private final int BONUS_HEALT_POINTS = 10;
        private final int BONUS_ROUND_CYCLE = 3; //give bnus healt each N-th turn

        private final Neighbour N1;
        private final Neighbour N2;

        private  int currentTurn = 0;

        private Neighbour winner = null;

        public FightGame(Neighbour N1, Neighbour N2) {
            this.N1 = N1;
            this.N2 = N2;
        }

        public void Run() {

            Fight();
            PrintWinner();
            
        }

        private void PrintWinner() {
            System.out.printf(String.format(WINNER_MESSAGE_TEMPLATE,winner.name,currentTurn));
        }

        private void Fight() {
            do{
                currentTurn++;

                if(N1.MakeTurnAgainst(N2))
                {
                    winner = N1;
                    break;
                }

                if(N2.MakeTurnAgainst(N1)){
                    winner = N2;
                    break;
                }

                if(isHealthBonusRound()){
                    N1.IncreaseHealth(BONUS_HEALT_POINTS);
                    N2.IncreaseHealth(BONUS_HEALT_POINTS);
                }

            }while (winner == null);
        }

        private boolean isHealthBonusRound() {
            return currentTurn % BONUS_ROUND_CYCLE == 0;
        }
    }
    static class Neighbour
    {
        private  final String ATTACK_MESSAGE_TEMPLATE = "%s used %s and reduced %s to %d health.\n";
        private final String name ;
        private final String attackName;
        private final boolean attacksWhenOdd;
        private final int attackDamage;
        private int healthPoints;
        private int currentTurn = 0;

        public Neighbour(String name ,String attackName,int attackDamage, boolean attacksWhenOdd,int healthPoints) {
            this.name = name;
            this.attackName=attackName;
            this.attacksWhenOdd=attacksWhenOdd;
            this.healthPoints = healthPoints;
            this.attackDamage = attackDamage;
        }

        public void Attack(Neighbour other) {

                other.DecreaseHealth(this.attackDamage);
                if(other.IsDead()){
                    return;
                }
                System.out.printf(ATTACK_MESSAGE_TEMPLATE,name,attackName,other.name,other.healthPoints);

        }

        private boolean shouldAttack() {
            return currentTurn % 2 == (attacksWhenOdd ? 1 : 0);
        }

        private void DecreaseHealth(int points) {
            this.healthPoints -= points;
        }

        public boolean IsDead() {
            return  healthPoints <= 0;
        }

        public void IncreaseHealth(int points) {
            this.healthPoints += points;
        }


        //Return true if the current player kills the other during the turn
        public boolean MakeTurnAgainst(Neighbour other) {

            currentTurn++;

            if(shouldAttack()){
                Attack(other);
            }

            return other.IsDead();
        }
    }

}
