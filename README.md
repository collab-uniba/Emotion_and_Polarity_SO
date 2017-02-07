## Requirements


## Usage

### Training a new model

```
train.sh file.csv -d delimiter -t type [-G] [-e emotion] 
```
where:
* `file.csv`: the input file coded in UTF-8 without BOM [use notepad ++ , go in format and chose it], containing the corpus for the training.
* `delimiter`: the delimiter ";" or "," used in the csv file
* `-G`: extract bigrams and unigrams in the output file (mandatory on the first run; extraction can be skipped afterwards for the same input file).
* `-e emotion`: the specific emotion for training the model, defined in {joy, anger, sadness, love, surprise, fear}

As a result, the script will generate two files:
* a csv file named `feature-emotion.csv` (e.g., if `-e anger` is used, output file is `feature-anger.csv`) containing all the features extracted from the input corpus
* a file named `model-emotion.rda` (e.g., if `-e joy` is used, output file is `model-joy.rda`) containing the final model learned that can be used to detect (i.e., classify) that specific emotion in new, unseen text.

### Classify a text or file

```
classify.sh ...
```

-----

If you want to create the file for calculate the politeness, run:
```
inputCorpus.csv -P delimiter
```



Else, you have to give use the following params:
```
inputCorpus.csv textsPoliteAndImpolite.csv textsMoodAndModality.csv delimiter -O/-S -J/-A/-L/-S/-Sp/-F -G
```
where:
* `delimiter`: ";" or ","
* `-O`: use Ortu's dataset group 3
* `-S`: use Stack Overflow dataset
* `-J,-S,-A,-L`: indicates if you are yorking on a specific dataset emotion , J= joy, A = anger , S= sadness, L = love, Sp= surprise , -F = fear
* `inputCorpus.csv`: in UTF-8 WITHOUT BOM , use notepad ++ , go in format and chose it.


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



