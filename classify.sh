#!/bin/bash

#Set Script Name variable
SCRIPT="test.sh"

print_help() {
		printf "\nHelp documentation for ${BOLD}$SCRIPT ${NC}\n\n"
		printf "The following command line options are recognized:\n"
		printf " ${BOLD}-i ${NC}\t -- the input file coded in **UTF-8 without BOM**, containing the corpus for the classification;[here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-classification-corpus).\n"
		printf " ${BOLD}-d ${NC}\t -- the delimiter semicolon or  comma used in the csv file.\n"
		printf " ${BOLD}-m ${NC}\t-- path to the liblinear model will be used for classification\n"
		printf " ${BOLD}-o ${NC}\t-- path to the dictionary folder containing  UnigramsList.txt and BigramsList.txt used to train the model given in input\n"
		printf " ${BOLD}-f ${NC}\t-- path to the Inverse document frequency folder containing  the idfs (unigrams, bigrams, positive,negative,neutral,ambiguos) used for the feature.csv created for the classification task\n"
		printf " ${BOLD}-e ${NC}\t -- the specific emotion for training the model, defined in joy, anger,sadness, love, surprise, fear.\n"
		printf " ${BOLD}-l ${NC}\t -- indicates if the csv given in input has the column named label or not\n"
		printf " ${BOLD}-h ${NC}\t -- Displays this help message. No further functions are performed.\n\n"
		printf "Example: ${BOLD} bash classify.sh -i love.csv  -e love -g training_love/liblinear/NoDownSampling/modelLiblinear_0.Rda -l  -f training_love/InverseDocumentFrequency -o training_love/Dictionary -d semicolon ${NC}\n\n"
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
while getopts "i:d:e:lpf:o:m:h" FLAG; do
	case $FLAG in
		i )  INPUT=$OPTARG;; 
		f ) IDFPATH=$OPTARG;;
		o) DICTIONARYPATH=$OPTARG;;
		m ) MODEL=$OPTARG;;
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
if  [ "$DELIMITER" = 'semicolon' ] ; then 
	java  -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ';' -t classification -Ex createDocFormat
	elif [ "$DELIMITER"='comma' ] ; then 
	java  -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar -i $INPUT  -d ','  -t classification -Ex createDocFormat
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
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT -P classification_$filename/ElaboratedFiles/textsPoliteAndImpolite.csv -M classification_$filename/ElaboratedFiles/textsMoodAndModality.csv -d ';'  $EXTRACTDICTIONARY  -t classification -Ex SenPolImpolMoodModality
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex unigrams_1
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex bigrams_1
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex unigrams_2
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex bigrams_2
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ';'   -t classification -Ex wordnet $HASLABEL
	
	elif [ "$DELIMITER"='comma' ] ; then 
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT -P classification_$filename/ElaboratedFiles/textsPoliteAndImpolite.csv -M classification_$filename/ElaboratedFiles/textsMoodAndModality.csv -d ','  $EXTRACTDICTIONARY -t classification -Ex SenPolImpolMoodModality
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex unigrams_1
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex bigrams_1
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex unigrams_2
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex bigrams_2
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC Emotion_And_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex wordnet $HASLABEL
fi;

#merging the single features extracted
paste -d , classification_$filename/features-SenPolImpolMoodModality.csv classification_$filename/features-unigrams_1.csv classification_$filename/features-unigrams_2.csv  classification_$filename/features-bigrams_1.csv  classification_$filename/features-bigrams_2.csv classification_$filename/features-wordnet.csv > classification_$filename/features-$EMOTION.csv  
#rm classification_$filename/features-SenPolImpolMoodModality.csv
#rm classification_$filename/features-unigrams_1.csv
#rm classification_$filename/features-bigrams_1.csv
#rm classification_$filename/features-unigrams_2.csv
#rm classification_$filename/features-bigrams_2.csv
#rm classification_$filename/features-wordnet.csv

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


#rm -r classification_$filename/Dictionary
#rm -r classification_$filename/InverseDocumentFrequency
#rm -r classification_$filename/ElaboratedFiles




