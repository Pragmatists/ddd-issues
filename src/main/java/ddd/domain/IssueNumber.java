package ddd.domain;

public class IssueNumber {

    private final int value;

    public IssueNumber(int value) {
        this.value = value;
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
        return String.format("Issue #%s", value);
    }

    public int value() {
        return value;
    }
}
