import java.text.ParseException;
import java.util.Locale;
import java.util.Arrays;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.StringBuilder;
import java.lang.IllegalArgumentException;
import java.io.IOException;
import java.lang.NullPointerException;
import java.util.Calendar;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Calendar;
/**
* A class to manage your contacts and meetings.
*/
public class  ContactManagerImpl implements ContactManager {
   
    private List<FutureMeeting> futureMeetings; 
    private List<PastMeeting> pastMeetings; 
    private List<Contact> contacts;
    private int highestContactId;
    private int highestMeetingId;


    public ContactManagerImpl(){
        highestContactId = 0;
        highestMeetingId = 0;
        contacts = new LinkedList();
        futureMeetings = new LinkedList();
        pastMeetings = new LinkedList();
        //TODO: add contacts from file. Get highest ID
        loadContacts();
        loadPastMeetings();
        loadFutureMeetings();
        //What happens if there is no file to load? Check for in loaContacts
       // highestContactId = 0; -- this should be set in loadContacts
    }
    /**
    * Add a new meeting to be held in the future.
    *
    * An ID is returned when the meeting is put into the system. This
    * ID must be positive and non-zero.
    *
    * @param contacts a list of contacts that will participate in the meeting
    * @param date the date on which the meeting will take place
    * @return the ID for the meeting
    * @throws IllegalArgumentException if the meeting is set for a time
    * in the past, of if any contact is unknown / non-existent.
    * @throws NullPointerException if the meeting or the date are null
    */
    public int addFutureMeeting(Set<Contact> contacts, Calendar date){
        int newHighestFutureMeetingId = highestMeetingId + 1;
        if (date == null)
            throw new NullPointerException("Date supplied is null");
        Locale locale1 = Locale.UK;
        TimeZone tz1 = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(tz1, locale1);
        if(cal.compareTo(date) > 0)
            throw new IllegalArgumentException("Future meeting date is in the past.");
        if(!this.contacts.containsAll(contacts))
            throw new IllegalArgumentException("Unknown contact used in FutureMeeting");
        FutureMeeting fm = new FutureMeetingImpl(newHighestFutureMeetingId, date, contacts);
        futureMeetings.add(fm);
        highestMeetingId = newHighestFutureMeetingId;
        return newHighestFutureMeetingId;    
    }
    /**
    * Returns the PAST meeting with the requested ID, or null if it there is none.
    *
    * The meeting must have happened at a past date.
    *
    * @param id the ID for the meeting
    * @return the meeting with the requested ID, or null if it there is none.
    * @throws IllegalStateException if there is a meeting with that ID happening
    * in the future
    */
    public PastMeeting getPastMeeting(int id){
        for (FutureMeeting fm : futureMeetings ){
            if (fm.getId() == id){
                throw new IllegalArgumentException("The past meeting requested is in the past:" + fm.getId() + "-" + id);
            }
        }
        PastMeeting pm = null;
        for (PastMeeting tmpPm : pastMeetings){
            if (tmpPm.getId() == id)
                pm = tmpPm;
        }
        return pm;
    }
    /**
    * Returns the FUTURE meeting with the requested ID, or null if there is none.
    *
    * @param id the ID for the meeting
    * @return the meeting with the requested ID, or null if it there is none.
    * @throws IllegalArgumentException if there is a meeting with that ID happening
    * in the past
    */
    public FutureMeeting getFutureMeeting(int id){
        // TODO -- Dummy code 
        for (PastMeeting pm : pastMeetings ){
            if (pm.getId() == id){
                throw new IllegalArgumentException("The future1111 meeting requested is in the past" + pm.getId() + "-" + id);
            }
        }
        FutureMeeting fm = null;
        for (FutureMeeting tmpFm : futureMeetings){
            if (tmpFm.getId() == id)
                fm = tmpFm;
        }
        return fm;
    }
    /**
    * Returns the meeting with the requested ID, or null if it there is none.
    *
    * @param id the ID for the meeting
    * @return the meeting with the requested ID, or null if it there is none.
    */
    public Meeting getMeeting(int id){

        // TODO -- Dummy code 
        Meeting returnMeeting = null;
        for (PastMeeting pm : pastMeetings){
            if(pm.getId() == id){
                returnMeeting = pm;
                break;
            }
        }
        for (FutureMeeting fm : futureMeetings){
            if(fm.getId() == id){
                returnMeeting = fm;
                break;
            }  
        }
        return returnMeeting;
    }
    /**
    * Returns the list of future meetings scheduled with this contact.
    *
    * If there are none, the returned list will be empty. Otherwise,
    * the list will be chronologically sorted and will not contain any
    * duplicates.
    *
    * @param contact one of the users contacts
    * @return the list of future meeting(s) scheduled with this contact (maybe empty).
    * @throws IllegalArgumentException if the contact does not exist
    * @throws NullPointerException if the contact is null
    */
    public List<Meeting> getFutureMeetingList(Contact contact){
        List<Meeting> meetingSortedReturnList = getMeetingListFor(contact, false);


        Collections.sort( meetingSortedReturnList,  new Comparator<Meeting>(){
                @Override
                public int compare(Meeting m1, Meeting m2){
                    return (int) m1.getDate().compareTo(m2.getDate());
                }
        });
        return meetingSortedReturnList;
    }
    /**
    * Returns the list of meetings that are scheduled for, or that took
    * place on, the specified date
    *
    * If there are none, the returned list will be empty. Otherwise,
    * the list will be chronologically sorted and will not contain any
    * duplicates.
    *
    * @param date the date
    * @return the list of meetings
    * @throws NullPointerException if the date are null
    */
    public List<Meeting> getMeetingListOn(Calendar date){
       
        if(date == null){
            throw new NullPointerException("Date supplied is null");
        }
         
        Set<Meeting> returnSet = new LinkedHashSet();
        for(PastMeeting pm : pastMeetings){
            if (date == pm.getDate()){
                returnSet.add(pm);
            }
        } 
        for(FutureMeeting fm : futureMeetings){
            if (date == fm.getDate()){
                returnSet.add(fm);
            }
        }


        List<Meeting> returnList = new LinkedList(returnSet); 
        Collections.sort( returnList,  new Comparator<Meeting>(){
                @Override
                public int compare(Meeting m1, Meeting m2){
                    return (int) m1.getDate().compareTo(m2.getDate());
                }
        });
        return returnList;
    }

    /**
    *Generic method to search meetings by Contacts
    *
    * @param contact one of the users contacts
    * @param true to search past meetings, false to search futureMeetings 
    * @return the list of future meeting(s) scheduled with this contact (maybe empty).
    * @throws IllegalArgumentException if the contact does not exist
    * @throws NullPointerException if the contact is null
    */
    private List<Meeting> getMeetingListFor(Contact contact, boolean getPastMeetings){
        if(contact == null)
            throw new NullPointerException("Contact to search PastMeetings on is null");
        if(!contacts.contains(contact))
            throw new IllegalArgumentException("Unknown contact to search for in PastMeetings");
        
        List<? extends Meeting> toSearch;
        if(getPastMeetings){
            toSearch = pastMeetings;
        } else {
            toSearch = futureMeetings;
        }
        Set<Meeting> collectSet = new LinkedHashSet(); 
        for (Meeting m : toSearch ){
            if(m.getContacts().contains(contact))
                collectSet.add(m);
        }
        List<Meeting> meetingSortedReturnList = new LinkedList(collectSet); 
        Collections.sort( meetingSortedReturnList,  new Comparator<Meeting>(){
                @Override
                public int compare(Meeting m1, Meeting m2){
                    return (int) m2.getDate().compareTo(m1.getDate());
                }
            });
        return meetingSortedReturnList;
    }

    /**
    * Returns the list of past meetings in which this contact has participated.
    *
    * If there are none, the returned list will be empty. Otherwise,
    * the list will be chronologically sorted and will not contain any
    * duplicates.
    *
    * @param contact one of the users contacts
    * @return the list of future meeting(s) scheduled with this contact (maybe empty).
    * @throws IllegalArgumentException if the contact does not exist
    * @throws NullPointerException if the contact is null
    */
    public List<PastMeeting> getPastMeetingListFor(Contact contact){
        List<Meeting> meetingSortedReturnList = getMeetingListFor(contact, true);
        Collections.sort( meetingSortedReturnList,  new Comparator<Meeting>(){
                @Override
                public int compare(Meeting m1, Meeting m2){
                    return (int) m2.getDate().compareTo(m1.getDate());
                }
        });
        List<PastMeeting> pastMeetingSortedReturnList = (List) meetingSortedReturnList;
        return pastMeetingSortedReturnList;
    }
  
  /**
    * Create a new record for a meeting that took place in the past.
    *
    * @param contacts a list of participants
    * @param date the date on which the meeting took place
    * @param text messages to be added about the meeting.
    * @throws IllegalArgumentException if the list of contacts is
    *
    empty, or any of the contacts does not exist
    * @throws NullPointerException if any of the arguments is null
    */
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){
        //Dummy implementation
        if(contacts.size() == 0 || !(this.contacts.containsAll(contacts)))
            throw new IllegalArgumentException("Contacts size is null or unknown contact in supplied");
        
        int newHighestPastMeetingId = highestMeetingId +1;
        PastMeeting pm = new PastMeetingImpl(newHighestPastMeetingId,date, contacts, text ); 
        pastMeetings.add(pm);
        highestMeetingId = newHighestPastMeetingId;
    }
    /**
    * Add notes to a meeting.
    *
    * This method is used when a future meeting takes place, and is
    * then converted to a past meeting (with notes) and returned.
    *
    * It can be also used to add notes to a past meeting at a later date.
    *
    * @param id the ID of the meeting
    * @param text messages to be added about the meeting.
    * @throws IllegalArgumentException if the meeting does not exist
    * @throws IllegalStateException if the meeting is set for a date in the future
    * @throws NullPointerException if the notes are null
    */
    public PastMeeting addMeetingNotes(int id, String text){
        if(text == null){
            throw new NullPointerException("Meeting notes are null");
        }
        FutureMeeting fmToGet = null;

        for (Iterator<FutureMeeting> iter = futureMeetings.listIterator(); iter.hasNext(); ) {
            FutureMeeting fm = iter.next();
            if (fm.getId() == id) {
                fmToGet = fm;
                iter.remove();
            }
        }
        if(fmToGet == null){
            throw new IllegalArgumentException("The meeting id does not exist");
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy");
        Calendar now = Calendar.getInstance();
        /*System.out.println("---" + sdf.format(now)) ;
        System.out.println("---" + sdf.format(fmToGet.getDate()) );
        if(fmToGet.getDate().compareTo(now)>1){
            throw new IllegalStateException("The meetings to add notes to is in the future!");
        } TODO - remove the comments around this block*/
        PastMeeting newPastMeeting = new PastMeetingImpl(fmToGet.getId(), fmToGet.getDate(), fmToGet.getContacts(), text);
        pastMeetings.add(newPastMeeting);
        return newPastMeeting;
    }
    /**
    * Create a new contact with the specified name and notes.
    *
    * @param name the name of the contact.
    * @param notes notes to be added about the contact.
    * @return the ID for the new contact
    * @throws IllegalArgumentException if the name or the notes are empty strings
    * @throws NullPointerException if the name or the notes are null
    */
    public int addNewContact(String name, String notes){
        if(name.equals("") || notes.equals(""))
            throw new IllegalArgumentException("Name and notes cannot be null");
        if(name == null || notes == null)
            throw new NullPointerException("Name and notes cannot be null");
        int newHighestContactId = highestContactId +1;
        Contact newContact = new ContactImpl(newHighestContactId, name, notes);
        contacts.add(newContact);
        highestContactId = newHighestContactId;
        return newHighestContactId;
    }
    /**
    * Returns a list with the contacts whose name contains that string.
    *
    * If the string is the empty string, this methods returns the set
    * that contains all current contacts.
    *
    * @param name the string to search for
    * @return a list with the contacts whose name contains that string.
    * @throws NullPointerException if the parameter is null
    */
    public Set<Contact> getContacts(String name){
        Set<Contact> returnSet = new LinkedHashSet(); 
        for(int i=1; i<=highestContactId; i++){
            if ( name.equals(contacts.get(i-1).getName())){
                returnSet.add( contacts.get(i-1) ); 
          }        
        }
        return returnSet;
    }
    /**
    * Returns a list containing the contacts that correspond to the IDs.
    * Note that this method can be used to retrieve just one contact by passing only one ID.
    *
    * @param ids an arbitrary number of contact IDs
    * @return a list containing the contacts that correspond to the IDs.
    * @throws IllegalArgumentException if no IDs are provided or if
    * any of the provided IDs does not correspond to a real contact
    */
    public Set<Contact> getContacts(int... ids){
        List<Integer> argIds = new LinkedList();
        for (int id : ids){
            argIds.add(id);
        }
        Set<Contact> returnSet = new LinkedHashSet(); 
        for(int i=1; i<=highestContactId; i++){
            if ( argIds.contains(i)){
                returnSet.add( contacts.get(i-1) ); 
            }        
        }   
        return returnSet;    
    }
    /**
    * Save all data to disk.
    *
    * This method must be executed when the program is
    * closed and when/if the user requests it.
    */
    public void flush(){
       flushContacts(); 
       flushPastMeetings();
       flushFutureMeetings();
    }

    private void flushContacts(){
        StringBuilder fileContents = new StringBuilder();
        fileContents.append("`id`,`name`,`notes`".replace('`','"' ) + System.lineSeparator());
        for(int i=0; i<=contacts.size()-1; i++){
            fileContents.append("\"" + contacts.get(i).getId()  + "\",");
            fileContents.append("\"" + contacts.get(i).getName().replace("\n", "\\n")  + "\",");
            fileContents.append("\"" + contacts.get(i).getNotes()  + "\"" + System.lineSeparator());
        } 
        try(PrintWriter writer = new PrintWriter("Contacts.csv")){
            writer.print(fileContents);
            writer.flush();        
        } catch ( FileNotFoundException ex) {
            System.out.println("Can't open file (Contacts.csv) to write contacts.");
        }
    }

    private void flushPastMeetings(){
        StringBuilder fileContents = new StringBuilder();
        fileContents.append("`id`,`date`,`notes`,`contacts`,`notes`".replace('`','"' ) + System.lineSeparator());
        
        for(PastMeeting pm: pastMeetings){
            String idList = "";
            for(Contact meetingContact : pm.getContacts()){
                idList = idList + meetingContact.getId() + ",";
            }
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd/MM/yyyy H:m:s:S");
            fileContents.append("\"" + pm.getId()  + "\",");
            //fileContents.append("\"" + contacts.get(i).getName().replace("\n", "\\n")  + "\",");
            fileContents.append("\"" + sdf.format(pm.getDate().getTime())  + "\",");
            fileContents.append("\"" + idList  + "\",");
            fileContents.append("\"" + pm.getNotes().replace("\n", "\\n")  + "\"" + System.lineSeparator());
        } 
        try(PrintWriter writer = new PrintWriter("PastMeetings.csv")){
            writer.print(fileContents);
            writer.flush();        
        } catch ( FileNotFoundException ex) {
            System.out.println("Can't open file (PastMeetings.csv) to write contacts.");
        }
    }


    private void flushFutureMeetings(){
        StringBuilder fileContents = new StringBuilder();
        fileContents.append("`id`,`date`,`notes`,`contacts`".replace('`','"' ) + System.lineSeparator());
        
        for(FutureMeeting pm: futureMeetings){
            String idList = "";
            for(Contact meetingContact : pm.getContacts()){
                idList = idList + meetingContact.getId() + ",";
            }
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd/MM/yyyy H:m:s:S");
            fileContents.append("\"" + pm.getId()  + "\",");
            fileContents.append("\"" + sdf.format(pm.getDate().getTime())  + "\",");
            fileContents.append("\"" + idList  + "\"" + System.lineSeparator());
            //fileContents.append("\"" + pm.getNotes().replace("\n", "\\n")  + "\"" + System.lineSeparator());
        } 
        try(PrintWriter writer = new PrintWriter("FutureMeetings.csv")){
            writer.print(fileContents);
            writer.flush();        
        } catch ( FileNotFoundException ex) {
            System.out.println("Can't open file (FutureMeetings.csv) to write contacts.");
        }

    }

    private void loadContacts(){
        try(BufferedReader br = new BufferedReader(new FileReader("Contacts.csv"))){
            int id;
            String name;
            String notes;
            Pattern pattern = Pattern.compile("^\"(\\d+)\",\"(.*)\",\"(.*)\"$");
            String line = br.readLine(); //discard first line
            int highestContactIdInFile = -1;
            while((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()){
                    int entryId = Integer.parseInt(matcher.group(1));
                    String nameFromFile = matcher.group(2).replace("\\n", "\n");
                    if (entryId > highestContactIdInFile)
                        highestContactIdInFile = entryId;
                    Contact newContact = new ContactImpl( entryId, nameFromFile, matcher.group(3));
                    contacts.add(newContact);
                } else {
                    System.out.println("Could not load line:" + line);
                }
            }
            highestContactId = highestContactIdInFile;
        } catch (FileNotFoundException ex) {
            //do nothing -- there is no file yet to read. 
        } catch  (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadPastMeetings(){
        try(BufferedReader br = new BufferedReader(new FileReader("PastMeetings.csv"))){
            int id;
            String name;
            String notes;
            Pattern pattern = Pattern.compile("^\"(\\d+)\",\"(.*)\",\"(.*)\",\"(.*)\"$");
            String line = br.readLine(); //discard first line
            int highestIdInFile = -1;
            while((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()){
                    int entryId = Integer.parseInt(matcher.group(1));
                    System.out.println("entryId" + entryId);
                    //load date
                    Calendar calendarFile = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:m:s:S");
                    try {
                        calendarFile.setTime(sdf.parse(matcher.group(2)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //load contacts
                    List<String> contactList = Arrays.asList(matcher.group(3).split(","));
                    Set<Contact> meetContacts = new LinkedHashSet();
                    for (String s : contactList){
                        Set<Contact> c = getContacts(Integer.parseInt(s));
                        meetContacts.addAll(c);
                    }
                    //load notes
                    String notesFile = matcher.group(4).replace("\\n", "\n");
                    PastMeeting newPastMeeting  = new PastMeetingImpl( entryId, calendarFile, meetContacts, notesFile);
                    pastMeetings.add(newPastMeeting);
                    System.out.println(newPastMeeting);
                } else {
                    System.out.println("Could not load line:" + line);
                }
            }
            if(highestMeetingId == 0){
                highestMeetingId = highestIdInFile;
            }else if (highestMeetingId < highestIdInFile){
                highestMeetingId = highestIdInFile;
            }
        } catch (FileNotFoundException ex) {
            //do nothing -- there is no file yet to read. 
        } catch  (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadFutureMeetings(){
        try(BufferedReader br = new BufferedReader(new FileReader("FutureMeetings.csv"))){
            int id;
            String name;
            String notes;
            Pattern pattern = Pattern.compile("^\"(\\d+)\",\"(.*)\",\"(.*)\"$");
            String line = br.readLine(); //discard first line
            int highestIdInFile = -1;
            while((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()){
                    System.out.println("------------------");
                    int entryId = Integer.parseInt(matcher.group(1));
                    System.out.println("entryId" + entryId);
                    //load date
                    Calendar calendarFile = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:m:s:S");
                    try {
                        calendarFile.setTime(sdf.parse(matcher.group(2)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //load contacts
                    System.out.println("-----loading at 50%---------");
                    List<String> contactList = Arrays.asList(matcher.group(3).split(","));
                    Set<Contact> meetContacts = new LinkedHashSet();
                    for (String s : contactList){
                        Set<Contact> c = getContacts(Integer.parseInt(s));
                        meetContacts.addAll(c);
                    }
                    FutureMeeting newFutureMeeting  = new FutureMeetingImpl( entryId, calendarFile, meetContacts);
                    futureMeetings.add(newFutureMeeting);
                    System.out.println("-----loading at 100%------RESO---");
                    System.out.println(newFutureMeeting);
                } else {
                    System.out.println("Could not load line:" + line);
                }
            }
            if(highestMeetingId == 0){
                highestMeetingId = highestIdInFile;
            }else if (highestMeetingId < highestIdInFile){
                highestMeetingId = highestIdInFile;
            }
        } catch (FileNotFoundException ex) {
            //do nothing -- there is no file yet to read. 
        } catch  (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
    *Method to inject future or past meeting for testing only
    *
    *
    */
    public void injectMeetingForTest(int id, Set<Contact> conts, Calendar meetDate, boolean isPastMeeting){
        if(isPastMeeting){
            PastMeeting newPastDummyMeeting = new PastMeetingImpl(id, meetDate, conts,"DUMMY"); 
            pastMeetings.add(newPastDummyMeeting);
        }else{
            FutureMeeting newFutureDummyMeeting = new FutureMeetingImpl(id, meetDate, conts);
            futureMeetings.add(newFutureDummyMeeting);
        }   
    }


}
