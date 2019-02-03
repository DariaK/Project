package Entity;
public class Reserv {


    public String organiz;
    public String obj;
    public String floor;
    public String rooms;
    public String plase;
    public String entrydate;
    public String otmdate;

    public String getOrganiz() {
        return organiz;
    }

    public String getObj() {
        return obj;
    }

    public String getFloor() {
        return floor;
    }

    public String getRooms() {
        return rooms;
    }

    public String getPlase() {
        return plase;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public String getOtmdate() {
        return otmdate;
    }

    public void setOrganiz(String organiz) {
        this.organiz = organiz;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public void setPlase(String plase) {
        this.plase = plase;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public void setOtmdate(String otmdate) {
        this.otmdate = otmdate;
    }

   public Reserv(String organiz, String obj, String floor, String rooms, String plase, String entrydate,String otmdate){
        this.organiz = organiz;
        this.obj = obj;
        this.floor = floor;
        this.rooms = rooms;
        this.plase = plase;
        this.entrydate = entrydate;
        this.otmdate = otmdate;
   }

}
