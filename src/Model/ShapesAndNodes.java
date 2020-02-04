package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.NodeGestures;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ShapesAndNodes {
	PannableCanvas canvas;
	NodeGestures nodeGestures;
	List<Label> labels;
	List<Circle> circles;
	List<Rectangle> rectangles;
	
	public ShapesAndNodes(PannableCanvas canvas, double windowWidth, double windowHeight) {
		this.canvas = canvas;
		nodeGestures = new NodeGestures(canvas, windowWidth, windowHeight);
		labels = new ArrayList<>();
		circles = new ArrayList<>();
		rectangles = new ArrayList<>();
	}
	
	public void createLabel(String text, double translateX, double translateY) {
		Label label = new Label(text);
		label.setTranslateX(translateX);
        label.setTranslateY(translateY);
        label.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        label.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
        
        labels.add(label);
	}
	
	public void createCircle(double translateX, double translateY, double radius, Color color) {
		Circle circle = new Circle(translateX, translateY, radius);
		circle.setStroke(color);
        circle.setFill(color.deriveColor(1, 1, 1, 0.5));
        circle.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        circle.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
        
        circles.add(circle);
	}
	
	public void createRectangle(double width, double height, double translateX, double translateY, Color color) {
		Rectangle rect = new Rectangle(100,100);
        rect.setTranslateX(translateX);
        rect.setTranslateY(translateY);
        rect.setStroke(color);
        rect.setFill(color.deriveColor(1, 1, 1, 0.5));
        rect.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        rect.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
        
        rectangles.add(rect);
	}

	public List<Label> getLabels() {
		return labels;
	}

	public List<Circle> getCircles() {
		return circles;
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}
	
	public void addChildrenToCanvas() {
		for(Label label : labels) { canvas.getChildren().add(label); }
        for(Circle circle : circles) { canvas.getChildren().add(circle); }
        for(Rectangle rectangle : rectangles) { canvas.getChildren().add(rectangle); }
	}
}
