package ddd.domain;

import java.io.Serializable;

public class IssueNumber implements Serializable {

    private int value;

    public IssueNumber() {
    }

    public IssueNumber(int value) {
        this.value = value;
    }

    public IssueNumber next() {
        return new IssueNumber(++value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueNumber that = (IssueNumber) o;

        if (value != that.value) return false;

        return true;
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
