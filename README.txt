Java implementation of the Parameterless Population Pyramid. Command line version.

The ‘2016ParameterlessPopulationPyramidJava’ (JavaP3) implements the Parameterless Population Pyramid (P3) first created by Brian W. Goldman[1, 2]. 
This code is a close translation to Java from the C++ implementation, FastEfficientP3, made available by Goldman at https://github.com/brianwgoldman/FastEfficientP3.

To compile you will need a fairly up to date Java compiler. We used Java SE 1.7 for our compilation.
All the source code is available at the ‘src’ directory.

__________________________________________________________________________________________________________________________________________________________________________

To compile do:

    1. Open Terminal (or any other command line shell environment).

    2. Go to directory      2016ParameterlessPopulationPyramidJava/bin

    3. Compile with command javac ../src/com/z_P3/*.java -d ./ 

__________________________________________________________________________________________________________________________________________________________________________

To run P3 do:

    1. Open Terminal (or any other command line shell environment).

    2. Go to directory     2016ParameterlessPopulationPyramidJava/bin

    3. Run P3 with command java com/z_P3/P3 ./P3Parameters.txt

The argument ./P3Parameters.txt is the name of the input file that provides a default test configuration for P3.
To use P3 with different configurations and solve other problems you can change this file or create your own input file (just remember to change the corresponding argument in the java command).

Some of the options provided by Goldman in the FastEfficientP3 are not available in this Java version, namely:

    1. Pyramid is the only ‘optimizer’.
  
    2. First Improvement is the only ‘hill_climber’.

    3. Smallest First is the only ‘cluster_ordering’.
  
    4. Crossover always uses 1-sized clusters.

    5. Crossover always searches for donors until a change is made.

    6. Crossover never uses clusters with zero distance.

    7. Pyramid only adds solutions to higher levels if they were improved.


The Ising Spin Glass problem files are the ones provided by Goldman in https://github.com/brianwgoldman/FastEfficientP3.

__________________________________________________________________________________________________________________________________________________________________________

Each execution of JavaP3 (one or more runs) generates two output files:

    1. P3-TEST_nRuns … .txt   ==> Contains detailed information about the solutions evaluated in each iteration of P3. Provides a good sense of how the search evolved in each run of P3.

    2. P3-STATS_nRuns … .txt  ==> Contains the main statistical information generated in each run of P3. This data can be used to analyze the P3 performance.

__________________________________________________________________________________________________________________________________________________________________________

References

[1] B. W. Goldman and W. F. Punch. Parameter-less population pyramid. 
    In Proceedings of the 2014 Conference on Genetic and Evolutionary Computation, GECCO ’14, pages 785–792, New York, NY, USA, 2014. ACM. 
 

[2] B. W. Goldman and W. F. Punch. 2015. Fast and efficient black box optimization using the parameter-less population pyramid. 
    Evol. Comput. 23, 3 (September 2015), 451-479.



