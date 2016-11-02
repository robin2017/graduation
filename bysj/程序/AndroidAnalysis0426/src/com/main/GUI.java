package com.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.Thread.ToJarFileThread;
import com.Thread.UnZipFileThread;

/**
 * @author Robin
 * @date 2016年4月13日 上午8:29:56
 * @version 1.0
 */
public class GUI extends JFrame {

	// 1.成员类
	private class openL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			// Demonstrate "Open" dialog:
			int rVal = c.showOpenDialog(GUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION)
				choosePathReminder.setText(c.getCurrentDirectory().toString()
						+ File.separator + c.getSelectedFile().getName());
			if (rVal == JFileChooser.CANCEL_OPTION)
				choosePathReminder.setText("未选择文件");
			else{
				zipFile.setEnabled(true);
				toJar.setEnabled(false);
				toClass.setEnabled(false);
				toJarReminder.setText("");
				toClassReminder.setText("");
				zipFileReminder.setText("");
			}
		}
	}

	private class zipL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			File apkFile = new File(choosePathReminder.getText());
			String oldName = apkFile.getName();
			int index = oldName.indexOf("apk");
			if (index == -1) {
				zipFileReminder.setText("请输入apk文件");
				return;
			}
			String newName = oldName.substring(0, index) + "zip";
			String rootPath = apkFile.getParent();
			File newFile = new File(rootPath + File.separator + newName);
			if (!apkFile.renameTo(newFile))
				zipFileReminder.setText("修改失败!");
			String srcDir = newFile.getPath();
			// 赋值给成员变量
			apkFilePath = srcDir.substring(0, srcDir.lastIndexOf('.'));
			apkName=newName.substring(0, newName.indexOf(".zip"));
			String destDir = newFile.getParent() + File.separator
					+ newName.substring(0, newName.indexOf(".zip"))
					+ File.separator;
			zipFileReminder.setText("正在解压，请等待...");
			UnZipFileThread unZip = new UnZipFileThread(srcDir, destDir, 0);
			unZip.start();

		}
	}

	private class jarL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (apkFilePath == null) {
				zipFileReminder.setText("请输入apk文件");
				return;
			}
			String dex2jarbatPath = GUI.class.getResource("/").getPath()
					+ "dex2jar" + '/' + "dex2jar.bat";
			dex2jarbatPath = dex2jarbatPath.substring(1,
					dex2jarbatPath.length());
			String dexPath = apkFilePath + File.separator + "classes.dex";
			String cmd = dex2jarbatPath + " " + dexPath;
			toJarReminder.setText("正在转化，请稍等...");
			ToJarFileThread toJar = new ToJarFileThread(cmd);
			toJar.start();
		}
	}

	private class classL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (apkFilePath == null) {
				zipFileReminder.setText("请输入apk文件");
				return;
			}
			String jarFileName = apkFilePath + File.separator
					+ "classes_dex2jar.jar";
			String zipFileName = jarFileName.substring(0,
					jarFileName.indexOf("jar"))
					+ ".zip";
			File jarFile = new File(jarFileName);
			File zipFile = new File(zipFileName);
			if (!jarFile.renameTo(zipFile)) {
				toClassReminder.setText("修改失败!");
				return;
			}
			String folder = jarFileName
					.substring(0, jarFileName.indexOf("jar")) + File.separator;
			toClassReminder.setText("正在转化，请稍等");
			UnZipFileThread unZip = new UnZipFileThread(zipFileName, folder, 1);
			unZip.start();
			classFolderPath = apkFilePath + File.separator + "classes_dex2";
		}
	}
	private class runL implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String classPath=classFolderPath;
			String result=null;
			int number=0;
			try {
				result=Folder2String.traverseFolder(classPath);
				number=Folder2String.getNumber();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			conclusion.setText(number+result);
		}	
	}
	// 2.成员变量
	private JTextField title=new JTextField(12);
	
	private JButton choosePath = new JButton("选择apk文件");
	private JTextField choosePathReminder = new JTextField(30);

	private JButton zipFile = new JButton("执行解压");
	private static JTextField zipFileReminder = new JTextField(20);

	private static JButton toJar = new JButton("  转jar包  ");
	private static JTextField toJarReminder = new JTextField(20);

	private static JButton toClass = new JButton("转类文件");
	private static JTextField toClassReminder = new JTextField(20);
	
	private static JButton run=new JButton("采集");

	private JTextArea conclusion = new JTextArea(15, 80);
	// 使用的是双反斜，与File.separator一样
 
	private String apkFilePath = null;
	private String classFolderPath = null;
	public static String currentProjectPath=System.getProperty("user.dir");
	public static String apkName=null;
	public static String apkParentPath=null;


	// 3.构造函数
	public GUI() {
		super("AndroidAnalysis");
		// 3.1.组件设置
		title.setText("安卓恶意应用检测");
		title.setBorder(null);
		title.setEditable(false);
		title.setFont(new Font("Serif", 0, 24));
		
		choosePath.addActionListener(new openL());
		zipFile.addActionListener(new zipL());
		toJar.addActionListener(new jarL());
		toClass.addActionListener(new classL());
		run.addActionListener(new runL());

		choosePathReminder.setEditable(false);
		zipFileReminder.setEditable(false);
		toJarReminder.setEditable(false);
		toClassReminder.setEditable(false);
		
		this.zipFile.setEnabled(false);
		this.toClass.setEnabled(false);
		this.toJar.setEnabled(false);

		// 3.2.容器布局设置
		/*
		 * -1----mainPane 
		 * ----2----operationPanel 
		 * -------3----titlePanel
		 * -------3----pathChoose
		 * -------3----zipOperation 
		 * -------3----jarOperation
		 * -------3----classOperation 
		 * -------3----runOperation
		 * ----2----conclusionPanel
		 */
		Container mainPane = this.getContentPane();// 一级容器
		// 第一部分：操作选择
		JPanel operationPanel = new JPanel();// 二级容器
		operationPanel.setLayout(new GridLayout(8, 1, 0, 0));
		
		JPanel titlePanel=new JPanel(); //三级容器
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(title);
		operationPanel.add(titlePanel);

		JPanel pathChoose = new JPanel();// 三级容器
		pathChoose.setLayout(new FlowLayout(FlowLayout.LEFT));// 容器设置布局
		pathChoose.add(choosePath);
		pathChoose.add(choosePathReminder);
		operationPanel.add(pathChoose);// 三级容器添加到二级容器

		JPanel zipOperation = new JPanel();
		zipOperation.setLayout(new FlowLayout(FlowLayout.LEFT));
		zipOperation.add(zipFile);
		zipOperation.add(zipFileReminder);
		operationPanel.add(zipOperation);// 三级容器添加到二级容器

		JPanel jarOperation = new JPanel();
		jarOperation.setLayout(new FlowLayout(FlowLayout.LEFT));
		jarOperation.add(toJar);
		jarOperation.add(toJarReminder);
		operationPanel.add(jarOperation);// 三级容器添加到二级容器

		JPanel classOperation = new JPanel();
		classOperation.setLayout(new FlowLayout(FlowLayout.LEFT));
		classOperation.add(toClass);
		classOperation.add(toClassReminder);
		operationPanel.add(classOperation);// 三级容器添加到二级容器
		
		JPanel runOperation =new JPanel();
		runOperation.setLayout(new FlowLayout(FlowLayout.LEFT));
		runOperation.add(run);
		operationPanel.add(runOperation);// 三级容器添加到二级容器

		// 第二部分：结论显示
		JPanel conclusionPanel = new JPanel();// 二级容器
		conclusionPanel.setLayout(new GridLayout(1, 1, 10, 10));
		JScrollPane jsp = new JScrollPane(conclusion);
		conclusionPanel.add(jsp);

		// 二级容器添加到一级容器
		mainPane.add("North", operationPanel);
		mainPane.add("Center", conclusionPanel);
	}

	// 4.成员函数
	// 4.1 回调函数（被按钮监听器的子线程调用）
	public static void zipCallFunction() {// 此函数不能放在成员类中，否者成员类也得变成static
		zipFileReminder.setText("解压完毕，得到classes.dex文件");
		toJar.setEnabled(true);
	}
	public static void jarCallFunction() {// 此函数不能放在成员类中，否者成员类也得变成static
		toJarReminder.setText("转化完毕，得到classes_dex2jar.jar");
		toClass.setEnabled(true);
	}
	public static void classCallFunction() {// 此函数不能放在成员类中，否者成员类也得变成static
		toClassReminder.setText("转化完毕，得到class文件夹");
	}
 
	
	// 5.主函数
	public static void main(String[] args) {
		GUI gui = new GUI();
		 
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(700, 900);
		gui.setVisible(true);
	}
}
