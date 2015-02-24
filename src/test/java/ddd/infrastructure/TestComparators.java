package ddd.infrastructure;

import java.util.Comparator;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class TestComparators {

    private final static class FieldByFieldComparator<T> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            boolean areEqual = EqualsBuilder.reflectionEquals(o1, o2);
            return areEqual ? 0 : -1;
        }
    }

    public static <T> Comparator<T> fieldByField(){
        return new FieldByFieldComparator<>();
    }
    
}
