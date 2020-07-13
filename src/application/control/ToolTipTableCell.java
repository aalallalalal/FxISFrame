package application.control;

import java.lang.reflect.Field;

import beans.ProjectBean;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/*
 * 带有tooltip的cell
 */
public class ToolTipTableCell extends TableCell<ProjectBean, String> {
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		setText(item);
		if (!empty && !"".equals(item)) {
			Tooltip tooltip = new Tooltip(item);
			try {
				Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
				fieldBehavior.setAccessible(true);
				Object objBehavior = fieldBehavior.get(tooltip);
				Field fieldTimer;
				fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
				fieldTimer.setAccessible(true);
				Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
				objTimer.getKeyFrames().clear();
				objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			setTooltip(tooltip);
		}
	}
}