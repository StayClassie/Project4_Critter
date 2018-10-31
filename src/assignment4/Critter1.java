package assignment4;


//GYMRAT


//simple critter similar to the style of Craig

//These are the critters that are always in the GYM
//classic gymRats they never leave the gym
//They gain some energy each turn by working out
//They gain even more energy whenever they fight
//always down to fight
//never leave the gym so they never reproduce
//their energy is more than you can lift BRO


public class Critter1 extends Critter.TestCritter{
    @Override
    public String toString(){return "1";}
    private int dir;
    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];


    public Critter1(){
        dir = Critter.getRandomInt(8);
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
    }

    @Override
    public void doTimeStep() {
        setEnergy(getEnergy() + 5);
        walk(dir);

    }

    @Override
    public boolean fight(String opponent) {
        setEnergy(getEnergy() + 25);
        return true;
    }
    public static void runStats(java.util.List<Critter> gymRat) {
        int total_straight = 0;
        int total_left = 0;
        int total_right = 0;
        int total_back = 0;
        for (Object obj : gymRat) {
            Critter1 c = (Critter1) obj;
            total_straight += c.genes[0];
            total_right += c.genes[1] + c.genes[2] + c.genes[3];
            total_back += c.genes[4];
            total_left += c.genes[5] + c.genes[6] + c.genes[7];
        }
        System.out.print("" + gymRat.size() + " total Craigs    ");
        System.out.print("" + total_straight / (GENE_TOTAL * 0.01 * gymRat.size()) + "% straight   ");
        System.out.print("" + total_back / (GENE_TOTAL * 0.01 * gymRat.size()) + "% back   ");
        System.out.print("" + total_right / (GENE_TOTAL * 0.01 * gymRat.size()) + "% right   ");
        System.out.print("" + total_left / (GENE_TOTAL * 0.01 * gymRat.size()) + "% left   ");
        System.out.println();
    }
}
