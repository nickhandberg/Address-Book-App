/*
    CSC 322 - Program 6 - 10/19/2021
    Nicholas Handberg - handbern@csp.edu
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AddressBook extends Application {
    // Declares instance variables (TextFields, Buttons, etc.)
    private static final String FILENAME = "contacts.dat";
    private final ObservableList<String> states = FXCollections.observableArrayList("AL","AK","AS","AZ","AR","CA",
            "CO","CT","DE","DC","FL","GA","GU","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS",
            "MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","MP","OH","OK","OR","PA","PR","RI","SC","SD","TN","TX",
            "UT","VT", "VA","VI","WA","WV","WI","WY");
    TextField tfFirstName= new TextField(), tfLastName= new TextField(), tfAddress= new TextField(),
            tfCity = new TextField(), tfZip = new TextField(), tfEmail = new TextField();
    Button btNew = new Button("New"), btDelete = new Button("Delete"), btUpdate = new Button("Update"),
            btClear = new Button("Clear");
    private final TextArea taNotes = new TextArea();
    private final ComboBox<String> cbState = new ComboBox<>(states);
    private final Label titleLabel = new Label("Address Book");
    private final BorderPane pane = new BorderPane();
    private final GridPane centerPane = new GridPane();
    private final GridPane topPane = new GridPane();

    private final ContactsList contactsList = new ContactsList();
    private final ObservableList<Contacts> lvList = FXCollections.observableArrayList();;
    private ListView<Contacts> lv;
    private int listIndex = -1;

    @Override
    public void start(Stage stage) throws Exception {
        // calls the load method which initializes the contactsList instance with the loaded object
        contactsList.load(FILENAME);

        // For loop to add each contact from contactsList to the list view list
        for(int i=0; i<contactsList.list.size(); i++){
            lvList.add(contactsList.getContact(i));
        }

        // Creates new list view using the lvList and sets its size and selection mode
        lv = new ListView<>(lvList);
        lv.setPrefSize(200, 200);
        lv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Creates the layout using a BorderPane. Sets left to ListView, top to a GridView consisting of an image
        // and label, and center to GridPane consisting of Labels, TextFields, and Buttons for user input.
        titleLabel.setFont(Font.font("Cambria", 32));
        pane.setTop(topPane);
        pane.setLeft(lv);
        pane.setCenter(centerPane);
        pane.setStyle("-fx-padding: 10;");
        centerPane.setHgap(10);  centerPane.setVgap(20);
        centerPane.setPadding(new Insets(20));
        centerPane.addRow(0,new Label("First"),tfFirstName,new Label("Last"),tfLastName);
        centerPane.addRow(1,new Label("Street"));
        centerPane.add(tfAddress,1,1,3,1);
        centerPane.addRow(2,new Label("City"), tfCity,new Label("State"),cbState);
        centerPane.addRow(3,new Label("Zip"),tfZip);
        centerPane.addRow(4,new Label("Email"), tfEmail);
        centerPane.addRow(5,new Label("Notes"));
        centerPane.add(taNotes, 1,5,3,3);
        centerPane.addRow(10, btNew,btUpdate,btClear,btDelete);
        topPane.addRow(1,new ImageView(new Image("File:image.JPG")), titleLabel);
        topPane.setPadding(new Insets(10));

        // Creates the scene using the BorderPane, sets the scene in the stage and shows the stage.
        Scene scene = new Scene(pane, 700, 600);
        stage.setTitle("Address Book");
        stage.setScene(scene);
        stage.show();

        // Sets the on action method calls for the buttons
        btNew.setOnAction(e -> newButtonHandler());
        btUpdate.setOnAction(e -> updateButtonHandler());
        btDelete.setOnAction(e -> deleteButtonHandler());
        btClear.setOnAction(e -> clearTextFieldEntries());

        // Adds a listener to the listview
        lv.getSelectionModel().selectedItemProperty().addListener(
            ov -> {
                // Gets index of selected list view item
                for (Integer i: lv.getSelectionModel().getSelectedIndices()) {
                    // Sets the listIndex instance variable to the index and calls the listClickHandler method
                    listIndex = i;
                    listClickHandler();
                }});
    }

    // Defines the newButtonHandler method which is called when the "New" button is clicked
    public void newButtonHandler(){
        // Creates new contact instance calling the no arg constructor (empty strings)
        Contacts contact = new Contacts();

        // Adds the contact to the contactsList and lvList. Selects the new contact in ListView
        contactsList.add(contact);
        lvList.add(contact);
        lv.getSelectionModel().select(contact);

        // Saves the contactsList
        contactsList.save( FILENAME);
    }

    // Defines the updateButtonHandler method which is called when the "Update" button is clicked
    public void updateButtonHandler(){
        // Checks if the listIndex is not -1 (ListView item is selected)
        if(listIndex != -1){
            // Creates new contact instance using the returned ArrayList of the getTextFieldEntries method
            Contacts contact = new Contacts(getTextFieldEntries());

            // Updates the contactsList and lvList. Saves the contactsList
            contactsList.update(contact,listIndex);
            lvList.set(listIndex, contact);
            contactsList.save(FILENAME);
        }
    }

    // Defines the deleteButtonHandler method which is called when the "Delete" button is clicked
    public void deleteButtonHandler(){
        // Checks if the listIndex is not -1 (ListView item is selected)
        if(listIndex != -1){
            // Removes the given index from the contactsList and lvList
            contactsList.remove(listIndex);
            lvList.remove(listIndex);

            //Clears the fields, deselects the listview item, and saves the contactsList
            clearTextFieldEntries();
            lv.getSelectionModel().select(-1);
            listIndex = -1;
            contactsList.save(FILENAME);
        }
    }

    // Defines the listClickHandler method which is called when a ListView item is clicked
    public void listClickHandler(){
        // Sets the TextField's text to the contact data at the given index
        tfFirstName.setText(lvList.get(listIndex).getFirstName());
        tfLastName.setText(lvList.get(listIndex).getLastName());
        tfAddress.setText(lvList.get(listIndex).getAddress());
        tfCity.setText(lvList.get(listIndex).getCity());
        cbState.getSelectionModel().select(lvList.get(listIndex).getState());
        tfZip.setText(lvList.get(listIndex).getZip());
        tfEmail.setText(lvList.get(listIndex).getEmail());
        taNotes.setText(lvList.get(listIndex).getNotes());
    }

    // Defines the clearTextFieldEntries method which is called when the "Clear" button is clicked
    public void clearTextFieldEntries(){
        // Clears the TextFields. For the comboBox selects empty string
        tfFirstName.clear();
        tfLastName.clear();
        tfAddress.clear();
        tfCity.clear();
        cbState.getSelectionModel().select("");
        tfZip.clear();
        tfEmail.clear();
        taNotes.clear();
    }

    // Method getTextFieldEntries used in other methods. (added to reduce repeat code)
    public ArrayList<String> getTextFieldEntries(){
        // Returns the TextFields text in an ArrayList. For the combobox, gets selected item
        ArrayList<String> tfEntries = new ArrayList<>();
        tfEntries.add(tfFirstName.getText());
        tfEntries.add(tfLastName.getText());
        tfEntries.add(tfAddress.getText());
        tfEntries.add(tfCity.getText());
        tfEntries.add(cbState.getSelectionModel().getSelectedItem());
        tfEntries.add(tfZip.getText());
        tfEntries.add(tfEmail.getText());
        tfEntries.add(taNotes.getText());
        return tfEntries;
    }
}
