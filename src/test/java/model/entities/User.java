package model.entities;

import net.datafaker.Faker;
import utils.JSONTools;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class User {

    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    public User(int id,
                String name,
                String username,
                String email,
                String phone,
                String website,
                String street,
                String suite,
                String city,
                String zipcode,
                String lat,
                String lng,
                String companyName,
                String catchPhrase,
                String bs) {

        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.website = website;
        address = new Address(street, suite, city, zipcode, lat, lng);
        company = new Company(companyName, catchPhrase, bs);
    }

    public User(Map<String, String> record) {

        this(Integer.parseInt(record.get("id")),
                record.get("name"),
                record.get("username"),
                record.get("email"),
                record.get("phone"),
                record.get("website"),
                record.get("address_street"),
                record.get("address_suite"),
                record.get("address_city"),
                record.get("address_zipcode"),
                record.get("address_geo_lat"),
                record.get("address_geo_lng"),
                record.get("company_name"),
                record.get("company_catchPhrase"),
                record.get("company_bs"));

    }

    public User(String json) {

        this(JSONTools.getIntegerFromJSON(json, "id"),
                JSONTools.getStringFromJSON(json, "name"),
                JSONTools.getStringFromJSON(json, "username"),
                JSONTools.getStringFromJSON(json, "email"),
                JSONTools.getStringFromJSON(json, "phone"),
                JSONTools.getStringFromJSON(json, "website"),
                JSONTools.getNestedField(json, "address.street"),
                JSONTools.getNestedField(json, "address.suite"),
                JSONTools.getNestedField(json, "address.city"),
                JSONTools.getNestedField(json, "address.zipcode"),
                JSONTools.getNestedField(json, "address.geo.lat"),
                JSONTools.getNestedField(json, "address.geo.lng"),
                JSONTools.getNestedField(json, "company.name"),
                JSONTools.getNestedField(json, "company.catchPhrase"),
                JSONTools.getNestedField(json, "company.bs"));

    }

    public static User getUser() {

        Random random = new Random();
        Faker faker = new Faker();

        return new User(random.nextInt(1001),
                faker.name().fullName(),
                faker.internet().username(),
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone(),
                faker.internet().webdomain(),
                faker.address().streetName(),
                faker.address().streetAddressNumber(),
                faker.address().city(),
                faker.address().zipCode(),
                faker.address().latitude(),
                faker.address().longitude(),
                faker.company().name(),
                faker.company().catchPhrase(),
                faker.company().industry()
        );

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public Address getAddress() {
        return address;
    }

    public Company getCompany() {
        return company;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddressStreet(String street) {
        address.setStreet(street);
    }

    public void setAddressSuite(String suite) {
        address.setSuite(suite);
    }

    public void setAddressCity(String city) {
        address.setCity(city);
    }

    public void setAddressZipcode(String zipcode) {
        address.setZipcode(zipcode);
    }

    public void setAddressLat(String lat) {
        address.setGeoLat(lat);
    }

    public void setAddressLng(String lng) {
        address.setGeoLng(lng);
    }

    public void setCompanyName(String name) {
        company.setName(name);
    }

    public void setCompanyCatchPhrase(String phrase) {
        company.setCatchPhrase(phrase);
    }

    public void setCompanyBs(String bs) {
        company.setBs(bs);
    }

    public String toJSONString() {

        String jsonBody = String.format("""
                        {
                          "id": %d,
                          "name": "%s",
                          "username": "%s",
                          "email": "%s",
                          "address": {
                            "street": "%s",
                            "suite": "%s",
                            "city": "%s",
                            "zipcode": "%s",
                            "geo": {
                              "lat": "%s",
                              "lng": "%s"
                            }
                          },
                          "phone": "%s",
                          "website": "%s",
                          "company": {
                            "name": "%s",
                            "catchPhrase": "%s",
                            "bs": "%s"
                          }
                        }
                        """,
                id,
                name,
                username,
                email,
                address.getStreet(),
                address.getSuite(),
                address.getCity(),
                address.getZipcode(),
                address.getGeoLat(),
                address.getGeoLng(),
                phone,
                website,
                company.getName(),
                company.getCatchPhrase(),
                company.getBs());

        return jsonBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && Objects.equals(website, user.website) && Objects.equals(address, user.address) && Objects.equals(company, user.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, email, phone, website, address, company);
    }
}


class Company {

    private String name;
    private String catchPhrase;
    private String bs;

    Company(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    String getName() {
        return name;
    }

    String getCatchPhrase() {
        return catchPhrase;
    }

    String getBs() {
        return bs;
    }

    void setName(String name) {
        this.name = name;
    }

    void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    void setBs(String bs) {
        this.bs = bs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) && Objects.equals(catchPhrase, company.catchPhrase) && Objects.equals(bs, company.bs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, catchPhrase, bs);
    }
}

class Address {

    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private GeoLocation geo;

    protected Address(String street, String suite, String city, String zipcode, String lat, String lng) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = new GeoLocation(lat, lng);

    }

    String getStreet() {
        return street;
    }

    String getSuite() {
        return suite;
    }

    String getCity() {
        return city;
    }

    String getZipcode() {
        return zipcode;
    }

    String getGeoLat() {
        return geo.getLat();
    }

    String getGeoLng() {
        return geo.getLng();
    }

    void setStreet(String street) {
        this.street = street;
    }

    void setSuite(String suite) {
        this.suite = suite;
    }

    void setCity(String city) {
        this.city = city;
    }

    void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    void setGeoLat(String lat) {
        this.geo.setLat(lat);
    }

    void setGeoLng(String lng) {
        this.geo.setLng(lng);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(suite, address.suite) && Objects.equals(city, address.city) && Objects.equals(zipcode, address.zipcode) && Objects.equals(geo, address.geo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, suite, city, zipcode, geo);
    }
}

class GeoLocation {

    private String lat;
    private String lng;

    protected GeoLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    String getLat() {
        return lat;
    }

    String getLng() {
        return lng;
    }

    void setLat(String lat) {
        this.lat = lat;
    }

    void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return Objects.equals(lat, that.lat) && Objects.equals(lng, that.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

}