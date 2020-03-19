
package db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DB {
    
    private static Connection conn = null;

    
    public static Connection getConnection (){ /*Este metodo vai abrir a conexão com o banco*/
    
        if (conn == null){ 
        
            try{
                Properties prop = loadPropriedades();/*Pegando as propriedades do aquivo "bd.propriedades" através do metodo abaixo loadPropriedades*/

                String url = prop.getProperty("dburl"); /*Agora que a propriedade prop têm é igual ao metodo abaixo, vamos usa-lo pra carregar na variavel
                do tipo string url a o endereco na linha "dburl" do arquivo que criamos com as propriedades de conexão para com o banco*/

                conn = (Connection) DriverManager.getConnection(url, prop); /*Estebelecendo com conexão com o BD. É necessario chamar a classe do jdbc 
                DriverManager (têm que importar o java.sql.DriverManager) passando como parametro a variavel prop (possue os parametros do metodo loadPropriedades)
                e a variavel url, que agora possui o endereço de conexão com o banco desejado
                *Vamos precisar tratar a exceção SQLException no catch. */
                
                
                /*Ao ser salvo a conexão com o BD na variavel "conn" a primeira vez, na segunda em diante, ele não vai mais ser nulo, pulando então o
                if e retornando a conexão já existente*/
            }
            catch (SQLException e){ /*Tratando a exceção acima SQLException*/
            
                throw new DbException(e.getMessage()); /*Lançando a exceção personalizada que criamos e herda da classe de exceções do java RuntimeException*/
                /*Repare que estamos capturando SQLException mas lançando nossa exceção personalizada DbException. Isso acontece pq o DbException
                herda da classe RuntimeException que não te obriga a tratar, e a exceção SQLException herda da classe Exception que nos obriga a tratar
                Lembrando que criamos o DbException por dois motivos. Ter nossa exeção personalizada e não precisar ficar tratando toda hora por meio da herança com a RuntimeException */
                
            }      
        }
        
           
        return conn; /*Deve retornar o valor da variaveL conn que criamos acima. Mas não pode retornar nulo, por isso o if acima.*/
    } 
    
        private static Properties loadPropriedades (){ /*Metodo privado pois vai ser acessado somente dentro da classe, e estatico pois seus valores
        não serão alterados. "Properties" é um classe do proprio java e estamos dando o nome de propriedades.
        A ideia neste metodo é ler o arquivo db.propriedades e armazenar no objeto do tipo Properties.
        */
        
        try (FileInputStream fs = new FileInputStream ("bd.propriedades")){ /*Vai pegar o caminho do arquivo. Neste caso, o arquivo
            está na pasta raiz do projeto, então não precisa digitar o camihho, basta digitar o nome do arquivo. 
            *Vamos precisar tratar a exceção FileNotFaundException no catch. */
            
            
            Properties prop = new Properties(); /*Instaciando o objeto Properties e dando o nome de prop*/
            prop.load(fs); /*usando o prop para chamar a função da classe Properties que lê o arquivo no caminho especificado, neste caso fs.
            *Vamos ter que tratar a exceção IOException no catch*/
        
            return prop;
            
        }
            catch (IOException e){ /*O IOException trata também a exceção FileNotException. Neste caso estamos tratando as duas exceções acima*/

                throw new DbException (e.getMessage()); /*Lançando a exceção personalizada que criamos e herda da classe de exceções do java RuntimeException*/
        
            }
    
        }
    
        public static void closeStatement(Statement st) {
            if(st != null){
                try{
                    st.close();
                    /*Vamos precisar tratar a exceção SQLException no catch.*/
                }
                catch(SQLException e){
                    throw new DbException(e.getMessage()); /*Se acontecer um exceção, nós capturamos e lançamos na nossa exceção personalizada*/  
                }    
            }
        }
        
        
        public static void closeConnection (){
		
            if(conn != null){ /*Estou verificando se conn está instanciado (se possui uma conexão aberta)*/
                try{
                    conn.close(); /*Se houve conexão com o banco, está encerrando a conexão.
                    *Vamos precisar tratar a exceção SQLException no catch.*/
            }
            catch(SQLException e){ /*Tratando a exceção acima SQLException usando nossa exceção personalizada*/
                throw new DbException(e.getMessage());
                }
            }

        }
    
    
        public static void closeConnection(Statement st) {

            if(st != null){ /*Estou verificando se conn está instanciado. Se estiver ....*/
                try{
                    st.close();
                }
                catch(SQLException e){ /*Tratando a exceção acima SQLException usando nossa exceção personalizada*/
                    throw new DbException(e.getMessage());
                }
            }
        }
        
        
        public static void closeResulSet(ResultSet rs) {
            if(rs != null){
                try{
                    rs.close();
                    /*Vamos precisar tratar a exceção SQLException no catch.*/
                }
                catch(SQLException e){
                    throw new DbException(e.getMessage()); /*Se acontecer um exceção, nós capturamos e lançamos na nossa exceção personalizada*/  
                }    
            }
        }
 
}
