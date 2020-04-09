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
    
    public static Stage palcoAtual (ActionEvent evento){ /*ActionEvent é o evento que o btn recebu. Vai retornar o palco atual*/
    
        return (Stage) ((Node) evento.getSource()).getScene().getWindow();
        /*getSource é do tipo objeto (generico) mas queremos ele do tipo Node, por isso usamos o casting de node. A partir daí estamos pegando
        a cena com o getScene e depois a janela com getWindow. O getWindow é uma superclasse do Stage e por isso precisamos converter para Stage
        usando o Casting*/
    }
    
    public static Integer tryParsetToInt (String str){
    
        try{
            
            return Integer.parseInt(str);
            
        }
        catch(NumberFormatException e){
        
            return null;
            
        }
        
        
    }
    
}
