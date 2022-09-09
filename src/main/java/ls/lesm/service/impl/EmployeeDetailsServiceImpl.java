package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ls.lesm.exception.DateMissMatchException;
import ls.lesm.exception.DuplicateEntryException;
import ls.lesm.model.Address;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.payload.request.EmployeeDetailsRequest;
import ls.lesm.payload.request.EmployeeDetailsUpdateRequest;
import ls.lesm.payload.response.AllEmpCardDetails;
import ls.lesm.payload.response.EmpCorrespondingDetailsResponse;
import ls.lesm.payload.response.EmployeeDetailsResponse;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.AddressTypeRepository;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.service.EmployeeDetailsService;

@Service
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

	@Autowired
	private AddressRepositoy addressRepositoy;

	@Autowired
	private AddressTypeRepository addressTypeRepository;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	private DepartmentsRepository departmentsRepository;

	@Autowired
	private SubDepartmentsRepository subDepartmentsRepositorye;

	@Autowired
	private DesignationsRepository designationsRepository;

	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@Autowired
	private InternalExpensesRepository internalExpensesRepository;

	@Autowired
	private SalaryRepository salaryRepository;

	@Autowired
	private ClientsRepository clientsRepository;

	// UMER
	@Override
	public Address insertEmpAddress(Address address, Principal principal, Integer addTypeId) {
		// address.setCreatedAt(LocalDate.now());
		address.setCreatedBy(principal.getName());
		Optional<Object> optional = addressTypeRepository.findById(addTypeId).map(type -> {
			address.setAdressType(type);
			return type;
		});
		return addressRepositoy.save(address);
	}

	// UMER
	public EmployeeDetailsRequest insetEmpDetails(EmployeeDetailsRequest empDetails, Principal principal) {

		empDetails.getMasterEmployeeDetails().setCreatedAt(LocalDate.now());
		empDetails.getMasterEmployeeDetails().setCreatedBy(principal.getName());
		if (empDetails.getMasterEmployeeDetails().getDOB().isAfter(LocalDate.now())) {
			throw new DateMissMatchException("", "Date Of Birth can not be after todays date");
		}
		empDetails.getMasterEmployeeDetails()
				.setLancesoft(empDetails.getMasterEmployeeDetails().getLancesoft().toUpperCase());

		MasterEmployeeDetails emp = this.masterEmployeeDetailsRepository
				.findByLancesoft(empDetails.getMasterEmployeeDetails().getLancesoft());
		if (emp != null) {
			throw new DuplicateEntryException("", "Employee with this employee Id alreday exist in database");
		}
		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository
				.save(empDetails.getMasterEmployeeDetails());

		empDetails.getInternalExpenses().setCreatedAt(LocalDate.now());
		empDetails.getInternalExpenses().setCreatedBy(principal.getName());

		empDetails.getAddress().setCreatedAt(LocalDate.now());
		empDetails.getAddress().setCreatedBy(principal.getName());

		empDetails.getSalary().setCreatedAt(LocalDate.now());
		empDetails.getSalary().setCreatedBy(principal.getName());

		this.masterEmployeeDetailsRepository.findById(employee.getEmpId()).map(id -> {
			empDetails.getSalary().setMasterEmployeeDetails(id);
			return id;
		});

		this.masterEmployeeDetailsRepository.findById(employee.getEmpId()).map(id -> {
			empDetails.getAddress().setMasterEmployeeDetails(id);
			return id;
		});
		this.masterEmployeeDetailsRepository.findById(employee.getEmpId()).map(id -> {
			empDetails.getInternalExpenses().setMasterEmployeeDetails(id);
			return id;
		});

		this.addressRepositoy.save(empDetails.getAddress());
		this.internalExpensesRepository.save(empDetails.getInternalExpenses());
		this.salaryRepository.save(empDetails.getSalary());
		return empDetails;
	}

	// UMER
	@Override
	public EmployeesAtClientsDetails insertClientsDetails(EmployeesAtClientsDetails clientDetails,
			Principal principal) {
		clientDetails.setCreatedBy(principal.getName());
		clientDetails.setCreatedAt(LocalDate.now());

		if (clientDetails.getPOEdate() == null) {
			this.employeesAtClientsDetailsRepository.save(clientDetails);
			return clientDetails;
		}
		if (clientDetails.getPOSdate().isAfter(clientDetails.getPOEdate())) {
			throw new DateMissMatchException("", "Po start date can not be before po end date");
		}

		else {
			this.employeesAtClientsDetailsRepository.save(clientDetails);
			return clientDetails;
		}

	}

	// UMER
	@Override
	public Page<EmployeesAtClientsDetails> getAllEmpClinetDetails(PageRequest pageReuquest) {

		Page<EmployeesAtClientsDetails> list = employeesAtClientsDetailsRepository.findAll(pageReuquest);
		return list;

	}

	// UMER
	@Override
	public Page<AllEmpCardDetails> getAllEmpCardDetails(PageRequest pageRequest) {
		Page<AllEmpCardDetails> page = this.masterEmployeeDetailsRepository.getAlEmpCardDetails(pageRequest);
		return page;
	}

	// UMER
	@Override
	public Page<AllEmpCardDetails> getSortedEmpCardDetailsByDesg(Integer desgId, PageRequest pageRequest) {
		Page<AllEmpCardDetails> page = this.masterEmployeeDetailsRepository.getSortedEmpCardDetailsByDesg(desgId,
				pageRequest);
		return page;
	}

	// UMER
	@Override
	public EmpCorrespondingDetailsResponse getEmpCorresDetails(EmpCorrespondingDetailsResponse corssDetails,
			int empid) {
		Optional<InternalExpenses> data = this.internalExpensesRepository.findBymasterEmployeeDetails_Id(empid);
		if (data.isPresent())
			corssDetails.setInternalExpenses(data.get());// .setBenchTenure(data.get().getBenchTenure())

		Optional<Salary> data3 = this.salaryRepository.findBymasterEmployeeDetails_Id(empid);
		if (data3.isPresent())
			corssDetails.setSalary(data3.get());// .setSalary(data3.get().getSalary());

		EmployeeDetailsResponse data4 = this.masterEmployeeDetailsRepository.getEmpDetailsById(empid);
		corssDetails.setEmployeeDetailsResponse(data4);
		return corssDetails;
	}

	// UMER
	@Transactional
	@Override
	public EmployeeDetailsUpdateRequest updateEmployee(EmployeeDetailsUpdateRequest empReq, int id) {
		Optional<MasterEmployeeDetails> employee = this.masterEmployeeDetailsRepository.findById(id);
		if (employee.isPresent()) {
			employee.get().setFirstName(empReq.getMasterEmployeeDetails().getFirstName());
			employee.get().setLastName(empReq.getMasterEmployeeDetails().getLastName());

			employee.get().setDOB(empReq.getMasterEmployeeDetails().getDOB());

			employee.get().setEmail(empReq.getMasterEmployeeDetails().getEmail());
			employee.get().setJoiningDate(empReq.getMasterEmployeeDetails().getJoiningDate());
			employee.get().setLancesoft(empReq.getMasterEmployeeDetails().getLancesoft());
			employee.get().setLocation(empReq.getMasterEmployeeDetails().getLocation());
			employee.get().setPhoneNo(empReq.getMasterEmployeeDetails().getPhoneNo());

		}
		if (empReq.getMasterEmployeeDetails().getDOB().isAfter(LocalDate.now())) {
			throw new DateMissMatchException("", "Date Of Birth can not be after todays date");
		}
		MasterEmployeeDetails emp = this.masterEmployeeDetailsRepository
				.findByLancesoft(empReq.getMasterEmployeeDetails().getLancesoft());
		if (emp.getLancesoft() != empReq.getMasterEmployeeDetails().getLancesoft() && emp != null) {
			throw new DuplicateEntryException("", "Employee with this employee Id alreday exist in database");
		} else {
			MasterEmployeeDetails updatedEmployee = this.masterEmployeeDetailsRepository.save(employee.get());
		}
		List<Address> empAddress = this.addressRepositoy.findByEmpIdFk(id);
		ArrayList<Address> addList = new ArrayList<Address>();

		for (Address add : empAddress) {
			add.setCity(empReq.getAddress().getCity());
			add.setCountry(empReq.getAddress().getCountry());
			add.setZipCod(empReq.getAddress().getZipCod());
			add.setState(empReq.getAddress().getState());
			add.setStreet(empReq.getAddress().getStreet());
			addList.add(add);
		}
		List<Address> UpdateEmpAddress = this.addressRepositoy.saveAll(addList);
		Optional<InternalExpenses> exp = this.internalExpensesRepository.findByEmployeeById(id);
		exp.get().setCubicleCost(empReq.getInternalExpenses().getCubicleCost());
		exp.get().setFoodCost(empReq.getInternalExpenses().getFoodCost());
		exp.get().setTransportationCost(empReq.getInternalExpenses().getTransportationCost());

		InternalExpenses UpdatedExp = this.internalExpensesRepository.save(exp.get());

		// empReq.setAddress(UpdateEmpAddress);
		empReq.setInternalExpenses(UpdatedExp);

		return empReq;
	}

	// UMER
	@Override
	public EmployeesAtClientsDetails updateEmpClientDetails(EmployeesAtClientsDetails clientDetals, int empId,
			int newClientId, int clientId) {
		List<EmployeesAtClientsDetails> empClientDtails = this.employeesAtClientsDetailsRepository
				.findsBymasterEmployeeDetails_Id(empId);
		// Optional<EmployeesAtClientsDetails>
		// optEmpClientDetial=this.employeesAtClientsDetailsRepository.findByClientId(clientId);
		Optional<EmployeesAtClientsDetails> optEmpClientDetial = this.employeesAtClientsDetailsRepository
				.findById(clientId);

		optEmpClientDetial.get().setClientEmail(clientDetals.getClientEmail());
		optEmpClientDetial.get().setClientEmail(clientDetals.getClientManagerName());
		optEmpClientDetial.get().setClientSalary(clientDetals.getClientSalary());
		optEmpClientDetial.get().setDesgAtClient(clientDetals.getDesgAtClient());
		optEmpClientDetial.get().setPOEdate(clientDetals.getPOEdate());
		optEmpClientDetial.get().setPOSdate(clientDetals.getPOSdate());
		this.clientsRepository.findById(newClientId).map(id -> {
			optEmpClientDetial.get().setClients(id);
			return id;
		});
//		if(optEmpClientDetial.get().getPOSdate().isBefore(LocalDate.now()) && optEmpClientDetial.get().getPOEdate().isAfter(LocalDate.now()) && optEmpClientDetial.get().getPOEdate()==null)
		this.employeesAtClientsDetailsRepository.save(optEmpClientDetial.get());
//		else 
//			throw new DateMissMatchException("","The employee completed his/her tenure for this client "+optEmpClientDetial.get().getClients().getClientsNames()+
//					" on "+optEmpClientDetial.get().getPOEdate());

		return optEmpClientDetial.get();
	}

	
	
	

}
