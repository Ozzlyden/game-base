package com.victor.entities;

import java.awt.Graphics;
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
	
	public static double life = 100, maxLife = 100;
	public int ammo = 0;
	
	private BufferedImage[] frontPlayer;
	private BufferedImage[] backPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage playerDamage;
	
	public boolean isDamaged = false;
	
	private int damageFrames = 0;
	

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
		//LOGICA DE MOVIMENTACAO
		moved = false;
			if(right && World.isFree((int) (x + speed), this.getY())) {
				moved = true;
				dir = right_dir;
				x+=speed;
			}
			else if(left && World.isFree((int) (x - speed), this.getY())) {
				moved = true;
				dir = left_dir;
				x-=speed;
			}
			if(up && World.isFree(this.getX(), (int) (y - speed))) {
				moved = true;
				//dir = up_dir;
				y-=speed;
			}	
			else if(down && World.isFree(this.getX(), (int) (y + speed))) {
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
			
			if (isDamaged){
				this.damageFrames++;
				if(this.damageFrames == 8) {
					this.damageFrames = 0;
					isDamaged = false;
				}
			}
			
			if(life <= 0) {
				//Game over
				Game.entities = new ArrayList<Entity>();
				Game.enemies1 = new ArrayList<Enemy1>();
				Game.enemies2 = new ArrayList<Enemy2>();
				Game.spritesheet = new Spritesheet("/spritesheet.png");	//chamando o arquivo res/spritesheet.png
				Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16) );/*(0, 0, 16,16) eh onde o Player inicia no mapa e (32, 0, 16, 16) eh a 
				regiao em que esta a imagem do player na spritesheet*/
				Game.entities.add(Game.player);
				Game.world = new World("/map.png");
				return;
				//System.exit(1);
			}
			
			//LOGICA PARA A CAMERA SEGUIR e NAO MOSTRAR AS AREAS FORA DO MAPA
			Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH * 16 - Game.WIDTH);
			Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void checkCollisionAmmo() {
		//LOGICA DE PEGAR AMMO
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					ammo+=10;
					System.out.println("Muincao atual:" + ammo);
					Game.entities.remove(atual);
					return;
				}
			}
			
		}
	}
	
	public void checkCollisionLifePack() {
		//LOGICA DE PEGAR ITENS
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
	
	
	
	public void render (Graphics g) {
		
		//LOGICA ANIMACAO
		if(!isDamaged ) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}else {
			g.drawImage(playerDamage, this.getX() -  Camera.x, this.getY() - Camera.y, null);
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
