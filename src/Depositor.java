public class Depositor {
    private Name personName;
    private String SSN;

    // no Arg constructor
    public Depositor() {
        personName = new Name();
        SSN = "";
    }
    // 2-Arg constructor
    public Depositor(String ssn,Name personName) {
        this.personName = personName;
        SSN = ssn;
    }
    // copy constructor
    public Depositor(Depositor copy){
        this.personName = copy.personName;
        this.SSN = copy.SSN;
    }

    public Depositor(String s1, String s, String token) {
        SSN = s1;
        personName = new Name(s,token);
    }

    // getters
    public Name getPersonName() {
        return new Name(personName);
    }

    public String getSSN() {
        return SSN;
    }


}

