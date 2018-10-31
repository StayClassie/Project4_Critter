package assignment4;


//algae' (AlgaePrime)
//simple critter
//designed similar to algae
//runs away from fights in a random direction




public class Critter2 extends Critter{

    @Override
    public String toString() { return "2"; }

    /**
     * Critter2 runs in a random direction when
     * doTimeStep() is called
     */



    @Override
    public void doTimeStep() {
        walk(getRandomInt(8));
    }

    /**
     * @param opponent not used
     *  cause the Critter2 to run in a random direction, always return false
     * @return boolean false
     */


    @Override
    public boolean fight(String opponent) {
        run(getRandomInt(8));
        return false;
    }

}
