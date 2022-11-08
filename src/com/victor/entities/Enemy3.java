package com.victor.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.AStar;
import com.victor.world.Camera;
import com.victor.world.Vector2i;
import com.victor.world.World;

public class Enemy3 extends Entity{
	
	private double speed = 0.8;
	
	private int frames = 0, maxFrames = 25, index = 0, maxIndex = 1;
	
	private BufferedImage[] spriteEnemy3;
	
	private int life = 4;
	
	public boolean isDamaged = false;
	private int damageFrames = 8, damageCurrent = 0;
	
	//FAKE JUMP
		public boolean jump = false;
		public int z = 0;	//eixo z, altura jump
		public int jumpFrames = 40, jumpCur = 0;	//animacao do pulo
		public boolean isJumping = false;
		public int jumpSpd = 1;
		public boolean jumpUp = false, jumpDown = false;


	public Enemy3(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		spriteEnemy3 = new BufferedImage [2];
		
		spriteEnemy3[0] = Game.spritesheet.getSprite(0, 64, 16, 16);
		spriteEnemy3[1] = Game.spritesheet.getSprite(16, 64, 16, 16);
		
	}
	
	
	public void tick() {
	
		/*
		 //LOGICA I.A(Antiga)
		if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 60) {	// 60 eh distancia de avistamento 
		
		if(this.isCollidingWithPlayer() == false) {
			
		
		if((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY(), z)
				&& !isColliding((int)(x + speed), this.getY())) {
			x+=speed;
		}
		else if((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY(), z)
				&& !isColliding((int)(x - speed), this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed), z)
				&& !isColliding(this.getX(), (int)(y + speed))) {
			y+=speed;
		}
		else if((int)x > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed), z)
				&& !isColliding(this.getX(), (int) (y - speed))) {
			y-=speed;
		}
		}else {
			//SISTEMA DE DANO
			if(Game.rand.nextInt(100) < 10) {
				Game.player.life-=Game.rand.nextInt(12);
				Game.player.isDamaged = true;
					if(Game.player.life <= 0) {
						//Game over
						//System.exit(1);
					}
			//System.out.println("Vida: " + Game.player.life);
				}
			}
		}else {	//EM QUANTO NO VIU O PLAYER
			
		}
		*/
		
		//ALOGORITMO *A (IA Melhor)
		if(path == null || path.size() == 0) {
			Vector2i start = new Vector2i((int) (x/16), (int) (y/16));	//posicao inicial
			// Colocamos as posicoes atuais do Player
			Vector2i end = new Vector2i((int) (Game.player.x/16), (int) (Game.player.y/16));	//destino final
			path = AStar.findPath(Game.world, start, end);
		}
		followPath(path);
		
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
		Game.enemies3.remove(this);
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
		Rectangle enemyCurrent =  new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	
	
	public void render(Graphics g) {
		if(!isDamaged)
			g.drawImage(spriteEnemy3[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
			//g.drawImage(spriteEnemy2[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
		else 
			g.drawImage(Entity.ENEMY3_FEEDBACK,this.getX() - Camera.x, this.getY() - Camera.y, null);
		//DEBUG  MASK
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, maskw, maskh);
	}		
}

