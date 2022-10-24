package com.victor.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {
	
	public String[] options = {"Novo jogo", "Carregar jogo", "Sair"}; 
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	public boolean pause = false;

	public void tick() {
		//LOGICA DE SELECIONAR OPCOES
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) 
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;	
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}
			else if(options[currentOption] == "Carregar jogo") {
				//Sistema de salve
			}
			else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	//SAVE
	public static void salveGame(String[] val1,int[]  val2, int encode) {
		//endoce para o sistema de criptografia, e val eh o valor
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < val1.length; i++) {
			//CRIACAO DO ARQUIVO SALVO E VALORES
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();	//transformando os dados em String
			for(int n = 0; n < value.length; n++) {
				//CRIPTOGRAFIA
				value[n]+=encode;
				current+= value[n];
			}
			try {
				//ESCRITA DOS DADOS
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}
	
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("->Zelda Clone<-", (Game.WIDTH*Game.SCALE / 2) - 125, (Game.HEIGHT*Game.SCALE / 2 - 150));
		
		//OPCOE MENU
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if(pause == false)
		g.drawString("Novo jogo", (Game.WIDTH*Game.SCALE / 2) - 70, 150);
		else
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE / 2) - 70, 150);
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
