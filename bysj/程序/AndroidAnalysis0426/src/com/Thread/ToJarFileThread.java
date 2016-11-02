package com.Thread;

import java.io.IOException;
import com.main.GUI;

/**
 * @author  Robin
 * @date 2016年4月20日 下午3:19:28 
 * @version 1.0 
 */

public class ToJarFileThread extends Thread{
	String cmd;
	public ToJarFileThread(String cmd){this.cmd=cmd;}
	public void run(){
		Process process=null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			GUI.jarCallFunction();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
