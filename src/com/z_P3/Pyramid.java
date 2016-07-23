package com.z_P3;

import java.util.ArrayList;
import java.util.HashMap;


///////////////////////////////////////////////////////////////////////
//
// This eponymous class represents the pyramid itself.
// It is responsible for adding solutions to each level, while
// improving those solutions and generating new levels when necessary.
//
//////////////////////////////////////////////////////////////////////

public class Pyramid{
	
	private ArrayList<Level>              levels;
	private HashMap<Individual, Float> evaluated;
	
	public Pyramid(){
		levels    = new ArrayList<Level>();
		evaluated = new HashMap<Individual, Float>(); 
		levels.add(new Level(0));
	}							
	
	public ArrayList<Level> getLevels(){return levels;}
	public Level        getLevel(int i){return levels.get(i);}
	public int	       getPyramidSize(){return levels.size();}
	
	
	public int climb(Individual solution, float fitness){								// The 'climb' function is responsible for returning the state of its own success finding the optimum solution. NOTE: This is for test only!
		this.tryAdd(solution, fitness, 0);												// If not yet in the pyramid, add solution to level 0 of the pyramid.
		for(int l = 0; l < levels.size(); l++){			
			float prevFitness = fitness;
			fitness           = levels.get(l).improve(solution, fitness);				// Use Linkage Tree to try improve the current solution.	
			if(prevFitness < fitness){													// If there was an improvement, try add the new solution to the next level of the pyramid.						
				this.tryAdd(solution, fitness, l+1);									// NOTE: tryAdd() is responsible for printing any new info concerning each level.
				if(P3Stopper.foundOptimum(solution, fitness))							// Optimum found, no need to go on. NOTE: This is for test only!
					return 1;
			}
			else																		// If there was no improvement, keep old solution and proceed to next level.
				P3Press.printNoSolutionImprovement(l+1);
		}
		P3Press.printFinalPyramidInfo();
		return 0;																		// Optimum NOT found, just go on with the next iteration. NOTE: This is for test only!
	}	
	
	
	private void tryAdd(Individual solution, float fitness, int nLevel){
		if(!evaluated.containsKey(solution)){
			Individual solutionCopy = solution.copyIndividual();						// NOTE: A copy of current solution is stored in 'level' and 'evaluated'.
			evaluated.put(solutionCopy, fitness);										//		 The original 'solution' is used for further improvements.	
			if(levels.size() == nLevel)													// We're at the top of the pyramid.
				levels.add(new Level(nLevel));											// Add a new empty level to the pyramid.
			Level level = levels.get(nLevel);
			level.add(solutionCopy, fitness);											// NOTE: add() is responsible for updating all new data in this level: 
			level.rebuildTree(solutionCopy, fitness);									//		 unifrequencies, occurrences, worstFit, bestFit, avgFit
			P3Press.printCurrentLevelInfo(solutionCopy, fitness, level);
		}
		else
			P3Press.printSolutionAlreadyEvaluated(nLevel);
	}
	
}




