/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.util.Alerts;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import workshop.javafx.jdbc.Main;

/**
 *
 * @author David
 */
public class MainViewController implements Initializable{

    @FXML
    private MenuItem menuItemVendedor;
    
    @FXML
    private MenuItem menuItemDepartamento;
    
    @FXML
    private MenuItem menuItemSobre;
    
    public void onMenuItemVendedorAction(){
    
        System.out.println("onMenuItemVendedorAction");
        
    }
    
    public void onMenuItemDepartamentoAction(){
    
        System.out.println("onMenuItemDepartamentoAction");
        
    }
    
    public void onMenuItemSobreAction(){
    
        LoadView("/gui/Sobre.fxml");
        
    }
    
    @Override
    public void initialize(URL uri, ResourceBundle rb) {
  
    }
    
    public void LoadView (String absoluteNome){ /*Metodo para carregar a tela */
        
        try{
            FXMLLoader load = new FXMLLoader (getClass().getResource(absoluteNome)); /*Instanciando o objeto FXMLLoader e recebendo como parametro os metodos padrões para este caso*/
            VBox newBox = load.load(); /*Objeto do tipo vbox usando o metodo load da classe instanciada acima FXMLLoader que abre a tela */
            
            Scene mainScene = Main.geMainScene();
            
            VBox mainVBOx = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
            
            Node mainMenu = mainVBOx.getChildren().get(0);
            mainVBOx.getChildren().clear();
            mainVBOx.getChildren().add(mainMenu);
            mainVBOx.getChildren().addAll(newBox.getChildren());
            
        }
        catch (IOException e){
        
            Alerts.showAlert("IO Exception", "Erro ao carregar a tela", e.getMessage(), Alert.AlertType.ERROR);
            
        }
    }
    
}
