package profile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import com.csvreader.CsvReader;
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
	private Repository mUserRepository = getUserRepository();	
	
	public ProfileManager() {}

	public void setUserRepository(Repository pUserRepository) {
		mUserRepository = pUserRepository;
	}

	public Repository getUserRepository() {
		return mUserRepository;	 
	}	
	
	public RepositoryItem findProfileByLogin(String pLogin) throws RepositoryException{		
		RqlStatement findProfileRQL;
		RepositoryView profileView = mUserRepository.getView("user");
		Object rqlParams[] = new Object[1];
		rqlParams[0] = pLogin;
		findProfileRQL = RqlStatement.parseRqlStatement("login = ?0");
		RepositoryItem [] profileList = findProfileRQL.executeQuery (profileView,rqlParams);

		if (profileList != null) {
			return profileList[0];
		}
			return null;
	}
	
	public RepositoryItem[] viewProfiles (int offset) throws RepositoryException{
		RqlStatement viewProfileRQL;
		RepositoryView profileView = mUserRepository.getView("user");
		Object rqlParams[] = new Object[1];
		rqlParams[0] = offset;
		viewProfileRQL = RqlStatement.parseRqlStatement("all range ?0+10");
		RepositoryItem [] profileList = viewProfileRQL.executeQuery (profileView,rqlParams);

		if (profileList != null) {
			return profileList;
		}
			return null;
	}
	
	public int profilesCount () throws RepositoryException{		
		RepositoryView profileView = mUserRepository.getView("user");
		QueryBuilder qb = profileView.getQueryBuilder();
		Query q = qb.createUnconstrainedQuery();		
		return profileView.executeCountQuery(q);		
	}
	
	public void createRepositoryItems(InputStream is) throws RepositoryException{			
			
			try{
				CsvReader profiles = new CsvReader(is, Charset.forName("UTF-8"));
				profiles.readHeaders();
	
				while (profiles.readRecord()){			
					MutableRepositoryItem mutContactInfoItem = ((MutableRepository) mUserRepository).createItem("contactInfo");			
					mutContactInfoItem.setPropertyValue("phoneNumber", profiles.get("PhoneNumber"));
					((MutableRepository) mUserRepository).addItem(mutContactInfoItem);
				    MutableRepositoryItem mutProfileItem  = ((MutableRepository) mUserRepository).createItem("user");
				    mutProfileItem.setPropertyValue("firstName", profiles.get("FirstName"));
			        mutProfileItem.setPropertyValue("lastName", profiles.get("LastName"));
			        mutProfileItem.setPropertyValue("login", profiles.get("Login"));
			        mutProfileItem.setPropertyValue("email", profiles.get("Email"));
			        mutProfileItem.setPropertyValue("password", profiles.get("Login"));
			        mutProfileItem.setPropertyValue("homeAddress", mutContactInfoItem);
			        ((MutableRepository) mUserRepository).addItem(mutProfileItem);
				}
				
				profiles.close();
							
			} catch (FileNotFoundException e) {
				logError(e);
			} catch (IOException e) {
				logError(e);
			} 		
  }	
	
	public void updateRepositoryItems(InputStream is) throws RepositoryException{		
		
		try{
			CsvReader profiles = new CsvReader(is, Charset.forName("UTF-8"));
			profiles.readHeaders();
			
			while (profiles.readRecord()){				
				MutableRepositoryItem mutProfileItem = ((MutableRepository) mUserRepository).getItemForUpdate(findProfileByLogin(profiles.get("Login")).getRepositoryId(), "user");
				MutableRepositoryItem mutContactInfoItem = ((MutableRepository) mUserRepository).getItemForUpdate(((RepositoryItem)findProfileByLogin(profiles.get("Login")).getPropertyValue("homeAddress")).getRepositoryId(), "contactInfo");
				mutContactInfoItem.setPropertyValue("phoneNumber", profiles.get("PhoneNumber"));
				((MutableRepository) mUserRepository).updateItem(mutContactInfoItem);
				mutProfileItem.setPropertyValue("firstName", profiles.get("FirstName"));
				mutProfileItem.setPropertyValue("lastName", profiles.get("LastName"));
				mutProfileItem.setPropertyValue("login", profiles.get("Login"));
				mutProfileItem.setPropertyValue("email", profiles.get("Email"));
				mutProfileItem.setPropertyValue("password", profiles.get("Login"));
		        mutProfileItem.setPropertyValue("homeAddress", mutContactInfoItem);
		        ((MutableRepository) mUserRepository).updateItem(mutProfileItem);				
			}
		}catch (FileNotFoundException e) {
			logError(e);
		}catch (IOException e) {
			logError(e);
		}		   	
	}
}