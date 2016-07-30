install.packages('stargazer',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('DMwR',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('ISLR',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('e1071',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('caret',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('klaR',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('class',dependencies = TRUE,repos="http://cran.rstudio.com/")


library(stargazer)
library(caTools)
library(e1071)
library(caret)
library(klaR)
library(class)
library(DMwR)
library(ISLR)
#Splitting Data
pima_Data<-read.csv(
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
x=3
accuracy_table<-c()
accuracy_table1<-c()
while(x<12)
{
for(i in 1:10)
{
  #Splitting the data
  sample = sample.split(1:NROW(pima_Data),SplitRatio =.90)
  pima_Train = subset(pima_Data,sample == TRUE)
  test = subset(pima_Data,sample == FALSE)
  
  #Omitting rows with missing values
  newpima_Train <- na.omit(pima_Train)
  newTest <- na.omit(test)
  
  #Building model
  model<-knn(newpima_Train, newTest, newpima_Train$testVal,k = x, prob=TRUE)
  #attributes(.Last.value)
  
  accuracy<- sum(newTest$testVal == model)/nrow(test)
  accuracy_table <- rbind(accuracy_table,c(i,accuracy*100))
 
}
average_accuracy<-sum(accuracy_table)/nrow(accuracy_table)
accuracy_table1<-rbind(accuracy_table1,c(x,average_accuracy))
colnames(accuracy_table1)<-c("k","Average accuracy of 10 experiments in %")
x=x+2
}
stargazer(accuracy_table1, type = "text", title="Accuracy of kNN", digits=1, out="kNNAccuracy.text")