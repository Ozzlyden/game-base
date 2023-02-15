package com.victor.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.victor.entities.Player;
import com.victor.main.Game;

//USER INTERFACE
public class UI {
	
	public void tick() {
		
	}
	
	//UI AMMO
	public void messageAmmo(Graphics g) {
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Ammo: " + Game.player.ammo, 590, 35);
	}
	
	
	public void render(Graphics g) {
		//So serve de backGraund
		g.setColor(Color.red);
		g.fillRect(20, 6, 50, 10);
		//LOGICA DA BARRA DE VIDA
		g.setColor(Color.green);
		g.fillRect(20, 6, (int) ((Game.player.life/Game.player.maxLife) * 50), 10);		//Regra de 3 para o contador de vida
		//CONTAGEM DA BARRA DE VIDA
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxLife, 32 ,14);
	}

}
