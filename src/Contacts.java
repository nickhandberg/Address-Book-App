import java.io.Serializable;
import java.util.ArrayList;

public class Contacts implements Serializable {
    // Class instance variables
    private String firstName ="";
    private String lastName ="";
    private String address ="";
    private String city ="";
    private String state ="";
    private String zip ="";
    private String email ="";
    private String notes ="";

    // No Args Class Constructor
    public Contacts(){ }

    // Class Constructor
    public Contacts(ArrayList<String> contactDetails){
        this.firstName = contactDetails.get(0);
        this.lastName = contactDetails.get(1);
        this.address = contactDetails.get(2);
        this.city = contactDetails.get(3);
        this.state = contactDetails.get(4);
        this.zip = contactDetails.get(5);
        this.email = contactDetails.get(6);
        this.notes = contactDetails.get(7);
    }

    // Getter Methods
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getEmail() {
        return email;
    }

    public String getNotes() {
        return notes;
    }

    // toString method for ListView display
    @Override
    public String toString() {
        return lastName+", "+firstName;
    }
}
