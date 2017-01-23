If you want to create the file for calculate the politeness those are the params:
    inputCorpus.csv -P

Else
    you have to give those params :
        inputCorpus.csv textsPoliteAndImpolite.csv textsMoodAndModality.csv

To create textsPoliteAndImpolite.csv you have to run this jar with "inputCorpus.csv -P" to generate the file,alias docs.py, given as input from CalculatePoliteness.model.py
To create textsMoodAndModality.csv you have to run this jar with "inputCorpus.csv -P" to generate the file,alias docs.py,given as input from CalculateMoodAndModality.moodAndModality.py

To give docs.py in input to those python files you have to put these files into same directory where the python (model.py or moodAndModality.py) are.


