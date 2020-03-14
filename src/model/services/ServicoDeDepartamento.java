/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.services;

import java.util.ArrayList;
import java.util.List;
import model.entities.Departamento;

/**
 *
 * @author David
 */
public class ServicoDeDepartamento {
    
    public List<Departamento> findAll (){ /*Retornando por enquanto com os dados mocados*/
    
        List<Departamento> list = new ArrayList<>();
        
        list.add(new Departamento(1, "Livros"));
        list.add(new Departamento(2, "Computadores"));
        list.add(new Departamento(3, "Eletronicos"));
        return list;
        
    }
    
}
