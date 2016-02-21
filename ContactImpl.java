public class ContactImpl implements Contact {


    private int id;
    private String name;


    public ContactImpl(int id, String name){
        this.id = id;
        this.name = name;        
    }

    public ContactImpl(int id, String name, String notes){
        this.name = name;        
        this.id = id;
    }
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }

    public String  getNotes(){
        return "Character";
    }
    public void addNotes(String note){

    }

}
