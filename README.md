If you want to create the file for calculate the politeness, run:
```
inputCorpus.csv -P delimiter
```
where:
* `inputCorpus.csv`: input file UTF-8 WITHOUT BOM, use notepad ++ , go in format and chose it.
* `delimiter`: ";" or ","

Else, you have to give use the following params:
```
inputCorpus.csv textsPoliteAndImpolite.csv textsMoodAndModality.csv delimiter -O/-S -J/-A/-L/-S/-Sp/-F -G
```
where:
* `delimiter`: ";" or ","
* `-O`: use Ortu's dataset group 3
* `-S`: use Stack Overflow Dataset
* `-J,-S,-A,-L`: indicates if you are yorking on a specific dataset emotion , J= joy, A = anger , S= sadness, L = love, Sp= surprise , -F = fear
* `inputCorpus.csv`: in UTF-8 WITHOUT BOM , use notepad ++ , go in format and chose it.
* `-G`: Extract bigrams and unigrams if present.

To create `textsPoliteAndImpolite.csv` you have to run the following:
```
inputCorpus.csv -P
```
to generate the file,alias docs.py, given as input from CalculatePoliteness.model.py

To create `textsMoodAndModality.csv` you have to run this jar as:
```
inputCorpus.csv -P
```
to generate the file,alias docs.py,given as input from CalculateMoodAndModality.moodAndModality.py

To give docs.py in input to those python files you have to put those files into same directory where the python (model.py or moodAndModality.py) are.



