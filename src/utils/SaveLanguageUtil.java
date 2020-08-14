package utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import consts.ConstRes;

public class SaveLanguageUtil {
	private static String path = System.getProperty("user.home")+ConstRes.SOFT_PATH+ "/datas";
	private static String fileName = "Language_Data";

	static {
		checkFile();
	}

	public static int getData() {
		File writeName = checkFile();
		if (!writeName.exists() || writeName.length() == 0) {
			return 0;
		}
		FileInputStream writer;
		int data = 0;
		ObjectInputStream objectinputStream = null;
		try {
			writer = new FileInputStream(writeName);
			objectinputStream = new ObjectInputStream(writer);
			data = (int) objectinputStream.readObject();
			objectinputStream.close();
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectinputStream != null) {
					objectinputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public static void saveData(int i) {
		ObjectOutputStream objectOutputStream = null;
		try {
			File writeName = checkFile();
			FileOutputStream writer = new FileOutputStream(writeName);
			objectOutputStream = new ObjectOutputStream(writer);
			objectOutputStream.writeObject(i);
			objectOutputStream.flush(); // 把缓存区内容压入文件
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static File checkFile() {
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File writeName = new File(folder, fileName); // 相对路径，如果没有则要建立一个新的output.txt文件
		if (!writeName.exists()) {
			try {
				writeName.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File file = new File(path, fileName);
		return file;
	}
}
