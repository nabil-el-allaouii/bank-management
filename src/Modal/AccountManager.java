package Modal;

import java.util.UUID;

public class AccountManager extends Person{
    private String managerId;

    public AccountManager(String name,String email, String password){
        super(name,email,password);
        this.managerId = UUID.randomUUID().toString();
    }

    public String getManagerId() {
        return managerId;
    }
}
