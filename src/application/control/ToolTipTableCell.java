package application.control;

import javafx.scene.control.TableCell;
import utils.StrUtil;
import views.MyToolTip;

/*
 * 带有tooltip的cell
 */
public class ToolTipTableCell<T> extends TableCell<T, String> {
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && !StrUtil.isEmpty(item)) {
			setText(item);
			MyToolTip tooltip = new MyToolTip(item);
			setTooltip(tooltip);
		} else {
			setText("");
		}
	}
}