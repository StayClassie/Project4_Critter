package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Chris Classie>
 * <csc2859>
 * <16355>

 * Slip days used: <1>
 * Fall 2018
 */

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */


        while(true) {

            String command = kb.nextLine();
            String[] parse = command.split(" ");



            try {

                if(command.equals("quit")) {
                    return;
                }
                else if(parse[0].equals("show")) {

                    if(parse.length > 1) {

                        System.out.println("error processing: " + command);

                        continue;
                    }
                    Critter.displayWorld();
                }
                else if(parse[0].equals("step")) {

                    int numberofsteps = 0;
                    int num = 1;

                    if(parse.length > 2) {

                        System.out.println("error processing: " + command);

                        continue;
                    }
                    else if(parse.length == 2) {


                        try {
                            num = Integer.parseInt(parse[1]);
                        }

                        catch (NumberFormatException e) {
                            System.out.println("error processing: " + command);
                            continue;
                        }
                    }

                    for(int i = 0; i < num; i++) {
                        Critter.worldTimeStep();
                        numberofsteps++;
                    }
                }

                else if(parse[0].equals("make")) {
                    try {

                        String critName = parse[1];
                        int numberofsteps = 0;
                        int num = 1;

                        if(parse.length > 3) {
                            System.out.println("error processing: " + command);

                            continue;
                        }
                        else if(parse.length == 3) {

                            num = Integer.parseInt(parse[2]);

                            if(num <= 0) {
                                System.out.println("error processing: " + command);
                                continue;
                            }
                        }
                        for(int i = 0; i < num; i++) {
                            Critter.makeCritter(critName);
                            numberofsteps++;
                        }
                    }
                    catch (Exception e){
                        System.out.println("error processing: " + command);
                    }

                }
                else if(parse[0].equals("seed")) {

                    if(parse.length > 2) {

                        System.out.println("error processing: " + command);

                        continue;
                    }

                    else if(parse.length == 2) {
                        try {
                            Critter.setSeed(Integer.parseInt(parse[1]));
                        }
                        catch (NumberFormatException e) {
                            System.out.println("error processing: " + command);
                        }
                        continue;
                    }
                    System.out.println("error processing: " + command);
                }
                else if(parse[0].equals("stats")) {
                    try {
                        String critName = parse[1];
                        if(parse.length > 2) {
                            System.out.println("error processing: " + command);
                            continue;
                        }
                        else if(parse.length == 2) {


                            List<Critter> critters = Critter.getInstances(critName);
                            List<Critter> critters2 = Critter.getInstances(critName);

                            Class<?> Crit = null;
                            Class<?> Crit2 = null;

                            Constructor<?> constructor = null;
                            Constructor<?> constructor2 = null;

                            Object instanceOfNewCritter = null;
                            Object obj = null;

                            try {
                                Crit = Class.forName(myPackage + "." + critName);
                            } catch (ClassNotFoundException e) {
                                throw new InvalidCritterException(critName);
                            }
                            try {
                                constructor = Crit.getConstructor();
                                instanceOfNewCritter = constructor.newInstance();
                                obj = instanceOfNewCritter;
                            }
                            catch(Exception e) {
                                throw new InvalidCritterException(critName);
                            }
                            Method Method = Crit.getMethod("runStats", java.util.List.class);
                            Critter type = (Critter)instanceOfNewCritter;
                            obj = instanceOfNewCritter;

                            try {
                                Method.invoke(type, critters);
                            }
                            catch(Exception e) {
                                Critter.runStats(critters);
                            }
                        }


                    }
                    catch (Exception e){
                        System.out.println("error processing: " + command);
                    }
                }
                else {
                    System.out.println("invalid command: " + command);
                }
            }
            catch(Exception e) {
                System.out.println("invalid command: " + command);
                e.printStackTrace();
            }
        }

    }
}