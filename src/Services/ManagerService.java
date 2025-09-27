package Services;

import Modal.Client;
import Repositories.ClientRepository;

import java.util.List;

public class ManagerService {
    private final ClientRepository clientRepo;

    public ManagerService(ClientRepository clientRepo){
        this.clientRepo = clientRepo;
    }

    public void updateClientNameById(String clientId , String name){
        clientRepo.updateClientNameById(clientId,name);
    }
    public void updateClientEmailById(String clientId , String email){
        clientRepo.updateClientEmailById(clientId,email);
    }

    public void updateClientPasswordById(String clientId , String password){
        clientRepo.updateClientPasswordById(clientId,password);
    }
    public List<Client> getAllClients(){
        return clientRepo.getAllClients();
    }
}
