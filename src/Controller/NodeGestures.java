package Controller;

import Model.PannableCanvas;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Listeners for making the nodes draggable via left mouse button. Considers if parent is zoomed.
 */
public class NodeGestures {

	private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;
    
    private DragContext nodeDragContext = new DragContext();
    private boolean hasDragFilter = true;
    private double windowWidth;
    private double windowHeight;

    PannableCanvas canvas;

    public NodeGestures( PannableCanvas canvas, double windowWidth, double windowHeight) {
        this.canvas = canvas;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }
    
    public EventHandler<MouseEvent> getOnDragReleasedEventHandler() {
    	return onMouseDragReleasedEventHandler;
    }
    
    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
    	return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {
            // left mouse button => dragging
            if( !event.isPrimaryButtonDown())
                return;

            if(event.getClickCount() == 2) {
            	if(hasDragFilter) {
            		hasDragFilter = false;
            		canvas.removeEventFilter(MouseEvent.MOUSE_DRAGGED, getOnMouseDraggedEventHandler());
            		canvas.removeEventFilter(MouseEvent.MOUSE_RELEASED, getOnDragReleasedEventHandler());
            		canvas.addEventFilter(ScrollEvent.ANY, getOnScrollEventHandler());
            		
            		event.consume();
            		return;
            	} else {
            		hasDragFilter = true;
            		canvas.removeEventFilter(ScrollEvent.ANY, getOnScrollEventHandler());
            		canvas.addEventFilter( MouseEvent.MOUSE_DRAGGED, getOnMouseDraggedEventHandler());
            		canvas.addEventFilter( MouseEvent.MOUSE_RELEASED, getOnDragReleasedEventHandler());
            		event.consume();
            		return;
            	}
            }
            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();

            Node node = (Node) event.getSource();
            
            nodeDragContext.translateAnchorX = node.getTranslateX();
            nodeDragContext.translateAnchorY = node.getTranslateY();
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // left mouse button => dragging
            if( !event.isPrimaryButtonDown())
                return;

            double scale = canvas.getScale();

            Node node = (Node) event.getSource();

            node.setTranslateX(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

        }
    };
    
    private EventHandler<MouseEvent> onMouseDragReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			
			Node node = (Node) event.getSource();
			
			if(event.getSceneX() < windowWidth / 2) {
				node.setTranslateX(0);
			} else {
				node.setTranslateX(windowWidth / 2);
			}
			
			if(event.getSceneY() < windowHeight / 2) {
				node.setTranslateY(0);
			} else {
				node.setTranslateY(windowHeight / 2);
			}
			event.consume();
		}
    };
    
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {
			if(!event.isControlDown()) {
       		 double delta = 1.2;

                double scale = canvas.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;

                if (event.getDeltaY() < 0)
                    scale /= delta;
                else
                    scale *= delta;

                scale = clamp( scale, MIN_SCALE, MAX_SCALE);

                double f = (scale / oldScale)-1;

                double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
                double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));

                canvas.setScale( scale);

                // note: pivot value must be untransformed, i. e. without scaling
                canvas.setPivot(f*dx, f*dy);

                event.consume();
			}
			
		}
    	
    };
    
    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
}
