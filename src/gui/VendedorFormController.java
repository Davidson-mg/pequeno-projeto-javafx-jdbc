/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exception.ValidationException;
import model.services.ServicoDeDepartamento;
import model.services.ServicoVendedor;


/**
 *
 * @author David
 */
public class VendedorFormController implements Initializable {
    
    private Vendedor entidades; /*Essa variavel do tipo aluno é que vai me dar acesso aos metodos sets da classe Departamento. Por exemplo, no metodo
    bem abaixo (updateFormData), vamos precisar relacionar os campos do meu formulario com os seus respectivos atributos na classe Departamento, e por isso
    vamos precisar chamar os metodos gets da classe Departamento*/
    
    private ServicoVendedor servico; /*Precisamos criar esta varial para um dependencia com a classe ServicoDeDepartamento*/
    
    private ServicoDeDepartamento servicoDeDepartamento;
    
    private List<DataChangeListener> dataChangeListenners = new ArrayList <>();
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private TextField txtEmail;
            
    @FXML
    private DatePicker tpDataNascimento;
            
    @FXML
    private TextField txtSalarioBase;
    
    @FXML
    private ComboBox <Departamento> comboBoxDepartamento; /*Atributo referente ao comboBox onde vamos selecionar o Departamento desejado*/
    
    @FXML
    private Label labelErrorNome;
    
    @FXML
    private Label labelErrorEmail; /*Estes labels vão imprimir as msgs de erro*/
        
    @FXML
    private Label labelErrorDataNascimento; 
            
    @FXML
    private Label labelErrorSalarioBase;
    
    @FXML
    private Button btSalvar;
    
    @FXML
    private Button btCancelar;
    
    @FXML
    private ObservableList <Departamento> obsList;
    
    public void setVendedor(Vendedor entidades){ /*Necessario criar o set da variavel entidades*/
    
        this.entidades = entidades;
    
    }
    
    public void setServicos (ServicoVendedor servico, ServicoDeDepartamento servicoDeDepartamento){ /*Este metodo sets antes de implemetarmos o combobox para selecionar departamento, era um
    metodo set ServicoVendedor, mas alteremos seu nome para setServicos e acrescentamos a variavel servicoDeDepartamento. Dessa forma este unico metodo me da acesso as duas classes ServicoVendedor
    e ServicoDeDepartamento*/
    
        this.servico = servico;
        this.servicoDeDepartamento = servicoDeDepartamento;
    
    }
    
    
    public void subscribeDataChangeListener (DataChangeListener Listenner){ /*Esse metodo vai atualizar a lista quando clicarmos no btn salvar*/
    
        dataChangeListenners.add(Listenner);
    
    } 
    
    @FXML
    public void onBtSalvarAction(ActionEvent evento){
        if (entidades == null){
        
            throw new IllegalStateException("Entidade estava nulo");
        
        }
        if(servico == null){
        
            throw new IllegalStateException("Serviço estava nulo");
        
        }
        try{
        
            entidades = getFormeData();
            servico.salvarOuAtualizar(entidades);
            notificandoDataChangeListeners();
            Utils.palcoAtual(evento).close();
        
        }
        
        catch(ValidationException e){
        
            setErroMessages(e.getErros());
            
        }
        
        catch(DbException e){
        
            Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        
        }
    }
    
    @FXML
    public void onBtCancelarAction(ActionEvent evento){
        
        Utils.palcoAtual(evento).close();
    
    }
      
    @Override
    public void initialize(URL url, ResourceBundle rb) { /*Esse metodo somos obrigados a criar (cria automaticamente) quando implementamos 
        o Initializable na Classe em que estamos. Toda tela que ser iniciar e que utilizar esse controlador (DepartamentoFormController) 
        vai executar obrigariamento tudo que estiver dentro do metodo "initialize" (pode ser vazio)*/
        
        initializableNodes(); /*Chamando o metodo abaixo "initializableNodes" que criamos manualmente*/
    }
    
    private void initializableNodes (){ /*Esse metodo apenas têm o proposito de adicionar algumas restrições nos campos do formulario. 
        Para que seja possivel criar essas restricões, temos ter uma classe de restricões (criada manulamente). Nesse caso nossa classe se chama
        "Constraints" e foi criada no pacote "gui.util"*/
    
        Constraints.setTextFieldInteger(txtId); /*Estou dizendo que txtId só vai aceitar numeros inteiros*/   
        Constraints.setTextFieldMaxLength(txtNome, 30);
        Constraints.setTextFieldDouble(txtSalarioBase);   
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(tpDataNascimento, "dd/MM/yyyy"); /*Chamando o metodo formatDatePicker na classe Util do pacote gui.Util que formata
        a data no DataPicket no formulario*/
  
        initializeComboBoxDepartment(); /*Chamando o metodo que inicializa o combobox*/
    }
    
    public void updateFormData () { /*Esse metodo relacioa os campos do formulario com os atributos da classe Aluno*/
    
        if (entidades == null) {
    
            throw new IllegalStateException("Entidade é nulo");
            
        }
        
        /*Estou relacionado os campos do meu formulario com os seus respectivos atributos na classe Aluno */
        
        txtId.setText(String.valueOf(entidades.getId())); /*a caixa de texto trabalha só com string. Estamos convertendo o inteiro do id pra string */
        txtNome.setText(entidades.getNome());
        txtEmail.setText(entidades.getEmail());      
        txtSalarioBase.setText(String.format("%.2f", entidades.getSalarioBase())); /*a caixa de texto trabalha só com string. Estamos convertendo o double do id para string no formato de duas casas decimais */
        
        if(entidades.getDataNascimento() != null){ /*só vamos converter a data para localDate se a data não for nula. Se não da erro*/
        
            
            tpDataNascimento.setValue(LocalDate.ofInstant(entidades.getDataNascimento().toInstant(), ZoneId.systemDefault())); /*Estamos relacionando a variavel tpDataNascimento do tipo 
            DatePicker com a variavel  DataNascimento, l� da classe Vendedor. DatePicker � do tipo LocalDate (ou LocalDateTime). ofInstant � um  metodo que recebe um instante (a data) e o 
            fuso horario ZoneId (systemDefault pega o fuso horario do computador)*/
                  
        }
        
        if(entidades.getDepartamento() == null) { /*Se o departamento do meu departamento for nulo. Ou seja, um vendedor novo que estamos cadastro e n�o t�m departamento*/
        	 
        	comboBoxDepartamento.getSelectionModel().selectFirst(); /*Se a condicional for verdadeira, estamos definindo para que o meu comboBox inicie o no primeiro elemento*/
        	
        }
        
        comboBoxDepartamento.setValue(entidades.getDepartamento()); /*vai preencher o comboBox com a lista de departamentos. Mas isso s� vai funcionar se o departamento n�o for nulo*/
        
    }
    

    private Vendedor getFormeData() { /*Esse metodo pega os dados que estão nas caixinhas do formulario e instancia um Vendedor*/
        
        Vendedor obj = new Vendedor();
        
        ValidationException exception = new ValidationException ("Validando erro"); /*instanciando nossa exceção personalizada
        que trata possiveis erros ao inserir dados dos campos dos formularios*/
        
        obj.setId(Utils.tryParseToInt(txtId.getText())); /*Não vamos verificar nenhum erro no campo de id pq o campo dele
        está programado pra não ser possivel digitar nada e pessar valor vazio. Estamos usando o metodo tryParsetToInt da classe Utils do pacote gui.utils para converter de string para inteiro
        pois os campos txt só trabalham com string. Além disso, está armazenado no meu objeto (vendedor da classe vendedor) o valor inserido no campo txt referente ao id*/
        
        if(txtNome.getText() == null || txtNome.getText().trim().equals("")){ /*Vamos verificar se o campo de nome está vazio.
        Se estiver, não vai lançar a exceção ainda, vai apenas armazenar um erro no meu map lá na classe personalizada
        "Validation.Exception" no pacote "model.exception"*/
            
        /*trim() serve para eliminar os espaços vazio no inicio ou final e se for igual (equals) uma string vazia. Quer dizer
        que meu campo está vazio*/
        
            exception.addError("nome", " O campo não pode ser vazio"); /*o nome do campo deve ser com letra minuscula*/
            
        }
          
        obj.setNome(txtNome.getText()); /*Mesmo que seja vazio, vai setar o valor*/
        
        if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")){ /*Vamos verificar se o campo de email está vazio.
            Se estiver, não vai lançar a exceção ainda, vai apenas armazenar um erro no meu map lá na classe personalizada
            "Validation.Exception" no pacote "model.exception"*/
                
            /*trim() serve para eliminar os espaços vazio no inicio ou final e se for igual (equals) uma string vazia. Quer dizer
            que meu campo está vazio*/
            
                exception.addError("email", " O campo não pode ser vazio"); /*o nome do campo deve ser com letra minuscula*/
                
        }
        
        obj.setEmail(txtEmail.getText()); /*Mesmo que seja vazio, vai setar o valor*/
        
        
        if(tpDataNascimento.getValue() == null) { /*Só vai fazer a instanciação da data abaixo se o campo tpDataNascimento não for null*/
        	
        	exception.addError("dataNascimento", " O campo não pode ser vazio"); /*o nome do campo deve ser com letra minuscula*/
        	
        }
        else {
        	
            Instant instant = Instant.from(tpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault())); /*Estamos armazenando na variavel instant a data de nascimento. O metodo from
            recebe um instant (a data) e o metodo atStartOfDay converte para instant e recebe o fuso horario. ZoneId.systemDefault é o fuso horario do computador do usuario*/
            obj.setDataNascimento(Date.from(instant)); /*inserindo no objeto (vendedor da classe vendedor) a data armazenada no instant. setDataNascimento vai eseperar uma data, por isso usamos
            o from que vai converter o valor armazenado na variavel instant para Date*/
            
        }

        
        if(txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")){ /*Vamos verificar se o campo de salarioBase está vazio.
            Se estiver, não vai lançar a exceção ainda, vai apenas armazenar um erro no meu map lá na classe personalizada
            "Validation.Exception" no pacote "model.exception"*/
        	  
        	exception.addError("salarioBase", " O campo não pode ser vazio"); /*o nome do campo deve ser com letra minuscula*/
        
        }
        
        obj.setSalarioBase(Utils.tryParseToDouble(txtSalarioBase.getText()));/*Estamos usando o metodo tryParsetToInt da classe Utils do pacote gui.utils para converter de string para Double
        pois os campos txt só trabalham com string. Além disso, está armazenando no meu objeto (vendedor da classe vendedor) o valor inserido no campo txt referente ao SalarioBase*/
        
        obj.setDepartamento(comboBoxDepartamento.getValue()); /*Inserindo no meu objeto (vendedor da classe vendedor) o valor selecionado no combobox*/
        
        if (exception.getErros().size() > 0){ /*Caso tenha armazenado no meu map á na classe personalizada
        "Validation.Exception" no pacote "model.exception", um ou mais erros, vai lançar a exceção*/
        
            throw exception;
        
        } /*Se o if falhar, retorna o obj */
        
        return obj;
        
    }

    private void notificandoDataChangeListeners() { /*Vamos emitir os enventos da interface DataChangeListe em todos os listeners*/
        for (DataChangeListener listener : dataChangeListenners){
        
            listener.onDataChanged();
        
        }
    }
    
    
    public void carregarObjetosAssociados () { /*Este medodo vai inserir va variavel do meu comboBox a list de Departamentos*/
    	
    	if(servicoDeDepartamento == null) {
    		
    		throw new IllegalStateException("Servico de departamento estava nulo!");
    		
    	}
    	
    	List<Departamento> list = servicoDeDepartamento.findAll(); /*Armazenando numa lista os dados do departamento. Usando a variavel servicoDeDepartamento que me da acesso aos 
    	metodo do meu VendedorDaoJDBC*/
    	obsList = FXCollections.observableArrayList(list); /**/
    	comboBoxDepartamento.setItems(obsList);
    	
    }
    
    
    private void setErroMessages (Map<String, String> erros){ /*Vamos percorrer o map*/
    
        Set<String> campos = erros.keySet(); /*Estamos armazenando o conjunto criado com os possiveis erros na variavel Campos*/
        
        
        labelErrorNome.setText((campos.contains("nome")?erros.get("nome"):"")); /*Estamos verificando um por um do conjunto (repare que não precisa de um for ou while)
        Caso exista no conjunto algum campo coma a chava nome conforme possivel erro nome acrecentando no metodo acima getFormeData. Estamos escrevendo a msg de erro no label do formulario*/
        
        /*Essa linha de cima faz o mesmo que os ifs e elses abaixo. Poderia substituir todos os ifs e elses de baixo por essa linha, mas deixei assim pra ficar didatico*/
        
        
        if(campos.contains("email")){ /*Estamos verificando um por um do conjunto (repare que não precisa de um for ou while)
        Caso exista no conjunto algum campo coma a chava nome conforme possivel erro nome acrecentando no metodo acima getFormeData*/
        
            labelErrorEmail.setText(erros.get("email")); /*Estamos escrevendo a msg de erro no label do formulario*/
        
        }
        
        else {
        	
        	labelErrorEmail.setText("");
        	
        }
        
        if(campos.contains("salarioBase")){ /*Estamos verificando um por um do conjunto (repare que não precisa de um for ou while)
        Caso exista no conjunto algum campo coma a chava nome conforme possivel erro nome acrecentando no metodo acima getFormeData*/
        
            labelErrorSalarioBase.setText(erros.get("salarioBase")); /*Estamos escrevendo a msg de erro no label do formulario*/
        
        }
        
        else {
        	
        	labelErrorSalarioBase.setText("");
        	
        }
        
        if(campos.contains("dataNascimento")){ /*Estamos verificando um por um do conjunto (repare que não precisa de um for ou while)
        Caso exista no conjunto algum campo coma a chava nome conforme possivel erro nome acrecentando no metodo acima getFormeData*/
        
        	labelErrorDataNascimento.setText(erros.get("dataNascimento")); /*Estamos escrevendo a msg de erro no label do formulario*/
        
        }
        
        else {
        	
        	labelErrorDataNascimento.setText("");
        	
        }
        
        
    }
    
	private void initializeComboBoxDepartment() {/*Este metodo inicializa o combobox. Este codigo peguei pronto*/
		
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};

		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}

