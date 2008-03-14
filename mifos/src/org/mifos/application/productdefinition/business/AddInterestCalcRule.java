package org.mifos.application.productdefinition.business;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mifos.application.master.business.MasterDataEntity;
import org.mifos.application.master.business.MifosLookUpEntity;
import org.mifos.framework.persistence.DatabaseVersionPersistence;
import org.mifos.framework.persistence.Upgrade;

/* AddInterestCalcRule adds a new type of interest calculation method
 * e.g. Declining Balance - Equal Principal. This setting is used to
 * calculate interest with declining method, but with principal being
 * equal in all installments.
 */
public class AddInterestCalcRule extends Upgrade {

	private final int newRuleId;
	private final int categoryId;
	private final String lookupName;
	private final String description;

	private final Short locale;
	private final String message;
	protected static final String wrongLookupValueKeyFormat = "The key format must be InterestTypes-...";
	protected static final String keyFormat = "InterestTypes-";

	
	/* This constructor is used for version 174 and lower. 
	 * And it must not be used afterward
	 */
	public AddInterestCalcRule(int higherVersion, int newRuleId, 
		int categoryId, String lookupName, String description, Short locale, String message) {
		super(higherVersion);
		if (higherVersion > lookupValueChangeVersion)
			throw new RuntimeException(wrongConstructor);
		this.newRuleId = newRuleId;
		this.lookupName = lookupName;		
		this.categoryId = categoryId;
		this.description = description;
		this.locale = locale;
		this.message = message;
	}
	
	/*
	 * This constructor must be used after version 174. The lookupValueKey must in the format
	 * InterestTypes-...
	 */
	public AddInterestCalcRule(int higherVersion, int newRuleId, 
			int categoryId, String lookupName, String description) {
			super(higherVersion);
			if (!validateLookupValueKey(keyFormat, lookupName))
				throw new RuntimeException(wrongLookupValueKeyFormat);
			this.newRuleId = newRuleId;
			this.lookupName = lookupName;		
			this.categoryId = categoryId;
			this.description = description;
			this.locale = MasterDataEntity.CUSTOMIZATION_LOCALE_ID;
			this.message = null;
		}
	
	@Override
	public void upgrade(Connection connection, DatabaseVersionPersistence databaseVersionPersistence) throws IOException, SQLException {
		int lookupEntity = MifosLookUpEntity.INTEREST_TYPES;

		int lookupId = insertLookupValue(connection, lookupEntity, lookupName);
		insertMessage(connection, lookupId, locale, message);
		addInterestType(connection, newRuleId, description, lookupId);
		upgradeVersion(connection);
	}

	@Override
	public void downgrade(Connection connection, DatabaseVersionPersistence databaseVersionPersistence) throws IOException,
			SQLException {
		short lookupId = findLookupId(connection);

		deleteFromInterestTypes(connection);
		deleteFromLookupValueLocale(connection, lookupId);
		deleteFromLookupValue(connection, lookupId);

		downgradeVersion(connection);
	}

	private void addInterestType(Connection connection, 
		int newRuleId, String description, int lookupId) 
	throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO INTEREST_TYPES(" +
			"  INTEREST_TYPE_ID,LOOKUP_ID,CATEGORY_ID,DESCRIPTON)" +
			"VALUES(?,?,?,?)");
		statement.setInt(1, newRuleId);
		statement.setInt(2, lookupId);
		statement.setInt(3, categoryId);
		statement.setString(4, description);
		statement.executeUpdate();
		statement.close();
	}

	private short findLookupId(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"select LOOKUP_ID " +
			"from INTEREST_TYPES where INTEREST_TYPE_ID = ?");
		statement.setInt(1, newRuleId);
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			short lookupId = results.getShort("LOOKUP_ID");
			statement.close();
			return lookupId;
		}
		else {
			statement.close();
			throw new RuntimeException(
				"unable to downgrade: no Interest type " + newRuleId);
		}
	}
	
	private void deleteFromInterestTypes(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"delete from INTEREST_TYPES where INTEREST_TYPE_ID = ?");
		statement.setInt(1, newRuleId);
		statement.executeUpdate();
		statement.close();
	}
}
