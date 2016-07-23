package com.z_P3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;





class P3Parameter{
	
	// NOTE!! optionNames must coincide exactly with the option names in the 'P3Parameters.txt' file.
	private static final String optionNames[] =     
	{"p3Runs", "hillClimberType",
	 "problemType", "stringSize", "kayBipolar", "trapSize", "stepSize", "ISGDataFile", "bitsPerFloat", "clauseRatio",
	 "maxIterations", "maxFitnessCalls", "maxTimeAllowed", "goodEnough", "foundOnes",
	 "verbose"
	 };
	
	
	private static int hillClimberType;						// HillClimber type. At the moment the only option is 1 --> First Improvement HC.
	private static int     problemType;						// Problem type. Check 'TemplateParameters.txt' for the option menu.
	private static int      kayBipolar;						// Size of each bipolar trap.       NOTE: kayBipolar must be even. This is only for the K-DECEPTIVE BIPOLAR problems.
	private static int        trapSize;						// Size of each deceptive trap.     NOTE: This is only for TRAP-K and STEPTRAP-K,S problems.
	private static int        stepSize;						// Size of each step trap.          NOTE: This is only for STEPTRAP-K,S problems.
	private static String  ISGDataFile;						// Full path to Ising Spin Glasses data file.
	private static int    bitsPerFloat;						// Number of bits per float.        NOTE: This is only for DISCRETIZED-RASTRIGIN problems.
	private static float   clauseRatio;						// Ratio between the number of clauses and problem size: clauseRatio = nClauses * stringSize.
															//								    NOTE: this is only for MAXSAT problems.
	
	public static int getProblemType(){return problemType;}	// NOTE: This is for testing only!!
	
	
	
	public static void initializeParameters(String parameterFile){				// NOTE: Execute this initialization PRIOR to any other.		
		try{
			FileInputStream fstream = new FileInputStream(parameterFile);		// Open the file to be read			
			DataInputStream in = new DataInputStream(fstream);					// Create an object of DataInputStream
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			int nLine = 0; 														// Line number
			String line;
			while((line = buff.readLine())!= null){
				nLine++; 														// Increment line number
				if(line.length() > 0)		   									// Ignore empty lines
					if(line.charAt(0) != '#'){ 									// Ignore comments
						Scanner scanner = new Scanner(line);
						scanner.useDelimiter("=");
						
						String optionName = scanner.next().trim();				// Get option name and value. If not valid, exit program!
						validateOptionName(line, optionName, nLine);
						String optionValue = scanner.next().trim(); 
						validateOptionValue(optionName, optionValue, nLine); 
					}
			}
			in.close();
		}  
		catch(Exception e){														// Catch open file  error.
			System.err.println("Error: " + e.getMessage());
		}
	}	
	
	private static void validateOptionName(String line, String option, int nLine){
		if(option.length() >= line.length())
			exitError("Line " + nLine + " --> Missing equal sign '='");
		if(!validateName(option))
			exitError("Line " + nLine + " --> INVALID OPTION NAME '" + option + "'");
	}
	
	private static boolean validateName(String name){
		for(int i = 0; i < optionNames.length; i++)
			if(name.equals((String)optionNames[i]))
				return true;
		return false;
	}
	
	private static void validateOptionValue(String optionName, String optionValue, int nLine)	// NOTE: Usage of 'switch' with 'String' only legal for Java SE 7 or later!
	throws NumberFormatException{
		if(optionName.equals("p3Runs")){
			P3.p3Runs = Integer.parseInt(optionValue);
			if(P3.p3Runs < 0)
				exitError("Line " + nLine + " --> Number of runs to perform must be a POSITIVE integer.");
			return; 																			// Option validated!!
		}
		if(optionName.equals("hillClimberType")){
			hillClimberType = Integer.parseInt(optionValue);
			if(hillClimberType != 1)
				exitError("Line " + nLine + " --> The HillClimber Type option is not well defined.");
			return;																				// Option validated!!
		}		
		if(optionName.equals("problemType")){
			problemType = Integer.parseInt(optionValue);  										// NOTE: Add a new option for each new problem
			if(!(problemType >= 0 && problemType <= 13)) 
				exitError("Line " + nLine + " --> INVALID Problem Type. Please check 'P3Parameters.txt' for the complete list of valid problems.");
			return; 																			 // Option validated!!
		}
		if(optionName.equals("stringSize")){													 // NOTE: Link string size to problem type!
			Problem.n = Integer.parseInt(optionValue);
			if(Problem.n <= 0)
				exitError("Line " + nLine + " --> Individual string size must be a POSITIVE integer.");
			return; 																			 // Option validated!!
		}
		if(optionName.equals("kayBipolar")){													// NOTE: This is only for the K-DECEPTIVE BIPOLAR problems.
			kayBipolar = Integer.parseInt(optionValue);
			if(kayBipolar <= 1 || kayBipolar%2 != 0)
				exitError("Line " + nLine + " --> Size of Bipolar Trap must be an even positive integer.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("trapSize")){														// NOTE: This is only for TRAP-K and STEPTRAP-K,S problems.
			trapSize = Integer.parseInt(optionValue);
			if(trapSize <= 1)
				exitError("Line " + nLine + " --> Order of TRAP-K must be an integer greater than one.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("stepSize")){														// NOTE: This is only for STEPTRAP-K,S problems.
			stepSize = Integer.parseInt(optionValue);
			if(stepSize <= 1 || stepSize >= trapSize)
				exitError("Line " + nLine + " --> Size of a Step Trap must be an integer between one and trapSize.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("ISGDataFile")){													// Full path to Ising Spin Glasses data file.
			ISGDataFile = optionValue;			 												// No need to validate: 'optionValue' simply contains the name of the ISG data file.
			return;
		}
		if(optionName.equals("bitsPerFloat")){													// NOTE: This is only for DISCRETIZED-RASTRIGIN problems.		
			bitsPerFloat = Integer.parseInt(optionValue);
			if(problemType == 11 && (bitsPerFloat <= 1 || bitsPerFloat >= Problem.n))
				exitError("Line " + nLine + " --> Number of bits per float in DISCRETIZED-RASTRIGIN must be an integer between one and stringSize.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("clauseRatio")){													// NOTE: this is only for MAXSAT problems.
			clauseRatio = Float.parseFloat(optionValue);
			if(clauseRatio <= 0)
				exitError("Line " + nLine + " --> The clause ratio in MAXSAT must be a positive real number.");
			return; // Option validated!!  
		}
		if(optionName.equals("maxIterations")){
			P3Stopper.maxIterations = Integer.parseInt(optionValue);
			if(P3Stopper.maxIterations <= 0 && P3Stopper.maxIterations != -1)
				exitError("Line " + nLine + " --> Maximal number of portfolio iterations must be either '-1' or a POSITIVE integer.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("maxFitnessCalls")){
			P3Stopper.maxFitnessCalls = Long.parseLong(optionValue);
			if(P3Stopper.maxFitnessCalls <= 0 && P3Stopper.maxFitnessCalls != -1)
				exitError("Line " + nLine + " --> Maximal number of fitness calls must be either '-1' or a POSITIVE integer.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("maxTimeAllowed")){
			P3Stopper.maxTimeAllowed = Long.parseLong(optionValue);
			if(P3Stopper.maxTimeAllowed <= 0 && P3Stopper.maxTimeAllowed != -1)
				exitError("Line " + nLine + " --> 'P3Stopper.maxTime' must be a positive number.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("goodEnough")){
			P3Stopper.goodEnough = Float.parseFloat(optionValue);
			if((P3Stopper.goodEnough <= 0 && P3Stopper.goodEnough != -1) || P3Stopper.goodEnough > 1)
				exitError("Line " + nLine + " --> 'P3Stopper.goodEnough' must be a real number in range (0.0; 1.0] or -1 (to ignore).");
			return;  																			// Option validated!!
		}
		if(optionName.equals("foundOnes")){
			P3Stopper.foundOnes = Integer.parseInt(optionValue);
			if(P3Stopper.foundOnes != -1 && P3Stopper.foundOnes != 0 && P3Stopper.foundOnes != 1)
				exitError("Line " + nLine + " --> 	INVALID option for stopWhenFoundOnes.");
			return; 																			// Option validated!!
		}
		if(optionName.equals("verbose")){
			P3Press.verbose = Boolean.parseBoolean(optionValue);
			if(P3Press.verbose != false && P3Press.verbose != true)
				exitError("Line " + nLine + " --> Verbose option must be either 'false' or 'true'.");
			return;  																			// Option validated!!
		}
		if(true)
			exitError("Line" + nLine +
					  " --> If you are reading this message something is FUNDAMENTALLY WRONG with 'validateOptionValue(String, String, int)'.\n" + 
					  "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					  "Sorry for the inconvenience!");
	} 																							// END: validateOptionValue(...)
	
	
	public static void initializePress(){
		P3Press.hillClimberType = P3Parameter.hillClimberType;							
		P3Press.kayBipolar      = P3Parameter.kayBipolar;							
		P3Press.trapSize        = P3Parameter.trapSize;
		P3Press.stepSize        = P3Parameter.stepSize;
		P3Press.ISGDataFile     = P3Parameter.ISGDataFile;
		P3Press.bitsPerFloat    = P3Parameter.bitsPerFloat;
		P3Press.clauseRatio     = P3Parameter.clauseRatio;																	
	}
	
	
	public static Problem initializeProblem(){													// Design Pattern Strategy
		switch(problemType){																	// NOTE: When implementing a new problem one must add a corresponding new case.
			// UNITATION Problems
			case  1: return new Problem(new              OneMax());
			case  2: return new Problem(new         LeadingOnes());
			case  3: return new Problem(new      ThreeDeceptive());
			case  4: return new Problem(new   KDeceptiveBipolar(kayBipolar));
			case  5: return new Problem(new               TrapK(trapSize));
			case  6: return new Problem(new           StepTrapK(trapSize, stepSize));
			case  7: return new Problem(new                HIFF());
			case  8: return new Problem(new HierarchicalTrapOne());
			case  9: return new Problem(new HierarchicalTrapTwo());
			case 10: return new Problem(new    IsingSpinGlasses(ISGDataFile));
			case 11: return new Problem(new           Rastrigin(bitsPerFloat));
			case 12: return new Problem(new              MaxSat(clauseRatio));
			//case 13: return new Problem(new   NearestNeighborNK(NNKDataFile, k),     stringSize);
			default: exitError("If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'problemType' value.\n" + 
					   		"You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					   		"Sorry for the inconvenience!");
			 		 // This line is never executed!
					 return new Problem(new OneMax());
		}
	}
	
	public static HillClimber initializeHillClimber(){
		switch(hillClimberType){
		case 1: return new FirstImprovementHillClimber();
		default: exitError("If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'hillClimberType' value.\n" + 
		   		"You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
		   		"Sorry for the inconvenience!");
				// This line is never executed!
				return new FirstImprovementHillClimber();
		}
	}
	
	public static Pyramid initializePyramid(){			// NOTE: Try possible implementation with parameters to control the pyramid's behaviour.
		return new Pyramid();			
	}
	
	public static void exitError(String message){		// Input error found!! Exit program!
		System.err.println(new Error(message));
		System.exit(1);
	}
	
}// End of class Parameter





