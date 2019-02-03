package Entity;

public class Entry {

    String corpus;
    String floor;
    String numberRoom;
    String state;
    String client;
    String DateEntry;
    String DateDep;

    public String getCorpus() {
        return corpus;
    }

    public String getFloor() {
        return floor;
    }

    public String getNumberRoom() {
        return numberRoom;
    }

    public String getState() {
        return state;
    }

    public String getDateEntry() {
        return DateEntry;
    }

    public String getDateDep() {
        return DateDep;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setNumberRoom(String numberRoom) {
        this.numberRoom = numberRoom;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDateEntry(String dateEntry) {
        DateEntry = dateEntry;
    }

    public void setDateDep(String dateDep) {
        DateDep = dateDep;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Entry (String corpus, String floor, String numberRoom, String state,  String client, String DateEntry, String DateDep){

        this.corpus = corpus;
        this.floor = floor;
        this.numberRoom = numberRoom;
        this.state = state;
        this.client = client;
        this.DateEntry = DateEntry;
        this.DateDep = DateDep;

    }


}
