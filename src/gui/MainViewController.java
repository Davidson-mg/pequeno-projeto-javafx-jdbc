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
import javafx.scene.control.MenuItem;

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
    
        System.out.println("onMenuItemSobreAction");
        
    }
    
    @Override
    public void initialize(URL uri, ResourceBundle rb) {
        
        
        
    }
    
}
