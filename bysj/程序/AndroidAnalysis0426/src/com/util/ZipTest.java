package com.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author  Robin
 * @date 2016年4月19日 下午3:07:55 
 * @version 1.0 
 */

public class ZipTest {
	
/*	public static void main(String[] args) throws IOException{
        File[] sources = new File[]{new File("d:/Flipagram_1.89.apk"),new File("d:/test.py")};  
        File destination = new File("d:/robin.zip");  
        ZipFiles(destination,sources);   
        
         //File zipFile = new File("d:/robin.zip");
         String zipPath="d:/robin.zip";
        String path = "d:/robin---/";  
        unZipFiles(zipPath, path);   
	}*/
	/**
	 * 压缩文件-由于out要在递归调用外,所以封装一个方法用来
	 * 调用ZipFiles(ZipOutputStream out,String path,File... srcFiles)
	 * @param zip
	 * @param path
	 * @param srcFiles
	 * @throws IOException
	 * @author isea533
	 */
	public static void ZipFiles(File zip,File... srcFiles) throws IOException{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
		ZipTest.ZipFiles(out,srcFiles);
		out.close();
		System.out.println("*****************压缩完毕*******************");
	}
	/**
	 * 压缩文件-File
	 * @param zipFile  zip文件
	 * @param srcFiles 被压缩源文件
	 * @author isea533
	 */
	public static void ZipFiles(ZipOutputStream out,File... srcFiles){
	
		byte[] buf = new byte[1024];
		try {
			for(int i=0;i<srcFiles.length;i++){
				if(srcFiles[i].isDirectory()){
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if(!srcPath.endsWith("/")){
						srcPath+="/";
					}
					out.putNextEntry(new ZipEntry(srcPath));
					ZipFiles(out,files);
				}
				else{
					FileInputStream in = new FileInputStream(srcFiles[i]);
					System.out.println(srcFiles[i].getName());
					out.putNextEntry(new ZipEntry(srcFiles[i].getName()));
					int len;
					while((len=in.read(buf))>0){
						out.write(buf,0,len);
					}
					out.closeEntry();
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 

	public static void unZipFiles(String srcDir,String descDir)throws IOException{
		File zipFile=new File(srcDir);
		File pathFile = new File(descDir);
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for(Enumeration entries = zip.entries();entries.hasMoreElements();){
			ZipEntry entry = (ZipEntry)entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
			//判断路径是否存在,不存在则创建文件路径
			int index=outPath.lastIndexOf('/')!=-1?outPath.lastIndexOf('/'):outPath.lastIndexOf('\\');
			File file = new File(outPath.substring(0, index)); // 此处有异常要处理
			if(!file.exists()){
				file.mkdirs();
			}
			//判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if(new File(outPath).isDirectory()){
				continue;
			}
			//输出文件路径信息
			//System.out.println(outPath);
			
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while((len=in.read(buf1))>0){
				out.write(buf1,0,len);
			}
			in.close();
			out.close();
			}
		//System.out.println("******************解压完毕********************");
	}
}
 

