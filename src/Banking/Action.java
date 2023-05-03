
/**
* Assessment:   Create a banking system to handle a transaction, 
*               using JDBC Transaction to make transaction consistent.
* Date: Feb 05, 2023
* Original by Vy Tran
* Description: This Application provides Menu-Driven Console interface
*              that user can perform functions like create Account, Deposit,
*              Withdraw, and View Balance.
*/
package Banking;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains information of a client
 * @author VyTran
 */
public class Action {
    Bank bank = new Bank();
    Scanner input = new Scanner(System.in);
    boolean exit;
     
    public int choice;

    /**
     * main method
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Action action = new Action();
        action.runMenu();
    }
//end of main method
    
    private void runMenu(){
        printHeader();
        while(!exit){
           printMenu();
           implementAction(getInput());
        }
    }
    /**
     * method to print menu header
     */
    public void printHeader(){
        System.out.println("Welcome to V's Bank App");
        System.out.println("---------&&&&----------");
    }
    /**
     * method to print menu option
     */
    public void printMenu(){
        System.out.println("How can I help you?");
        System.out.println("1. Create new account");
        System.out.println("2. Make a deposit");
        System.out.println("3. Make a withdrawal");
        System.out.println("4. Check account balance");
        System.out.println("5. Exit");
    }
    /**
     * This method gets input from customer and catch the error of invalid input and out of range input
     * @return choice customer's selection
     */
    private int getInput(){
        do{
            System.out.println("Please choose 1 option");
            try{
                choice = Integer.parseInt(input.nextLine());    //parse the input string to find an integer and assign to choice
            }
        // if the input is not a number then print the message
            catch(NumberFormatException e){
                System.out.println("Invalid answer. Number only please");
            }
            if (choice < 1 || choice > 5){
                System.out.println("Out of range. Please choose again!");
            }
        }while (choice < 1 || choice > 5);
        return choice;
    }
    
    /**
     * This method implements user's choice
     */
    private void implementAction(int choice){
        switch(choice){
            case 1: //create Account
                createAccount();
                break;
            case 2: //make a deposit
                makeDeposit();
                break;
            case 3: //make a withdrawal
                makeWithdraw();
                break;
            case 4: //check account balance
                getBalance();
                break;
            case 5:
                System.out.println("Thank you for using our services");
                System.exit(0);
                break;
        }
    }
    /**
     * Method to create new account (should be in Client class)
     */
    private void createAccount() {
        Account account = null;
        String lastName;
        String firstName;
        String SIN;
        String accountType="";
        String password;
        double initialDeposit=0;
        double balance=0;
        Boolean valid=false;
        do{
            System.out.println("Do you want to open a chequing or saving accoutn?");
            accountType = input.nextLine();
            if (accountType.equalsIgnoreCase("Chequing") || accountType.equalsIgnoreCase("Checking") || accountType.equalsIgnoreCase("Saving")){    
            valid = true;
            }else{
                System.out.println("Please enter valid Account type!");
            }
        }while(!valid);  
        
//        Ask for customer information
        System.out.println("Please enter your first name");
        firstName = input.nextLine();
        System.out.println("Please enter your last name");
        lastName = input.nextLine();
        System.out.println("Please enter your Social Insurance Number");
        SIN = input.nextLine();
        System.out.println("Please enter password");
        password = input.nextLine();
        valid = false;
        
//        Ask for initial deposit
        while(!valid){
            System.out.println("Please enter an initial deposit");
            // handle the wrong input type
            try{
                initialDeposit = Double.parseDouble(input.nextLine());    //parse the input string to find a double and assign to initialDeposit
            }
            // if the input is not a number then print the message
            catch(NumberFormatException e){
                System.out.println("Please enter a number");
            }
            // if entered number then check minimum deposit constraint for each account
            if(accountType.equalsIgnoreCase("chequing")||accountType.equalsIgnoreCase("checking")){
                if(initialDeposit<100){
                    System.out.println("Chequing account requires a minimum of $100 to begin with");
                }else{  
                    //when customer want to open chequing account and deposit at least $100
                    valid=true;
//                    balance = account.getBalance()+initialDeposit;
                    bank.addCustomer(firstName,lastName,SIN,accountType,password,initialDeposit);
                    System.out.println("Your Chequing Account has been created successfully!");
                }
            }else if (accountType.equalsIgnoreCase("saving")){
                if(initialDeposit<50){
                    System.out.println("Saving account requires a minimum of $50 to begin with");
                }else{
                    //when customer want to open saving account and deposit at least $50
                    // then create saving account
                    valid = true;
//                    balance = account.getBalance()+initialDeposit;
                    bank.addCustomer(firstName,lastName,SIN,accountType,password,initialDeposit);
                    System.out.println("Your Saving Account has been created successfully!");
                }  
            }    
        }
    }
    /**
     * Method to make a deposit
     */
    private void makeDeposit() {
        boolean valid = true;
        double amount=0;
        int account = selectAccount();  //choose the type of account to deposit
        do{
            if (account>=0){
            System.out.println("How much would you like to deposit?");
            try{
                amount=Double.parseDouble(input.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Please enter a number");
                valid = false;
            }
            valid=true;
            }
        }while(!valid);
    
/*
    From object bank, call method getClient() to return account in ArrayList
    then implement getAccount() in Client class to return account number,
    finally implement deposit method from the coresponding Chequing/Saving class
*/
        bank.deposit(account,amount);
    
    }
    /**
     * method to make a withdrawal
     */
    private void makeWithdraw() {
        int account = selectAccount();
        double amount = 0;
        boolean valid =false;
        do{
            if (account>=0){
                System.out.println("How much would you like to withdraw?");
                try{
                    amount=Double.parseDouble(input.nextLine());
                }catch(NumberFormatException e){
                    System.out.println("Please enter a number");
                    valid = false;
                }
                valid = true;
            }
        }while(!valid);
         bank.withdraw(account,amount);
    }
    /**
     * check account balance
     */
    private void getBalance() {
        int account = selectAccount();
        double balance = bank.getBalance(account);
        System.out.println("Balance: "+balance);
    }
    /**
     * select account to deposit/withdraw
     * @return  account
     */
    private int selectAccount() {
//the list of clients in bank class
        ArrayList<Customer> customers = bank.getCustomerList();
        boolean valid=true;
        if(customers.size()<=0){
            System.out.println("No customer at your bank");
            return -1;  //terminate the method
        }
        System.out.println("Select an account");
        //print the information of all the client from the list of clients above
        for (int i=0;i<customers.size();i++){
            System.out.println((i+1)+") "+customers.get(i).clientInfo());
        }
        int account = 0;
        do{
            try{
                System.out.println("Please enter your account");
            //take the input of customer and return coresponding account number
            //The list started at 1 so in order to return customer with the correct index, we need to subtract the input for 1
                account = Integer.parseInt(input.nextLine())-1;     //this s the index of the customer list
                
            }catch(NumberFormatException e){
                System.out.println("Invalid account number!");
                valid = false;
            }
            valid = true;
        }while(!valid);
        return account+1;   //this returns the row of the customer list
    }
}
