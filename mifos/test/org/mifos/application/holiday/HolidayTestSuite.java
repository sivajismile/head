package org.mifos.application.holiday;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.mifos.application.holiday.business.TestHolidayBO;
import org.mifos.application.holiday.business.service.TestHolidayBusinessService;
import org.mifos.application.holiday.persistence.TestHolidayPersistence;
import org.mifos.application.holiday.struts.action.TestHolidayAction;
import org.mifos.application.holiday.util.helpers.TestHolidayUtils;
import org.mifos.application.holiday.persistence.AddRepaymentRuleTest;

public class HolidayTestSuite extends TestSuite {

	public static void main(String[] args) {
		Test testSuite = suite();
		TestRunner.run(testSuite);
	}

	public static Test suite() {
		TestSuite testSuite = new HolidayTestSuite();
		testSuite.addTestSuite(TestHolidayBO.class);
		testSuite.addTestSuite(TestHolidayBusinessService.class);
		testSuite.addTestSuite(TestHolidayPersistence.class);
		testSuite.addTestSuite(TestHolidayAction.class);
		testSuite.addTestSuite(TestHolidayUtils.class);
		testSuite.addTest(AddRepaymentRuleTest.testSuite());
		
		return testSuite;
	}
}
