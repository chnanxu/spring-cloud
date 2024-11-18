import re
import sys

# -*- coding: utf-8 -*-

pattern_chapter = re.compile(r'第[一二三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,6}[章卷] ')

def read_chapter():
    file = sys.argv[1]
    with open(file, 'r', encoding='utf-8') as f:
        line = f.readline()
        while line:
            if pattern_chapter.search(line):
                print(line.strip())
            line=f.readline()
if __name__ == '__main__':
    read_chapter()