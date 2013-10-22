package lab.sodino.unziplocalzip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

public class ZipUtil {
	public static final int BUFFER = 4096;
	public static void unzip4Asset(Context context,String assetsFileName,String targetDir) throws RuntimeException {
		String strEntry; 
		if(null == assetsFileName){
			return;
		}
		  ZipInputStream zis =null;
		try {
			File dir = new File(targetDir);
			File[] files = dir.listFiles();
			if(null != files){
				for (File file : files) {
					file.delete();
				}
			}
			
			BufferedOutputStream dest = null;
			zis = new ZipInputStream(new BufferedInputStream(context.getAssets().open(assetsFileName)));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
			    FileOutputStream fos = null;
			    try {
					int count;
					byte data[] = new byte[BUFFER];
					strEntry = entry.getName();
					File entryFile = new File(targetDir + strEntry);
					File entryDir = new File(entryFile.getParent());
					if (!entryDir.exists()) {
						entryDir.mkdirs();
					}
					fos = new FileOutputStream(entryFile);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}

				} catch (Exception ex) {
//					ex.printStackTrace();
				} finally{
					try { // 在里面catch住部分sdk版本下的重复关闭outputStream带来的ioException,以免跳出while循环
						if (dest != null) {
							dest.flush();
							dest.close();
						}
						if (fos != null) {
							fos.flush();
							fos.close();
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
			
		} catch (Exception cwj) {
//			cwj.printStackTrace();
		} finally{
		    if(zis!=null){
		    try {
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		    }
		    
		}
	}
	public static void unzip(Context context,String filePath,String targetDir) throws RuntimeException {
		String strEntry;
		if(null == filePath){
			return;
		}
		File fileZip = new File(filePath);
		if(fileZip.exists() == false){
			return;
		}
		ZipInputStream zis =null;
		try {
			File dir = new File(targetDir);
			File[] files = dir.listFiles();
			if(null != files){
				for (File file : files) {
					file.delete();
				}
			}
			
			BufferedOutputStream dest = null;
			zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(filePath)));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				FileOutputStream fos = null;
				try {
					int count;
					byte data[] = new byte[BUFFER];
					strEntry = entry.getName();
					File entryFile = new File(targetDir + strEntry);
					File entryDir = new File(entryFile.getParent());
					if (!entryDir.exists()) {
						entryDir.mkdirs();
					}
					fos = new FileOutputStream(entryFile);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					
				} catch (Exception ex) {
//					ex.printStackTrace();
				} finally{
					try { // 在里面catch住部分sdk版本下的重复关闭outputStream带来的ioException,以免跳出while循环
						if (dest != null) {
							dest.flush();
							dest.close();
						}
						if (fos != null) {
							fos.flush();
							fos.close();
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
			
		} catch (Exception cwj) {
//			cwj.printStackTrace();
		} finally{
			if(zis!=null){
				try {
					zis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
