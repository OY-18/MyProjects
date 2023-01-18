package on_screen_elements;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game_operation.Game;

public class Bird extends Canvas implements Drawable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int VELOCITY_FALL = 1;
	public final static int VELOCITY_JUMP = -5;
	public final static int VELOCITY_X = 0;
	public final static double ACCLERATION = -0.1;
	
	public final static int WIDTH = 42;
	
	private final int x = (int) ((float) Game.WIDTH * 0.25);
	private int y, vel_y;
	
	public Bird() {
		this.initBird();
	}
	
	public void initBird() {
		this.vel_y = VELOCITY_FALL;
		this.y = (int) ((float) Game.HEIGHT * 0.5);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void draw(Graphics g) {
		// draw the bird
//		g.setColor(Color.ORANGE);	// draw a orange circle
//		g.fillOval(this.x, this.y, WIDTH / 2, WIDTH / 2);
//		g.setColor(new Color(100,100,100));
//		g.drawOval(this.x, this.y, WIDTH / 2, WIDTH / 2);
		BufferedImage img;
		try {
			img = ImageIO.read(new File("./src/bird.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		g.drawImage(img, this.x, this.y, this);
	}
	
	public int getVel() { return this.vel_y; }
	
	public void setVel(int vel) { this.vel_y = vel; }
	
	public int update() {
		this.setY(this.getY() + this.vel_y);
		return 0;
	}

}
