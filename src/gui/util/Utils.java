/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
    
    public static <T> void formatTableColumnDate(
        TableColumn<T, Date> tableColumn, String format) {  
            
            tableColumn.setCellFactory(column -> {   
            
                TableCell<T, Date> cell = new TableCell<T, Date>() {    
                    private SimpleDateFormat sdf = new SimpleDateFormat(format); 
 
        @Override    
        protected void updateItem(Date item, boolean empty) {     
            super.updateItem(item, empty);     
                if (empty) {      
                    setText(null);     
                } 
                else{      
                    setText(sdf.format(item));     
                }    
            }   
        }; 
 
        return cell;  
        
            }); 
        }
    
    
    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {  
        
        tableColumn.setCellFactory(column -> {   
            
            TableCell<T, Double> cell = new TableCell<T, Double>(){ 
 
                @Override    
                protected void updateItem(Double item, boolean empty){     
                    
                    super.updateItem(item, empty);     
                    
                    if (empty) {      
                        setText(null);     
                    } 
                    else {      
                        Locale.setDefault(Locale.US);      
                        setText(String.format("%."+decimalPlaces+"f", item));     
                    }    
                }   
            }; 
 
                return cell;
                
            }); 
        } 
    }
