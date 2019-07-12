package wgu_c195.model;

public class Country {

    private Integer countryId;
    private String country;

    public Country(Integer countryId) {
        this.countryId = countryId;
    }

    public Country(Integer countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
