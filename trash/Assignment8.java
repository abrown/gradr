/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package trash;


/**
 * @author Emerole2
 *
 */
public class Assignment8 {

    public static void main(String[] args) {
        Customer Customer1 = new Customer("121212121", "Laura", "Smith", "10-02-78", "123 Main St.", "Statesboro", "GA",
                "30458", "121212121", "myemail@yahoo.com");

        Customer Customer2 = new Customer("232523252", "John", "Deal", "04-13-45", "212 S. Zetterower", "Statesboro", " GA",
                "30458", "912-489-1122", "null");

        CheckingAccount CheckingAccount1 = new CheckingAccount("111222333", 1500.75, 5175.42, 2.5, "06/02/11", "06/03/11", true);
        CheckingAccount CheckingAccount2 = new CheckingAccount("111222334", 501.80, 150.32, 1.5, "06/15/11", "06/11/11", false);

        System.out.println("Name: " + Customer1.getFirstName() + Customer1.getLastName());
        System.out.println("Account Number: " + CheckingAccount1.getaccountNum());





        System.out.println("Name: " + Customer2.getFirstName() + Customer2.getLastName());
        System.out.println("Account Number: " + CheckingAccount2.getaccountNum());


        String[][] Customer = {
            {"Laura Smith", "John Deal"},
            {"123 Main St.", "212 S. Zetterower"},
            {"Statesboro", "Statesboro"},
            {" GA", " GA"},
            {"30458", "30458"},
            {"121212121", "232523252"},
            {"10-02-78", "04-13-45"},
            {"912-489-9056", "912-489-1122"},
            {"myemail@yahoo.com", " null "}
        };








    }
}

class Transaction{
    public double amount;
}

class Customer {

    private String SSN;
    private String LastName;
    private String FirstName;
    private String DOB;
    private String address;
    private String City;
    private String State;
    private String Zip;
    private String phoneNumber;
    private String email;
    private java.util.ArrayList<Transaction> customers = new java.util.ArrayList<Transaction>();

    public Customer() {
    }

    public Customer(String SSN, String LastName, String FirstName, String DOB, String address,
            String City, String State, String Zip, String phoneNumber, String email) {

        this.SSN = SSN;
        this.LastName = LastName;
        this.FirstName = FirstName;
        this.DOB = DOB;
        this.address = address;
        this.City = City;
        this.State = State;
        this.Zip = Zip;
        this.phoneNumber = phoneNumber;
        this.email = email;



    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String sSN) {
        SSN = sSN;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String dOB) {
        DOB = dOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Customer Information\n"
                + "\nYour LastName " + LastName
                + "\nYour FirstName " + FirstName;


    }
}

class Account {

    private String accountNum;
    private Customer Cust;
    private double balance;
    private double avgBalance;
    private static double InterestRate;
    private java.util.Date dateOpened;

    public Account() {
        dateOpened = new java.util.Date();
    }

    public Account(String accountNum, Customer Cust, double balance, double avgBalance, double InterestRate) {
        this.accountNum = accountNum;
        this.Cust = Cust;
        this.balance = balance;
        this.avgBalance = avgBalance;
        Account.InterestRate = InterestRate;
        dateOpened = new java.util.Date();
    }

    public String getaccountNum() {
        return accountNum;
    }

    public void setaccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public Customer getCust() {
        return Cust;
    }

    public void setCust(Customer Cust) {
        this.Cust = Cust;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getavgBalance() {
        return avgBalance;
    }

    public void setavgBalance(double avgBalance) {
        this.avgBalance = avgBalance;
    }

    public static double getlInterestRate() {
        return InterestRate;
    }

    public static void setInterestRate(double InterestRate) {
        Account.InterestRate = InterestRate;
    }

    public java.util.Date getDateOpened() {
        return dateOpened;
    }

    public String toString() {
        return " BankAccount Information\n"
                + "\nYour accountNum " + accountNum
                + "\nYour Cust " + Cust
                + "\nYour balance " + balance
                + "\nYour avgBalance " + avgBalance
                + "\nYour InterestRate " + InterestRate
                + "\nYour dateOpened " + dateOpened;

        //String accountNum, Customer Cust, double balance, double avgBalance, double InterestRate
    }
}

class LoanAccount extends Account {

    private String type;
    private String lastPayment;
    private double Principal;
    private int numberOfYears;

    /** Default constructor */
    public LoanAccount() {
        super();
    }

    public LoanAccount(String accountNum, Customer Cust, double balance, double avgBalance, double InterestRate,
            String type, String lastPayment, double Principal, int numberOfYears) {
        super();
        this.type = type;
        this.lastPayment = lastPayment;
        this.Principal = Principal;
        this.numberOfYears = numberOfYears;

    }

    public String gettype() {
        return type;
    }

    public void settypes(String type) {
        this.type = type;
    }

    public String getlastPayment() {
        return lastPayment;
    }

    public void setlastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }

    /** Set a new numberOfYears */
    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public double getPrincipal() {
        return Principal;
    }

    /** Set a newloanAmount */
    public void setPrincipal(double Principal) {
        this.Principal = Principal;
    }

    public String toString() {
        return " LoanAccount Information\n"
                + super.toString()
                + "\nYour type " + type
                + "\nYour lastPayment " + lastPayment
                + "\nYour Principal " + Principal
                + "\nYour numberOfYears " + numberOfYears;

        //String type, String lastPayment, double Principal, int numberOfYears
    }
}

class BankAccount extends Account {

    private String lastDeposite;
    private String lastWithdrawal;

    public BankAccount() {
    }

    public BankAccount(String accountNum, Customer Cust, double balance, double avgBalance, double InterestRate,
            String lastDeposite, String lastWithdrawal) {
        super();
        this.lastDeposite = lastDeposite;
        this.lastWithdrawal = lastWithdrawal;

    }

    public String getLastDeposite() {
        return lastDeposite;
    }

    public void setLastDeposite(String lastDeposite) {
        this.lastDeposite = lastDeposite;
    }

    public String getLastWithdrawal() {
        return lastWithdrawal;
    }

    public void setLastWithdrawal(String lastWithdrawal) {
        this.lastWithdrawal = lastWithdrawal;
    }

    public String toString() {
        return " BankAccount Information\n"
                + super.toString()
                + "\nYour lastDeposite " + lastDeposite
                + "\nYour lastWithdrawal " + lastWithdrawal;
    }
}

class CheckingAccount extends BankAccount {

    protected static double overdraftCharges = 35.00;
    protected boolean overdraftProtection;

    public CheckingAccount() {
    }

    public CheckingAccount(String accountNum, double balance, double avgBalance, double InterestRate,
            String lastDeposite, String lastWithdrawal, boolean overdraftProtection) {
        super();
        CheckingAccount.overdraftCharges = overdraftCharges;
        this.overdraftProtection = overdraftProtection;
    }

    public static double getOverdraftCharges() {

        return overdraftCharges;
    }

    public static void setOverdraftCharges(double overdraftCharges) {
        CheckingAccount.overdraftCharges = overdraftCharges;
    }

    public Boolean getOverdraftProtection() {
        return overdraftProtection;
    }

    public void setOverdraftProtection(Boolean overdraftProtection) {
        this.overdraftProtection = overdraftProtection;
    }

    public String toString() {
        return " CheckingAccount Information\n"
                + super.toString()
                + "\nYour overdraftCharges " + overdraftCharges
                + "\nYour overdraftProtection " + overdraftProtection;
    }
}

class SavingsAccount extends BankAccount {

    protected static double bonus = 0.01;

    public static double getBonus() {
        return bonus;
    }

    public static void setBonus(double bonus) {
        SavingsAccount.bonus = bonus;
    }

    public String toString() {
        return " SavingsAccount Information\n"
                + super.toString()
                + "\nYour bonus " + bonus;

    }
}
