/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.ServicoDeDepartamento;
import workshop.javafx.jdbc.Main;

/**
 *
 * @author David
 */
public class DepartamentoListController implements Initializable{
    
    private ServicoDeDepartamento servico; /*Declarando uma dependencia da classe serviçoDeDepartamento. Não estamos utilizando o " = new Departamento ();"
    pois seria um acoplamento forte. Ao inves disso, vamos usar mais abaixo o metodo setServicosDeDepartamento para injetar dependencia*/
    
    @FXML
    private TableView<Departamento> tableViewDepartamento;
    
    @FXML
    private TableColumn<Departamento, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Departamento, String> tableColumnNome;
    
    @FXML
    private Button btNew;
    
    
    private ObservableList<Departamento> obsList; /*ObservableList é do javaFX.collections. Vamos carregar os departamentos nessa ObservableList através do metodo
    "updateTableView" criado mais abaixo, e em seguida será associado a tableview para aperecer na tabela do app*/
    
    @FXML
    public void onBtNewAction (ActionEvent evento){ /*Quando clicar no btn "novo", vamos chamar o metodo criandoDialogoForm que executa */
        
        Stage parenteStage = Utils.palcoAtual(evento);
        criandoDialogoForm("/gui/DepartamentoForm.fxml", parenteStage);
    
    }
    
    public void setServicosDeDepartamento (ServicoDeDepartamento servico){ /*Usando esse metodo para não precisar colocar o " = new Departamento ();" na 
        variavel servicos do tipo ServicoDeDepartamento*/
    
        this.servico = servico; 
        
        /*Posteriormente, vamos pegar esse servico, carregar na classe departamento e mostrar dentro do objeto da tabela (TableView)*/
    
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { /*Este metodo somos obrigados a criar quando implementamos o Initializable. OBS: ele é criado a principio fazio*/
        
        initializebleNodes(); /*Chmando o metodo abaixo initializebleNodes*/
        
    }

    private void initializebleNodes() { /*Este metodo nós criamos manualmente, mas ele é padrão para iniciar o comportamento das colunas de uma tabela*/
        
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); /*Este comando serve para iniciar o comportamento da coluna ID da tabela*/
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome")); /*Este comando serve para iniciar o comportamento da coluna NOME da tabela*/
        
        /*Aqui vamos fazer com que a tabela preencha toda tela do app */
        Stage stage = (Stage) Main.getMainScene().getWindow();/*Pegando uma referente para o stage. Com o getMainScene acessamos a cena e com o getWindow dizemos que essa
        referencia deve ser para janela, O Window é uma super classe para o Stage, por isso devemos fazer o dawncating para stage*/
        
        /*Aqui vamos fazer com que, ao aumentar e diminuir o tamanho da tela do app com o mouse, a tabela acompanhe o tamanho*/
        tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
        
    }
    
    public void updateTableView (){ /*Esse metodo vai ser reposavel por acessar o servico, carregar os departamentos e lançar na ObservableList criada acima.
        De lá, vai ser associado a tableview para aperecer na tabela do app*/
    
        if (servico == null){ /*Se por acaso o programador esquecer de lançar o serviço*/
        
            throw new IllegalStateException ("Seviço está vazio");
        
        }
    
        List<Departamento> list = servico.findAll(); /*Se passar na condicional acima, vai pegar através do metodo findAll criado na classe 
        ServicoDeDepartamento, todos os dados mocados da lista em ServicoDeDepartamento*/
        
        obsList = FXCollections.observableArrayList(list); /*Agora que temos os dados da lista na variavel list na linha de cima, estamos lançando
        na instancia do ObservableList que é obsList*/
        
        tableViewDepartamento.setItems(obsList); /*Vai carregar os instens na tableview e mostrar na tela*/
        
    }
    
    private void criandoDialogoForm (String absoluteNome, Stage parentStage){ /*Função para abrir um nova jenela afim de preencher um novo departamento*/
    
        try{
            FXMLLoader load = new FXMLLoader (getClass().getResource(absoluteNome)); /*Instanciando o objeto FXMLLoader e recebendo como parametro os metodos padrões para este caso*/
            Pane pane = load.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Informe os dados do depatamento"); /*titulo que da nova janela*/
            dialogStage.setScene(new Scene(pane)); /*Vai criar uma nova scena, nova janela*/
            dialogStage.setResizable(false); /*Diz que a janela pode ou não ser redimensionada. Neste caso não*/
            dialogStage.initOwner(parentStage); /*Vai entrar o parentStage que é o pai desse janela*/
            dialogStage.initModality(Modality.WINDOW_MODAL); /*Enquanto vc não fechar a janela vc não pode acessar a janela anterior*/
            dialogStage.showAndWait();
        }
        catch(IOException e){
        
            Alerts.showAlert("IOException", "Erro ao carregar a view", e.getMessage(), Alert.AlertType.ERROR);
        
        }
    
    }
    
}
