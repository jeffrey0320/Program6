import java.util.Calendar;

public class TransactionTicket {
    private Calendar dateOfTransaction;
    private String typeOfTransaction;
    private double amountOfTransaction;
    private int termOfCD;

    // no-args constructor
    public TransactionTicket(){
        dateOfTransaction = Calendar.getInstance();
        typeOfTransaction = "";
        amountOfTransaction = 0.0;
        termOfCD = 0;
    }
    // copy constructor
    public TransactionTicket(TransactionTicket copy){
        this.dateOfTransaction = copy.dateOfTransaction;
        this.typeOfTransaction = copy.typeOfTransaction;
        this.amountOfTransaction = copy.amountOfTransaction;
        this.termOfCD = copy.termOfCD;
    }

    public TransactionTicket(Calendar dateOfTransaction, String typeOfTransaction, double amountOfTransaction, int termOfCD) {
        this.dateOfTransaction = dateOfTransaction;
        this.typeOfTransaction = typeOfTransaction;
        this.amountOfTransaction = amountOfTransaction;
        this.termOfCD = termOfCD;
    }

    public TransactionTicket(Calendar currentDate, String type) {
        dateOfTransaction = currentDate;
        typeOfTransaction = type;
    }

    public TransactionTicket(Calendar currentDate, String deposit, double amountToDeposit) {
        dateOfTransaction = currentDate;
        typeOfTransaction = deposit;
        amountOfTransaction = amountToDeposit;
    }

    public String toString(){
        String str = "Transaction Requested: " + this.getTypeOfTransaction() +"\n"+
                     "Transaction Date: " + this.getDateOfTransaction().getTime();
        return str;
    }

    public Calendar getDateOfTransaction() {
        return dateOfTransaction;
    }

    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public double getAmountOfTransaction() {
        return amountOfTransaction;
    }

    public int getTermOfCD() {
        return termOfCD;
    }
}
