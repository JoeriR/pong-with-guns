package gui;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends StackPane {
	private double maxHealth, currentHealth;
	private double width, height;
	private Rectangle backgroundRect, foregroundRect;

	public HealthBar(double width, double height, int maxHealth, Color fgColor, Color bgColor) {
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.width = width;
		this.height = height;

		backgroundRect = new Rectangle(width, height, bgColor);
		foregroundRect = new Rectangle(width, height, fgColor);

		this.getChildren().add(backgroundRect);
		this.getChildren().add(foregroundRect);
		
		this.setPadding(new Insets(10));
	}

	public void minusOne() {
		if (currentHealth > 0) {
			currentHealth -= 1;
			//double widthcalc = width * (maxHealth * (currentHealth/100));
			double widthcalc = width / maxHealth * currentHealth;
			foregroundRect.setWidth(widthcalc);
			
			System.out.println("healthbar stats: \nmaxHealth = " + maxHealth 
					+ "\ncurrentHealth = " + currentHealth 
					+ "\nwidth = " + width 
					+ "\nsetWidth = " + widthcalc);
		}
	}
	
	public void reset() {
		currentHealth = maxHealth;
	}
	
	public void reset(double newMaxHealth) {
		maxHealth = newMaxHealth;
		currentHealth = newMaxHealth;
	}
}
