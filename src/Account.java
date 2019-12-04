import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Account {
    private Depositor personInfo;
    private int acctNumber;
    private String acctType;
    private boolean acctStatus;
    private double acctBalance;
    private ArrayList<TransactionReceipt> arrayOfReceipts;

    //no-args constructor
    public Account() {
        personInfo = new Depositor();
        this.acctNumber = 0;
        this.acctType = "";
        this.acctBalance = 0;
        arrayOfReceipts = new ArrayList<>();
    }

    public Account(Depositor personInfo, int accountNumber, String accountType, boolean accountStatus, double accountBalance) {
        this.personInfo = personInfo;
        this.acctNumber = accountNumber;
        this.acctType = accountType;
        this.acctStatus = accountStatus;
        this.acctBalance = accountBalance;
        arrayOfReceipts = new ArrayList<>();
    }

    public Account(int parseInt, String token, double parseDouble, Depositor acctInfo, boolean accountStatus){
        this.acctNumber = parseInt;
        this.acctType = token;
        this.acctBalance = parseDouble;
        this.personInfo = acctInfo;
        this.acctStatus = accountStatus;
        arrayOfReceipts = new ArrayList<>();
    }

    public Account(String s, String token, String s1, String s2, int acctNum,boolean status) {
        personInfo = new Depositor(s1,s,token);
        acctType = s2;
        acctNumber = acctNum;
        acctStatus = status;
        arrayOfReceipts = new ArrayList<>();
    }

    // Account toString override
    public String toString(){
        Name myName = personInfo.getPersonName();

        String str = String.format("%-12s%-12s%-9s%13s%19s%-3s$%9.2f",
                myName.getFirstName(),
                myName.getLastName(),
                personInfo.getSSN(),
                this.acctNumber,
                this.acctType, " ",
                this.acctBalance);
        return str;
    }

    public String toStringAccInfo(){
        Name myName = personInfo.getPersonName();

        String str =  "Name: " + myName.getFirstName() +"\t"+ myName.getLastName() +"\n"+
                      "Social secruity number: " + personInfo.getSSN() +"\n"+
                      "Account number: " + this.acctNumber +"\n"+
                      "Account type: " + this.acctType +"\n"+
                      String.format("Account Balance: $%.2f\n", this.acctBalance);
        return str;
    }
    // copy constructor
    public Account(Account copy){
        this.personInfo = copy.personInfo;
        this.acctNumber = copy.acctNumber;
        this.acctType = copy.acctType;
        this.acctStatus = copy.acctStatus;
        this.acctBalance = copy.acctBalance;
        this.arrayOfReceipts = copy.arrayOfReceipts;
    }

    public TransactionReceipt getBalance(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt newRec;
        Account accInfo = obj.getAccts(index);

        if(index == -1) {
            String reason = "Account not found. ";
            newRec = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(newRec);
            return newRec;
        }else{
            if(accInfo.getAccountStatus()){
                double balance = accInfo.getAccountBalance();
                newRec = new TransactionReceipt(ticketInfo, true, balance);
                accInfo.addTransaction(newRec);
                return  newRec;
            }else{
                String reason = "Account is closed.";
                newRec = new TransactionReceipt(ticketInfo,false,reason);
                accInfo.addTransaction(newRec);
                return newRec;
            }
        }
    }

    public TransactionReceipt makeDeposit(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt newRec;
        Account accInfo;
        accInfo = obj.getAccts(index);
        String accType = accInfo.getAccountType();

        if(accInfo.getAccountStatus()){
            if(ticketInfo.getAmountOfTransaction() <= 0.00){
                String reason = "Invalid amount.";
                newRec = new TransactionReceipt(ticketInfo,false,reason);
                accInfo.addTransaction(newRec);
                return  newRec;
            }else{
                double balance = accInfo.getAccountBalance();
                double newBalance = balance + ticketInfo.getAmountOfTransaction();
                newRec = new TransactionReceipt(ticketInfo,true,balance,newBalance);
                accInfo.setAccountBalance(newBalance);
                obj.checkTypeDeposit(accType,ticketInfo.getAmountOfTransaction());
                accInfo.addTransaction(newRec);
                return newRec;
            }
        }else{
            String reason = "Account is closed.";
            newRec = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(newRec);
            return newRec;
        }
    }

    public TransactionReceipt makeDepositCD(TransactionTicket ticket,Bank ob,int inde,String matDate) throws ParseException {
        TransactionReceipt cdRec;
        Calendar timeNow = Calendar.getInstance();
        Calendar newDate = Calendar.getInstance();
        Account accInfo = ob.getAccts(inde);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date oDate = sdf.parse(matDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(oDate);
        String acctType = accInfo.getAccountType();

        if(accInfo.getAccountStatus()){
            if(cal.before(timeNow) || cal.equals(timeNow)){
                if(ticket.getAmountOfTransaction() <= 0.00){
                    String reason = "Invalid amount.";
                    cdRec = new TransactionReceipt(ticket,false,reason);
                    accInfo.addTransaction(cdRec);
                    return  cdRec;
                }else{
                    acctBalance = accInfo.getAccountBalance();
                    double newBalance = acctBalance + ticket.getAmountOfTransaction();
                    newDate.add(Calendar.MONTH,ticket.getTermOfCD());
                    cdRec = new TransactionReceipt(ticket,true,acctBalance,newBalance,newDate);
                    accInfo.setAccountBalance(newBalance);
                    ob.checkTypeDeposit(acctType,ticket.getAmountOfTransaction());
                    accInfo.addTransaction(cdRec);
                    return cdRec;
                }
            }else{
                String reason = "Term has not ended.";
                cdRec = new TransactionReceipt(ticket,false,reason);
                accInfo.addTransaction(cdRec);
                return cdRec;
            }
        }else{
            String reason = "Account is closed.";
            cdRec = new TransactionReceipt(ticket,false,reason);
            accInfo.addTransaction(cdRec);
            return cdRec;
        }
    }

    public TransactionReceipt makeWithdrawal(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt newRec;
        Account bal = obj.getAccts(index);
        double balance = bal.getAccountBalance();

        if(bal.getAccountStatus()){
            if(ticketInfo.getAmountOfTransaction() <= 0.0) {
                String reason = "Trying to withdraw invalid amount.";
                newRec = new TransactionReceipt(ticketInfo,false,reason,balance);
                bal.addTransaction(newRec);
                return newRec;
            }
            else if(ticketInfo.getAmountOfTransaction() > balance) {
                String reason = "Balance has insufficient funds.";
                newRec = new TransactionReceipt(ticketInfo,false,reason,balance);
                bal.addTransaction(newRec);
                return newRec;
            }
            else {
                double newBal = balance - ticketInfo.getAmountOfTransaction();
                newRec = new TransactionReceipt(ticketInfo,true,balance,newBal);
                bal.setAccountBalance(newBal);
                obj.checkTypeWithdraw(bal.getAccountType(),ticketInfo.getAmountOfTransaction());
                bal.addTransaction(newRec);
                return newRec;
            }
        }else{
            String reason = "Account is closed.";
            newRec = new TransactionReceipt(ticketInfo,false,reason);
            bal.addTransaction(newRec);
            return newRec;
        }
    }

    public TransactionReceipt makeWithdrawalCD(TransactionTicket ticket, Bank obj, int index, String openDate) throws ParseException {
        TransactionReceipt cdRec;
        Calendar timeNow = Calendar.getInstance();
        Calendar newDate = Calendar.getInstance();
        Account accInfo;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date oDate = sdf.parse(openDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(oDate);

        accInfo = obj.getAccts(index);
        double balance = accInfo.getAccountBalance();

        if(accInfo.getAccountStatus()){
            if(cal.before(timeNow) || cal.equals(timeNow)){
                if(ticket.getAmountOfTransaction() <= 0.00){
                    String reason = "Invalid amount.";
                    cdRec = new TransactionReceipt(ticket,false,reason);
                    accInfo.addTransaction(cdRec);
                    return  cdRec;
                }else if(ticket.getAmountOfTransaction() > balance)
                {
                    String reason = "Balance has insufficient funds.";
                    cdRec = new TransactionReceipt(ticket,false,reason,balance);
                    accInfo.addTransaction(cdRec);
                    return cdRec;
                }else{
                    double newBalance = balance - ticket.getAmountOfTransaction();
                    newDate.add(Calendar.MONTH,ticket.getTermOfCD());
                    cdRec = new TransactionReceipt(ticket,true,balance,newBalance,newDate);
                    accInfo.setAccountBalance(newBalance);
                    obj.checkTypeWithdraw(accInfo.getAccountType(),ticket.getAmountOfTransaction());
                    accInfo.addTransaction(cdRec);
                    return cdRec;
                }
            }else{
                String reason = "Term has not ended.";
                cdRec = new TransactionReceipt(ticket,false,reason);
                accInfo.addTransaction(cdRec);
                return cdRec;
            }
        }else{
            String reason = "Account is closed.";
            cdRec = new TransactionReceipt(ticket,false,reason);
            accInfo.addTransaction(cdRec);
            return cdRec;
        }
    }

    public TransactionReceipt clearCheck(Check checkInfo, TransactionTicket info, Bank acc, int index){
        TransactionReceipt clearedCheck;
        Account bal = acc.getAccts(index);

        Calendar timeNow = Calendar.getInstance();
        Calendar beforeSixMonths = Calendar.getInstance();
        beforeSixMonths.add(Calendar.MONTH, -6);
        Calendar check = checkInfo.getDateOfCheck();
        check.add(Calendar.MONTH,6);

        if(bal.getAccountStatus()){
            if(timeNow.before(check)) {

                double drawAmount = checkInfo.getCheckAmount();
                bal = acc.getAccts(index);
                double balance =  bal.getAccountBalance();

                if(drawAmount <= 0.0)
                {
                    String reason = "Trying to withdraw invalid amount.";
                    clearedCheck = new TransactionReceipt(info,false,reason,balance);
                    bal.addTransaction(clearedCheck);
                    return clearedCheck;
                }
                else if(drawAmount > balance)
                {
                    String reason = "Balance has insufficient funds. You have been charged a $2.50 service fee. ";
                    final double fee = 2.50;
                    double newBal = balance - fee;
                    clearedCheck = new TransactionReceipt(info,false,reason,balance,newBal);
                    bal.setAccountBalance(newBal);
                    acc.checkTypeWithdraw(bal.getAccountType(),fee);
                    bal.addTransaction(clearedCheck);
                    return clearedCheck;
                }
                else
                {
                    double newBal = balance - drawAmount;
                    clearedCheck = new TransactionReceipt(info,true,balance,newBal);
                    bal.setAccountBalance(newBal);
                    acc.checkTypeWithdraw(bal.getAccountType(),drawAmount);
                    bal.addTransaction(clearedCheck);
                    return clearedCheck;
                }
            }
            else
            {
                String reason = "The date on the check is more than 6 months ago.";
                clearedCheck = new TransactionReceipt(info,false,reason);
                bal.addTransaction(clearedCheck);
                return clearedCheck;
            }
        }else{
            String reason = "Account is closed.";
            clearedCheck = new TransactionReceipt(info,false,reason);
            bal.addTransaction(clearedCheck);
            return clearedCheck;
        }
    }

    public TransactionReceipt closeAccount(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt close;
        Account accInfo = obj.getAccts(index);

        if(accInfo.getAccountBalance()>0){
            String reason = "Account cant be close, Withdraw first.";
            close = new TransactionReceipt(ticketInfo,false,reason);
            return close;
        }else{
            if(obj.getAccts(index).acctStatus){
                accInfo = obj.getAccts(index);
                accInfo.setAccountStatus(false);
                close = new TransactionReceipt(ticketInfo,true);
                return close;
            }else{
                String reason = "Account is closed already.";
                close = new TransactionReceipt(ticketInfo,false,reason);
                return close;
            }
        }
    }

    public TransactionReceipt reopenAccount(TransactionTicket ticketInfo, Bank obj, int index){
        TransactionReceipt close;
        Account accInfo = obj.getAccts(index);

        if(obj.getAccts(index).acctStatus){
            String reason = "Account is active.";
            close = new TransactionReceipt(ticketInfo,false,reason);
            accInfo.addTransaction(close);
            return close;
        }else{
            accInfo.setAccountStatus(true);
            close = new TransactionReceipt(ticketInfo,true);
            accInfo.addTransaction(close);
            return close;
        }
    }

    public ArrayList<TransactionReceipt> getTransactionHistory(TransactionTicket ticket, Bank obj, int index) {
        Account accInfo = obj.getAccts(index);
        ArrayList<TransactionReceipt> allReceipts = accInfo.arrayOfReceipts;
        return allReceipts;
    }

    public void addTransaction(TransactionReceipt receipt){
        arrayOfReceipts.add(receipt);
    }

    private void setAccountStatus(boolean b) {
        acctStatus = b;
    }

    public Depositor getPersonInfo() {
        return new Depositor(personInfo);
    }

    public int getAccountNumber() {
        return acctNumber;
    }

    public String getAccountType() {
        return acctType;
    }

    public boolean getAccountStatus() {
        return acctStatus;
    }

    public double getAccountBalance() {
        return acctBalance;
    }

    private void setAccountBalance(double amount){
        this.acctBalance = amount;
    }

    private boolean equals(Account accInfo){
        if(acctNumber == accInfo.acctNumber)
            return true;
        else
            return false;
    }
}
