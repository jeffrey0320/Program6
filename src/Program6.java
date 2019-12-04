import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Program6 {
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        Bank bankObj = new Bank();
        TransactionTicket transTicket = new TransactionTicket();

        char choice;
        boolean notDone = true;

        File outFile = new File("testcases.txt");
        Scanner kybd = new Scanner(outFile);
        //Scanner kybd = new Scanner(System.in);
        PrintWriter outputFile = new PrintWriter("myoutput.txt");

        readAccts(bankObj);
        printAccts(bankObj, outputFile);

        do{
            menu();
            choice = kybd.next(".").charAt(0);
            switch (choice) {
                case 'q':
                case 'Q':
                    notDone = false;
                    printAccts(bankObj, outputFile);
                    break;
                case 'b':
                case 'B':
                    balance(bankObj, kybd, outputFile,transTicket);
                    break;
                case 'i':
                case 'I':
                    acctInfo(bankObj, kybd, outputFile);
                    break;
                case 'd':
                case 'D':
                    deposit(bankObj, kybd, outputFile,transTicket);
                    break;
                case 'w':
                case 'W':
                    withdrawal(bankObj, kybd, outputFile,transTicket);
                    break;
                case 'c':
                case 'C':
                    clearCheck(bankObj, kybd, outputFile);
                    break;
                case 'n':
                case 'N':
                    newAccount(bankObj, kybd, outputFile,transTicket);
                    break;
                case 'h':
                case 'H':
                    transactionHistory(bankObj, kybd, outputFile,transTicket);
                    break;
                case 's':
                case 'S':
                    closeAccount(bankObj, kybd, outputFile,transTicket);
                    break;
                case 'r':
                case 'R':
                    reopenAccount(bankObj, kybd, outputFile,transTicket);
                    break;
                case 'x':
                case 'X':
                    deleteAccount(bankObj, kybd, outputFile,transTicket);
                    break;
                default:
                    outputFile.println("Error: " + choice + " is an invalid selection -  try again");
                    outputFile.println();
                    outputFile.flush();
                    break;
            }
        } while (notDone);
        kybd.close();
        outputFile.close();
        System.out.println();
        System.out.println("The program is terminating");
    }

    private static void transactionHistory(Bank bankObj, Scanner kybd, PrintWriter outputFile,TransactionTicket ticket) {
        Account accInfo;
        Calendar date = Calendar.getInstance();
        ArrayList<TransactionReceipt> newAL;

        String requestedAccount;
        // prompt for account number
        System.out.print("Enter social security number: ");
        requestedAccount = kybd.next(); // read-in the SSN

        int index = bankObj.acctUsingSSN(requestedAccount);

        if(index == -1){
            // Account not found
            ticket = new TransactionTicket(date,"Transaction History");
            TransactionReceipt newRec = new TransactionReceipt(ticket,false,"Account not found");

            outputFile.println("Transaction Requested: " + newRec.getTicket().getTypeOfTransaction());
            outputFile.println("Date of Transaction: " + newRec.getTicket().getDateOfTransaction().getTime());
            outputFile.println("Error: " + newRec.getReasonForFailureString());
            outputFile.println();
        }else{
            accInfo = bankObj.getAccts(index);

            ticket = new TransactionTicket(date,"Transaction History");
            newAL = accInfo.getTransactionHistory(ticket,bankObj,index);

            outputFile.println("Transaction Requested: Transaction History");
            outputFile.println(accInfo.toStringAccInfo());

            for(TransactionReceipt newRec : newAL){
                if(newRec.getSuccessIndicatorFlag()){
                    if(newRec.getTicket().getTypeOfTransaction().equals("Balance Inquiry")){
                        outputFile.println(newRec.toString(bankObj,index));
                    }else if(newRec.getTicket().getTypeOfTransaction().equals("Account Info")){
                        outputFile.println("Transaction Requested: Account Info");
                        outputFile.println(accInfo.toStringAccInfo());
                        outputFile.println();
                    }else if(newRec.getTicket().getTypeOfTransaction().equals("Close Account")){
                        outputFile.println(ticket.toString());
                        outputFile.println("Account has been closed");
                        outputFile.println();
                    }else if(newRec.getTicket().getTypeOfTransaction().equals("Reopen Account")) {
                        outputFile.println(ticket.toString());
                        outputFile.println("Account has been reopened");
                        outputFile.println();
                    }else{
                        outputFile.println(newRec.toString(bankObj,index));
                    }
                }else{
                    outputFile.println(newRec.toStringError());
                }
            }
            outputFile.println("End of Transaction History.");
            outputFile.println();
        }
        outputFile.flush();
    }

    public static void readAccts(Bank bankObj) throws FileNotFoundException {
        String line;
        File myFile = new File("myinput.txt");
        Scanner cin = new Scanner(myFile);

        while(cin.hasNext()){
            line = cin.nextLine();
            String[] tokens = line.split(" ");

            Name acctName = new Name(tokens[0],tokens[1]);
            Depositor acctInfo = new Depositor(tokens[2],acctName);
            Account allInfo = new Account(Integer.parseInt(tokens[3]),tokens[4],Double.parseDouble(tokens[5]),acctInfo,true);
            bankObj.checkTypeDeposit(allInfo.getAccountType(),allInfo.getAccountBalance());
            Bank.addAmountOfAllAccts();
            bankObj.openNewAccount(allInfo);
        }
    }

    public static void printAccts(Bank bank, PrintWriter output) {
        Account clientInfo;

        output.println("\t\t\t\tClients in the Database");
        output.println();
        output.printf("%-27s%-9s%-16s%-8s%13s", "Name", "SSN",
                "Account Number", " Account Type", " Balance");
        output.println();

        for (int count = 0; count < bank.getNumAccts(); count++) {
            clientInfo = bank.getAccts(count);
            if(clientInfo.getAccountStatus())
                output.println(clientInfo.toString());
        }
        output.println();
        output.printf("Total amount in all Savings Accounts: $%.2f\n", Bank.getTotalAmountInSavingsAccts());
        output.printf("Total amount in all Checking Accounts: $%.2f\n", Bank.getTotalAmountInCheckingAccts());
        output.printf("Total amount in all CD Accounts: $%.2f\n", Bank.getTotalAmountInCDAccts());
        output.printf("Total amount in all Accounts: $%.2f\n", Bank.getTotalAmountInAllAccts());
        output.println();
        output.flush();
    }

    public static void menu(){
        System.out.println();
        System.out.println("Select one of the following transactions:");
        System.out.println("\t****************************");
        System.out.println("\t    List of Choices         ");
        System.out.println("\t****************************");
        System.out.println("\t     W -- Withdrawal");
        System.out.println("\t     D -- Deposit");
        System.out.println("\t     C -- Clear Check");
        System.out.println("\t     N -- New Account");
        System.out.println("\t     B -- Balance Inquiry");
        System.out.println("\t     I -- Account Info");
        System.out.println("\t     H -- Account Info with Transaction History");
        System.out.println("\t     S -- Close Account");
        System.out.println("\t     R -- Reopen Account");
        System.out.println("\t     X -- Delete Account");
        System.out.println("\t     Q -- Quit");
        System.out.println();
        System.out.print("\tEnter your selection: ");
    }

    public static void balance(Bank bankObj, Scanner kybd, PrintWriter outputFile,TransactionTicket ticket) {
        Account customerAcct = new Account();
        Calendar currentDate = Calendar.getInstance();

        int requestedAccount;
        int index;
        // prompt for account number
        System.out.print("Enter the account number: ");
        requestedAccount = kybd.nextInt(); // read-in the account number

        // call findAcct to search if requestedAccount exists
        index = bankObj.findAcct(requestedAccount);

        ticket = new TransactionTicket(currentDate, "Balance Inquiry");
        TransactionReceipt receiptBalance = customerAcct.getBalance(ticket, bankObj, index);

        if(receiptBalance.getSuccessIndicatorFlag()){
            outputFile.println(receiptBalance.toString(bankObj,index));
        }else{
            outputFile.println(receiptBalance.toStringError());
        }
        outputFile.flush();
    }

    public static void deposit(Bank bankObj, Scanner kybd, PrintWriter outputFile,TransactionTicket ticket) throws ParseException {
        Account customerAcct;
        Calendar currentDate;

        int requestedAccount;
        int index;
        // prompt for account number
        System.out.print("Enter the account number: ");
        requestedAccount = kybd.nextInt(); // read-in the account number
        // call findAcct to search if requestedAccount exists
        index = bankObj.findAcct(requestedAccount);

        if(index == 1){
            currentDate = Calendar.getInstance();
            ticket = new TransactionTicket(currentDate, "Deposit");
            TransactionReceipt info = new TransactionReceipt(ticket,false,"Account is not found.");

            outputFile.println(info.toStringError());
        }else{
            customerAcct = bankObj.getAccts(index);
            String accType = customerAcct.getAccountType();

            System.out.print("Enter amount to deposit: ");
            double amountToDeposit = kybd.nextDouble();

            if(accType.equals("CD")){

                System.out.print("Enter CD term: ");
                int amountOfTerm = kybd.nextInt();

                System.out.print("Enter maturity date: ");
                String dateOfMature = kybd.next();

                currentDate = Calendar.getInstance();
                ticket = new TransactionTicket(currentDate,"Deposit",amountToDeposit,amountOfTerm);
                TransactionReceipt depReceipt = customerAcct.makeDepositCD(ticket,bankObj,index,dateOfMature);

                if(depReceipt.getSuccessIndicatorFlag()){
                    outputFile.println(depReceipt.toString(bankObj,index));
                }else{
                    outputFile.println(depReceipt.toStringError());
                }
            }else{
                currentDate = Calendar.getInstance();
                ticket = new TransactionTicket(currentDate,"Deposit",amountToDeposit);
                TransactionReceipt depReceipt = customerAcct.makeDeposit(ticket,bankObj,index);

                if(depReceipt.getSuccessIndicatorFlag()) {
                    outputFile.println(depReceipt.toString(bankObj,index));
                }else{
                    outputFile.println(depReceipt.toStringError());
                }
            }
        }
        outputFile.flush();
    }

    public static void withdrawal(Bank bankObj, Scanner kybd, PrintWriter outputFile, TransactionTicket ticket) throws ParseException {
        Account customerAcct;
        Calendar currentDate;

        int requestedAccount;
        int index;
        // prompt for account number
        System.out.print("Enter the account number: ");
        requestedAccount = kybd.nextInt(); // read-in the account number
        // call findAcct to search if requestedAccount exists
        index = bankObj.findAcct(requestedAccount);

        if (index == 1) {
            currentDate = Calendar.getInstance();
            ticket = new TransactionTicket(currentDate, "Withdrawal");
            TransactionReceipt info = new TransactionReceipt(ticket, false, "Account is not found.");

            outputFile.println(info.toStringError());
        }else{
            customerAcct = bankObj.getAccts(index);
            String accType = customerAcct.getAccountType();

            System.out.print("Enter amount to withdraw: ");
            double amountToDeposit = kybd.nextDouble();

            if(accType.equals("CD")) {

                System.out.print("Enter new CD term, Choose from 6, 12, 18, or 24 months: ");
                int amountOfTerm = kybd.nextInt();

                System.out.print("Enter maturity date: ");
                String dateOfMature = kybd.next();

                currentDate = Calendar.getInstance();
                ticket = new TransactionTicket(currentDate, "Withdrawal", amountToDeposit, amountOfTerm);
                TransactionReceipt depReceipt = customerAcct.makeWithdrawalCD(ticket, bankObj, index, dateOfMature);

                if(depReceipt.getSuccessIndicatorFlag()) {
                    outputFile.println(depReceipt.toString(bankObj,index));
                }else{
                    outputFile.println(depReceipt.toStringError());
                }
            }
            else{
                currentDate = Calendar.getInstance();
                ticket = new TransactionTicket(currentDate,"Withdrawal",amountToDeposit);
                TransactionReceipt depReceipt = customerAcct.makeWithdrawal(ticket,bankObj,index);

                if(depReceipt.getSuccessIndicatorFlag()) {
                    outputFile.println(depReceipt.toString(bankObj,index));
                }else{
                    outputFile.println(depReceipt.toStringError());
                }
            }
        }
        outputFile.flush();
    }

    public static void clearCheck(Bank bankObj, Scanner kybd, PrintWriter outputFile) throws ParseException {
        Account accInfo;
        TransactionTicket newTicket;
        TransactionReceipt receiptInfo;
        Calendar transactionDate = Calendar.getInstance();

        int requestedAccount;
        int index;
        // prompt for account number
        System.out.print("Enter the account number: ");
        requestedAccount = kybd.nextInt(); // read-in the account number

        index = bankObj.findAcct(requestedAccount); // index of account

        if(index == -1)
        {
            //account not found
            transactionDate = Calendar.getInstance();
            newTicket = new TransactionTicket(transactionDate, "Clear a check");
            receiptInfo = new TransactionReceipt(newTicket, false, "Account not found");

            outputFile.println(receiptInfo.toStringError());
        }
        else
        {	// Account is found
            accInfo = bankObj.getAccts(index);				// get Account type
            String accountType = accInfo.getAccountType();

            if(accountType.equals("Checking"))
            {
                // is a checking account
                System.out.print("Enter check date: ");
                String dateOfCheck = kybd.next();			// read-in newTicket of the check

                System.out.print("Enter check amount: ");
                double checkAmount = kybd.nextDouble();		// read-in amount of check

                newTicket = new TransactionTicket(transactionDate,"Clear a check");
                Check checkInfo = new Check(requestedAccount,checkAmount,dateOfCheck);
                receiptInfo = accInfo.clearCheck(checkInfo,newTicket,bankObj,index);

                if(receiptInfo.getSuccessIndicatorFlag()){
                    outputFile.println(receiptInfo.toString(bankObj,index));
                }else{
                    outputFile.println(receiptInfo.toStringError());
                }
            }
            else
            {
                // not a checking account
                String reason = "Account is not Checking";
                transactionDate = Calendar.getInstance();
                newTicket = new TransactionTicket(transactionDate, "Clear a check");
                receiptInfo = new TransactionReceipt(newTicket, false, reason);

                outputFile.println(receiptInfo.toStringError());
            }
        }
        outputFile.flush();
    }

    public static void acctInfo(Bank bankObj, Scanner kybd, PrintWriter outputFile) {
        Account accInfo;
        String requestedAccount;
        // prompt for account number
        System.out.print("Enter social security number: ");
        requestedAccount = kybd.next(); // read-in the SSN

        for (int count = 0; count < bankObj.getNumAccts(); count++) {
            accInfo = bankObj.getAccts(count);
            if (accInfo.getPersonInfo().getSSN().equals(requestedAccount)) {
                outputFile.println("Transaction Requested: Account Info");
                outputFile.println(accInfo.toStringAccInfo());
            }
        }
        outputFile.flush();
    }

    public static void newAccount(Bank bankObj, Scanner kybd, PrintWriter outputFile,TransactionTicket ticket) {
        TransactionReceipt newReceipt;

        System.out.print("Enter New Account Number: ");
        int newAccount = kybd.nextInt();

        int index = bankObj.findAcct(newAccount);
        if(index == -1){
            if (newAccount <= 999999 && newAccount >= 100000) {
                System.out.println("Please enter your full name, Social Security Number and Account type.");
                String first = kybd.next();
                String last = kybd.next();

                String ssn = kybd.next();
                String type = kybd.next();

                String[] customerInfo = {first,last,ssn,type};
                Calendar date = Calendar.getInstance();
                ticket = new TransactionTicket(date,"New Account");
                newReceipt = bankObj.openNewAccount(ticket,bankObj,customerInfo,newAccount);

                Account acct = bankObj.getAccts(bankObj.getNumAccts()-1);

                if(newReceipt.getSuccessIndicatorFlag()){
                    outputFile.println(ticket.toString());
                    outputFile.println("Account number " + newAccount + " has been created.");
                    outputFile.println(acct.toStringAccInfo());
                }else{
                    outputFile.println(newReceipt.toStringError());
                }
            }else{
                Calendar date = Calendar.getInstance();
                ticket = new TransactionTicket(date,"Open an Account");
                newReceipt = new TransactionReceipt(ticket,false,"Account number is too small or too large.");

                outputFile.println(newReceipt.toStringError());
            }
        }
        outputFile.flush();
    }

    public static void closeAccount(Bank bankObj, Scanner kybd, PrintWriter outputFile,TransactionTicket ticket) {
        TransactionReceipt info;

        System.out.print("Enter Account Number: ");
        int closeAcct = kybd.nextInt();

        int index = bankObj.findAcct(closeAcct);

        if(index == -1){
            Calendar currentDate = Calendar.getInstance();
            ticket = new TransactionTicket(currentDate, "Close Account");
            info = new TransactionReceipt(ticket, false, "Account is not found.");

            outputFile.println(info.toStringError());
        }else{
            Account accInfo = new Account();
            Calendar currentDate = Calendar.getInstance();

            ticket = new TransactionTicket(currentDate,"Close Account");
            info = accInfo.closeAccount(ticket, bankObj, index);

            if(info.getSuccessIndicatorFlag()) {
                outputFile.println(ticket.toString());
                outputFile.println("Account has been closed.");
                outputFile.println();
            }else{
                outputFile.println(info.toStringError());
            }
        }
        outputFile.flush();
    }

    public static void reopenAccount(Bank bankObj, Scanner kybd, PrintWriter outputFile, TransactionTicket ticket)  {
        TransactionReceipt info;

        System.out.print("Enter Account Number: ");
        int openAcct = kybd.nextInt();

        int index = bankObj.findAcct(openAcct);

        if(index == -1){
            Calendar currentDate = Calendar.getInstance();
            ticket = new TransactionTicket(currentDate, "Reopen Account");
            info = new TransactionReceipt(ticket, false, "Account is not found.");

            outputFile.println(info.toStringError());
        }else{
            Account accInfo = new Account();
            Calendar currentDate = Calendar.getInstance();

            ticket = new TransactionTicket(currentDate,"Reopen Account");
            info = accInfo.reopenAccount(ticket, bankObj, index);

            if(info.getSuccessIndicatorFlag()) {
                outputFile.println(ticket.toString());
                outputFile.println("Account has been reopened.");
                outputFile.println();
            }else{
                outputFile.println(info.toStringError());
            }
        }
        outputFile.flush();
    }

    public static void deleteAccount(Bank bankObj, Scanner kybd, PrintWriter outputFile,TransactionTicket ticket) {
        TransactionReceipt newReceipt;

        System.out.print("Enter Account Number: ");
        int delAccount = kybd.nextInt();

        int index = bankObj.findAcct(delAccount);

        if(index == -1){
            Calendar date = Calendar.getInstance();
            ticket = new TransactionTicket(date,"Delete an Account");
            newReceipt = new TransactionReceipt(ticket,false,"Account number doesn't exist");

            outputFile.println(newReceipt.toStringError());
        }else{
            Calendar date = Calendar.getInstance();
            ticket = new TransactionTicket(date,"Delete an Account");
            newReceipt = bankObj.deleteAccount(ticket,bankObj,index);

            if(newReceipt.getSuccessIndicatorFlag()){
                //delete account
                outputFile.println(ticket.toString());
                outputFile.println("Account " + delAccount + " has been deleted.");
                outputFile.println();
            }else{
                //cant delete
                outputFile.println(newReceipt.toStringError());
            }
        }
        outputFile.flush();
    }
}
