public class ContactImpl implements Contact {


    private int id;
    private String name;
    private String notes="";
    
    public ContactImpl(int id, String name){
        this(id, name, "");
    }

    public ContactImpl(int id, String name, String notes){
        if(id <= 0){
            throw new IllegalArgumentException("Id has to be larger than 0.");
        }
        if(name == null || notes == null){
            throw new NullPointerException("Name or notes supplied are null");
        } 
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
    
    @Override
    public boolea(Object obj){
        if(obj == null) {
            return false;
        }
        if(!ContactImpl.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        final ContactImpl cntImpl = (ContactImpl) obj;




    }
}
