package assignment4;

public class Critter4 extends Critter {

    //Rocky IV Critter
    //get it like the boxing movies
    //and since its critter 4 its rocky 4
    //always fighting

   //This is an aggressive critter
    //he will always fight when he encounters another critter
    //he will always run on his turn so he has to fight

    public String toString() {
        return "4";
    }

    @Override
    public void doTimeStep() {
        if(getEnergy() > Params.start_energy * 2.5
                && getEnergy() >= Params.min_reproduce_energy) {

            Critter1 child = new Critter1();

            reproduce(child, Critter.getRandomInt(8));
        }
        run(getRandomInt(8));
    }
    /**
   //runs on his turn
     //has to fight
     */
    @Override
    public boolean fight(String opponent) {
        return true;

        //above code does not matter
    }

}
