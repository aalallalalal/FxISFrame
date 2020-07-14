package application.control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import beans.ImageBean;
import consts.ConstSize;
import javafx.animation.PathTransition;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.ToastUtil;
import utils.ProgressTask.ProgressTask;
import views.MyToolTip;

public class FlightLineController implements Initializable {

	@FXML
	BorderPane root;
	@FXML
	Pane pane_Canvas;
	private double windowX = ConstSize.Second_Frame_Width - 60;
	private double windowY = ConstSize.Second_Frame_Height - 50 - 80;
	private DropShadow dropShadow;
	private Image image = new Image(getClass().getResourceAsStream("/resources/camera.png"), 16, 16, false, false);
	private Image imageFocus = new Image(getClass().getResourceAsStream("/resources/camera-fill.png"), 16, 16, false,
			false);
	private Image imageTarget = new Image(getClass().getResourceAsStream("/resources/target.png"), 25, 25, false,
			false);

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
					//修改数据
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

					drawShapes(gc, xList, yList, xList.length >= yList.length ? yList.length : xList.length, listData,
							analyData);
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
			ObservableList<ImageBean> listData, ArrayList<Double> analyData) {
		//画线
		gc.setStroke(Color.LIGHTSKYBLUE);
		gc.translate(30, 30);
		gc.scale(0.85, 0.85);
		gc.setEffect(dropShadow);
		gc.setLineWidth(3);
		gc.strokePolyline(xList, yList, size);

		//画点
		for (int i = 0; i < size; i++) {
			Label item;
			if (listData.size() > i) {
				item = generateLabel(listData.get(i));
			} else {
				item = generateLabel(null);
			}
			item.setLayoutX((xList[i] - 7) * 0.85 + 30);
			item.setLayoutY((yList[i] - 7) * 0.85 + 30);
			pane_Canvas.getChildren().add(item);
		}

		//画动画
		Polyline polyLine = new Polyline();
		polyLine.setScaleX(0.85);
		polyLine.setScaleY(0.85);
		polyLine.getPoints().addAll(analyData);

		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(25000));
		pathTransition.setPath(polyLine);
		pathTransition.setCycleCount(Timeline.INDEFINITE);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);// 方向

		Label target = new Label();
		target.setPrefWidth(14);
		target.setPrefHeight(14);
		target.setGraphic(new ImageView(imageTarget));
		target.setEffect(dropShadow);

		pane_Canvas.getChildren().addAll(target);

		pathTransition.setNode(target);
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
		System.out.println("scale" + scale);
		if (radioData >= radioWindow) {
			// 用y
			scale = windowY / distY;
		} else {
			// 用X
			scale = windowX / distX;
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
