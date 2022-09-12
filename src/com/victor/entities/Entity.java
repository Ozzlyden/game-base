package com.victor.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.victor.main.Game;
import com.victor.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage ENEMY1_EN = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage ENEMY2_EN = Game.spritesheet.getSprite(0, 48, 16, 16);


	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	//GET E SETTERS -> Sao metodos de acesso para as var privadas
	//serve para proteger o as variaveis de alteracoes
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public void render (Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x,this.getY() - Camera.y,this.getWidth(),this.getHeight(), null);
	}
}
