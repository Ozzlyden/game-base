package com.victor.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
			 * vermelho = inimigo
			 * preto = chão (0xFF000000)
			 * Branco = parede (0xFFFFFFFF)
			 * laranja = arma
			 * rosa = vida
			 * amarelo = munição
			 */
			
			//LOGICA  DE IDENTIFICACAO POR PIXELS
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					if(pixelAtual == 0xFF000000) {
						//floor 
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}else if (pixelAtual == 0xFFFFFFFF) {
						//	Wall
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
					}else if (pixelAtual == 0xFF0026FF) {
						//Player
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}else {
						//Floor/wall
						tiles[xx + (yy * WIDTH) ] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
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
