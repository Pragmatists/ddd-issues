package ddd.domain;

public class ProductVersion {

    private ProductID product;
    
    // "1.5.12", "2.0.0.RC1", "2.0.0.GA"
    private Integer buildNumber; 
    private Integer minorVersion;
    private Integer majorVersion;
    
    private Integer releaseCandidate;
    private boolean generalAvailability;
    
    // public ProductVersion nextBuildVersion();
    // public ProductVersion nextMinorVersion();
    // public ProductVersion nextMajorVersion();
    // public ProductVersion nextReleaseCandidate();
    // public ProductVersion nextGeneralAvailabilityVersion();
    
    
}
