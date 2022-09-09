package  ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InternalExpenses {
	
	public InternalExpenses(MasterEmployeeDetails masterEmployeeDetails) {
		super();
		this.masterEmployeeDetails = masterEmployeeDetails;
	}
	@Id
	@GeneratedValue(generator = "int_exp_gen",strategy = GenerationType.AUTO)
	private Integer internalExpId;
	private Double foodCost;    // in month
	private Double cubicleCost;  //in months
	private Double transportationCost;//in months
	private long benchTenure;// PO end date to sysdate or po start date
	private Double totalSalPaidTillNow;// salary*bechTenure
	private Double totalExpenses;// (salary+foodCost+cubicleCost+transportationCost)*benchTenure=totalExpenses
	private Double profitOrLoss;// totalExpenses-totalEarningAtClients from (EmployeesAtClientsDetails table) if that expense from is internal will add that
	
	
	private LocalDate createdAt;//timpStamp
	
	
	@Column(length=30)
	private String createdBy;//principal
	@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	
	
	
	
	
	
}
