package com.victor.world;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
	
	
	
	public World (String path){
		try {
			//IDENTIFICACAO DE CADA PIXEL DO MAP
			BufferedImage map = ImageIO.read(getClass().getResourceAsStream(path));
			//Calcular a area de px (20px por 20px) e colocando em um Array
			int [] pixels = new int[map.getWidth() * map.getHeight()];	
			//Identificacao de cores
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			
			/*	FUNCAO DE CADA COR
			 * Azul = player
			 * vermelho = inimigo
			 * preto = chão
			 * Branco = parede
			 * laranja = arma
			 * rosa = vida
			 * amarelo = munição
			 */
			
			
			for(int i = 0; i < pixels.length; i++) {
				if(pixels [i] == 0xFFFF0000 ) {			//Vermelho em Hexadecimal
					System.out.println("Vermelhor");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
