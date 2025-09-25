package Modal;
import Modal.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client extends Person{
    private String clientId;
    private List<Client> accounts = new ArrayList<>();
    
    public Client(String name , String email, String password) {
        super(name , email, password);
        this.clientId = UUID.randomUUID().toString();
    }

    public String getClientId(){
        return clientId;
    }
    public List<Client> getAccounts(){
        return accounts;
    }
}
