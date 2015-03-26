package openlauncher;

import net.minecraft.bootstrap.Bootstrap;
import net.minecraft.bootstrap.Downloader;
import net.minecraft.bootstrap.FatalBootstrapError;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class mcBootstrap {

	public static void downloadLauncher(Main main) {
		List<String> list = new ArrayList<String>();
		Bootstrap bootstrap = new customBootstrap(Main.mcDir, Proxy.NO_PROXY, null, (String[]) list.toArray(new String[list.size()]), main);
		bootstrap.setVisible(false);
		File packedLauncherJar = new File(Main.mcDir, "launcher.pack.lzma");
		File packedLauncherJarNew = new File(Main.mcDir, "launcher.pack.lzma.new");
		Boolean force = false;
		if (packedLauncherJarNew.isFile()) {
			main.println("Found cached update");
			bootstrap.renameNew();
		}
		Downloader.Controller controller = new Downloader.Controller();

		if ((force) || (!packedLauncherJar.exists())) {
			Downloader downloader = new Downloader(controller, bootstrap, Proxy.NO_PROXY, null, packedLauncherJarNew);
			downloader.run();

			if (controller.hasDownloadedLatch.getCount() != 0L) {
				throw new FatalBootstrapError("Unable to download while being forced");
			}

			bootstrap.renameNew();
		} else {
			String md5 = bootstrap.getMd5(packedLauncherJar);

			Thread thread = new Thread(new Downloader(controller, bootstrap, Proxy.NO_PROXY, md5, packedLauncherJarNew));
			thread.setName("Launcher downloader");
			thread.start();
			try {
				main.println("Looking for update");
				boolean wasInTime = controller.foundUpdateLatch.await(3L, TimeUnit.SECONDS);

				if (controller.foundUpdate.get()) {
					main.println("Found update in time, waiting to download");
					controller.hasDownloadedLatch.await();
					bootstrap.renameNew();
				} else if (!wasInTime) {
					main.println("Didn't find an update in time.");
				}
			} catch (InterruptedException e) {
				throw new FatalBootstrapError(new StringBuilder().append("Got interrupted: ").append(e.toString()).toString());
			}
		}

		main.println("unpacking launcher");
		bootstrap.unpack();
	}


}
