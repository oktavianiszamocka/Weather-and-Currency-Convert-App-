package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


public class Controller {
    Service service;
    @FXML
    public TextField country_name;
    public TextField city_name;
    public Button weather_button;
    public TextArea weather_textArea;
    public Label cur;
    public Label dest_cur;
    public TextField currency;
    public TextField cur_textfield;
    public TextField destCur_textfield;
    public TextField PLN_textfield;
    public TextField result_textfield;
    public Label result;




    public void initialize() {
        cur_textfield.setText("1");

    }

    public void weather_click(){
        service = new Service(country_name.getText());
        weather_textArea.setText(service.getWeather(city_name.getText()));
    }

    public void exchange_click(){
        Double rate = service.getRateFor(currency.getText());
        cur.setText(service.getBase());
        dest_cur.setText(currency.getText());
        destCur_textfield.setText(rate.toString());

    }

    public void NBP_click(){
        service.getNBPRate();
        result.setText(service.getBase());
        result_textfield.setText(service.getResult_cur());
        PLN_textfield.setText(service.getZloty());

    }

    @FXML
    public void getWiki(ActionEvent event){


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("wiki.fxml"));
            Parent root = (Parent) loader.load();
            WikiController wiki = loader.getController();

            wiki.setCity(service.getCity());

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 1000));
            stage.setTitle(service.getCity());
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
