package com.haiersoft.ccli.common.utils;

import java.io.FileWriter;
import java.io.IOException;

public class MyFileUtils {
	//设置保存路径
	private String filePath="";
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void saveStrToFile(String str) {
		 FileWriter fwriter = null;
		 try {
		  fwriter = new FileWriter(filePath);
		  fwriter.write(str);
		 } catch (IOException ex) {
		  ex.printStackTrace();
		 } finally {
			  try {
			   fwriter.flush();
			   fwriter.close();
			  } catch (IOException ex) {
			   ex.printStackTrace();
			  }
		 }
	}
}
