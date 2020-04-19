/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.services.ServicoDeDepartamento;
import model.services.ServicoVendedor;
import workshop.javafx.jdbc.Main;

/**
 *
 * @author David
 */
public class VendedorListController implements Initializable, DataChangeListener {
 
	private ServicoVendedor servico; /*Declarando uma dependencia da classe serviçoDeDepartamento. Não estamos utilizando o " = new Departamento ();"
    pois seria um acoplamento forte. Ao inves disso, vamos usar mais abaixo o metodo setServicosDeDepartamento para injetar dependencia*/
    
    @FXML
    private TableView<Vendedor> tableViewVendedor; /*Variavel correspondete a tabela*/ 
    
    @FXML
    private TableColumn<Vendedor, Integer> tableColumnId; /*variavel correspondente a coluna da tabela*/
    
    @FXML
    private TableColumn<Vendedor, String> tableColumnNome;
    
    @FXML
    private TableColumn<Vendedor, String> tableColumnEmail;
            
    @FXML
    private TableColumn<Vendedor, Date> tableColumnDataNascimento;
    
    @FXML
    private TableColumn<Vendedor, Double> tableColumnSalarioBase;
    
    @FXML
    private TableColumn <Vendedor, Vendedor> tableColumnEDIT; /*Esse atributo têm haver com atualizar as colunas de tabela*/
    
    @FXML
    private Button btNew;
    
    @FXML
    private TableColumn<Vendedor, Vendedor> tableColumnREMOVE;
    
    
    private ObservableList<Vendedor> obsList; /*ObservableList é do javaFX.collections. Vamos carregar os departamentos nessa ObservableList através do metodo
    "updateTableView" criado mais abaixo, e em seguida será associado a tableview para aperecer na tabela do app*/
    
    @FXML
    public void onBtNewAction (ActionEvent evento){ /*Quando clicar no btn "novo", vamos chamar o metodo criandoDialogoForm que executa */
    /*É necessario passar o ActionEvent como parametro que vai ser um referencia do controle que recebeu o envento. Esse evento foi criado lá na 
    classe utils no metodo palcoAtual do tipo Stage. Abaxio vamos criar uma variavel parenteStage dot Stage que vai armazenar o palco atual que 
    pegamos lá no metodo "palcoAtual" da classe "utils" */    
        
    Stage parenteStage = Utils.palcoAtual(evento); /*Variavel parenteStage do tipo parenteStage que vai armazenar meu palco atual*/
     Vendedor obj = new Vendedor();
        criandoDialogoForm(obj, "/gui/VendedorForm.fxml", parenteStage); /*parenteStage é o meu palco atual */
    
    }
    
    public void setServicoVendedor (ServicoVendedor servico){ /*Usando esse metodo para não precisar colocar o " = new Departamento ();" na 
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
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        Utils.formatTableColumnDate(tableColumnDataNascimento, "dd/MM/yyyy"); /*Chamando o metodo formatTableColumnDate na classe Utils do pacote
        gui.utils resposavel por formatar a data. Passando como parametro a variavel que armazena a data e o formato que eu quero*/
        tableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));/*Chamando o metodo formatTableColumnDate na classe Utils do pacote
        gui.utils resposavel formatar numeros do tipo double. Passando como parametro a variavel que armazena o numero e o numero de casas decimais*/
        Utils.formatTableColumnDouble(tableColumnSalarioBase, 2); /*Chamando o metodo formatTableColumnDouble da classe Utils que formata o valor do salario com duas casas decimais*/
        
        /*Aqui vamos fazer com que a tabela preencha toda tela do app */
        Stage stage = (Stage) Main.getMainScene().getWindow();/*Pegando uma referente para o stage. Com o getMainScene acessamos a cena e com o getWindow dizemos que essa
        referencia deve ser para janela, O Window é uma super classe para o Stage, por isso devemos fazer o dawncating para stage*/
        
        /*Aqui vamos fazer com que, ao aumentar e diminuir o tamanho da tela do app com o mouse, a tabela acompanhe o tamanho*/
        tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
        
    }
    
    public void updateTableView (){ /*Esse metodo vai ser reposavel por acessar o servico, carregar os departamentos e lançar na ObservableList criada acima.
        De lá, vai ser associado a tableview para aperecer na tabela do app*/
    
        if (servico == null){ /*Se por acaso o programador esquecer de lançar o serviço*/
        
            throw new IllegalStateException ("Seviço está vazio");
        
        }
    
        List<Vendedor> list = servico.findAll(); /*Se passar na condicional acima, vai pegar através do metodo findAll criado na classe 
        ServicoDeDepartamento, todos os dados mocados da lista em ServicoDeDepartamento*/
        
        obsList = FXCollections.observableArrayList(list); /*Agora que temos os dados da lista na variavel list na linha de cima, estamos lançando
        na instancia do ObservableList que é obsList*/
        
        tableViewVendedor.setItems(obsList); /*Vai carregar os instens na tableview e mostrar na tela*/
        
        initEditButtons(); /*Chamando o metodo que acrescenta um btn de edição em cada linha da minha tabela de dados*/
        initRemoveButtons(); /*Chamando o metodo que acrescenta um btn de remove em cada linha da minha tabela de dados*/
    
    }
    
    private void criandoDialogoForm (Vendedor obj, String absoluteNome, Stage parentStage){ /*Função para abrir um nova jenela afim de preencher um novo departamento*/
    /*é necessario passar o Stage (palco) da tela que vai chamar a nova tela, nesse caso uma tela de formulario*/
        try{
            FXMLLoader load = new FXMLLoader (getClass().getResource(absoluteNome)); /*Instanciando o objeto FXMLLoader e recebendo como parametro os metodos padrões para este caso*/
            Pane pane = load.load();
            
            VendedorFormController  controler = load.getController();
            controler.setVendedor(obj);
            controler.setServicos(new ServicoVendedor(), new ServicoDeDepartamento());
            controler.carregarObjetosAssociados();
            controler.updateFormData();
            controler.subscribeDataChangeListener(this); /*Atualizando a tela*/
            
            Stage dialogStage = new Stage(); /*Quando eu crio uma nova janela na frente da outra, eu preciso criar um novo stage (palco). Vai ser um pauco na frente do outro*/
            dialogStage.setTitle("Informe os dados do vendedor"); /*titulo que da nova janela*/
            dialogStage.setScene(new Scene(pane)); /*Vai criar uma nova scena, nova janela*/
            dialogStage.setResizable(false); /*Diz que a janela pode ou não ser redimensionada. Neste caso não*/
            dialogStage.initOwner(parentStage); /*Vai entrar o parentStage que é o pai desse janela*/
            dialogStage.initModality(Modality.WINDOW_MODAL); /*Enquanto vc não fechar a janela vc não pode acessar a janela anterior*/
            dialogStage.showAndWait();
        }
        catch(IOException e){
        	
        	e.printStackTrace();
            Alerts.showAlert("IOException", "Erro ao carregar a view", e.getMessage(), Alert.AlertType.ERROR);
        
        }
    
    }

    @Override
    public void onDataChanged() { /*atualizando a tela*/
        updateTableView();
    }
    
    private void initEditButtons() {/*Esse metodo criar um btn de edição em cada linha da tabela. O codigo desse metodo é
        especifico de um fremeork, por isso não dei muitas explicações sobr o funcionamento dele.
        Esse metodo será chamado no metodo "updateTableView"*/
        
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));  
        tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {   
        private final Button button = new Button("Editar"); 
 
        @Override   
        protected void updateItem(Vendedor obj, boolean empty) {    
            super.updateItem(obj, empty); 
 
            if (obj == null) {     
                setGraphic(null);     
                return;    
            } 
 
            setGraphic(button);    
            button.setOnAction(    
                    event -> criandoDialogoForm(     
                            obj, "/gui/VendedorForm.fxml",Utils.palcoAtual(event)));   
        }  
        
        }); 
    }
    
    private void initRemoveButtons() { /*Esse metodo vai criar um bto de remove em cada linha da tabela. O codigo desse metodo é
        especifico de um fremeork, por isso não dei muitas explicações sobr o funcionamento dele.*/  
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));  
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {         
            private final Button button = new Button("Remover"); 
 
        @Override         
        protected void updateItem(Vendedor obj, boolean empty) {             
            super.updateItem(obj, empty); 
 
            if (obj == null) {                 
                setGraphic(null);                 
                return;             
            } 
 
            setGraphic(button);             
            button.setOnAction(event -> removeEntity(obj));         
        }     
        }); 
    }
    
    private void removeEntity (Vendedor obj){
    
        Optional <ButtonType> resultado = Alerts.showConfirmation("Confimação", "Têm certeza que deseja deletar?");/*Estou
        armazenando na varial resultado o resultado do botão que o usuario clicou (sim ou não). O Optional é um objeto que 
        carrega outro obejeto dentro dele podendo este objeto estar presente ou não, sendo necessario usar um .get para acessar
        */
        
        if (resultado.get() == ButtonType.OK){/*Se for igual ok é pq ele confirmou a deleção*/
        
            if(servico == null){ /*verificando se o programado injetou o serviço*/
                
                throw new IllegalStateException("Seviço está nulo!");
            
            }
        
            try{
            
                servico.remove(obj);
                updateTableView(); /*chamando o metodo que atualiza os dados da tebela de dados*/
            
            }
            catch(DbException e){ /*O correto seria usarmos a exceção "DbIntegrityExcepetion", mas devemos usar a mesma exceção usada no 
            metodo de delete na classe DaoJDBC (nesse caso DepartamentoDaoJDBC)*/
            
                Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), Alert.AlertType.ERROR);
            
            }
        }
    }
    
}
