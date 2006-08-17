/**

* TestClientCustAction.java version: 1.0



* Copyright (c) 2005-2006 Grameen Foundation USA

* 1029 Vermont Avenue, NW, Suite 400, Washington DC 20005

* All rights reserved.



* Apache License
* Copyright (c) 2005-2006 Grameen Foundation USA
*

* Licensed under the Apache License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may obtain
* a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*

* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and limitations under the

* License.
*
* See also http://www.apache.org/licenses/LICENSE-2.0.html for an explanation of the license

* and how it is applied.

*

*/

package org.mifos.application.customer.client.struts.action;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.struts.Globals;
import org.mifos.application.accounts.business.AccountBO;
import org.mifos.application.accounts.loan.business.LoanBO;
import org.mifos.application.customer.business.CustomFieldDefinitionEntity;
import org.mifos.application.customer.business.CustomerBO;
import org.mifos.application.customer.center.business.CenterBO;
import org.mifos.application.customer.client.business.ClientBO;
import org.mifos.application.customer.client.util.helpers.ClientConstants;
import org.mifos.application.customer.group.business.GroupBO;
import org.mifos.application.customer.util.helpers.CustomerConstants;
import org.mifos.application.customer.util.helpers.CustomerStatus;
import org.mifos.application.fees.business.AmountFeeBO;
import org.mifos.application.fees.business.FeeView;
import org.mifos.application.fees.persistence.FeePersistence;
import org.mifos.application.fees.util.helpers.FeeCategory;
import org.mifos.application.meeting.business.MeetingBO;
import org.mifos.application.meeting.util.helpers.MeetingFrequency;
import org.mifos.application.productdefinition.business.LoanOfferingBO;
import org.mifos.application.util.helpers.ActionForwards;
import org.mifos.framework.MifosMockStrutsTestCase;
import org.mifos.framework.components.fieldConfiguration.util.helpers.FieldConfigImplementer;
import org.mifos.framework.components.fieldConfiguration.util.helpers.FieldConfigItf;
import org.mifos.framework.hibernate.helper.HibernateUtil;
import org.mifos.framework.security.util.ActivityContext;
import org.mifos.framework.security.util.UserContext;
import org.mifos.framework.struts.plugin.helper.EntityMasterData;
import org.mifos.framework.util.helpers.Constants;
import org.mifos.framework.util.helpers.ResourceLoader;
import org.mifos.framework.util.helpers.SessionUtils;
import org.mifos.framework.util.helpers.TestObjectFactory;

public class TestClientCustAction extends MifosMockStrutsTestCase{
	private UserContext userContext;
	private CenterBO center;
	private GroupBO group;
	private ClientBO client;
	private MeetingBO meeting;
	private AccountBO accountBO;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		try {
			setServletConfigFile(ResourceLoader.getURI("WEB-INF/web.xml")
					.getPath());
			setConfigFile(ResourceLoader.getURI(
					"org/mifos/framework/util/helpers/struts-config.xml")
					.getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		userContext = new UserContext();
		userContext.setId(new Short("1"));
		userContext.setLocaleId(new Short("1"));
		Set<Short> set = new HashSet<Short>();
		set.add(Short.valueOf("1"));
		userContext.setRoles(set);
		userContext.setLevelId(Short.valueOf("2"));
		userContext.setName("mifos");
		userContext.setPereferedLocale(new Locale("en", "US"));
		userContext.setBranchId(new Short("1"));
		userContext.setBranchGlobalNum("0001");
		request.getSession().setAttribute(Constants.USERCONTEXT, userContext);
		addRequestParameter("recordLoanOfficerId", "1");
		addRequestParameter("recordOfficeId", "1");
		ActivityContext ac = new ActivityContext((short) 0, userContext
				.getBranchId().shortValue(), userContext.getId().shortValue());
		request.getSession(false).setAttribute("ActivityContext", ac);
		request.getSession().setAttribute(Constants.USERCONTEXT, userContext);		EntityMasterData.getInstance().init();
		FieldConfigItf fieldConfigItf=FieldConfigImplementer.getInstance();
		fieldConfigItf.init();		
		FieldConfigImplementer.getInstance();
		getActionServlet().getServletContext().setAttribute(Constants.FIELD_CONFIGURATION,fieldConfigItf.getEntityMandatoryFieldMap());
	}

	@Override
	protected void tearDown() throws Exception {
		TestObjectFactory.cleanUp(accountBO);
		TestObjectFactory.cleanUp(client);
		TestObjectFactory.cleanUp(group);
		TestObjectFactory.cleanUp(center);
		HibernateUtil.closeSession();
		super.tearDown();
	}

	public void testLoad() throws Exception {
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		verifyNoActionErrors();
		verifyNoActionMessages();
		verifyForward(ActionForwards.load_success.toString());
		assertNotNull(SessionUtils.getAttribute(ClientConstants.SALUTATION_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.MARITAL_STATUS_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.CITIZENSHIP_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.BUSINESS_ACTIVITIES_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.EDUCATION_LEVEL_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.GENDER_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.SPOUSE_FATHER_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.HANDICAPPED_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(ClientConstants.ETHINICITY_ENTITY, request.getSession()));
		assertNotNull(SessionUtils.getAttribute(CustomerConstants.CUSTOM_FIELDS_LIST,request.getSession()));
		assertNotNull(SessionUtils.getAttribute(CustomerConstants.FORMEDBY_LOAN_OFFICER_LIST,request.getSession()));
			
	}
	
	public void testFailureNextWithAllValuesNull() throws Exception {
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		verifyNoActionErrors();
		verifyNoActionMessages();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("input", "personalInfo");
		actionPerform();
		assertEquals("Client salutation", 1, getErrrorSize(CustomerConstants.SALUTATION));				
		assertEquals("Client first Name", 1, getErrrorSize(CustomerConstants.FIRST_NAME));				
		assertEquals("Client last Name", 1, getErrrorSize(CustomerConstants.LAST_NAME));
		assertEquals("spouse first Name", 1, getErrrorSize(CustomerConstants.SPOUSE_FIRST_NAME));
		assertEquals("spouse last Name", 1, getErrrorSize(CustomerConstants.SPOUSE_LAST_NAME));
		assertEquals("spouse type", 1, getErrrorSize(CustomerConstants.SPOUSE_TYPE));
		assertEquals("Gender", 1, getErrrorSize(CustomerConstants.GENDER));
		assertEquals("DOB", 1, getErrrorSize(CustomerConstants.DOB));
		verifyInputForward();
	}
	
	public void testFailureNext_WithoutMandatoryCustomField_IfAny() throws Exception{
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		
		List<CustomFieldDefinitionEntity> customFieldDefs = (List<CustomFieldDefinitionEntity>)SessionUtils.getAttribute(CustomerConstants.CUSTOM_FIELDS_LIST, request.getSession());
		boolean isCustomFieldMandatory = false;
		for(CustomFieldDefinitionEntity customFieldDef: customFieldDefs){
			if(customFieldDef.isMandatory()){
				isCustomFieldMandatory = true;
				break;
			}
		}
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("gender", "1");
		addRequestParameter("input", "personalInfo");
		int i = 0;
		for(CustomFieldDefinitionEntity customFieldDef: customFieldDefs){
			addRequestParameter("customField["+ i +"].fieldId", customFieldDef.getFieldId().toString());
			addRequestParameter("customField["+ i +"].fieldValue", "");
			i++;
		}
		actionPerform();
		
		if(isCustomFieldMandatory)
			assertEquals("CustomField", 1, getErrrorSize(CustomerConstants.CUSTOM_FIELD));	
		else
			assertEquals("CustomField", 0, getErrrorSize(CustomerConstants.CUSTOM_FIELD));	
	
	}
	
	public void testNextSuccess() throws Exception{
		
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		
		List<CustomFieldDefinitionEntity> customFieldDefs = (List<CustomFieldDefinitionEntity>)SessionUtils.getAttribute(CustomerConstants.CUSTOM_FIELDS_LIST, request.getSession());
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("clientDetailView.gender", "1");
		addRequestParameter("input", "personalInfo");
		int i = 0;
		for(CustomFieldDefinitionEntity customFieldDef: customFieldDefs){
			addRequestParameter("customField["+ i +"].fieldId", customFieldDef.getFieldId().toString());
			addRequestParameter("customField["+ i +"].fieldValue", "Req");
			i++;
		}
		actionPerform();
		verifyNoActionErrors();
		verifyNoActionMessages();
		verifyForward(ActionForwards.next_success.toString());
	}
	
	public void testPreviewFailureForTrainedDate() throws Exception {
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		List<CustomFieldDefinitionEntity> customFieldDefs = (List<CustomFieldDefinitionEntity>)SessionUtils.getAttribute(CustomerConstants.CUSTOM_FIELDS_LIST, request.getSession());
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("clientDetailView.gender", "1");
		addRequestParameter("input", "personalInfo");
		int i = 0;
		for(CustomFieldDefinitionEntity customFieldDef: customFieldDefs){
			addRequestParameter("customField["+ i +"].fieldId", customFieldDef.getFieldId().toString());
			addRequestParameter("customField["+ i +"].fieldValue", "Req");
			i++;
		}
		actionPerform();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "preview");
		addRequestParameter("formedByPersonnel", "1");
		addRequestParameter("trained", "1");
		addRequestParameter("input", "mfiInfo");
		actionPerform();
		System.out.println("Errors: "+ request.getAttribute(Globals.ERROR_KEY));
		assertEquals(1, getErrrorSize());
		assertEquals("Client Trained date not present", 1, getErrrorSize(ClientConstants.TRAINED_DATE_MANDATORY  ));
		
	}
	
	public void testPreviewFailureFormedByPersonnelNotPresent() throws Exception {
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		List<CustomFieldDefinitionEntity> customFieldDefs = (List<CustomFieldDefinitionEntity>)SessionUtils.getAttribute(CustomerConstants.CUSTOM_FIELDS_LIST, request.getSession());
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("clientDetailView.gender", "1");
		addRequestParameter("input", "personalInfo");
		int i = 0;
		for(CustomFieldDefinitionEntity customFieldDef: customFieldDefs){
			addRequestParameter("customField["+ i +"].fieldId", customFieldDef.getFieldId().toString());
			addRequestParameter("customField["+ i +"].fieldValue", "Req");
			i++;
		}
		actionPerform();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "preview");
		addRequestParameter("input", "mfiInfo");
		actionPerform();
		assertEquals(1, getErrrorSize());
		assertEquals("Client formed by not present", 1, getErrrorSize(CustomerConstants.FORMED_BY_LOANOFFICER  ));
		
	}
	public void testFailurePreview_WithDuplicateFee() throws Exception{
		List<FeeView> feesToRemove = getFees();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("gender", "1");
		addRequestParameter("input", "personalInfo");
		actionPerform();
		List<FeeView> feeList = (List<FeeView>)SessionUtils.getAttribute(CustomerConstants.ADDITIONAL_FEES_LIST, request.getSession());
		FeeView fee = feeList.get(0);
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "preview");	
		addRequestParameter("input", "mfiInfo");
		addRequestParameter("selectedFee[0].feeId", fee.getFeeId());
		addRequestParameter("selectedFee[0].amount", "100");
		addRequestParameter("selectedFee[1].feeId", fee.getFeeId());
		addRequestParameter("selectedFee[1].amount", "150");
		actionPerform();		
		assertEquals("Fee", 1, getErrrorSize(CustomerConstants.FEE));
		removeFees(feesToRemove);
	}
	
	public void testFailurePreview_WithFee_WithoutFeeAmount() throws Exception{
		List<FeeView> feesToRemove = getFees();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("gender", "1");
		addRequestParameter("input", "personalInfo");
		actionPerform();
		List<FeeView> feeList = (List<FeeView>)SessionUtils.getAttribute(CustomerConstants.ADDITIONAL_FEES_LIST, request.getSession());
		FeeView fee = feeList.get(0);
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("input", "mfiInfo");
		addRequestParameter("method", "preview");		
		addRequestParameter("selectedFee[0].feeId", fee.getFeeId());
		addRequestParameter("selectedFee[0].amount", "");
		actionPerform();
		assertEquals("Fee", 1, getErrrorSize(CustomerConstants.FEE));
		removeFees(feesToRemove);
	}
	public void testPreviewSuccess() throws Exception {
		List<FeeView> feesToRemove = getFees();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("officeId", "3");
		addRequestParameter("groupFlag", "0");
		actionPerform();
		List<CustomFieldDefinitionEntity> customFieldDefs = (List<CustomFieldDefinitionEntity>)SessionUtils.getAttribute(CustomerConstants.CUSTOM_FIELDS_LIST, request.getSession());
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "next");
		addRequestParameter("officeId", "3");
		addRequestParameter("clientName.salutation", "1");
		addRequestParameter("clientName.firstName", "Client");
		addRequestParameter("clientName.lastName", "LastName");
		addRequestParameter("spouseName.firstName", "Spouse");
		addRequestParameter("spouseName.lastName", "LastName");
		addRequestParameter("spouseName.nameType", "1");
		addRequestParameter("dateOfBirth", "03/20/2006");
		addRequestParameter("clientDetailView.gender", "1");
		addRequestParameter("input", "personalInfo");
		int i = 0;
		for(CustomFieldDefinitionEntity customFieldDef: customFieldDefs){
			addRequestParameter("customField["+ i +"].fieldId", customFieldDef.getFieldId().toString());
			addRequestParameter("customField["+ i +"].fieldValue", "Req");
			i++;
		}
		actionPerform();
		
		
		List<FeeView> feeList = (List<FeeView>)SessionUtils.getAttribute(CustomerConstants.ADDITIONAL_FEES_LIST, request.getSession());
		FeeView fee = feeList.get(0);
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "preview");	
		addRequestParameter("input", "mfiInfo");
		addRequestParameter("formedByPersonnel", "1");
		addRequestParameter("selectedFee[0].feeId", fee.getFeeId());
		addRequestParameter("selectedFee[0].amount", fee.getAmount());
		actionPerform();
		verifyNoActionErrors();
		verifyNoActionMessages();
		verifyForward(ActionForwards.preview_success.toString());
		removeFees(feesToRemove);
	}
	
	public void testSuccessfulPrevPersonalInfo() throws Exception {
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "prevPersonalInfo");
		actionPerform();
		verifyForward(ActionForwards.prevPersonalInfo_success.toString());
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
	
	public void testSuccessfulPrevMfiInfo() throws Exception {
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "prevMFIInfo");
		actionPerform();
		verifyForward(ActionForwards.prevMFIInfo_success.toString());
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
	
	private List<FeeView> getFees() {
		List<FeeView> fees = new ArrayList<FeeView>();
		AmountFeeBO fee1 = (AmountFeeBO) TestObjectFactory
				.createPeriodicAmountFee("PeriodicAmountFee",
						FeeCategory.CENTER, "200", MeetingFrequency.WEEKLY,
						Short.valueOf("2"));
		fees.add(new FeeView(fee1));
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
		return fees;
	}
	
	private void removeFees(List<FeeView> feesToRemove){
		for(FeeView fee :feesToRemove){
			TestObjectFactory.cleanUp(new FeePersistence().getFee(fee.getFeeIdValue()));
		}
	}
	
	private void createInitialCustomers(){
		meeting = TestObjectFactory.createMeeting(TestObjectFactory.getMeetingHelper(1, 1, 4, 2));
		center = TestObjectFactory.createCenter("Center", Short.valueOf("13"), "1.4", meeting, new Date(System.currentTimeMillis()));
		group = TestObjectFactory.createGroup("group", CustomerStatus.GROUP_ACTIVE.getValue(), center.getSearchId()+".1", center, new Date());
		client = TestObjectFactory.createClient("client",CustomerStatus.CLIENT_ACTIVE.getValue(), group.getSearchId()+".1", group, new Date());
	}
	
	private java.sql.Date offSetCurrentDate(int noOfyears) {
		Calendar currentDateCalendar = new GregorianCalendar();
		int year = currentDateCalendar.get(Calendar.YEAR);
		int month = currentDateCalendar.get(Calendar.MONTH);
		int day = currentDateCalendar.get(Calendar.DAY_OF_MONTH);
		currentDateCalendar = new GregorianCalendar(year-noOfyears, month, day);
		return new java.sql.Date(currentDateCalendar.getTimeInMillis());
	}
	
	private LoanBO getLoanAccount(CustomerBO customer, MeetingBO meeting) {
		Date startDate = new Date(System.currentTimeMillis());
		LoanOfferingBO loanOffering = TestObjectFactory.createLoanOffering(
				"Loan", Short.valueOf("2"), startDate, Short
						.valueOf("1"), 300.0, 1.2, Short.valueOf("3"), Short
						.valueOf("1"), Short.valueOf("1"), Short.valueOf("1"),
				Short.valueOf("1"), Short.valueOf("1"), meeting);
		return TestObjectFactory.createLoanAccount("42423142341", customer, Short
				.valueOf("5"), startDate, loanOffering);

	}
	
	public void testGet(){	
		createInitialCustomers();
		accountBO = getLoanAccount(client,meeting);
		client.setDateOfBirth(offSetCurrentDate(50));
		TestObjectFactory.updateObject(client);	
		HibernateUtil.closeSession();
		setRequestPathInfo("/clientCustAction.do");
		addRequestParameter("method", "get");
		addRequestParameter("globalCustNum", client.getGlobalCustNum());
		actionPerform();	
		verifyForward(ActionForwards.get_success.toString());
		assertEquals("Age of customer should be 50 years",50,SessionUtils.getAttribute(ClientConstants.AGE,request.getSession()));
		//assertEquals("No of active loan accounts should be 1",1,((List<LoanBO>)SessionUtils.getAttribute(ClientConstants.CUSTOMERACTIVELOANACCOUNTS,request.getSession())).size());
		HibernateUtil.closeSession();
		group = (GroupBO) HibernateUtil.getSessionTL().get(GroupBO.class,group.getCustomerId());
		center = (CenterBO) HibernateUtil.getSessionTL().get(CenterBO.class,center.getCustomerId());
		client = (ClientBO)HibernateUtil.getSessionTL().get(ClientBO.class,client.getCustomerId());
		accountBO = (LoanBO) HibernateUtil.getSessionTL().get(LoanBO.class, accountBO.getAccountId());
	}
	
	
	
}
