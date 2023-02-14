module com.example.proyectoapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires org.json;


    opens com.example.proyectoapi to javafx.fxml;
    exports com.example.proyectoapi;
}