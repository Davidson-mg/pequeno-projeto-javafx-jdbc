
package db;

public class DbIntegrityExcepetion extends RuntimeException {
    
   public DbIntegrityExcepetion (String msg){
       super(msg);
   }
    
}
