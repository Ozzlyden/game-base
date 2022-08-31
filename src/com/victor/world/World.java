package com.victor.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.victor.entities.Bullet;
import com.victor.entities.Enemy;
import com.victor.entities.Entity;
import com.victor.entities.Lifepack;
import com.victor.entities.Weapon;
import com.victor.main.Game;

public class World {
	
	//Array Simple -> []  e tem ArrayMultiDimencional -> [] [] 
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	
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
			 * vermelho = inimigo (0xFFFF0000)
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
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
					}else if (pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);	//Localizacao do player no map 
						Game.player.setY(yy*16);
					}
					else if(pixelAtual == 0xFFFF0000) {
						//Enemy
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
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
	

	public void render (Graphics g) {
		
		for (int xx = 0; xx < WIDTH; xx ++) {
			for (int yy = 0; yy < HEIGHT; yy++ ) {
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
