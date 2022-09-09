package ls.lesm.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.EmployeeStatus;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;

import ls.lesm.model.Salary;
import ls.lesm.model.exp.TotalFinanceExpenses;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.OnsiteBillExpensesRepository;
import ls.lesm.repository.OnsiteExpensesTypeRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.expRepo.TotalFinanceExpensesRepo;

@Service
public class BusinessCalculation {

	@Autowired
	private EmployeesAtClientsDetailsRepository clientDetail;

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepo;

	@Autowired
	ReleaseEmpDetailsRepository releaseEmpDetailsRepository;

	@Autowired
	TotalFinanceExpensesRepo totalFinanceExpensesRepository;

	@Autowired
	InternalExpensesRepository internalExpenseRepo;

	@Autowired
	private SalaryRepository salaryRepo;

	@Autowired
	private SubDepartmentsRepository sdr;

	// double Total_expenses = 0l;// u

//	long Total_internal_tenure;
//	Double Total_internal_pay = 0.0;
//	Double totalFinanceExp = 0.0;

	public double Employee_cal(int employeeId, LocalDate fromDate, LocalDate toDate) {

		double profit_or_los = 0.0;
		long Total_internal_tenure = 0l;
		double Total_expenses = 0l;// u
		long tenure = 0l;
		Double Total_internal_pay = 0.0;
		Double totalFinanceExp = 0.0;
		double paid_till_now = 0l;

		int inc = 0;

		Integer Total_client_tenure[] = { 0 };// u
		double Total_salary_from_client[] = { 0l };// u

		try {
			if (fromDate == null && toDate == null) {
				throw new NullPointerException(" No values present, default will be 12");
			}

		} catch (Exception e) {
			toDate = LocalDate.now();

			int year = toDate.getYear() - 1;
			int month = toDate.getMonthValue();
			int day = toDate.getDayOfMonth();

			fromDate = LocalDate.of(year, month, day);

			System.out.println(ChronoUnit.MONTHS.between(fromDate, toDate));

		}

		MasterEmployeeDetails MasterEmployee = masterEmployeeDetailsRepo.findById(employeeId).get();

		List<Salary> salary = salaryRepo.findsBymasterEmployeeDetails_Id(employeeId);

		System.out.println(salary);

		int salaryRecords = salary.size();

		System.out.println(salaryRecords);

		if (salaryRecords == 1)// number_of_salary_records_of_single_employee==1
		{
			System.out.println("\n\n\n\n\n salary record is one  \n\n\n\n\n");
			System.out.println("\n\n\nprofit" + profit_or_los);

			LocalDate Date1 = MasterEmployee.getJoiningDate();
			LocalDate Date2 = null;

			if (MasterEmployee.getStatus() != EmployeeStatus.EXIT)

			{

				Date2 = LocalDate.now();

			}

			else {

				Date2 = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(employeeId).get().getReleaseDate();

				inc++;

				// Date2 = releaseDate;

			}

			// method

			if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

				if (Date1.isBefore(fromDate)) {

					Date1 = fromDate;

				}

				if (Date2.isAfter(toDate)) {

					Date2 = toDate;

				}

				tenure = ChronoUnit.MONTHS.between(Date1, Date2);

				for (Salary s : salary) {

					///////////////////////////////////////////

					if (inc == 1) {

						int v1 = Date1.getDayOfMonth();
						int v2 = Date2.getDayOfMonth();
						int v3 = 0;

						if (v2 > v1) {

							v3 = v2 - v1;

						}

						else if (v1 > v2) {

							int tempV = 30 - v1;

							v3 = tempV + v2;

						}

						double perday = s.getSalary() / 30;

						System.out.println(Date1.getDayOfMonth() + "  " + Date2.getDayOfMonth() + "  remainingdays="
								+ v3 + " remaining Amount=" + perday * v3);

						paid_till_now = tenure * s.getSalary() + perday * v3;

					}

					else {

						paid_till_now = tenure * s.getSalary();
					}

					System.out.println("tenure1" + tenure + "salary" + s.getSalary() + "paidTillNow" + paid_till_now
							+ " fromdate=" + fromDate + "   todate=" + toDate + "   startdate=" + Date1 + "   todate="
							+ Date2);

					Optional<InternalExpenses> inten = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId);

					InternalExpenses expenses = null;

					if (!inten.isPresent()) {

						System.out.println("Am creating the record");
						// new InternalExpenses(MasterEmployee);

						internalExpenseRepo.save(new InternalExpenses(MasterEmployee));

						expenses = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId).get();

					} else {
						expenses = inten.get();

					}

					expenses.setTotalSalPaidTillNow(paid_till_now);

					clientCalculation(employeeId, fromDate, toDate, Total_client_tenure, Total_salary_from_client);

					long Bench_tenure = tenure - Total_client_tenure[0];

					expenses.setBenchTenure(Bench_tenure);

					List<TotalFinanceExpenses> totalFinanceExpenses = totalFinanceExpensesRepository
							.findBymasterEmployeeDetails_Id(employeeId);

					if (!totalFinanceExpenses.isEmpty()) {

						for (TotalFinanceExpenses tf : totalFinanceExpenses) {

							totalFinanceExp += tf.getTotal();

						}
					}

					profit_or_los = Total_salary_from_client[0] - (paid_till_now + totalFinanceExp);

					expenses.setTotalExpenses(paid_till_now);
					expenses.setProfitOrLoss(profit_or_los);

					internalExpenseRepo.save(expenses);

					System.out.println("  tenure=" + tenure + "    paidTillNow=" + paid_till_now + " fromdate="
							+ fromDate + "   todate=" + toDate + "   startdate1=" + Date1 + "    todate2="
							+ LocalDate.now() + "  profit======" + profit_or_los);

					break;

				}

			}

		}

		else {

			System.out.println("\n\n\nprofit" + profit_or_los);

			LocalDate Date1 = null;
			LocalDate Date2 = null;
			long i = 0;
			Double tempsal = 0.0;

			for (Salary record : salary) {
				if (Date1 == null) {

					Date1 = MasterEmployee.getJoiningDate();

				}

				Date2 = record.getUpdatedAt();

				Double sal = record.getSalary();

				if (i == 0) {
					tempsal = sal;
					i++;
					continue;
				}

				if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

					if (Date1.isBefore(fromDate)) {

						Date1 = fromDate;

					}

					if (Date2.isAfter(toDate)) {

						Date2 = toDate;

					}

					tenure = ChronoUnit.MONTHS.between(Date1, Date2);

					Total_internal_tenure += tenure;

					paid_till_now = tenure * tempsal;

					Total_internal_pay += paid_till_now;

					System.out.println("  tenure=" + tenure + "    salary=" + tempsal + "    paidTillNow="
							+ paid_till_now + " fromdate=" + fromDate + "   todate=" + toDate + "    startdate=" + Date1
							+ " todate=" + Date2);

				}

				Date1 = Date2;
				tempsal = record.getSalary();
			}

			// tenure=Date1 to till date;

			if (MasterEmployee.getStatus() != EmployeeStatus.EXIT)

			{

				Date2 = LocalDate.now();

			}

			else {

				Date2 = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(employeeId).get().getReleaseDate();
				inc++;

				// Date2 = releaseDate;

			}

			if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

				if (Date1.isBefore(fromDate)) {

					Date1 = fromDate;

				}

				if (Date2.isAfter(toDate)) {

					Date2 = toDate;

				}

				tenure = ChronoUnit.MONTHS.between(Date1, Date2);

				Total_internal_tenure += tenure;

				if (inc == 1) {

					int v1 = Date1.getDayOfMonth();
					int v2 = Date2.getDayOfMonth();
					int v3 = 0;

					if (v2 > v1) {

						v3 = v2 - v1;

					}

					else if (v1 > v2) {

						int tempV = 30 - v1;

						v3 = tempV + v2;

					}

					double perday = tempsal / 30;

					paid_till_now = tenure * tempsal + v3 * perday;

				} else {

					paid_till_now = tenure * tempsal;

				}

				Total_internal_pay += paid_till_now;

				Optional<InternalExpenses> inten = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId);

				InternalExpenses expenses = null;

				if (!inten.isPresent()) {

					System.out.println("Am creating the record");
					// new InternalExpenses(MasterEmployee);
					internalExpenseRepo.save(new InternalExpenses(MasterEmployee));
					expenses = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId).get();

				} else {
					expenses = inten.get();

				}

				expenses.setTotalSalPaidTillNow(Total_internal_pay);

				clientCalculation(employeeId, fromDate, toDate, Total_client_tenure, Total_salary_from_client);

				long Bench_tenure = tenure - Total_client_tenure[0];

				expenses.setBenchTenure(Bench_tenure);

				List<TotalFinanceExpenses> totalFinanceExpenses = totalFinanceExpensesRepository
						.findBymasterEmployeeDetails_Id(employeeId);

				for (TotalFinanceExpenses tf : totalFinanceExpenses) {

					totalFinanceExp += tf.getTotal();

				}

				profit_or_los = Total_salary_from_client[0] - (Total_internal_pay + totalFinanceExp);
				expenses.setTotalExpenses(Total_internal_pay);
				expenses.setProfitOrLoss(profit_or_los);

				internalExpenseRepo.save(expenses);

				System.out.println("  tenure=" + tenure + "    salary=" + tempsal + "    paidTillNow=" + paid_till_now
						+ " fromdate=" + fromDate + "   todate=" + toDate + "   startdate1=" + Date1 + "    todate2="
						+ LocalDate.now() + "  profit======" + profit_or_los);

			}

		}

		return profit_or_los;

	}

	public void clientCalculation(int employeeId, LocalDate fromDate, LocalDate toDate, Integer Total_client_tenure[],
			double Total_salary_from_client[]) {

//		Integer Total_client_tenure[] = 0;// u
//		double Total_salary_from_client = 0l;// u

		List<EmployeesAtClientsDetails> slidet = clientDetail.findsBymasterEmployeeDetails_Id(employeeId);// u

		if (!slidet.isEmpty())

		{

			for (EmployeesAtClientsDetails cl : slidet) {

				LocalDate PoSdate = cl.getPOSdate();

				LocalDate PoEdate = cl.getPOEdate();
				LocalDate systemdate = LocalDate.now();
				try {
					if (PoEdate == null || systemdate.isBefore(PoEdate)) {

						PoEdate = LocalDate.now();

					}
				}

				catch (Exception e) {
					PoEdate = LocalDate.now();
					//

				}

				if (PoSdate.isBefore(toDate) && PoEdate.isAfter(fromDate)) {

					if (PoSdate.isBefore(fromDate)) {

						PoSdate = fromDate;

					}

					if (PoEdate.isAfter(toDate)) {

						PoEdate = toDate;

					}

					Period monthsAtClient = Period.between(PoSdate, PoEdate);

					Integer client_tenure = (monthsAtClient.getYears() * 12 + monthsAtClient.getMonths());

					System.out.println(client_tenure);

					double Bill_at_client = cl.getClientSalary() * client_tenure;
					cl.setTotalEarningAtclient(Bill_at_client);

					clientDetail.save(cl);

					Total_client_tenure[0] += client_tenure;

					Double salary = cl.getClientSalary();

					int v1 = PoSdate.getDayOfMonth();
					int v2 = PoEdate.getDayOfMonth();
					int v3 = 0;

					if (v2 > v1) {

						v3 = v2 - v1;

					}

					else if (v1 > v2) {

						int tempV = 30 - v1;

						v3 = tempV + v2;

					}

					double perday = salary / 30;

					System.out.println(PoSdate.getDayOfMonth() + "  " + PoEdate.getDayOfMonth() + "  remainingdays="
							+ v3 + " remaining Amount=" + perday * v3);

					Total_salary_from_client[0] = Total_salary_from_client[0] + (cl.getClientSalary() * client_tenure)
							+ perday * v3;

				}

			}
		}
	}
}
