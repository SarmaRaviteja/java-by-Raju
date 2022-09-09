package ls.lesm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;

public interface DesignationsRepository extends JpaRepository<Designations, Integer> {

	Designations findByDesgNames(String desgNames);
	
	@Query("FROM Designations g where g.designations.id = :id")
	List<Designations> findBySupervisorId(@Param("id")Integer id);

	//Optional<Designations> findDesgId(int id);

	

}
