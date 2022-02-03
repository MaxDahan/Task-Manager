import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.SwingConstants;

class MyButton extends JButton {

	private Color hoverBackgroundColor;
	private Color pressedBackgroundColor;
	private Color hoverTextColor;
	private Color pressedTextColor;

	public MyButton() {
		this(null);
	}

	public MyButton(String text) {
		super(text);
		super.setContentAreaFilled(false);
	}

	protected void paintComponent(Graphics g) {
		// paints background
		if(getModel().isPressed())
			g.setColor(pressedBackgroundColor);
		else if(getModel().isRollover())
			g.setColor(hoverBackgroundColor);
		else
			g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		// paints text
		if(getModel().isPressed())
			g.setColor(pressedTextColor);
		else if(getModel().isRollover())
			g.setColor(hoverTextColor);
		else
			g.setColor(getForeground());
		
		g.drawString(getText(), 3,
								(int)(this.getHeight()/2 - g.getFontMetrics().getStringBounds(getText(), g).getHeight()/2 + (this.getFont().getSize() * 0.83333)));
		// draws the text
		//super.paintComponent(g);
	}

	public void setContentAreaFilled(boolean b) {
		this.setContentAreaFilled(b);
	}

	public Color getHoverBackgroundColor() {
		return hoverBackgroundColor;
	}

	public void setHoverBackgroundColor(Color hoverBackgroundColor) {
		this.hoverBackgroundColor = hoverBackgroundColor;
	}

	public Color getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}

	public void setPressedBackgroundColor(Color pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}

	public Color getHoverTextColor() {
		return this.hoverTextColor;
	}

	public void setHoverTextColor(Color hoverTextColor) {
		this.hoverTextColor = hoverTextColor;
	}

	public Color getPressedTextColor() {
		return this.pressedTextColor;
	}

	public void setPressedTextColor(Color pressedTextColor) {
		this.pressedTextColor = pressedTextColor;
	}
}