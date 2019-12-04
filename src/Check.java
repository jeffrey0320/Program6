import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Check {
    private int accountNumber;
    private double checkAmount;
    private String dateOfCheck;
    private Calendar calFormat;

    public Check(){
        accountNumber = 0;
        checkAmount = 0.0;
        dateOfCheck = "";
    }

    public Check(int accountNumber, double checkAmount, String dateOfCheck) throws ParseException {
        this.accountNumber = accountNumber;
        this.checkAmount = checkAmount;
        this.dateOfCheck = dateOfCheck;
        calFormat = newDate(dateOfCheck);
    }

    public Calendar newDate(String ate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse(ate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getCheckAmount() {
        return checkAmount;
    }

    public Calendar getDateOfCheck() {
        return calFormat;
    }
}

