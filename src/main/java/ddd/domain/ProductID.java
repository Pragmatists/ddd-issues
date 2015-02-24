package ddd.domain;

import java.io.Serializable;


public class ProductID implements Serializable {

    private String value;

    public ProductID(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductID)) {
            return false;
        }

        ProductID productID = (ProductID) o;

        if (!value.equals(productID.value)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return value;
    }
}
