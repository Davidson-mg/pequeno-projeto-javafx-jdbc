/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
    
    
    public static Integer tryParseToInt (String str){ /*Este metodo converte de string para int. Vamos usa-lo nos campos txt que vão receber valores inteiros, pois txt só aceita string*/
        
        try{
            
            return Integer.parseInt(str);
            
        }
        catch(NumberFormatException e){
        
            return null;
            
        }
              
    }




	public static Double tryParseToDouble(String str) { /*Este metodo converte de string para Double. Vamos usa-lo nos campos txt que vão receber valores Double, pois txt só aceita string*/
        try{
            
            return Double.parseDouble(str);
            
        }
        catch(NumberFormatException e){
        
            return null;
            
        }
	}
   
    
    public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) { /*Este metodo formata a data na coluna
        correpondente a tabela de dados recebendo o formato como parametro e variavel que aramena o valor*/  
            
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
    
    
    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) { /*Este metodo formata numeros com ponto
        flutuante para tenham duas casas decimais na coluna corresponte na tabela de dados*/  
        
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

    public static void formatDatePicker(DatePicker datePicker, String format) { /*Este metodo formata a data no DatePicker do formulario*/ 
        datePicker.setConverter(new StringConverter<LocalDate>() {      
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format); {    
                datePicker.setPromptText(format.toLowerCase());   
            } 
 
        @Override   
        public String toString(LocalDate date) {    
            if (date != null) {     
                return dateFormatter.format(date);    
            } else {     
                return "";    
            }   
        } 

        @Override   
        public 
              LocalDate fromString(String string) {    
                  if (string != null && !string.isEmpty()) {     
                      return LocalDate.parse(string, dateFormatter);    
                  } else {     
                      return null;    
                  }   
              }  
        }); 
    }  

}
