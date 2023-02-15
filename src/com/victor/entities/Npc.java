package com.victor.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;

public class Npc extends Entity{
	
	public static String[] frases = new String[1];
	public static String[] frases_dialogo = new String[2];
	public static String[] frases_interacao = new String[1];
	
	public static boolean showMessage = false;
	public boolean show = false;
	
	public int curIndexMsg = 0;	//mensagem atual
	public int fraseIndex = 0;	//contador
	public int time = 0;	//velocidade do texto
	public int maxTime = 5;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		//FRASES SEM DIALOGO (Game.Npc.meessageNpc())
		frases[0] = "Vai matar eles!";
				
		//FRASE CAIXA DE DIALOGO (Npc.render())
		frases_dialogo[0] = "Bem-Vindo ao Game";
		frases_dialogo[1] = "Destrua os Inimigos pelo mapa";
		
		frases_interacao[0] = ">Pressione Enter para fechar<";
	}

	
	public void tick() {
		depth = 3;
		
		//CALCULO DE DISTANCIA
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer - xNpc) < 20 && Math.abs(yPlayer - yNpc) < 20) {	//20 distancia para mostrar mensagem
			
			//ABIR DIALOGO
			if(show == false)
				showMessage = true;
				show = true;
		}else {
			//showMessage = false;
		}
		
		
		time++;
		if(showMessage) {
			//DELAY
			if(time >= maxTime) {
				time = 0;
				//MUDANDO FRASES
				if(curIndexMsg < frases_dialogo[fraseIndex].length()) {
					curIndexMsg++;
			
				}else {
					if(fraseIndex < frases_dialogo.length) {
						fraseIndex++;
						curIndexMsg = 0;
					}
				}
			}	
		}
	}
	
	//MENSAGEM NPC
	public void messageNpc(Graphics g) {
		if(showMessage) {
			g.setFont(new Font("Arial",Font.BOLD,11));
			g.setColor(Color.white);
			g.drawString(Npc.frases[0],70 , 90);
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
			g.drawString(frases_dialogo[fraseIndex].substring(0, curIndexMsg), (int)x + 30, (int)y);
			g.drawString(frases_interacao[0],(int)x + 10, (int)y + 10);
			
		}
	}
	
}
