/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.services.ServicoDeDepartamento;

/**
 *
 * @author David
 */
public class DepartamentoFormController implements Initializable {

    private Departamento entidades;
    
    private ServicoDeDepartamento servico;
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private Label labelErrorNome;
    
    @FXML
    private Button btSalvar;
    
    @FXML
    private Button btCancelar;
    
    public void setDepartamento(Departamento entidades){
    
        this.entidades = entidades;
    
    }
    
    public void setServicoDeDepartamento (ServicoDeDepartamento servico){
    
        this.servico = servico;
    
    }
    
    @FXML
    public void onBtSalvarAction(ActionEvent evento){
        if (entidades == null){
        
            throw new IllegalStateException("Entidade estava nulo");
        
        }
        if(servico == null){
        
            throw new IllegalStateException("Serviço estava nulo");
        
        }
        try{
        
            entidades = getFormeData();
            servico.salvarOuAtualizar(entidades);
            Utils.palcoAtual(evento).close();
        
        }
        catch(DbException e){
        
            Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        
        }
        
    
    }
    
    @FXML
    public void onBtCancelarAction(){
        
        System.out.println("Botão de cancelar");
    
    }
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializableNodes();
    }
    
    private void initializableNodes (){
    
        Constraints.setTextFieldInteger(txtId); /*Estou dizendo que txtId só vai aceitar numeros inteiros*/
        
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }
    
    public void updateFormData (){ /*Esse metodo vai pegar o valores o obj entidades do departamento e inserir nos txts  */
    
        if (entidades == null) {
    
            throw new IllegalStateException("Entidade é nulo");
            
        }
        txtId.setText(String.valueOf(entidades.getId())); /*a caixa de texto trabalha só com string. Estamos convertendo o inteiro do id pra string */
        txtId.setText(entidades.getNome());
    }

    private Departamento getFormeData() {
        
        Departamento obj = new Departamento();
        
        obj.setId(Utils.tryParsetToInt(txtId.getText()));
        obj.setNome(txtNome.getText());
        
        return obj;
        
    }
    
}
