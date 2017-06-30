cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:microRTS trials:3 maxGens:500 mu:25 io:true netio:true mating:true task:edu.utexas.cs.nn.tasks.microrts.MicroRTSTask cleanOldNetworks:true fs:false log:MicroRTS-UCTvsUCTComplexConvolution saveTo:UCTvsUCTComplexConvolution microRTSAgent:micro.ai.mcts.uct.UCT microRTSOpponent:micro.ai.mcts.uct.UCT microRTSMapSequence:edu.utexas.cs.nn.tasks.microrts.iterativeevolution.DecceleratingMapSequence mRTSMobileUnits:false mRTSBuildings:false mRTSAll:false mRTSResources:true hyperNEAT:true genotype:edu.utexas.cs.nn.evolution.genotypes.HyperNEATCPPNGenotype allowMultipleFunctions:true ftype:1 netChangeActivationRate:0.3 extraHNLinks:true convolution:true mRTSAllSqrt3MobileUnits:true mRTSMyBuildingGradientMobileUnits:true watch:false stepByStep:false monitorSubstrates:false HNProcessWidth:3