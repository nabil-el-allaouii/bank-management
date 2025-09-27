package Services;

import Modal.AccountManager;
import Modal.Person;
import Repositories.ClientRepository;
import Modal.Client;
import Repositories.ManagerRepository;

import java.util.Optional;

public class AuthService {
    private final ClientRepository clientRepo;
    private final ManagerRepository managerRepo;
    public AuthService(ClientRepository clientRepo , ManagerRepository managerRepo) {
        this.clientRepo = clientRepo;
        this.managerRepo = managerRepo;
    }

    public void RegisterClient(String name ,String email , String password){
        Client client = new Client(name , email , password);
        clientRepo.Save(client);
    }

    public void RegisterManager(String name , String email , String password){
        AccountManager manager = new AccountManager(name, email , password);
        managerRepo.Save(manager);
    }

    public Person login(String email , String password){
        Optional<Client> cli = clientRepo.getClientByEmail(email);
        if(cli.isPresent()){
            if(cli.get().getPassword().equals(password)){
                return cli.get();
            }else{
                throw new RuntimeException("Wrong password");
            }
        }
        Optional<AccountManager> man = managerRepo.findByEmail(email);
        if(man.isPresent()){
            if(man.get().getPassword().equals(password)){
                return man.get();
            }else{
                throw new RuntimeException("Wrong password");
            }
        }
        throw new RuntimeException("Login failed: User not Found");
    }

}
