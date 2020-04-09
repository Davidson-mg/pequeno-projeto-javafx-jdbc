
package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{ /*Essa é uma exceção personalizada para validar um formulario
    Ela vai carregar as msgs de erro dos campos do formulario caso exista*/
    
    private Map<String, String> erros = new HashMap<>(); /*Esse atributo do tipo map vai armazenar os erros.
    Lembrando que o map salva uma coleção de pares chave e o valor, é semelhante uma list só que valores pares. Ou seja, 
    a primeira string entre <> vai ser a chave e a segunda o valor. Para cada campo do formulario vai funcionar mais ou menos assim: para o campo nome (chave) o erro é tal (valor)*/
    
    public ValidationException (String msg){ /*Estamos forçando a instanciação da exceção com string */
    
        super(msg); /*passando pra super classe (RuntimeException) o msg*/
    
    }

    public Map<String, String> getErros() { /*metodo get para acessar o valor da variavel erros acima*/
        return erros;
    }
    
    public void addError (String nomeCampo, String msgErro){ /*Esse metodo vai adicionar a msg de erro no meu map*/
    
        erros.put(nomeCampo, msgErro); /*Lembrando que o put é parecido com o add do list. Ele vai adicionar o nome do campo
        e sua msg de erro*/
    
    }
    
}
