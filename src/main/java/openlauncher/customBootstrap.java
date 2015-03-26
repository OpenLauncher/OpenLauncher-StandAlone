package openlauncher;

import net.minecraft.bootstrap.Bootstrap;

import java.io.File;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * Created by mark on 25/03/15.
 */
public class customBootstrap extends Bootstrap {

	Main main;

	public customBootstrap(File workDir, Proxy proxy, PasswordAuthentication proxyAuth, String[] remainderArgs, Main main) {

		super(workDir, proxy, proxyAuth, remainderArgs);
		this.main = main;
	}

	@Override
	public void print(String string) {
		if (main != null)
			main.print(string);
	}
}
