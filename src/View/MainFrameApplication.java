package View;

import java.awt.Label;
import java.util.ArrayList;
import java.util.List;

import Controller.NodeGestures;
import Controller.SceneGestures;
import Model.PannableCanvas;
import Model.ShapesAndNodes;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * This application displays the ability to produce a multi-window manager
 * that snaps children sub-windows into four quadrants. 
 * When a sub-window is dragged and dropped over another sub-window, they 
 * swap places.
 * 
 * @author Steven
 *
 */
public class MainFrameApplication extends Application {
	// The dimensions of the parent window
	public double windowWidth = 1024;
    public double windowHeight = 768;
	// Launch
	public static void main(String[] args) {
        launch(args);
    }
	/**
	 * This method is called by start() and is intended to be a repeatable
	 * canvas builder for the sub-windows.
	 * @param startX The starting X position that the sub-window will snap to
	 * @param startY The starting Y position that the sub-window will snap to
	 * @return canvas - A sub-window built around a sheet
	 */
	private PannableCanvas createCanvas(double startX, double startY) {
		// The canvas to be built
		PannableCanvas canvas = new PannableCanvas(this);
		// Event Handler
		NodeGestures nodeGestures = new NodeGestures(canvas, windowWidth, windowHeight);
		// Set the starting positioning for this canvas
        canvas.setTranslateX(startX);
        canvas.setTranslateY(startY);
        // Gets the starting position of first click, but also handles double click to turn off event handlers
        canvas.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        // Drags the canvas around the parent
		canvas.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
		// Snaps window into a zone. Currently defined by 4 quadrants, will be upgraded to n quadrants.
		canvas.addEventFilter( MouseEvent.MOUSE_RELEASED, nodeGestures.getOnDragReleasedEventHandler());
        
		// Create what the canvas is filled by.
        ShapesAndNodes sheet1 = new ShapesAndNodes(canvas, windowWidth, windowHeight);
        sheet1.createLabel("Draggable node 1", -windowWidth/4 + 50, -windowHeight/4 + Label.HEIGHT + 10);
        sheet1.createLabel("Draggable node 2", 0, 0);
        sheet1.createLabel("Draggable node 3", -windowWidth/4 + 50, windowHeight/4 - Label.HEIGHT - 10);
        sheet1.createCircle(windowWidth/4, windowHeight/4, 50, Color.ORANGE);
        sheet1.createRectangle(100, 100, windowWidth/4 - 50, windowHeight/4 - 50, Color.BLUE);
        // Add creations to canvas
        sheet1.addChildrenToCanvas();
        // Create a grid for the canvas
        canvas.addGrid(this);
		return canvas;
	}
	/**
	 * Adds the event handler to the scene.
	 * @param canvasList Each sub-window (canvas). Used to swap positions in the scene.
	 * @param scene Parent window
	 */
	private void addSceneGestures(List<PannableCanvas> canvasList, Scene scene) {
		// Event handler
		SceneGestures sceneGestures = new SceneGestures(canvasList, windowWidth, windowHeight);
		// Gets the starting quadrant and canvas in that quadrant.
		scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
		// Swaps canvas if necessary
        scene.addEventFilter( MouseEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
	}
	/**
	 * Start the show. This is currently hard-coded to create two canvas.
	 * Future capability would be to add canvas as they are selected.
	 * Instead of adding canvas here, we would add a toolbar on the left.
	 * We would then provide buttons that simulate various types of sheets
	 * that whenever pressed, would send a model to a specific "add'XModel'Canvas".
	 * Each addition would then update the number of quadrants and also the size
	 * of each sub-window.
	 */
    @Override
    public void start(Stage stage) {

        Group group = new Group();

        // create canvas
        PannableCanvas canvas = createCanvas(0, 0);
        PannableCanvas canvas2 = createCanvas(windowWidth/2, 0);
        // Add each canvas to be tracked to a list
        // Handles swapping canvas placement
        List<PannableCanvas> canvasList = new ArrayList<>();
        canvasList.add(canvas);
        canvasList.add(canvas2);
        // Add each canvas to a group that will be added to the scene
        group.getChildren().addAll(canvas, canvas2);

        // create scene which can be dragged and zoomed
        Scene scene = new Scene(group, windowWidth, windowHeight);
        // Track canvas movement
        addSceneGestures(canvasList, scene);

        stage.setScene(scene);
        stage.show();

    }
    
    public double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	public double getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(double windowHeight) {
		this.windowHeight = windowHeight;
	}
}
