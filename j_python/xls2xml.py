import sys
import os
import xlrd
import tkinter as Tk
import tkinter.filedialog as fd

path = fd.askdirectory()

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
                value = sheet.row_values(row_num)[1].replace(r"'", r"\'")

                xmlData.write('    <string name=',)
                xmlData.write("\"" + str(key) + "\">" + str(value) + '</string>' + "\n") 

                prevKey = key

                for i in range(len(keys)):
                    if keys[i] == prevKey:
                        xmlData.write("ERROR")
                        raise SystemExit("Duplicated key value: " + str(keys[i]) + "; aborting app.")

                keys.append(key)

            keys.clear()

            xmlData.write('</resources>' + "\n")
            xmlData.close()

main()