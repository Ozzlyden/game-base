package com.victor.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	public String[] options = {"Novo jogo", "Carregar jogo", "Sair"}; 
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down;
	

	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}	
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > 0) {
				currentOption = 0;
			}	
		}	
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("->Zelda Clone<-", (Game.WIDTH*Game.SCALE / 2) - 125, (Game.HEIGHT*Game.SCALE / 2 - 150));
		
		//OPCOE MENU
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 24));
		g.drawString("Novo jogo", (Game.WIDTH*Game.SCALE / 2) - 70, 150);
		g.drawString("Carregar jogo", (Game.WIDTH*Game.SCALE / 2) - 100, 190);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE / 2) - 50, 230);
		
		//OPCOES SELECIONADAS
		if(options[currentOption] == "Novo jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE / 2) - 90, 150);
		}else if(options[currentOption] == "Carregar jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE / 2) - 120, 190);
		}else if(options[currentOption] == "Sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE / 2) - 70, 230);
		}
	}
	
}
