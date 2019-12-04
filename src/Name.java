public class Name {
    private String firstName;
    private String lastName;

    // no Arg constructor
    public Name() {
        firstName = "";
        lastName = "";
    }
    // 2 Arg constructor
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // copy constructor
    public Name(Name copy) {
        this.firstName = copy.firstName;
        this.lastName = copy.lastName;
    }
    // toString override
    public String toString(){
        String str = String.format(this.firstName,this.lastName);
        return str;
    }
    // Accessors
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean equals(Name myName) {
        if(lastName.equals(myName.lastName) && firstName.equals(myName.firstName))
            return true;			//myName found
        else
            return false;			//myName not found
    }

}
