package training.parallelJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class App 
{
	static Connection con;
	static Scanner sc=new Scanner(System.in);
    public static void main( String[] args )
    {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/training?serverTimezone=UTC", "root", ""); 
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Trouble connecting to database...");
		}
    	while(true) {
    		
        System.out.println("Welcome to XYZ Bank.\n Which operation do you want to perform");
        System.out.println("1 - Create Account");
        System.out.println("2 - Show Balance");
        System.out.println("3 - Deposit");
        System.out.println("4 - Withdraw");
        System.out.println("5 - Fund Transfer");
        System.out.println("6 - Print transactions");
        System.out.println("Enter choice");
        int choice = Integer.parseInt(sc.nextLine());
        switch(choice) {
        case 1:
        	new App().createAccount();
        	break;
        case 2:
        	new App().showBalance();
        	break;
        case 3:
        	new App().depositHelper();
        	break;
        case 4:
        	new App().withdrawHelper();
        	break;
        case 5:
        	new App().fundTransfer();
        	break;
        case 6:
        	new App().printTransactions();
        	break;
        default:
        	System.out.println("Invalid choice");
        }
        System.out.println("Do you wish to use another service (y/n)");
        String rep = sc.nextLine();
        if(rep.equals("n"))
        	break;
    }
    }
    private void fundTransfer() {
    	System.out.println("Enter account number from where to transfer");
		int wacn = Integer.parseInt(sc.nextLine());
		System.out.println("Enter password for that account");
		String pass = sc.nextLine();
		System.out.println("Enter amount to transfer");
		int amount = Integer.parseInt(sc.nextLine());
		System.out.println("Enter account number of receiver");
		int dacn = Integer.parseInt(sc.nextLine());
		
		//Check if the receiver account exists
		try {
			PreparedStatement stmt = con.prepareStatement("select * from accountDetails"
					+ " where accountNumber =?");
			stmt.setInt(1,dacn);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				
				System.out.println("Invalid Receiver Account number");
				System.out.println("Fund transfer unsuccessfull");
			return;
		}
			else
			{
				int res= withdraw(wacn,pass,amount);
				if(res!=0) {
					deposit(dacn,amount);
					updateTransaction(wacn,"FUND TRANSFER TO AC. NO."+dacn,amount);
					updateTransaction(dacn,"FUND TRANSFER FROM AC. NO."+wacn,amount);
					System.out.println("Fund transer successfull");
					return;
				}
				else
					System.out.println("Fund transfer unsuccessfull");
			}
		}
		catch (SQLException e) {
			System.out.println("Problem with retrieving results..."+e);
		}
		
	}
	private void withdrawHelper() {
    	System.out.println("Enter account number");
		int acn = Integer.parseInt(sc.nextLine());
		System.out.println("Enter password");
		String pass = sc.nextLine();
		System.out.println("Enter amount to withdraw");
		int amount = Integer.parseInt(sc.nextLine());
		int i=withdraw(acn,pass,amount);
		if(i!=0)
		{
			System.out.println("Withdrawl Successful");
			updateTransaction(acn,"WITHDRAWL",amount);
		}
		else
			System.out.println("Withdrawl Unsuccessful");
		
	}
	private int withdraw(int acn, String pass, int amount) {
		try {
			PreparedStatement stmt = con.prepareStatement("select accountBalance, accountPassword from accountDetails"
					+ " where accountNumber =?");
			stmt.setInt(1,acn);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if(!rs.getString("accountPassword").equals(pass)) {
					System.out.println("Invalid password");
					return 0;
				}
				else if(rs.getInt("accountBalance")<amount) {
					System.out.println("Insufficient funds");
					return 0;
				}
				
				else
				{
					stmt = con.prepareStatement("UPDATE accountdetails"
							+ " SET accountBalance=accountBalance-? "
							+ "WHERE accountNumber=?");
					stmt.setInt(1, amount);
					stmt.setInt(2, acn);
					int i= stmt.executeUpdate();
					
					if(i>0)
						return 1;
				}
		}
			else {
				System.out.println("Invalid Account number");
			return 0;}
		}
		catch (SQLException e) {
			System.out.println("Problem with retrieving results..."+e);
		}
		return 0;
	}
	private void depositHelper() {
    	System.out.println("Enter account number");
		int acn = Integer.parseInt(sc.nextLine());
		System.out.println("Enter amount to deposit");
		int amount = Integer.parseInt(sc.nextLine());
		int i=deposit(acn,amount);
		
		if(i!=0)
		{
			System.out.println("Deposit Successful");
			updateTransaction(acn,"DEPOSIT",amount);
		}
		else
			System.out.println("Deposit Unsuccessful");
    }
	private int deposit(int acn, int amount) {
		
		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE accountdetails"
					+ " SET accountBalance=accountBalance+? "
					+ "WHERE accountNumber=?");
			stmt.setInt(1, amount);
			stmt.setInt(2, acn);
			int i= stmt.executeUpdate();
			if(i>0)
				return 1;
			else return 0;
		} catch (SQLException e) {
			System.out.println("Trouble depositing money..."+e);
		}
		return 0;
	}
	private void updateTransaction(int acn, String type, int amount) {
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("insert into transactions (accountNumber, transactionType, transactionAmount"
					+ ") values (?,?,?)");
			stmt.setInt(1, acn);
			stmt.setString(2,type);
			stmt.setInt(3, amount);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	private void showBalance()
	{
		System.out.println("Enter Account Number");
		int acn= Integer.parseInt(sc.nextLine());
		System.out.println("Enter password");
		String pass = sc.nextLine();
		int balance = getBalance(acn,pass);
		if(balance!=0)
			System.out.println("The balance is: "+balance);
		
	}
	private int getBalance(int acn, String pass) {
		
		try {
			PreparedStatement stmt = con.prepareStatement("select accountHolderName, accountBalance, accountPassword from accountDetails"
					+ " where accountNumber =?");
			stmt.setInt(1,acn);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if(!rs.getString("accountPassword").equals(pass)) {
					System.out.println("Invalid password");
					return 0;
			}
				else
				{
					 return rs.getInt("accountBalance");
				}
		}
			else
				System.out.println("Invalid Account number");
			return 0;
		}
		catch (SQLException e) {
			System.out.println("Problem with retrieving results..."+e);
		}
		return 0;
		
	}
	private void printTransactions() {
		int j=0;
		System.out.println("Enter Account Number");
		int acn= Integer.parseInt(sc.nextLine());
		System.out.println("Enter password");
		String pass = sc.nextLine();
		try {
			PreparedStatement stmt = con.prepareStatement("select accountPassword from accountDetails"
					+ " where accountNumber =?");
			stmt.setInt(1,acn);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if(!rs.getString("accountPassword").equals(pass)) {
					System.out.println("Incorrect password");
					return;
				} 
			} else {
				System.out.println("Invalid account numer...");
			    return;
			}

				System.out.println("here");
		
				stmt = con.prepareStatement("select * from transactions where accountNumber = ?");
				stmt.setInt(1,acn);
				rs = stmt.executeQuery();
				j=1;
				while(rs.next()) 
				{
					System.out.print(j+" - ");
					System.out.print("Type : " + rs.getString("transactionType"));
					System.out.print(" | Amount : " + rs.getInt("transactionAmount"));
					System.out.print(" | Date : " + rs.getString("transactionDate"));
					System.out.println(" | STATUS : " + rs.getString("transactionStatus"));
					j++;
				}

		} catch (SQLException e) {
			System.out.println("Problem with verification..."+e);
		}
		
	}
	private void createAccount() {
		int acn=0;
		System.out.println("Enter your name");
		String name = sc.nextLine();
		System.out.println("Enter your password");
		String password = sc.nextLine();
		try {
			PreparedStatement stmt = con.prepareStatement("insert into "
					+ "accountDetails (accountHolderName,accountPassword)"
					+ " values (?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, password);
			int i= stmt.executeUpdate();
			if(i>0)
				System.out.println("Account creation successful...");
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				acn = rs.getInt(1);
				System.out.println("Your account number is: " + acn);
			}
			stmt= con.prepareStatement("insert into transactions (accountNumber, transactionType"
					+ ") values (?,?)");
			stmt.setInt(1, acn);
			stmt.setString(2,"ACCOUNT CREATION");
			i= stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Trouble creating new account..."+e);
		}
		
	}
}
