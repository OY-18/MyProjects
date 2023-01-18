package input_listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game_operation.Game;
import game_operation.Game.STATE;


public class MouseListener extends MouseAdapter {
	private Game game;
	
	public  MouseListener(Game g) {
		this.game = g;
	}
	
	private boolean isMouseInRect(int xpos, int ypos, int x, int y, int w, int h) {
		return x < xpos && xpos < x+w && y < ypos && ypos < y+h; 
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int xpos = e.getX();
		int ypos = e.getY();
		
		if(Game.gameState == STATE.OpeningScreen) {
			if(isMouseInRect(xpos, ypos,Game.WIDTH / 2 - 235, 250, Game.WIDTH / 2 - 60, 70)) {
				game.gameState = STATE.Game;
			}
		}
		
	}
}
