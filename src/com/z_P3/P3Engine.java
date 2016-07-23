package com.z_P3;



public class P3Engine{
	public static Problem 	  problem;
	public static HillClimber hillClimber;
	public static Pyramid	  pyramid;
	
	public static P3Register  p3Register;
	public static int         nClusters;
	
	public P3Engine(String portParamFile){
		P3Parameter.initializeParameters(portParamFile); 						// Initialize and validate P3 parameters.		
		problem 	= P3Parameter.initializeProblem();							// Design Pattern Strategy.
		hillClimber = P3Parameter.initializeHillClimber();						// Design Pattern Strategy.			
		nClusters   = 2 * Problem.n - 1;										// Use this to initialize the LTGAEngine in each level.
	}
	
	public int RUN(int nRun){
		pyramid		= P3Parameter.initializePyramid();							// (Re)Initialize 'pyramid' for this run.
		p3Register  = new P3Register(); 										// (Re)Initialize 'p3Register' for this run.
		int success = 0;
		do{			
			long startIterationTime = P3Time.getAppTime();
			P3Press.printInicialIterationInfo();
			Individual solution  	= RandomSolutionGenerator.generate();		// 1. Generate random solution.
			float fitness           = solution.computeFitness();
			fitness                 = hillClimber.improve(solution, fitness);	// 2. Improve solution with Hill Climber.
			if(P3Stopper.foundOptimum(solution, fitness))						
				success = 1;													//  2.1. If optimum found, no need to go on. NOTE: This is for test only!
			else
				success = pyramid.climb(solution, fitness);						// 3. Improve solution with Pyramid method and update success accordingly.
			long iterationTime = P3Time.getAppTime() - startIterationTime;		// Time that takes to perform one iteration. NOTE: one iteration = hillClimber.improve() + pyramid.climb()
			p3Register.updateIterationData(iterationTime, success);				// Update iteration time and increment total number of iterations.
			P3Press.printFinalIterationInfo(iterationTime);
		}while(success == 0 && !P3Stopper.criteria());										
		
		P3Press.printRunFinalStats();
		return success;															 
	} 
}