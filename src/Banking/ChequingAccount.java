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

/**
 * This class contains methods to deposit, withdraw, check balance, and report of the chequing account
 * @author VyTran
 */
public class ChequingAccount implements AccountBehaviour{
    private static String accountType = "chequing";
    private int accountID;
    private double balance=0;
    private double interest=0;
    private double initialDeposit=0;
    
    public ChequingAccount (double initialDeposit){
        //this.initialDeposit=initialDeposit;
    }

    ChequingAccount() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double deposit(double amount) {
        balance=balance+amount;
        return balance+amount;
    }

    @Override
    public double withdraw(double amount) {
        balance = balance-amount;
        return balance-amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public double getInterest() {
        if(initialDeposit>1000){
            this.interest=0.05;
        }else
            this.interest=0.02;
        return interest;
    }
    @Override
    public String toString(){
        return "Account Type: "+ getAccountType() + "Account\n" +
                "Balance: "+ this.getBalance() + "\n" + 
                "Interest rate: " + this.getInterest() + "\n";
    }

    @Override
    public String getAccountType() {
       return accountType;
    }
    
    
}
