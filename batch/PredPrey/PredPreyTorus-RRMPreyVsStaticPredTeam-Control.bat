cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:torus trials:20 maxGens:1000 mu:100 io:true netio:true mating:false fs:false task:edu.utexas.cs.nn.tasks.gridTorus.TorusEvolvedPreyVsStaticPredatorsTask log:RRMPreyVsStaticPredTeam-Control saveTo:Control allowDoNothingActionForPredators:true torusPreys:2 torusPredators:3 staticPredatorController:edu.utexas.cs.nn.gridTorus.controllers.AggressivePredatorController preyRRM:true preyMaximizeTotalTime:false torusSenseTeammates:true