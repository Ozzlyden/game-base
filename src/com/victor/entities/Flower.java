package com.victor.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;

public class Flower extends Entity{
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 2;
	
	private BufferedImage[] spriteFlower;

	public Flower(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		spriteFlower = new BufferedImage [3];
		
		spriteFlower[0] = Game.spritesheet.getSprite(0, 128, 16, 16);
		spriteFlower[1] = Game.spritesheet.getSprite(16, 128, 16, 16);
		spriteFlower[2] = Game.spritesheet.getSprite(32, 128, 16, 16);
	}
	
	public void tick() {
		depth = 0;
		//LOGICA ANIMACAO
		frames++;
		if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		
	}
	
	public void render(Graphics g) {
		g.drawImage(spriteFlower[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

}
