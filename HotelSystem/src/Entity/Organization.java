package Entity;

import java.util.Objects;

public class Organization {

    String nameOrg;
    String adresOrg;

    public String getNameOrg() {
        return nameOrg;
    }

    public String getAdresOrg() {
        return adresOrg;
    }

    public void setNameOrg(String nameOrg) {
        nameOrg = nameOrg;
    }

    public void setAdresOrg(String adresOrg) {
        adresOrg = adresOrg;
    }

    public Organization (String nameOrg, String adresOrg){
        this.nameOrg = nameOrg;
        this.adresOrg = adresOrg;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization student = (Organization) o;
        return Objects.equals(nameOrg, student.nameOrg) &&
                Objects.equals(adresOrg, student.adresOrg);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOrg, adresOrg);
    }

}
