package com.z_P3;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class P3Press{
	
	public static int          hillClimberType;							// HillClimber type. At the moment the only option is 1 --> First Improvement HC.
	public static int               kayBipolar;							// Size of each bipolar trap.       NOTE: kayBipolar must be even. This is only for the K-DECEPTIVE BIPOLAR problems.
	public static int                 trapSize;							// Size of each deceptive trap.     NOTE: This is only for TRAP-K and STEPTRAP-K,S problems.
	public static int                 stepSize;							// Size of each step trap.          NOTE: This is only for STEPTRAP-K,S problems.
	public static String           ISGDataFile;							// Full path to Ising Spin Glasses data file.
	public static int             bitsPerFloat;							// Number of bits per float.        NOTE: This is only for DISCRETIZED-RASTRIGIN problems.
	public static float            clauseRatio;							// Ratio between the number of clauses and problem size: clauseRatio = nClauses * stringSize.
																		//								    NOTE: this is only for MAXSAT problems.
	public static boolean	           verbose;							// Verbose mode prints all currentSolver info in each generation. Default = false.	
																		// NOTE: 'verbose' is initialized by PortParameter.validateOptionValue().
	
	private static String         testFileName;							// This file stores all the information that is also printed in the console during an entire run.
	private static FileWriter      fstreamTest;
	private static BufferedWriter  testFileOut;
	
	
	private static String         testFileNameStats;					// This file stores only the statistics necessary to generate graphics.
	private static BufferedWriter testFileOutStats;
	
	
	public static void initializePress(){		
		P3Parameter.initializePress();
		     testFileName = "P3-TEST_nRuns"  + P3.p3Runs + "_" + Problem.problemName + "_n" + Problem.n + ".txt";
		testFileNameStats = "P3-STATS_nRuns" + P3.p3Runs + "_" + Problem.problemName + "_n" + Problem.n + ".txt";
		try{
			fstreamTest 	 = new FileWriter(testFileName);			// 'true' => Append to file.
			testFileOut 	 = new BufferedWriter(fstreamTest);
			
			fstreamTest      = new FileWriter(testFileNameStats);		// 'true' => Append to file.
			testFileOutStats = new BufferedWriter(fstreamTest);
		}catch(Exception e){System.err.println("ERROR: " + e.getMessage());}
	}
	
	public static void printString(String str){							// NOTE: Use this method to print simultaneous in 
		System.out.println(str);										//	 	 the console and in the testFileOut.
		try{	
			testFileOut.write("\n" + str);
		}catch(Exception e){System.err.println("ERROR: " + e.getMessage());} 
	}
	
	private static void printStats(String str){							// NOTE: Use this method to print in the STATS file.										//	 	 the console and in the testFileOut.
		try{	
			testFileOutStats.write(str + "\n");
		}catch(Exception e){System.err.println("ERROR: " + e.getMessage());} 
	}
	
	
	public static void printInitialInfo(){
		String str  = "############ - P3 METHOD - ############ - P3 METHOD - ############ - P3 METHOD - ############\n#" +
					  "\n#" 														   +
					  "\n#   PARAMETERS:" 											   +
					  "\n#              p3Runs = " + P3.p3Runs               		   +
					  "\n#     hillClimberType = " + P3Press.hillClimberType   		   +
					  "\n#   ---------------------------------"               		   +
					  "\n#         problemType = " + Problem.problemName 		       +
					  "\n#          stringSize = " + Problem.n 					       +
					  "\n#          kayBipolar = " + P3Press.kayBipolar 		       +
					  "\n#            trapSize = " + P3Press.trapSize 			       +
					  "\n#            stepSize = " + P3Press.stepSize 			       +
					  "\n#         ISGDataFile = " + P3Press.ISGDataFile 		       +
					  "\n#        bitsPerFloat = " + P3Press.bitsPerFloat		       +
					  "\n#         clauseRatio = " + P3Press.clauseRatio		       +
					  "\n#   ---------------------------------"               		   +
					  "\n#       maxIterations = " + P3Stopper.getMaxIterations()      +
					  "\n#         maxFitCalls = " + P3Stopper.getMaxFitnessCalls()    +
					  "\n#      maxTimeAllowed = " + P3Stopper.getMaxTimeAllowed()     +
					  "\n#          goodEnough = " + P3Stopper.getGoodEnough()      +
					  "\n#           foundOnes = " + P3Stopper.getFoundOnes()      +
					  "\n#   ---------------------------------"               		   +
					  "\n#             verbose = " + P3Press.verbose                   +
					  "\n#" 														   +					   
					  "\n#\n############ - P3 METHOD - ############ - P3 METHOD - ############ - P3 METHOD - ############\n";
		printString(str);		
		
		printStats("StringSize  TotalIteration  TotalRunTime  TotalFitCalls  BestLevel  BestLevelSize  BestLevelAvgFitness  TotalNIndividuals  PyramidSize  GlobalAvgFitness  BestFitness");
	}
	
	
	public static void printRunInitialInfo(int r){
		if(verbose)
			printString("\n##### RUN " + (r+1) + "/" + P3.p3Runs + " #####" + 
						  "##### RUN " + (r+1) + "/" + P3.p3Runs + " #####" +
						  "##### RUN " + (r+1) + "/" + P3.p3Runs + " #####");
	}
	public static void printRunFinalInfo(int r){
		String str =  "\n##### RUN " + (r+1) + "/" + P3.p3Runs + " #####" + 
					  "##### RUN " + (r+1) + "/" + P3.p3Runs + " #####"   +
					  "##### RUN " + (r+1) + "/" + P3.p3Runs + " #####"   +
					  "\n# " + "               Total Run Time: " + P3Engine.p3Register.getTotalRunTime()      +
					  "\n# " + "     Current Total Iterations: " + P3Engine.p3Register.getTotalIterations()   +
				 	  "\n# " + "  Current Total Fitness Calls: " + P3Engine.p3Register.getTotalFitnessCalls() +
					  "\n# " + "                      Success: " + P3Engine.p3Register.getSuccess()           + 
					  "\n# " + "         Current Success Rate: " + P3.nSuccess + "/" + (r+1);
		printString(str);
	}
	
	
	public static void printRunFinalStats(){
		int bestLevelOrder = P3Engine.p3Register.getBestIndividualLevel();
		Level    bestLevel = P3Engine.pyramid.getLevel(bestLevelOrder);
		printStats(String.format("%6d %13d %15d %15d %8d %14d %17.2f %17d %16d %17.2f %15.2f",
						Problem.n,
						P3Engine.p3Register.getTotalIterations(),
						P3Engine.p3Register.getTotalRunTime(),
						P3Engine.p3Register.getTotalFitnessCalls(),
						bestLevelOrder,
						bestLevel.getLevelSize(),
						bestLevel.getAvgFit(),
						P3Engine.p3Register.getTotalNIndividuals(),
						P3Engine.pyramid.getPyramidSize(),
						P3Engine.p3Register.getGlobalAverageFitness(),
						P3Engine.p3Register.getBestIndividualFitness()
					));
	}
	
	
	public static void printInicialIterationInfo(){
		String str = "";
		if(verbose){
			str = "\n==============  STARTING ITERATION: " + (P3Engine.p3Register.getTotalIterations()+1) + "  ==============";
		    printString(str);
		}
	}
	public static void printFinalIterationInfo(long iterationTime){
		String str = "";
		if(verbose){
			str = "\n==============    END ITERATION: " + P3Engine.p3Register.getTotalIterations() + "     =============="  +
	           	  "\n      Current Iteration Time: " +                         iterationTime + " milliseconds." +
	              "\n Current Total  Running Time: " + P3Engine.p3Register.getTotalRunTime() + " milliseconds." +
	              "\n Current Total    Iterations: " + P3Engine.p3Register.getTotalIterations()                 +
	              "\n Current Total Fitness Calls: " + P3Engine.p3Register.getTotalFitnessCalls()               +
	              "\n============= ============ ============ =============\n";
			printString(str);
		}
	}
		
	public static void printInicialHillInfo(Individual solution, float fitness){
		String str = "";
		if(verbose){
			str = "\n=====   STARTING HILL CLIMBER   =====" +
				  "\n Initial Solution: "   + solution + " [" + fitness + "]" +
				  "\n";
			printString(str);
		}
	}
	public static void printCurrentHillInfo(Individual solution, float fitness){
		String str = "";
		if(verbose){
			str = "\n"                      +
				  "\n Current Solution: " + solution + " [" + fitness + "]" +
				  "\n";
			printString(str);
		}
	}
	public static void printFinalHillInfo(Individual solution, float fitness){
		String str = "";
		if(verbose){
			str = "\n=====   STOPPING HILL CLIMBER   =====" +
				  "\n    Best Solution: "   + solution + " [" + fitness + "]" +
				  "\n\n"                        +
				  "\n=====  STARTING PYRAMID METHOD  =====";					 
			printString(str);
		}
	}
	
	
	public static void printNoSolutionImprovement(int nLevel){
		String str = "";
		if(verbose){
			str = "\n NOT ADDING to level " + nLevel + ". Solution not improved." + "\n";
			printString(str);
		}
	}
	public static void printSolutionAlreadyEvaluated(int nLevel){
		String str = "";
		if(verbose){
			str = "\n NOT ADDING to level " + nLevel + ". Solution already evaluated." + "\n";
			printString(str);
		}
	}
	public static void printCurrentLevelInfo(Individual solution, float fitness, Level level){
		String str = "";
		if(verbose){
			str = " ADDING solution " + solution + " [" + fitness + "] to level " + level.getLevelOrder() + ":" +
		          "\n  Current Level Worst Individual Fitness: " + level.getWorstFit() +
		          "\n  Current Level  Best Individual Fitness: " + level.getBestFit()  +
		          "\n  Current Level          Average Fitness: " + level.getAvgFit();
			printString(str);
		}
	}
	public static void printFinalPyramidInfo(){
		String str = "";
		if(verbose){
			str = "\n=====  STOPPING PYRAMID METHOD  =====" +
				  "\n  Current Total Number of Individuals: " + P3Engine.p3Register.getTotalNIndividuals()     +
				  "\n       Current Global Average Fitness: " + P3Engine.p3Register.getGlobalAverageFitness()  +
			   	  "\n                 Current Pyramid Size: " + P3Engine.pyramid.getPyramidSize()              +
				  "\n---------------------------------------"   +
				  "\n   Current Best Level Average Fitness: " + P3Engine.p3Register.getBestAverageFitness()    + " at Level" + P3Engine.p3Register.getBestAverageLevel()    +     
				  "\n      Current Best Individual Fitness: " + P3Engine.p3Register.getBestIndividualFitness() + " at Level" + P3Engine.p3Register.getBestIndividualLevel()	+
				  "\n";
			printString(str);
		}
	}

	
	public static void printFinalInfo(){
		printString("\nSUCCESS RATE = " + P3.nSuccess + "/" + P3.p3Runs + "\n");
	}
	
	public static void closeTestFileOut(){		
		try{testFileOut.close();}
		catch(Exception e){System.err.println("ERROR: " + e.getMessage());}
		try{testFileOutStats.close();}
		catch(Exception e){System.err.println("ERROR: " + e.getMessage());}
	}		
	
}













