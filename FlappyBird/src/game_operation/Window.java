package game_operation;

import javax.swing.JFrame;


public class Window {

	
	private static final long serialVersionUID = -2777505463133663034L;
	private JFrame frame;
	
	public Window(String title,Game game) {
		frame = new JFrame(title);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close window after click on X
		frame.setResizable(false);	// the size of the window can not be changed	
		
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}	
}
