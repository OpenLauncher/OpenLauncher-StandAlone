package openlauncher.util;

import openlauncher.OpenLauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mark on 01/08/15.
 */
public class FileUtils {
	/**
	 * Gets the m d5.
	 *
	 * @param file the file
	 * @return the m d5
	 */
	public static String getMD5(File file) {
		if (!file.exists()) {
			OpenLauncher.logger.error("Cannot get MD5 of " + file.getAbsolutePath() + " as it doesn't exist");
			return "0"; // File doesn't exists so MD5 is nothing
		}
		StringBuffer sb = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}

			byte[] mdbytes = md.digest();

			sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			if (fis != null) {
				fis.close();
			}
		} catch (NoSuchAlgorithmException e) {
			OpenLauncher.logger.logStackTrace(e);
		} catch (FileNotFoundException e) {
			OpenLauncher.logger.logStackTrace(e);
		} catch (IOException e) {
			OpenLauncher.logger.logStackTrace(e);
		}
		return sb.toString();
	}
}
