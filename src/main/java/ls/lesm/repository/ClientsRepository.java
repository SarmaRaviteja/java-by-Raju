package ls.lesm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.Clients;

public interface ClientsRepository extends JpaRepository<Clients, Integer> {

	Clients findByClientsNames(String clientsNames);

}
