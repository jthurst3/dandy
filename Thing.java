import java.awt.geom.Rectangle2D;

public class Thing{
	Rectangle2D bounds;
	enum ThingType{
		OBSTACLE, FIXEDTOKEN, END
	}
	ThingType type;
	Object data;
	
	public Thing(Rectangle2D r, ThingType t, Object o){
		bounds=r;
		type=t;
		data=o;
	}
}