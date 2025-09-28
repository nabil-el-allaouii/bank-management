package Repositories;

import Modal.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InMemoryClientService implements ClientRepository {
    private final ArrayList<Client> clientList = new ArrayList<>();

    @Override
    public void Save(Client client) {
        clientList.add(client);
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientList.stream().filter(c -> c.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<Client> getAllClients() {
        return clientList;
    }

    @Override
    public void updateClientNameById(String clientId, String name) {
        clientList.stream().filter(c -> c.getClientId().equals(clientId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Client not found"))
                .setName(name);
    }

    @Override
    public void updateClientEmailById(String clientId, String email) {
        clientList.stream().filter(c -> c.getClientId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Client not found"))
                .setEmail(email);
    }

    @Override
    public void updateClientPasswordById(String clientId, String password) {
        clientList.stream().filter(c -> c.getClientId().equals(clientId)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Client not found"))
                .setPassword(password);
    }

    @Override
    public void deleteClientById(String clientId){
        Client clientToRemove =  clientList.stream().filter(c -> c.getClientId().equals(clientId)).findFirst().orElseThrow(() -> new NoSuchElementException("Client not found"));
        clientList.remove(clientToRemove);
    }
}
