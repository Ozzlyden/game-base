package com.victor.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.World;

public class Enemy2 extends Entity{
	
	private double speed = 0.4;
	
	//var para as dimensoes da mascara de colisao
	private int maskx = 2, masky = 3, maskw = 10, maskh = 10;
	
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;
	
	private BufferedImage[] spriteEnemy2;
	
	private int life = 10;
	
	public boolean isDamaged = false;
	private int damageFrames = 8, damageCurrent = 0;
	

	public Enemy2(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		spriteEnemy2 = new BufferedImage [2];
		
		spriteEnemy2[0] = Game.spritesheet.getSprite(0, 48, 16, 16);
		spriteEnemy2[1] = Game.spritesheet.getSprite(16, 48, 16, 16);
	}
	
	//LOGICA I.A
	public void tick() {
		
		if(this.isCollidingWithPlayer() == false) {
		
		if((int)x < Game.player.getX() /*&& World.isFree((int)(x + speed), this.getY())
				*/&& !isColliding((int)(x + speed), this.getY())) {
			x+=speed;
		}
		else if((int)x > Game.player.getX() /*&& World.isFree((int)(x - speed), this.getY())
				*/&& !isColliding((int)(x - speed), this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY() /*&& World.isFree(this.getX(), (int)(y + speed))
				*/&& !isColliding(this.getX(), (int)(y + speed))) {
			y+=speed;
		}
		else if((int)x > Game.player.getY() /*&& World.isFree(this.getX(), (int) (y - speed))
				*/&& !isColliding(this.getX(), (int) (y - speed))) {
			y-=speed;
		}
		}else {
			//SISTEMA DE DANO
			if(Game.rand.nextInt(100) < 10) {
			Game.player.life-=Game.rand.nextInt(15);
			Game.player.isDamaged = true;
			if(Game.player.life <= 0) {
				//Game over
				//System.exit(1);
			}
			//System.out.println("Vida" + Game.player.life);
			}
		}

			//LOGICA ANIMACAO
			frames++;
			if(frames == maxFrames) {
					frames = 0;
					index++;
					if(index > maxIndex)
						index = 0;
				}	
		
		collidingBullet();
				
				if(life <= 0) {
					destroySelf();
					return;
				}
				
				//FEEDBACK DANO
				if(isDamaged) {
					this.damageCurrent++;
					if(this.damageCurrent == this.damageFrames) {
						this.damageCurrent = 0;
						this.isDamaged = false;
					}
				}	
					
		}
			
			//REMOVE
			public void destroySelf() {
				Game.enemies2.remove(this);
				Game.entities.remove(this);
			}
			
			//DANO BALA
			public void collidingBullet() {
				for (int i = 0; i < Game.bullets.size(); i++) {
					Entity e = Game.bullets.get(i);
					if(e instanceof BulletShoot) {
						if(Entity.isColliding(this, e)) {
							isDamaged = true;
							life--;
							Game.bullets.remove(i);
							return;
						}
						
					}
				}
			}
			

	
	//COLISAO COM PLAYER
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent =  new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	
	public boolean isColliding(int xnext, int ynext) {
		//lembre-se metodo de colisao usando retangulos
		Rectangle enemyCurrent =  new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies2.size(); i++) {
			Enemy2 e = Game.enemies2.get(i);
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
		if(!isDamaged)
			g.drawImage(spriteEnemy2[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
			//g.drawImage(spriteEnemy2[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
		else 
			g.drawImage(Entity.ENEMY2_FEEDBACK,this.getX() - Camera.x, this.getY() - Camera.y, null);
		
			//DEBUG  MASK
			//g.setColor(Color.BLUE);
			//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, maskw, maskh);
	}
		
}
