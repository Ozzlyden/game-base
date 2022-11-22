package com.victor.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;

public class Npc extends Entity{
	
	public static String[] frases = new String[5];
	
	public static boolean showMessage = false;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "render Npc x3 scala";
	}

	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer - xNpc) < 20 && Math.abs(yPlayer - yNpc) < 20) {	//20 distancia para mostrar mensagem
			showMessage = true;
		}else {
			showMessage = false;
		}
		
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		if(showMessage) {
			g.setFont(new Font("Arial",Font.BOLD,7));
			g.setColor(Color.white);
			g.drawString(frases[0], 32, 32);
		}
	}
	
}
