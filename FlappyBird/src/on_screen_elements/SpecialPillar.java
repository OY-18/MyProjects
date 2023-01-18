package on_screen_elements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import game_operation.Game;

public class SpecialPillar extends Pillar {
	public final static int SCORE = 5;
	
	public POWER currPower;
	
	public static enum POWER { // comfortable way to arrange the powers
		smallerSpace(), biggerWidth(), biggerBird();
	}
	
	@Override
	public int getScore() { return SCORE; }
	@Override
	public int getWidth() { return this.width; }
	@Override
	public int getHeight() { return this.height; }
	
	public SpecialPillar() {
		// initiate the pillar
		this.initPillar();
		
		// randomly select the power
		Random rand = new Random();
		int powerNum = rand.nextInt(2);
		
		switch (powerNum) {
		case 0:
			this.currPower = POWER.smallerSpace;
			break;
		case 1:
			this.currPower = POWER.biggerWidth;
			break;
		}
		this.specialPower();
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(this.x, this.y, this.width, this.height);
		g.setColor(Color.red);
		g.fillRect(this.x, this.y + this.height + this.space, this.width, Game.HEIGHT - this.height - this.space);
//		System.out.println("x: " + this.x );
	}
	
	public void specialPower() {
		if (currPower == POWER.smallerSpace) {
			this.setSpace((int) (0.75 * (double) this.space));
//			System.out.println(this.space);
		}
		else if (currPower == POWER.biggerWidth) {
			this.setWidth((int) (1.25 * (double) this.width) + 1);
//			System.out.println(this.width);
		}
	
	}

	@Override
	public String toString() {
		return "SpecialPillar";
	}
	
}
