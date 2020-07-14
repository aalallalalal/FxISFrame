package consts;

public final class ConstRes {
	// css文件路径
	public static final String CSS_Path = "/application/css/application.css";
	// 含有bar的界面路径,且bar有最小化界面按钮
	public static final String UI_Bar_Path = "/base/fxml/BaseBarFrame.fxml";
	// 含有bar的界面路径,且bar无最小化界面按钮
	public static final String UI_Bar_No_Path = "/base/fxml/BaseBarFrame2.fxml";
	// 含有bar的界面路径,用于dialog类型界面
	public static final String UI_Bar_Dialog_Path = "/base/fxml/MyDialog.fxml";
	// 含有bar的界面路径,用于确认dialog类型界面
	public static final String UI_Bar_Confirm_Dialog_Path = "/base/fxml/ConfirmDialog.fxml";
	// 含有bar的界面路径,用于提示dialog类型界面
	public static final String UI_Bar_Notice_Dialog_Path = "/base/fxml/NoticeDialog.fxml";

	// 经纬度文件提示文本
	public static final String Text_LocationFile_Notice = "两种可选的经纬度读入方式：\n" + "1)从图像中读取经纬度、高度等数据：需要图像携带经纬度信息。\n"
			+ "2)从文件中读取经纬度、高度等数据：选择后缀为.txt或Tetracam 飞行日志文件。\n" + "    .txt文件格式为：(图像名称,纬度,经度,高度)。"
			+ "例：DJI_0006.tif,36.8284518611111,116.570739083333,118";
}
