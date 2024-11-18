import re
import sys
# -*- coding: utf-8 -*-
chapter_start = re.compile(r''+sys.argv[2])
chapter_end= re.compile(r'第[一二三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,6}[章卷]')

def readPage():
    f = sys.argv[1]
    with open(f,"r",encoding="utf-8") as file:
        chapter="";
        line=file.readline();
        while line:
            #判断是否为目录章节
            if chapter_start.search(line):
                chapter += line
                line=file.readline()
                while not chapter_end.search(line):
                    chapter += line
                    line = file.readline()
                break
            line=file.readline()
    print(chapter)
if __name__=="__main__":
    readPage();