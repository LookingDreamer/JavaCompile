package com.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportUtil { 
	public static void toTxt(String insbChannelcarinfo,String filepath){
		filepath="c:";
		byte[] d=insbChannelcarinfo.getBytes();
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(filepath+"/文件名.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.write(d);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//最后都到将流关闭
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
