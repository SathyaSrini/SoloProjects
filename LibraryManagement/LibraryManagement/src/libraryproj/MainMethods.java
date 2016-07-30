/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryproj;

import java.sql.*;
import java.util.Arrays;
import javax.swing.table.*;
import javax.swing.JTable;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class MainMethods {
    
    // DB CONNECTION STRINGS
    public static String user_name = "root";
    public static String password = "12345";
    public static String db_name = "library";
    public static String DbConnectUrl = "jdbc:mysql://localhost:3306/"+db_name+".db?zeroDateTimeBehavior=convertToNull";
    // DB CONNECTION STRINGS
    
    
        //****************************************** Action Methods *******************************/////////// 
        
    // LOGIN Method //
        static int authenticateUser(String userid, String passcode) {

        Connection connection = null;
        ResultSet resultSet = null;
        String perRowValueArray[] = new String[2];
        perRowValueArray[0]=null;
        perRowValueArray[1]=null;
        String url = DbConnectUrl;
	String user = user_name;
	String pswd = password;
        JOptionPane opt1 = new JOptionPane();
        int iFlag = 0;
        try 
        {
            
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pswd);
            Statement connectionStatement = connection.createStatement();
            String dbQuery = null;
            
            dbQuery = "select * from staff_users where staff_user_id ='"+ userid +"' and staff_password = '" + passcode + "';";
            resultSet = connectionStatement.executeQuery(dbQuery);
           if (!resultSet.next() ) {
                iFlag = 0;  
                connection.close();
                resultSet.close();
                System.out.println("no data");
            } 
            else
            {
                do 
                 {
                    if(!resultSet.wasNull())
                    {
                        perRowValueArray[0]=resultSet.getString("staff_user_id");
                        perRowValueArray[1]=resultSet.getString("staff_password");
                    }
                     if  ((perRowValueArray[0].equals(userid)) && (perRowValueArray[1].equals(passcode))&&!(perRowValueArray[0] == null) && !(perRowValueArray[1] == null))
            {
                iFlag = 1;
            }
            else
            {
                iFlag = 0;
                JOptionPane.showMessageDialog(null, resultSet.getString("staff_user_id") + "  & " + resultSet.getString("staff_password") ,"ERROR",JOptionPane.ERROR_MESSAGE);
            } 
                 } while (resultSet.next());
}
            connection.close();
            resultSet.close();
            return iFlag;
        } catch (SQLException e) {
                String sMsg = e.getMessage();
                System.out.println("Error : " + sMsg);
                JOptionPane.showMessageDialog(null, "Invalid input \n"+sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);
            return iFlag;
        } catch (ClassNotFoundException ex) {
            opt1.showMessageDialog(null, "Error Class not found Exception : " + ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            return iFlag;
        }
        
    }
        
    // SEARCH BOOK Method    
        static void searchBook(JTable resultTableInput, String searchByString, String inputQuery) {
       
        ResultSet rsltSet = null;
        String[] perRowValueArray = new String[6];
        Connection connection = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement stmt = connection.createStatement();
            
            switch (searchByString) {
                case "author's name":
                    rsltSet = stmt.executeQuery("select tbl1.book_id,tbl1.title,tbl1.author_name,tbl1.branch_id,tbl1.no_of_copies,(tbl1.no_of_copies-ifnull(tbl2.book_loan,0)) as available_copies from(select book.book_id,book.title,author_name,book_copies.branch_id,no_of_copies "
                        + "from book join book_authors join library_branch join book_copies "
                        + "where book_authors.author_name like '%" + inputQuery + "%' and book.book_id = book_authors.book_id and book.book_id = book_copies.book_id and library_branch.branch_id = book_copies.branch_id and book_copies.no_of_copies != 0) as tbl1 left join (select count(*) as book_loan,book_id,branch_id from book_loans where date_in is null group by branch_id,book_id) as tbl2 "
                        + "on tbl1.branch_id = tbl2.branch_id and tbl1.book_id = tbl2.book_id order by tbl1.title,tbl1.branch_id;");
                    break;
                case "book's title":
                    rsltSet = stmt.executeQuery("select tbl1.book_id,tbl1.title,tbl1.author_name,tbl1.branch_id,tbl1.no_of_copies,(tbl1.no_of_copies-ifnull(tbl2.book_loan,0)) as available_copies from(select book.book_id,book.title,author_name,book_copies.branch_id,no_of_copies "
                        + "from book join book_authors join library_branch join book_copies "
                        + "where book.title like '%" + inputQuery + "%' and book.book_id = book_authors.book_id and book.book_id = book_copies.book_id and library_branch.branch_id = book_copies.branch_id and book_copies.no_of_copies != 0) as tbl1 left join (select count(*) as book_loan,book_id,branch_id from book_loans where date_in is null group by branch_id,book_id) as tbl2 "
                        + "on tbl1.branch_id = tbl2.branch_id and tbl1.book_id = tbl2.book_id order by tbl1.title,tbl1.branch_id;");
                    break;
                case "book's id":
                    rsltSet = stmt.executeQuery("select tbl1.book_id,tbl1.title,tbl1.author_name,tbl1.branch_id,tbl1.no_of_copies,(tbl1.no_of_copies-ifnull(tbl2.book_loan,0)) as available_copies from(select book.book_id,book.title,author_name,book_copies.branch_id,no_of_copies "
                        + "from book join book_authors join library_branch join book_copies "
                        + "where book.book_id like '%" + inputQuery + "%' and book.book_id = book_authors.book_id and book.book_id = book_copies.book_id and library_branch.branch_id = book_copies.branch_id and book_copies.no_of_copies != 0) as tbl1 left join (select count(*) as book_loan,book_id,branch_id from book_loans where date_in is null group by branch_id,book_id) as tbl2 "
                        + "on tbl1.branch_id = tbl2.branch_id and tbl1.book_id = tbl2.book_id order by tbl1.title,tbl1.branch_id;");
                    break;
            }
            DefaultTableModel table = (DefaultTableModel) resultTableInput.getModel();
            table.setRowCount(0);
            while (rsltSet.next()) {
                perRowValueArray[0] = rsltSet.getString("book_id");
                perRowValueArray[1] = rsltSet.getString("title");
                perRowValueArray[2] = rsltSet.getString("author_name");
                perRowValueArray[3] = rsltSet.getString("branch_id");
                perRowValueArray[4] = rsltSet.getString("no_of_copies");
                perRowValueArray[5] = rsltSet.getString("available_copies");
                table.addRow(perRowValueArray);
            }
            connection.close();
            rsltSet.close();

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
        
    //CHECKIN BOOK Method
        static void checkInBook(int row,DefaultTableModel inputTableModel,JTable checkInInputTable){
        Connection connection = null;
        ResultSet resultSet = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement stmt = connection.createStatement();
            String loan = inputTableModel.getValueAt(row, 0).toString();
            int inputLoanId = Integer.parseInt(loan);
            Calendar calObj = Calendar.getInstance();
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");   
            String date_in = format.format(calObj.getTime());
            stmt.executeUpdate("update book_loans set date_in = '"+date_in+"' where loan_id ="+inputLoanId+";");
            JOptionPane.showMessageDialog(null, "Check in at: "+date_in+" is complete","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
        }
            
            catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
            
            JOptionPane.showMessageDialog(null, "Invalid input","ERROR",JOptionPane.ERROR_MESSAGE);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
            
        inputTableModel.removeRow(row);
        checkInInputTable.setModel(inputTableModel);
        if(checkInInputTable.getRowCount()>0)
            checkInInputTable.setRowSelectionInterval(0, 0);
    }
    
    //CHECKOUT BOOK Method    
        static void checkOutBook(String bookId,String branchId,String cardNumber){
    
        Connection connection = null;
        ResultSet resultSet = null;
        
        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name,password);
            Statement connectionStatement = connection.createStatement();
            int copies_Max = 99;
            int computedLoanId = 0;
            int isBorrowed = 0;
            int loanedCopies = 0 ;
            resultSet = connectionStatement.executeQuery("select * from book_loans order by loan_id desc limit 1;");
           
            while(resultSet.next())
            {
            String loanId = resultSet.getString("loan_id");
            computedLoanId = Integer.parseInt(loanId)+1;
            }
            
            Calendar calObj = Calendar.getInstance();
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");   
            String date_out = format.format(calObj.getTime());
            calObj.add(Calendar.DATE, 14);
            String dueDate = format.format(calObj.getTime());
            
            resultSet = connectionStatement.executeQuery("select tbl1.countOfBorrower from (select card_no,count(*) as countOfBorrower from book_loans where date_in is NULL group by card_no) as tbl1 where card_no ="+cardNumber+";");
            while(resultSet.next())
            {
               isBorrowed = Integer.parseInt(resultSet.getString("countOfBorrower"));
            }
            resultSet = connectionStatement.executeQuery("select count(*) as book_loan from book_loans where date_in is null and book_id = '"+bookId+"' and branch_id = "+branchId+" group by branch_id,book_id;");
            while(resultSet.next())
            {
                loanedCopies = Integer.parseInt(resultSet.getString("book_loan"));
       
            }   
            resultSet = connectionStatement.executeQuery("select no_of_copies from book_copies where book_id = '"+bookId +"' and branch_id ="+branchId+ ";");
            while(resultSet.next())
            {
                copies_Max = Integer.parseInt(resultSet.getString("no_of_copies"));
            }
            if(isBorrowed == 3)
            {
                JOptionPane.showMessageDialog(null, "Borrower can't borrow more than 3 books!","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else if(copies_Max == loanedCopies)
            {
                JOptionPane.showMessageDialog(null, "All copies of this book has been lent","ERROR",JOptionPane.ERROR_MESSAGE);

            }
            else
            {
        
                connectionStatement.executeUpdate("insert into book_loans values('"+computedLoanId+"','"+bookId+"','"+branchId+"','"+cardNumber+"','"+date_out+"','"+dueDate+"','0000-00-00');"  );
             
            }
        }
        catch (SQLException e) {
            
            String sMsg = e.getMessage();
            System.out.println("Error : " + sMsg);
            JOptionPane.showMessageDialog(null, "Invalid input!\n " + sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);            

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            
        }
    }
    
    //PAY FINE Method    
        static void payFinesButtonPressed(int inputRow,DefaultTableModel inputFineSearchTable,JTable inputPayFineTable){
         Connection connection = null;
         ResultSet resultSet = null;
         String[] perRowValueArray = new String[3];
         
        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement connectionStatementCheckDate = connection.createStatement();
            Statement connectionStatementToPayFines = connection.createStatement();

            String loanIdAsString = inputFineSearchTable.getValueAt(inputRow, 0).toString();
            int loanId = Integer.parseInt(loanIdAsString);
             inputFineSearchTable = (DefaultTableModel) inputPayFineTable.getModel();
             resultSet = connectionStatementCheckDate.executeQuery("select fines.loan_id,date_in from book_loans,fines where book_loans.loan_id = fines.loan_id and fines.loan_id = "+loanId+";");
             while(resultSet.next())
             {
                 System.out.println(resultSet.getString("date_in"));
                 
                 if(resultSet.getString("date_in") == null)
                 
                     JOptionPane.showMessageDialog(null, "Book has to be returned before paying fines. Cannot Approve","ERROR",JOptionPane.ERROR_MESSAGE);
                
                 else           
                 {
                     connectionStatementToPayFines.executeUpdate("update fines set paid = 1 where loan_id = "+loanId+" ;");
                     JOptionPane.showMessageDialog(null, "Cumulative fines paid succesfully","sucess",JOptionPane.INFORMATION_MESSAGE);
                 }
                 

                     
             }
            
              }
        
         catch (SQLException e) {
                String sMsg = e.getMessage();
                System.out.println("Error : " + sMsg);
                JOptionPane.showMessageDialog(null, "Invalid input \n"+sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
        
    //ADD BORROWER Method
        static void addBorrower(String firstName,String lastName,String mailId,String address,String city, String state, String phone){
            Connection connection = null;
            ResultSet resultSetForCardNumber = null;
            ResultSet resultSetForInsert = null;
            int generatedCardNumber = 0;
           try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name,password);
            Statement stmt = connection.createStatement();
            resultSetForCardNumber = stmt.executeQuery("select * from borrower order by card_no desc limit 1;");
            while(resultSetForCardNumber.next())
            {
            String card_no = resultSetForCardNumber.getString("card_no");
            generatedCardNumber = Integer.parseInt(card_no)+1;
            }
            String sqlStmt = "";
            sqlStmt = "select * from borrower where "
                    + "upper(fname) = trim(upper('" + firstName
                    + "')) and upper(lname) = trim(upper('" + lastName
                    + "')) and upper(address) = trim(upper('" + address
                    + "')) and upper(city) = trim(upper('" + city
                    + "')) and upper(state) = trim(upper('" + state
                    + "')) and upper(email) = trim(upper('" + mailId
                    + "')) and upper(phone) = trim(upper('" + phone
                    + "'));";
            resultSetForInsert = stmt.executeQuery(sqlStmt);
            if(resultSetForInsert.next() == false)
            {
                sqlStmt = "insert into borrower(card_no,fname,lname,email,address,city, state, phone) values("
                          +String.valueOf(generatedCardNumber)+",'"
                          +firstName+"','"
                          +lastName+"','"
                          +mailId+"','"
                          +address+"','"
                          +city+"','"
                          +state+"','"
                          +phone
                        +"');";
                stmt.executeUpdate(sqlStmt);
                JOptionPane.showMessageDialog(null, "Added Borrower Succesfully\n" 
                        + " Please note the Card No : " + String.valueOf(generatedCardNumber),
                        "MESSAGE",JOptionPane.INFORMATION_MESSAGE);
            }
            else 
                JOptionPane.showMessageDialog(null, "Borrower already exists","ERROR",JOptionPane.ERROR_MESSAGE);

           }
           catch (SQLException e) {
              String sMsg = e.getMessage();
              System.out.println("Error : " + sMsg);
              JOptionPane.showMessageDialog(null, sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);
            
            }catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            
            }
    }
        //****************************************** Action Methods *******************************/////////// 
    
    //*********************************************************************************************************************////

        /////////****************************************** Helper Methods *******************************///////////  
    //SEARCH FOR CHECKIN Method        
        static DefaultTableModel checkInSearch(JTable checkInInputTable,String searchByInputString,String input){
        Connection connection = null;
        ResultSet resultSet = null;
        String perRowValueArray[] = new String[7];

         try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement stmt = connection.createStatement();
            switch (searchByInputString) {
                case "Book id":
                    resultSet = stmt.executeQuery("select * from book_loans where date_in is NULL and book_id like '%"+input+"%';");
                    break;
                case "Card number":
                    Integer.parseInt(input);
                    resultSet = stmt.executeQuery("select * from book_loans where date_in is NULL and card_no = "+input+";");
                    break;
                case "Borrower name":
                    resultSet = stmt.executeQuery("select loan_id,book_id,branch_id,card_no,date_out,due_date,date_in " +
                            "from(select loan_id,book_id,branch_id,book_loans.card_no,date_out,due_date,date_in,fname,lname from book_loans,borrower where book_loans.card_no = borrower.card_no and date_in is null) as tbl1 " +
                            "where tbl1.fname like '%"+input+"%' or tbl1.lname like '%"+input+"%' ;");
                    break;       
            }
            DefaultTableModel tablemodel = (DefaultTableModel)checkInInputTable.getModel();
            tablemodel.setRowCount(0);
            
            while(resultSet.next())
            {
                perRowValueArray[0] = resultSet.getString("loan_id");
                perRowValueArray[1] = resultSet.getString("book_id");
                perRowValueArray[2] = resultSet.getString("branch_id");
                perRowValueArray[3] = resultSet.getString("card_no");
                perRowValueArray[4] = resultSet.getString("date_out");
                perRowValueArray[5] = resultSet.getString("due_date");
                perRowValueArray[6] = resultSet.getString("date_in");
                
                tablemodel.addRow(perRowValueArray);
                
            }
             System.out.println(Arrays.toString(perRowValueArray));
             
            if(checkInInputTable.getRowCount()>0)
            checkInInputTable.setRowSelectionInterval(0, 0);
            
            return tablemodel;
         }
         catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
            
            JOptionPane.showMessageDialog(null, "Invalid input","ERROR",JOptionPane.ERROR_MESSAGE);
            return null;

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    //RELOAD FINE Method
        static DefaultTableModel reloadFineTable(JTable inputFineTable){
        Connection connection = null;
            ResultSet resultSet = null;
            String[] perRowValueArray = new String[4];
            try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement connectionStatement = connection.createStatement();
            connectionStatement.executeUpdate("INSERT IGNORE INTO fines(loan_id,fine_amt) SELECT tbl1.loan_id,tbl1.fine_amt FROM (select loan_id,datediff(curdate(),due_date)*0.25 as fine_amt from book_loans where date_in is null and curdate() > due_date UNION select loan_id,datediff(date_in,due_date)*0.25 as fine_amt from book_loans where date_in is not null and date_in > due_date) AS tbl1;");
            resultSet = connectionStatement.executeQuery("select tbl1.card_no,fname,lname,fine_amt from (select card_no,sum(fine_amt) as fine_amt from fines,book_loans where paid = 0 or paid is NULL and fines.loan_id = book_loans.loan_id group by card_no) AS tbl1,borrower where tbl1.card_no = borrower.card_no;");
            DefaultTableModel inputTableModel = (DefaultTableModel) inputFineTable.getModel();
            inputTableModel.setRowCount(0);
             
            while(resultSet.next())
            {
                perRowValueArray[0] = resultSet.getString("card_no");
                perRowValueArray[1] = resultSet.getString("fname");
                perRowValueArray[2] = resultSet.getString("lname");
                perRowValueArray[3] = resultSet.getString("fine_amt");
                inputTableModel.addRow(perRowValueArray);
            }
            
            resultSet.close();
            
            return inputTableModel;
            
            }
           
             catch (SQLException e) {
                String sMsg = e.getMessage();
                System.out.println("Error : " + sMsg);
                JOptionPane.showMessageDialog(null, sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);
                return null;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
            
        }
        
    }
    
    //DISPLAY FINE Method    
        static DefaultTableModel displayFines(int inputRow,DefaultTableModel inputTable,JTable inputShowFineTable){
         Connection connection = null;
         ResultSet resultSet = null;
         String[] perRowValueArray = new String[3];

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement connectionStatement = connection.createStatement();
            String cardNumberAsString = inputTable.getValueAt(inputRow, 0).toString();
            int cardNumber = Integer.parseInt(cardNumberAsString);
            resultSet = connectionStatement.executeQuery("select book_loans.loan_id,fine_amt,paid from book_loans,fines where book_loans.card_no = "+cardNumber +" and book_loans.loan_id = fines.loan_id;");
            DefaultTableModel fineTableModel = (DefaultTableModel) inputShowFineTable.getModel();
            fineTableModel.setRowCount(0);
            while (resultSet.next())
            {
                perRowValueArray[0] = resultSet.getString("loan_id");
                perRowValueArray[1] = resultSet.getString("fine_amt");
                int result = resultSet.getInt("paid");
                if(result == 1)
                    perRowValueArray[2] = "yes";
                else
                    perRowValueArray[2] = "no";
                fineTableModel.addRow(perRowValueArray);
                        
            }
            return fineTableModel;
        }
        catch (SQLException e) {
                String sMsg = e.getMessage();
                System.out.println("Error : " + sMsg);
                JOptionPane.showMessageDialog(null, "Invalid input\n"+sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);
                return null;

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        
    }
    
    //DISPLAY FINES FOR CARDNUMBER
        static DefaultTableModel cardNumberSpecificFines(int row, int iCardNo,JTable fineTable2){
         Connection connection = null;
         ResultSet resultSet = null;
         String[] perRowValueArray = new String[3];

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnectUrl, user_name, password);
            Statement connectionStatement = connection.createStatement();
            resultSet = connectionStatement.executeQuery("select book_loans.loan_id,fine_amt,paid from book_loans,fines where book_loans.card_no = "+iCardNo +" and book_loans.loan_id = fines.loan_id;");
            DefaultTableModel inputFineTableModel = (DefaultTableModel) fineTable2.getModel();
            inputFineTableModel.setRowCount(0);
            while (resultSet.next())
            {
                perRowValueArray[0] = resultSet.getString("loan_id");
                perRowValueArray[1] = resultSet.getString("fine_amt");
                int result = resultSet.getInt("paid");
                if(result == 1)
                    perRowValueArray[2] = "yes";
                else
                    perRowValueArray[2] = "no";
                inputFineTableModel.addRow(perRowValueArray);
                        
            }
            return inputFineTableModel;
        }
        catch (SQLException e) {
                String sMsg = e.getMessage();
                System.out.println("Error : " + sMsg);
                JOptionPane.showMessageDialog(null, "Invalid input \n"+sMsg,"ERROR",JOptionPane.ERROR_MESSAGE);
                return null;

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        
    }
    
        /////////****************************************** Helper Methods *******************************///////////

}
