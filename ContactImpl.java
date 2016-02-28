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
    public boolean equals(Object obj){
        if(obj == null) {
            return false;
        }
        if(!ContactImpl.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        final Contact other = (ContactImpl) obj;
        //check that fields in both objects are the same. Id and name can't be null. 
        if (!(this.getId() == other.getId()))
            return false;
        if(!(this.getName() == other.getName()))
            return false;
        if( (this.getNotes() == null) ? (other.getNotes() != null) : !this.getNotes().equals(other.getNotes()))
            return false;  
        return true;
    }

    @Override
    public int hashCode(){
    
        int hash = 3;
        hash = 53 * hash + this.getId() + this.getName().hashCode() + (this.getNotes() != null ? this.getNotes().hashCode() : 0); 
        return hash;
    }

}
