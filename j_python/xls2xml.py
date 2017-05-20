import os
import sys
import xlrd
import Tkinter as tk
import tkFileDialog
import tkMessageBox

keys = []
values = []
dialogEnabled = False

root = tk.Tk()
root.withdraw()

file = sys.argv[1]
project_path = sys.argv[2]

if len(sys.argv) > 3:
    if sys.argv[3] == "dialog":
        dialogEnabled = True
    else:
        raise SystemExit("Wrong parameter: '" + sys.argv[3] + "'. Aborting app.")

if dialogEnabled:
        file = tkFileDialog.askopenfilename(title="Specify the target .xls file")
        project_path = tkFileDialog.askdirectory(title="Specify the path of the project", mustexist=1)

os.chdir(project_path)


def main():
    print("main() called")
    # for xlsFile in xlsFiles:
    workbook = xlrd.open_workbook(file)
    sheets = workbook.sheet_names()
    prevKey = ""

    for langs in range(len(sheets)):
        sheet = workbook.sheet_by_index(langs)
        name = sheet.name

        save_dir = project_path + "/src/main/res/values-" + name

        if not os.path.exists(save_dir):
            os.makedirs(save_dir)

        xmlFile = os.path.join(save_dir, "strings.xml")
        xmlData = open(xmlFile, 'w')
        xmlData.write('<resources>' + "\n")

        for row_num in range(sheet.nrows):
            if row_num == 0:
                continue
            key = sheet.row_values(row_num)[0]
            value = sheet.row_values(row_num)[1].replace("'", r"\'").replace("\n", r"\n").encode('utf-8')

            xmlData.write('    <string name=',)
            xmlData.write("\"" + str(key) + "\">" + str(value) + '</string>' + "\n")

            prevKey = key

            for i in range(len(keys)):
                if keys[i] == prevKey:
                    xmlData.write("ERROR")
                    if dialogEnabled:
                        tkMessageBox.showerror("Error", "Duplicated key value: '" + str(keys[i]) + "'. Aborting app.")
                    raise SystemExit("Duplicated key value: '" + str(keys[i]) + "'. Aborting app.")

            keys.append(key)

        # keys.clear()
        del keys[:]

        xmlData.write('</resources>' + "\n")
        xmlData.close()


main()
