package ddd.domain;

public class ParticipantID {

    private final String value;

    public ParticipantID(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    public boolean equals(Object obj) {
     
        if(!(obj instanceof ParticipantID)){
            return false;
        }
        
        ParticipantID other = (ParticipantID) obj;
        return this.value.equals(other.value);
    }
}
