package org.mifos.test.acceptance.admin;

import org.mifos.test.acceptance.framework.MifosPage;
import org.mifos.test.acceptance.framework.UiTestCaseBase;
import org.mifos.test.acceptance.framework.admin.FeesCreatePage.SubmitFormParameters;
import org.mifos.test.acceptance.framework.testhelpers.NavigationHelper;
import org.mifos.test.acceptance.util.StringUtil;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:ui-test-context.xml"})
@Test(sequential = true, groups = {"smoke", "fees", "acceptance", "no_db_unit"})
public class DefineAndViewFeesTest extends UiTestCaseBase {

    private FeesHelper feesHelper;

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        super.setUp();
        feesHelper = new FeesHelper(new NavigationHelper(selenium));
    }

    @AfterMethod
    public void logOut() {
        (new MifosPage(selenium)).logout();
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void verifyViewFeesTableContentsTest() throws Exception {
        defineFee("ClientFee", "All Customers");
        defineFee("ProductFee", "Loans");
        feesHelper.viewClientFees("ClientFee", 2, 1);
        feesHelper.viewProductFees("ProductFee", 0, 1);
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    public void createPeriodicFees() throws Exception {
        SubmitFormParameters feeParameters = feesHelper.getFeeParameters(StringUtil.getRandomString(5), "Group", false, SubmitFormParameters.PERIODIC_FEE_FREQUENCY, 6, 6201);
        feeParameters.setFeeFrequencyType(feeParameters.PERIODIC_FEE_FREQUENCY);
        feeParameters.setFeeRecurrenceType(feeParameters.MONTHLY_FEE_RECURRENCE);
        feeParameters.setMonthRecurAfter(2);
        feesHelper.defineFees(feeParameters);
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // one of the dependent methods throws Exception
    public void createOneTimeFees() {
        SubmitFormParameters feeParameters = feesHelper.getFeeParameters(StringUtil.getRandomString(5), "All Customers", false, SubmitFormParameters.ONETIME_FEE_FREQUENCY, 20, 31301);
        feeParameters.setCustomerCharge("Upfront");
        feesHelper.defineFees(feeParameters);
    }

    private void defineFee(String feeName, String categoryType) {
        SubmitFormParameters feeParameters = feesHelper.getFeeParameters(feeName, categoryType, false, SubmitFormParameters.ONETIME_FEE_FREQUENCY, 20, 31301);
        feeParameters.setCustomerCharge("Upfront");
        feesHelper.defineFees(feeParameters);
    }

}
