## Requirements
* Python 2.7.x
* Java 8+

## Usage

### Training a new model
```
train.sh file.csv -d delimiter -t type [-G] [-e emotion] 
```
where:
* `file.csv`: the input file coded in UTF-8 without BOM [use notepad ++ , go in format and chose it], containing the corpus for the training; the format of the input file is specified [here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-training-corpus).
* `delimiter`: the delimiter `;` or `,` used in the csv file
* `-G`: extract bigrams and unigrams (mandatory on the first run; extraction can be skipped afterwards for the same input file); dictionaries will be stored in `./<file.csv>/dictionary/unigrams.txt` and `./dictionary/<file.csv>/bigrams.txt`)
* `-e emotion`: the specific emotion for training the model, defined in {`joy`, `anger`, `sadness`, `love`, `surprise`, `fear`}

As a result, the script will generate two output files:
* a csv file named `./<file.csv>/feature-<emotion>.csv` (e.g., if `-e anger` is used, output file is `feature-anger.csv`) containing all the features extracted from the input corpus
* a file named `./<file.csv>/model-<emotion>.rda` (e.g., if `-e joy` is used, output file is `model-joy.rda`) containing the final model learned that can be used to detect (i.e., classify) that specific emotion in new, unseen text.

### Classify a text or file
```
classify.sh -f file.csv -d delimiter -t text -e emotion -m model
```
where:
* `file.csv`: the input file coded in UTF-8 without BOM [use notepad ++ , go in format and chose it], containing the corpus to be annotated.
* `delimiter`: the delimiter `;` or `,` used in the csv file
* `-t "text"`: instead of parsing a csv file, detect emotion in the text (please, note the text must be surrounded by "...").
* `-e emotion`: the specific emotion to be detected in the file or text, defined in {`joy`, `anger`, `sadness`, `love`, `surprise`, `fear`}
* `-m model`: the model file learned as a result of the training step (e.g., `model-anger.rda`)

As a result, the script will generate one output file:
* a csv file name `./<file.csv>/predictions-<emotion>.csv` (e.g., if `-e sad` is used, output file is `predictions-sad.csv`) containing the binary prediction (yes/no) made on each line of the input corpus.


