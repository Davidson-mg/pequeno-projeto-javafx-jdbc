
package model.dao;

import java.util.List;
import model.entities.Departamento;

/**
 *
 * @author David
 */
public interface DepartamentoDao {
    
    void insert(Departamento obj);
    void update(Departamento obj);
    void delete(Integer id);
    Departamento findById(Integer id); /*Vai pegar o id e consultar no BD se existe esse id. Se não existe, retorna null*/
    List<Departamento> findAll ();
    
}
