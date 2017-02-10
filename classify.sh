#!/bin/bash

#Set Script Name variable
SCRIPT="test.sh"

# print help instructions, da rivedere, questi sono da buttare , sono solo esemplari
print_help() {
		printf "\nHelp documentation for ${BOLD}$SCRIPT ${NC}\n\n"
		printf "The following command line options are recognized:\n"
		printf "\t ${BOLD}-i ${NC}\t -- the input file coded in **UTF-8 without BOM**, containing the corpus for the training; the format of the input file is specified [here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-training-corpus).\n"
		printf "\t ${BOLD}-d ${NC}\t -- the delimiter semicolon or  comma used in the csv file\n"
		printf "\t ${BOLD}-g ${NC}\t\t -- extract bigrams and unigrams (mandatory on the first run; extraction can be skipped afterwards for the same input file); dictionaries will be stored in `./<file.csv>/dictionary/unigrams.txt` and `./dictionary/<file.csv>/bigrams.txt`)\n"
		printf "\t ${BOLD}-e ${NC}\t\t -- the specific emotion for training the model, defined in `joy`, `anger`, `sadness`, `love`, `surprise`, `fear`.\n"
		printf "\t ${BOLD}-h ${NC}\t\t -- Displays this help message. No further functions are performed.\n\n"
		printf "Example: ${BOLD} bash $SCRIPT -i input.csv -e anger -d semicolon -g ${NC}\n\n"
		exit 1
}

# redefine an echo function depending on verbose switch 
print() {
	local level=$1
        local code=$GREEN # default is "NOTICE"
        if [ "${level}" = 'ERROR' ]; then
		code=$RED
	elif [ "${level}" = 'INFO' ]; then
		code=$CYAN
	fi
	
	if [ "${VERBOSE}" == 'on' ]; then
		printf "${code}[${level}]${NC}: " | tee -a $LOG
		echo $2 | tee -a $LOG
	else
		printf "${code}[${level}]${NC}: "
		echo $2 
	fi
}




HASLABEL=""
CALCPOLITEIMPOLITEMOODMODALITY=""
# parse args
while getopts "i:d:e:lpf:o:g:h" FLAG; do
	case $FLAG in
		i )  INPUT=$OPTARG;; 
		f ) IDFPATH=$OPTARG;;
		o) DICTIONARYPATH=$OPTARG;;
		g ) MODEL=$OPTARG;;
		l ) HASLABEL="-L";;
		p ) CALCPOLITEIMPOLITEMOODMODALITY="-P";;
		e ) EMOTION=$OPTARG
			if [ "$EMOTION" != 'anger' ] && [ "$EMOTION" != 'fear' ] && [ "$EMOTION" != 'sadness' ] && [ "$EMOTION" != 'love' ] && [ "$EMOTION" != 'joy' ] && [ "$EMOTION" != 'surprise' ]; then 
				print "ERROR" "-e option has wrong argument."
				exit 2;
			fi;;
		d ) DELIMITER=$OPTARG
			if [ "$DELIMITER" != 'comma' ] && [ "$DELIMITER" != 'semicolon' ] ; then 
				print "ERROR" "-d option has wrong argument." 
				exit 2;
			fi;;
		
		h ) print_help;;
		\? ) #unrecognized option - show help
			printf "INFO" "Use $SCRIPT -h to see the help documentation." 
			exit 2;;
		: ) print "ERROR" "Missing option argument for -$OPTARG"
			exit 2;;
		* ) printf "ERROR" "Unimplemented option: -$OPTARG" 
			exit 2;;
		esac
done
shift $((OPTIND-1))  #This tells getopts to move on to the next argument.
### end getopts code ###


filename=${INPUT##*/}
filename=${filename%.*}

rm -rf classification_$filename

#Creating the format to give at python files.
if [ "$CALCPOLITEIMPOLITEMOODMODALITY" = '-P' ]; then
	if  [ "$DELIMITER" = 'semicolon' ] ; then 
		java  -jar -Xmx30000m Emotion_And_Polarity_SO.jar  -i $INPUT -P -d ';' -t classification
		 elif [ "$DELIMITER"='comma' ] ; then 
		java  -jar -Xmx30000m Emotion_And_Polarity_SO.jar -i $INPUT -P -d ','  -t classification
	fi;
fi;




#taking only the file.csv name, deleting path and the extension

#taking the files   created for the two python files
cp classification_$filename/ElaboratedFiles/docs.py CalculatePoliteAndImpolite/
cp classification_$filename/ElaboratedFiles/docs.py CalculateMoodModality/

#starting python files for polite , impolite mood and modality extraction
cd CalculatePoliteAndImpolite
#alias pj="cd CalculatePoliteAndImpolite/"
python model.py 
rm docs.py
rm docs.pyc
cd ..
cp CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv classification_$filename/ElaboratedFiles/
rm CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv


cd CalculateMoodModality
python  moodAndModality.py 
rm docs.py
rm docs.pyc
cd ..
cp  CalculateMoodModality/textsMoodAndModality.csv classification_$filename/ElaboratedFiles/
rm  CalculateMoodModality/textsMoodAndModality.csv

#copy the 
mkdir -p classification_$filename/InverseDocumentFrequency
mkdir -p classification_$filename/Dictionary

echo $IDFPATH
cp $IDFPATH/*  classification_$filename/InverseDocumentFrequency/
cp $DICTIONARYPATH/*  classification_$filename/Dictionary/



#starting Emotion_And_Polarity_SO.jar to extract the features

if [ "$DELIMITER" = 'semicolon' ] ; then 
	java -jar -Xmx30000m Emotion_And_Polarity_SO.jar  -i $INPUT -P classification_$filename/ElaboratedFiles/textsPoliteAndImpolite.csv -M classification_$filename/ElaboratedFiles/textsMoodAndModality.csv -d ';'  -e $EMOTION -t classification $HASLABEL
	 elif [ "$DELIMITER"='comma' ] ; then 
	java -jar -Xmx30000m Emotion_And_Polarity_SO.jar  -i $INPUT -P classification_$filename/ElaboratedFiles/textsPoliteAndImpolite.csv -M classification_$filename/ElaboratedFiles/textsMoodAndModality.csv -d ','  -e $EMOTION -t classification $HASLABEL
fi;


#run the R script for classification

#create a folder for the liblinear's generated outputs into the output folder
cd classification_$filename

modelName=${MODEL##*/}

cd .. 
mv  classification_$filename/features-$EMOTION.csv Liblinear/
cp $MODEL Liblinear/

cd Liblinear
rm -rf output/Results_$EMOTION
if [ "$HASLABEL" = '-L' ] ; then 
	 Rscript classification.R Results_$EMOTION $modelName features-$EMOTION.csv 1
	else 
	 Rscript classification.R Results_$EMOTION $modelName features-$EMOTION.csv 0
fi;
cd ..
mv Liblinear/output/Results_$EMOTION/*  classification_$filename/
rm -r Liblinear/$modelName
rm -r Liblinear/output/Results_$EMOTION


rm -r classification_$filename/Dictionary
rm -r classification_$filename/InverseDocumentFrequency
rm -r classification_$filename/ElaboratedFiles




