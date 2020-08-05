package application.control;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import utils.ResUtil;

/**
 * 创建项目界面controller
 * 
 * @author DP
 *
 */
public class CreateProjectController extends BaseController implements Initializable {
	private CreateProjectListener listener;
	@FXML
	JFXButton button_help;
	@FXML
	JFXButton btn_create;
	@FXML
	JFXButton btn_open;
	private DropShadow effectOn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (listener != null) {
			listener.onClearData();
		}
		initButtonEffect();
	}

	private void initButtonEffect() {
		final Transition animation = new Transition() {
			{
				setCycleDuration(Duration.millis(200));
			}

			protected void interpolate(double frac) {
				effectOn.setRadius((double) 12 * (double) frac);
			}
		};
		effectOn = new DropShadow();
		effectOn.setColor(Color.DEEPSKYBLUE);
		effectOn.setRadius(0);

		btn_create.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btn_create.setEffect(effectOn);
				animation.play();
			}
		});
		btn_create.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btn_create.setEffect(null);
			}
		});
		btn_open.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btn_open.setEffect(effectOn);
				animation.play();
			}
		});
		btn_open.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btn_open.setEffect(null);
			}
		});
	}

	public void test() {
	}

	@FXML
	public void createProject() {
		if (listener != null) {
			listener.onCreateProject();
		}
	}

	public void setListener(CreateProjectListener listener) {
		this.listener = listener;
	}

	public interface CreateProjectListener {
		void onCreateProject();

		void onOpenProject();

		void onClearData();

		void onClickHelp();

		void onClickSet();
	}

	@Override
	protected void onClickLeftBtn() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onClickRightBtn() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetBottomBtnsAndTitle() {
		if (title != null) {
			title.setText(ResUtil.gs("software_title"));
		}
	}

	@FXML
	public void openProject() {
		if (listener != null) {
			listener.onOpenProject();
		}
	}

	@FXML
	public void onClickHelp() {
		if (listener != null) {
			listener.onClickHelp();
		}
	}

	@FXML
	public void onClickSetting() {
		if (listener != null) {
			listener.onClickSet();
		}
	}
}
