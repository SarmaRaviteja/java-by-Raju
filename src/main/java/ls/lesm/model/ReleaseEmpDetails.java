package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseEmpDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer empId;

	@Enumerated(EnumType.STRING)
	private ReleaseStatus releaseStatus; 

	private LocalDate releaseDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "masterEmployeeDetailsId_fk")
	private MasterEmployeeDetails masterEmployeeDetailsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "masterEmployeeDetailssupervisor_fk")
	private MasterEmployeeDetails masterEmployeeDetailssupervisor;

}
