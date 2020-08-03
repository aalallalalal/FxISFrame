package utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLanguageUtil {
	
	public static int getData() {
		String path = System.getProperty("user.dir");
		File writeName = new File(path, "language"); // 相对路径，如果没有则要建立一个新的output.txt文件
		if (!writeName.exists() || writeName.length() == 0) {
			return 0;
		}
		FileInputStream writer;
		int data = 0;
		try {
			writer = new FileInputStream(writeName);
			ObjectInputStream objectinputStream = new ObjectInputStream(writer);
			data = (int) objectinputStream.readObject();
			objectinputStream.close();
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static void saveData(int i) {
		try {
			String path = System.getProperty("user.dir");
			File writeName = new File(path, "language"); // 相对路径，如果没有则要建立一个新的output.txt文件
			if (!writeName.exists()) {
				writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			}
			FileOutputStream writer = new FileOutputStream(writeName);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(writer);
			objectOutputStream.writeObject(i);
			objectOutputStream.flush(); // 把缓存区内容压入文件
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
