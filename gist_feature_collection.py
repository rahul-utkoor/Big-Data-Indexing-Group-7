import os
import sys
import cv2
import numpy as np
import shlex , shutil
from PIL import Image
import subprocess as sp

if os.path.isfile("features.txt") :
    os.remove("features.txt")

files = os.listdir("peekaboom/images")

for file in files[:10000] :
    args = shlex.split("./build/LibGIST peekaboom/images/"+file)
    p = sp.Popen(args)
    p.wait()
