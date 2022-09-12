package com.victor.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.World;

public class Enemy1 extends Entity{
	
	private double speed = 0.6;
	
	//var para as dimensoes da mascara de colisao
	private int maskx = 8, masky = 8, maskw = 5, maskh = 5;
	
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;
	
	private BufferedImage[] spriteEnemy1;

	

	public Enemy1(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		spriteEnemy1 = new BufferedImage [2];
		
		spriteEnemy1[0] = Game.spritesheet.getSprite(16, 32, 16, 16);
		spriteEnemy1[1] = Game.spritesheet.getSprite(32, 32, 16, 16);
		
	}
	
	//LOGICA I.A
	public void tick() {
		
		if((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY())
				&& !isColliding((int)(x + speed), this.getY())) {
			x+=speed;
		}
		else if((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY())
				&& !isColliding((int)(x - speed), this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed))
				&& !isColliding(this.getX(), (int)(y + speed))) {
			y+=speed;
		}
		else if((int)x > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
				&& !isColliding(this.getX(), (int) (y - speed))) {
			y-=speed;
		}
		
		//LOGICA ANIMACAO
			
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
			
		}
	
	
	public boolean isColliding(int xnext, int ynext) {
		//lembre-se metodo de colisao usando retangulos
		Rectangle enemyCurrent =  new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies1.size(); i++) {
			Enemy1 e = Game.enemies1.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy =  new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
			return true;
			}
			
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(spriteEnemy1[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.drawImage(spriteEnemy2[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		//DEBUG  MASK
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, maskw, maskh);
	}
	
	
		
}
