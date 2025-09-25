package Modal;

import java.util.ArrayList;
import java.util.List;

public class AccountManager extends Person{
    private String managerId;
    private List<Client> clientList = new ArrayList<>();

    public AccountManager(String name,String email, String password){
        super(name,email,password);
    }

    public List<Client> getClientList(){
        return clientList;
    }
}
