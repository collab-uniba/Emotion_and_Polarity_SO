# -*- coding: utf-8 -*- 
from pattern.en import parse, Sentence, parse
from pattern.en import modality
from pattern.en import mood

import codecs
import csv
with open('textsMoodAndModality.csv', 'wb') as csvfile:
     fieldnames = ['texts', 'indicative','imperative','conditional','subjunctive','min_modality','max_modality']
   
     writer = csv.DictWriter(csvfile,fieldnames=fieldnames)
     writer.writeheader()
     
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
       writer.writerow({'texts': doc['text'], 'indicative': str(indicatives),'imperative': str(imperatives),'conditional': str(conditionals), 'subjunctive': str(subjunctives),'min_modality': "%.3f" % minModality,'max_modality': "%.3f" % maxModality, })
     