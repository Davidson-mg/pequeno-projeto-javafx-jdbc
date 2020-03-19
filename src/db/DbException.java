
package db;


public class DbException extends RuntimeException { /*Criamos uma classe personalizada para exceção, e ela vai herdar da classe do java
    RuntimeException*/
    
    public DbException (String msg){ /*Construtor*/
    
        super(msg); /*Super para herdar os atributos da classe RuntimeException*/
    
    }  
}
