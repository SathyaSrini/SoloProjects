
#Preparing the requirements

install.packages('rpart',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages('rattle',dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages("partykit",dependencies = TRUE,repos="http://cran.rstudio.com/")
install.packages("klaR",dependencies = TRUE,repos="http://cran.rstudio.com/")

#Loading libraries
library(rpart)
library(rattle)
library(partykit)
library(rpart.plot)
library(caTools)
library(klaR)


splitDataSet<-function(myData)
{
  require(caTools)
  sample = sample.split(1:NROW(myData),SplitRatio =.80)
  str(myData)
  train = subset(myData,sample == TRUE)
  str(train)
  test = subset(myData,sample == FALSE)
  str(test)
}
importance <- function(mytree) {
  
  # Calculate variable importance for an rpart classification tree
  
  # NOTE!! The tree *must* be based upon data that has the response (a factor)
  #        in the *first* column
  
  # Returns an object of class 'importance.rpart'
  
  # You can use print() and summary() to find information on the result
  
  delta_i <- function(data,variable,value) {
    # Calculate the decrease in impurity at a particular node given:
    
    #  data -- the subset of the data that 'reaches' a particular node
    #  variable -- the variable to be used to split the data
    #  value -- the 'split value' for the variable
    
    current_gini <- gini(data[,1])
    size <- length(data[,1])
    left_dataset <- eval(parse(text=paste("subset(data,",paste(variable,"<",value),")")))
    size_left <- length(left_dataset[,1])
    left_gini <- gini(left_dataset[,1])
    right_dataset <- eval(parse(text=paste("subset(data,",paste(variable,">=",value),")")))
    size_right <- length(right_dataset[,1])
    right_gini <- gini(right_dataset[,1])
    # print(paste("     Gini values: current=",current_gini,"(size=",size,") left=",left_gini,"(size=",size_left,"), right=", right_gini,"(size=",size_right,")"))
    current_gini*size-length(left_dataset[,1])*left_gini-length(right_dataset[,1])*right_gini
  }
  
  gini <- function(data) {
    # Calculate the gini value for a vector of categorical data
    numFactors = nlevels(data)
    nameFactors = levels(data)
    proportion = rep(0,numFactors)
    for (i in 1:numFactors) {
      proportion[i] = sum(data==nameFactors[i])/length(data)
    }
    1-sum(proportion**2)
  }
  
  frame <- mytree$frame
  splits <- mytree$splits
  allData <- eval(mytree$call$data)
  
  output <- ""
  finalAnswer <- rep(0,length(names(allData)))
  names(finalAnswer) <- names(allData)
  
  d <- dimnames(frame)[[1]]
  # Make this vector of length = the max nodeID
  # It will be a lookup table from frame-->splits
  index <- rep(0,as.integer(d[length(d)]))
  total <- 1
  for (node in 1:length(frame[,1])) {
    if (frame[node,]$var!="<leaf>") {
      nodeID <- as.integer(d[node])
      index[nodeID] <- total
      total <- total + frame[node,]$ncompete + frame[node,]$nsurrogate+ 1
    }
  }
  
  for (node in 1:length(frame[,1])) {
    if (frame[node,]$var!="<leaf>") {
      nodeID <- as.integer(d[node])
      output <- paste(output,"Looking at nodeID:",nodeID,"\n")
      output <- paste(output," (1) Need to find subset","\n")
      output <- paste(output,"   Choices made to get here:...","\n")
      data <- allData
      if (nodeID%%2==0) symbol <- "<"
      else symbol <- ">="
      i <- nodeID%/%2
      while (i>0) {
        output <- paste(output,"    Came from nodeID:",i,"\n")
        variable <- dimnames(splits)[[1]][index[i]]
        value <- splits[index[i],4]
        command <- paste("subset(allData,",variable,symbol,value,")")
        output <- paste(output,"      Applying command",command,"\n")
        data <- eval(parse(text=command))
        if (i%%2==0) symbol <- "<"
        else symbol <- ">="
        i <- i%/%2
      }
      output <- paste(output,"   Size of current subset:",length(data[,1]),"\n")
      
      output <- paste(output," (2) Look at importance of chosen split","\n")
      variable <- dimnames(splits)[[1]][index[nodeID]]	
      value <- splits[index[nodeID],4]
      best_delta_i <- delta_i(data,variable,value)
      output <- paste(output,"   The best delta_i is:",format(best_delta_i,digits=3),"for",variable,"and",value,"\n")
      finalAnswer[variable] <- finalAnswer[variable] + best_delta_i
      
      output <- paste(output,"                   Final answer: ",paste(finalAnswer,collapse=" "),"\n")
      
      output <- paste(output," (3) Look at importance of surrogate splits","\n")
      ncompete <- frame[node,]$ncompete
      nsurrogate <- frame[node,]$nsurrogate
      if (nsurrogate>0) {
        start <- index[nodeID]
        for (i in seq(start+ncompete+1,start+ncompete+nsurrogate)) {
          variable <- dimnames(splits)[[1]][i]
          value <- splits[i,4]
          best_delta_i <- delta_i(data,variable,value)
          output <- paste(output,"   The best delta_i is:",format(best_delta_i,digits=3),"for",variable,"and",value,"and agreement of",splits[i,3],"\n")
          finalAnswer[variable] <- finalAnswer[variable] + best_delta_i*splits[i,3]
          output <- paste(output,"                   Final answer: ",paste(finalAnswer[2:length(finalAnswer)],collapse=" "),"\n")
        }
      }
    }
  }
  result <- list(result=finalAnswer[2:length(finalAnswer)],info=output)
  class(result) <- "importance.rpart"
  result
}
print.importance.rpart <- function(self) {
  print(self$result)
}
summary.importance.rpart <- function(self) {
  cat(self$info)
}

kyfn <- function(temp=1)
{
  
kyTree <- rpart(Kyphosis ~ Age + Number + Start,
                         method="class", data=kyphosis)
fancyRpartPlot(kyTree,main="Decision Tree for Kyphosis",sub ="Analysis using Kyphosis as Class Variable")

plotcp(kyTree)
printcp(kyTree)
kyTreeCopy <- kyTree #To find the important variable

minVal <- kyTree$cptable[which.min(kyTree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation

############### IF minVal <0.02, the output is a tree otherwise its just a root###############
text(minVal)
############### IF minVal <0.02, the output is a tree otherwise its just a root###############


#Pruning tkyTreeree using Cp from Cross Validation

kyFit<- prune(kyTree, cp=minVal)

#Plotting the pruned Tree
prp(kyFit,main="Pruned Tree",sub="Using Kyphosis Tree")

plotcp(kyFit)

#splitDataSet(kyphosis) #Problem returning two values -> Keeping for later

################### Plotting for 80 percent f training ad
myData<-kyphosis
sample = sample.split(1:NROW(myData),SplitRatio =.80)
#str(myData)
train = subset(myData,sample == TRUE)
#str(train)
test = subset(myData,sample == FALSE)
#str(test)

kyTrainTree <- rpart(Kyphosis ~ Age + Number + Start,
                method="class", data=train)


#str(kyTrainTree)

fancyRpartPlot(kyTrainTree,main="Training Decision Tree for Kyphosis",sub ="Analysis using Kyphosis as Class Variable in Training Data Set")


plotcp(kyTrainTree)


minValTrain <- kyTrainTree$cptable[which.min(kyTrainTree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation

############### IF minVal <0.02, the output is a tree otherwise its just a root###############
text(minVal)
############### IF minVal <0.02, the output is a tree otherwise its just a root###############

#Pruning the tree using Cp from Cross Validation

kyTrainFit<- prune(kyTrainTree, cp=minValTrain)

#Plotting the pruned Tree
prp(kyTrainFit,main="Pruned Tree from Training Data",sub="Using Kyphosis Tree in Training Data Set")

plotcp(kyTrainFit)

predict80 <- predict(kyTrainFit,test,type=c("class"))

accuracy <- sum(test$Kyphosis==predict80)/nrow(test)

print("Printing prediction for 80 percent of data : ")

print(predict80)

plot(predict80)

print("Printing accuracy for 80 percent of data : ") 

print(accuracy*100)


#################### PLotting for 90 percent of data as training and 10 percent as testing ################################3

myData2<-kyphosis

sample = sample.split(1:NROW(myData2),SplitRatio =.90)
#str(myData)

train90 = subset(myData2,sample == TRUE)
#str(train)

test10 = subset(myData2,sample == FALSE)
#str(test)

kyTrain90Tree <- rpart(Kyphosis ~ Age + Number + Start,
                     method="class", data=train90)
#str(kyTrainTree)


fancyRpartPlot(kyTrain90Tree,main="Training Decision Tree for 90 percent data
               Kyphosis",sub ="Analysis using Kyphosis as Class Variable in Training Data Set")

plotcp(kyTrain90Tree)

minVal90Train <- kyTrain90Tree$cptable[which.min(kyTrain90Tree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation

############### IF minVal <0.02, the output is a tree otherwise its just a root###############
text(minVal90Train)
############### IF minVal <0.02, the output is a tree otherwise its just a root###############

#Pruning the tree using Cp from Cross Validation

kyTrain90Fit<- prune(kyTrain90Tree, cp=minVal90Train)


#Plotting the pruned Tree
prp(kyTrain90Fit,main="Pruned Tree from Training 90 percent Data",sub="Using Kyphosis Tree in Training Data Set")

plotcp(kyTrain90Fit)

predict90<- predict(kyTrain90Fit,test10,type=c("class"))

accuracy90 <- sum(test10$Kyphosis==predict90 )/nrow(test10)

print("Printing prediction for prediction for 90 percent of data: ")

print(predict90)

plot(predict90)

print("Printing accuracy for 90 percent of data :")
      
print(accuracy90*100)

return(kyTreeCopy)

}

soldfn <- function(temp=2)
{
  
  soldTree <- rpart(Solder ~ Panel + skips + Mask + PadType, method="class", data=solder)
  

  fancyRpartPlot(soldTree,main="Decision Tree for Solder",sub ="Analysis using Solder as Class Variable")

  plotcp(soldTree)
  
  soldTreeCopy <- soldTree #To find the important variable
  
  minVal <- soldTree$cptable[which.min(soldTree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation
  
  ############### IF minVal <0.02, the output is a tree otherwise its just a root###############
  text(minVal)
  ############### IF minVal <0.02, the output is a tree otherwise its just a root###############
  
  #Pruning the tree using Cp from Cross Validation
  
  soldFit<- prune(soldTree, cp=minVal)
  
  
  
  #Plotting the pruned Tree
 
  prp(soldFit,main="Pruned Tree",sub="Using Solder Tree")
  
  plotcp(soldFit)
  
  #splitDataSet(solder) #Problem returning two values -> Keeping for later

  
  ################## Constructing and pruning tree for 80 percent training data########################3
  myData<-solder
  
  sample = sample.split(1:NROW(myData),SplitRatio =.80)
  #str(myData)
  
  train = subset(myData,sample == TRUE)
  #str(train)
  
  test = subset(myData,sample == FALSE)
  #str(test)
  
  soldTrainTree <- rpart(Solder ~ Panel + skips + Mask + PadType,
                       method="class",data=train)

  
  
  fancyRpartPlot(soldTrainTree,main="Training Decision Tree for Solder",sub ="Analysis using Solder as Class Variable in Training Data Set")
  
  plotcp(soldTrainTree)
  
  minValTrain <- soldTrainTree$cptable[which.min(soldTrainTree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation
  
  ############### IF minVal <0.02, the output is a tree otherwise its just a root###############
  text(minValTrain)
  ############### IF minVal <0.02, the output is a tree otherwise its just a root###############
  
  #Pruning the tree using Cp from Cross Validation
  
  soldTrainFit<- prune(soldTrainTree,cp=minValTrain)
  
  
  
  #Plotting the pruned Tree
  prp(soldTrainFit,main="Pruned Tree from Training Data",sub="Using Solder Tree in Training Data Set")
  
  plotcp(soldTrainFit)
  
  predict80 <- predict(soldTrainFit,test,type=c("class"))
  
  accuracy <- (sum(test$Solder==predict80)/nrow(test)) #Printing accuracy of training set
  
  print("Printing prediction for 80 percent of data : ")
  
  print(predict80)
  
  plot(predict80)
  
  print("Printing accuracy for 80 percent of data : ") 
  
  print(accuracy*100)
  
  
  #################### PLotting for 90 percent of data as training and 10 percent as testing ################################3
  
  myData2<-solder
  
  sample = sample.split(1:NROW(myData2),SplitRatio =.90)
  #str(myData)
  
  sold90 = subset(myData2,sample == TRUE)
  #str(train)
  
  test10 = subset(myData2,sample == FALSE)
  #str(test)
  
  soldTrain90Tree <- rpart(Solder ~ Panel + skips + Mask + PadType,
                           
                         method="class", data=sold90)
  #str(kyTrainTree)
  
  fancyRpartPlot(soldTrain90Tree,main="Training Decision Tree for 90 percent data
                 Solder",sub ="Analysis using Solder as Class Variable in Training Data Set")
  
  plotcp(soldTrain90Tree)
  
  minVal90Train <- soldTrain90Tree$cptable[which.min(soldTrain90Tree$cptable[,"xerror"]),"CP"] #Calculate minimum Cp by Cross Validation
  
  ############### IF minVal <0.02, the output is a tree otherwise its just a root###############
  text(minVal90Train)
  ############### IF minVal <0.02, the output is a tree otherwise its just a root###############
  
  #Pruning the tree using Cp from Cross Validation
  
  soldTrain90Fit<- prune(soldTrain90Tree, cp=minVal90Train)
  
  #Plotting the pruned Tree
  prp(soldTrain90Fit,main="Pruned Tree from Training 90 percent Data",sub="Using Solder Tree in Training Data Set")
  
  plotcp(soldTrain90Fit)
  
  predict90<- predict(soldTrain90Fit,test10,type=c("class"))
  
  accuracy90 <- sum(test10$Solder==predict90 )/nrow(test10)
  
  print("Printing prediction for 90 percent of data: ")
  
  print(predict90)
  
  plot(predict90)
  
  print("Printing accuracy for 90 percent of data :")
  
  print(accuracy90*100)
  
  return(soldTreeCopy)
  
}

#Getting User Input

cat("Type 1 for Kyphosis Tree and 2 for Solder Tree : ") 
value <- scan("stdin", 0,  n=1, quiet = TRUE) 

#Calling Kyphosis Function


if(as.integer(value)==1)
{
  
  dev.new( width=8, height=10) #Setting Space for Decision Tree
  #Calling the construct tree function
  
  kyfn()
  
  #ImporVar <- importance(kyfn()) #http://www.redbrick.dcu.ie/~noel/R_classification.html
  
  #summary(ImporVar) #Printing the summary of the most important attribute
  
}

#Calling SolderTree Function

if(as.integer(value) ==2)
{
  
  dev.new( width=8, height=10) #Setting Space for Decision Tree
  
  soldfn() #Calling decision tree
  
  #ImporVar <- importance(soldfn()) #http://www.redbrick.dcu.ie/~noel/R_classification.html
  
  #summary(ImporVar) #Printing the summary of the most important attribute
}


