package Repositories;

import Modal.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    void Save(Client client);
    Optional<Client> getClientByEmail(String email);
    List<Client> getAllClients();
    void updateClientNameById(String clientId , String name);
    void updateClientEmailById(String clientId , String email);
    void updateClientPasswordById(String clientId , String password);
}
