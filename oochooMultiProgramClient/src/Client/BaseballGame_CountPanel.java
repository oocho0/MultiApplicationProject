package Client;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BaseballGame_CountPanel extends JPanel {
	private int strike;
	private int ball;
	private int out;
	
	public BaseballGame_CountPanel() {
		this.strike = 0;
		this.ball = 0;
		this.out = 0;
	}
	void setData(int strike, int ball, int out) {
		this.strike = strike;
		this.ball = ball;
		this.out = out;
	}
	@Override
	public void paintComponent(Graphics graphic) {
		int plus = 215;
		super.paintComponent(graphic);
		if(strike == 3) {
			graphic.drawString("Home Run!!", 260, 222);
		}else {
			if(strike ==0) {
				graphic.setColor(Color.RED);
				graphic.drawString("S", 30+plus, 222);
				graphic.drawOval(50+plus, 200, 30, 30);
				graphic.drawOval(90+plus, 200, 30, 30);
			}else if(strike == 1) {
				graphic.setColor(Color.RED);
				graphic.drawString("S", 30+plus, 222);
				graphic.fillOval(50+plus, 200, 30, 30);
				graphic.drawOval(90+plus, 200, 30, 30);
			}else {
				graphic.setColor(Color.RED);
				graphic.drawString("S", 30+plus, 222);
				graphic.fillOval(50+plus, 200, 30, 30);
				graphic.fillOval(90+plus, 200, 30, 30);
			}
			if(ball ==0) {
				graphic.setColor(Color.BLUE);
				graphic.drawString("B", 30+plus, 262);
				graphic.drawOval(50+plus, 240, 30, 30);
				graphic.drawOval(90+plus, 240, 30, 30);
				graphic.drawOval(130+plus, 240, 30, 30);
			}else if(ball == 1) {
				graphic.setColor(Color.BLUE);
				graphic.drawString("B", 30+plus, 262);
				graphic.fillOval(50+plus, 240, 30, 30);
				graphic.drawOval(90+plus, 240, 30, 30);
				graphic.drawOval(130+plus, 240, 30, 30);
			}else if(ball == 2) {
				graphic.setColor(Color.BLUE);
				graphic.drawString("B", 30+plus, 262);
				graphic.fillOval(50+plus, 240, 30, 30);
				graphic.fillOval(90+plus, 240, 30, 30);
				graphic.drawOval(130+plus, 240, 30, 30);
			}else {
				graphic.setColor(Color.BLUE);
				graphic.drawString("B", 30+plus, 262);
				graphic.fillOval(50+plus, 240, 30, 30);
				graphic.fillOval(90+plus, 240, 30, 30);
				graphic.fillOval(130+plus, 240, 30, 30);
			}
			if(out == 1) {
				graphic.setColor(Color.ORANGE);
				graphic.drawString("O", 30+plus, 302);
				graphic.fillOval(50+plus, 280, 30, 30);
			}else {
				graphic.setColor(Color.ORANGE);
				graphic.drawString("O", 30+plus, 302);
				graphic.drawOval(50+plus, 280, 30, 30);
			}
		}
	}
}
