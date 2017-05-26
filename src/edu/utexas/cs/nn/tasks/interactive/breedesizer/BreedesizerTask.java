package edu.utexas.cs.nn.tasks.interactive.breedesizer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.networks.Network;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.tasks.interactive.InteractiveEvolutionTask;
import edu.utexas.cs.nn.util.graphics.DrawingPanel;
import edu.utexas.cs.nn.util.graphics.GraphicsUtil;
import edu.utexas.cs.nn.util.sound.MIDIUtil;
import edu.utexas.cs.nn.util.sound.PlayDoubleArray;
import edu.utexas.cs.nn.util.sound.SoundFromCPPNUtil;

public class BreedesizerTask<T extends Network> extends InteractiveEvolutionTask<T> {

	//private static final int LENGTH_DEFAULT = 60000; //default length of generated amplitude
	private static final int FREQUENCY_DEFAULT = 440; //default frequency of generated amplitude: A440

	//ideal numbers to initialize AudioFormat; based on obtaining formats of a series of WAV files
	public static final float DEFAULT_SAMPLE_RATE = 11025; //default frame rate is same value
	public static final int DEFAULT_BIT_RATE = 8; 
	public static final int DEFAULT_CHANNEL = 1; 
	public static final int BYTES_PER_FRAME = 1; 

	public static final int CPPN_NUM_INPUTS	= 3;
	public static final int CPPN_NUM_OUTPUTS = 1;

	Keyboard keyboard;
	protected JSlider clipLength;
	protected boolean initializationComplete = false;

	public BreedesizerTask() throws IllegalAccessException {
		this(true);
	}
	
	public BreedesizerTask(boolean justBreedesizer) throws IllegalAccessException {
		super();
		clipLength = new JSlider(JSlider.HORIZONTAL, Keyboard.NOTE_LENGTH_DEFAULT, Parameters.parameters.integerParameter("maxClipLength"), Parameters.parameters.integerParameter("clipLength"));

		Hashtable<Integer,JLabel> labels = new Hashtable<>();
		clipLength.setMinorTickSpacing(10000);
		clipLength.setPaintTicks(true);
		labels.put(Keyboard.NOTE_LENGTH_DEFAULT, new JLabel("Shorter clip"));
		labels.put(Parameters.parameters.integerParameter("maxClipLength"), new JLabel("Longer clip"));
		clipLength.setLabelTable(labels);
		clipLength.setPaintLabels(true);
		clipLength.setPreferredSize(new Dimension(350, 40));

		/**
		 * Implements ChangeListener to adjust clip length of generated sounds. When clip length is specified, 
		 * input length is used to reset and redraw buttons. 
		 */
		clipLength.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(!initializationComplete) return;
				// get value
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting()) {

					int newLength = (int) source.getValue();

					Parameters.parameters.setInteger("clipLength", newLength);
					// reset buttons
					resetButtons();
				}
			}
		});

		top.add(clipLength);	

		if(justBreedesizer) {
			keyboard = new Keyboard();
		
			JButton playWithMIDI = new JButton("PlayWithMIDI");
			// Name is first available numeric label after the input disablers
			playWithMIDI.setName("" + (CHECKBOX_IDENTIFIER_START - inputMultipliers.length));
			playWithMIDI.addActionListener(this);
			top.add(playWithMIDI);
		}
		initializationComplete = true;
	}


	protected void respondToClick(int itemID) {
		super.respondToClick(itemID);
		// Play original sound if they click the button
		if(itemID == (CHECKBOX_IDENTIFIER_START - inputMultipliers.length)) {
			// Magic number 1: for track index 1: May need to fix later
			MIDIUtil.playMIDIWithCPPNFromString(Parameters.parameters.stringParameter("remixMIDIFile"), 1, currentCPPN);
		}
	}
	
	@Override
	public String[] sensorLabels() {
		return new String[] { "Time", "Sine of time", "bias" };
	}

	@Override
	public String[] outputLabels() {
		return new String[] { "amplitude" };
	}

	@Override
	protected String getWindowTitle() {
		return "Breedesizer";
	}

	/**
	 * Creates BufferedImage from amplitude generated by network (saved in double array) and plays amplitude generated. 
	 */
	@Override
	protected BufferedImage getButtonImage(Network phenotype, int width, int height, double[] inputMultipliers) {
		double[] amplitude = SoundFromCPPNUtil.amplitudeGenerator(phenotype, Parameters.parameters.integerParameter("clipLength"), FREQUENCY_DEFAULT, inputMultipliers);
		BufferedImage wavePlotImage = GraphicsUtil.wavePlotFromDoubleArray(amplitude, height, width);
		return wavePlotImage;
	}

	/**
	 * Plays sound associated with an image when the image is clicked
	 */
	@Override
	protected void additionalButtonClickAction(int scoreIndex, Genotype<T> individual) {
		if(chosen[scoreIndex]) { // Play sound if item was just selected
			Network phenotype = individual.getPhenotype();
			double[] amplitude = SoundFromCPPNUtil.amplitudeGenerator(phenotype, Parameters.parameters.integerParameter("clipLength"), FREQUENCY_DEFAULT, inputMultipliers);
			PlayDoubleArray.playDoubleArray(amplitude);	
			keyboard.setCPPN(phenotype);
		} else {
			PlayDoubleArray.stopPlayback();
		}
	}

	@Override
	protected void save(int i) {	
		//SAVING IMAGE

		// Use of imageHeight and imageWidth allows saving a higher quality image than is on the button
		BufferedImage toSave = getButtonImage((Network)scores.get(i).individual.getPhenotype(), Parameters.parameters.integerParameter("imageWidth"), Parameters.parameters.integerParameter("imageHeight"), inputMultipliers);
		DrawingPanel p = GraphicsUtil.drawImage(toSave, "" + i, toSave.getWidth(), toSave.getHeight());
		JFileChooser chooser = new JFileChooser();//used to get save name 
		chooser.setApproveButtonText("Save");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP Images", "bmp");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {//if the user decides to save the image
			System.out.println("You chose to call the image: " + chooser.getSelectedFile().getName());
			p.save(chooser.getCurrentDirectory() + "\\" + chooser.getSelectedFile().getName() + (showNetwork ? "network" : "image") + ".bmp");
			System.out.println("image " + chooser.getSelectedFile().getName() + " was saved successfully");
			p.setVisibility(false);
		} else { //else image dumped
			p.setVisibility(false);
			System.out.println("image not saved");
		}

		//SAVING AUDIO

		chooser = new JFileChooser();
		AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, DEFAULT_SAMPLE_RATE, DEFAULT_BIT_RATE, DEFAULT_CHANNEL, BYTES_PER_FRAME, DEFAULT_SAMPLE_RATE, true);
		chooser.setApproveButtonText("Save");
		FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("WAV audio files", "wav");
		chooser.setFileFilter(audioFilter);
		int audioReturnVal = chooser.showOpenDialog(frame);
		if(audioReturnVal == JFileChooser.APPROVE_OPTION) {//if the user decides to save the image
			System.out.println("You chose to call the file: " + chooser.getSelectedFile().getName());
			try {
				SoundFromCPPNUtil.saveFileFromCPPN(scores.get(i).individual.getPhenotype(), Parameters.parameters.integerParameter("clipLength"), FREQUENCY_DEFAULT, chooser.getSelectedFile().getName() + ".wav", af);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("audio file " + chooser.getSelectedFile().getName() + " was saved successfully");
			p.setVisibility(false);
		} else { //else image dumped
			p.setVisibility(false);
			System.out.println("audio file not saved");
		}	

	}


	@Override
	public int numCPPNInputs() {
		return CPPN_NUM_INPUTS;
	}


	@Override
	public int numCPPNOutputs() {
		return CPPN_NUM_OUTPUTS;
	}

}
