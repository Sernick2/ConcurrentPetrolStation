module com.example.stacja_paliw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.stacja_paliw to javafx.fxml;
    exports com.example.stacja_paliw;
}