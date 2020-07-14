package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import beans.ImageBean;
import beans.ProjectBean;

public class GpsUtil {

	public static final String Imagename = "Imagename";// 图片名key
	public static final String GPSLongitude = "GPSLongitude";// 经度key
	public static final String GPSLatitude = "GPSLatitude";// 纬度key
	public static final String GPSAlitude = "GPSAlitude";// 高度key

	/**
	 * 解析location文件
	 * 
	 * @param proj
	 */
	public static ArrayList<HashMap<String, String>> analysingGps(ProjectBean proj) {
		String locationPath = proj.getProjectLocationFile();

		if ("".equals(locationPath)) {
			return new ArrayList<HashMap<String, String>>();
		}

		ArrayList<String> arrayList = new ArrayList<>();

		FileReader fr = null;
		BufferedReader bf = null;
		try {
			fr = new FileReader(locationPath);
			bf = new BufferedReader(fr);
			String str;
			// 按行读取字符串
			while ((str = bf.readLine()) != null) {
				arrayList.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<HashMap<String, String>> res = null;
		if (locationPath.endsWith(".txt") || locationPath.endsWith("TXT")) {
			try {
				res = divideTxt(arrayList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (locationPath.endsWith(".GPS") || locationPath.endsWith(".gps")) {
			try {
				res = divdeGPS(arrayList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}
		return res;
	}

	private static ArrayList<HashMap<String, String>> divdeGPS(ArrayList<String> arrayList) throws Exception {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (String item : arrayList) {
			HashMap<String, String> map = new HashMap<String, String>(3);
			String[] split = item.split(",");
			if (split.length < 5) {
				continue;
			}
			split[1] = ddmmDeal(split[1]);
			split[2] = ddmmDeal(split[2]);

			map.put(Imagename, split[0]);
			map.put(GPSLatitude, split[1]);
			map.put(GPSLongitude, split[2]);
			map.put(GPSAlitude, split[4]);
			list.add(map);
		}
		return list;
	}

	/**
	 * 针对DDMM.MMMM格式的转换
	 */
	private static String ddmmDeal(String ddmm) {
		String s1 = "", s2;
		double d2 = 0;
		try {
			int indexDivide = ddmm.indexOf(".") - 2;
			s1 = ddmm.substring(0, indexDivide);
			s2 = ddmm.substring(indexDivide);
			d2 = Double.parseDouble(s2) / 60f;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s1 + d2;
	}

	private static ArrayList<HashMap<String, String>> divideTxt(ArrayList<String> arrayList) throws Exception {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (String item : arrayList) {
			HashMap<String, String> map = new HashMap<String, String>(3);
			String[] split = item.split(",");
			if (split.length < 4) {
				continue;
			}
			map.put(Imagename, split[0]);
			map.put(GPSLatitude, split[1]);
			map.put(GPSLongitude, split[2]);
			map.put(GPSAlitude, split[3]);
			list.add(map);
		}
		return list;
	}

	/**
	 * 将解析出来的数据，与imagebean列表数据对应
	 * 
	 * @param analysingGps
	 * @param listData2
	 */
	public static ArrayList<ImageBean> setImageDataFromFile(ArrayList<HashMap<String, String>> analysingGps,
			ArrayList<ImageBean> listData2) {
		if (analysingGps == null || analysingGps.size() == 0 || listData2 == null || listData2.size() == 0) {
			return listData2;
		}
		try {
			Iterator<ImageBean> iterator = listData2.iterator();
			while (iterator.hasNext()) {
				ImageBean item = iterator.next();
				String name = item.getName();
				for (HashMap<String, String> mapItem : analysingGps) {
					String imageName = mapItem.get(Imagename);
					if (name.equals(imageName)) {
						item.setLatitude(Double.parseDouble(mapItem.get(GPSLatitude)));
						item.setLongitude(Double.parseDouble(mapItem.get(GPSLongitude)));
						item.setHeight(mapItem.get(GPSAlitude));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listData2;
	}

}
