package Repositories;

import Modal.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryClientService implements ClientRepository {
    private final ArrayList<Client> clientList = new ArrayList<>();

    @Override
    public void Save(Client client){
        clientList.add(client);
    }
    @Override
    public Optional<Client> getClientByEmail(String email){
        return clientList.stream().filter(c -> c.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<Client> getAllClients(){
        return clientList;
    }
}
