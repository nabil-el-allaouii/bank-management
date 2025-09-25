package Services;

import Repositories.ClientRepository;
import Modal.Client;

import java.util.Optional;

public class AuthService {
    private ClientRepository clientRepo;
    public AuthService(ClientRepository clientRepo){
        this.clientRepo = clientRepo;
    }

    public void RegisterClient(String name ,String email , String password){
        Client client = new Client(name , email , password);
        clientRepo.Save(client);
    }

    public Client login(String email , String password){
        Optional<Client> cli = clientRepo.getClientByEmail(email);
        if(cli.isPresent()){
            if(cli.get().getPassword().equals(password)){
                return cli.get();
            }
        }
        return null;
    }
}
