/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Banking;

/**
 *
 * @author VyTran
 */
public interface AccountBehaviour {
    public double deposit(double amount);
    public double withdraw(double amount);
    public double getBalance();
    public double getInterest();
    public String getAccountType();
}
