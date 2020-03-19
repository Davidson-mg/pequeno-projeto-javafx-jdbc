/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.services;

import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFabrica;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

/**
 *
 * @author David
 */
public class ServicoDeDepartamento {
    
    private DepartamentoDao dao = DaoFabrica.createDepartamentoDao();
    
    public List<Departamento> findAll (){ /*Retornando por enquanto com os dados mocados*/
    
        return dao.findAll();
    
    }
    
}
