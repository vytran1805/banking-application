/* File: Account.java
 * Author: Stanley Pieda
 * Modified: George Kriger 2022
 * Date: 2015
 * Description: Demonstration of Strategy design pattern
 */
package Banking;

/**
 * class that represents the "context" in the Strategy Design Pattern
 * @author Stan Pieda
 * @author George Kriger
 */
public class Account {

	private double balance;
        private double interest;
        private int accountID;
        private int accountNumber;
	private AccountBehaviour accountBehaviour;
	
    /**
     * no-args constructor initializes account behavior/strategy to that of a
     * savings account
     */
    public Account(int accountID,AccountBehaviour accountBehaviour){
        this.accountID=accountID;
        this.accountBehaviour = accountBehaviour;
    }

//    /**
//     * @return the balance
//     */
//    public double getBalance() {
//        return balance;
//    }

    public void selectAccount(AccountBehaviour accountBehaviour){
        this.accountBehaviour = accountBehaviour;
            
              ;
    }

    /**
     * get the interest from accountBehaviour object
     * @return the interest
     */
    public double getInterest() {
        this.interest=accountBehaviour.getInterest();
        return interest;
    }
    
    /**
     * @return the accountNumber
     */
    public int getAccountNumber() {
        return accountID;
    }
    
    /**
     * get balance of the account
     * @return balance 
     */
    public double getBalance() {
        this.balance = accountBehaviour.getBalance();
        return balance;
    }

}
