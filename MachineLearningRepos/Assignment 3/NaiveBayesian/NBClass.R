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
#Splitting Data
my_Data<-read.csv(
  "https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data",header = FALSE
)

pima_Data <-
  read.csv(
    "https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data",header = FALSE
  )

#Adding column Names to the dataset
colnames(pima_Data) <-
  c("pregnant","glucose","blood","triceps","insulin","bmi","pedigree","age","testVal")

col <- ncol(pima_Data) - 1 #To omit the testClass

#Observed some zero values in the columns <- Setting them to NA

pima_Data$blood[pima_Data$blood == 0] <-
  NA # set zero values in the variable blood to "NA"

pima_Data$glucose[pima_Data$glucose == 0] <-
  NA # set zero values in the variable glucose to "NA"

pima_Data$triceps[pima_Data$triceps == 0] <-
  NA # set zero values in the variable triceps to "NA"

pima_Data$insulin[pima_Data$insulin == 0] <-
  NA # set zero values in the variable insulin to "NA"

pima_Data$bmi[pima_Data$bmi == 0] <-
  NA # set zero values in the variable bmi to "NA"

#Converting test variable to a factor in R
pima_Data$testVal <- factor(pima_Data$testVal)

summary(pima_Data$testVal)

accuracy = 0
sumOfAccuracy = 0

accuracy_table <- c()

for(i in 1:10)
{
  #Splitting the data
  sample = sample.split(1:NROW(pima_Data),SplitRatio =.90)
  train = subset(pima_Data,sample == TRUE)
  test = subset(pima_Data,sample == FALSE)
  
  #Building the model using training data
  NBmodel<-naiveBayes(testVal~pregnant+glucose+blood+triceps+insulin+bmi+pedigree+age,data=train)
  
  #Predicting using test data
  val<-predict(NBmodel,test)
  table(val,test$testVal)
  #print(val)
  
  #Accuracy
  accuracy<- sum(test$testVal == val)/nrow(test)
   
  accuracy_table <- rbind(accuracy_table,c(i,accuracy*100))
  
  sumOfAccuracy = sumOfAccuracy + accuracy

  print(accuracy)
  
}

overallAccuracy <- (sumOfAccuracy/i)*100

accuracy_table<-rbind(accuracy_table,c("Overall accuracy",overallAccuracy))

colnames(accuracy_table)<-c("Experiment","Accuracy in %")
print(accuracy_table)

stargazer(accuracy_table, type = "text", title="Accuracy for Naive Bayesian", digits=5, out="NBAccuracy.text")