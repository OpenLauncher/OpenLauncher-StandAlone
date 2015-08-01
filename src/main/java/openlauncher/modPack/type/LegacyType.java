package openlauncher.modPack.type;

import openlauncher.modPack.ModPackInstance;
import openlauncher.modPack.PackType;

public class LegacyType extends PackType {
	@Override
	public void checkMods(ModPackInstance modPackInstance) {
		//TODO http://stackoverflow.com/questions/1823264/quickest-way-to-convert-xml-to-json-in-java
		//We convert to json on the launcher end, or we might make a script to convert all of the old packs
	}
}
