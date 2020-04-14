/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.services;

import java.util.List;
import model.dao.DaoFabrica;
import model.dao.VendedorDao;
import model.entities.Vendedor;

/**
 *
 * @author David
 */
public class ServicoVendedor {
    
    private VendedorDao dao = DaoFabrica.createVendedorDao(); /*Instanciando a interface DaoFabrica que retorna algum metodo da classe DepartamentoDaoJDBC que por sua vez
        é uma DepartamentoDao (DepartamentoDao é um interface e o DepartamentoDaoJDB implementa ela)*/
    
    public List<Vendedor> findAll (){  
    
        return dao.findAll(); /*Retornando o resultado do metodo findAll que é uma list*/
    
    }
    
    public void salvarOuAtualizar (Vendedor obj){ /*Vai verificar se devemos inserir um departamento no banco ou atualizar um existente*/
    
        if(obj.getId() == null){
        
            dao.insert(obj); /*Se o aluno o id é igual a nulo, significa que estamos inserindo um novo aluno*/
        
        }
        else {
        
            dao.update(obj); /*Se o id já existe, então chama o metodo de atualizar*/
        
        }
    
    }
    
    public void remove (Vendedor obj){ 
    
        dao.delete(obj.getId());
    
    }
}
