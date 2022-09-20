package com.victor.main;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.victor.entities.BulletShoot;
import com.victor.entities.Enemy1;
import com.victor.entities.Enemy2;
import com.victor.entities.Entity;
import com.victor.entities.Player;
import com.victor.graficos.Spritesheet;
import com.victor.graficos.UI;
import com.victor.world.World;

public class Game extends Canvas implements Runnable,KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy1> enemies1;
	public static List<Enemy2> enemies2;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		//JANELA
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		intFrame();
		
		//INICIALIZANDO OBJETOS
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies1 = new ArrayList<Enemy1>();
		enemies2 = new ArrayList<Enemy2>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");	//chamando o arquivo res/spritesheet.png
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16) );/*(0, 0, 16,16) eh onde o Player inicia no mapa e (32, 0, 16, 16) eh a 
		regiao em que esta a imagem do player na spritesheet*/
		entities.add(player);
		world = new World("/map.png");
	}
	
	public void intFrame() {
		frame = new JFrame("Zelda");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main (String [] args) {
		Game game = new Game();
		game.start();
		
	}
	
	
	public void tick () {
		//LOGICA PARA CRIAR ENTIDADES
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
	} 
	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		//Otimizacao de render, para veriguar o Buffer
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		
		g.setColor(new Color (0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Render do game
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		//UI AMMMO 
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Ammo: " + player.ammo, 590, 35);
		bs.show();	
	}


	//GAME LOOP PROFISSIONAL
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus(); 	//Comando para n ter que clicar na janela para se mxer
		
		while (isRunning ) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:" + frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		stop();
	}
	
	//TECLADO
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;	
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT  || e.getKeyCode() == KeyEvent.VK_A){
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S){
			player.down = true;
		}
		if(e.getKeyCode() ==  KeyEvent.VK_X) {
			player.shoot = true;
		}
		
		
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;			
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT  || e.getKeyCode() == KeyEvent.VK_A){
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S){
			player.down = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	
}