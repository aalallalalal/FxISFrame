package views;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * 利用反射修改tooltip显示时间
 * 
 * @author DP
 *
 */
public class MyToolTip extends Tooltip {
	
	public MyToolTip() {
		this("");
	}

	public MyToolTip(String str) {
		this(str,15000);
	}

	public MyToolTip(String str, int duration) {
		super(str);
		try {
			Field fieldBehavior = this.getClass().getSuperclass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
//			Object objBehavior = fieldBehavior.get(this);
//			Field fieldTimer;
//			fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
//			fieldTimer.setAccessible(true);
//			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
//			objTimer.getKeyFrames().clear();
//			objTimer.getKeyFrames().add(new KeyFrame(new Duration(500)));
			setStyle("-fx-font-size:12;");

			@SuppressWarnings("rawtypes")
			Class behavior = Class.forName("javafx.scene.control.Tooltip$TooltipBehavior");
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor constructor = behavior.getDeclaredConstructor(Duration.class, Duration.class, Duration.class,
					boolean.class);
			constructor.setAccessible(true);
			fieldBehavior.set(behavior,
					constructor.newInstance(new Duration(500), new Duration(duration), new Duration(300), false));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
