package Entity;

public class Client {


    public String surname;
    public String name;
    public String patronymic;
    public String adres;
    public String phoneNumber;
    public String passport;
    public String org;

    public String getPassport() {
        return passport;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdres() {
        return adres;
    }

    public String getOrg() {
        return org;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Client (String surname, String name, String patronymic, String phoneNumber,String passport, String adres, String org){

        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.adres = adres;
        this.org = org;
    }
    public Client (String surname, String name, String patronymic, String phoneNumber, String passport){
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
    }
}
