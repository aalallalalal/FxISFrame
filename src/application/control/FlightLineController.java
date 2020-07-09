package application.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import beans.ImageBean;
import consts.ConstSize;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class FlightLineController implements Initializable {

	@FXML
	BorderPane root;
	@FXML
	BorderPane borderPane_Canvas;
	private double windowX = ConstSize.Second_Frame_Width - 60;
	private double windowY = ConstSize.Second_Frame_Height - 50 - 80;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setData(ObservableList<ImageBean> listData) {
		ArrayList<Double> analyData = analyData(listData);
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
		drawShapes(gc, xList, yList, xList.length >= yList.length ? yList.length : xList.length);
		borderPane_Canvas.setCenter(canvas);
	}

	private void drawShapes(GraphicsContext gc, double[] xList, double[] yList, int size) {
		gc.setStroke(Color.LIGHTSKYBLUE);
		gc.translate(30, 30);
		gc.scale(0.85, 0.85);
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.DARKGREY);
		gc.setEffect(dropShadow);
		gc.setLineWidth(5);
		gc.strokePolyline(xList, yList, size);
		gc.setFill(Color.ORANGE);
		dropShadow.setColor(Color.YELLOW);
		for (int i = 0; i < size; i++) {
			gc.fillOval(xList[i] - 7, yList[i] - 7, 14, 14);
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
