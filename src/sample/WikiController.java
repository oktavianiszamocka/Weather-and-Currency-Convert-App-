package sample;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;


import java.net.URL;
import java.util.ResourceBundle;

public class WikiController implements Initializable{

    String city;
    @FXML
    public  WebView webView;

    public void setCity(String city1){
        city = city1;
        browser();
    }

    public void browser(){
        if(city.contains(" ")){
            city = city.replaceAll(" ", "_");
        }
        String url = "https://wikitravel.org/en/" + city;

        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
