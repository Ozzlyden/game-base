package com.victor.main;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.victor.entities.BulletShoot;
import com.victor.entities.Enemy1;
import com.victor.entities.Enemy2;
import com.victor.entities.Enemy3;
import com.victor.entities.Entity;
import com.victor.entities.Npc;
import com.victor.entities.Player;
import com.victor.graficos.Spritesheet;
import com.victor.graficos.UI;
import com.victor.world.Camera;
import com.victor.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy1> enemies1;
	public static List<Enemy2> enemies2;
	public static List<Enemy3> enemies3;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf"); 	//fomte personalizada
	public Font newfont;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	//CUT SCENE
	public static int prohibited = 1;	//entrada
	public static int start = 2;	//comecar
	public static int playing = 3; 	//jogando
	public static int statusScene = prohibited;		//status da cena
	public int timeScene = 0, maxTimeScene = 60*2;	//tempo animacao cut scene
	
	public Menu menu;
	
	public int xx, yy;
	public BufferedImage lightmap, icon;	
	
	public int[] pixels;	//pixels da imagem
	public int [] lightMapPixels;
	public static int[] minimapaPixels;
	
	public static BufferedImage minimapa;
	
	//TESTANDO COLISAO AVANCADA
	public BufferedImage sprite1, sprite2;
	public int x1 = 30, y1 = 90;
	public int x2 = 100, y2 = 100;
	public int[]pixels1;
	public int[]pixels2;
	
	public Npc npc;
	
	public boolean saveGame = false;
	
	public int mx, my;	//posicao mouse
	
	public Game() {
		
		//CHAMANDO BIBLIOTECAS
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//FULLSCREEN
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		
		//JANELA
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		intFrame();
		
		//COLISAO AVANCADA(pixels collision)
		/*
		try {
			sprite1 = ImageIO.read(getClass().getResource("/sprite_test_collision1.png"));
			sprite2 = ImageIO.read(getClass().getResource("/sprite_test_collision2.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pixels1 = new int[sprite1.getWidth() * sprite1.getHeight()];	//mask	
		sprite1.getRGB(0, 0, sprite1.getWidth(), sprite1.getHeight(), pixels1, 0, sprite1.getTileWidth());
		
		pixels2 = new int[sprite2.getWidth() * sprite2.getHeight()];	
		sprite2.getRGB(0, 0, sprite2.getWidth(), sprite2.getHeight(), pixels2, 0, sprite2.getTileWidth());
		*/
		
		//INICIALIZANDO OBJETOS
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//LIGHT
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(),lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();	//Pega os pixels na var "image" para manipulacao
	
		//Instanciando Entities
		entities = new ArrayList<Entity>();
		enemies1 = new ArrayList<Enemy1>();
		enemies2 = new ArrayList<Enemy2>();
		enemies3 = new ArrayList<Enemy3>();
		bullets = new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");	//chamando o arquivo res/spritesheet.png
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16) );/*(0, 0, 16,16) eh onde o Player inicia no mapa e (32, 0, 16, 16) eh a 
		regiao em que esta a imagem do player na spritesheet*/
		entities.add(player);
		world = new World("/level1.png");
		
		minimapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt) minimapa.getRaster().getDataBuffer()).getData();
		
		//CARREGANDO FONTE
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f); 	//tam font
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		npc = new Npc(32, 32, 16, 16,spritesheet.getSprite(48, 64, 16, 16));
		entities.add(npc);
		
		menu = new Menu();
	}
	
	public void intFrame() {
		frame = new JFrame("Game Base");
		frame.add(this);
		//frame.setUndecorated(true);	//vai tirar as barras da janela
		frame.setResizable(false);
		frame.pack();
		
		//ICONE DA JANELA
		Image icon = null;
		try {
			icon = ImageIO.read(getClass().getResource("/icon.png"));	//tentar buscar a imagem
		}catch (IOException e) {
			e.printStackTrace();
		}
		//CURSO MOUSE NO GAME
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image icon_cursor = toolkit.getImage(getClass().getResource("/icon_mouse.png"));	//pegando a imagem
		Cursor c = toolkit.createCustomCursor(icon_cursor, new Point(0, 0), "img");	//criando o cursor
		
		frame.setCursor(c);
		frame.setIconImage(icon);	//troca o icon do java pelo seu
		frame.setAlwaysOnTop(true);
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
		//Sound.Clips.music1.loop();
		Game game = new Game();
		game.start();
		
	}
	
	
	public void tick () {
		if(gameState == "NORMAL") {
			//xx++;
			
			//SAVE
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level", "vida"};
				int[] opt2 = {this.CUR_LEVEL, (int) player.life};
				Menu.saveGame(opt1, opt2, 10);
				System.out.print("Jogo foi salvo");
			}
		this.restartGame = false;
		
		//MODO PLAYING DEPOIS CUT SCENE
		if(Game.statusScene == Game.playing) {
			
		//LOGICA PARA CRIAR ENTIDADES
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		//TIRO
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		}else {
			//INICIO CUT SCENE
			if(Game.statusScene == Game.prohibited) {
				if(player.getX() < 100) {
					player.x ++;
				}else {
					System.out.println("Entrada do Game finalizado");
					Game.statusScene = Game.start;	//transicao
				}
			//COMECO CUT SCENE
			}else if(Game.statusScene == Game.start) {
				timeScene++;
				System.out.println("Comecando");
				if(timeScene == maxTimeScene) {
					Game.statusScene = Game.playing;	//transicao
				}
			}
		}
		
		//NEXT LEVEL
		if(enemies1.size() == 0 && enemies2.size() == 0 && enemies3.size() == 0) {
			CUR_LEVEL ++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "level" + CUR_LEVEL + ".png";
			World.restarGame(newWorld);
		}
		
		}else if(gameState == "GAME_OVER") {
			//ANIMCAO TEXTO
			framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) 
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
				
			}
			
			//PRESS ENTER
			if(restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restarGame(newWorld);
			}
		}else if(gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
		
		//TESTANDO COLISAO AVANCADO
		/*
		x1++;
		if(this.isCollidingPerfect(x1, y1, x2, y2, pixels1, pixels2, sprite1, sprite2)) {
			System.out.println("ESTAO COLIDINDO");
		}
		*/
	} 
	

	//EXEMPLO DE MANIPULACAO DE PIXELS
	public void drawRectangleExemple(int xoff, int yoff) {
		//32 eh o numero de pixels, 32px por 32px
		for(int xx = 0; xx < 32; xx ++) {
			for(int yy = 0; yy < 32; yy++) {
				int xOff = xx + xoff;	//posicionamento do retangulo
				int yOff = xx + yoff;
				if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)	//verificar se ta dando negativo
					continue;
				pixels[xOff + (yOff * WIDTH)] = 0xff0000;	//cor do retangulo
			}
		}
	}

	
	//SISTEMA DE ILUMINACAO
	public void applyLight() {
		
		for(int xx = 0; xx < Game.WIDTH; xx++) {
			for (int yy = 0; yy < Game.HEIGHT; yy ++) {
				if(lightMapPixels[xx + (yy * Game.WIDTH)] == 0xffffffff) {
					int pixel = Pixel.getLightBlend(pixels[xx + yy * WIDTH], 0x808080, 0);	//0x808080 eh a opacidade
					pixels[xx + (yy * Game.WIDTH)] = pixel;
				}
			}
		}
	}
	
	
	//COLISAO AVANCADA(pixels Perfect)
	public boolean isCollidingPerfect (int x1, int y1, int x2, int y2, int pixels1[], int[] pixels2, BufferedImage sprite1, BufferedImage sprite2) {
		for(int xx1 = 0; xx1 < sprite1.getWidth(); xx1 ++) {
			for(int yy1 = 0; yy1 < sprite1.getHeight(); yy1 ++) {
				for(int xx2 = 0; xx2 < sprite2.getWidth(); xx2 ++) {
					for(int yy2 = 0; yy2 < sprite2.getHeight(); yy2 ++) {
						int pixelAtual1 = pixels1[xx1 + yy1 * sprite1.getWidth()];
						int pixelAtual2 = pixels2[xx2 + yy2 * sprite2.getWidth()];
						if(pixelAtual1 == 0x00ffffff || pixelAtual2 == 0x00ffffff) {	//se as posicoes do pixel da image(sprite) for transparente
							continue;	//nao faz nada
						}
						if(xx1 + x1 == xx2 + x2 & yy1 + y1 == yy2 + y2) {	//verificacao para ver se realmente estao colidindo
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	//RENDER PIXELS PERFECT
	public void renderPixelsCollision(Graphics g) {
		g.drawImage(sprite1, x1, y1, null);
		g.drawImage(sprite2, x2, y2, null);
	}
	
	//UI AMMO
	public void uiAmmo(Graphics g) {
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Ammo: " + player.ammo, 590, 35);
	}
	
	//UI AMMO
	public void messageNpc(Graphics g) {
		if(Npc.showMessage) {
			g.setFont(new Font("Arial",Font.BOLD,11));
			g.setColor(Color.white);
			g.drawString(Npc.frases[0],70 , 90);
		}
	}
	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		//OTIMIZACAO DE RENDER, para veriguar o Buffer
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		
		g.setColor(new Color (0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//RENDER DO GAME
		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);	//Organizar camadas de render
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		//applyLight(); 	//efeito de luz
		//renderPixelsCollision(g);
		
		ui.render(g);
		npc.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		
		//drawRectangleExemple(xx, yy);
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);	//modo janela
		//fullscreen 
		//g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null); 
		
		uiAmmo(g);
		messageNpc(g);
		
		/*	
		//CASO QUEIRA USAR A FONT
		g.setFont(newfont);
		g.setColor(Color.red);
		g.drawString("teste de font", 250, 50);
		*/
		
		//TELA GAME OVER
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,200)); //Opacidade
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			
			g.setFont(new Font("arial", Font.BOLD, 40));
			g.setColor(Color.white);
			g.drawString("GAME OVER ",(WIDTH*SCALE) / 2 - 100,(HEIGHT*SCALE) / 2 - 0);
			g.setFont(new Font("arial", Font.BOLD, 30));
			if(showMessageGameOver)		//animacao texto
				g.drawString("PRESS ENTER PARA RESET ",(WIDTH*SCALE) / 2 - 175,(HEIGHT*SCALE) / 2 + 90);
			
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		
		/*
		//EXEMPLO ROTACIONAMENTO MOUSE
		Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(my - 200+25, mx - 200+25);
		g2.rotate(angleMouse, 200+25, 200+25);	//para fica no meio do retangulo (50 por 50)
		g.setColor(Color.blue);
		g.fillRect(200, 200, 50, 50);
		*/
		
		World.renderMiniMap();
		g.drawImage(minimapa,618, 377,World.WIDTH * 5, World.HEIGHT * 5, null);
		
		//CUT SCENE
		if(Game.statusScene == Game.start) {
			g.drawString("O jogo esta para comecar", 230, 30);
	
		}
		
		bs.show();	
	}


	//GAME LOOP PROFISSIONAL
	@Override
	public void run() {
		
		requestFocus();	//foco na janela do game ao iniciar
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
		
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			player.jump = true;			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;	
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT  || e.getKeyCode() == KeyEvent.VK_A){
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S){
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() ==  KeyEvent.VK_X) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() ==  KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() ==  KeyEvent.VK_ESCAPE) {	//ESCAOE = Esc
			gameState = "MENU";
			menu.pause = true;
		}
		
		if(e.getKeyCode() ==  KeyEvent.VK_SPACE) {
			if(gameState == "NORMAL")
			this.saveGame = true;
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

	//MOUSE
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3);	//divide por 3 por causa da scale
		player.my = (e.getY() / 3);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
		
	}
	
}