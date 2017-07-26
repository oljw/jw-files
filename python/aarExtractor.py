from shutil import copy
import os
import Tkinter as tk
import tkFileDialog

root = tk.Tk()
root.withdraw()

modules_path = tkFileDialog.askdirectory(title="Specify the path of the modules folder.")
# modules_path = "C:/Users/JW/Desktop/JW/git/rm_common/modules"
copy_path = tkFileDialog.askdirectory(title="Specify folder to save .aar files.")
# copy_path = "C:/Users/JW/Desktop/aarfiles"

for root, dirs, files in os.walk(modules_path):
    for name in files:
        absfile = os.path.join(root, name)
        if name.endswith(".aar"):
            copy(absfile, copy_path)
