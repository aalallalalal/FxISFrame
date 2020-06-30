package views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;

public class RoundPBorderPane extends BorderPane {
	Canvas c = new Canvas();
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		clip();
	}

	private void clip() {
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.beginPath();
		gc.arc(getWidth()/2, getHeight()/2, getWidth()/2,  getWidth()/2, 0, 360);
		gc.clip();
	}
}
