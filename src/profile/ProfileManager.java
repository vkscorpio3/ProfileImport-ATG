package profile;

import javax.transaction.TransactionManager;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;

public class ProfileManager extends atg.nucleus.GenericService {
	private RqlStatement findProfileRQL;
	private RqlStatement viewProfileRQL;
	private Object rqlParams[];
	private RepositoryView profileView;
	private Repository mUserRepository;
	private TransactionManager mTransactionManager;

	public TransactionManager getTransactionManager() {
		return mTransactionManager;
	}

	public void setTransactionManager(TransactionManager pTransactionManager) {
		mTransactionManager = pTransactionManager;
	}

	public void setUserRepository(Repository pUserRepository) {
		mUserRepository = pUserRepository;
	}

	public Repository getUserRepository() {
		return mUserRepository;
	}	
	
	@Override
	public void doStartService(){
		try{
			findProfileRQL = RqlStatement.parseRqlStatement("login = ?0");
			viewProfileRQL = RqlStatement.parseRqlStatement("all order by ?1 range ?2+?3");
			profileView = getUserRepository().getView("user");
			rqlParams = new Object[4];			
		}catch(RepositoryException re){
			
			if(isLoggingError())
				logError(re);
		}
	}

	public RepositoryItem findProfileByLogin(String pLogin)throws RepositoryException {		
		rqlParams[0] = pLogin;
		RepositoryItem[] profileList = findProfileRQL.executeQuery(profileView, rqlParams);

		if (profileList != null) {
			return profileList[0];
		}
		return null;
	}

	public RepositoryItem[] viewProfiles(String sortProperty, int offset,
			int recordsCount) throws RepositoryException {		
		rqlParams[1] = sortProperty;
		rqlParams[2] = offset;
		rqlParams[3] = recordsCount;		
		RepositoryItem[] profileList = viewProfileRQL.executeQuery(profileView,	rqlParams);

		if (profileList != null) {
			return profileList;
		}
		return null;
	}

	public int profilesCount() throws RepositoryException {		
		QueryBuilder qb = profileView.getQueryBuilder();
		Query q = qb.createUnconstrainedQuery();
		return profileView.executeCountQuery(q);
	}

	public void createRepositoryItems(String[] profileProperties) {
		
		try {
			TransactionDemarcation td = new TransactionDemarcation();
			td.begin(getTransactionManager(), td.REQUIRED);

			try {
				MutableRepositoryItem mutContactInfoItem = ((MutableRepository) mUserRepository).createItem("contactInfo");
				mutContactInfoItem.setPropertyValue("phoneNumber", profileProperties[4]);
				((MutableRepository) mUserRepository).addItem(mutContactInfoItem);
				MutableRepositoryItem mutProfileItem = ((MutableRepository) mUserRepository).createItem("user");
				mutProfileItem.setPropertyValue("firstName",profileProperties[0]);
				mutProfileItem.setPropertyValue("lastName",profileProperties[1]);
				mutProfileItem.setPropertyValue("login", profileProperties[2]);
				mutProfileItem.setPropertyValue("email", profileProperties[3]);
				mutProfileItem.setPropertyValue("password", "12345");
				mutProfileItem.setPropertyValue("homeAddress",mutContactInfoItem);
				((MutableRepository) mUserRepository).addItem(mutProfileItem);
			} catch (RepositoryException re) {

				if (isLoggingError())
					logError(re);

				try {
					getTransactionManager().setRollbackOnly();
				} catch (Exception se) {
					if (isLoggingError())
						logError("Unable to set rollback for transaction", se);
				}
			} finally {
				td.end();
			  }
		} catch (TransactionDemarcationException e) {
			if (isLoggingError())
				logError("creating transaction demarcation failed, no profile created", e);
		}
	}

	public void updateRepositoryItems(RepositoryItem profile, String[] profileProperties) throws RepositoryException {
		
		try {
			TransactionDemarcation td = new TransactionDemarcation();
			td.begin(getTransactionManager(), td.REQUIRED);

			try {		
				MutableRepositoryItem mutProfileItem = ((MutableRepository) mUserRepository).getItemForUpdate(profile.getRepositoryId(), "user");
				MutableRepositoryItem mutContactInfoItem = ((MutableRepository) mUserRepository)
						.getItemForUpdate(((RepositoryItem) profile.getPropertyValue("homeAddress")).getRepositoryId(),"contactInfo");
				mutContactInfoItem.setPropertyValue("phoneNumber", profileProperties[4]);
				((MutableRepository) mUserRepository).updateItem(mutContactInfoItem);
				mutProfileItem.setPropertyValue("firstName", profileProperties[0]);
				mutProfileItem.setPropertyValue("lastName", profileProperties[1]);
				mutProfileItem.setPropertyValue("login", profileProperties[2]);
				mutProfileItem.setPropertyValue("email", profileProperties[3]);
				mutProfileItem.setPropertyValue("password", "12345");
				mutProfileItem.setPropertyValue("homeAddress", mutContactInfoItem);
				((MutableRepository) mUserRepository).updateItem(mutProfileItem);
			}catch (RepositoryException re) {

				if (isLoggingError())
					logError(re);

				try {
					getTransactionManager().setRollbackOnly();
				} catch (Exception se) {
					if (isLoggingError())
						logError("Unable to set rollback for transaction", se);
				}
			} finally {
				td.end();
			  }
	    } catch (TransactionDemarcationException e) {
		      if (isLoggingError())
			     logError("creating transaction demarcation failed, no profile created", e);
	    }
     }		
}
	