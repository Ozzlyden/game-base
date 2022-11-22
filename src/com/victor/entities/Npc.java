package com.victor.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;

public class Npc extends Entity{
	
	public static String[] frases = new String[5];
	
	public static boolean showMessage = false;
	public boolean show = false;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Bem-Vindo ao Game";
		frases[1] = "Destrua os Inimigos pelo mapa";
		frases[2] = "Pressione Enter para fechar";
	}

	
	public void tick() {
		depth = 3;
		
		//CALCULO DE DISTANCIA
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer - xNpc) < 20 && Math.abs(yPlayer - yNpc) < 20) {	//20 distancia para mostrar mensagem
			
			if(show == false)
			showMessage = true;
			show = true;
		}else {
			//showMessage = false;
		}
		
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		if(showMessage) {
			g.setColor(Color.white);
			g.fillRect(9, 9, Game.WIDTH - 18, Game.HEIGHT - 18);
			g.setColor(Color.blue);
			g.fillRect(10, 10, Game.WIDTH - 20, Game.HEIGHT - 20);
			
			g.setFont(new Font("Arial",Font.BOLD,9));
			g.setColor(Color.white);
			g.drawString(frases[0], (int)x + 40, (int)y);
			g.drawString(frases[1],(int)x + 10, (int)y + 10);
			
			g.drawString(frases[2],(int)x + 20, (int)y + 25);
		}
	}
	
}
