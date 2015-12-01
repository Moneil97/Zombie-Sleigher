package sleigher.zombie;

import java.io.IOException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.SamplePlayer;

public class Sound {
	
	SamplePlayer sample;
	Glide gainValue;
	Gain gain;
	
	public Sound(String path) {
		AudioContext ac = ZombieSleigher.audioContext;
		gainValue = new Glide(ac, 1); //audio context, init value
		gain = new Gain(ac, 1, gainValue);
		
		sample = null;
		try {
			sample = new SamplePlayer(ZombieSleigher.audioContext, new Sample(path));
			gain.addInput(sample);
			
			setKillOnEnd(false);
		} catch (IOException e) {
			System.err.println("Failed to load sound at " + path);
		}	
		
		ZombieSleigher.masterGain.addInput(gain);	
	}
	
	public void play() {
		sample.setToLoopStart();
		sample.start();
	}
	
	public void setKillOnEnd(boolean kill) {
		sample.setKillOnEnd(kill);
	}

}
