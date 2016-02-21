public class ContactImpl implements Contact {


    private int id;
    private String name;
    private String notes="";
    
    public ContactImpl(int id, String name){
        this.id = id;
        this.name = name;        
    }

    public ContactImpl(int id, String name, String notes){
        this.name = name;        
        this.id = id;
        this.notes = notes;    
    }
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }

    public String  getNotes(){
        return this.notes;
    }
    public void addNotes(String note){
        this.notes+=note;
    }

}
