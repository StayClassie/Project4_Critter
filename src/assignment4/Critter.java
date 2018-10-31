package assignment4;

/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Chris Classie>
 * <CSC2859>
 * <16355>
 * Slip days used: <1>
 * Fall 2018
 */




import java.util.List;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }

	private void setEnergy(int energy){this.energy = energy;}
	
	private int x_coord;
	private int y_coord;
	
	protected final void walk(int direction) {
		switch (direction) {

			case 0:
				x_coord++;
				if (x_coord >= Params.world_width) {
					x_coord = 0;
				}
				break;

			case 1:
				y_coord++;
				if (y_coord > Params.world_height - 1) {
					y_coord = 0;
				}
				break;

			case 2:
				x_coord++;
				y_coord++;

				if (y_coord > Params.world_height - 1
						&& x_coord > Params.world_width - 1) {
					x_coord = 0;
					y_coord = 0;
				}

				if (x_coord > Params.world_width - 1) {
					x_coord = 0;
				}
				if (y_coord > Params.world_height - 1) {
					y_coord = 0;
				}
				break;

			case 3:
				x_coord--;
				if (x_coord < 0) {
					x_coord = Params.world_width - 1;
				}
				break;

			case 4:
				y_coord--;
				if (y_coord < 0) {
					y_coord = Params.world_height - 1;
				}
				break;

			case 5:
				x_coord++;
				y_coord--;

				if(x_coord >= Params.world_width
						&& y_coord < 0){
					y_coord= Params.world_height - 1;
					x_coord = 0;
				}

				if(y_coord < 0){
					y_coord = Params.world_height - 1;
				}

				if(x_coord >= Params.world_width){
					x_coord = 0;
				}
				break;

			case 6:
				x_coord--;
				y_coord--;

				if(y_coord < 0
						&& x_coord < 0){
					x_coord = Params.world_width - 1;
					y_coord = Params.world_height - 1;
				}

				if(x_coord < 0){
					x_coord = Params.world_width - 1;
				}

				if(y_coord < 0){
					y_coord = Params.world_height - 1;
				}
				break;

			case 7:
				y_coord++;
				x_coord--;

				if(x_coord < 0
						&& y_coord > Params.world_height - 1){

					x_coord = Params.world_width - 1;
					y_coord = 0;

				}

				if(y_coord > Params.world_height - 1){
					y_coord = 0;
				}

				if(x_coord < 0){
					x_coord = Params.world_width - 1;
				}
				break;
		}

	}
	
	protected final void run(int direction) {
		this.energy += (2*Params.walk_energy_cost);
		this.energy -= Params.rest_energy_cost;
		this.energy -= Params.run_energy_cost;
		walk(direction);
		walk(direction);
		
	}
	
	protected final void reproduce(Critter offspring, int direction) {

		//here we want to check if there is enough energy

		if(this.energy < Params.min_reproduce_energy){
			return;
		}

		//First step sets the offsprings energy to half of the parents
		//Second Step: gives the parents half energy and rounds up

		offspring.energy = this.energy / 2;
		this.energy = this.energy / 2 + this.energy & 2;


		//These two set the proper coordinates to the parents location

		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;

		//move in specific direction
		offspring.walk(direction);

		//add to list of babies
		babies.add(offspring);
	}


	private static void fightInstance(Critter crit1, Critter crit2){

		crit1.energy = crit1.energy - Params.rest_energy_cost;
		crit2.energy = crit2.energy - Params.rest_energy_cost;

		if(crit1.getEnergy() <= 0 || crit2.getEnergy() <= 0){
			return;
		}

		int crit1_AttackRoll = 0;
		int crit2_AttackRoll = 0;

		if(crit1.fight(crit2.toString())){
			if(crit1.getEnergy() <= 0){
				return;
			}
			crit1_AttackRoll = Critter.getRandomInt(crit1.getEnergy());
		}
		if(crit2.fight(crit1.toString())){
			if(crit2.getEnergy() <= 0){
				return;
			}
			crit2_AttackRoll = Critter.getRandomInt(crit2.getEnergy());
		}
		if(crit1.x_coord == crit2.x_coord
				&& crit1.y_coord == crit2.y_coord){
			if(crit1_AttackRoll >= crit2_AttackRoll){
				crit1.energy = crit2.getEnergy() / 2;
				crit2.energy = 0;
			}
			else{
				crit2.energy = crit1.getEnergy() / 2;
				crit1.energy = 0;
			}
		}

		return;

	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {

		String className = myPackage + "." + critter_class_name;
		Class<?> newCritterClass = null;

		Class<?> c = null;
		int i = 0;
		int j = 0;

		try {

			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		try {

				Critter newCritter = (Critter) c.newInstance();
				Critter.population.add(newCritter);
				newCritter.energy = Params.start_energy;
				newCritter.x_coord = Critter.getRandomInt(Params.world_width);
				newCritter.y_coord = Critter.getRandomInt(Params.world_height);

		} catch (InstantiationException | IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		}

	}

	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();

		Class<?> critter = null;
		Class<?> crit;

		String className = myPackage+"."+critter_class_name;



		try{
			critter = Class.forName(className);
		}
		catch(ClassNotFoundException e){
			throw new InvalidCritterException(critter_class_name);
		}


		for(Critter c : population){
			if(critter.isInstance(c)){
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}

		public static void fightInstance(Critter a, Critter b){
			Critter.fightInstance(a,b);
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	public static void worldTimeStep() {
		int babycount = 0;
		boolean algaeRef = true;

		for (Critter c : population){
			c.doTimeStep();
		}

		for (Critter crit1: population){
			for(Critter crit2: population){
				if(crit1 != crit2
						&& crit1.x_coord == crit2.x_coord
						&& crit1.y_coord == crit2.y_coord){
						fightInstance(crit1,crit2);
				}
			}
		}

		for(Critter c: population){
			c.energy = c.energy - Params.rest_energy_cost - Params.walk_energy_cost;
		}

		for(int i = 0; i < Params.refresh_algae_count; i++){
			try{
				makeCritter("Algae");
				algaeRef = true;
			}
			catch (InvalidCritterException e){
				System.out.println("Algae didn't refresh");
				algaeRef = false;
			}
		}

		for(Critter babyCritter : babies){
			population.add(babyCritter);
			babycount++;

		}
		babies.clear();

	for(int i = 0; i < population.size();){
		if(population.get(i).energy <= 0){
			population.remove(i);
		}
		else{
			i++;
		}
	}

	}
	
	public static void displayWorld() {

	String[][] worldArray = new String[Params.world_height+2][Params.world_width+2];

	//char[][] world = new char[Params.world_width][Params.world_height];

	int rows = Params.world_height + 2;
	int cols = Params.world_width + 2;
	int displaycounter = 0;
	int i;
	int j;

	//first of possible array instance, rows and cols both 0
	worldArray[0][0] = "+";

	//second option, take rows-1 and 0 for col
		 worldArray[0][cols - 1] = "+";

	//third option take value of rows = 0 and col - 1
		worldArray[rows-1][0] = "+";

	//fourth option both rows and cols - 1
	worldArray[rows-1][cols-1] = "+";

	for(int x = 1; x < rows-1; x++){

		worldArray[x][0] = "|";

		worldArray[x][cols-1]= "|";
	}

	for(int x = 1; x < cols-1; x++){

		worldArray[0][x] = "-";

		worldArray[rows-1][x] = "-";
	}

	for(Critter c : population){

		worldArray[c.y_coord+1][c.x_coord+1] = c.toString();
		displaycounter++;
	}

	for(int x = 0;  x < rows; x++){

		//int coord = displaycounter;
	//	int rows2 = rows;
		//int col2 = cols;

		String result = "";

		for(int y = 0; y<cols; y++){

			if(worldArray[x][y] == null)
				result+= " ";

			else
				result+= worldArray[x][y];

		}
		System.out.println(result);
	}







	}
}
