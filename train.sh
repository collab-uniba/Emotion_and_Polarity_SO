#!/bin/bash

#Set Script Name variable
SCRIPT="train.sh"

print_help() {
		printf "\nHelp documentation for ${BOLD}$SCRIPT ${NC}\n\n"
		printf "The following cnd line options are recognized:\n"
		printf " ${BOLD}-i ${NC}\t -- the input file coded in **UTF-8 without BOM**, containing the corpus for the training; the format of the input file is specified [here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-training-corpus).\n"
		printf " ${BOLD}-d ${NC}\t -- the delimiter semicolon or  comma used in the csv file.\n"
		printf " ${BOLD}-g ${NC}\t-- extract bigrams and unigrams (mandatory on the first run; extraction can be skipped afterwards for the same input file); dictionaries will be stored in `./training_filename/n-grams/UnigramsList.txt` and `./training_filename/n-grams/BigramsList.txt`).\n"
		printf " ${BOLD}-e ${NC}\t -- the specific emotion for training the model, defined in joy, anger,sadness, love, surprise, fear.\n"
		printf " ${BOLD}-h ${NC}\t -- Displays this help message. No further functions are performed.\n\n"
		printf "Example: ${BOLD} bash $SCRIPT -i path/file.csv -e anger -d semicolon -g ${NC}\n\n"
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




EXTRACTDICTIONARY=""
# parse args
while getopts "i:d:e:gph" FLAG; do
	case $FLAG in
		i )  INPUT=$OPTARG;;
		g ) EXTRACTDICTIONARY="-G";;
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


if [ "$EXTRACTDICTIONARY" = '-G' ] ; then 
	
	print "training_$filename""_$EMOTION"
	rm -rf "training_$filename""_$EMOTION" #if the same folder exists i will remove it 
	

	elif [ "$EXTRACTDICTIONARY" = '' ] ; then 
	  if [ -d  "training_$filename""_$EMOTION" ] ; then 
	     cd "training_$filename""_$EMOTION"/  #if the same folder exists  and he don't want  re-extract the n-grams
	 
		 if [ -d "n-grams" ] ; then
			
			cd n-grams/
			if [ -e 'BigramsList_1.txt' ] && [ -e 'UnigramsList_1.txt' ] && [ -e 'BigramsList_2.txt' ] && [ -e 'UnigramsList_2.txt' ] ; then #check  the existence of the two files bigrams and unigrams
				cd ..
				find -maxdepth 1 -not -name n-grams -not -name "." -exec rm -rf {} \; #removes all others directories
				cd ..
				else 
					print "ERROR" "UnigramsList.txt OR  bigramsList.txt don't exist , I will extract the n-grams.."
					EXTRACTDICTIONARY="-G"
					cd ..
					cd ..
					rm -rf "training_$filename""_$EMOTION"


		     fi;
		else
			print "ERROR" "The folder n-grams doesn't exists. I will extract the n-grams.."
			EXTRACTDICTIONARY="-G"
			cd ..
			rm -rf "training_$filename""_$EMOTION"
		fi; 
	else 
		print "ERROR" "The folder "training_$filename""_$EMOTION" doesn't exist, extracting n-grams.."
		EXTRACTDICTIONARY="-G"
	fi;
fi;



#Creating the format to give at python files.
if  [ "$DELIMITER" = 'sc' ] ; then 
	java  -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT -d ';' -t training -Ex createDocFormat -e $EMOTION
		elif [ "$DELIMITER"='c' ] ; then 
	java  -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar -i $INPUT  -d ','  -t training -Ex createDocFormat -e $EMOTION
fi;

#taking only the file.csv name, deleting path and the extension

#taking the files   created for the two python files
cp "training_$filename""_$EMOTION"/ElaboratedFiles/docs.py python/CalculatePoliteAndImpolite/
cp "training_$filename""_$EMOTION"/ElaboratedFiles/docs.py python/CalculateMoodModality/

#starting python files for polite , impolite mood and modality extraction
cd python/CalculatePoliteAndImpolite

python model.py 
rm docs.py
rm docs.pyc
cd ..
cd ..
cp python/CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv "training_$filename""_$EMOTION"/ElaboratedFiles/
rm python/CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv


cd python/CalculateMoodModality
python  moodAndModality.py 
rm docs.py
rm docs.pyc
cd ..
cd ..
cp  python/CalculateMoodModality/textsMoodAndModality.csv "training_$filename""_$EMOTION"/ElaboratedFiles/
rm python/CalculateMoodModality/textsMoodAndModality.csv




#starting Emotion_and_Polarity_SO.jar to extract the features

if [ "$DELIMITER" = 'sc' ] ; then 
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT -P "training_$filename""_$EMOTION"/ElaboratedFiles/textsPoliteAndImpolite.csv -M "training_$filename""_$EMOTION"/ElaboratedFiles/textsMoodAndModality.csv -d ';'  $EXTRACTDICTIONARY  -t training -Ex SenPolImpolMoodModality -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t training -Ex unigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t training -Ex bigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t training -Ex unigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t training -Ex bigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ';'  -t training -Ex wordnet -e $EMOTION
	
	elif [ "$DELIMITER"='c' ] ; then 
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT -P "training_$filename""_$EMOTION"/ElaboratedFiles/textsPoliteAndImpolite.csv -M "training_$filename""_$EMOTION"/ElaboratedFiles/textsMoodAndModality.csv -d ','  $EXTRACTDICTIONARY  -t training -Ex SenPolImpolMoodModality -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','   -t training -Ex unigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','   -t training -Ex bigrams_1 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','   -t training -Ex unigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','   -t training -Ex bigrams_2 -e $EMOTION
	java -jar -Xmx30000m -XX:+UseConcMarkSweepGC java/Emotion_and_Polarity_SO.jar  -i $INPUT  -d ','   -t training -Ex wordnet -e $EMOTION
fi;

#merging the single features extracted
paste -d , "training_$filename""_$EMOTION"/features-SenPolImpolMoodModality.csv "training_$filename""_$EMOTION"/features-unigrams_1.csv "training_$filename""_$EMOTION"/features-unigrams_2.csv  "training_$filename""_$EMOTION"/features-bigrams_1.csv  "training_$filename""_$EMOTION"/features-bigrams_2.csv "training_$filename""_$EMOTION"/features-wordnet.csv > "training_$filename""_$EMOTION"/features-$EMOTION.csv  


#rm "training_$filename""_$EMOTION"/features-SenPolImpolMoodModality.csv
#rm "training_$filename""_$EMOTION"/features-unigrams_1.csv
#rm "training_$filename""_$EMOTION"/features-bigrams_1.csv
#rm "training_$filename""_$EMOTION"/features-unigrams_2.csv
#rm "training_$filename""_$EMOTION"/features-bigrams_2.csv
#rm "training_$filename""_$EMOTION"/features-wordnet.csv

#run the R script without downSamping (save the model) , and with downsampling(save the model)

#create a folder for the liblinear's generated outputs into the output folder
cd "training_$filename""_$EMOTION"
rm -rf liblinear
mkdir -p liblinear/DownSampling
mkdir -p liblinear/NoDownSampling
cd .. 

mv  "training_$filename""_$EMOTION"/features-$EMOTION.csv r/Liblinear/
cd r/Liblinear
rm -rf output/Results_$EMOTION
Rscript svmLiblinearWithoutDownSampling.R Results_$EMOTION modelsLiblinear.txt features-$EMOTION.csv $EMOTION
cd ..
cd ..
mv r/Liblinear/output/Results_$EMOTION/*  "training_$filename""_$EMOTION"/liblinear/NoDownSampling/
rm -r r/Liblinear/output/Results_$EMOTION


#same thing with downSampling. 
cd r/Liblinear
Rscript svmLiblinearDownSampling.R Results_$EMOTION modelsLiblinear.txt features-$EMOTION.csv $EMOTION
cd ..
cd ..
mv  r/Liblinear/features-$EMOTION.csv  "training_$filename""_$EMOTION"/
mv r/Liblinear/output/Results_$EMOTION/*  "training_$filename""_$EMOTION"/liblinear/DownSampling/
rm -r r/Liblinear/output/Results_$EMOTION



#cd "training_$filename""_$EMOTION"/ElaboratedFiles
#find -maxdepth 1 -not -name docs.py -not -name "." -exec rm -rf {} \; #removes all others directories

#cd ..