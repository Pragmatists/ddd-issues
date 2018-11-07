package ddd.domain;

import static java.lang.String.format;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductVersion implements Serializable{

    private static final Pattern PATTERN= Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");

    private ProductID product;
    
    // "1.5.12", "2.0.0.RC1", "2.0.0.GA"
    private Integer buildNumber; 
    private Integer minorVersion;
    private Integer majorVersion;
    
    private Integer releaseCandidate;
    private boolean generalAvailability;

    public ProductVersion(ProductID product, String version) {
        this.product = product;
        parseVersion(version);
    }

    private void parseVersion(String version) {
        Matcher matcher = PATTERN.matcher(version);
        if (matcher.matches()) {
            majorVersion = Integer.valueOf(matcher.group(1));
            minorVersion = Integer.valueOf(matcher.group(2));
            buildNumber = Integer.valueOf(matcher.group(3));
        } else {
            throw new IllegalArgumentException(String.format("Wrong format for version '%s'", version));
        }
    }

    // public ProductVersion nextBuildVersion();
    // public ProductVersion nextMinorVersion();
    // public ProductVersion nextMajorVersion();
    // public ProductVersion nextReleaseCandidate();
    public ProductID product() {
        return product;
    }

    // public ProductVersion nextGeneralAvailabilityVersion();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVersion)) {
            return false;
        }

        ProductVersion that = (ProductVersion) o;

        if (generalAvailability != that.generalAvailability) {
            return false;
        }
        if (!buildNumber.equals(that.buildNumber)) {
            return false;
        }
        if (!majorVersion.equals(that.majorVersion)) {
            return false;
        }
        if (!minorVersion.equals(that.minorVersion)) {
            return false;
        }
        if (!product.equals(that.product)) {
            return false;
        }
        if (releaseCandidate != null ? !releaseCandidate.equals(that.releaseCandidate) : that.releaseCandidate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = product.hashCode();
        result = 31 * result + buildNumber.hashCode();
        result = 31 * result + minorVersion.hashCode();
        result = 31 * result + majorVersion.hashCode();
        result = 31 * result + (releaseCandidate != null ? releaseCandidate.hashCode() : 0);
        result = 31 * result + (generalAvailability ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return format("%s %s.%s.%s", product, majorVersion, minorVersion, buildNumber);
    }

    public static ProductVersion of(String value) {
        
        String product = value.split(" ")[0];
        String version = value.split(" ")[1];
        
        return new ProductVersion(new ProductID(product), version);
    }

    public String version() {
        return format("%s.%s.%s", majorVersion, minorVersion, buildNumber);
    }
}
