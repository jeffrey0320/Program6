import java.util.Calendar;

public class TransactionReceipt {
    private TransactionTicket ticket;
    private boolean successIndicatorFlag;
    private String reasonForFailureString;
    private double preTransactionBalance;
    private double postTransactionBalance;
    private Calendar postTransactionMaturityDate;

    public String toStringError(){
            String str = "Transaction type: " + ticket.getTypeOfTransaction() + "\n"+
                         "Date of Transaction: " + ticket.getDateOfTransaction().getTime() + "\n" +
                         "Error: " + this.reasonForFailureString + "\n";
            return str;
    }

    public String toString(Bank obj,int index){
        Account accInfo = obj.getAccts(index);
        String accType = accInfo.getAccountType();

        if(accType.equals("CD")){
            String str = "Transaction type: " + ticket.getTypeOfTransaction() +"\n"+
                         "Date of Transaction: " + ticket.getDateOfTransaction().getTime() +"\n"+
                         String.format("Previous Balance: $%.2f",this.getPreTransactionBalance()) +"\n"+
                         String.format("Current Balance: $%.2f",this.getPostTransactionBalance()) +"\n"+
                         "New maturity date: " + this.getPostTransactionMaturityDate().getTime() +"\n";
            return str;
        }else if(this.ticket.getTypeOfTransaction().equals("Balance Inquiry")){
            String str = "Transaction type: " + ticket.getTypeOfTransaction() +"\n"+
                         "Date of Transaction: " + ticket.getDateOfTransaction().getTime() +"\n"+
                         String.format("Current Balance: $%.2f",this.getPostTransactionBalance())+"\n";
            return str;
        }
        else{
            String str = "Transaction type: " + ticket.getTypeOfTransaction() +"\n"+
                         "Date of Transaction: " + ticket.getDateOfTransaction().getTime() +"\n"+
                         String.format("Previous Balance: $%.2f",this.getPreTransactionBalance()) +"\n"+
                         String.format("Current Balance: $%.2f",this.getPostTransactionBalance())+"\n";
            return str;
        }
    }

    // no-args constructor
    public TransactionReceipt(){
        ticket = new TransactionTicket();
        successIndicatorFlag = false;
        reasonForFailureString = "";
        preTransactionBalance = 0.0;
        postTransactionBalance = 0.0;
        postTransactionMaturityDate = Calendar.getInstance();
    }
    // copy constuctor
    public TransactionReceipt(TransactionReceipt copy){
        this.ticket = copy.ticket;
        this.successIndicatorFlag = copy.successIndicatorFlag;
        this.reasonForFailureString = copy.reasonForFailureString;
        this.preTransactionBalance = copy.preTransactionBalance;
        this.postTransactionBalance = copy.postTransactionBalance;
        this.postTransactionMaturityDate = copy.postTransactionMaturityDate;
    }

    public TransactionReceipt(TransactionTicket ticket, boolean successIndicatorFlag, String reasonForFailureString,
                              double preTransactionBalance, double postTransactionBalance, Calendar postTransactionMaturityDate) {
        this.ticket = ticket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.reasonForFailureString = reasonForFailureString;
        this.preTransactionBalance = preTransactionBalance;
        this.postTransactionBalance = postTransactionBalance;
        this.postTransactionMaturityDate = postTransactionMaturityDate;
    }

    public TransactionReceipt(TransactionTicket ticket, boolean successIndicatorFlag, String reasonForFailureString) {
        this.ticket = ticket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.reasonForFailureString = reasonForFailureString;
    }

    public TransactionReceipt(TransactionTicket info,boolean flag,double balance) {
        ticket = info;
        successIndicatorFlag = flag;
        preTransactionBalance = balance;
        postTransactionBalance = balance;
    }

    public TransactionReceipt(TransactionTicket info,boolean flag,String reason, double balance) {
        ticket = info;
        successIndicatorFlag = flag;
        reasonForFailureString = reason;
        preTransactionBalance = balance;
        postTransactionBalance = balance;
    }

    public TransactionReceipt(TransactionTicket ticketInfo, boolean b, double accountBalance, double newBalance, Calendar newMatDate) {
        ticket = ticketInfo;
        successIndicatorFlag = b;
        preTransactionBalance = accountBalance;
        postTransactionBalance = newBalance;
        postTransactionMaturityDate = newMatDate;
    }

    public TransactionReceipt(TransactionTicket ticketInfo, boolean b, double accountBalance, double newBalance) {
        ticket = ticketInfo;
        successIndicatorFlag = b;
        preTransactionBalance = accountBalance;
        postTransactionBalance = newBalance;
    }

    public TransactionReceipt(TransactionTicket info, boolean b, String reason, double balance, double newBal) {
        ticket = info;
        successIndicatorFlag = b;
        reasonForFailureString = reason;
        preTransactionBalance = balance;
        postTransactionBalance = newBal;
    }

    public TransactionReceipt(TransactionTicket ticket, boolean b) {
        this.ticket = ticket;
        successIndicatorFlag = b;
    }

    public TransactionTicket getTicket() {
        return new TransactionTicket(ticket);
    }

    public boolean getSuccessIndicatorFlag() {
        return successIndicatorFlag;
    }

    public String getReasonForFailureString() {
        return reasonForFailureString;
    }

    public double getPreTransactionBalance() {
        return preTransactionBalance;
    }

    public double getPostTransactionBalance() {
        return postTransactionBalance;
    }

    public Calendar getPostTransactionMaturityDate() {
        return postTransactionMaturityDate;
    }
}
