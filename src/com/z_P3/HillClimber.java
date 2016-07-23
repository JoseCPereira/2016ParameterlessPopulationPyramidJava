package com.z_P3;

import java.util.HashSet;

import com.z_P3.Individual;
import com.z_P3.Problem;


abstract class HillClimber{
	
	protected int[] indices;
	
	public HillClimber(){
		this.indices = new int[Problem.n];
		for(int i = 0; i < Problem.n; i++)
			this.indices[i] = i;
	}
	
	public abstract float improve(Individual solution, float fitness);
}


class FirstImprovementHillClimber extends HillClimber{
	
	public FirstImprovementHillClimber(){super();}
	
	public float improve(Individual solution, float fitness){
		P3Press.printInicialHillInfo(solution, fitness);
		
		boolean improvement;
		HashSet<Integer> tried = new HashSet<Integer>();
		do{
			improvement = false;
			Util.shuffle(indices);
			for(int index: indices){
				if(tried.contains(index))
					continue;
				solution.flipBit(index);
				float newFitness = solution.computeFitness();
				if(fitness < newFitness){
					fitness = newFitness;
					improvement = true;
					tried.clear();
					// TODO: Implement several levels of verbosity.
					//P3Press.printCurrentHillInfo(solution, fitness);
				}
				else
					solution.flipBit(index);
				tried.add(index);
			}
		}while(improvement);
		
		P3Press.printFinalHillInfo(solution, fitness);
		return fitness;
	}
}





