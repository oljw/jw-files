import os
import xlrd
import Tkinter as tk
import tkFileDialog
import tkMessageBox

root = tk.Tk()
root.withdraw()
path = tkFileDialog.askdirectory()

os.chdir(path)

xlsFiles = [files for files in os.listdir('.') if files.endswith('.xls') or files.endswith('.XLS')]

keys = []
values = []

def main():
    print("main called")
    for xlsFile in xlsFiles:
        workbook = xlrd.open_workbook(xlsFile)
        sheets = workbook.sheet_names()
        prevKey = ""

        for langs in range(len(sheets)):
            sheet = workbook.sheet_by_index(langs)
            name = sheet.name

            xmlFile = name + '_strings.xml'
            xmlData = open(xmlFile, 'w')
            xmlData.write('<resources>' + "\n")    

            for row_num in range(sheet.nrows):
                if row_num == 0:
                    continue
                key = sheet.row_values(row_num)[0]
                value = sheet.row_values(row_num)[1].replace(r"'", r"\'").replace("\n", r"\n").encode('utf-8')

                xmlData.write('    <string name=',)
                xmlData.write("\"" + str(key) + "\">" + str(value) + '</string>' + "\n") 

                prevKey = key

                for i in range(len(keys)):
                    if keys[i] == prevKey:
                        xmlData.write("ERROR")
                        tkMessageBox.showerror("Error", "Duplicated key value: '" + str(keys[i]) + "'. Aborting app.")
                        raise SystemExit("Duplicated key value: '" + str(keys[i]) + "'. Aborting app.")

                keys.append(key)

            # keys.clear()
            del keys[:]

            xmlData.write('</resources>' + "\n")
            xmlData.close()

main()