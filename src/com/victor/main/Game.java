package com.victor.main;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	public Game() {
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		intFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
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
		
	} 
	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		//Otimizacao de render, para veriguar o Buffer
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(new Color (0, 0, 0));
		g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		
		//Render do game
		//Graphics2D g2 = (Graphics2D) g;
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
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
		
		while (isRunning ) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
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
	
	
	
	/*

	//TECLADO (KeyListener) Config
	
	@Override
	public void keyPressed(KeyEvent e) {
		//LOGICA para precionar
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = true;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = true;
		}
		//Botao para atirar
		if(e.getKeyCode() == KeyEvent.VK_Q) {
			player.shoot = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			player.up = true;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.down = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//LOGICA para soltar botao
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			player.up = false;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.down = false;
		}
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
		
	}
	
	*/
}