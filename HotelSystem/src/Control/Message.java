package Control;


import javafx.scene.control.Alert;

public class Message {

    public String messName;
    public String mess;

    public String getMessName() {
        return messName;
    }

    public String getMess() {
        return mess;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public Message (String messName, String mess){
        this.mess = mess;
        this.messName = messName;
    }

    public Message showAlertDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(messName);
        alert.setContentText(mess);
        alert.showAndWait();
        return null;
    }


}
