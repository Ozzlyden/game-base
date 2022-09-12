package com.victor.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.victor.entities.Bullet;
import com.victor.entities.Enemy1;
import com.victor.entities.Enemy2;
import com.victor.entities.Entity;
import com.victor.entities.Lifepack;
import com.victor.entities.Weapon;
import com.victor.main.Game;

public class World {
	
	//Array Simple -> []  e tem ArrayMultiDimencional -> [] [] 
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	
	public World (String path){
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
			 * vermelho = inimigo 2 (0xFFFF3F4C)
			 * preto = chão (0xFF000000)
			 * Branco = parede (0xFFFFFFFF)
			 * laranja = arma  (0xFF7F3300)
			 * rosa = vida	(0xFFFF7F7F)
			 * amarelo = munição (0xFFFFD800)
			 */
			
			//LOGICA  DE IDENTIFICACAO POR PIXELS
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					
					if(pixelAtual == 0xFF000000) {
						//Floor 
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}else if (pixelAtual == 0xFFFFFFFF) {
						//Wall
						tiles[xx + (yy * WIDTH) ] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					}else if (pixelAtual == 0xFF0026FF) {
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
					else if(pixelAtual == 0xFFFF3F4C) {
						//Enemy 2
						Enemy2 en2 = new Enemy2(xx*16, yy*16, 16, 16, Entity.ENEMY2_EN); 
						Game.entities.add(en2);
						Game.enemies2.add(en2);
					}else if(pixelAtual == 0xFF7F3300) {
						//Weapon
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
					}else if(pixelAtual ==  0xFFFF7F7F) {
						//Life Pack
						Game.entities.add(new Lifepack (xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
					}else if (pixelAtual == 0xFFFFD800) {
						//Bullet
						Game.entities.add(new Bullet (xx*16, yy*16, 16, 16, Entity.BULLET_EN));

					}
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//METODO DE COLISOES
	public static boolean isFree(int xnext, int ynext) {
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
		return ! (tiles[x1 + (y1 * World.WIDTH)]instanceof WallTile ||
				(tiles[x2 + (y2 * World.WIDTH)]instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)]instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)]instanceof WallTile));	
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
	
}
