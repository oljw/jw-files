import sys
import os
import csv

if len(sys.argv) != 2:
    os._exit(1)

path = sys.argv[1]  # get folder as a command line argument

os.chdir(path)

# csvFiles = []
# for f in os.listdir('.'):
#     if f.endswith('.csv') or f.endswith('.CSV'):
#         csvFiles.append(f)

csvFiles = [f for f in os.listdir('.') if f.endswith('.csv') or f.endswith('.CSV')]

print(path)
print(csvFiles)

for csvFile in csvFiles:
    xmlFile = csvFile[:-4] + '.xml'
    csvData = csv.reader(open(csvFile))
    xmlData = open(xmlFile, 'w')
    # there must be only one top-level tag
    xmlData.write('<resources>' + "\n")
    rowNum = 0

    # First row of the csv files must be the header
    for row in csvData:
        if rowNum == 0:
            tags = row
            # replace spaces w/ underscores in tag names
            for i in range(len(tags)):
                tags[i] = tags[i].replace(' ', '_')
        else:
            print(row)
            xmlData.write('    <string name=',)
            # for i in range(len(tags)):
            for i in range(1):
                xmlData.write("\"" + tags[i] + '_' + row[i] + "\">" + row[1] + '</string>' + "\n")
        rowNum += 1

    xmlData.write('</resources>' + "\n")
    xmlData.close()
