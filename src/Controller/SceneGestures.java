package Controller;

import java.util.List;

import Model.PannableCanvas;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class SceneGestures {
	// Tracked list of canvas
    private List<PannableCanvas> canvasList;
    // Used to determine quadrants
    private double windowWidth;
    private double windowHeight;
    // Used to determine starting quadrant
    private double startingPositionX;
    private double startingPositionY;
    // The selectedCanvas and swap target canvas will have the same values.
    // selectedCanvas tracking is meant to ensure the selectedCanvas isn't
    // sent back to its starting position instead of the target canvas.
    private PannableCanvas selectedCanvas;

    /**
     * Constructor
     * @param canvasList Tracked list of canvas
     * @param windowWidth Used to determine quadrants
     * @param windowHeight Used to determine quadrants
     */
    public SceneGestures(List<PannableCanvas> canvasList, double windowWidth, double windowHeight) {
        this.canvasList = canvasList;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    // Return Listeners
    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }
    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
    	return onMouseReleasedEventHandler;
    }
    /**
     * Gets the starting position quadrant
     */
    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // If not left click, return.
            if( !event.isPrimaryButtonDown())
                return;
            /**
             * TODO
             * 2 can be replaced by Math.ceil(numberOfCanvas/2)
             * This would also require a different set of if/else
             */
            //
           /*int numberOfCanvas = 2;
           double windowXValue = windowWidth;
           double sceneX = event.getSceneX();
           if(numberOfCanvas == 2) {
        	   while(sceneX < windowXValue) {
            	   startingPositionX = windowXValue;
            	   windowXValue /=2;
               }
           } else {
        	   
           }*/
            
           // Set x-quadrant value
           // If smaller than half the screen
           if(event.getSceneX() < windowWidth / 2) {
        	   startingPositionX = 0;
           } else {
        	   startingPositionX = windowWidth / 2;
           }
           // Set y-quadrant value
           // If smaller than half the screen
           if(event.getSceneY() < windowHeight / 2) {
        	   startingPositionY = 0;
           } else {
        	   startingPositionY = windowHeight/ 2;
           }
            // Find the current selected canvas.
            for(PannableCanvas canvas : canvasList) {
            	if(canvas.getTranslateX() == startingPositionX && canvas.getTranslateY() == startingPositionY) {
            		selectedCanvas = canvas;
            	}
            }
        }

    };
    /**
     * Handles the swapping of canvas
     */
    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			// Tracks dropped quadrant
			double droppedPosX = 0;
			double droppedPosY = 0;
			// Set x-quadrant value
            // If smaller than half the screen
			if(event.getSceneX() < windowWidth / 2) {
				droppedPosX = 0;
            } else {
        	    droppedPosX = windowWidth / 2;
            }
			// Set y-quadrant value
            // If smaller than half the screen
            if(event.getSceneY() < windowHeight / 2) {
        	    droppedPosY = 0;
            } else {
        	    droppedPosY = windowHeight/ 2;
            }
	        // Find out if any canvas other than dropped canvas are in that quadrant
			for(PannableCanvas canvas : canvasList) {
				if(canvas != selectedCanvas &&
						canvas.getTranslateX() == droppedPosX && canvas.getTranslateY() == droppedPosY) {
					canvas.setTranslateX(startingPositionX);
					canvas.setTranslateY(startingPositionY);
				}
			}
			
		}
    };

}
