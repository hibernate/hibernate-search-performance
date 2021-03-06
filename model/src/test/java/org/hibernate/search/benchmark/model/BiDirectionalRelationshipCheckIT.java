package org.hibernate.search.benchmark.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.benchmark.model.application.DomainDataInitializer;
import org.hibernate.search.benchmark.model.application.HibernateORMHelper;
import org.hibernate.search.benchmark.model.application.ModelService;
import org.hibernate.search.benchmark.model.entity.Employee;
import org.hibernate.search.benchmark.model.param.RelationshipSize;
import org.hibernate.search.benchmark.model.service.EmployeeRepository;

import org.junit.jupiter.api.Test;

public class BiDirectionalRelationshipCheckIT {

	@Test
	public void test() {
		try ( SessionFactory sessionFactory = HibernateORMHelper.buildSessionFactory( new Properties() ) ) {
			ModelService modelService = new NoIndexingModelService();
			new DomainDataInitializer( modelService, sessionFactory, RelationshipSize.MEDIUM ).initAllCompanyData( 0 );

			try ( Session session = sessionFactory.openSession() ) {
				EmployeeRepository repository = new EmployeeRepository( session );

				List<Employee> employees = repository.getEmployees( 0 );
				assertThat( employees ).hasSize( RelationshipSize.MEDIUM.getEmployeesPerBusinessUnit() );

				Employee employee = employees.get( 0 );

				// verify that employee#performanceSummaries is filled
				assertThat( employee.getPerformanceSummaries() ).hasSize(
						RelationshipSize.MEDIUM.getQuestionnaireDefinitionsForCompany() );

				// verify that employee#questionnaires is filled
				assertThat( employee.getQuestionnaires() ).hasSize(
						// 6 is the number of questions per questionnaire for MEDIUM relationship size
						6 * RelationshipSize.MEDIUM.getQuestionnaireDefinitionsForCompany() );
			}
		}
	}

}
