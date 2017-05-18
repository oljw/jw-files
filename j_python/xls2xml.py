import sys
import os
import xlrd

if len(sys.argv) != 2:
    os._exit(1)
path = sys.argv[1]  # get folder as a command line argument
os.chdir(path)

xlsFiles = [files for files in os.listdir('.') if files.endswith('.xls') or files.endswith('.XLS')]

def main():
    for xlsFile in xlsFiles:
        workbook = xlrd.open_workbook(xlsFile)
        sheets = workbook.sheet_names()

        for langs in range(len(sheets)):
            sheet = workbook.sheet_by_index(langs)
            name = sheet.name

            xmlFile = name + '_strings.xml'
            xmlData = open(xmlFile, 'w')
            xmlData.write('<resources>' + "\n")    

            for row_num in range(sheet.nrows):
                key = sheet.row_values(row_num)[0]
                value = sheet.row_values(row_num)[1]

                xmlData.write('    <string name=',)
                xmlData.write("\"" + str(key) + "\">" + str(value) + '</string>' + "\n") 

            xmlData.write('</resources>' + "\n")
            xmlData.close()

main()








# CSV STUFF
# import csv
# csvFiles = [files for files in os.listdir('.') if files.endswith('.csv') or files.endswith('.CSV')]

# def csv_to_xml():
#     for csvFile in csvFiles:
#         xmlFile = csvFile[:-4] + '.xml'
#         csvData = csv.reader(open(csvFile))
#         xmlData = open(xmlFile, 'w')
#         # there must be only one top-level tag
#         xmlData.write('<resources>' + "\n")
#         rowNum = 0

#         # First row of the csv files must be the header
#         for row in csvData:
#             if rowNum == 0:
#                 tags = row
#                 # replace spaces w/ underscores in tag names
#                 for i in range(len(tags)):
#                     tags[i] = tags[i].replace(' ', '_')
#             else:
#                 xmlData.write('    <string name=',)
#                 # for i in range(len(tags)):
#                 for i in range(1):
#                     xmlData.write("\"" + tags[i] + '_' + row[i] + "\">" + row[1] + '</string>' + "\n")
#             rowNum += 1
            
#         xmlData.write('</resources>' + "\n")
#         xmlData.close()
