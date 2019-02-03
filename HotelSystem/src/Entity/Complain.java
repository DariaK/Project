package Entity;

public class Complain {

    String fio;
    String text;

    public String getFio() {
        return fio;
    }

    public String getText() {
        return text;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

   public Complain (String fio, String text){
        this.fio = fio;
        this.text = text;
    }
}
