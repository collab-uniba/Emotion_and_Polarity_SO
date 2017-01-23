# -*- coding: utf-8 -*- 
from pattern.en import parse, Sentence, parse
from pattern.en import modality
from pattern.en import mood
import os
os.linesep

f = open('moodAndModality.csv','w')
f.write('Text§indicative§imperative§conditional§subjunctive§min_modality§max_modality\n')

from docs import TEST_DOCUMENTS

for doc in TEST_DOCUMENTS:
     sentences = doc['sentences']
     conditionals=0
     indicatives=0
     imperatives=0
     subjunctives=0
     minModality=1
     maxModality=-1
	
     for sentence in sentences:
        s = parse(sentence, lemmata=True)
        s = Sentence(s)
        m= mood(s)
        modal= modality(s)
		#set the max or min value
        if modal > maxModality:
           maxModality=modal
        if modal < minModality:
           minModality=modal
	
		#this count moods
        if m is "conditional" :
           conditionals=conditionals+1
        elif m is "indicative":
           indicatives=indicatives+1
        elif m is "imperative":
           imperatives=imperatives+1
        elif m is "subjunctive":
           subjunctives=subjunctives+1
		
     f.write(doc['text']+"§")
     f.write(str(indicatives))
     f.write('§')
     f.write(str(imperatives))
     f.write('§')
     f.write(str(conditionals))
     f.write('§')
     f.write(str(subjunctives))
     f.write('§')
     f.write("%.3f" % minModality)
     f.write('§')
     f.write("%.3f" % maxModality)
     f.write('§\n')
	 
   	
f.close()