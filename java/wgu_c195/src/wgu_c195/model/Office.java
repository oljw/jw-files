package wgu_c195.model;

public class Office {
    private int cityId;
    private String city;
    private int countryId;
    private String country;

    public Office(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }
}
