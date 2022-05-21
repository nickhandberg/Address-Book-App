import java.io.*;
import java.util.ArrayList;

public class ContactsList implements Serializable {
    ArrayList<Contacts> list = new ArrayList<>();

    // add method to add a contact object to the list
    public void add(Contacts contact){
        list.add(contact);
    }

    // remove method to remove a contact at the index from the list
    public void remove(int index){
        list.remove(index);
    }

    // update method to replace contact at the index with the passed in contact object
    public void update(Contacts contact, int index){
        list.set(index, contact);
    }

    // Getter method to get the contact object at the index in the list
    public Contacts getContact(int index){
        return list.get(index);
    }

    // Save method to save the contacts list object
    public void save(String filename) {
        try{ // Try/Catch on any Exception
            // Creates a fileOutput and objectOutput stream using hte filename
            FileOutputStream fileIn = new FileOutputStream(filename);
            ObjectOutputStream output = new ObjectOutputStream(fileIn);
            // Writes the contacts list object to output and closes the output
            output.writeObject(this);
            output.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Load method to load the contacts list object
    public void load(String filename) throws IOException {
        // Checks if the file does not exist, if so creates a new file using save method
        File tempFile = new File(filename);
        if(!tempFile.exists()){ save(filename); }

        // Creates a fileInput and ObjectInput stream using the filename
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream input = new ObjectInputStream(fileIn);
        // Creates ContactsList instance
        ContactsList contactsList = new ContactsList();

        try {   // Try/Catch to catch a IOException and ClassNotFoundException
            // reads the object from the file, casts it as type ContactsList and initializes the contactsList
            contactsList = (ContactsList) (input.readObject());
        } catch (IOException e) {
            input.close();
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Closes the input and assigns the reference to the loaded object's list
        input.close();
        this.list = contactsList.list;
    }
}
