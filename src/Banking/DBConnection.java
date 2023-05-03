/**
 * This class returns a Global Databases Connection using the object of connection
 */
package Banking;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is to establish a connection to the server
 * @author VyTran
 */
public class DBConnection {
    private Connection connection;
    Properties props = new Properties();

    
    /**
     * Establish the connection
     * @return connection object
     */
    public Connection connect(){
//        try(InputStream in = Files.newInputStream(Paths.get("src/Banking.sql/database.properties"));){
//                props.load(in);
//            } catch (IOException ex) {
//                //Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
//                ex.printStackTrace();
//            }
//        String url = props.getProperty("jdbc.url");
//        String username = props.getProperty("jdbc.username");
//        String password = props.getProperty("jdbc.password");
        
        String url = "jdbc:mysql://localhost:3306/bank";
        String username = "bank";
        String password = "CST8288";
        try{
            connection = DriverManager.getConnection(url,username,password);
        }catch(SQLException e){
            connection = null;
            System.out.println("Connection failed!");
            System.err.println(e.getMessage());
        }    
        return connection;
    }
    /**
     * The method to insert new Customer, Account to tables
     * @param firstName First name
     * @param lastName  Last name
     * @param SIN   Social Insurance Number
     * @param accountType   Saving or Chequing
     * @param balance   Account's balance
     * @param password  password
     * @return accountID
     */
    public int createAccount(String firstName, String lastName, String SIN, String accountType, String password, double balance){
        int customerID=0;     
        int accountID=0;
        Connection connection = connect();
        
        try{          
            if(lastName == null || firstName == null || password == null){
                System.out.println("All Field Required!");
            }
        // disable autoCommit so it won't commit the queries until we explicitly call the commit    
        //connection.setAutoCommit(false);
/**
 * Insert new Customer record to the table Customers
 */  
        //prepared Update statement is a normal sql that uses "?" as placeholders, it sanitizes user's input
        String addCustomerSql = "INSERT INTO CUSTOMERS(FirstName, LastName, SIN, Password) values(?,?,?,?)";
        //create a prepared statement with RETURN_GENERATED_KEYS which allows us to access the ID key that gets created
        PreparedStatement addCustomer = connection.prepareStatement(addCustomerSql, Statement.RETURN_GENERATED_KEYS);
        // set the question mark position to the coresponding value
        addCustomer.setString(1,firstName);
        addCustomer.setString(2, lastName);
        addCustomer.setString(3, SIN);
        addCustomer.setString(4, password);
        addCustomer.executeUpdate();    //run the update statement (Insert, delete, create..)
        ResultSet addCustomerResult = addCustomer.getGeneratedKeys();  //this line of code is to access the generated key
        if(addCustomerResult.next()){  //if this result has a value
            customerID = addCustomerResult.getInt(1);  //getInt() is to get value of the first column which is the CustomerID field
        }
 /**
 * Insert new Account record to the table Account
 */ 
        String addAccountSql = "INSERT INTO ACCOUNTS(AccType,Balance) values(?,?)";
        // create a prepared statement with RETURN_GENERATED_KEYS which allows us to access the primary ID key that gets created
        PreparedStatement addAccount = connection.prepareStatement(addAccountSql, Statement.RETURN_GENERATED_KEYS);
        // set the question mark position to the coresponding value
        addAccount.setString(1,accountType);
        addAccount.setDouble(2, balance);
        // run the update statement
        addAccount.executeUpdate();
        // get the generated key
        ResultSet addAccountResult = addAccount.getGeneratedKeys();
        // if the result has value then get the value of the first column of table Accounts
        if(addAccountResult.next()){
            accountID = addAccountResult.getInt(1);
        }
 /**
 * Link Customer to their Account by Inserting data into table Customer_Account
 */       
        //Check if the CustomerID is existed
        if(customerID>0 && accountID>0){
            String linkAccountSql = "INSERT INTO Customer_Account(CustomerID,AccountID) values(?,?)";
            // we don't need to return a generated key this time
            PreparedStatement linkAccount = connection.prepareStatement(linkAccountSql);
            linkAccount.setInt(1,customerID);
            linkAccount.setInt(2, accountID);
            linkAccount.executeUpdate();
        }else{  //if there is no customer or account record in the tables
            connection.rollback();
        }
        connection.close();
        }catch(SQLException e){
            System.err.println("An error has occured"+ e.getMessage());
        }
        return accountID;
    }
    /**
     * Find the Customer info by checking their accountID, then create account
     * @param accountID
     * @return 
     */
    public Customer getAccount(int accountID){
        Customer customer = null;
        Connection connection = connect();
        try{
            
            String getCustomerSql = "SELECT FirstName, LastName, SIN, Password, AccType, Balance from Customers as C "
                + "join Customer_Account as CA on C.CustomerID = CA.CustomerID "
                + "join Accounts as A on A.AccountID = CA.AccountID where A.AccountID = ?";
            PreparedStatement getCustomer = connection.prepareStatement(getCustomerSql);
            getCustomer.setInt(1,accountID);    //assign the accountID to the first question mark of the prepared statement
            ResultSet getCustomerResult = getCustomer.executeQuery();
            if(getCustomerResult.next()){
                String firstName = getCustomerResult.getString("FirstName");   //get value of the column FirstName
                String lastName = getCustomerResult.getString("LastName");   //get value of the column LastName
                String SIN = getCustomerResult.getString("SIN");   //get value of the column SIN
                String password = getCustomerResult.getString("Password");   //get value of the column AccType
                String accountType = getCustomerResult.getString("AccType");   //get value of the column AccType
                double balance = getCustomerResult.getDouble("Balance");   //get value of the column Balance
        // Now we actually create a new saving/chequing account
                Account account=null;
                if (accountType.equalsIgnoreCase("Chequing") || accountType.equalsIgnoreCase("Checking")){
                    account = new Account(accountID, new ChequingAccount(balance));
                }else if(accountType.equalsIgnoreCase("saving")){
                    account = new Account(accountID, new SavingAccount(balance));
                }
                customer = new Customer(firstName,lastName,SIN,password,account);
            }else{
                System.err.println("No Customer account was found for ID "+ accountID);
            }
            
        }catch(Exception e){
            connection = null;
            System.err.println(e.getMessage());
        }
        return customer;
    }
    
    public double getAccountBalance(int accountID){
        Connection connection = connect();
        double balance=0;
        try{
            
            String getBalanceSql = "SELECT * from Accounts WHERE accountID = ?";
            PreparedStatement getBalance = connection.prepareStatement(getBalanceSql);
            getBalance.setInt(1,accountID);    //assign the accountID to the first question mark of the prepared statement
            ResultSet getBalanceResult = getBalance.executeQuery();
            if (getBalanceResult.next()){
                balance = getBalanceResult.getDouble("Balance");   //get value of the column Balance
            }else{
                System.err.println("No Customer account was found for ID "+ accountID);
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        return balance;
    }
    /**
     * Update an account(withdraw/deposit)
     * @param accountID AccountID
     * @param balance   balance
     * @return 
     */
    public boolean updateAccount(int accountID, double balance){
        boolean success = false;
        Connection connection = connect();
        try{
            String updateBalanceSql = "UPDATE Accounts SET Balance = ? WHERE accountID = ?";
            PreparedStatement updateBalance = connection.prepareStatement(updateBalanceSql); 
            updateBalance.setDouble(1,balance);
            updateBalance.setInt(2,accountID);
            updateBalance.executeUpdate();
            success = true;
        }catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return success;
    }
        /**
     * Delete an account (also customer)
     * @param accountID AccountID
     * @param balance   balance
     * @return 
     */
    public boolean deleteAccount(int accountID){
        boolean success = false;
        Connection connection = connect();
        try{
            String deleteAccountSql = "DELETE Customers, Accounts "
                    + "FROM Customers C JOIN Customer_Account CA ON C.CustomerID = CA.CustomerID "
                    + "JOIN Accounts A ON A.AccountID = CA.AccountID WHERE A.AccountID = ?";
            PreparedStatement deleteAccount = connection.prepareStatement(deleteAccountSql); 
            deleteAccount.setDouble(1,accountID);

            success = true;
        }catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return success;
    }
    /**
     * Display all the Customers
     * @return 
     */
    ArrayList<Customer> getAllCustomers(){
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            Connection connection = connect();
            String findAllCustomersSql = "SELECT FirstName, LastName, SIN, Password, AccountID, AccType, Balance FROM Customers, Accounts "
                    + "where Customers.customerID = (select customer_account.customerID from customer_account "
                    + "where customer_account.accountID = accounts.accountID)";
            PreparedStatement findAllCustomers = connection.prepareStatement(findAllCustomersSql);
            ResultSet findAllCustomersResult = findAllCustomers.executeQuery();
            while(findAllCustomersResult.next()){
                String firstName = findAllCustomersResult.getString("FirstName");
                String lastName = findAllCustomersResult.getString("LastName");
                String SIN = findAllCustomersResult.getString("SIN");
                String accountType = findAllCustomersResult.getString("AccType");
                String password = findAllCustomersResult.getString("Password");
                double balance = findAllCustomersResult.getDouble("Balance");
                int accountID = findAllCustomersResult.getInt("AccountID");
                Account account=null;
                if (accountType.equalsIgnoreCase("Chequing") || accountType.equalsIgnoreCase("Checking")){
                    account = new Account(accountID,new ChequingAccount(balance));
                }else if(accountType.equalsIgnoreCase("saving")){
                    account = new Account(accountID, new SavingAccount(balance));
                }
                //Add customer to the customer list
                customers.add(new Customer(firstName, lastName, SIN, password,account));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customers;
    }
}