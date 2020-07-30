package application.control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import beans.ImageBean;
import consts.ConstSize;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.ToastUtil;
import utils.ProgressTask.ProgressTask;
import views.MyToolTip;

public class FlightLineController implements Initializable {

	private static final double Scale = 0.85;
	private static final double InitTransX = 30, InitTransY = 30;
	@FXML
	BorderPane root;
	@FXML
	Pane pane_Canvas;
	private double windowX = ConstSize.Second_Frame_Width - 80;
	private double windowY = ConstSize.Second_Frame_Height - 50 - 80;
	private DropShadow dropShadow;
	private Image image = new Image(getClass().getResourceAsStream("/resources/camera-fill-normal.png"), 14, 14, false,
			false);
	private Image imageFocus = new Image(getClass().getResourceAsStream("/resources/camera-fill-focus.png"), 14, 14,
			false, false);
	private Image imageTarget = new Image(getClass().getResourceAsStream("/resources/flight.png"), 25, 25, false, true);
	private double centerOffsetX, centerOffsetY;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.DARKGREY);
	}

	/**
	 * 设置数据
	 * 
	 * @param listData
	 */
	public void setData(ObservableList<ImageBean> listData) {
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<ArrayList<Double>>() {
			@Override
			protected ArrayList<Double> call() throws Exception {
				return analyData(listData);
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				try {
					// 修改数据
					ArrayList<Double> analyData = get();
					double[] xList = new double[analyData.size() / 2];
					double[] yList = new double[analyData.size() / 2];
					for (int i = 0; i < analyData.size(); i++) {
						if (i % 2 == 0) {
							xList[i / 2] = analyData.get(i);
						} else {
							yList[i / 2] = analyData.get(i);
						}
					}

					Canvas canvas = new Canvas(windowX, windowY);
					GraphicsContext gc = canvas.getGraphicsContext2D();
					canvas.setLayoutX(0);
					canvas.setLayoutY(0);
					pane_Canvas.getChildren().add(canvas);

					drawShapes(gc, xList, yList, xList.length >= yList.length ? yList.length : xList.length, listData);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}, (Stage) root.getParent().getScene().getWindow());
		task.start();
	}

	/**
	 * 画线路,添加点，启动动画
	 * 
	 * @param gc
	 * @param xList
	 * @param yList
	 * @param size
	 * @param listData
	 * @param analyData
	 */
	private void drawShapes(GraphicsContext gc, double[] xList, double[] yList, int size,
			ObservableList<ImageBean> listData) {
		// 画线
		LinearGradient linearGradient1 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0, Color.LIGHTGREEN), new Stop(1, Color.LIGHTSKYBLUE) });
		gc.setStroke(linearGradient1);
		gc.save();
//		gc.translate(InitTransX + centerOffsetX, InitTransY + centerOffsetY);
		gc.translate(InitTransX, InitTransY);
		gc.scale(Scale, Scale);
		gc.setLineWidth(3);
		gc.strokePolyline(xList, yList, size);
//		gc.setEffect(dropShadow);

		ArrayList<Double> lineData = new ArrayList<Double>();
		// 画点
		for (int i = 0; i < size; i++) {
			Label item;
			if (listData.size() > i) {
				item = generateLabel(listData.get(i));
			} else {
				item = generateLabel(null);
			}
			item.setPadding(new Insets(0));
			item.setLayoutX((xList[i] - 3 + InitTransX) * Scale);
			item.setLayoutY((yList[i] - 11 + InitTransY) * Scale);
			lineData.add((xList[i] - 7 + InitTransX) * Scale);
			lineData.add((yList[i] - 7 + InitTransY) * Scale);
			pane_Canvas.getChildren().add(item);
		}

		gc.restore();
		// 画动画
		Polyline polyLine = new Polyline();
		polyLine.setTranslateX(25 / 2 * Scale);
		polyLine.setTranslateY(25 / 2 * Scale);
		polyLine.getPoints().addAll(lineData);

		PathTransition pathTransition = new PathTransition();

		pathTransition.setDuration(Duration.millis(500 * size));
		pathTransition.setPath(polyLine);
		pathTransition.setCycleCount(Timeline.INDEFINITE);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);// 方向

		Label target = new Label();
		target.setPrefWidth(25);
		target.setPrefHeight(25);
		target.setGraphic(new ImageView(imageTarget));
		target.setEffect(dropShadow);

		pane_Canvas.getChildren().addAll(target);

		pathTransition.setNode(target);
		pathTransition.setInterpolator(Interpolator.LINEAR);
		pathTransition.play();
	}

	/**
	 * 生成点
	 * 
	 * @param imageBean
	 * @return
	 */
	private Label generateLabel(ImageBean imageBean) {
		Label item = new Label();
		item.setPrefWidth(14);
		item.setPrefHeight(14);
		item.setGraphic(new ImageView(image));
		item.setEffect(dropShadow);
		item.addEventHandler(MouseEvent.MOUSE_ENTERED, new EnteredHandler(item));
		item.addEventHandler(MouseEvent.MOUSE_EXITED, new ExitHandler(item));
		item.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec("cmd /c " + imageBean.getPath());
				} catch (IOException e) {
					ToastUtil.toast("打开图片失败！");
					e.printStackTrace();
				}
			}
		});
		MyToolTip myToolTip = new MyToolTip(generateToolTipInfo(imageBean));
		item.setTooltip(myToolTip);
		return item;
	}

	/**
	 * 初始化tooltip内容
	 * 
	 * @param imageBean
	 * @return
	 */
	private String generateToolTipInfo(ImageBean imageBean) {
		if (imageBean == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder("");
		sb.append("图像名称:" + imageBean.getName());
		if ("".equals(imageBean.getLongitudeRef())) {
			sb.append("\n图像经度:" + imageBean.getLongitude());
			sb.append("\n图像纬度:" + imageBean.getLatitude());
		} else {
			sb.append("\n图像经度:" + imageBean.getLongitudeRef() + ":" + imageBean.getLongitude());
			sb.append("\n图像纬度:" + imageBean.getLatitudeRef() + ":" + imageBean.getLatitude());
		}

		return sb.toString();
	}

	/**
	 * 鼠标移入监听
	 * 
	 * @author DP
	 */
	class EnteredHandler implements EventHandler<Event> {
		private Label label;

		public EnteredHandler(Label label) {
			this.label = label;
		}

		@Override
		public void handle(Event event) {
			label.setGraphic(new ImageView(imageFocus));
		}
	}

	/**
	 * 鼠标移出监听
	 * 
	 * @author DP
	 */
	class ExitHandler implements EventHandler<Event> {
		private Label label;

		public ExitHandler(Label label) {
			this.label = label;
		}

		@Override
		public void handle(Event event) {
			label.setGraphic(new ImageView(image));
		}
	}

	private ArrayList<Double> analyData(ObservableList<ImageBean> listData) {
		// 1.找出max，min的经纬度。
		// 1.1经度的max,min
		double xMax = listData.get(0).getLongitude(), xMin = listData.get(0).getLongitude();
		// 1.2纬度的max，min
		double yMax = listData.get(0).getLatitude(), yMin = listData.get(0).getLatitude();
		// 1.3获取
		for (ImageBean item : listData) {
			if (item.getLatitude() == 0 && item.getLongitude() == 0) {
				continue;
			}
			if (xMax < item.getLongitude()) {
				xMax = item.getLongitude();
			}
			if (xMin > item.getLongitude()) {
				xMin = item.getLongitude();
			}
			if (yMax < item.getLatitude()) {
				yMax = item.getLatitude();
			}
			if (yMin > item.getLatitude()) {
				yMin = item.getLatitude();
			}
		}
		// 2 最大最小的差距
		double distX = xMax - xMin, distY = yMax - yMin;
		// 4.radio
		double radioData = distY / distX;
		double radioWindow = windowY / windowX;
		// 5.选择按x还是y
		double scale = 0;
		if (radioData >= radioWindow) {
			// 用y
			scale = windowY / distY;
			centerOffsetX = (windowX - distX) / 2;
			centerOffsetY = 0;
		} else {
			// 用X
			scale = windowX / distX;
			centerOffsetY = (windowY - distY) / 2;
			centerOffsetX = 0;
		}
		// 6.重置数据大小
		ArrayList<Double> afterData = new ArrayList<Double>();
		for (ImageBean item : listData) {
			if (item.getLatitude() == 0 && item.getLongitude() == 0) {
				continue;
			}
			double dataX = (item.getLongitude() - xMin) * scale;
			double dataY = (item.getLatitude() - yMin) * scale;
			afterData.add(dataX);
			afterData.add(dataY);
		}
		return afterData;
	}

}
