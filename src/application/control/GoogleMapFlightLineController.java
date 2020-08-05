package application.control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import beans.AMapGeocodingBean;
import beans.ImageBean;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import utils.AnimatorUtil;
import utils.GoogleMapUtil;
import utils.ResUtil;
import utils.ToastUtil;
import utils.ProgressTask.ProgressTask;
import views.MyToolTip;

public class GoogleMapFlightLineController implements Initializable {
	private static final String TIP = ResUtil.gs("flight_tips");
	private static final double FlightPaneWH = 450;
	private static final double FlightDataOffset = 20;

	private final Image camera = new Image(getClass().getResourceAsStream("/resources/camera-fill-normal.png"), 14, 14,
			false, false);
	private final Image cameraFocus = new Image(getClass().getResourceAsStream("/resources/camera-fill-focus.png"), 14,
			14, false, false);
	private final Image imageTarget = new Image(getClass().getResourceAsStream("/resources/flight.png"), 25, 25, false,
			true);
	private final Image imageSwitchOn = new Image(getClass().getResourceAsStream("/resources/icon_switch_on.png"), 35,
			35, false, true);
	private final Image imageSwitchOff = new Image(getClass().getResourceAsStream("/resources/icon_switch_off.png"), 35,
			35, false, true);
	private DropShadow dropShadow;

	@FXML
	BorderPane root;
	@FXML
	ImageView imageView;
	@FXML
	AnchorPane pane;
	@FXML
	GesturePane gesturePane;
	@FXML
	TextArea textArea_tip;
	@FXML
	Pane pane_Canvas;
	@FXML
	GesturePane gesturePaneFlight;
	@FXML
	ImageView btn_switch;

	// 谷歌地图图片
	private Image imageMap;

	// 经纬度数据分析得到的
	private double distX;
	private double distY;
	private double xMin;
	private double yMin;

	private double radioData;
	private double scale;
	private int radioType;

	private boolean isShowFlightPane = true;

	private ArrayList<ImageBean> listData;
	private AMapGeocodingBean aMapGeocodingBean;
	private PathTransition pathTransition;
	@FXML
	TextArea textArea_place;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(1.0);
		dropShadow.setOffsetY(1.0);
		dropShadow.setColor(Color.LIGHTBLUE);
		initGesturePane();
		initTextData();
	}

	public void setData(ArrayList<ImageBean> listData) {
		this.listData = listData;
		if (listData == null || listData.size() == 0) {
			System.out.println("飞机界面数据：" + listData.size());
			ToastUtil.toast(ResUtil.gs("load_data_error"));
		}
		ProgressTask task = new ProgressTask(new ProgressTask.MyTask<double[][]>() {
			private double xMax;
			private double yMax;
			private boolean isReverseY;
			private boolean isReverseX;

			/**
			 * 画东西
			 * 
			 * @param gc
			 * @param xList
			 * @param yList
			 * @param size
			 */
			private void drawShapes(GraphicsContext gc, double[] xList, double[] yList, int size) {
				// 画线
				LinearGradient linearGradient1 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
						new Stop[] { new Stop(0, Color.LIGHTGREEN), new Stop(1, Color.LIGHTSKYBLUE) });
				gc.setStroke(linearGradient1);
				gc.setLineWidth(3);
				gc.setLineJoin(StrokeLineJoin.ROUND);
				gc.strokePolyline(xList, yList, size);

				ArrayList<Double> lineData = new ArrayList<Double>();
				// 画点
				for (int i = 0; i < size; i++) {
					Label item;
					if (listData.size() > i) {
						item = generateLabel(listData.get(i));
					} else {
						item = generateLabel(null);
					}
					item.setLayoutX(xList[i] - item.getLayoutBounds().getMinX() - 7);
					item.setLayoutY(yList[i] - item.getLayoutBounds().getMinY() - 14);
					lineData.add(xList[i]);
					lineData.add(yList[i]);
					pane_Canvas.getChildren().add(item);
				}

				gc.restore();
				// 画动画
				Polyline polyLine = new Polyline();
				polyLine.getPoints().addAll(lineData);
				pathTransition = new PathTransition();
				pathTransition.setDuration(Duration.millis(500 * size));
				pathTransition.setPath(polyLine);
				pathTransition.setCycleCount(Timeline.INDEFINITE);
				pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);// 方向

				Label target = new Label();
				target.setPrefWidth(25);
				target.setPrefHeight(25);
				target.setGraphic(new ImageView(imageTarget));
				DropShadow dropShadowFlight = new DropShadow();
				dropShadowFlight.setRadius(3.0);
				dropShadowFlight.setOffsetX(3.0);
				dropShadowFlight.setOffsetY(3.0);
				dropShadowFlight.setColor(Color.DARKGRAY);
				target.setEffect(dropShadowFlight);

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
				item.setPrefHeight(14);
				item.setPrefWidth(14);
				item.setMaxWidth(14);
				item.setMaxHeight(14);
				Insets insets = new Insets(0);
				item.setPadding(insets);
				item.setGraphic(new ImageView(camera));
//				item.setEffect(dropShadow);
				item.addEventHandler(MouseEvent.MOUSE_ENTERED, new EnteredHandler(item));
				item.addEventHandler(MouseEvent.MOUSE_EXITED, new ExitHandler(item));
				item.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
					private double oldScreenY;
					private double oldScreenX;
					private final double GAP = 5;

					@Override
					public void handle(MouseEvent e) {
						if (e.getEventType() == MouseEvent.MOUSE_PRESSED) { // 鼠标按下的事件
							this.oldScreenX = e.getScreenX();
							this.oldScreenY = e.getScreenY();
						} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) { // 鼠标抬起的事件
							double nowX = e.getScreenX();
							double nowY = e.getScreenY();
							if (Math.abs(oldScreenX - nowX) <= GAP && Math.abs(oldScreenY - nowY) <= GAP) {
								Runtime runtime = Runtime.getRuntime();
								try {
									runtime.exec("cmd /c " + imageBean.getPath());
								} catch (IOException exc) {
									ToastUtil.toast(ResUtil.gs("open_image_error"));
									exc.printStackTrace();
								}
							}
							oldScreenX = nowX;
							oldScreenY = nowY;
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
				sb.append(ResUtil.gs("flight_image_name") + imageBean.getName());
				if ("".equals(imageBean.getLongitudeRef())) {
					sb.append("\n" + ResUtil.gs("flight_image_long") + imageBean.getLongitude());
					sb.append("\n" + ResUtil.gs("flight_image_lat") + imageBean.getLatitude());
				} else {
					sb.append("\n" + ResUtil.gs("flight_image_long") + imageBean.getLongitudeRef() + ":"
							+ imageBean.getLongitude());
					sb.append("\n" + ResUtil.gs("flight_image_lat") + imageBean.getLatitudeRef() + ":"
							+ imageBean.getLatitude());
				}

				return sb.toString();
			}

			/**
			 * 解析数据成为google 地图可用的数据
			 * 
			 * @param listData
			 * @return
			 */
			private void initFlightMapView(double[][] analyData) {
				double canvasW = FlightPaneWH, canvasH = FlightPaneWH;
				if (radioType == 0) {
					// 飞行路线为瘦高，固定高，缩小宽
					canvasW = FlightPaneWH / radioData + 2 * FlightDataOffset;
					canvasH = FlightPaneWH;
				} else {
					// 飞行路线为矮胖，固定宽，缩小高
					canvasW = FlightPaneWH;
					canvasH = FlightPaneWH * radioData + 2 * FlightDataOffset;
				}
				pane_Canvas.setPrefSize(canvasW, canvasH);
				gesturePaneFlight.setPrefSize(canvasW, canvasH);
				gesturePaneFlight.setMaxSize(canvasW, canvasH);
				Canvas canvas = new Canvas(canvasW, canvasH);
				GraphicsContext gc = canvas.getGraphicsContext2D();
				canvas.setLayoutX(0);
				canvas.setLayoutY(0);
				pane_Canvas.getChildren().add(canvas);

				drawShapes(gc, analyData[0], analyData[1],
						analyData[0].length >= analyData[1].length ? analyData[1].length : analyData[0].length);
			}

			/**
			 * 解析出谷歌地图需要的数据
			 * 
			 * @param listData
			 * @return
			 */
			private ArrayList<Double> analyDataToGoogleData(ArrayList<ImageBean> listData) {
				xMax = listData.get(0).getLongitude();
				xMin = listData.get(0).getLongitude();
				yMax = listData.get(0).getLatitude();
				yMin = listData.get(0).getLatitude();
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
				double centerX = (xMax + xMin) / 2;
				double centerY = (yMax + yMin) / 2;
				ArrayList<Double> data = new ArrayList<Double>();
				data.add(centerX);
				data.add(centerY);
				for (ImageBean item : listData) {
					if (item.getLatitude() == 0 && item.getLongitude() == 0) {
						continue;
					}
					double dataX = item.getLongitude();
					double dataY = item.getLatitude();
					if ("S".equals(item.getLatitudeRef())) {
						dataX = -dataX;
						isReverseY = false;
					} else if ("N".equals(item.getLatitudeRef())) {
						isReverseY = true;
					} else {
						if (dataX >= 0) {
							isReverseY = true;
						} else {
							isReverseY = false;
						}
					}

					if ("W".equals(item.getLongitudeRef())) {
						dataY = -dataY;
						isReverseX = true;
					} else if ("E".equals(item.getLongitudeRef())) {
						isReverseX = false;
					} else {
						if (dataY >= 0) {
							isReverseX = false;
						} else {
							isReverseX = true;
						}
					}
					data.add(dataX);
					data.add(dataY);
				}
				distX = xMax - xMin;
				distY = yMax - yMin;
				return data;
			}

			/**
			 * 初始化google 地图
			 * 
			 * @param listData
			 * @return
			 */
			private void initGoogleMapData(ArrayList<ImageBean> listData) {
				ArrayList<Double> analyGoogleData = analyDataToGoogleData(listData);
				System.out.println("analyGoogleData" + analyGoogleData.size());
				if (analyGoogleData != null && analyGoogleData.size() >= 2) {
//					geocodingBean = GoogleMapUtil.getGeocoding(analyGoogleData.get(1), analyGoogleData.get(0));
					aMapGeocodingBean = GoogleMapUtil.getAMapGeocoding(analyGoogleData.get(1), analyGoogleData.get(0));
				}
				imageMap = GoogleMapUtil.getMapImage(analyGoogleData);
			}

			/**
			 * 初始化飞行图 数据
			 * 
			 * @param listData
			 * @return
			 */
			private double[][] initFlightMapData() {
				double[][] afterData = new double[2][];
				try {
					radioData = distY / distX;
					// 1.选择按x还是y
					scale = 0;
					if (radioData >= 1) {
						// 瘦高，固定高
						scale = (FlightPaneWH - FlightDataOffset * 2) / distY;
						radioType = 0;
					} else {
						// 矮胖，固定宽
						scale = (FlightPaneWH - FlightDataOffset * 2) / distX;
						radioType = 1;
					}
					System.out.println("radioData" + radioData + "scale" + scale);
					// 2.重置数据大小
					double[] xList = new double[listData.size()];
					double[] yList = new double[listData.size()];
					for (int i = 0; i < listData.size(); i++) {
						ImageBean item = listData.get(i);
						if (item.getLatitude() == 0 && item.getLongitude() == 0) {
							continue;
						}
						double dataX = (item.getLongitude() - xMin) * scale;
						double dataY = (item.getLatitude() - yMin) * scale;
						if (isReverseX) {
							xList[i] = (distX) * scale - dataX + FlightDataOffset;
						} else {
							xList[i] = dataX + FlightDataOffset;
						}
						if (isReverseY) {
							yList[i] = (distY) * scale - dataY + FlightDataOffset;
						} else {
							yList[i] = dataY + FlightDataOffset;
						}
					}
					afterData[0] = xList;
					afterData[1] = yList;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return afterData;
			}

			@Override
			protected double[][] call() {
				double[][] initFlightMapData = null;
				try {
					// 初始化谷歌地图数据
					initGoogleMapData(listData);
					initFlightMapData = initFlightMapData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return initFlightMapData;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				try {
					// 设置地图地理位置信息
					if (aMapGeocodingBean != null && "1".equals(aMapGeocodingBean.getStatus())) {
						if (aMapGeocodingBean.getRegeocode() != null
								&& !"".equals(aMapGeocodingBean.getRegeocode().getFormatted_address())) {
							textArea_place.setVisible(true);
							// 设置place背景透明
							Region region = (Region) textArea_place.lookup(".content");
							region.setBackground(
									new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
							region.setStyle("-fx-background-color: rgba(56,56,56,0.4)");
							textArea_place.setText(
									ResUtil.gs("flight_geo") + aMapGeocodingBean.getRegeocode().getFormatted_address());
						}
					}

					// 设置tips
					if (imageMap != null) {
						imageView.setImage(imageMap);
						textArea_tip.setVisible(true);
						// 设置tip背景透明
						Region region = (Region) textArea_tip.lookup(".content");
						region.setBackground(
								new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
						region.setStyle("-fx-background-color: rgba(56,56,56,0.4)");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					initFlightMapView(get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				AnimatorUtil.fadeShow(pane, 700);
			}

			@Override
			protected void unSucceeded() {
				super.unSucceeded();
				ToastUtil.toast(ResUtil.gs("load_data_error"));
			}

		}, (Stage) root.getParent().getScene().getWindow());
		task.start();
	}

	/**
	 * 设置放大缩小手势功能
	 */
	private void initGesturePane() {
		gesturePane.setMaxSize(976, 626);
		gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
		gesturePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
					Point2D pivotOnTarget = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
							.orElse(gesturePane.targetPointAtViewportCentre());
					gesturePane.animate(Duration.millis(200)).interpolateWith(Interpolator.EASE_BOTH)
							.zoomBy(gesturePane.getCurrentScale(), pivotOnTarget);
				}
			}
		});
		gesturePaneFlight.setMaxSize(FlightPaneWH, FlightPaneWH);
		gesturePaneFlight.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
	}

	private void initTextData() {
		textArea_tip.setText(TIP);
	}

	@FXML
	public void onClickPaneSwitch() {
		if (isShowFlightPane) {
			if (pathTransition != null) {
				pathTransition.stop();
			}
			gesturePaneFlight.setVisible(false);
			btn_switch.setImage(imageSwitchOn);
		} else {
			if (pathTransition != null) {
				pathTransition.play();
			}
			gesturePaneFlight.setVisible(true);
			btn_switch.setImage(imageSwitchOff);
		}
		isShowFlightPane = !isShowFlightPane;
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
			label.setGraphic(new ImageView(cameraFocus));
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
			label.setGraphic(new ImageView(camera));
		}
	}

}
