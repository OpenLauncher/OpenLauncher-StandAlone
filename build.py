 
#!/usr/bin/python
print("Packaging openLauncher")

import os
import errno
import sys
import shutil
from distutils.dir_util import copy_tree
import zipfile

baseDir = os.path.dirname(os.path.abspath(sys.argv[0])) + "/buildZip/"
buildDir = os.path.dirname(os.path.abspath(sys.argv[0])) + "/builds/"
lwjglDir = os.path.dirname(os.path.abspath(sys.argv[0]))
runDir = os.path.dirname(os.path.abspath(sys.argv[0])) + "/"
jarVersion = "0.0.0"
if(os.environ['BUILD_NUMBER'] != None):
	jarVersion = "0.0." + os.environ['BUILD_NUMBER']

jarName = "OpenLauncher-lwjgl-test-main.jar"

print("Buidling version: " + jarVersion)


def build(version):
	print("Building " + version + " version")
	filename =  baseDir + version + "/"
	if(os.path.exists(filename)):
			print("--Removing " + version)
			shutil.rmtree(filename);

	make_sure_path_exists(filename)
	#copys the jar file into the dir ready to make the zip
	print("--Copying jar file " + version)
	shutil.copy2(runDir + "build/libs/" + jarName, filename + jarName);
	#copy libs
	make_sure_path_exists(filename + "OpenLauncher/")
	print("--Copying lwjgl libs " + version)
	copy_tree(lwjglDir + "/lwjgl/jar", filename + "OpenLauncher/jar");
	#copy natives
	print("--Copying lwjgl natives " + version)
	copy_tree(lwjglDir + "/lwjgl/native/" + version, filename + "OpenLauncher/natives");
	#build zipFile
	print("--Creating zip file " + version)
	shutil.make_archive(buildDir + 'OpenLauncher-' + version + '-' + jarVersion, 'zip', filename)


def make_sure_path_exists(path):
    try:
        os.makedirs(path)
    except OSError as exception:
        if exception.errno != errno.EEXIST:
            raise      

def zipdir(path, zip):
    for root, dirs, files in os.walk(path):
        for file in files:
            zip.write(os.path.join(root, file))              


build("macosx");
build("windows");
build("linux");
build("freebsd");
build("openbsd");
build("solaris");