package openlauncher;

import net.minecraft.launcher.Launcher;
import net.minecraft.launcher.MinecraftUserInterface;
import net.minecraft.launcher.SwingUserInterface;

import javax.swing.*;

/**
 * Created by mark on 25/03/15.
 */
public class FakeMCInterface extends SwingUserInterface {
	public FakeMCInterface(Launcher minecraftLauncher, JFrame frame) {
		super(minecraftLauncher, frame);
	}

	@Override
	public void initializeFrame() {

	}


}
