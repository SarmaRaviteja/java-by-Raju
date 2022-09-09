package ls.lesm.payload.request;

import java.util.List;

import lombok.Data;
import ls.lesm.model.Address;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
@Data
//UMER
public class EmployeeDetailsUpdateRequest {
	
	private MasterEmployeeDetails masterEmployeeDetails;
	private Address address;
	private InternalExpenses internalExpenses;
	

}
