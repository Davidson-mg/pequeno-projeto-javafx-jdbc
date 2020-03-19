/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.impl;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import db.DB;
import db.DbException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

/**
 *
 * @author David
 */
public class DepartamentoDaoJDBC implements DepartamentoDao{
    
    private Connection conn;

    public DepartamentoDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Departamento obj) {
       
        PreparedStatement st = null;
      
        try{
        
            st = conn.prepareStatement(
                    "insert into departamento(nome) values (?)",
                    Statement.RETURN_GENERATED_KEYS /*vai gerar um valor id para cada inserção */   
            );
            
            st.setString(1, obj.getNome());
            
            int linhasAfetadas = st.executeUpdate();
            
            if(linhasAfetadas > 0){ 
      
                ResultSet rs = st.getGeneratedKeys(); /*ResultSet serve para gerar um obj no formato de tabela semelhante as 
                geradas pelo sql quando fazemos uma consulta. O getGeneratedKeys vai armazenar na tabela gerada pelo ResultSet os valores
                gerados em RETURN_GENERATED_KEYS. Será uma tabela de apenas uma coluna com os valores de RETURN_GENERATED_KEYS*/
                
                while (rs.next()){ /*Se o rs não receber nenhum registro, vai da falso. Se for verdadeiro, signfica que retornou com uma linha de registro do banco*/
                
                    int id = rs.getInt(1); /*Estou indicando que a variavel id vai receber o valor contido na primeira coluna da tabela gerada
                    pelo ResultSet*/
                    System.out.println("Feito! Id = "+id);
                }
            
            }
            
            System.out.println("Feito! Linhas afetadas: "+linhasAfetadas);
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        
    }

    @Override
    public void update(Departamento obj) {
        
        PreparedStatement st = null;
        
        try{
        
            st = conn.prepareStatement("update departamento set nome = ? where id = ? ");
            
            st.setString(1, obj.getNome());
            st.setInt(2, obj.getId());
            
            st.executeUpdate();
        } 
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
        
            DB.closeStatement(st);
        
        }
        
    }

    @Override
    public void delete(Integer id) {
        
        PreparedStatement st = null;
        try{
        
            st = conn.prepareStatement(
                    "DELETE FROM departamento WHERE Id = ?");
            
            st.setInt(1, id);
            
            st.executeUpdate();
            
            System.out.println("Feito!");

            
        } 
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
        
            DB.closeStatement(st);
            
        }       
        
    }

    @Override
    public Departamento findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("select * from departamento where id = ? ");
            
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if(rs.next()){
            
                Departamento dep = instaciandoDepartamento(rs);
                return dep;
            }
        
            return null;
        } 
        catch (SQLException e) {
            throw new DbException (e.getMessage());
        }
    }

    @Override
    public List<Departamento> findAll() {
        
        PreparedStatement st = null; 
        ResultSet rs = null;
        try{
        
            st = conn.prepareStatement("select * from departamento order by nome"); /*statement serve para lançar uma quary no sql*/
        
        rs = st.executeQuery(); /*Estou excutando a quary e guardando o resultado no ResultSet. ResultSet serve para gerar um obj no formato de tabela semelhante
        as geradas pelo sql quando fazemos uma consulta*/
        
        List<Departamento> list = new ArrayList<>(); /*Como serão retornados mais de uma linha na consulta, é necessario criar uma list, pois vamos percorrer
        a tabela gerada pelo ResulSet e armazenar os valores de suas linhas e colunas na classe departamento*/
             
        while(rs.next()){ /*next() move para o proximo e retorna null se estiver vazio. */
        
            Departamento dep = instaciandoDepartamento(rs); /*Chamando o metodo que vai escrever na classe Departamento os valores contidos na coluna id e nome*/
            list.add(dep);
        
        }
        
        return list;
                    
                    
        } 
        catch (SQLException e) {
            
            throw new DbException("Erro: "+e.getMessage());
        
        }
        finally{
        
            DB.closeConnection(st);
            DB.closeStatement(st);
            
        }
        
    }
    
    
    public Departamento instaciandoDepartamento (ResultSet rs) throws SQLException{
    
        Departamento dep = new Departamento();
        dep.setId(rs.getInt("id"));
        dep.setNome(rs.getString("nome"));
        return dep;
    
    }
    
}
