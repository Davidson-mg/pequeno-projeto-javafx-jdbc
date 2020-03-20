/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class Utils {
    
    public static Stage palcoAtual (ActionEvent evento){
    
        return (Stage) ((Node) evento.getSource()).getScene().getWindow();
    
    }
    
}
