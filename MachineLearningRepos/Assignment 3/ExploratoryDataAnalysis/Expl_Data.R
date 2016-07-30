#install.packages('caTools',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('moments',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('nortest',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('fitdistrplus',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('gplots',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('gridExtra',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('grid',dependencies = TRUE,repos="http://cran.rstudio.com/")
#install.packages('gtable',dependencies = TRUE,repos="http://cran.rstudio.com/")

library(caTools)
library(moments)
library(nortest)
library(fitdistrplus)
library(gplots)
library(gridExtra)
library(grid)
library(gtable)

options(digits=7)

setPdf<-function(title,tableInput,...) 
{
  grid.newpage()
  grid.text(title,y = unit(0.65, "npc"),gp=gpar(fontsize=20, col="red"))
  grid.table(tableInput,rows=row.names(tableInput),cols = colnames(tableInput))
}


#Read input from CSV

pima_Data <-
  read.csv(
    "https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data",header = FALSE
  )

#Adding column Names to the dataset
colnames(pima_Data) <-
  c("pregnant","glucose","blood","triceps","insulin","bmi","pedigree","age","test")

col <- ncol(pima_Data) - 1 #To omit the testClass

pima_DataForCor <- pima_Data

pdf(file='Plots.pdf')


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
pima_Data$test <- factor(pima_Data$test)
summary(pima_Data$test)

#Assigning 0 and 1 to the levels of the test column
levels(pima_Data$test) <- c("No","Yes")
summary(pima_Data)


#Plots - Bar + Histogram and Checking Normal Distribtuion

lapply(1:col,function(i)
  hist(pima_Data[,i], main = paste("Histogram of",names(pima_Data)[i])))
lapply(1:col,function(i)
  barplot(pima_Data[,i], main = paste("BarPlot of",names(pima_Data)[i])))
lapply(1:col,function(i)
  qqnorm(pima_Data[,i], main = paste("Q-Q Plot of",names(pima_Data)[i])))

#Determining Type of distribution
lapply(1:col,function(i)
  descdist(pima_DataForCor[,i], discrete = TRUE,boot=500,method = "unbiased"))

dev.off()

#Calculating Skewness and Kurtosis using Moments Package

pdf(file='Measurements.pdf', height=11, width=8.5)

skewNessValues <-
  lapply(1:col,function(x) skewness(as.numeric(pima_Data[,x]),na.rm = TRUE))

setPdf("Skewness Values",skewNessValues)

kurtoSisValues <-
  lapply(1:col,function(x) kurtosis(as.numeric(pima_Data[,x]),na.rm = TRUE))

setPdf("kurtosis Values",kurtoSisValues)


#Calculating the ShapiroWilk test Values
ShapiroList<-
  lapply(1:col,function(x) as.double
         (shapiro.test(as.numeric(pima_Data[,x]))$p.value))

setPdf("Shapiro-Wilk Test p-Values",ShapiroList)

# Calculting Lilliefors Test
LillieforsList<-
  lapply(1:col,function(x) as.double
         (lillie.test(as.numeric(pima_Data[,x]))$p.value))

setPdf("Lilliefors p-Values",LillieforsList)

# Calculting Anderson-Darling Test for Normality Test
AndersonList<-
  lapply(1:col,function(x) as.double
         (ad.test(as.numeric(pima_Data[,x]))$p.value))

setPdf(" Anderson-Darling p-Values",AndersonList)

#The data contains missing values - as highlighted here -  http://blog.revolutionanalytics.com/2015/06/pairwise-complete-correlation-considered-dangerous.html

# I am using use = everything.

classCorrelation<- lapply(1:col,function(x) as.double(cor(as.numeric(pima_Data[,x]),as.numeric(pima_Data[,col+1],use = "everything"))))
  
setPdf("Correlation with Class Variable",classCorrelation)

classCorrelationWithoutMissingValues<- lapply(1:col,function(x) as.double(cor(as.numeric(pima_DataForCor[,x]),as.numeric(pima_DataForCor[,col+1]),use = "everything")))

setPdf("Correlation with Class Variable without considering missing values",classCorrelationWithoutMissingValues)

previousMax = cor(as.numeric(pima_DataForCor[,1]),as.numeric(pima_DataForCor[,2]))
previousLeft = colnames(pima_DataForCor[1])
previousRight = colnames(pima_DataForCor[2])
AttributeCorrelation = NULL
TitleofTable = NULL

for(i in 1:(ncol(pima_Data)-1))
{
  #print(i)
  for(j in 1:(ncol(pima_Data)-1))
  {
    #print(j)
    if(i==j)
    {
      #print("reached i=j")
      
    }  
    else
    {
      
      AttributeCorrelation = cor(as.numeric(pima_DataForCor[,i]),as.numeric(pima_DataForCor[,j]))
      TitleofTable<-paste("Correlation between",colnames(pima_DataForCor[i])," and ",colnames(pima_DataForCor[j]))
      setPdf(TitleofTable,AttributeCorrelation)
      
      if(previousMax<AttributeCorrelation)
      {
        previousMax = AttributeCorrelation
        previousLeft = colnames(pima_DataForCor[i])
        previousRight = colnames(pima_DataForCor[j])
        
      }
    }
    
  }
}

TitleofTable<-paste("Maximum Correlation between",previousLeft," and ",previousRight)
setPdf(TitleofTable,previousMax)

dev.off()











