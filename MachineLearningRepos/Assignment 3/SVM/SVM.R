install.packages('stargazer',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('caTools',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('e1071',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('caret',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('klaR',dependencies = TRUE,repos="http://cran.rstudio.com/")

library(stargazer)
library(caTools)
library(e1071)
library(caret)
library(klaR)

callSVM<-c()

callSVM<-function(kernelType) 
{
#Splitting Data
  pima_Data <-
    read.csv(
      "https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data",header = FALSE
  )

#Adding column Names to the dataset
colnames(pima_Data) <-
  c("pregnant","glucose","blood","triceps","insulin","bmi","pedigree","age","testVal")

col <- ncol(pima_Data) - 1 #To omit the testClass

#Converting test variable to a factor in R
pima_Data$testVal <- factor(pima_Data$testVal)

summary(pima_Data$testVal)

accuracy=0

accuracy_table <- c()

for(i in 1:10)
{
  #Splitting the data
  sample = sample.split(1:NROW(pima_Data),SplitRatio =.90)
  train = subset(pima_Data,sample == TRUE)
  test = subset(pima_Data,sample == FALSE)
  
  #Building the model using training data
  NBmodel<-svm(testVal~pregnant+glucose+blood+triceps+insulin+bmi+pedigree+age,data=train,kernel=kernelType)
  
  #Predicting using test data
  val<-predict(NBmodel,test)
  table(val,test$testVal)
  #print(val)
  
  #Accuracy
  accuracy<- sum(test$testVal == val)/nrow(test)
  
  accuracy_table <- rbind(accuracy_table,c(i,accuracy*100))
  #print(accuracy)
  
}

colnames(accuracy_table)<-c("Output","Accuracy in %")
average_accuracy<-sum(accuracy_table)/nrow(accuracy_table)
##print(accuracy_table)
print(average_accuracy)

return(average_accuracy)
}

kernelType<-list("linear","polynomial","radial","sigmoid")
average_values<-c()
accuracy_type_average<-c()


for(i in 1:length(kernelType))
{

  average_values<-callSVM(kernelType[i])
  accuracy_type_average <- rbind(accuracy_type_average,c(i,kernelType[i],average_values))
  #print(accuracy_type_average)

}

colnames(accuracy_type_average)<-c("Index","Kernel","Average Accuracy of 10 Experiments in %")
stargazer(accuracy_type_average, type = "text", title="Avergae Accuracy for different kernels in SVM", digits=5, out="SVMAccuracy.text")

