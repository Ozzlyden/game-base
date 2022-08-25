package com.victor.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity {
	
	public boolean right, up, left, down; 
	public double speed = 1.4;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}

	public void tick() {
		//LOGICA DE MOVIMENTACAO
			if(right)
				x+=speed;
			else if(left)
				x-=speed;
			
			if(up)
				y-=speed;
			else if(down)
				y+=speed;
			
			
	}
	
}
