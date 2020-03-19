/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import com.mysql.jdbc.Connection;
import db.DB;
import model.dao.impl.DepartamentoDaoJDBC;
import model.dao.impl.VendedorDaoJDBC;

/**
 *
 * @author David
 */
public class DaoFabrica {
    
    public static VendedorDao createVendedorDao (){
    
        return new VendedorDaoJDBC((Connection) DB.getConnection());
        
    }
    
    public static DepartamentoDao createDepartamentoDao(){
    
       return new DepartamentoDaoJDBC ((Connection) DB.getConnection());     
  
    } 
}
