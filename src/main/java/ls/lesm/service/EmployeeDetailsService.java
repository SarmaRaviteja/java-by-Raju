package ls.lesm.service;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import ls.lesm.model.Address;
import ls.lesm.model.EmployeePhoto;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.payload.request.EmployeeDetailsRequest;
import ls.lesm.payload.request.EmployeeDetailsUpdateRequest;
import ls.lesm.payload.response.AllEmpCardDetails;
import ls.lesm.payload.response.EmpCorrespondingDetailsResponse;

public interface EmployeeDetailsService {
	//UMER
	Address insertEmpAddress(Address address, Principal principal, Integer addTypeId);
	//UMER
	EmployeeDetailsRequest insetEmpDetails(EmployeeDetailsRequest empReq, Principal principal );
	//UMER
	EmployeesAtClientsDetails insertClientsDetails(EmployeesAtClientsDetails clientDetails, Principal principal);
	//UMER
	Page<EmployeesAtClientsDetails> getAllEmpClinetDetails(PageRequest pageReuquest);
	//UMER
	Page<AllEmpCardDetails> getAllEmpCardDetails(PageRequest pageRequest);
	//UMER
	Page<AllEmpCardDetails> getSortedEmpCardDetailsByDesg(Integer desgId,PageRequest pageRequest);
	//UMER
	EmpCorrespondingDetailsResponse getEmpCorresDetails(EmpCorrespondingDetailsResponse corssDetailsint,int id);
	//UMER
	//EmployeeDetailsRequest updateEmployee(EmployeeDetailsRequest empReq);
	//UMER
	EmployeeDetailsUpdateRequest updateEmployee(EmployeeDetailsUpdateRequest empReq, int id);
	
	//UMER
	EmployeesAtClientsDetails updateEmpClientDetails(EmployeesAtClientsDetails clientDetals, int empId, int newClientId, int clientId);	
	
	

	
	
}
