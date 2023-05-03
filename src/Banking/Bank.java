/**
* Assessment:   Create a banking system to handle a transaction, 
*               using JDBC Transaction to make transaction consistent.
* Date: Jan 27, 2023
* Course & Section #: 22S_CST8288_012
* Description: This Application provides Menu-Driven Console interface
*               that user can perform functions like create Account, Deposit,
*               Withdraw, and View Balance.
*/
package Banking;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class contains methods to create new client
 * @author VyTran
 */
class Bank implements Serializable{
    private DBConnection database = new DBConnection();
    
    public int addCustomer(String firstName, String lastName, String SIN, String accountType,String password, double balance){
        return database.createAccount(firstName, lastName, SIN, accountType, password, balance);
    }
    
    /**
     * this method calls getCustomer() method in DBConnection class and returns a customer
     * @param accountID
     * @return customer
     */
    public Customer getCustomer(int accountID){
        return database.getAccount(accountID);
    }
    /**
     * Display the customer list
     * @return customer list
     */
    ArrayList<Customer> getCustomerList(){
        return database.getAllCustomers();
    }
    
    /**
     * deposit to a specific amount
     * @param accountID accountID
     * @param amount amount to deposit
     */
    public void deposit(int accountID, double amount){
         Customer customer = getCustomer(accountID);
         if(amount<0){
             System.out.println("The amount must not be negative");
         }
         double balance = database.getAccountBalance(accountID);
         double interest = customer.getAccount().getInterest();
         double amountToDeposit = amount + amount*interest;
         double newBalance = balance+amountToDeposit;
         database.updateAccount(accountID, newBalance);
         System.out.println("You have deposited $"+amount+ " dollars with an interest rate of "+ interest*100+"%");
         System.out.println("Your new balance is "+newBalance);
         database.updateAccount(accountID, newBalance);
         System.out.println();
    
    }

    /**
     * withdraw from a specific amount
     * @param accountID accountID
     * @param amount amount to withdraw
     */
    public void withdraw(int accountID, double amount){
        double balance = database.getAccountBalance(accountID);
        System.out.println("balance here"+balance);
        if(amount<0){
            System.out.println("The amount must not be negative");
        }
        if(amount + 5 > balance){
            System.out.println("You have insufficient funds");
        }else{
            double newBalance = balance-amount-5;
            System.out.println("You have withdrawn $"+amount+ " dollars and incurred a fee of $5");
            System.out.println("You now have a balance of $"+newBalance);
            System.out.println();
            database.updateAccount(accountID, newBalance);
       }
    }
    /**
     * Method to check balance
     * @param accountID 
     */
    public double getBalance(int accountID){
        double balance = database.getAccountBalance(accountID);
        return balance;
    }
    /**
     * close the account
     * @param accountID
     * @return boolean
     */
    public boolean closeAccount(int accountID){
        return database.deleteAccount(accountID);
    }

    
}
