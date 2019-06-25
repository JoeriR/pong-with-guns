package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class BottomMenu extends GridPane implements EventHandler<ActionEvent> {
	private Label score1, score2;
	private VBox buttonBox;
	private Button newGameButton, optionsButton;
	
	// graphic variables
	private Insets padding = new Insets(10);
	private double buttonSpacing = 5;
	
	public BottomMenu(Label score1, Label score2){
		this.score1 = score1;
		this.score2 = score2;
		
		prepareAndFillButtonBox();
		makeNodesLookNice();
		fillThisBorderPane();
	}
	
	private void prepareAndFillButtonBox(){
		buttonBox = new VBox();
		
		newGameButton = new Button("New Game");
		optionsButton = new Button("Options");
		
		newGameButton.setOnAction(this);
		optionsButton.setOnAction(this);
		
		buttonBox.getChildren().addAll(newGameButton, optionsButton);
	}
	
	private void makeNodesLookNice(){
		buttonBox.setSpacing(buttonSpacing);
		
		buttonBox.setPadding(padding);
		score1.setPadding(padding);
		score2.setPadding(padding);
		
		
	}
	
	private void fillThisBorderPane(){
		this.add(score1, 0, 0);
		this.add(buttonBox, 1, 0);
		this.add(score2, 2, 0);
		
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == newGameButton){
			System.out.println("newGameButton not implemented yet");
		}
		
		if (event.getSource() == optionsButton){
			System.out.println("otionsButton not implemented yet");
		}
		
	}
}
