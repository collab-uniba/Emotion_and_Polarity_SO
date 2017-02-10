#!/bin/bash

#Set Script Name variable
SCRIPT="train.sh"

print_help() {
		printf "\nHelp documentation for ${BOLD}$SCRIPT ${NC}\n\n"
		printf "The following command line options are recognized:\n"
		printf " ${BOLD}-i ${NC}\t -- the input file coded in **UTF-8 without BOM**, containing the corpus for the training; the format of the input file is specified [here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-training-corpus).\n"
		printf " ${BOLD}-d ${NC}\t -- the delimiter semicolon or  comma used in the csv file.\n"
		printf " ${BOLD}-g ${NC}\t-- extract bigrams and unigrams (mandatory on the first run; extraction can be skipped afterwards for the same input file); dictionaries will be stored in `./training_filename/dictionary/UnigramsList.txt` and `./training_filename/dictionary/BigramsList.txt`).\n"
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
CALCPOLITEIMPOLITEMOODMODALITY=""
# parse args
while getopts "i:d:e:gph" FLAG; do
	case $FLAG in
		i )  INPUT=$OPTARG;;
		g ) EXTRACTDICTIONARY="-G";;
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


if [ "$EXTRACTDICTIONARY" = '-G' ] ; then 
	
	rm -rf training_$filename #if the same folder exists i will remove it 


	elif [ "$EXTRACTDICTIONARY" = '' ] ; then 
	  if [ -d  "training_$filename" ] ; then 
	     cd training_$filename/  #if the same folder exists  and he wanna re-extract the dictionary 
	 
		 if [ -d "Dictionary" ] ; then 
			
			cd Dictionary/ 
			if [ -e 'BigramsList.txt' ] && [ -e 'UnigramsList.txt' ] ; then #check  the existence of the two files bigrams and unigrams
				cd ..
				find -maxdepth 1 -not -name Dictionary -not -name "." -exec rm -rf {} \; #removes all others directories
				cd ..
				else 
					print "ERROR" "UnigramsList.txt OR  bigramsList.txt don't exist , I will extract the dictionary.."
					EXTRACTDICTIONARY="-G"
					cd ..
					cd ..
					rm -rf training_$filename

		     fi;
		else
			print "ERROR" "The folder Dictionary doesn't exists. I will extract the dictionary.."
			EXTRACTDICTIONARY="-G"
			cd ..
			rm -rf training_$filename
		fi; 
	else 
		print "ERROR" "The folder training_$filename doesn't exist, extracting dictionary.."
		EXTRACTDICTIONARY="-G"
	fi;
fi;



#Creating the format to give at python files.
if [ "$CALCPOLITEIMPOLITEMOODMODALITY" = '-P' ]; then
	if  [ "$DELIMITER" = 'semicolon' ] ; then 
		java  -jar -Xmx30000m Emotion_And_Polarity_SO.jar  -i $INPUT -P -d ';' -t training
		 elif [ "$DELIMITER"='comma' ] ; then 
		java  -jar -Xmx30000m Emotion_And_Polarity_SO.jar -i $INPUT -P -d ','  -t training
	fi;
fi;

#taking only the file.csv name, deleting path and the extension

#taking the files   created for the two python files
cp training_$filename/ElaboratedFiles/docs.py CalculatePoliteAndImpolite/
cp training_$filename/ElaboratedFiles/docs.py CalculateMoodModality/

#starting python files for polite , impolite mood and modality extraction
cd CalculatePoliteAndImpolite
#alias pj="cd CalculatePoliteAndImpolite/"
python model.py 
rm docs.py
rm docs.pyc
cd ..
cp CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv training_$filename/ElaboratedFiles/
rm CalculatePoliteAndImpolite/textsPoliteAndImpolite.csv


cd CalculateMoodModality
python  moodAndModality.py 
rm docs.py
rm docs.pyc
cd ..
cp  CalculateMoodModality/textsMoodAndModality.csv training_$filename/ElaboratedFiles/
rm  CalculateMoodModality/textsMoodAndModality.csv




#starting Emotion_And_Polarity_SO.jar to extract the features

if [ "$DELIMITER" = 'semicolon' ] ; then 
	java -jar -Xmx30000m Emotion_And_Polarity_SO.jar  -i $INPUT -P training_$filename/ElaboratedFiles/textsPoliteAndImpolite.csv -M training_$filename/ElaboratedFiles/textsMoodAndModality.csv -d ';'  $EXTRACTDICTIONARY -e $EMOTION -t training
	 elif [ "$DELIMITER"='comma' ] ; then 
	java -jar -Xmx30000m Emotion_And_Polarity_SO.jar  -i $INPUT -P training_$filename/ElaboratedFiles/textsPoliteAndImpolite.csv -M training_$filename/ElaboratedFiles/textsMoodAndModality.csv -d ','  $EXTRACTDICTIONARY -e $EMOTION -t training
fi;

#run the R script without downSamping (save the model) , and with downsampling(save the model)

#create a folder for the liblinear's generated outputs into the output folder
cd training_$filename
rm -rf liblinear
mkdir -p liblinear/DownSampling
mkdir -p liblinear/NoDownSampling


cd .. 
mv  training_$filename/features-$EMOTION.csv Liblinear/
cd Liblinear
rm -rf output/Results_$EMOTION
Rscript svmLiblinearWithoutDownSampling.R Results_$EMOTION modelsLiblinear features-$EMOTION.csv
cd ..
mv Liblinear/output/Results_$EMOTION/*  training_$filename/liblinear/NoDownSampling/
rm -r Liblinear/output/Results_$EMOTION


#same thing with downSampling. 
cd Liblinear
Rscript svmLiblinearDownSampling.R Results_$EMOTION modelsLiblinear features-$EMOTION.csv
cd ..
mv  Liblinear/features-$EMOTION.csv  training_$filename/
mv Liblinear/output/Results_$EMOTION/*  training_$filename/liblinear/DownSampling/
rm -r Liblinear/output/Results_$EMOTION



cd training_$filename/ElaboratedFiles
find -maxdepth 1 -not -name docs.py -not -name "." -exec rm -rf {} \; #removes all others directories

cd ..
