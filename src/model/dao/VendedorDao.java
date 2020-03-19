/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.entities.Departamento;
import model.entities.Vendedor;

/**
 *
 * @author David
 */
public interface VendedorDao {
    
    void insert(Vendedor obj);
    void update(Vendedor obj);
    void delete(Integer id);
    Vendedor findById(Integer id); /*Vai pegar o id e consultar no BD se existe esse id. Se não existe, retorna null*/
    List<Vendedor> findAll ();
    List<Vendedor> findByDepartamento (Departamento departamento);
}
