package lab.sodino.unziplocalzip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

/**
 * @author jacobwang
 * @date 2011-4-24
 */
public class FileUtils {
    public static boolean writeFile(String filePath, StringBuffer fileName, String str) {

        FileOutputStream out = null;
        File wfileDir = new File(filePath);
        String newFileName = fileName.toString();
        if(fileName!=null){
        	newFileName = fileName.toString().replaceAll("[\\\\/*?<>:\"|]","");
        }
        File wfile = new File(filePath + newFileName);

        boolean ret = true;

        if (!wfileDir.exists()) {
            ret = wfileDir.mkdirs();
        }
        if (wfileDir.exists()) {
            if (!wfile.exists()) {
                try {
                    wfile.createNewFile();
                } catch (IOException e) {
                    ret = false;
                }
            }
            try {
                out = new FileOutputStream(wfile, false);
            } catch (FileNotFoundException e) {
                ret = false;
            }
            try {
                str = str + "\r\n";
                if (out != null)
                    out.write(str.getBytes());
            } catch (IOException e) {
                ret = false;
            }
            try {
                if (out != null)
                    out.flush();
            } catch (IOException e) {
                ret = false;
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                ret = false;
            }
        }
        return ret;
    }

    public static boolean justOnExistFileAndAddSuffix(String filePath, StringBuffer fileName, String fileClass) {
        // 文件已经存在，创建一个新的
        File wfileDir = new File(filePath);
        File wfile = new File(filePath + fileName.toString());
        String suffixStr = null;
        int suffixInt;
        int suffixStart;
        int suffixEnd;
        boolean ret = true;

        if (!wfileDir.exists()) {
            ret = wfileDir.mkdirs();
        }
        if (wfileDir.exists()) {
            if (wfile.exists()) {
                suffixStart = fileName.indexOf(fileClass);
                fileName.insert(suffixStart, "(0)");

                while (wfile.exists()) {

                    suffixStart = fileName.lastIndexOf("(") + 1;
                    suffixEnd = fileName.lastIndexOf(")");
                    suffixStr = fileName.substring(suffixStart, suffixEnd);
                    suffixInt = Integer.parseInt(suffixStr);
                    suffixStr = String.valueOf(suffixInt + 1);
                    fileName.replace(suffixStart, suffixEnd, suffixStr);
                    wfile = new File(filePath + fileName);
                }
            }

        }
        return ret;

    }

    public static void copyFile(File src, File dst) {
    	 FileOutputStream fos = null;
         FileInputStream fis = null;
        try {
            if (dst.exists()) {
                dst.delete();
            }
            fos = new FileOutputStream(dst);
            fis = new FileInputStream(src);
            int len = 0;
            byte[] buffer = new byte[1024 * 100];
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
           
        } catch (IOException e) {
            // e.printStackTrace();
        }
        finally
        {
        	if (fos != null)
        	{
        		try {fos.close();} catch (IOException e) {}
        	}
        	if (fis != null)
        	{
        		try {fis.close();} catch (IOException e) {}
        	}
        }
    }

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     *参数：type: 0:今日流量  1:本月流量
     *    size:流量数值 
     *返回值：转换后的显示字符串
     */
    public static String byteCountToDisplaySize(int type, long size) {
    	if (size == 0 ) {
    		if (type == 0) {  //今日流量
    			return "0.0B";
    		} else if (type == 1) { //本月流量
    			return "0.0K";
    		}
    	}
        String displaySize = null;
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0        
        
        if (size / ONE_GB > 0) {
            float temp = (float) size / ONE_GB;
            String format = df.format(temp);
            
            if (format.endsWith(".0")) {
                displaySize = (int) temp + "G";
            } else {
                displaySize = format + "G";
            }
          
        } else if (size / ONE_MB > 0) {
            float temp = (float) size / ONE_MB;
            
            String format = df.format(temp);
            if (format.endsWith(".0")) {
                displaySize = (int) temp + "M";
            } else {
                displaySize = format + "M";
            }
            
        } else if (size / ONE_KB > 0) {
            int temp =  (int) (size / ONE_KB);            
            displaySize = temp + "K";
        } else {
        	if (type == 0) {
        		displaySize =  df.format(size) + "B";
        	} else if (type == 1) {
        		int temp =  (int) (size / ONE_KB);            
                displaySize = temp + "K";
        	}
        }
    
        return displaySize;
    }

    /**
     * 判断文件是否存在
     * 
     * @param path
     * @return
     */
    public static boolean fileExists(String path) {
        if (path == null) {
            return false;
        }

        File file = new File(path);
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean fileExistsAndNotEmpty(String path){
        if (path == null) {
            return false;
        }
        File file = new File(path);
        if (file != null && file.exists() && file.length() > 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 创建文件
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public static File createFile(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            if (f.getParentFile() != null && !f.getParentFile().exists()) {
                if (f.getParentFile().mkdirs()) {
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
        }
        return f;
    }

    /**
     * 移动文件，注意支持从sd卡移动到sd卡，或者内置存储器到内置存储器
     * 
     * @param fromPath
     * @param toPath
     * @return
     */
    public static boolean moveFile(String fromPath, String toPath) {
        boolean ret = false;
        File from = new File(fromPath);
        if (from.exists()) {
            try {
                File to = FileUtils.createFile(toPath);
                FileUtils.copyFile(from, to);
                from.delete();
                ret = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return ret;
    }
    
    public static boolean rename(String fromPath, String toPath) {
    	if (fromPath == null || toPath == null)
    		return false;
        boolean ret = false;
        File from = new File(fromPath);
        if (from.exists()) {
            File to = new File(toPath);
			ret = from.renameTo(to);
        }
        return ret;
    }

    /**
     * 复制文件，注意支持从sd卡移动到sd卡，或者内置存储器到内置存储器
     * 
     * @param fromPath
     * @param toPath
     * @return
     */
    public static boolean copyFile(String fromPath, String toPath) {
        boolean ret = false;
        File from = new File(fromPath);
        if (from.exists()) {
            try {
                File to = FileUtils.createFile(toPath);
                FileUtils.copyFile(from, to);
                ret = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return ret;
    }

    /**
     * 删除文件
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean ret = true;
        File f = new File(path);
        if (f.exists()) {
            ret = f.delete();
        }
        return ret;
    }
    
    public static void delete(String path, boolean ignoreDir) {
    	if(path == null) return;
    	File file = new File(path);
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        
        String[] fileList = file.list();
        if (fileList == null) {
            return;
        }

        for (String f : fileList) {
            delete(f, ignoreDir);
        }
        // delete the folder if need.
        if (!ignoreDir) file.delete();
    }
    
    public static String unKnownFileTypeMark ="unknown_";
    public static String estimateFileType(String filePath) {
        String fileType = "";
        FileInputStream inputStream = null;
        try {
        	inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[2];
            // 通过读取出来的前两个字节来判断文件类型
            if (inputStream.read(buffer) != -1) {
                fileType = estimateFileType(buffer);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
        	if (inputStream != null)
        	{
        		try {inputStream.close();} catch (IOException e) {}
        	}
        }
        return fileType;
    }
    /**
     * 计算图片文件的类型
     * @param buffer
     * @return
     */
	public static String estimateFileType(byte[] buffer) {
		String fileType = "";
		String filecode = "";
		if(buffer!=null && buffer.length>=2){
			for (int i = 0; i < buffer.length; i++) {
				// 获取每个字节与0xFF进行与运算来获取高位，这个读取出来的数据不是出现负数
				// 并转换成字符串
				filecode += Integer.toString((buffer[i] & 0xFF));
			}
			// 把字符串再转换成Integer进行类型判断
			switch (Integer.parseInt(filecode)) {
			case 7790:
				fileType = "exe";
				break;
			case 7784:
				fileType = "midi";
				break;
			case 8297:
				fileType = "rar";
				break;
			case 8075:
				fileType = "zip";
				break;
			case 255216:
				fileType = "jpg";
				break;
			case 7173:
				fileType = "gif";
				break;
			case 6677:
				fileType = "bmp";
				break;
			case 13780:
				fileType = "png";
				break;
			default:
				fileType = unKnownFileTypeMark + filecode;
			}
		}else{
			fileType = unKnownFileTypeMark + filecode;
		}
		
		return fileType;
	}

    public static boolean isPicFile(String filePath) {
        boolean result = false;
        String fileType = estimateFileType(filePath);
        if (fileType.equals("jpg") || fileType.equals("gif") || fileType.equals("bmp") || fileType.equals("png")) {
            result = true;
        }
        return result;
    }
	//取得文件大小
	static public long getFileSizes(String url){
		String filePath = "";
		FileInputStream fis = null;
        try { 
        	filePath = url;
        	File f = new File(filePath);
	        if (f.exists()) {
	            fis = new FileInputStream(f);
	            int ret = fis.available();
	            return ret;
	        } 
        } catch (Exception e) {
		//	Log.e(LOG_TAG, "get file size failed:" + filePath, e);			
			return 0;
        } finally {
			if(fis != null){
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					// empty
				}
			}
		}
        return 0;
    }
	
	public static int copyDirectory(String fromFilePath, String toFilePath, boolean isDelFrom) {
		// 要复制的文件目录
		File[] currentFiles;
		File fromeFileDir = new File(fromFilePath);
		 
		// 如果不存在则 return出去
		if (!fromeFileDir.exists()) {
			return -1;
		}
		
		// 如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = fromeFileDir.listFiles();

		// 目标目录
		File targetDir = new File(toFilePath);
		// 创建目录
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		// 遍历要复制该目录下的全部文件
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory())// 如果当前项为子目录 进行递归
			{
				copyDirectory(currentFiles[i].getPath() + "/",
						toFilePath + currentFiles[i].getName() + "/", isDelFrom);

			} else// 如果当前项为文件则进行文件拷贝
			{
				File from = new File(currentFiles[i].getPath());
				File to = new File(toFilePath + currentFiles[i].getName());
				if(from != null && to != null && from.exists()){
					if(!to.exists()){
						try {
							to.createNewFile();
							copyFile(from, to);
						} catch (IOException e) {
						}
					}
					if(isDelFrom){
						from.delete();
					}
				}
			}
		}
		return 0;
	}
	
	//一定要在同一个目录下
	public static boolean renameFile(File srcFile, File destFile){
		boolean bRet = true;
		if(destFile.exists()){
			bRet = destFile.delete();
		}
		if(bRet){
			bRet = srcFile.renameTo(destFile);
		}
		return bRet;
	}

	
	/**不适用于读取大文件.*/
	public static String readFileContent(File file){
		String content = null;
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try{
			baos = new ByteArrayOutputStream();
			fis = new FileInputStream(file);
			long length = file.length();
			int count = 0;
			byte[] data = new byte[2048];
			while(count < length){
				int len = fis.read(data);
				baos.write(data,0,len);
				count += len;
			}
			data = baos.toByteArray();
			content = new String(data,"utf-8");
		}catch(IOException ie){
			ie.printStackTrace();
		}finally{
			try{
				if(baos!= null){
					baos.close();
				}
				if(fis != null){
					fis.close();
				}
			}catch(IOException ie){
				ie.printStackTrace();
			}
		}
		return content;
	}
	
	public static String getFileSize(long size) {
		DecimalFormat df = new DecimalFormat("##0.00");
		String fileSize = "";
		double KB = 1024;
		double MB = 1024 * KB;
		double GB = 1024 * MB;
		if (size < KB) {
			fileSize = df.format((double) size) + "B";
		} else if (size < MB) {
			fileSize = df.format(size / KB) + "KB";
		} else if (size < GB) {
			fileSize = df.format(size / MB) + "MB";
		} else {
			fileSize = df.format(size / GB) + "GB";
		}
		return fileSize;
	}
	
	
	public static void uncompressZip(String zipPath, String destDir) throws IOException{
		//do not handle exception
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try{
			Log.d("ANDROID_LAB", "zipPath is:"+",destDir is:"+destDir);
			fis = new FileInputStream(zipPath);
			bis = new BufferedInputStream(fis);
			zis = new ZipInputStream(bis);
			File rootDir = new File(destDir);
			rootDir.mkdirs();
			while (true){
				ZipEntry entry = zis.getNextEntry();
				if (entry == null)
					break;
				if(entry.isDirectory()){
					continue;
				}
				String fileName = entry.getName();
				Log.d("ANDROID_LAB", "fileName is:"+fileName);
				int fileSeparatorIndex = fileName.lastIndexOf(File.separatorChar);
				if(fileSeparatorIndex == fileName.length()-1){
					throw new IllegalArgumentException("file name must not be empty,fileName is:"+fileName);
				}
				String absoluteParentDir = null;
				String realFileName = null;
				if(fileSeparatorIndex<=0){
					absoluteParentDir = destDir;
					if(fileSeparatorIndex<0){
						realFileName = fileName;
					}else{
						realFileName = fileName.substring(1);
					}
				}else{
					String parentDir = fileName.substring(0,fileSeparatorIndex);
					if(parentDir.charAt(0) == File.separatorChar){
						absoluteParentDir = destDir+parentDir;
					}else{
						absoluteParentDir = destDir+File.separatorChar+parentDir;
					}
					realFileName = fileName.substring(fileSeparatorIndex+1);
				}
				File target = new File(absoluteParentDir, realFileName);
				if(!target.getParentFile().exists()){
					target.getParentFile().mkdirs();
				}
				target.createNewFile();
				int size = -1;
				byte[] buffer = new byte[2048];
				fos = new FileOutputStream(target);
				bos = new BufferedOutputStream(fos, buffer.length);

				while ((size = zis.read(buffer, 0, buffer.length)) != -1){
					bos.write(buffer, 0, size);
				}
				bos.flush();
				bos.close();
				fos.close();
			}
		}
		finally
		{
			if (zis != null){
				try{
					zis.close();
				}catch(IOException ioe){

				}
			}
			if(bis != null){
				try{
					bis.close();
				}catch(IOException ioe){

				}
			}
			if (fis != null){
				try{
					fis.close();
				}catch(IOException ioe){

				}
			}
			if(fos != null){
				try{
					fos.close();
				}catch(IOException ioe){

				}
			}
			if(bos != null){
				try{
					bos.close();
				}catch(IOException ioe){

				}
			}
		}
	}
	
	public static void deleteDirectory(String dirStr){
		if(dirStr == null || dirStr.trim().length() == 0){
			return;
		}
		File rootDir = new File(dirStr);
		File[] childDirList = rootDir.listFiles();
		if(childDirList != null && childDirList.length>0){
			for(int i=0;i<childDirList.length;i++){
				if(childDirList[i].isDirectory()){
					deleteDirectory(childDirList[i].getAbsolutePath());
				}else{
					childDirList[i].delete();
				}
			}
		}
		rootDir.delete();
	}

	/**
	 * 将byte[]写入file中去。<br/>
	 * @param appendDate 是否将日期时间构建在文件名中。<br/>
	 * 原:filename.txt→filename.txt.2013.07.09.12.00.13
	 * **/
	public static boolean pushData2File(String filePath,byte[]data,boolean appendDate){
		boolean result = false;
		if(filePath == null || filePath.length() == 0 || data == null || data.length == 0){
			return result;
		}
		if(appendDate){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.CHINA);
			Date date = new Date(System.currentTimeMillis());
			String timeStr = sdf.format(date);
			filePath = filePath + "." + timeStr;
		}
		File file = new File(filePath);
		FileOutputStream fos = null;
		try{
			File parent = file.getParentFile();
			if(parent.exists() == false){
				parent.mkdirs();
			}
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			result = true;
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}catch(IOException ie){
			ie.printStackTrace();
		}finally{
			try{
				if(fos != null){
					fos.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
}