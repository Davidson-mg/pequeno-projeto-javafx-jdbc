/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.impl;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import db.DB;
import java.sql.PreparedStatement;
import java.sql.Date;
import db.DbException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

/**
 *
 * @author David
 */
public class VendedorDaoJDBC implements VendedorDao{
    
    
    private Connection conn; /*Estamos criando essa dependencia com o connection para que não seja necessario ficar criando um objeto de conexão
    toda vez que precisarmos de uma consulta no banco nos metodos abaixo*/

    public VendedorDaoJDBC(Connection conn) { /*Construtor*/
        this.conn = conn;
    }
    
    @Override
    public void insert(Vendedor obj) {
        
        PreparedStatement st = null; /*statement serve para lançar uma quary no sql*/
        
        try{ 
            st = conn.prepareStatement(
                    "insert into vendedor (nome, email, dataNascimento, salarioBase, DepartamentoId)  VALUES  (?, ?, ?, ?, ?) ",
                    Statement.RETURN_GENERATED_KEYS /*vai retornar o ID do novo vendedor inserido*/
            );
            
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
            st.setDouble(4, obj.getSalarioBase());
            st.setInt(5, obj.getDepartamento().getId());
            
            int linhasAfetadas = st.executeUpdate(); /*Vai executar o comando SQL e inserir na variavel*/
            
            if (linhasAfetadas > 0){ /*Se as linhas afetadas for maior que zero é pq inseriu*/
            
                ResultSet rs = st.getGeneratedKeys();/*ResulSet pode receber mais de um valor tipo um vetor, para caso faça mais de um insert ao mesmo tempo.
                Vai receber o valor contido no "Statement.RETURN_GENERATED_KEYS" escrito no insert do BD.*/
                
                if (rs.next()){
                    
                    int id = rs.getInt(1); /*Vai pegar o valor da primeira coluna que é referente ao ID e que é inteiro*/
                    obj.setId(id); /*Vou atribuir esse ID dentro do meu objeto obj*/
                    
                }
                
                DB.closeResulSet(rs); /*Apenas fechando o ResultSet. Não estou fechando no finally pq foi usado somente dentro do if*/
            
            }
            else{
            
                throw new DbException("Erro: nennhuma linha foi afetada!");
            
            }
            
        }
        catch (SQLException e) {
            throw new DbException (e.getMessage());
        }
        finally{
        
            DB.closeStatement(st);
        
        }
    }
    
    

    @Override
    public void update(Vendedor obj) {
        
        PreparedStatement st = null;
        
        try{
        
            st = conn.prepareStatement(
                    "update vendedor  set nome = ?, email = ?, dataNascimento = ?, salarioBase = ?, DepartamentoId = ?  where Id = ? "
                    
            ); 
        
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
            st.setDouble(4, obj.getSalarioBase());
            st.setInt(5, obj.getDepartamento().getId());
            st.setInt(6, obj.getId());
            
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
        
            st = conn.prepareStatement("delete from vendedor where Id = ? ");
            
            st.setInt(1, id);
            
            int linhas = st.executeUpdate();
            
            System.out.println("Feito!");
            
            if (linhas == 0){
            
                throw new DbException("Nenhuma linha foi afetada");
            
            }
            
        } 
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
        
            DB.closeStatement(st);
            
        }
        
    }
    

    @Override
    public Vendedor findById(Integer id) { /*Vai buscar por id*/
        PreparedStatement st = null;
        ResultSet rs = null; /*Estou excutando a quary e guardando o resultado no ResultSet. ResultSet serve para gerar um obj no formato de tabela semelhante
        as geradas pelo sql quando fazemos uma consulta*/
        
            try{
                
                st = conn.prepareStatement( 
                    "SELECT Vendedor.*, Departamento.nome as depNome " 
                    +"FROM Vendedor INNER JOIN Departamento "  
                    +"ON Vendedor.DepartamentoId = departamento.Id "  
                    +"WHERE vendedor.id = ?"
                ); /*Quando a consulta for muito grande e não tiver como fazer na mesma linha. Não esquecer dos espaços ao final de cada linha, se não da erro de sintax no mysql*/
                
                st.setInt(1, id);
                rs = st.executeQuery(); /*Inicialmente a variavel do tipo rs ResultSet inicial com zero. Quando excutamos uma consulta, ela é armazenada na variavel rs*/
                
                
                if(rs.next()){ /*Se o rs não receber nenhum registro, vai da falso. Se for verdadeiro, signfica que retornou com uma linha de registro do banco*/
                    
                    /*Vamos pegar os registros de todas a colunas que serão retornadas na consulta e armazenar nos meus objetos Departamento e Vendedor*/
                    
                    Departamento dep = instanciandoDepartamento(rs); /*Chamando o metodos que serão responsaveis por passar para as classe Departamento e Vendedor o atributos retornados na consulta do mysql*/
                    Vendedor obj = instanciandoVendedor(rs, dep);
                    
                    return obj;
                    
                }
                return null;
            
            } 
            catch (SQLException e) {
                throw new DbException (e.getMessage());
            }
            finally {
            
                DB.closeStatement(st);
                DB.closeResulSet(rs);
            }     
    }

    @Override
    public List<Vendedor> findAll() { /*Vai buscar todos os vendedores e ordenar por nome */
        
        PreparedStatement st = null;
        ResultSet rs = null;
        
            try{
            
                st = conn.prepareStatement(
                    "select vendedor.*, departamento.nome as DepNome "
                        +"from vendedor inner join departamento "
                        +"on vendedor.departamentoId = departamento.Id "
                        +"group by nome "    
                );
                
                rs = st.executeQuery();
                
                
                /*O NOSSO OBJETIVO A PARTIR DE AGORA É TER MAIS DE UM VENDEDOR APONTANDO PARA O MESMO DEPARTAMENTO.
                                
                                Um vendedor pode estar associado a um ou mais departamentos*/
                
                
                /* A solução comentada abaixo é incorreta pois para cada vendedor (cada linha do ResultSet) eu estaria criando um novo departamento
                
                List<Vendedor> list = new ArrayList();
                
                while(rs.next()){
                
                    Departamento dep = instanciandoDepartamento (rs);
                    Vendedor obj = instanciandoVendedor (rs, dep);
                    list.add(obj);
                }
                return list
                
                */
                
                List<Vendedor> list = new ArrayList<>(); /*Um vendedor pode estar associando a mais de um departamento. Por este motivo, o obj
                vendedor terá que ser uma lista. */
                
                Map<Integer, Departamento> map = new HashMap<>();/*Criamos um Map vazio com o objetivo abaixo de guardar todo departamento que instaciarmos*/
                
                while(rs.next()){ /*next() move para o proximo e retorna null se estiver vazio. Neste caso, o rs pode receber mais de um registro, por isso, 
                    ao contrato do que foi feito no metodo acima "Vendedor findById" onde só é retornado no maximo um registro, ao invez de um if, nós usamos um while para percorrer todos os registros retornados*/
                
                    Departamento dep = map.get(rs.getInt("departamentoId")); /*Toda vez que passar pelo while, estamos testando se o departamento já existe.
                    Fazemos isso buscando no departamento (rs) o id que foi buscado na consulta acima. Se não existir, o map vai retornar nulo pra variavel dep*/
                    
                    if(dep == null){ /*Só vamos instanciar o departamento se o dep for nulo. Pois quer dizer que ele não existe ainda*/
                    
                        dep = instanciandoDepartamento(rs); /*Instanciando meu departamento a partir do ResultSet (rs)*/
                        
                        map.put(rs.getInt("departamentoId"), dep);/*Estamos salvando o departamento dentro do map para que da proxima vez, o if não retorne null pois já existe*/
                        /*Lembrando que o primeiro valor do mep entre parentese corresponde a chave e o segundo ao valor. Neste caso, dep é o departamento que vou salvar*/
                    }
     
                    Vendedor obj = instanciandoVendedor(rs, dep); /*Instanciando o vendedor apontando para o meu departamento (dep)*/
                    
                    list.add(obj);
                    
                }
                
                return list;
            
            }
            catch(SQLException e){
            
                throw new DbException(e.getMessage());
            
            }
            finally {
            
                DB.closeStatement(st);
                DB.closeResulSet(rs);
            
            }
        
    }

    private Departamento instanciandoDepartamento(ResultSet rs) throws SQLException { 
                                                                /*Não vamos tratar a exceção que vão ocorrer abaixo, pois já está sendo
                                                                tratada acima. Vamos simplesmen propagar para eleminar os erros*/
        Departamento dep = new Departamento();
        dep.setId(rs.getInt("DepartamentoId")); /*dep.setId, vai passar para a classe Departamento. getInt vai pegar da consulta um valor inteiro na coluna DepartamentoId */
        dep.setNome(rs.getString("depNome")); /*estou pegando o nome do departamento pelo apelido que dei na consulta*/
        return dep;
    }

    
    
    private Vendedor instanciandoVendedor(ResultSet rs, Departamento dep) throws SQLException {
                                                                /*Não vamos tratar a exceção que vão ocorrer abaixo, pois já está sendo
                                                                tratada acima. Vamos simplesmen propagar para eleminar os erros*/
        
        
        Vendedor obj = new Vendedor();
        obj.setId(rs.getInt("id"));
        obj.setNome(rs.getString("nome"));
        obj.setEmail(rs.getString("email"));
        //obj.setDataNascimento(rs.getDate("dataNascimento"));/*Quando inserimos o DatePicker na tela de formulario é necessario trocar essa linha pela de baixo*/
        obj.setDataNascimento(new java.util.Date(rs.getTimestamp("dataNascimento").getTime()));
        obj.setSalarioBase(rs.getDouble("salarioBase"));
                    
        obj.setDepartamento(dep); /*Estamos associando o objeto vendedor com o departamento. Vale lembrar que essa associação não é feita pelo id
        de chave estrangeira das tabelas, e sim pelo atributo departamento criado na tabela vendedor*/
    
        return obj;
    }

    @Override
    public List<Vendedor> findByDepartamento(Departamento departamento) { /*Vai buscar por departamento*/
        
        PreparedStatement st = null;
        ResultSet rs = null;
        
            try{
            
                st = conn.prepareStatement(
                    "select vendedor.*, departamento.nome as DepNome "
                        +"from vendedor inner join departamento "
                        +"on vendedor.departamentoId = departamento.Id "
                        +"where departamentoId = ? "
                        +"group by nome "    
                );
                
                st.setInt(1, departamento.getId());
                rs = st.executeQuery();
                
                
                /*O NOSSO OBJETIVO A PARTIR DE AGORA É TER MAIS DE UM VENDEDOR APONTANDO PARA O MESMO DEPARTAMENTO.
                                
                                Um vendedor pode estar associado a um ou mais departamentos*/
                
                
                /* A solução comentada abaixo é incorreta pois para cada vendedor (cada linha do ResultSet) eu estaria criando um novo departamento
                
                List<Vendedor> list = new ArrayList();
                
                while(rs.next()){
                
                    Departamento dep = instanciandoDepartamento (rs);
                    Vendedor obj = instanciandoVendedor (rs, dep);
                    list.add(obj);
                }
                return list
                
                */
                
                List<Vendedor> list = new ArrayList<>(); /*Um vendedor pode estar associando a mais de um departamento. Por este motivo, o obj
                vendedor terá que ser uma lista. */
                
                Map<Integer, Departamento> map = new HashMap<>();/*Criamos um Map vazio com o objetivo abaixo de guardar todo departamento que instaciarmos*/
                
                while(rs.next()){ /*Neste caso, o rs pode receber mais de um registro, por isso, ao contrato do que foi feito no metodo acima "Vendedor findById"
                    onde só é retornado no maximo um registro, ao invez de um if, nós usamos um while para percorrer todos os registros retornados*/
                
                    Departamento dep = map.get(rs.getInt("departamentoId")); /*Toda vez que passar pelo while, estamos testando se o departamento já existe.
                    Fazemos isso buscando no departamento (rs) o id que foi buscado na consulta acima. Se não existir, o map vai retornar nulo pra variavel dep*/
                    
                    if(dep == null){ /*Só vamos instanciar o departamento se o dep for nulo. Pois quer dizer que ele não existe ainda*/
                    
                        dep = instanciandoDepartamento(rs); /*Instanciando meu departamento a partir do ResultSet (rs)*/
                        
                        map.put(rs.getInt("departamentoId"), dep);/*Estamos salvando o departamento dentro do map para que da proxima vez, o if não retorne null pois já existe*/
                        /*Lembrando que o primeiro valor do mep entre parentese corresponde a chave e o segundo ao valor. Neste caso, dep é o departamento que vou salvar*/
                    }
     
                    Vendedor obj = instanciandoVendedor(rs, dep); /*Instanciando o vendedor apontando para o meu departamento (dep)*/
                    
                    list.add(obj);
                    
                }
                
                return list;
            
            }
            catch(SQLException e){
            
                throw new DbException(e.getMessage());
            
            }
            finally {
            
                DB.closeStatement(st);
                DB.closeResulSet(rs);
            
            }
        
    }
    
}
