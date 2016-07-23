package com.z_P3;

import java.util.ArrayList;


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// This class represents a level of the pyramid.
// It is responsible for adding solutions to itself and keep the pairwise occurrences up to date.
// It contains the inner class LTGAEngine, responsible for performing all linkage tree operations in this level.
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Level{
	
	private ArrayList<Individual>     individuals; 
	private int[][][]    	   		  occurrences;				// Symmetric matrix of pairwise occurrences: 00 - 01 - 10 - 11.  
	private LTGAEngine        		   ltgaEngine;				// Inner class responsible for performing all linkage tree operations in this level.
	private int                        levelOrder;			    // Each level knows its order in the pyramid.
	
	private int                 worstPos, bestPos;				// Position of current best and worst individuals.
	private float               worstFit, bestFit;				// Fitness of current best and worst individuals.
	private float                 sumFit,  avgFit;
		
	public Level(int levelOrder){
		individuals     = new ArrayList<Individual>();									
		occurrences     = new int[Problem.n][Problem.n][4];	
		ltgaEngine      = new LTGAEngine();
		
		this.levelOrder = levelOrder;
		this.worstFit   = Float.POSITIVE_INFINITY;
		this.bestFit    = Float.NEGATIVE_INFINITY;
		this.sumFit	    = 0;
	}
	
	public ArrayList<Individual> getIndividuals(){return individuals;}
	public Individual	     getIndividual(int i){return individuals.get(i);}
	public int                     getLevelSize(){return individuals.size();}
	
	public int[][][]              getOccurences(){return occurrences;}
	public int  getOccurence(int i, int j, int k){return occurrences[i][j][k];}
	
	public int					  getLevelOrder(){return levelOrder;}
	public int 				        getWorstPos(){return worstPos;}
	public int 				         getBestPos(){return bestPos;}
	public float 			        getWorstFit(){return worstFit;}
	public float 			         getBestFit(){return bestFit;}
	public float 			          getAvgFit(){return avgFit;}
	
	
	public void add(Individual solution, float fitness){
		individuals.add(solution);
		this.updateLevelData(solution, fitness);		
	}
	
	public void rebuildTree(Individual solution, float fitness){
		this.ltgaEngine.rebuildTree(solution, fitness, occurrences);
	}
	
	public float improve(Individual solution, float fitness){
		return this.ltgaEngine.improve(solution, fitness, individuals);
	}
	
	
	private void updateLevelData(Individual solution, float fitness){
		for(int i = 0; i < Problem.n-1; i++){
			char ci = solution.getAllele(i);							
			for(int j = i+1; j < Problem.n; j++){				// UPDATE: Pairwise occurrences given the new solution.
				char cj = solution.getAllele(j);
				int  xi = ci - '0';						
				int  xj = (cj - '0') * 2;						//        ij         ij         ij         ij
				int pair = xi + xj;								// NOTE: '00' => 0; '10' => 1; '01' => 2; '11' => 3
				this.occurrences[i][j][pair]++;					// NOTE: Matrix 'occurrences' is NOT symmetric but merely upper triangular. Check getDistance().
			}
		}
		
		int levelSize = individuals.size();
		if(fitness < worstFit){			
			this.worstFit = fitness;
			this.worstPos = levelSize - 1;
			P3Engine.p3Register.updateWorstData(levelOrder, worstFit);
		}
		if(fitness > bestFit){			
			this.bestFit  = fitness;
			this.bestPos  = levelSize - 1;
			P3Engine.p3Register.updateBestData(levelOrder, bestFit);
		}
		this.sumFit += fitness;
		this.avgFit  = sumFit/((float)levelSize);
		P3Engine.p3Register.updateAverageData(levelOrder, fitness, avgFit);
	}	
	
	
	public String toString(){
		String str ="";
		for(int i = 0; i < this.individuals.size(); i++){
			for(int j = 0; j < Problem.n; j++)
				str += individuals.get(i).getAllele(j);
			str += "\n";
			//str += "    fitness: " + fitness[i] + "\n";
		}
		return str;// + statistics.printStats();
	}

}// END: class Level







