
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
	
	x=SO[,predictorsNames]
	
	yTest  = factor(SO[,outcomeName])
	
	p <- predict(m,x)
	pred = p$predictions
	predictions <- c()
	predictions<-c(predictions,(paste("id","predicted","Annotated", sep=",")))
	  for (i in 0:length(temp[,"id"])){
		predictions <- c(predictions, paste(temp[i,"id"],pred[i],temp[i,"label"], sep=","))
	  }
	output_file <- paste(output_dir, paste("confusion_matrix","txt", sep="."), sep = "/")
	
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
outputPrediction <- paste("predictions","csv",sep=".")
write.table(predictions, file=paste(output_dir,outputPrediction,sep="/"), quote = FALSE, row.names = FALSE, col.names = FALSE, append=TRUE) 

