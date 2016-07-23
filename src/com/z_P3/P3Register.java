package com.z_P3;



public class P3Register{

	private int   totalIterations;
	private long  totalFitnessCalls;				// P3Engine.problem.computeFitness() is responsible for updating totalFitnessCalls.
	private long  totalRunTime;
	
	private int   worstIndividualLevel;
	private float worstIndividualFitness;

	private int   bestIndividualLevel;
	private float bestIndividualFitness;
	
	private int   bestAverageLevel;
	private float bestAverageFitness;
	
	private int   totalNIndividuals;
	private float globalSumFitness;
	private float globalAverageFitness;
	
	private int   success;							// P3Engine.p3Register.updateIterationData(...) is responsible for updating success.
													// Check main loop @P3Engine.RUN(...).
	public P3Register(){
		this.totalIterations        = 0;
		this.totalFitnessCalls      = 0;
		this.totalRunTime           = 0;
		this.worstIndividualFitness = Float.POSITIVE_INFINITY;
		this.bestIndividualFitness  = Float.NEGATIVE_INFINITY;
		this.bestAverageFitness     = Float.NEGATIVE_INFINITY;
		this.totalNIndividuals      = 0;      
		this.globalSumFitness       = 0;
		this.success                = 0;
	}																	 
																				 
	public int          getTotalIterations(){return this.totalIterations;}	
	public long       getTotalFitnessCalls(){return this.totalFitnessCalls;}
	public long            getTotalRunTime(){return this.totalRunTime;}

	public int     getWorstIndividualLevel(){return this.worstIndividualLevel;}
	public float getWorstIndividualFitness(){return this. worstIndividualFitness;}
	
	public int      getBestIndividualLevel(){return this.bestIndividualLevel;}
	public float  getBestIndividualFitness(){return this.bestIndividualFitness;}
	
	public int         getBestAverageLevel(){return this.bestAverageLevel;} 
	public float     getBestAverageFitness(){return this.bestAverageFitness;}
	
	public int        getTotalNIndividuals(){return this.totalNIndividuals;}
	public float   getGlobalAverageFitness(){return this.globalAverageFitness;}
	
	public boolean              getSuccess(){return (this.success == 1)? true : false;}
	
	public void   	   incrementIterations(){this.totalIterations++;}
	public void incrementTotalFitnessCalls(){this.totalFitnessCalls++;}	
	
	public void updateWorstData(int levelOrder, float worstFit){
		if(worstFit < this.worstIndividualFitness){
			this.worstIndividualLevel   = levelOrder;
			this.worstIndividualFitness = worstFit;
		}
	}
	
	public void updateBestData(int levelOrder, float bestFit){
		if(bestFit > this.bestIndividualFitness){
			this.bestIndividualLevel    = levelOrder;
			this.bestIndividualFitness  = bestFit;
		}
	}
	
	public void updateAverageData(int levelOrder, float fitness, float avgFit){
		if(avgFit > this.bestAverageFitness){
			this.bestAverageLevel      = levelOrder;
			this.bestAverageFitness    = avgFit;
		}
		this.totalNIndividuals++;
		this.globalSumFitness    += fitness;
		this.globalAverageFitness = globalSumFitness / ((float)totalNIndividuals);
	}
		
	public void updateIterationData(long iterationTime, int success){
		this.totalIterations++;
		this.totalRunTime   += iterationTime;
		this.success         = success;
	}
		
}









