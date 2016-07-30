args <- commandArgs(TRUE)
dataURL<-as.character(args[1])
header<-as.logical(args[2])
d<-read.csv(dataURL,header = header)
d<-read.csv(dataURL,header = header,na.strings = c("NA","?","NaN","Inf"))
d <- d[complete.cases(d),]
# create 10 samples
set.seed(123)
library(neuralnet)

DecisionTrees<-function(trainingData,testData){
method="decision trees"
library(rpart)
fit<-rpart(f, data = trainingData, method="class")
pfit<- prune(fit, cp=fit$cptable[which.min(fit$cptable[,"xerror"]),"CP"]) 
testptrainfit<-predict(pfit,testData,type="class")
accuracy=sum(testptrainfit==testData[,classnumber])/nrow(testData)
cat("Method = ", method,", accuracy= ", accuracy,"\n")	 
}

SVM<-function(trainingData,testData){
method="SVM" 
library(e1071)
model <- svm(f, data = trainingData,kernel='polynomial',type='C-classification')
testptrainfit<-predict(model,testData)
accuracy=sum(testptrainfit==testData[,classnumber])/nrow(testData)
cat("Method = ", method,", accuracy= ", accuracy,"\n")	 
}




NaiveBayes1 <- function(training,test){
	library(e1071)
	method="Naive Bayes"
	training[,classnumber]<-as.factor(training[,classnumber])
	test[,classnumber]<-as.factor(test[,classnumber])
	NBmodel <- naiveBayes(f,data = training)
	pred <- predict(NBmodel,test,type = "class")
	results <- table(pred,test[,classnumber])
	accuracy = sum(diag(results))/sum(results)
	cat("Method =",method, "accuracy = ",accuracy,"\n")
}


Boosting<-function(trainingData,testData){
library(ada)
method="Boosting"
model <- ada(f, data = trainingData, iter=20, nu=1, type="discrete")
p=predict(model,testData)
# accuracy
accuracy=sum(testData[,classnumber]==p)/length(p)
cat("Method = ", method,", accuracy= ", accuracy,"\n")
}

Bagging<-function(trainingdata,testdata){
library(ipred)
check = 0
method="Bagging"
trainingdata[,classnumber]<-as.factor(trainingdata[,classnumber])
testdata[,classnumber]<-as.factor(testdata[,classnumber])
if(all(dataURL == "http://www.utdallas.edu/~axn112530/cs6375/creditset.csv"))
{
	testdata[,classnumber][testdata[,classnumber] == 1] <- 0
	testdata[,classnumber][testdata[,classnumber] == 2] <- 1
	trainingdata[,classnumber][trainingdata[,classnumber] == 1] <- 0
	trainingdata[,classnumber][trainingdata[,classnumber] == 2] <- 1
	check = 1
}
if(all(dataURL == "http://www.ats.ucla.edu/stat/data/binary.csv"))
{
	testdata[,classnumber][testdata[,classnumber] == 1] <- 0
	testdata[,classnumber][testdata[,classnumber] == 2] <- 1
	trainingdata[,classnumber][trainingdata[,classnumber] == 1] <- 0
	trainingdata[,classnumber][trainingdata[,classnumber] == 2] <- 1
 	check = 1
}
model <- bagging(f, data = trainingData)
p=predict(model,testData)
if(check)
	p <- round(p)
results <- table(p,testdata[,classnumber])
#accuracy
accuracy=sum(diag(results))/sum(results)
cat("Method = ", method,", accuracy= ", accuracy,"\n")
}

KNN<-function(trainingData,testData){
library(class)
method="KNN" 
trainingData <- sapply(trainingData,as.numeric)
testData <- sapply(testData,as.numeric)	
cl = factor(trainingData[,classnumber])
c1fortest=factor(testData[,classnumber])
knn<-knn(trainingData, testData, cl,k=3,prob=TRUE)
accuracy=(sum(c1fortest==knn)/nrow(testData))
cat("Method = ", method,", accuracy= ", accuracy,"\n")
}

LogisticRegression<-function(trainingdata,testdata){
library(stats)
method="LogisticRegression"
trainingdata[,classnumber]<-as.numeric(trainingdata[,classnumber])
testdata[,classnumber]<-as.numeric(testdata[,classnumber])
if(all(dataURL == "http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wpbc.data"))
  {
  	testdata[,classnumber][testdata[,classnumber] == 1] <- 0
	testdata[,classnumber][testdata[,classnumber] == 2] <- 1
	trainingdata[,classnumber][trainingdata[,classnumber] == 1] <- 0
	trainingdata[,classnumber][trainingdata[,classnumber] == 2] <- 1
  }
  if(all(dataURL == "http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.data"))
  {
	testdata[,classnumber][testdata[,classnumber] == 1] <- 0
	testdata[,classnumber][testdata[,classnumber] == 2] <- 1
	trainingdata[,classnumber][trainingdata[,classnumber] == 1] <- 0
	trainingdata[,classnumber][trainingdata[,classnumber] == 2] <- 1
  }
  if(all(dataURL == "http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data"))
  {
	testdata[,classnumber][testdata[,classnumber] == 1] <- 0
	testdata[,classnumber][testdata[,classnumber] == 2] <- 1
	trainingdata[,classnumber][trainingdata[,classnumber] == 1] <- 0
	trainingdata[,classnumber][trainingdata[,classnumber] == 2] <- 1
  }
model<-glm(f, family=binomial(link="logit"), data=trainingdata)
P<-predict(model,newdata=testdata,type="response")
P<-as.numeric(P)
conftble<-table(actual=testdata[,classnumber],predicted=P>0.5)
accuracy=(sum(diag(conftble)))/sum(conftble)
cat("Method = ", method,", accuracy= ", accuracy,"\n")
}

NNet<-function(trainingData,testData){

method="NeuralNetworks"
trainingData <- sapply(trainingData,as.numeric)
testData <- sapply(testData,as.numeric)
model <- neuralnet(f, trainingData, hidden = ncol(d) - 1,linear.output = FALSE, threshold = 0.15)
pred <- compute(model,testData[,-classnumber])
pred.results<-round(pred$net.result)
accuracy=(sum(pred.results==testData[,classnumber])/nrow(testData))
cat("Method =",method,"accuracy = ",accuracy,"\n")
}

RandomForest<-function(trainingData,testData){
library(randomForest)
set.seed(415)
method="RandomForest"
trainingData[,classnumber]<-as.factor(trainingData[,classnumber])
testData[,classnumber]<-as.factor(testData[,classnumber])
fit <- randomForest(f, data=trainingData, importance=TRUE, ntree=2000)
prediction <- predict(fit, testData)
accuracy=(sum(prediction==testData[,classnumber])/nrow(testData))
cat("Method =",method,"accuracy = ",accuracy,"\n")
}


for(i in 1:10) {
cat("Running sample ",i,"\n")
if (all(dataURL == "http://www.utdallas.edu/~axn112530/cs6375/creditset.csv"))
 {
  d<-subset(d,select=c("LTI","age","default10yr"))
  classnumber<-3
  f<-as.formula("default10yr ~ LTI + age")
 }
  else 
 {
  namesofattr<-names(d)
  classnumber<-as.integer(args[3])
  f <- as.formula(paste(namesofattr[classnumber],paste(namesofattr[!namesofattr %in% namesofattr[classnumber]],collapse = " + "),sep = " ~ "))
 }	
sampleInstances<-sample(1:nrow(d),size = 0.9*nrow(d))
trainingData<-d[sampleInstances,]
testData<-d[-sampleInstances,]
# which one is the class attribute
#Class<-d[,classnumber]
# now create all the classifiers and output accuracy values:
# example of how to output
# method="kNN" 
# accuracy=0.9
# cat("Method = ", method,", accuracy= ", accuracy,"\n")
#DecisionTrees(trainingData,testData)
#SVM(trainingData,testData)
#NaiveBayes1(trainingData,testData)  
#KNN(trainingData,testData)
#LogisticRegression(trainingData,testData)
#NNet(trainingData,testData)
#Boosting(trainingData,testData)
#RandomForest(trainingData,testData)
Bagging(trainingData,testData)
}
