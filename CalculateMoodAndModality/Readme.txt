Estrai il docs.py dal mio jar dando in input : inputCorpus e -P 

poi :
python moodAndModality.py 

e ti tira fuori il modd e modality per ogni doc in questo modo:

Mood: 
	Splitta il documento in sentences.
	Per ogni frase assegna una label tra : imperative, conditional, indicative,subjunctive
	e conta quanti sono , per questo documento, le frasi con label imperative, conditional, indicative,subjunctive
	
Modality:
	Splitta il documento in sentences.
	Calcola,per ogni sentences, il modality.
	Estrae il min e max modality tra le frasi e lo assegna al documento.

	
Installare il  pattern 2.6.zip