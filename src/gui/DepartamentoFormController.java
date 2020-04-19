/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exception.ValidationException;
import model.services.ServicoDeDepartamento;

/**
 *
 * @author David
 */
public class DepartamentoFormController implements Initializable {

    private Departamento entidades; /*Essa variavel do tipo aluno é que vai me dar acesso aos metodos sets da classe Departamento. Por exemplo, no metodo
    bem abaixo (updateFormData), vamos precisar relacionar os campos do meu formulario com os seus respectivos atributos na classe Departamento, e por isso
    vamos precisar chamar os metodos gets da classe Departamento*/
    
    private ServicoDeDepartamento servico; /*Precisamos criar esta varial para um dependencia com a classe ServicoDeDepartamento*/
    
    private List<DataChangeListener> dataChangeListenners = new ArrayList <>();
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private Label labelErrorNome;
    
    @FXML
    private Button btSalvar;
    
    @FXML
    private Button btCancelar;
    
    public void setDepartamento(Departamento entidades){ /*Necessario criar o set da variavel entidades*/
    
        this.entidades = entidades;
    
    }
    
    public void setServicoDeDepartamento (ServicoDeDepartamento servico){
    
        this.servico = servico;
    
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
    }
    
    public void updateFormData (){ /*Esse metodo joga nas caixinhas os dados dos atributos correspondentes da classe Departamento. Ao contraio
        do metodo abaixo getFormeData que pega os valores da caixinha e joga na classe departamento*/
    
        if (entidades == null) {
    
            throw new IllegalStateException("Entidade é nulo");
            
        }
        
        /*Estou relacionado os campos do meu formulario com os seus respectivos atributos na classe Aluno */
        
        txtId.setText(String.valueOf(entidades.getId())); /*a caixa de texto trabalha só com string. Estamos convertendo o inteiro do id pra string */
        txtNome.setText(entidades.getNome());
    }

    private Departamento getFormeData() { /*Esse metodo pega os dados que estão nas caixinhas do formulario e instancia um deparamento*/
        
        Departamento obj = new Departamento();
        
        ValidationException exception = new ValidationException ("Validando erro"); /*instanciando nossa exceção personalizada
        que trata possiveis erros ao inserir dados dos campos dos formularios*/
        
        obj.setId(Utils.tryParseToInt(txtId.getText())); /*Não vamos verificar nenhum erro no campo de id pq o campo dele
        está programado pra não ser possivel digitar nada e pessar valor vazio*/
        
        if(txtNome.getText() == null || txtNome.getText().trim().equals("")){ /*Vamos verificar se o campo de nome está vazio.
        Se estiver, não vai lançar a exceção ainda, vai apenas armazenar um erro no meu map lá na classe personalizada
        "Validation.Exception" no pacote "model.exception"*/
            
        /*trim() serve para eliminar os espaços vazio no inicio ou final e se for igual (equals) uma string vazia. Quer dizer
        que meu campo está vazio*/
        
            exception.addError("nome", " O campo não pode ser vazio"); /*o nome do campo deve ser com letra minuscula*/
            
        }
          
        obj.setNome(txtNome.getText()); /*Mesmo que seja vazio, vai setar o valor*/
        
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
    
    private void setErroMessages (Map<String, String> erros){ /*Vamos percorrer o map*/
    
        Set<String> campos = erros.keySet(); /*Estamos armazenando o conjunto criado com os possiveis erros na variavel Campos*/
        
        if(campos.contains("nome")){ /*Estamos verificando um por um do conjunto (repare que não precisa de um for ou while)
        Caso exista no conjunto algum campo coma a chava nome conforme possivel erro nome acrecentando no metodo acima getFormeData*/
        
            labelErrorNome.setText(erros.get("nome")); /*Estamos escrevendo a msg de erro no label do formulario*/
        
        }
        
    }
}
