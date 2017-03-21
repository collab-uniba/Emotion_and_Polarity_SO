
# enable commandline arguments from script launched using Rscript
args<-commandArgs(TRUE)

set.seed(846)

# saves that script start time
date_time <- ifelse(is.na(args[1]), format(Sys.time(), "%Y-%m-%d_%H.%M"), args[1])


# creates current output directory for current execution
output_dir <- paste("output", date_time, sep="/")
if(!dir.exists(output_dir))
  dir.create(output_dir, showWarnings = FALSE, recursive = TRUE, mode = "0777")

# Params
model_file <- args[2]
csv_file <- args[3]
hasLabel <- as.integer(args[4])
emotion<- args[5]

# library setup, depedencies are handled by R
library(caret) # for param tuning
library(e1071) # for normality adjustment
library(LiblineaR)

# comma delimiter
SO <- read.csv(csv_file, header = TRUE, sep=",")
outcomeName <- "label"
# list of predictor vars by name
excluded_predictors <- c("id")
temp <- SO
SO <- SO[ , !(names(SO) %in% excluded_predictors)]


# if any, exclude rows with Na, NaN and Inf (missing values)
SO <- na.omit(SO)

load(file = model_file)

if(hasLabel == 1){
	predictorsNames <- names(SO[,!(names(SO)  %in% c(outcomeName))]) # removes the var to be predicted from the test set
	output_file <- paste(output_dir, paste(paste("performance",emotion,sep="_"),"txt", sep="."), sep = "/")
	x=SO[,predictorsNames]
	
	yTest  = factor(SO[,outcomeName])
	
	 p <- predict(m,x, type='raw')
        pred = p$predictions

        #precision , recall and F-1

        precisionNo <- posPredValue(factor(pred), yTest)
        recallNo <- sensitivity(factor(pred),yTest,"NO")
        F1No <- (2 * precisionNo * recallNo) / (precisionNo + recallNo)

        precisionYes<-negPredValue(factor(pred), yTest)
        recallYes<- sensitivity(factor(pred),yTest,"YES")
        F1Yes<- (2 * precisionYes * recallYes) / (precisionYes + recallYes)


        cat("Precision for NO is: ",precisionNo,file=output_file,sep="\n",append=TRUE)
        cat("Precision for NO is: ",precisionNo,sep="\n")
        cat(" Recall for NO is: ",recallNo,file=output_file,sep="\n",append=TRUE)
        cat("Recall  for NO is: ",recallNo,sep="\n")
        cat(" F1  for NO is: ",F1No,file=output_file,sep="\n",append=TRUE)
        cat("F1  for NO is ",F1No,sep="\n")

        cat("Precision for YES is: ",precisionYes,file=output_file,sep="\n",append=TRUE)
        cat("Precision for YES is: ",precisionYes,sep="\n")
        cat(" Recall for YES is: ",recallYes,file=output_file,sep="\n",append=TRUE)
        cat("Recall  for YES is: ",recallYes,sep="\n")
        cat(" F1  for YES is: ",F1Yes,file=output_file,sep="\n",append=TRUE)
        cat("F1  for YES is ",F1Yes,sep="\n")



	predictions <- c()
	predictions<-c(predictions,(paste("id","predicted","Annotated", sep=",")))
	  for (i in 0:length(temp[,"id"])){
		predictions <- c(predictions, paste(temp[i,"id"],pred[i],temp[i,"label"], sep=","))
	  }
	
   #Display confusion matrix
    res=table(pred,yTest)
	cat("\nConfusion Matrix\n",file=output_file,append=TRUE)
	capture.output( print(res), file=output_file,append=TRUE)
	print(res)
  }else {
	p <- predict(m,SO)
	pred = p$predictions
	predictions <- c()
	predictions<-c(predictions,(paste("id","predicted", sep=",")))
	  for (i in 0:length(temp[,"id"])){
		predictions <- c(predictions, paste(temp[i,"id"],pred[i], sep=","))
	  }
}

# save classification to csv file
outputPrediction <- paste(paste("predictions",emotion,sep="_"),"csv",sep=".")
write.table(predictions, file=paste(output_dir,outputPrediction,sep="/"), quote = FALSE, row.names = FALSE, col.names = FALSE, append=TRUE) 

