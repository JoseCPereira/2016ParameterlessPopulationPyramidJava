package com.z_P3;

// import org.apache.commons.math3.random.MersenneTwister;							// Not necessary. Using local MersenneTwisterFast instead.
	

public class P3{
	public  static int    	 	   	         p3Runs;								// Number of runs to perform with the same problem.
	public  static int		 	   	       nSuccess;								// Number of successful runs;
	public  static P3Engine 	   	       p3Engine;
	
	private static String 	 	   	    p3ParamFile;								// Name of the main parameters file.
	public  static final MersenneTwisterFast random = new MersenneTwisterFast();	// Responsible for all random number operations.
	
	public static void main(String[] args){			
		//random.setSeed(654321);													// This will fix the sequence of seeds that will be used on each run of the Portfolio.
        p3ParamFile = args[0];
		nSuccess 	  = 0;
		p3Engine      = new P3Engine(p3ParamFile);									// Initialize the P3 Engine. This also initializes all parameters, including p3Runs.
		P3Press.initializePress();									
		P3Press.printInitialInfo();
		
		for(int nRun = 0; nRun < P3.p3Runs; nRun++){								// Perform all runs of P3.
			P3Press.printRunInitialInfo(nRun);
			nSuccess += p3Engine.RUN(nRun);
			P3Press.printRunFinalInfo(nRun);										// Print success status for this run. Print current success rate.
		}
		
		P3Press.printFinalInfo();
		P3Press.closeTestFileOut();
	}		
}		
		

							
		





