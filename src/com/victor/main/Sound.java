package com.victor.main;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {

	public static class Clips{
		public Clip[] clips;
		private int p;	//controle
		private int count; //contador
		
		
		//CONSTRUTOR									ERROS					ERROS			MENSAGEM DE ERROS
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			if(buffer == null)
				return;
			
			clips = new Clip[count];
			this.count = count;
			
			for(int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}
		
		//TOCAR UMA VEZ
		public void play() {
			if(clips == null) 
				return;
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			if(p >= count)
				p = 0;
		}
		
		//TOCAR COM LOOP
		public void loop() {
			if(clips == null)
				return;
			clips[p].loop(300);
		}
		
		//LISTA DE SONS
		public static Clips music1 = load("/music.wav", 1);
		public static Clips music2 = load("/lost_woods.wav", 1);
		
		public static Clips effect_hurt1 = load("/hurt.wav", 1);
		public static Clips effect_shoot1 = load("/shoot.wav", 1);
		
		
		
		//LER ARQUIVO 
		private static Clips load(String name, int count) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
				
				byte[] buffer = new byte[1024];
				int read = 0;
				while((read = dis.read(buffer)) >= 0 ) {
					baos.write(buffer, 0, read);
				}
				dis.close();
				byte[] data = baos.toByteArray();
				return new Clips(data, count);
			}catch(Exception e) {
				try {
					return new Clips(null, 0);
				}catch(Exception ee){
					return null;
				}
				
			}
		}
		
	}
	
	
	
}
