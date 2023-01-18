package on_screen_elements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import game_operation.Game;

public class Pillar implements Drawable {
	
	protected int space;
	protected int height;
	protected int width;
	public final static int SCORE = 1;

	protected int x;
	protected int y;
	
	public Pillar() {
		this.initPillar();
	}
	
	public int getX() { return x; }

	public void setX(int x) { this.x = x; }

	public int getY() { return y; }

	public void setY(int y) { this.y = y; }
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public int getSpace() { return this.space; }
	
	public int getScore() { return SCORE; }
	
	public void setSpace(int s) { this.space = s; }
	public void setWidth(int w) { this.width = w; }
	
	public void initPillar() {
		this.setX((int) ((float) Game.WIDTH * 1.25));
		this.setY(0);
		this.space = 2 * Bird.WIDTH;
		this.width = Bird.WIDTH + 10;
		Random rand = new Random();
		double min = 0.25, max = 0.75;
		double randD = min + (max - min) * rand.nextDouble();
		//System.out.println(randD);
		this.height = (int) (randD * Game.HEIGHT); // the position of the space
		//System.out.println(this.height);
	}


	@Override
	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(this.x, this.y, this.width, this.height);
		g.setColor(Color.green);
		g.fillRect(this.x, this.y + this.height + this.space, this.width, Game.HEIGHT - this.height - this.space);
//		System.out.println("x: " + this.x );
	}
	
	public void update() {
//		System.out.println("x update: " + this.x );

		this.x = (this.x - 2);
	}

	@Override
	public String toString() {
		return "Pillar";
	}
}
