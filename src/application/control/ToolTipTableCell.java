package application.control;

import javafx.scene.control.TableCell;
import views.MyToolTip;

/*
 * 带有tooltip的cell
 */
public class ToolTipTableCell<T> extends TableCell<T, String> {
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		setText(item);
		if (!empty && !"".equals(item)) {
			MyToolTip tooltip = new MyToolTip(item);
			setTooltip(tooltip);
		}
	}
}