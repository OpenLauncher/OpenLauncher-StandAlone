 
#!/usr/bin/python
print("Packaging openLauncher")

import os
import errno
import sys
import shutil

baseDir = os.path.dirname(os.path.abspath(sys.argv[0])) + "/buildZip/"
runDir = os.path.dirname(os.path.abspath(sys.argv[0])) + "/"
jarName = "OpenLauncher-lwjgl-test-main.jar"


def buildMac():
	print("Building OSX version")
	filename =  baseDir + "OSX/"
	make_sure_path_exists(filename)
	#copys the jar file into the dir ready to make the zip
	shutil.copyfile(runDir + "build/libs/" + jarName, filename + jarName);

def buildWin():
	print("Building Windows version")	
	filename = baseDir + "Windows/"
	make_sure_path_exists(filename)
	#copys the jar file into the dir ready to make the zip
	shutil.copyfile(runDir + "build/libs/" + jarName, filename + jarName);

def buildLinux():
	print("Building Linux version")
	filename = baseDir + "Linux/"
	make_sure_path_exists(filename)
	#copys the jar file into the dir ready to make the zip
	shutil.copyfile(runDir + "build/libs/" + jarName, filename + jarName);


def make_sure_path_exists(path):
    try:
        os.makedirs(path)
    except OSError as exception:
        if exception.errno != errno.EEXIST:
            raise


buildMac();
buildWin();
buildLinux();