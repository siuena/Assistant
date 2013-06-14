package com.coofee.assistant.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileUtil {

	public static StringBuffer readFromFile(String filePath, String charset) {
		return readFromFile(new File(filePath), charset);
	}
	/**
	 * 读取文件中的全部内容, 并转换为字符串. 使用GBK编码.
	 * @param filePath 文件的路径
	 */
	public static StringBuffer readFromFile(String filePath) {
		return readFromFile(new File(filePath), "GBK");
	}
	
	/**
	 * 读取文件中的全部内容, 并转换为字符串.
	 * @param filePath 文件的路径
	 */
	public static StringBuffer readFromFile(File f, String charset) {
		if (f.exists() && f.isFile()) {
			StringBuffer content = new StringBuffer();
			try {
				BufferedReader in = 
						new BufferedReader(
								new InputStreamReader(
										new FileInputStream(f), charset));
				String line = null;
				try {
					while ((line = in.readLine()) != null) {
						content.append(line);
					}
					in.close();
					return content;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * 读取文件中的全部内容, 并转换为字符串. 使用GBK编码.
	 * @param filePath 文件的路径
	 */
	public static StringBuffer readFromFile(File f) {
		return readFromFile(f, "GBK");
	}
	
	/**
	 * 将content的内容写入到名称为fileName的文件中.
	 * @param fileName 文件的全名, 带路径.
	 * @param charset content的字符编码
	 * @param content 要写入的内容
	 */
	public static void writeToFile(String fileName, String charset,
			String content) {
		try {
			BufferedWriter out = 
					new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(fileName), charset),
									8192);
			out.write(content);
			out.flush();
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
