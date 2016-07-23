package com.z_P3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

///////////////////////////////////////////////////////////////////
//
// DESIGN PATTERN STRATEGY
// Each particular problem must implement interface IProblem.
// To generate an instance of problem IsingSpin (for example) use
//
// Problem problem = new Problem(new HierarchicalTrapOne(), 27);
//
// where the second argument (27 in this case) is the
// string size of an individual.
//
///////////////////////////////////////////////////////////////////

interface IProblem{
	public float computeFitness(Individual individual);
	public String toString();
}
 
public class Problem{
	private IProblem      problem;
	public  static int    n;												// NOTE: The string size is initialized by 'P3Parameter.java'. No default, it's problem dependent.
	public  static String problemName;
	
	public Problem(IProblem problem){
		this.problem         = problem;
		Problem.problemName  = problem.toString();
	}
	
	public float computeFitness(Individual individual){
		P3Engine.p3Register.incrementTotalFitnessCalls();
		return (float)(this.problem.computeFitness(individual));
	}
	
	
	public static boolean validateXSize(Individual individual){
		return (individual.getIndividual().length == n) ? true : false;
	}
	
	public String toString(){
		return "  Solving: " + problemName +"\n        n: " + n;
	}
}



//################## UNITATION PROBLEMS ## UNITATION PROBLEMS ## UNITATION PROBLEMS ##################

//###### 1 --> ONEMAX PROBLEM ######
//
// best Individual(s): 111 ... 11
//
class OneMax implements IProblem{	
	public float computeFitness(Individual individual){
		int totalFit = 0;
		for(int i = 0; i < Problem.n; i++)
			if(individual.getAllele(i) == '1')
				totalFit++;
		return Util.floatRound((float)totalFit / ((float)Problem.n));
	}

	public String toString(){
		return "ONEMAX";
	}
}


// ###### 2 --> LEADING ONES PROBLEM ######
//
// best Individual(s): 111 ... 11
//
class LeadingOnes implements IProblem{
	public float computeFitness(Individual individual){
		for(int i = 0; i < Problem.n; i++) 
			if(individual.getAllele(i) == '0')			
				return Util.floatRound((float)i / ((float)Problem.n));		// Return proportion of 1s up to first '0'
		return (float)1;													// There are no 0s, return maximum fitness 1.
	}

	
	public String toString(){
		return "LEADING-ONES";
	}
}


// ###### 3 --> 3-DECEPTIVE PROBLEM ######
//
// best Individual(s): 111 ... 11
//
class ThreeDeceptive implements IProblem{
	float[] scores = {0.9f, 0.8f, 0, 1};
	
	public float computeFitness(Individual individual){
		int three;
		float totalFit = 0;
		for(int i = 0; i < Problem.n; i +=3){ 
			three = 0;
			for(int j = i; j < i+3; j++)
				three += Integer.parseInt(String.valueOf(individual.getAllele(j)));
			totalFit += scores[three];
		}
		return Util.floatRound((float)(3*totalFit) / ((float)Problem.n));
	}

	
	public String toString(){
		return "3-DECEPTIVE";
	}
}


// ###### 4 --> K-DECEPTIVE BIPOLAR PROBLEM ######
//
// best Individual(s): 000 ... 00  or
//                     111 ... 11  for each trap.  					   
//

class KDeceptiveBipolar implements IProblem{
	private int kay;
	
	public KDeceptiveBipolar(int kay){this.kay = kay;}
	
	public float computeFitness(Individual individual){
		int partialFit;
		float totalFit = 0;
		int      halfK = kay/2;													// NOTE: 'kay' must be even!
		for(int i = 0; i < Problem.n; i += kay){
			partialFit = 0;
			for(int j = i; j < i+kay; j++)
				partialFit += Integer.parseInt(String.valueOf(individual.getAllele(j)));
			partialFit = Math.abs(partialFit - halfK);
			if      (partialFit == 0)     totalFit += 0.9;
			else if (partialFit == 1)     totalFit += 0.8;
			else if (partialFit == halfK) totalFit += 1;
		}
		return Util.floatRound((float)(kay*totalFit) / ((float)Problem.n));
	}

	
	public String toString(){
		return kay + "-DECEPTIVEBIPOLAR";
	}
}


//###### 5 --> TRAP-K PROBLEM ######
//
// best Individual(s): 111 ... 11
//

class TrapK implements IProblem{
	private int kay;
	
	public TrapK(int kay){this.kay = kay;}

	public float computeFitness(Individual individual){
		int k;
		float totalFit = 0;
		for(int i = 0; i < Problem.n;){ //Empty increment
			k = Integer.parseInt(String.valueOf(individual.getAllele(i++)));
			for(int j = 1; j < kay; j++)
				k += Integer.parseInt(String.valueOf(individual.getAllele(i++)));
			if(k < kay)
				totalFit += kay-1 - k;
			else
				totalFit += kay;
		}
		return Util.floatRound((float)totalFit / ((float)Problem.n));
	}

	
	public String toString(){
		return "TRAP-" + kay;
	}
}


// ###### 6 --> STEPTRAP-K PROBLEM ######
//
// best Individual(s): 111 ... 11
//
// 
//
class StepTrapK implements IProblem{
	private int trapSize;
	private int stepSize;
	private int offset;
	
	public StepTrapK(int trapSize, int stepSize){
		this.trapSize = trapSize;
		this.stepSize = stepSize;
		this.offset   = (trapSize - stepSize) % stepSize;
	}
	
	public float computeFitness(Individual individual){
		int partialFit;
		float totalFit = 0;
		int    trapMax = (offset + trapSize) / stepSize;
		for(int i = 0; i < Problem.n; i += trapSize){
			partialFit = 0;
			for(int j = i; j < i+trapSize; j++)
				partialFit += Integer.parseInt(String.valueOf(individual.getAllele(j)));
			if(partialFit < trapSize)
				partialFit = trapSize - partialFit - 1;
			//totalFit += ((float)(offset + partialFit)) / ((float)stepSize);
			totalFit += (offset + partialFit) / (stepSize);
		}
		return Util.floatRound((float)(trapSize*totalFit) / ((float)(Problem.n*trapMax)));
	}

	
	public String toString(){
		return "STEPTRAP-k" + trapSize + "s" + stepSize;
	}
}


//################## HIERARCHICAL PROBLEMS HIERARCHICAL PROBLEMS HIERARCHICAL PROBLEMS ##################

//###### 7 --> HIFF PROBLEM ######
//
// Check Pelikan, Martin, "Hierarchical Bayesian Optimization Algorithm", pp. 88 - 103
//
// best Individual(s): 000 ... 00  and
//                     111 ... 11
//
// stringSize = power-of-2   |   optimumValue = stringSize * Log(2, stringSize)
//                  2        |           2 * 1 = 2
//                  4        |           4 * 2 = 8
//                  8        |           8 * 3 = 24
//                 16        |          16 * 4 = 64
//                 32        |          32 * 5 = 160
//                 64        |          64 * 6 = 384
//
//////////////////////////////////////////////////////////////////////////////////////

class HIFF implements IProblem{
	public float computeFitness(Individual individual){
		float     totalFit = 0;
		int            two = 99;													// Bogus value. Just to initialize.
		char[] levelString = individual.copyIndividualLoci(); 						// NOTE: Consider implementing Individual class with String instead of char[]
		int      levelSize = Problem.n;
		int        nLevels = 0;
		float     levelFit;
		
		while(levelSize > 1){
			nLevels++;
			levelFit = 0;															// Fitness contribution of each level.
			for(int i = 0; i < levelSize;){	 										// Empty increment
				two  = Integer.parseInt(String.valueOf(levelString[i++]));
				two += Integer.parseInt(String.valueOf(levelString[i++]));
				if(two == 2){							 
					levelFit += 1;												
					levelString[i/2-1] = '1';										// '11' -> 1. NOTE: rewriting over the leftmost part of levelString. 
				}
				else if(two == 0){
					levelFit += 1;												
					levelString[i/2-1] = '0';										// '00' -> 0. NOTE: rewriting over the leftmost part of levelString.
				}
				else{
					// levelFit += 0												// NULL symbol present.
					levelString[i/2-1] = '8';										// anything else -> 8 (NULL symbol). NOTE: rewriting over the leftmost part of levelString.
				}
			}
			levelSize /= 2;							 								// Next levelSize. Each 2-string is collapsed in to a single symbol.
			totalFit  += levelFit*Problem.n/levelSize; 								// Each level contribution is multiplied by the factor: 2^level = stringSize/(next levelSize). 
		}
		
		return Util.floatRound((float)totalFit / ((float)(Problem.n*nLevels)));
	}

	
	public String toString(){
		return "HIFF";
	}
}


//###### 8 --> HIERARCHICAL TRAP ONE PROBLEM ######
//
// Check Pelikan, Martin, "Hierarchical Bayesian Optimization Algorithm", pp. 88 - 103
//
// best Individual(s): 111 ... 11
//
// stringSize = power-of-3   |   optimumValue = stringSize * Log(3, stringSize)
//                  3        |           3 * 1 = 3
//                  9        |           9 * 2 = 18
//                 27        |          27 * 3 = 81
//                 81        |          81 * 4 = 324
//                243        |         243 * 5 = 1215
//                729        |         729 * 6 = 4374
//
// NOTES: k = 3; 
//		  All lower levels => flow = fhigh = 1;
//		    	 Top level => flow = .9; fhigh = 1;
//		  Consider implementation with general values of k, flow, fhigh.
//
//
// TODO: Something WRONG here!!
//
//
//////////////////////////////////////////////////////////////////////////////////////

class HierarchicalTrapOne implements IProblem{
	
	public float computeFitness(Individual individual){
		float     totalFit = 0;
		int          three = 99;							  						// Bogus value. Just to initialize.
		char[] levelString = individual.copyIndividualLoci(); 						// NOTE: Consider implementing Individual class with String instead of char[]
		int      levelSize = Problem.n;	
		int        nLevels = 0;
		float     levelFit;
		
		float     flow = 1,															// Parameters for trap at all levels except the top one.
			     fhigh = 1;
		float  topFlow = (float)0.9,												// Parameters for topTrap.
			  topFhigh = 1;
		
		while(levelSize > 3){														// All levels except the top one.
			nLevels++;
			levelFit = 0;															// Fitness contribution of each level.
			for(int i = 0; i < levelSize;){	 										// Empty increment
				three  = Integer.parseInt(String.valueOf(levelString[i++]));
				three += Integer.parseInt(String.valueOf(levelString[i++]));
				three += Integer.parseInt(String.valueOf(levelString[i++]));
				if(three == 3){							 
					levelFit += fhigh;												// fhigh = 1.
					levelString[i/3-1] = '1';										// '111' -> 1. NOTE: rewriting over the leftmost part of levelString. 
				}
				else if(three == 0){
					levelFit += flow;												// flow = 1.
					levelString[i/3-1] = '0';										// '000' -> 0. NOTE: rewriting over the leftmost part of levelString.
				}
				else if(three < 3){
					levelFit += flow - ((float)three)*flow/((float)2);				// flow - u*flow/(k-1).
					levelString[i/3-1] = '8';										// anything else -> 8 (NULL symbol). NOTE: rewriting over the leftmost part of levelString.
				}
				else if(three > 3){
					// levelFit += 0												// NULL symbol present.
					levelString[i/3-1] = '8';										// anything else -> 8 (NULL symbol). NOTE: rewriting over the leftmost part of levelString.
				}
			}
			levelSize /= 3;							 								// Next levelSize. Each 3-string is collapsed in to a single symbol.
			totalFit += levelFit*Problem.n/levelSize; 								// Each level contribution is multiplied by the factor: 3^level = stringSize/(next levelSize). 
		}
		nLevels++;
		levelFit = 0;																// We are at the top level. No need for mapping. Use topTrap as the contribution function.
		for(int i = 0; i < 3;){	
			three  = Integer.parseInt(String.valueOf(levelString[i++]));
			three  += Integer.parseInt(String.valueOf(levelString[i++]));
			three  += Integer.parseInt(String.valueOf(levelString[i++]));
		}
		if(three == 3)						 
			levelFit += topFhigh;													// fhigh = 1.
		else if(three == 0)
			levelFit += topFlow;													// flow = .9.
		else if(three < 3)
			levelFit += topFlow - ((float)three)*topFlow/((float)2);				// flow - u*flow/(k-1).
																					// else NULL symbol present, do nothing.
		totalFit += levelFit*Problem.n;												// At the top level the contribution factor is
		return Util.floatRound((float)totalFit / ((float)(Problem.n*nLevels))); 		// 3^nLevels = stringSize.
	}	// END: computeFitness(...)

	
	public String toString(){
		return "HIERARCHICALTRAP-ONE";
	}
} // END: HierarchicalTrapOne


//###### 9 --> HIERARCHICAL TRAP TWO PROBLEM ######
//
//Check Pelikan, Martin, "Hierarchical Bayesian Optimization Algorithm", pp. 88 - 103
//
// best Individual(s): 111 ... 11
//
// stringSize = power-of-3   |   optimumValue = stringSize * Log(3, stringSize)
//                  3        |           3 * 1 = 3
//                  9        |           9 * 2 = 18
//                 27        |          27 * 3 = 81
//                 81        |          81 * 4 = 324
//                243        |         243 * 5 = 1215
//                729        |         729 * 6 = 4374
//
//NOTES: k = 3; 
//		  All lower levels => flow = 1 + 0.1/l; fhigh = 1;
//		    	 Top level => flow = .9; fhigh = 1;
//		  Consider implementation with general values of k, flow, fhigh.
//
////////////////////////////////////////////////////////////////////////////////////////

class HierarchicalTrapTwo implements IProblem{
	
	public float computeFitness(Individual individual){
		float     totalFit = 0;
		int          three = 99;									  				// Bogus value. Just to initialize.
		char[] levelString = individual.copyIndividualLoci(); 						// NOTE: Consider implementing the Individual class with String instead of char[]
		int      levelSize = Problem.n;
		int        nLevels = (int)(Math.log(levelSize)/Math.log(3));
		float levelFit;
		
		float     flow = (float)1 + (float)0.1/(float)nLevels,						// Parameters for trap at all levels except the top one.
			     fhigh = 1;
		float  topFlow = (float)0.9,												// Parameters for topTrap
			  topFhigh = 1;
		
		while(levelSize > 3){														// All levels except the top one.
			levelFit = 0;															// Fitness contribution of each level.
			for(int i = 0; i < levelSize;){	 										// Empty increment
				three  = Integer.parseInt(String.valueOf(levelString[i++]));
				three  += Integer.parseInt(String.valueOf(levelString[i++]));
				three  += Integer.parseInt(String.valueOf(levelString[i++]));
				if(three == 3){							 
					levelFit += fhigh;												// fhigh = 1.
					levelString[i/3-1] = '1';										// '111' -> 1. NOTE: rewriting over the leftmost part of levelString. 
				}
				else if(three == 0){
					levelFit += flow;												// flow = 1 + 0.1/l.
					levelString[i/3-1] = '0';										// '000' -> 0. NOTE: rewriting over the leftmost part of levelString.
				}
				else if(three < 3){
					levelFit += flow - ((float)three)*flow/((float)2);				// flow - u*flow/(k-1).
					levelString[i/3-1] = '8';										// anything else -> 8 (NULL symbol). NOTE: rewriting over the leftmost part of levelString.
				}
				else if(three > 3){
					// levelFit += 0												// NULL symbol present.
					levelString[i/3-1] = '8';										// anything else -> 8 (NULL symbol). NOTE: rewriting over the leftmost part of levelString.
				}
			}
			levelSize /= 3;							 								// Next levelSize. Each 3-string is collapsed in to a single symbol.
			totalFit  += levelFit*Problem.n/levelSize; 								// Each level contribution is multiplied by the factor: 3^level = stringSize/(next levelSize). 
		}		
		levelFit = 0;																// We are at the top level. No need for mapping. Use topTrap as the contribution function.
		for(int i = 0; i < 3;){	
			three  = Integer.parseInt(String.valueOf(levelString[i++]));
			three  += Integer.parseInt(String.valueOf(levelString[i++]));
			three  += Integer.parseInt(String.valueOf(levelString[i++]));
		}
		if(three == 3)						 
			levelFit += topFhigh;													// topFhigh = 1.
		else if(three == 0)
			levelFit += topFlow;													// topFlow = .9.
		else if(three < 3)
			levelFit += topFlow - ((float)three)*topFlow/((float)2);				// flow - u*flow/(k-1).
																					// else NULL symbol present, do nothing.
		totalFit += levelFit*Problem.n;												// At the top level the contribution factor is
		return Util.floatRound((float)totalFit / ((float)(Problem.n*nLevels))); 	// 3^nLevels = stringSize.
	}// END: computeFitness(...)

	
	public String toString(){
		return "HIERARCHICALTRAP-TWO";
	}
} // END: HierarchicalTrapTwo




//################## NON UNITATION PROBLEMS ## NON UNITATION PROBLEMS ## NON UNITATION PROBLEMS ##################

//###### 10 --> ISING SPIN GLASSES PROBLEM ######
//
// best Individual(s): CHECK ISG data files @IsingSpinGlassesDataFiles folder.
//
class IsingSpinGlasses implements IProblem{
	int            length;
	int         minEnergy;
	float            span;
	String solutionString;
	int[][]         spins;	
	int            nSpins;
	int[]       bitToSign = {-1, 1};
	
	
	public IsingSpinGlasses(String ISGDataFile){
		try{
			FileInputStream fstream = new FileInputStream(ISGDataFile);				// Open the file to be read			
			DataInputStream      in = new DataInputStream(fstream);					// Create an object of DataInputStream
			BufferedReader     buff = new BufferedReader(new InputStreamReader(in));
			Scanner         scanner = new Scanner(buff.readLine());					// NOTE: Do NOT change the line reading order!!
			scanner.useDelimiter(" ");
			this.minEnergy          = Integer.parseInt(scanner.next().trim());		// Line 1: minEnergy" "solutionString		
			this.solutionString     = scanner.next().trim();
			this.nSpins             = Integer.parseInt(buff.readLine());			// Line 2: total number of spins
			this.spins              = new int[nSpins][3];
			for(int i = 0; i < nSpins; i++){
				scanner = new Scanner(buff.readLine());								// Line i: i-th spin
				this.spins[i][0] = Integer.parseInt(scanner.next().trim());			// NOTE: Each spin consists of three integers:
				this.spins[i][1] = Integer.parseInt(scanner.next().trim());			//       index, index, and sign
				this.spins[i][2] = Integer.parseInt(scanner.next().trim());
			}
			in.close();
			this.span = nSpins - minEnergy;
//			Individual bestSolution = new Individual(solutionString);
//			float       bestFitness = computeFitness(bestSolution);
//			P3Press.printString("BEST SOLUTION: " + this.solutionString + "  [" + bestFitness + "]");
		}  
		catch(Exception e){															// Catch open file  error.
			System.err.println("Error: " + e.getMessage());
		}
	}
	
//	public boolean validateDataFile(){
//		Individual bestSolution = new Individual(solutionString.toCharArray()); 	// Validate data by evaluating best solution.
//		float           fitness = bestSolution.computeFitness();
//		System.out.println("    BEST: " + bestSolution + " [" + fitness + "]");
//		return fitness == 1;
//	}
	
	public float computeFitness(Individual individual){
		float energy = 0;
		for(int i = 0; i < nSpins; i++){
			int allele0 = Integer.parseInt(String.valueOf(individual.getAllele(spins[i][0])));
			int allele1 = Integer.parseInt(String.valueOf(individual.getAllele(spins[i][1])));
			energy -= (bitToSign[allele0] * bitToSign[allele1] * spins[i][2]);
		}
			
		return Util.floatRound((float)1 - ((float)(energy - minEnergy)) / ((float)span));
	}																				// Return standardized fitness value.
	
	public String toString(){
		return "ISING-SPIN-GLASSES";
	}
}


//###### 11 --> DISCRETIZED RASTRIGIN FUNCTION ######
//
// best Individual(s): 111 ... 11
//
class Rastrigin implements IProblem{
	float                       min = -5.12f;
	float                       max =  5.12f;
	int                bitsPerFloat;													// Size of each binary substring to be evaluated by the discretized Rastringin function. Default = 10. Check "Fast and Efficient ... P3", Goldman, Punch.
	float[]                    keys;													// Store key values corresponding to the Gray code of each possible binary string with size 'bitsPerFloat'.
	int                       nKeys;													// NOTE: nKeys = 2^bitsPerFloat.
	float                     worst;													// Use this to standardize the final fitness value.
	HashMap<Float, Float> rastrigin;
	
	public Rastrigin(int bitsPerFloat){
		this.bitsPerFloat = bitsPerFloat;
		this.nKeys        = 1 << bitsPerFloat;											// NOTE: 1 << bitsPerFloat = 2^bitsPerFloat.
		this.keys         = new float[nKeys];
		worst             = 0;
		this.computeAllKeys();															// Initialize key values with Gray code for all possible binary strings with size 'bitsPerFloat'.
		this.computeAllValues();														// Initialize Rastrigin function values for all keys.
	}																					// This is also responsible for updating the value of 'worst'.

	
	public float computeFitness(Individual individual){
		float totalFit = 0;
		int    nBlocks = 0;																// Use this to standardize the final fitness value.
		for(int i = 0; i < Problem.n; i += bitsPerFloat){
			int index = 0;
			for(int j = i; j < i + bitsPerFloat; j++){
				int allele = (int)individual.getAllele(j) - '0';						// Convert (char) allele to corresponding (int). 
				index <<= 1;
				index  |= allele;
			}
			nBlocks++;
			float key = keys[index];
			totalFit += rastrigin.get(key);
		}
		totalFit /= (nBlocks * worst);
		return Util.floatRound((float)1 - totalFit);									// Return standardized fitness value.
	}
	
	
	private void computeAllKeys(){
		float span = max - min;
		int   gray;
		for(int i = 0; i < nKeys; i++){
			gray       = (i >> 1) ^ i;
			keys[gray] = i / ((float)nKeys) * span + min; 
		}
	}
	
	private void computeAllValues(){
		this.rastrigin = new HashMap<Float, Float>(); 
		for(int i = 0; i < nKeys; i++){
			float   key = keys[i];
			float value = 10 + key*key - 10 * (float)Math.cos(2 *Math.PI*key);
			if(worst < value)
				worst = value;
			rastrigin.put(key, value);	
		}
	}
	
	public String toString(){
		return "DISCRETIZED-RASTRIGIN";
	}
}



//###### 12 --> MAXSAT PROBLEM ######
//
// best Individual(s): CHECK maxSat.getTargetSolution().
//
// NOTES: 
//    - Each clause consists of 3 terms.
//    - Every term can be negated or not.
//    - The clause ratio is initialized by default to 4.27.
//
//////////////////////////////////////////////////////////////////////////

class MaxSat implements IProblem{
	private float         clauseRatio;			  										        // NOTE: 'clauseRatio' is initialized by P3Parameter. Default = 4.27. 
	private int              nClauses;															//       clauseRatio = nClauses/Problem.n. Check 'default.cfg'@Goldman.
	private int[][]           clauses;															// Store all variable indices for each term on each clause.     
	private char[][]            signs;															// Store all negation signs: '0' => !xi; '1' => xi
	private char[][]      signOptions = {{'0', '0', '1'}, {'0', '1', '0'}, {'1', '0', '0'},		// Some random(?) possible negation signs. Use this to generate random target solution.
										 {'1', '0', '0'}, {'0', '1', '1'}, {'1', '1', '1'}};	// Check "Evaluation.h"@Goldman.
	private Individual targetSolution;
	

	public MaxSat(float clauseRati){
		this.clauseRatio = clauseRati;  
		this.nClauses    = Math.round(clauseRatio * (float)Problem.n);
		this.clauses     = new int[nClauses][3];
		this.signs       = new char[nClauses][3];
		this.generateRandomTargetSolution();
	}
	
	private void generateRandomTargetSolution(){
		this.targetSolution = RandomSolutionGenerator.generate();								// Generate new targetSolution.
		int[] variables     = Util.iota(Problem.n, 0);											// List of all possible variables.		
		for(int clause = 0; clause < nClauses; clause++){										// Create all clauses.
			int selectSign = P3.random.nextInt(0, signOptions.length);							// Choose a random sign setting for the current clause.
			for(int term = 0; term < 3; term++){												// For each term in the current clause...
				int index = P3.random.nextInt(term, Problem.n);									// Choose a random variable for the current term.
				Util.swap(variables, term, index);												// Ensure the same variable is not chosen twice.
				int variable          = variables[term];
				clauses[clause][term] = variable;										
				signs[clause][term]   = (signOptions[selectSign][term] == targetSolution.getAllele(variable))? '1' : '0';
			}																					// Set the sign to agree/disagree with the targetSolution,
		}																						// based on the chosen sign option.
	}
	
	public int              getNClauses(){return this.nClauses;}								// NOTE: These 'get' functions are not visible for an instance
	public int[][]           getClauses(){return this.clauses;}									//		 of class Problem. They are only visible to particular
	public char[][]            getSigns(){return this.signs;}									//		 instances of class MaxSat.
	public Individual getTargetSolution(){return this.targetSolution;}
	
	public float computeFitness(Individual individual){
		int totalFit = 0;																		// Count how many clauses evaluate to true.
		for(int clause = 0; clause < nClauses; clause++)
			for(int term = 0; term < 3; term++){
				int variable = clauses[clause][term];
				if(individual.getAllele(variable) == signs[clause][term]){						// If this term evaluates to 'true' then the current clause
					totalFit++;																	// also evaluates to 'true'. Increment 'totalFit' and
					break;																		// go on to the next clause.
				}
			}
		return Util.floatRound((float)totalFit / ((float)nClauses));							// Return standardized fitness value.
	}

	
	public String toString(){
		return "MAXSAT";
	}
}










