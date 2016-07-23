package com.z_P3;



public class P3Stopper{
	
	public static int   maxIterations;
	public static long  maxFitnessCalls;
	public static long  maxTimeAllowed;
	public static float goodEnough;			// NOTE: goodEnough in (0.0 ; 1.0]
	public static int   foundOnes;
	
	

	public static int     getMaxIterations(){return maxIterations;}
	public static long  getMaxFitnessCalls(){return maxFitnessCalls;}
	public static long   getMaxTimeAllowed(){return maxTimeAllowed;}
	public static float      getGoodEnough(){return goodEnough;}
	public static int         getFoundOnes(){return foundOnes;}
	
	
	public static boolean criteria(){ 
		return ((maxIterations   == -1)? false : P3Engine.p3Register.getTotalIterations()       >= maxIterations)   ||
			   ((maxFitnessCalls == -1)? false : P3Engine.p3Register.getTotalFitnessCalls()     >= maxFitnessCalls) ||
			   ((maxTimeAllowed  == -1)? false : P3Engine.p3Register.getTotalRunTime()          >= maxTimeAllowed);			   
	}
	
	
	public static boolean foundOptimum(Individual solution, float fitness){
		return ((goodEnough == -1)? false : fitness >= goodEnough) ||
			   ((foundOnes  == -1)? false : solution.isOne());
			  
	}
	
}