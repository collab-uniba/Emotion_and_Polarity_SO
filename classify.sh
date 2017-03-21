#!/bin/bash

#Set Script Name variable
SCRIPT="test.sh"

print_help() {
		printf "\nHelp documentation for ${BOLD}$SCRIPT ${NC}\n\n"
		printf "The following command line options are recognized:\n"
		printf " ${BOLD}-i ${NC}\t -- the input file coded in **UTF-8 without BOM**, containing the corpus for the classification;[here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-classification-corpus).\n"
		printf " ${BOLD}-d ${NC}\t -- the delimiter semicolon or  comma used in the csv file.\n"
		printf " ${BOLD}-m ${NC}\t-- path to the liblinear model will be used for classification; if you omit this input, the script will use the model trained on Stack Overflow on the emotion you specify\n"
		printf " ${BOLD}-o ${NC}\t-- path to the n-grams folder containing  UnigramsList.txt and BigramsList.txt used to train the model given in input\n"
		printf " ${BOLD}-f ${NC}\t-- if you give the model as input you must specify n-grams path containing  UnigramsList.txt and BigramsList.txt used to train the model given in input\n"
		printf " ${BOLD}-e ${NC}\t -- the specific emotion for training the model, defined in joy, anger,sadness, love, surprise, fear.\n"
		printf " ${BOLD}-l ${NC}\t -- indicates if the csv given in input has the column named label or not\n"
		printf " ${BOLD}-h ${NC}\t -- Displays this help message. No further functions are performed.\n\n"
		printf "Example: ${BOLD} bash classify.sh -i love.csv  -e love -g training_love/liblinear/NoDownSampling/modelLiblinear_0.Rda -l  -f training_love/idfs -o training_love/n-grams -d semicolon ${NC}\n\n"
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
MODEL=""
# parse args
while getopts "i:d:e:lf:o:m:h" FLAG; do
	case $FLAG in
		i )  INPUT=$OPTARG;; 
		f ) IDFPATH=$OPTARG;;
		o) DICTIONARYPATH=$OPTARG;;
		m ) MODEL=$OPTARG;;
		l ) HASLABEL="-L";;
		e ) EMOTION=$OPTARG
			if [ "$EMOTION" != 'anger' ] && [ "$EMOTION" != 'fear' ] && [ "$EMOTION" != 'sadness' ] && [ "$EMOTION" != 'love' ] && [ "$EMOTION" != 'joy' ] && [ "$EMOTION" != 'surprise' ]; then 
				print "ERROR" "-e option has wrong argument."
				exit 2;
			fi;;
		d ) DELIMITER=$OPTARG
			if [ "$DELIMITER" != 'c' ] && [ "$DELIMITER" != 'sc' ] ; then 
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

if [ "$MODEL" != '' ] ; then 
#use the model given as input
	cp $MODEL r/Liblinear/
  else 
	if [ "$EMOTION" = 'anger' ] ; then 
			MODEL=r/Liblinear/SOModels/modelAnger.Rda
		elif [ "$EMOTION" = 'joy' ] ; then 
			MODEL=r/Liblinear/SOModels/modelJoy.Rda
		elif [ "$EMOTION" = 'fear' ] ; then 
			MODEL=r/Liblinear/SOModels/modelFear.Rda
		elif [ "$EMOTION" = 'love' ] ; then 
			MODEL=r/Liblinear/SOModels/modelLove.Rda
		elif [ "$EMOTION" = 'sadness' ] ; then 
			MODEL=r/Liblinear/SOModels/modelSadness.Rda
		elif [ "$EMOTION" = 'surprise' ] ; then 
			MODEL=r/Liblinear/SOModels/modelSurprise.Rda
	fi;
	cp $MODEL r/Liblinear/
	#use the default grams and IDF
	DICTIONARYPATH=java/res/n-gramsSO
	IDFPATH=java/res/idfsSO
fi;

rm -rf  "classification_$filename""_$EMOTION"

#Creating the format to give at python files.
if  [ "$DELIMITER" = 'sc' ] ; then 
	java  -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';' -t classification -Ex createDocFormat -e $EMOTION
	elif [ "$DELIMITER"='c' ] ; then 
	java  -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar -i $INPUT  -d ','  -t classification -Ex createDocFormat -e $EMOTION
fi;


#taking only the file.csv name, deleting path and the extension

#taking the files   created for the two python files

cp  "classification_$filename""_$EMOTION"/ElaboratedFiles/docs.py python/CalculatePoliteAndImpolite/
cp  "classification_$filename""_$EMOTION"/ElaboratedFiles/docs.py python/CalculateMoodModality/


#starting python files for polite , impolite mood and modality extraction
cd python/CalculatePoliteAndImpolite
#alias pj="cd CalculatePoliteAndImpolite/"
python model.py 
rm docs.py
rm docs.pyc
cd ..
cd ..
cp python/CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv  "classification_$filename""_$EMOTION"/ElaboratedFiles/

rm python/CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv


cd python/CalculateMoodModality
python  moodAndModality.py 
rm docs.py
rm docs.pyc
cd ..
cd .. 

cp  python/CalculateMoodModality/textsMoodAndModality.csv  "classification_$filename""_$EMOTION"/ElaboratedFiles/
rm  python/CalculateMoodModality/textsMoodAndModality.csv

#copy the 
mkdir -p  "classification_$filename""_$EMOTION"/idfs
mkdir -p  "classification_$filename""_$EMOTION"/n-grams


cp $IDFPATH/*  "classification_$filename""_$EMOTION"/idfs/
cp $DICTIONARYPATH/*  "classification_$filename""_$EMOTION"/n-grams/


#starting Emotion_and_Polarity_SO.jar to extract the features


if [ "$DELIMITER" = 'sc' ] ; then 

	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT -P  "classification_$filename""_$EMOTION"/ElaboratedFiles/textsPoliteAndImpolite.csv -M  "classification_$filename""_$EMOTION"/ElaboratedFiles/textsMoodAndModality.csv -d ';'  $EXTRACTDICTIONARY  -t classification -Ex SenPolImpolMoodModality -e $EMOTION

	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex unigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex bigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex unigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t classification -Ex bigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'   -t classification -Ex wordnet $HASLABEL -e $EMOTION
	
	elif [ "$DELIMITER"='c' ] ; then 

	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT -P  "classification_$filename""_$EMOTION"/ElaboratedFiles/textsPoliteAndImpolite.csv -M  "classification_$filename""_$EMOTION"/ElaboratedFiles/textsMoodAndModality.csv -d ','  $EXTRACTDICTIONARY -t classification -Ex SenPolImpolMoodModality -e $EMOTION

	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex unigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex bigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex unigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex bigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','  -t classification -Ex wordnet $HASLABEL -e $EMOTION
fi;

#merging the single features extracted

paste -d ,  "classification_$filename""_$EMOTION"/features-SenPolImpolMoodModality.csv  "classification_$filename""_$EMOTION"/features-unigrams_1.csv  "classification_$filename""_$EMOTION"/features-unigrams_2.csv   "classification_$filename""_$EMOTION"/features-bigrams_1.csv   "classification_$filename""_$EMOTION"/features-bigrams_2.csv  "classification_$filename""_$EMOTION"/features-wordnet.csv >  "classification_$filename""_$EMOTION"/features-$EMOTION.csv  
#rm  "classification_$filename""_$EMOTION"/features-SenPolImpolMoodModality.csv
#rm  "classification_$filename""_$EMOTION"/features-unigrams_1.csv
#rm  "classification_$filename""_$EMOTION"/features-bigrams_1.csv
#rm  "classification_$filename""_$EMOTION"/features-unigrams_2.csv
#rm  "classification_$filename""_$EMOTION"/features-bigrams_2.csv
#rm  "classification_$filename""_$EMOTION"/features-wordnet.csv


#run the R script for classification

#create a folder for the liblinear's generated outputs into the output folder




mv   "classification_$filename""_$EMOTION"/features-$EMOTION.csv r/Liblinear/




modelName=${MODEL##*/}

cd r/Liblinear
rm -rf output/Results_$EMOTION

if [ "$HASLABEL" = '-L' ] ; then 
	 Rscript classification.R Results_$EMOTION $modelName features-$EMOTION.csv 1 $EMOTION
	else 
	 Rscript classification.R Results_$EMOTION $modelName features-$EMOTION.csv 0 $EMOTION
fi;
cd ..
cd ..

mv r/Liblinear/output/Results_$EMOTION/*   "classification_$filename""_$EMOTION"/

rm -r r/Liblinear/$modelName
rm -r r/Liblinear/output/Results_$EMOTION



rm -r "classification_$filename""_$EMOTION"/n-grams
rm -r "classification_$filename""_$EMOTION"/idfs
rm -r "classification_$filename""_$EMOTION"/ElaboratedFiles
rm "classification_$filename""_$EMOTION"/features-bigrams_1.csv
rm "classification_$filename""_$EMOTION"/features-bigrams_2.csv
rm "classification_$filename""_$EMOTION"/features-SenPolImpolMoodModality.csv
rm "classification_$filename""_$EMOTION"/features-unigrams_1.csv
rm "classification_$filename""_$EMOTION"/features-unigrams_2.csv
rm "classification_$filename""_$EMOTION"/features-wordnet.csv
rm "classification_$filename""_$EMOTION"/features-"$EMOTION".csv
