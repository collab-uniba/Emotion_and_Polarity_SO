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
models_file <- args[2]
csv_file <- args[3]
emotion<- args[4]

# logs errors to file
error_file <- paste(date_time, "log", sep = ".")
log.error <- function() {
  cat(geterrmessage(), file=paste(output_dir, error_file, sep = "/"), append=TRUE)
}
options(show.error.locations=TRUE)
options(error=log.error)

# library setup, depedencies are handled by R
library(caret) # for param tuning
library(e1071) # for normality adjustment
library(LiblineaR)

# comma delimiter
SO <- read.csv(csv_file, header = TRUE, sep=",")

# name of outcome var to be predicted
outcomeName <- "label"
# list of predictor vars by name
excluded_predictors <- c("id")
temp <- SO
SO <- SO[ , !(names(SO) %in% excluded_predictors)]
predictorsNames <- names(SO[,!(names(SO)  %in% c(outcomeName))]) # removes the var to be predicted from the test set

# if any, exclude rows with Na, NaN and Inf (missing values)
SO <- na.omit(SO)

x=SO[,predictorsNames]
y=factor(SO[,outcomeName])


# create stratified training and test sets from SO dataset
splitIndex <- createDataPartition(SO[,outcomeName], p = .70, list = FALSE)

xTrain=x[splitIndex,]
xTest=x[-splitIndex,]
yTrain=y[splitIndex]
yTest=y[-splitIndex]

training<- temp[splitIndex,]
testing <- temp[-splitIndex, ]


#save the training set
trains <- c()
trains <- c(trains,(paste("id","label", sep=",")))
for (i in 0:length(training[,"id"])){
  trains <- c(trains, paste(training[i,"id"],training[i,"label"], sep=","))
}
outputTraining <- paste("trainingSet","csv",sep=".")
write.table(trains, file=paste(output_dir,outputTraining,sep="/"), quote = FALSE, row.names = FALSE, col.names = FALSE, append=TRUE) 


#save the testing set
test <- c()
test <- c(test,(paste("id","label", sep=",")))
for (i in 0:length(testing[,"id"])){
  test <- c(test, paste(testing[i,"id"],testing[i,"label"], sep=","))
}
outputTest <- paste("testingSet","csv",sep=".")
write.table(test, file=paste(output_dir,outputTest,sep="/"), quote = FALSE, row.names = FALSE, col.names = FALSE, append=TRUE) 


# load all the classifiers to tune
classifiers <- readLines(models_file)

for(i in 1:length(classifiers)){
  nline <- strsplit(classifiers[i], ":")[[1]]
  classifier <- nline[1]
  number <- as.integer(nline[2])
  print(paste("Building model for classifier", classifier))
  print(number)  
  #tryCosts=c(0.25,0.50,1,2,4,8)
  tryCosts=c(0.01,0.05,0.10,0.20,0.25,0.50,1,2,4,8)
  bestCost=NA
  bestAcc=0
 
   # output file for the classifier at nad
 # output_file <- paste(output_dir, paste(paste("confusion_matrix_model",number,sep="_"),"txt", sep="."), sep = "/")
 
  output_file <- paste(output_dir,paste(paste("performance",paste(emotion,number,sep="_"),sep="_"),"txt", sep="."), sep = "/")
   
  cat("Input file:",csv_file,"\n",sep=" ",file=output_file)
  cat("Classifier:",classifier,"\n",sep=" ",file=output_file,append=TRUE)
  
  for(co in tryCosts){
		 
      start.time <- Sys.time() 
      
      acc=LiblineaR(data=xTrain,target=yTrain,type=number,cost=co,bias=TRUE,cross=10,verbose=FALSE)
      
      end.time <- Sys.time()
      time.taken <- end.time-start.time

      cat("Results for C=",co," : ",acc," accuracy.\n",file=output_file,sep="",append=TRUE)
      cat("Time taken for k-fold: ",capture.output(time.taken),"\n",file=output_file,sep="",append=TRUE)
      cat("Results for C=",co," : ",acc," accuracy.\n",sep="")
      cat("Time taken for k-fold: ",capture.output(time.taken),"\n",sep="")
      if(acc>bestAcc){
        bestCost=co
        bestAcc=acc
      } }
  
  cat("Best cost is: ",bestCost,file=output_file,sep="",append=TRUE)
  cat("Best cost is: ",bestCost,sep="\n")
  cat(" Best accuracy is: ",bestAcc,file=output_file,sep="",append=TRUE)
  cat(" Best accuracy is: ",bestAcc,sep="\n")
  
  start.time <- Sys.time()
  # Re-train best model with best cost value.
  m=LiblineaR(data=xTrain,target=yTrain,type=number,cost=bestCost,bias=TRUE,verbose=FALSE)
  #save the model
  output_model <- paste(output_dir, paste(paste("model",emotion,number,sep="_"),"Rda", sep="."), sep="/")
  save(m, file=output_model)
  
  
  end.time <- Sys.time()
  time.taken <- end.time - start.time
  cat("Time taken for training on test set: ",capture.output(time.taken),"\n",file=output_file,sep="",append=TRUE)

  p <- predict(m,xTest, type='raw')

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
  for (i in 0:length(testing[,"id"])){
    predictions <- c(predictions, paste(testing[i,"id"],pred[i],testing[i,"label"], sep=","))
  }
  # save classification to text file
  outputPrediction <- paste(paste(paste("predictions",emotion,sep="_"),number,sep="_"),"csv",sep=".")
  write.table(predictions, file=paste(output_dir,outputPrediction,sep="/"), quote = FALSE, row.names = FALSE, col.names = FALSE, append=TRUE) 


  # Display confusion matrix
  res=table(pred,yTest)
  cat("\nConfusion Matrix\n",file=output_file,append=TRUE)
  capture.output( print(res), file=output_file,append=TRUE)
  print(res)
}
