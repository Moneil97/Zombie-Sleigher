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
		gainValue = new Glide(ac, 1, 0); //audio context, init value, glide time in ms
		gain = new Gain(ac, 1, gainValue);
		
		try {
			sample = new SamplePlayer(ZombieSleigher.audioContext, new Sample(path));
		} catch (IOException e) {
			System.err.println("Failed to load sound at " + path);
		}
		
		gain.addInput(sample);
		
		setKillOnEnd(false);
		
		ZombieSleigher.masterGain.addInput(gain);
	}
	
	public void play() {
		
	}
	
	public void setKillOnEnd(boolean kill) {
		sample.setKillOnEnd(kill);
	}

}
