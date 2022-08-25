package com.victor.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;

public class Player extends Entity {
	
	public boolean  stay, right, up, left, down; 
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 4;
	public int dir = 0;						//dir = direcao
	public double speed = 1.4;
	
	private int frames = 0, maxFrames =5, index = 0, maxIndex = 3;
	private boolean moved = false;
	
	private BufferedImage[] frontPlayer;
	private BufferedImage[] backPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		//SPRTESHEETS ARRAY
		frontPlayer = new BufferedImage[2];
		backPlayer = new BufferedImage[2];
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		
		
		//SPRITESHEET
		
		for (int i = 0; i < 2; i++) {
			frontPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for (int i = 0; i < 2; i++) {
			backPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (i*16), 0, 16, 16);	
		}
		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(64 + (i*16), 16, 16, 16);
		}
		for (int i = 0; i < 2;i++) {
			upPlayer[i] = Game.spritesheet.getSprite(128 + (i*16), 0, 16, 16);
		}
		for (int i = 0; i< 2; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(128 + (i*16), 16, 16, 16);
		}
		
	}

	public void tick() {
		//LOGICA DE MOVIMENTACAO
		moved = false;
			if(right) {
				moved = true;
				dir = right_dir;
				x+=speed;
			}
			else if(left) {
				moved = true;
				dir = left_dir;
				x-=speed;
			}
			if(up) {
				moved = true;
				//dir = up_dir;
				y-=speed;
			}	
			else if(down) {
				moved = true;
				//dir = down_dir;
				y+=speed;
			}
			//LOGICA ANIMACAO
			if(moved) {		//se o player mexer, acada 5 frames, faz uma animacao
				frames++;
				if(frames == maxFrames) {
					frames = 0;
					index++;
					if(index > maxIndex)
						index = 0;
				}
				
			}
	}
	
	public void render (Graphics g) {
		
		//LOGICA ANIMACAO
		
		
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);
		}else if (dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);
		}
		
		/*
		if(dir == up_dir) {
			g.drawImage(upPlayer[index], this.getX(), this.getY(), null);
		}else if (dir == down_dir) {
			g.drawImage(downPlayer[index], this.getX(), this.getY(), null);
		}
		
		*/
	}
	
}
