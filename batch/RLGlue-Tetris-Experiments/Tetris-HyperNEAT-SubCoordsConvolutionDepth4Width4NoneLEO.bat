cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:tetris trials:3 maxGens:500 mu:50 io:true netio:true mating:true task:edu.utexas.cs.nn.tasks.rlglue.tetris.HyperNEATTetrisTask  rlGlueEnvironment:org.rlcommunity.environments.tetris.Tetris rlGlueExtractor:edu.utexas.cs.nn.tasks.rlglue.featureextractors.tetris.RawTetrisStateExtractor tetrisTimeSteps:true tetrisBlocksOnScreen:false  rlGlueAgent:edu.utexas.cs.nn.tasks.rlglue.tetris.TetrisAfterStateAgent splitRawTetrisInputs:true senseHolesDifferently:true hyperNEAT:true genotype:edu.utexas.cs.nn.evolution.genotypes.HyperNEATCPPNGenotype allowMultipleFunctions:true ftype:1 netChangeActivationRate:0.3 substrateMapping:edu.utexas.cs.nn.networks.hyperneat.BottomSubstrateMapping steps:500000 perLinkMutateRate:0.05 netLinkRate:0.4 netSpliceRate:0.2 crossoverRate:0.5 log:Tetris-SubCoordsConvolutionDepth4Width4ExtraLEO saveTo:SubCoordsConvolutionDepth4Width4ExtraLEO extraHNLinks:false HNProcessDepth:4 HNProcessWidth:4 convolution:true substrateLocationInputs:true substrateBiasLocationInputs:true leo:true