/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
import workshop.javafx.jdbc.Main;

/**
 *
 * @author David
 */
public class DepartamentoListController implements Initializable{
    
    @FXML
    private TableView<Departamento> tableViewDepartamento;
    
    @FXML
    private TableColumn<Departamento, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Departamento, String> tableColumnNome;
    
    @FXML
    private Button btNew;
    
    @FXML
    public void onBtNewAction (){
    
        System.out.println("coleeeee");
    
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { /*Este metodo somos obrigados a criar quando implementamos o Initializable. OBS: ele é criado a principio fazio*/
        
        initializebleNodes(); /*Chmando o metodo abaixo initializebleNodes*/
        
    }

    private void initializebleNodes() { /*Este metodo nós criamos manualmente, mas ele é padrão para iniciar o comportamento das colunas de uma tabela*/
        
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); /*Este comando serve para iniciar o comportamento da coluna ID da tabela*/
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome")); /*Este comando serve para iniciar o comportamento da coluna NOME da tabela*/
        
        
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartamento.prefHeightProperty().bind(stage.maxHeightProperty());
        
    }
    
    
    
}
