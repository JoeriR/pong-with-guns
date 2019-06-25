package gui;

//import java.awt.Paint;
import java.util.ArrayList;

import gui.Bullet.Direction;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PongMain extends Application {
	
	private final String versionNumber = "0.6.1";
	
	private int score1 = 0, score2 = 0;
	private int winningScore = 10;

	private Label score1Label = new Label("Player 1 Score: " + score1);
	private Label score2Label = new Label("Player 2 Score: " + score2);

	private Font scoreLabelFont = new Font(20);

	private BottomMenu bottomMenu = new BottomMenu(score1Label, score2Label);
	private HBox healthbarBox = new HBox();

	private HealthBar healthbar1 = new HealthBar(100, 20, winningScore, Color.GREEN, Color.RED);
	private HealthBar healthbar2 = new HealthBar(100, 20, winningScore, Color.GREEN, Color.RED);

	@Override
	public void start(Stage stage) throws Exception {

		score1Label.setFont(scoreLabelFont);
		score2Label.setFont(scoreLabelFont);
		
		healthbarBox.getChildren().add(healthbar1);
		healthbarBox.getChildren().add(healthbar2);
		
		healthbar1.setAlignment(Pos.CENTER_LEFT);
		healthbar2.setAlignment(Pos.CENTER_RIGHT);
		healthbarBox.setSpacing(250);

		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10));

		Scene scene = new Scene(gp, 520, 400);

		Canvas canvas = new Canvas(500, 200);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// gp.add(gamescreen, 0, 0);
		gp.add(healthbarBox, 0, 0);
		gp.add(canvas, 0, 1);
		gp.add(bottomMenu, 0, 2);

		stage.setScene(scene);
		stage.setTitle("Pong with Guns v" + versionNumber + " - by Joeri");
		stage.show();

		ArrayList<String> input = new ArrayList<String>();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();

				// only add once... prevent duplicates
				if (!input.contains(code))
					input.add(code);
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				input.remove(code);
			}
		});

		long startNanoTime = System.nanoTime();

		new AnimationTimer() {

			double multiplier = 5;

			double currentY1;
			double currentY2;
			double size1;
			double size2;

			boolean doInitialise = true;

			long cooldown1 = System.nanoTime();
			long cooldown2 = System.nanoTime();

			Rectangle player1 = new Rectangle(10, 100, 15, 50);
			Rectangle player2 = new Rectangle(canvas.getWidth() - 25, 100, 15, 50);

			BoundingBox leftscreensideBounds   = new BoundingBox(0, 0, 0, canvas.getHeight());
			BoundingBox rightscreensideBounds  = new BoundingBox(canvas.getWidth(), 0, 0, canvas.getHeight());
			BoundingBox topscreensideBounds    = new BoundingBox(0, 0, canvas.getWidth(), 0);
			BoundingBox bottomscreensideBounds = new BoundingBox(0, canvas.getHeight(), canvas.getWidth(), 0);

			ArrayList<Bullet> bullets = new ArrayList<Bullet>();

			public void handle(long currentNanoTime) {
				
				double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				
				// place to further initialise variables before the game loop starts
				if (doInitialise) {
					player1.setFill(Color.RED);
					player2.setFill(Color.BLUE);

					System.out.println("game objects: initialised!");
					doInitialise = false;
				}

				// HANDLE USER INPUT
				{
				currentY1 = player1.getY();
				currentY2 = player2.getY();
				size1 = player1.getHeight();
				size2 = player2.getHeight();
				Direction bulletDirection1 = Direction.straight;
				Direction bulletDirection2 = Direction.straight;
				boolean fireshotPlayer1 = false;
				boolean fireshotPlayer2 = false;

				for (String s : input) {
					switch (s) {
					// player 1 KeyBindings
					case "W":
						bulletDirection1 = Direction.up;
						if (currentY1 > 0)
							player1.setY(currentY1 - 2);
						break;
					case "S":
						bulletDirection1 = Direction.down;
						if (currentY1 + size1 < canvas.getHeight())
							player1.setY(currentY1 + 2);
						break;
					case "F":
						if (currentNanoTime > cooldown1)
							fireshotPlayer1 = true;
						break;
						
					// player 2 KeyBindings
					case "UP":
						bulletDirection2 = Direction.up;
						if (currentY2 > 0)
							player2.setY(currentY2 - 2);
						break;
					case "DOWN":
						bulletDirection2 = Direction.down;
						if (currentY2 + size2 < canvas.getHeight())
							player2.setY(currentY2 + 2);
						break;
					case "SLASH":
						if (currentNanoTime > cooldown2)
							fireshotPlayer2 = true;
						break;
					}

					if (fireshotPlayer1) {
						double x1 = player1.getX();
						double y1 = player1.getY() + (player1.getHeight() / 2);
						double width1 = player1.getWidth();
						Bullet newBullet1 = new Bullet(x1 + width1, y1 - (width1 / 2), width1, width1, bulletDirection1);
						newBullet1.setFill(player1.getFill());
						bullets.add(newBullet1);

						cooldown1 = currentNanoTime + 1000000000;
						
						System.out.println("currentNanoTime = " + currentNanoTime);
						System.out.println("cooldown1 =       " + cooldown1 + "\n");
					}

					if (fireshotPlayer2) {
						double x2 = player2.getX();
						double y2 = player2.getY() + (player2.getHeight() / 2);
						double width2 = player2.getWidth();
						Bullet newBullet2 = new Bullet(x2 - width2, y2 - (width2 / 2), width2, width2, bulletDirection2);
						newBullet2.setFill(player2.getFill());
						bullets.add(newBullet2);

						cooldown2 = currentNanoTime + 1000000000;
					}
					
					// fixes the bug that caused players to shoot twice when holding up and shoot at the same time
					fireshotPlayer1 = false;
					fireshotPlayer2 = false;

				}
				
			}
				// background image clears canvas
				gc.setFill(Color.WHITE);
				gc.fillRect(0, 0, 500, 200);
				gc.strokeRect(0, 0, 500, 200);
				
				// draw player1 and player2
				gc.setFill(player1.getFill());
				gc.fillRect(player1.getX(), player1.getY(), player1.getWidth(), player1.getHeight());

				gc.setFill(player2.getFill());
				gc.fillRect(player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight());
				
				// draw transparent rectangle when player can shoot
				if (currentNanoTime > cooldown1) {
					double x1 = player1.getX();
					double y1 = player1.getY() + (player1.getHeight() / 2);
					double width1 = player1.getWidth();
					
					Color c = new Color(1, 0, 0, 0.6);
					gc.setFill(c);
					gc.fillRect(x1 + width1, y1 - (width1 / 2), width1, width1);
				}
				
				if (currentNanoTime > cooldown2) {
					double x2 = player2.getX();
					double y2 = player2.getY() + (player2.getHeight() / 2);
					double width2 = player2.getWidth();
					
					Color c = new Color(0, 0, 1, 0.6);
					gc.setFill(c);
					gc.fillRect(x2 - width2, y2 - (width2 / 2), width2, width2);
				}
				
				
				
				// loop through all bullets, 
				// draw them, 
				// then do Collision Detection on them
				for (Bullet bullet : (ArrayList<Bullet>) bullets.clone()) {
					gc.setFill(bullet.getFill());
					gc.fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());

					if (bullet.intersects(player1.getBoundsInLocal()))
						bullet.setFill(player1.getFill());

					else if (bullet.intersects(player2.getBoundsInLocal()))
						bullet.setFill(player2.getFill());

					if (bullet.intersects(topscreensideBounds)) {
						System.out.println("top screen hit, dir: " + bullet.direction);
						bullet.direction = Direction.down;
					}

					else if (bullet.intersects(bottomscreensideBounds)) {
						System.out.println("bottom screen hit, dir: " + bullet.direction);
						bullet.direction = Direction.up;
					}

					if (bullet.intersects(rightscreensideBounds)) {
						score1++;
						healthbar2.minusOne();
						score1Label.setText("Player 1 Score: " + score1);

						bullets.remove(bullet);
						System.out.println("rightscreensideBounds got hit!");
					}

					else if (bullet.intersects(leftscreensideBounds)) {
						score2++;
						healthbar1.minusOne();
						score2Label.setText("Player 2 Score: " + score2);
						
						bullets.remove(bullet);
						System.out.println("leftscreensideBounds got hit!");
					}

					if (bullet.getFill() == player1.getFill())
						bullet.setX(bullet.getX() + 2);
					else if (bullet.getFill() == player2.getFill())
						bullet.setX(bullet.getX() - 2);

					if (bullet.direction == Direction.up)
						bullet.setY(bullet.getY() - 2);

					if (bullet.direction == Direction.down)
						bullet.setY(bullet.getY() + 2);
					
					// Check Winconditions
					if (score1 == winningScore) {
						System.out.println("player 1 wins m8");
						System.exit(0);
					}
					
					if (score2 == winningScore) {
						System.out.println("player 2 wins k");
						System.exit(0);
					}
						

				}
				
				// System.out.println("\nliving bullets: " + bullets.size());
			}
		}.start();

	}

	private void resetGame() {
		score1 = 0;
		score2 = 0;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
