package com.victor.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.World;

public class BulletShoot extends Entity{
	
	private double dx, dy, dz;
	private double spd = 3;
	
	private int life = 80, curLife = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite); 
		this.dx = dx;
		this.dy = dy;
		
	}
	
	public void tick() {
		depth = 0;
		
		//EDIT MASK
		mwidth = 2;
		mheight = 2;
		maskx = 1;
		masky = 1;
		
		//LOGICA TIRO
		x+=dx * spd;
		y+=dy * spd;
		curLife++;
		
		//COLISAO 
		if( World.isFree((int)(dx + spd), (int)dy, (int)dz)) {
			Game.bullets.remove(this);
			//Tentativa de remover bala ao colidir com tile
		}
		
		if(curLife == life){
			Game.bullets.remove(this);
			return;
		}
	}
	
	
	//COLISAO BALA
	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				if(Entity.isColliding(this, e)) {
					Game.bullets.remove(i);
					return;
				}	
			}
		}
	}
	
	//MASK COLISAO
	public boolean isColliding(int xnext, int ynext) {
		//lembre-se metodo de colisao usando retangulos
		Rectangle bulletCurrent =  new Rectangle(xnext + maskx, ynext + masky, mwidth, mheight);
		
		for(int i = 0; i < Game.bullets.size(); i++) {
			BulletShoot e = Game.bullets.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy =  new Rectangle(e.getX() + maskx, e.getY() + masky, mwidth, mheight);
			if(bulletCurrent.intersects(targetEnemy)) {
			return true;
			}
			
		}
		return false;
	}
	
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height );
		
		//DEBUG  MASK
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, mwidth, mheight);
	}

}
