/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.util.Constraints;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;

/**
 *
 * @author David
 */
public class DepartamentoFormController implements Initializable {

    private Departamento entidades;
    
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
    
    @FXML
    public void onBtSalvarAction(){
    
        System.out.println("Botão de Salvar");
    
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
    
}
