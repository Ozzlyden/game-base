package com.victor.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.victor.entities.Bullet;
import com.victor.entities.Enemy1;
import com.victor.entities.Enemy2;
import com.victor.entities.Enemy3;
import com.victor.entities.Entity;
import com.victor.entities.Flower;
import com.victor.entities.Lifepack;
import com.victor.entities.Player;
import com.victor.entities.Weapon;
import com.victor.graficos.Spritesheet;
import com.victor.main.Game;

public class World {
	
	//Array Simple -> []  e tem ArrayMultiDimencional -> [] [] 
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	
	public World (String path){
		
		//ALGORITMO MAP RANDOMICO
		Game.player.setX(0);	//posicao inicial Player
		Game.player.setY(0);
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH * HEIGHT];
		
		//COLOCANDO PAREDES (WALL)
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				tiles[xx + yy *WIDTH] = new WallTile(xx * 16, yy * 16,Tile.TILE_WALL);
			}
		}
		
		int dir = 0;
		int xx = 0, yy = 0;
		
		
		for (int i = 0; i < 200; i++) {	//200 tamanho do mundo
			//RESCREVENDO E COLOCANDO CHAO (FLOOR_
			tiles[xx + yy *WIDTH] = new FloorTile(xx * 16, yy * 16,Tile.TILE_FLOOR1);
			
			if(dir == 0) {
			//direita
				if(xx < WIDTH) {
					xx++;
				}
				
			}else if(dir == 1) {
			//esquerda
				if(xx > 0) {
					xx--;
				}
				
			}else if(dir == 2) {
			//baixo
				if(yy < HEIGHT) {
					yy++;
				}
			}else if(dir == 3) {
			//cima
				if(yy > 0) {
					yy--;
				}
			}
			
			if(Game.rand.nextInt(100) < 30) {
				dir = Game.rand.nextInt(4);
			}
		}
		
		
		/*
		 //CRIACAO DO MAP NORMAL 
		try {
			//IDENTIFICACAO DE CADA PIXEL DO MAP
			BufferedImage map = ImageIO.read(getClass().getResourceAsStream(path));
			//Calcular a area de px (20px por 20px) e colocando em um Array
			int [] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			//Identificacao de cores
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			
			/*	FUNCAO DE CADA COR
			 * Azul = player (0xFF0026FF)
			 * vermelho = inimigo 1 (0xFFFF0000)
			 * vermelho = inimigo 2 (0xFFFF0001)
			 * vermelho = inimigo 3 (0xFFFF0002)
			 * 
			 * preto = chão (0xFF000000)
			 * Branco = parede (0xFFFFFFFF)
			 * 
			 * laranja = arma  (0xFF7F3300)
			 * rosa = vida	(0xFFFF7F7F)
			 * amarelo = munição (0xFFFFD800)
			 * verde = flower (0xFF4CFF00)
			 */
		
			/*
			
			//LOGICA  DE IDENTIFICACAO POR PIXELS
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR1);
					
					
					if(pixelAtual == 0xFF000000){
						//Floor1 
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR1);
					}
					else if (pixelAtual == 0xFF000001) {
						//Floor2
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR2);
					}
					else if (pixelAtual == 0xFF000002) {
						//Floor3
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR3);
					}
					else if (pixelAtual == 0xFFFFFFFF) {
						//Wall
						tiles[xx + (yy * WIDTH) ] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					}
					else if (pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);	//Localizacao do player no map 
						Game.player.setY(yy*16);
					}
					else if(pixelAtual == 0xFFFF0000) {
						//Enemy
						Enemy1 en1 = new Enemy1(xx*16, yy*16, 16, 16, Entity.ENEMY1_EN); 
						Game.entities.add(en1);
						Game.enemies1.add(en1);
					}
					else if(pixelAtual == 0xFFFF0001) {
						//Enemy 2
						Enemy2 en2 = new Enemy2(xx*16, yy*16, 16, 16, Entity.ENEMY2_EN); 
						Game.entities.add(en2);
						Game.enemies2.add(en2);
					}
					else if(pixelAtual == 0xFFFF0002) {
						//Enemy 3
						Enemy3 en3 = new Enemy3(xx*16, yy*16, 16, 16, Entity.ENEMY3_EN); 
						Game.entities.add(en3);
						Game.enemies3.add(en3);
					}
					else if(pixelAtual == 0xFF7F3300) {
						//Weapon
						Weapon weapon = new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN);
						weapon.setMask(8, 8, 8, 8);	//Tamanho mascara
						Game.entities.add(weapon);
					}
					else if(pixelAtual ==  0xFFFF7F7F) {
						//Life Pack
						Lifepack pack = new Lifepack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN);
						pack.setMask(8, 8, 8, 8);
						Game.entities.add(pack);
					}
					else if (pixelAtual == 0xFFFFD800) {
						//Bullet
						Bullet bullet = new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_EN);
						bullet.setMask(8, 8, 8, 8);
						Game.entities.add(bullet);
					}
					else if (pixelAtual == 0xFF4CFF00) {
						//Flower
						Flower flower = new Flower(xx*16, yy*16, 16, 16, Entity.FLOWER);
						flower.setMask(8, 8, 8, 8);
						Game.entities.add(flower);
					}
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	//METODO DE COLISOES
	public static boolean isFree(int xnext, int ynext, int z) {
		//LOGICA DE COLOSIAO
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		//TILE_SIZE eh o tamanho da mascara em px
		
		int x2 =( xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 =(ynext+TILE_SIZE-1)  / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1)  / TILE_SIZE;
		
		// ! serve para negar o dado	
		if(!((tiles[x1 + (y1 * World.WIDTH)]instanceof WallTile ||
				(tiles[x2 + (y2 * World.WIDTH)]instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)]instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)]instanceof WallTile)))) {	
			return true;
		}
		if(z > 0) {
			return true;
		}
		return false;
	}
	
	//RESET AO PASSAR LVL
	public static void restarGame(String level ) {
		Game.entities.clear();
		Game.enemies1.clear();
		Game.enemies2.clear();
		Game.enemies3.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies1 = new ArrayList<Enemy1>();
		Game.enemies2 = new ArrayList<Enemy2>();
		Game.enemies3 = new ArrayList<Enemy3>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");	//chamando o arquivo res/spritesheet.png
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16) );
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}

	public void render (Graphics g) {
		
		//LOGICA DE RENDER PARA AREA QUE O PLAYER ESTA
		
		int xstart = Camera.x>>4;
		int ystart = Camera.y>>4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for (int xx = xstart; xx <= xfinal; xx ++) {
			for (int yy = ystart; yy <= yfinal; yy++ ) {
				
				//Caso xx e yy fique negativo da um erro, pois o uso desse if()
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {	
					continue;
				}
				
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
			
		}
	}
	
	
	/*
	public static void renderMiniMap() {
		for(int i = 0; i < Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;
		}
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile) {
					Game.minimapaPixels[xx + (yy*WIDTH)] = 0xffFFFF;
				}
			}
		}
		//passando as posicoes Player para Tile
		int xPlayer = Game.player.getX() / 16;
		int yPlayer = Game.player.getY() / 16;
		
		Game.minimapaPixels[xPlayer + (yPlayer*WIDTH)] = 0xff0026FF;
	}
	*/
}

