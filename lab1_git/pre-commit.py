#!D:\cygwin\bin\python2.6.exe
"""
Examine staged git diffs for lines that are > 80 chars.
"""
MAX_LINE_LENGTH = 80
import sys
from subprocess import Popen, PIPE
changedFiles = Popen(["git", "diff", "--cached", "--name-only"], 
				stdout=PIPE).communicate()[0]
for changedFileName in changedFiles.split("\n"):
	if not changedFileName: continue
	thisDiff = Popen(["git", "diff", "--cached", changedFileName],
				stdout=PIPE).communicate()[0].split("\n")
	# Ignore the first 5 lines of headers.
	for line in thisDiff[5:]:
		if line[0] != "+": continue  # ignore non-additions
		lineLen = len(line)
		if lineLen > MAX_LINE_LENGTH:
			print("ERROR! Found line length %(lineLen)s (max: %(MAX_LINE_LENGTH)s) " \
			"in %(changedFileName)s\n" % locals())
			sys.exit(1)
