package game_operation;

//import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.Font;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import game_operation.Game.STATE;
import input_listener.KeyListener;
import input_listener.MouseListener;
import on_screen_elements.Bird;
import on_screen_elements.Pillar;
import on_screen_elements.SpecialPillar;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = WIDTH * 9 / 16;

	public boolean running = false;
	private Thread gameThread;

	private static Bird myBird;

	private boolean exchange = false;
	private long startWait;

	private int max_score = 0;
	private int score = 0;
	
	public static int gameChoice;
	private static ArrayList<Pillar> pillars;
	
	private final static int counterTillPillars = 200; 
	private int counterTillSpecial;
	private int counterTillNextPillar;
	
	private static ArrayList<String> backgrounds;
	private static String folderName = "./src/game_backgrounds";
	private static String currBg;
	
	public static enum STATE { // comfortable way to arrange the game process
		OpeningScreen(), Game(), EndingScreen();
	}
	
	public static STATE gameState;
	
	public Game() {
		canvasSetup();
		initialize();
		new Window("Flappy Bird", this);
		this.addKeyListener(new KeyListener(this, myBird));
		this.addMouseListener(new MouseListener(this));
		// this.setFocusable(true);
		
	}

	public void canvasSetup() { // sets the size of the window
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
	}

	public void initialize() {
		if (Game.gameState != Game.STATE.Game)
			Game.gameState = STATE.OpeningScreen;
//		Game.gameState = STATE.Game;
		Game.myBird = new Bird();
		
		Game.pillars = new ArrayList<Pillar>();
		Game.pillars.add(new Pillar());
//		Game.pillars.add(new Pillar());
//		Game.pillars.add(new Pillar());
//		Game.pillars.add(new SpecialPillar());
		
		Game.gameChoice = 0;
		this.score = 0;
		
		this.counterTillSpecial = 9*counterTillPillars;	// 10 pillars before the special
		this.counterTillNextPillar = counterTillPillars;	// 100 updates until next pillar
		
		// get all background images names
		Game.backgrounds = new ArrayList<String>();
		File folder = new File(folderName);
		File[] allImages = folder.listFiles();
		
		for (File image : allImages) {
			backgrounds.add(image.getName());
		}
		
		// choose one randomly
		Random rand = new Random();
		int index = rand.nextInt(allImages.length);
		currBg = backgrounds.get(index);
		
	}
	
	
	public void start() {
		gameThread = new Thread(this);
		gameThread.start();
		running = true;
	}
	
	public void stop() {
		try {
			gameThread.join();
			running = false;
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOFTicks = 60.0;
		double ns = 1000000000 / amountOFTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			if (running) {
				draw();
//				update();
			}

			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	public void draw() {
		// Initialize drawing tools
		BufferStrategy buffer = this.getBufferStrategy();

		if (buffer == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = buffer.getDrawGraphics();
		
		if (Game.gameState  == STATE.Game) {
			drawBackground(g);
			
			myBird.draw(g);
			
			for (Pillar pillar : pillars) {
				pillar.draw(g);
			}
			
			drawScore(g);
		}
		
		else if(Game.gameState == STATE.EndingScreen) {
			drawBackground(g);
			
			g.setFont(new Font("ariel",10,80));	// prints the name on screen
			g.setColor(Color.ORANGE);
			g.drawString("Flappy Bird", Game.WIDTH / 2 - 205,140);
			
			g.setFont(new Font("ariel",10,51));	// prints the 'Game Over' on screen
			g.setColor(Color.black);
			g.drawString("~The Round is Over~", 265,210);
			g.setFont(new Font("ariel",10,51));	// prints the 'Game Over' on screen
			g.setColor(Color.white);
			g.drawString("~The Round is Over~", 268,210);
			
			g.setFont(new Font("ariel",10,50));	// score
			g.setColor(Color.RED);
			String scoreStr = "Score: " + this.score;	
			g.drawString(scoreStr, 385,400);
			
			g.setFont(new Font("ariel",10,50));	// score
			g.setColor(Color.RED);
			String maxScoreStr = "Max score: " + this.max_score;	
			g.drawString(maxScoreStr, 345,320);
			
			
			g.setFont(new Font("ariel",10,35));	// quit by pressing on Escape
			g.setColor(Color.white);
			g.drawString("(Press Escape to play again)", 290,530);
		}
		else if(Game.gameState == STATE.OpeningScreen) {
			drawBackground(g);
			
			g.setFont(new Font("ariel", 10, 80));	// prints the name on screen
			g.setColor(Color.ORANGE);
			g.drawString("Flappy Bird", 285, 140);
			
			g.setFont(new Font("ariel", 10, 50));	// enter to start the game
			g.setColor(Color.WHITE);
			g.drawRect(Game.WIDTH / 2 - 235, 250, Game.WIDTH / 2 - 60, 70);
			g.drawString("Press Here to Start", Game.WIDTH / 2 - 225,300);

		}
		
		// dispose
		g.dispose();
		buffer.show();
	}
	
	public void update() {
		//System.out.println(Game.gameState == STATE.Game);
		if(Game.gameState == STATE.Game) {
			if (this.exchange) {
				if (System.currentTimeMillis() - this.startWait > 2 * 1000) { // begin new round
					this.exchange = false;
	
				}
			} 
			
			else {
	
				int retValue = Game.myBird.update();
				if (retValue == 1) { // new round
					this.startWait = System.currentTimeMillis();
					this.exchange = true;
					this.initRound();
				}
			}
			
			for (Pillar pillar : pillars) { pillar.update(); } // move the pillars
			
			// check if need to add a pillar
			this.counterTillSpecial--;
			this.counterTillNextPillar--;
			
			if (this.counterTillSpecial == 0) { // check if time to special
				SpecialPillar sp = new SpecialPillar();
				pillars.add(sp); 
				this.counterTillNextPillar = counterTillPillars;
				this.counterTillSpecial = 9*counterTillPillars;
			}
			
			else if (this.counterTillNextPillar == 0) {  // check if time for regular
				pillars.add(new Pillar()); 
				this.counterTillNextPillar = counterTillPillars;
			}
			
			// check if some pillars is out of the screen - remove them from list
			for (Pillar pillar : pillars) {
				if (pillar.getX() < -(Bird.WIDTH + 10)) {
					pillars.remove(pillar);
				}
				break; // if we got to a pillar that in the screen, the rest is also
			}
			
			// check for score
			checkForPoint();
			
			// checks if the game is over
			int gameOver = isGameOver();
			
			if (gameOver != 0) {
				Game.gameState = STATE.EndingScreen;
				if (this.score > this.max_score) { this.max_score = this.score; }
			}
		}
	}
	
	private void initRound() {
		Game.myBird.initBird();
		for (Pillar pillar : pillars) { pillar.initPillar(); }
	}
	
	// This function return the winner index if the game over and 0 else
	private int isGameOver() {
		if (myBird.getY() < 0 || myBird.getY() > HEIGHT - (int) ((float) Bird.WIDTH * 0.5))
			return 1;
		
		for (Pillar pillar : pillars) {
			if (pillar.getX() - (int) ((float) Bird.WIDTH * 0.5) <= myBird.getX() && myBird.getX() <= pillar.getX() + pillar.getWidth()) {
				if (myBird.getY() <= pillar.getHeight() || myBird.getY() + (int) ((float) Bird.WIDTH * 0.5) >= pillar.getHeight() + pillar.getSpace()) {
						return 1;
				}
			}
		}
		return 0;
	}
	
	private void drawBackground(Graphics g) {
//		g.setColor(Color.black);
//		g.fillRect(0, 0, WIDTH, HEIGHT);
//		
//		Graphics2D g2d = (Graphics2D)g;	
//		g2d.setColor(Color.white);
//		g2d.setStroke(new BasicStroke(5));
//		g2d.drawLine(Game.WIDTH/2, 0, Game.WIDTH/2, Game.HEIGHT);
//		
//		
//		// circle in the middle
//		int r = 120;
//		g2d.drawOval(Game.WIDTH/2-r, Game.HEIGHT/2-r, 2*r, 2*r);
//		g2d.setStroke(new BasicStroke(2));
		BufferedImage img;
		try {
			img = ImageIO.read(new File(folderName + "/" + currBg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		g.drawImage(img, 0, 0, this);
		
	}
	
	// this function is responsible for saving the var value in the given range between min and max.
	public static int fixedVar(int var,int min,int max){
		if(var >= max) var = max;
		if(min >= var) var = min;
		return var;
	}
	
	private void handleUpgrades() {
//		double dist;
//		int upgradeX,upgradeY;
//		
//		int ballCenterX = ball.getX() + ball.getDiameter()/2;
//		int ballCenterY = ball.getY() + ball.getDiameter()/2;
//
//		for(Upgrade u: upgrades) {
//			upgradeX = u.getX() + u.getUpgradeDiameter()/2;	//gets the upgrade coordinates
//			upgradeY = u.getY() + u.getUpgradeDiameter()/2;
//			
//			// calculate the distance from the ball
//			dist = Math.sqrt(Math.pow(ballCenterX-upgradeX, 2)+Math.pow(ballCenterY-upgradeY, 2));
//			
//			if(dist <= ball.getDiameter()/2 + u.getUpgradeDiameter()/2) {	// check collision
//				u.applyModification();// Polymorphism
//				
//				upgrades.remove(u);
//				break;
//			}
//		}
	}
	
	
	public void checkForPoint() {
		// check if we passed a pillar - we got a point
		for (Pillar pillar : pillars) {
			// got a point
			if (pillar.getX() + pillar.getWidth() == myBird.getX()) {
				this.score += pillar.getScore();
			}
			else if (pillar.getX() + pillar.getWidth() > myBird.getX()) {
				return;			
			}
		}
	}
	
	
	public void drawScore(Graphics g) {
		String scoreStr;
		scoreStr = "Score: " + this.score;
		g.setColor(Color.black);
		g.setFont(new Font("ariel", 10, 30));
		g.drawString(scoreStr, Game.WIDTH/2 - 72, 40);
		g.setColor(Color.white);
		g.setFont(new Font("ariel", 10, 30));
		g.drawString(scoreStr, Game.WIDTH/2 - 70, 40);
	}


	public static void main(String[] args) {
		new Game();
	}

	public static Bird getMyBird() {
		return myBird;
	}

	public static void setMyBird(Bird myBird) {
		Game.myBird = myBird;
	}

	public static ArrayList<Pillar> getPillars() {
		return pillars;
	}

	public static void setPillars(ArrayList<Pillar> pillars) {
		Game.pillars = pillars;
	}
}
