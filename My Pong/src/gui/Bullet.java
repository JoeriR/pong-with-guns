package gui;

import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
	protected enum Direction { straight, up, down }

	protected Direction direction = Direction.straight;

	public Bullet(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.direction = Direction.straight;
	}

	public Bullet(double x, double y, double width, double height, Direction direction) {
		super(x, y, width, height);
		this.direction = direction;
	}
}
