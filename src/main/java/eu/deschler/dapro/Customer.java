package eu.deschler.dapro;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.Set;
import java.util.HashSet;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Customer {
    private ObjectId id;
    @BsonProperty(value = "customer_no")
    private Integer customerNo;
    private String gender;
    @BsonProperty(value = "first_name")
    private String firstName;
    @BsonProperty(value = "last_name")
    private String lastName;
    private Contact contact;
    @BsonProperty(value = "date_of_birth")
    private LocalDate dateOfBirth;
    private Character[] driversLicenseClasses;
    @BsonProperty(value = "driving_license_classes")
    private Set<String> drivingLicenseClasses = new HashSet<>();

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId objectId) {
        this.id = objectId;
    }
    public Integer getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(Integer customerNo) {
        this.customerNo = customerNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDrivingLicenseClasses(Set<String> drivingLicenseClasses) {
        this.drivingLicenseClasses = drivingLicenseClasses;
    }

    public Set<String> getDrivingLicenseClasses() {
        return drivingLicenseClasses;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        return "Customer [contact=" + contact + ", customerNo=" + customerNo + ", dateOfBirth=" + sdf.format(dateOfBirth)
                + ", firstName=" + firstName + ", gender=" + gender + ", lastName=" + lastName + ", objectId="
                + id + ", drivingLicenseClasses=" + drivingLicenseClasses + "]";
    }

}