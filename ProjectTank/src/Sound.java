import javax.sound.sampled.*;

public class Sound {
	private Clip clip;
	
	// Change file name to match yours, of course
        public static Sound explosion = new Sound("Explosion_02.wav");
	public static Sound bgm = new Sound("Conflict.wav");
        public static Sound fail = new Sound("fail.wav");
        public static Sound win = new Sound("win.wav");
        //Sound.sound1.play(); to play once, or
        //Sound.sound1.loop();
	public Sound (String fileName) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			if (clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.start();
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		if(clip == null) return;
		clip.stop();
	}
	
	public void loop() {
		try {
			if (clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isActive(){
		return clip.isActive();
	}
}
