[#ftl]
[#--
* Copyright (c) 2005-2011 Grameen Foundation USA
*  All rights reserved.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
*  implied. See the License for the specific language governing
*  permissions and limitations under the License.
*
*  See also http://www.apache.org/licenses/LICENSE-2.0.html for an
*  explanation of the license and how it is applied.
--]
[@layout.header "mifos" /]
[#assign mifostag=JspTaglibs["/tags/mifos-html"]]

[@widget.topNavigationNoSecurityMobile currentTab="ClientsAndAccounts" /]
<span id="page.id" title="MainSearchResults"></span>
<div class="content">
	<div class="error">
		<span class="fontnormalRedBold"></span>
	</div>
	<div class="fontnormal">
		<form method="POST" action="searchResult.ftl">
			[@form.showAllErrors "customerSearch.*"/]
			[@spring.message "CustomerSearch.searchFor" /]:
			[@spring.bind "customerSearch.searchString" /]
			<input type="text" name="${spring.status.expression}" value="${spring.status.value?if_exists}" maxlength="200"/>
			[@form.singleSelectWithNested "customerSearch.officeId", customerSearch.offices ]
				<option value="0">
					[@spring.message "CustomerSearch.all" /]&nbsp; 
					[@spring.message "${ConfigurationConstants.BRANCHOFFICE}" /]
					[@spring.message "CustomerSearch.s" /]
				</option>
			[/@form.singleSelectWithNested]
			<input type="submit" value="Search" id="home.button.search" name="searchButton" class="buttn"/>
		</form>
	</div>
	[#if customerHierarchy?has_content]
	<div style="width: 325px">
		[#assign number = pageSize*currentPage /]
		<div class="blueline">
		</div>
		<div>
			<span class="headingorange">
				${customerHierarchy.size} [@spring.message "Customer.resultsFor" /] 
			</span>
			<span class="heading">
				${customerSearch.searchString}
			</span>
		</div>
		[#list customerHierarchy.clients as client]
		<div>
			<div>
				[#assign number = number + 1 /]
				${number}.
				[@spring.message "${ConfigurationConstants.CLIENT}" /]
				<span class="headingblue">
					<a href="viewClientDetails.ftl?globalCustNum=${client.clientGlobalCustNum}">
						${client.clientName}: ${client.clientGlobalCustNum}
					</a>
				</span>
			</div>
			<div>
				<span class="fontnormalbold">
					[@spring.message "accounts.status" /]
				</span>
				<span>
					[@mifostag.MifosImage id="${client.customerStatusId}" moduleName="org.mifos.customers.util.resources.customerImages" /]
				</span>
				<span class="fontnormalbold">
					[@spring.message "client.LoanOff" /]
				</span>
				<span>
					${client.loanOfficerName}
				</span>
			</div>
			[#if client.savingsGlobalAccountNum?has_content]
			<div>
				<span class="fontnormalbold">
					[@spring.message "${ConfigurationConstants.SAVINGS}.Savings" /]:
				</span>
				<span>
				[#list client.savingsGlobalAccountNum as saving]
					<a href="savingsAction.do?globalAccountNum=${saving}&method=get&recordOfficeId=${client.branchId}&recordLoanOfficerId=${client.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${saving}
					</a>
				[/#list]
				</span>
			</div>
			[/#if]
			[#if client.loanGlobalAccountNum?has_content]
			<div>
				<span class="fontnormalbold">
					[@spring.message "${ConfigurationConstants.LOAN}" /]:
				</span>
				<span>
				[#list client.loanGlobalAccountNum as loan]
					<a href="loanAccountAction.do?globalAccountNum=${loan}&method=get&recordOfficeId=${client.branchId}&recordLoanOfficerId=${client.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${loan}
					</a>
				[/#list]
				</span>
			</div>
			[/#if]
			<div>
				<span>
					[@spring.message "alsosee" /]:
				</span>
				<span>
					<a href="custSearchAction.do?method=getOfficeHomePage&officeId=${client.branchId}&randomNUm=${Session.randomNUm}">
						${client.branchName}
					</a>
					[#if client.centerGlobalCustNum?has_content]
					 /
					<a href="viewCenterDetails.ftl?globalCustNum=${client.centerGlobalCustNum}&recordOfficeId=${client.branchId}&recordLoanOfficerId=${client.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${client.centerName}
					</a>
					[/#if]
					[#if client.groupGlobalCustNum?has_content]
					 /
					<a href="viewGroupDetails.ftl?globalCustNum=${client.groupGlobalCustNum}&recordOfficeId=${client.branchId}&recordLoanOfficerId=${client.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${client.groupName}
					</a>
					[/#if]
				</span>
			</di>
		</div>
		<div>
			<img src="pages/framework/images/trans.gif" width="10" height="10">
		</div>
		[/#list]
		[#list customerHierarchy.groups as group]
		<div>
			<div>
				[#assign number = number + 1 /]
				${number}.
				[@spring.message "${ConfigurationConstants.GROUP}" /]
				<span class="headingblue">
					<a href="viewGroupDetails.ftl?globalCustNum=${group.groupGlobalCustNum}&recordOfficeId=${group.branchId}&recordLoanOfficerId=${group.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${group.groupName}: ${group.groupGlobalCustNum}
					</a>
				</span>
			</div>
			<div>
				<span class="fontnormalbold">
					[@spring.message "accounts.status" /]
				</span>
				<span>
					[@mifostag.MifosImage id="${group.customerStatusId}" moduleName="org.mifos.customers.util.resources.customerImages" /]
				</span>
				<span class="fontnormalbold">
					[@spring.message "client.LoanOff" /]
				</span>
				<span>
					${group.loanOfficerName}
				</span>
			</div>
			[#if group.savingsGlobalAccountNum?has_content]
			<div>
				<span class="fontnormalbold">
					[@spring.message "${ConfigurationConstants.SAVINGS}.Savings" /]:
				</span>
				<span>
				[#list group.savingsGlobalAccountNum as saving]
					<a href="savingsAction.do?globalAccountNum=${saving}&method=get&recordOfficeId=${group.branchId}&recordLoanOfficerId=${group.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${saving}
					</a>
				[/#list]
				</span>
			</div>
			[/#if]
			[#if group.loanGlobalAccountNum?has_content]
			<div>
				<span class="fontnormalbold">
					[@spring.message "${ConfigurationConstants.LOAN}" /]:
				</span>
				<span>
				[#list group.loanGlobalAccountNum as loan]
					<a href="loanAccountAction.do?globalAccountNum=${loan}&method=get&recordOfficeId=${group.branchId}&recordLoanOfficerId=${group.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${loan}
					</a>
				[/#list]
				</span>
			</div>
			[/#if]
			<div>
				<span>
					[@spring.message "alsosee" /]:
				</span>
				<span>
					<a href="custSearchAction.do?method=getOfficeHomePage&officeId=${group.branchId}&randomNUm=${Session.randomNUm}">
						${group.branchName}
					</a> 
					[#if group.centerGlobalCustNum?has_content]
					 /
					<a href="viewCenterDetails.ftl?globalCustNum=${group.centerGlobalCustNum}&recordOfficeId=${group.branchId}&recordLoanOfficerId=${group.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${group.centerName}
					</a>
					[/#if]
				</span>
			</di>
		</div>
		<div>
			<img src="pages/framework/images/trans.gif" width="10" height="10">
		</div>
		[/#list]
		[#list customerHierarchy.centers as center]
		<div>
			<div>
				[#assign number = number + 1 /]
				${number}.
				[@spring.message "${ConfigurationConstants.CENTER}" /]
				<span class="headingblue">
					<a href="viewCenterDetails.ftl?globalCustNum=${center.centerGlobalCustNum}&recordOfficeId=${center.branchId}&
							 recordLoanOfficerId=${center.loanOfficerId?if_exists}&randomnum=${Session.randomNUm}">
						${center.centerName}: ${center.centerGlobalCustNum}
					</a>
				</span>
			</div>
			<div>
				<span class="fontnormalbold">
					[@spring.message "accounts.status" /]
				</span>
				<span>
					[@mifostag.MifosImage id="${center.customerStatusId}" moduleName="org.mifos.customers.util.resources.customerImages" /]
				</span>
				<span class="fontnormalbold">
					[@spring.message "client.LoanOff" /]
				</span>
				<span>
					${center.loanOfficerName}
				</span>
			</div>
			[#if center.savingsGlobalAccountNum?has_content]
			<div>
				<span class="fontnormalbold">
					[@spring.message "${ConfigurationConstants.SAVINGS}.Savings" /]:
				</span>
				<span>
				[#list center.savingsGlobalAccountNum as saving]
					<a href="savingsAction.do?globalAccountNum=${saving}&method=get&recordOfficeId=${center.branchId}&recordLoanOfficerId=${center.loanOfficerId?if_exists}&randomNUm=${Session.randomNUm}">
						${saving}
					</a>
				[/#list]
				</span>
			</div>
			[/#if]
			<div>
				<span>
					[@spring.message "alsosee" /]:
				</span>
				<span>
					<a href="custSearchAction.do?method=getOfficeHomePage&officeId=${center.branchId}&randomNUm=${Session.randomNUm}">
						${center.branchName}
					</a>
				</span>
			</di>
		</div>
		<div>
			<img src="pages/framework/images/trans.gif" width="10" height="10">
		</div>
		[/#list]
		<div class="blueline">
		</div>
	</div>
	<div>
		[#if isPrevPageAvailable ]
		<span class="fontnormalbold">
			<a href="searchResult.ftl?currentPage=${currentPage-1}">	
				[@spring.message "Previous" /]	
			</a>
		</span>
		[#else]
		<span class="fontnormalboldgrey">
			[@spring.message "Previous" /]	
		</span>
		[/#if]
		<span>
		</span>
		[#if isNextPageAvailable ]
		<span class="fontnormalbold">
			<a href="searchResult.ftl?currentPage=${currentPage+1}">	
				[@spring.message "Next" /]	
			</a>
		</span>
		[#else]
		<span class="fontnormalboldgrey">
			[@spring.message "Next" /]	
		</span>
		[/#if]			
	</div>
	[/#if]
</div>