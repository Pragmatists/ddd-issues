package ddd.domain;

import java.io.Serializable;

public class IssueNumber implements Serializable {

    private int value;

    public IssueNumber(int value) {
        this.value = value;
    }

    public IssueNumber next() {
        return new IssueNumber(++value);
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof IssueNumber)){
            return false;
        }
        
        IssueNumber other = (IssueNumber) obj;

        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
