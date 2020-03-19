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
import model.entities.Departamento;
import model.services.ServicoDeDepartamento;
import workshop.javafx.jdbc.Main;
import gui.DepartamentoListController;
import java.util.function.Consumer;
import javax.sound.midi.ControllerEventListener;

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
    
        LoadView("/gui/DepartamentoList.fxml", (DepartamentoListController controler) -> {
        
            controler.setServicosDeDepartamento(new ServicoDeDepartamento());
            controler.updateTableView();
            
        });     
    }
    
    @FXML
    public void onMenuItemSobreAction(){
    
        LoadView("/gui/Sobre.fxml", x -> {});
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { /*Este metodo somos obrigados a criar quando implementamos o Initializable. OBS: ele é criado a principio fazio*/
  
    }
    
    public <T> void LoadView(String absoluteNome, Consumer<T> initializingAction){ /*Metodo para carregar a tela. Vai receber uma string e uma expressão lambda como paramentro */
        
        try{
            FXMLLoader load = new FXMLLoader (getClass().getResource(absoluteNome)); /*Instanciando o objeto FXMLLoader e recebendo como parametro os metodos padrões para este caso*/
            VBox newBox = load.load(); /*Objeto do tipo vbox usando o metodo load da classe instanciada acima, FXMLLoader que abre a tela */
            
            Scene mainScene = Main.getMainScene(); /*Variavel do tipo Scene que vai acessar a referencia da classe Scene através do metodo get que criamos */
            
            /*Nossa tela principal é feita em um ScrollPane (veja no fxml ou no SceneBuilder), e nele temos um vbox.
            Vamos carregar abaixo a tela "sobre" do menu "ajuda". A nossa intenção abaixo é carregar os filhos da tela "sobre" do menu "ajuda"*/
            
            VBox mainVBOx = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
            /* primeiro usamos um getRoot que vai pegar o primeiro elemento da view que é um ScrollPane. É necessario usar um casting antes do ScrollPane para mostrar quem está pegando um ScrollPane.
            Dentro do ScrollPane temos um Content (conteudo), devemos acessa-lo com o getContent. O Content já uma referencia para o Vbox.
            Por ultimo, realizamos um casting do VBox*/
            
            /*Quando usuario clicar no menu "ajuda" e depois em "sobre", vai carregar uma nova tela. Ao mudar de tela, o menuBar deve ser preservado
            Vamos excluir todos os filhos do vbox no ScrollPane, incluir o menuBar e em seguida os filho do menu na tela "sobre"*/
            
            Node mainMenu = mainVBOx.getChildren().get(0);/*Variavel do tipo Node que vai receber os filhos do vbox na janela principal "mainView"*/
            mainVBOx.getChildren().clear(); /*Vai limpar todos os filhos do mainVobox */
            mainVBOx.getChildren().add(mainMenu); /*Vai adicionar os filhos do vbox que foram armazenados na variavel mainMenu acima*/
            mainVBOx.getChildren().addAll(newBox.getChildren());/*Vai adicionar todos os filhos do newBox*/
            
            T controler = load.getController();
            initializingAction.accept(controler);
            
        }
        catch (IOException e){
        
            Alerts.showAlert("IO Exception", "Erro ao carregar a tela", e.getMessage(), Alert.AlertType.ERROR);
            
        }
    }
   
}
