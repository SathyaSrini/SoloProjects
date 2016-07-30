#install.packages("rpart",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("e1071",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("neuralnet",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("randomForest",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("stargazer",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("adabag",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("ipred",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("mlbench",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("adabag",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("aod",dependencies=TRUE,repos="http://cran.rstudio.com/")
#install.packages("stats",dependencies=TRUE,repos="http://cran.rstudio.com/")

library(stats)
library(rpart)
library(e1071)
library(class)
library(neuralnet)
library(randomForest)
library(mlbench)
library(ada)
library(adabag)

args <- commandArgs(TRUE)
dataURL<-as.character(args[1])
header<-as.logical(args[2])
d<-read.csv(dataURL,header = header,na.strings = c("NA","?","NaN","Inf"))
d <- d[complete.cases(d),]
colnames(d)[as.numeric(args[3])]<-"Class"
Class_Col_number <- as.integer(args[3])
namesofattr<-names(d)
f <- as.formula(paste(namesofattr[Class_Col_number],paste(namesofattr[!namesofattr %in% namesofattr[Class_Col_number]],collapse = " + "),sep = " ~ "))

accu_tree<-c()
accu_SVM<-c()
accu_NB<-c()
accu_knn<-c()
accu_LR<-c()
accu_nn<-c()
accu_bag<-c()
accu_boost<-c()
accu_RFor<-c()

i<-1
set.seed(110)

# create 10 samples
for(i in 1:10) 
{
  cat("Running sample ",i,"\n")
  sampleInstances<-sample(1:nrow(d),size = 0.9*nrow(d))
  
  trainingData<-d[sampleInstances,]
  testData<-d[-sampleInstances,]
  # which one is the class attribute
  
  Class<-trainingData[,Class_Col_number]
  method="Decision tree"
  myTree<-rpart(as.factor(Class)~.,data=trainingData,method='class',parms=list(split='information'),minsplit=2,minbucket=1)
  cpVal<- myTree$cptable[which.min(myTree$cptable[,"xerror"]),"CP"]
  pTree<-prune(myTree,cp=cpVal)
  predict1<- predict(pTree,testData,type=c("class"))
  #accu_tree <- sum(testData$Class==predict1)/nrow(testData)
  accu_tree <- append(accu_tree ,sum(testData$Class==predict1)/nrow(testData))
  temp1 <- accu_tree[[i]]
  cat("Method = ", method,", accuracy= ", temp1*100,"\n")
  
  #SVM
  method="SVM"
  fit2_train1=svm(as.factor(Class)~.,data=trainingData ,kernal="radial")
  pre1<-predict(fit2_train1,newdata=testData)
  #accu_SVM<-sum(testData$Class==pre1)/length(pre1)
  accu_SVM <- append(accu_SVM ,sum(testData$Class==pre1)/length(pre1))
  temp2 <- accu_SVM[[i]]
  cat("Method = ", method,", accuracy= ", temp2*100,"\n")
  
  #NaiveBayes
  method="NaiveBayes"
  NBmodel<-naiveBayes(as.factor(Class)~.,data=trainingData)
  val<-predict(NBmodel,testData)
  #accu_NB<- sum(testData$Class == val)/nrow(testData)
  accu_NB <- append(accu_NB ,sum(testData$Class == val)/nrow(testData))
  temp3 <- accu_NB[[i]]
  cat("Method = ", method,", accuracy= ", temp3*100,"\n")
  
  #kNN
  method="kNN"
  trainingData_Nrm <- sapply(trainingData,as.numeric) 
  testData_Nrm <- sapply(testData,as.numeric)
  model_knn <-knn(train=trainingData_Nrm,test=testData_Nrm,cl=Class,k=3)
  table_pred<-table(model_knn,testData_Nrm[,Class_Col_number])
  #accu_knn<-(table_pred[1]+table_pred[4])/nrow(testData_Nrm)
  accu_knn <- append(accu_knn ,(table_pred[1]+table_pred[4])/nrow(testData_Nrm))
  temp4 <- accu_knn[[i]]
  cat("Method = ", method,", accuracy= ", temp4*100,"\n")
  
  #Logistic Regression
  method="LogisticRegression"
  trainingData[,Class_Col_number]<-as.numeric(trainingData[,Class_Col_number])
  testData[,Class_Col_number]<-as.numeric(testData[,Class_Col_number])
  if(all(dataURL == "http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wpbc.data"))
  {
    testData[,Class_Col_number][testData[,Class_Col_number] == 1] <- 0
    testData[,Class_Col_number][testData[,Class_Col_number] == 2] <- 1
    trainingData[,Class_Col_number][trainingData[,Class_Col_number] == 1] <- 0
    trainingData[,Class_Col_number][trainingData[,Class_Col_number] == 2] <- 1
  }
  if(all(dataURL == "http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.data"))
  {
    testData[,Class_Col_number][testData[,Class_Col_number] == 1] <- 0
    testData[,Class_Col_number][testData[,Class_Col_number] == 2] <- 1
    trainingData[,Class_Col_number][trainingData[,Class_Col_number] == 1] <- 0
    trainingData[,Class_Col_number][trainingData[,Class_Col_number] == 2] <- 1
  }
  if(all(dataURL == "http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data"))
  {
    testData[,Class_Col_number][testData[,Class_Col_number] == 1] <- 0
    testData[,Class_Col_number][testData[,Class_Col_number] == 2] <- 1
    trainingData[,Class_Col_number][trainingData[,Class_Col_number] == 1] <- 0
    trainingData[,Class_Col_number][trainingData[,Class_Col_number] == 2] <- 1
  }
  model<-glm(f, family=binomial(link="logit"), data=trainingData)
  P<-predict(model,newdata=testData,type="response")
  P<-as.numeric(P)
  conftble<-table(actual=testData[,Class_Col_number],predicted=P>0.5)
  #accu_LR=(sum(diag(conftble)))/sum(conftble)
  accu_LR <- append(accu_LR ,(sum(diag(conftble)))/sum(conftble))
  temp5 <- accu_LR[[i]]
  cat("Method = ", method,", accuracy= ", temp5*100,"\n")
  
  #Neural Networks
  method="Neural Networks"
  trainingData_NN <- sapply(trainingData,as.numeric) 
  testData_NN <- sapply(testData,as.numeric)    
  model <- neuralnet(f,trainingData_NN, hidden = ncol(d) - 1,linear.output = FALSE, threshold = 0.15) 
  results <- compute(model,testData_NN[,-Class_Col_number]) 
  results$net.result <- round(results$net.result) 
  predictiontable <- table(testData_NN[,Class_Col_number],results$net.result) 
  accu_nn <- append(accu_nn ,sum(diag(predictiontable))/sum(predictiontable))
  temp6 <- accu_nn[[i]]
  cat("Method = ", method,", accuracy= ", temp6*100,"\n")
  
  #Bagging
  method="Bagging"
  require(ipred)
  class_variable<- as.integer(args[3])
  Class<-as.matrix(trainingData[,class_variable])
  Class<-as.factor(Class)
  model <- bagging(Class[sampleInstances] ~.,data=trainingData[,-class_variable],coob=TRUE)
  pred <- predict(model,data = testData[,-class_variable])
  accu_bag <- append(accu_bag, sum(pred == Class[-sampleInstances])/length(pred))
  temp7 <- accu_bag[[i]]
  cat("Method = ", method,", accuracy= ", temp7*100,"\n")
  
  
  #Boosting
  require(ada)
  method="Boosting"
  model <- ada(f, data = trainingData, iter=20, nu=1, type="discrete")
  p=predict(model,testData)
  # accuracy
  #accu_boost=sum(testData[,Class_Col_number]==p)/length(p)
  accu_boost <- append(accu_boost ,sum(testData[,Class_Col_number]==p)/length(p))
  temp8<-accu_boost[[i]]
  cat("Method = ", method,", accuracy= ", temp8*100,"\n")
  
  
  #Random Forest
  method="Random Forest"
  mod1<-randomForest (factor(Class)~.,data=trainingData,mtype=4,ntree=400)
  pre1<-predict(mod1 , newdata=testData)
  #accu_RFor<-sum(testData$Class==pre1)/length(pre1)
  accu_RFor <- append(accu_RFor ,sum(testData$Class==pre1)/length(pre1))
  temp9<-accu_RFor[[i]]
  cat("Method = ", method,", accuracy= ", temp9*100,"\n")
}

accuracy_type_average <- c()
accuracy_type_average<-mean(accu_tree)
paste("FINAL TABLE FOR",dataURL)
paste("Average Accuracy for Decision Tree: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_SVM)
paste("Average Accuracy for SVM:",accuracy_type_average*100)
accuracy_type_average<-mean(accu_NB)
paste("Average Accuracy for NaiveBayes: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_knn)
paste("Average Accuracy for KNN: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_LR)
paste("Average Accuracy for Logistic Regression: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_nn)
paste("Average Accuracy for Neural Nets: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_bag)
paste("Average Accuracy for Bagging: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_boost)
paste("Average Accuracy for Boosting: ",accuracy_type_average*100)
accuracy_type_average<-mean(accu_RFor)
paste("Average Accuracy for Random Forest: ",accuracy_type_average*100)