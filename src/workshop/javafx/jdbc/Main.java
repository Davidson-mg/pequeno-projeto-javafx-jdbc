/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop.javafx.jdbc;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class Main extends Application {
    
    private static Scene mainScene; /*Estamos criando um referencia externa para a classe Scene. Como ela é privada, vamos precisar de um metodo
    get para acessa-la fora da classe. Esse metodo foi criado mais abaixo*/
    
    public void start(Stage primaryStage) {  
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));   
            ScrollPane scrollPane = loader.load();

            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            mainScene = new Scene(scrollPane);   
            primaryStage.setScene(mainScene);   
            primaryStage.setTitle("Sample JavaFX application");   
            primaryStage.show();  
        
        } 
        catch (IOException e) {   
            e.printStackTrace();  
        } 
    }
    
    public static Scene getMainScene() {/*Metodo que acessa a referencia da classe Scene*/ 

        return mainScene;

    }
    
    public static void main(String[] args) {
        launch(args);
    }   
}
