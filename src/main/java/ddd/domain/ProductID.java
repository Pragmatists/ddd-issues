package ddd.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductID implements Serializable {

    @Column(name = "product_id")
    private String value;

    public ProductID(String value) {
        this.value = value;
    }

    public ProductID() {
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
