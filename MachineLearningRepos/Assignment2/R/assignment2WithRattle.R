args<-commandArgs(TRUE)


#install.packages('rpart',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('rattle',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages("RGtk2",dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages("partykit",dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages("caret",dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages("klaR",dependencies = TRUE,repos="http://cran.rstudio.com/")

library(rpart)
library(rattle)
library(RGtk2)
library(caret)
library(partykit)
library(rpart.plot)
library(RColorBrewer)
library(partykit)
library(caTools)
library(klaR)

#Read input from CSV
training1<-read.csv(args[1])

#Factoring the input
fact1<-factor(training1$Class,levels=0:1,labels=c("no","yes"))

#Constructing the training model
myTrainingTree<-rpart(Class~ XB+XC+XD+XE+XF+XG+XH+XI+XJ+XK+XL+XM+XN+XO+XP+XQ+XR+XS+XT+XU,data=training1,method='class',parms=list(split='information'),minsplit=2,minbucket=1)

#Plotting the training tree
fancyRpartPlot(myTrainingTree,main="Decision Tree for DataSet", sub = "Assignment2 - Submission by SathyaNarayanan Srinivasan(sxs142031)")

#Printing the summary of the model
printcp(myTrainingTree)

#Plotting the summary of the model
plotcp(myTrainingTree)

#Cross validation of Cp Table
minVal <- myTrainingTree$cptable[which.min(myTrainingTree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation

#Pruning the training model
prunedTree<-prune(myTrainingTree,cp=minVal)

#Plot the pruned model
fancyRpartPlot(prunedTree,main="Pruning Tree for DataSet", sub = "Assignment2 - Submission by SathyaNarayanan Srinivasan(sxs142031)")
#Printing the summary of the model
printcp(prunedTree)

#Plotting the summary of the model
plotcp(prunedTree)

#Reading testing data
fact1 <- read.csv(args[3])

#Predicting the input
predictVal <- predict(prunedTree,fact1,type=c("class"))
print("Printing prediction for dataset : ")
print(predictVal)
summary(predictVal)

#Calculating the accuracy
accuracy <- sum(fact1$Class==predictVal)/nrow(fact1)
print("Printing accuracy for dataset : ") 
print(accuracy*100)