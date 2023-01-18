package input_listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game_operation.Game;
import on_screen_elements.Bird;

public class KeyListener extends KeyAdapter {
	private Bird b;
	private Game game;

	public KeyListener(Game game, Bird b){
		this.game = game;
		this.b = b;
	}
	
	public void keyPressed(KeyEvent e) {
		if (Game.gameState == Game.STATE.Game) {
			int key = e.getKeyCode();
			// if pressed change the velocity to jump!!
			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
				this.b.setVel(Bird.VELOCITY_JUMP);
			}
		}
		
//		else if (Game.gameState == Game.STATE.EndingScreen) {
//			int key = e.getKeyCode();
//			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
//				game.initialize();
//				this.b = game.getBird();
//				Game.gameState = Game.STATE.Game;
//			}
//		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (Game.gameState == Game.STATE.Game) {
			int key = e.getKeyCode();
			// if released change the velocity to jump
			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
				this.b.setVel(Bird.VELOCITY_FALL);
			}
		}
		
		else if (Game.gameState == Game.STATE.EndingScreen) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ESCAPE) {
				Game.gameState = Game.STATE.Game;
				game.initialize();
				this.b = Game.getMyBird();
			}
		}
	}
}
