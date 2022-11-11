package com.victor.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.victor.graficos.Spritesheet;
import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.World;

public class Player extends Entity {
	
	public boolean  stay, right, up, left, down; 
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 4;
	public int dir = 0;						//dir = direcao
	public double speed = 1.4;
	
	private int frames = 0, maxFrames =5, index = 0, maxIndex = 3;
	private boolean moved = false;
	
	public double life = 100, maxLife = 100;
	public int ammo = 0;
	public int mx, my;	// posicao mouse x e y
	
	//FAKE JUMP
	public boolean jump = false;
	public int z = 0;	//eixo z, altura jump
	public int jumpFrames = 40, jumpCur = 0;	//animacao do pulo
	public boolean isJumping = false;
	public int jumpSpd = 1;
	public boolean jumpUp = false, jumpDown = false;
	
	private BufferedImage[] frontPlayer;
	private BufferedImage[] backPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage playerDamage;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	private boolean arma = false;
	public boolean shoot = false, mouseShoot = false;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		//SPRTESHEETS ARRAY
		frontPlayer = new BufferedImage[2];
		backPlayer = new BufferedImage[2];
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		
		playerDamage = Game.spritesheet.getSprite(80, 33, 16, 16);
		
		
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
		fakeJump();
		// CAMADA DE RENDER
		depth = 2;
		
		//EDIT MASK
		mwidth = 10;
		mheight = 10;
		maskx = 2;
		masky = 3;
		
		//LOGICA DE MOVIMENTACAO
		moved = false;
			if(right && World.isFree((int) (x + speed), this.getY(), z)) {
				moved = true;
				dir = right_dir;
				x+=speed;
			}
			else if(left && World.isFree((int) (x - speed), this.getY(), z)) {
				moved = true;
				dir = left_dir;
				x-=speed;
			}
			if(up && World.isFree(this.getX(), (int) (y - speed), z)) {
				moved = true;
				//dir = up_dir;
				y-=speed;
			}	
			else if(down && World.isFree(this.getX(), (int) (y + speed), z)) {
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
			this.checkCollisionLifePack();
			checkCollisionAmmo();
			checkCollisionGun();
			feedBackDamage();
			
			//LOGICA SISTEMA DE TIRO TECLADO
			if(shoot) {
				shoot = false;
				if(arma && ammo > 0) {
				ammo--;
				int dx = 0;		//direcao tiro
				int px = 0;		// Posicao do tiro
				int py = 8;
				if (dir == right_dir) {
					px = 16;
					dx = 1;
				}else {
					px = -6;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
				}
			}
			
			//SISTEMA DE TIRO COM MOUSE
			if (mouseShoot) {
				mouseShoot = false;
				if(arma && ammo > 0) {
				ammo--;
				//CALCULO DO ANGULO
				double angle = Math.atan2(my - (this.getY() + 8 - Camera.y),mx - (this.getX() + 8 - Camera.x));
				double dx = Math.cos(angle);		//direcao tiro
				double dy = Math.sin(angle);
				int px = 8;		// Posicao do tiro
				int py = 8;
				if (dir == right_dir) {
					px = 16;
					dx = 1;
				}else {
					px = -6;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
				}
			}
			
			//GAME OVER
			if(life <= 0) {
				life = 0;
				Game.gameState = "GAME_OVER";
			}
			updateCamera();
	}
	
	private void fakeJump() {
		//FAKE JUMP
			if(jump) {
				if (isJumping == false) {
					jump = false;
					isJumping = true;
					jumpUp = true;
				}
			}
				
			if(isJumping == true) {
				if(jumpUp ) {
					jumpCur+=2;
				}else if(jumpDown) {
						jumpCur -=2;
					if(jumpCur <= 0) {
						isJumping = false;
						jumpDown = false;
						jumpUp = false;
					}
				}
			z = jumpCur;
			if(jumpCur >= jumpFrames ) {	//altura MAX
				jumpUp = false;
				jumpDown = true;
			}	
		}
	}
	
	
			
			
		
	
	private void feedBackDamage() {
		//FEEDBACK DANO ANIMCACAO
		if (isDamaged){
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
	}
	
	
	//LOGICA DE PEGAR AMMO
	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					ammo+=15;
					//System.out.println("Muincao atual:" + ammo);
					Game.entities.remove(atual);
					return;
				}
			}
			
		}
	}
	
	//LOGICA DE PEGAR ARMA
	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					arma = true;
					//System.out.println("Pegou a arma");
					Game.entities.remove(atual);
					return;
				}
			}
			
		}
	}
	
	//LOGICA DE PEGAR ITENS
	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColliding(this, atual)) {
					life+=10;
					if(life > 100)
						life = 100;
					Game.entities.remove(atual);
					return;
				}
			}
			
		}	
	}
	
	//LOGICA PARA A CAMERA SEGUIR e NAO MOSTRAR AS AREAS FORA DO MAPA
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	
	public void render (Graphics g) {
		
		//LOGICA ANIMACAO
		if(!isDamaged ) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(arma) {
					//render arma direita
					//esse + 6 e + 2 -> eh a posicao da arma
					g.drawImage(GUN_RIGHT, this.getX() + 6 - Camera.x, this.getY() +2 - Camera.y - z, null);
				}
			}else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(arma) {
					//render arma oara esquerda
					g.drawImage(GUN_LEFT, this.getX() - 6 - Camera.x, this.getY() + 2  - Camera.y - z, null);
				}
			}else if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			}else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			}
			
		}else {
			g.drawImage(playerDamage, this.getX() -  Camera.x, this.getY() - Camera.y - z, null);
			if(arma) {
				if(dir == left_dir) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x,this.getY() - Camera.y - z , null);
				}else {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x,this.getY() - Camera.y - z, null);
				}
			}
		}
		if(isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX() -  Camera.x + 4, this.getY() - Camera.y  + 10, 8, 8);
		}
		
		//DEBUG  MASK
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, mwidth, mheight);
	}	
}
