package com.Thread;

import com.main.GUI;
import com.util.ZipTest;

import java.io.IOException;

/**
 * @author  Robin
 * @date 2016年4月20日 下午2:07:14 
 * @version 1.0 
 */

public class UnZipFileThread extends Thread {
	String srcDir, destDir;
	int type;
	public UnZipFileThread(String src,String dest,int type){
		this.srcDir=src;
		this.destDir=dest;
		this.type=type;
	}
	public void run(){
		try {
			ZipTest.unZipFiles(srcDir, destDir);
			if(type==0)
				GUI.zipCallFunction();
			else if(type==1)
				GUI.classCallFunction();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
