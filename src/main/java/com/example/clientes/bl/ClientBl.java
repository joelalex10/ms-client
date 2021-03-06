package com.example.clientes.bl;

import com.example.clientes.dto.AddressDto;
import com.example.clientes.dto.ClientDetailsDto;

import com.example.clientes.dto.ClientDto;
import com.example.clientes.entity.Client;
import com.example.clientes.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ClientBl {
    private ClientRepository clientRepository;
    private static Logger LOGGER = LoggerFactory.getLogger(ClientBl.class);

    @Autowired
    public ClientBl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    public List<ClientDto> findAllClients() {
        LOGGER.info("DATABASE: Iniciando consulta para obtener la lista de clientes");
        List<Client> clientList = (List<Client>) this.clientRepository.findAll();
        List<ClientDto> clientDtoList =clientList.stream()
                .filter(
                        data ->  data.getStatus()==1
                ).map(client -> new ClientDto(
                        client.getClientId(),
                        client.getName(),
                        client.getLastname(),
                        client.getEmail(),
                        client.getPhone(),
                        client.getAddressId())
                ).collect(Collectors.toList());

        LOGGER.info("DATABASE-SUCCESS: Consulta exitosa para obtener el listado de clientes {}", clientDtoList);
        return clientDtoList;
    }
    public ClientDto findClientById(Integer clientId){
        Optional<Client> optionalClient= this.clientRepository.findById(clientId);
        if(optionalClient.isPresent()){
            if(optionalClient.get().getStatus()==1){
                ClientDto clientDto = new ClientDto();
                clientDto.setClientId(optionalClient.get().getClientId());
                clientDto.setAddressId(optionalClient.get().getAddressId());
                clientDto.setEmail(optionalClient.get().getEmail());
                clientDto.setName(optionalClient.get().getName());
                clientDto.setLastname(optionalClient.get().getLastname());
                clientDto.setPhone(optionalClient.get().getPhone());
                return clientDto;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    public Client insertNewClient(ClientDto clientDto){
        Client client=new Client();
        client.setName(clientDto.getName());
        client.setLastname(clientDto.getLastname());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddressId(clientDto.getAddressId());
        client.setStatus(1);

        Client newClient =this.clientRepository.save(client);
        return newClient;

    }
    public boolean deleteClient(Integer idClient){

        Optional<Client> optionalClient = this.clientRepository.findById(idClient);
        if(optionalClient.isPresent()){
            Client client = optionalClient.get();
            client.setStatus(0);
            this.clientRepository.save(client);
            return true;
        }else{
            return false;
        }
    }
    public List<ClientDetailsDto>findAllClientDetails(){
        return this.clientRepository.findAllClientsDeatils();
    }

    public Client updateClient(Integer id, ClientDto clientDto) {
        Optional<Client> optionalClient= this.clientRepository.findById(id);
        if(optionalClient.isPresent()){
            Client client=optionalClient.get();
            client.setName(clientDto.getName());
            client.setLastname(clientDto.getLastname());
            client.setPhone(clientDto.getPhone());
            client.setAddressId(clientDto.getAddressId());
            this.clientRepository.save(client);
            return client;
        }else{
            return null;
        }
    }
}
