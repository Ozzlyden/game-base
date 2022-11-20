package com.victor.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.victor.main.Game;
import com.victor.world.Camera;
import com.victor.world.Node;
import com.victor.world.Vector2i;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage ENEMY1_EN = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage ENEMY2_EN = Game.spritesheet.getSprite(0, 48, 16, 16);
	public static BufferedImage ENEMY3_EN = Game.spritesheet.getSprite(0, 64, 16, 16);
	public static BufferedImage ENEMY1_FEEDBACK = Game.spritesheet.getSprite(96, 32, 16, 16);
	public static BufferedImage ENEMY2_FEEDBACK = Game.spritesheet.getSprite(112, 32, 16, 16);
	public static BufferedImage ENEMY3_FEEDBACK = Game.spritesheet.getSprite(128, 32, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 49, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 49, 16, 16);
	public static BufferedImage FLOWER = Game.spritesheet.getSprite(0, 128, 16, 16);
	

	public double x;
	public double y;
	public double z;
	protected int width;
	protected int height;
	
	public int depth;	//Camadas de renderizacoa
	
	protected List <Node> path;
	
	private BufferedImage sprite;
	
	public int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		//PADRAO DO TAM DAS MASKS
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	//COMPARACAO CAMADAS RENDER
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
			//O primeiro da lista eh renderizado primeiro e o ultimo eho ultimo
			// ex: depth = 10, vai renderizar primeiro que um depth = 1
			
			@Override
			public int compare(Entity n0, Entity n1){
				if(n1.depth < n0.depth) 
					return +1;
				
				if(n1.depth > n0.depth)
					return -1;
				return 0;
					
			}
		};
	
	//GET E SETTERS -> Sao metodos de acesso para as var privadas
	//serve para proteger o as variaveis de alteracoes
	
	public void setMask(int maskx,int masky, int mwidth,int mheight) {
		//COSO QUEIRA MUDAR O PADRAO DE TAM
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
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
	
	//METODO PARA CALCULAR DISTANCIA ENTRE ENTITIES
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		
		//retorna a distancia usando angulos para um direcao
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));	//retorna angulos	
	}
	
	//COLISAO
	public boolean isColliding(int xnext, int ynext) {
		//lembre-se metodo de colisao usando retangulos
		Rectangle enemyCurrent =  new Rectangle(xnext + maskx, ynext + masky, mwidth, mheight);
		
		for(int i = 0; i < Game.enemies3.size(); i++) {
			Enemy3 e = Game.enemies3.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy =  new Rectangle(e.getX() + maskx, e.getY() + masky, mwidth, mheight);
			if(enemyCurrent.intersects(targetEnemy)) {
			return true;
			}
			
		}
		return false;
	}
	
	
	//SEGUIR O CAMINHO DO ALGORITMO AStar
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				//target eh caminho
				Vector2i target = path.get(path.size() - 1).tile;	//pega o ultimo item da list e depois o tile
				//xprev = x;
				//yprev = y;
				if(x < target.x * 16) {	//16 pq as sprites do tile ta em 16X16
					x++;
				}else if(x > target.x * 16) {
					x--;
				}
				
				if(y < target.y * 16) {
					y++;
				}else if( y > target.y * 16) {
					y--;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {	// se os dois forem iguais, significa que achou o caminho
					path.remove(path.size() - 1);	//remove da lista e comeca outra ansalie de caminho
				}
			}
		}
	}
	
	public static boolean isColliding(Entity e1,Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		if(e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}
		return false;
	}
	
	public void render (Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x,this.getY() - Camera.y,this.getWidth(),this.getHeight(), null);
		
		//Testar mascaras
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y, mwidth, mheight);
	}
}
