package openlauncher.util;

import openlauncher.OpenLauncher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadUtils {

	public static JFrame frame;
	public static JProgressBar progressBar;

	public static void downloadFile(String url, File target, String name, String md5) throws IOException {
		frame = new JFrame("Openlauncher Downloader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = frame.getContentPane();
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder("Downloading " + name + "...");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 100);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int sx = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int sy = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(sx, sy);
		frame.setVisible(true);

		URL dl = new URL(url);
		File fl = new File(target, name);
		String x = null;
		OutputStream os = null;
		InputStream is = null;
		ProgressListener progressListener = new ProgressListener();
		try {
			os = new FileOutputStream(fl);
			is = dl.openStream();

			DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
			dcount.setListener(progressListener);

			// this line give you the total length of source stream as a String.
			// you may want to convert to integer and store this value to
			// calculate percentage of the progression.
			progressBar.setMaximum(Integer.parseInt(dl.openConnection().getHeaderField("Content-Length")));

			// begin transfer by writing to dcount, not os.
			IOUtils.copy(is, dcount);

		} catch (Exception e) {
			e.printStackTrace();
			frame.setVisible(false);
		} finally {
			if (os != null) {
				os.close();
				frame.setVisible(false);
			}
			if (is != null) {
				is.close();
				frame.setVisible(false);
			}
		}
		if (!md5.isEmpty()) {
			String fileMD = FileUtils.getMD5(target);
			if (!md5.equals(fileMD)) {
				OpenLauncher.logger.error("Invaild MD5!" + target.getAbsolutePath());
				//TODO do something? try again?
			} else {
				//file fine
			}
		} else {
			//Asume file file
			OpenLauncher.logger.debug(target.getAbsolutePath() + " Does not have an md5. Assuming file intact!");
		}
		frame.setVisible(false);
	}

	private static class ProgressListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// e.getSource() gives you the object of DownloadCountingOutputStream
			// because you set it in the overriden method, afterWrite().
			long bytes = ((DownloadCountingOutputStream) e.getSource()).getByteCount();

			//Launch.main.println("Downloaded bytes : " + (short)(bytes / 100000) + "MB");
			progressBar.setValue((int) ((DownloadCountingOutputStream) e.getSource()).getByteCount());
		}
	}
}

class DownloadCountingOutputStream extends CountingOutputStream {

	private ActionListener listener = null;

	public DownloadCountingOutputStream(OutputStream out) {
		super(out);
	}

	public void setListener(ActionListener listener) {
		this.listener = listener;
	}

	@Override
	protected void afterWrite(int n) throws IOException {
		super.afterWrite(n);
		if (listener != null) {
			listener.actionPerformed(new ActionEvent(this, 0, null));
		}
	}

}
