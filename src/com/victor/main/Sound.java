package com.victor.main;

import java.applet.Applet;
import java.applet.AudioClip;

@SuppressWarnings("removal")
public class Sound {

	private AudioClip clip;
	public static final Sound musicBackground1 = new Sound("/music.wav");
	public static final Sound musicBackground2 = new Sound("/lost_woods.mp3");
	public static final Sound hurtEffect = new Sound("/hurt.wav");
	public static final Sound shootEffect = new Sound("/shoot.wav");
	
	private Sound(String name) {
		//ENCONTRAR MUSICA
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e) {
			
		}
		
	}
	
	
	public void play() {
		//TENTIVA DE BUSCA
		try {
			new Thread() {
				public void run(){
					clip.play();	//funcao do applet
				}
			}.start();
		}catch(Throwable e){}	//caso de erro na busca
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run(){
					clip.loop();	// funcao do applet
				}
			}.start();
		}catch(Throwable e){}
	}
	
}
