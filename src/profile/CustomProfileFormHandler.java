package profile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import com.csvreader.CsvReader;
import atg.droplet.DropletException;
import atg.droplet.GenericFormHandler;
import atg.repository.MutableRepository;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.UploadedFile;

public class CustomProfileFormHandler extends GenericFormHandler {
	private ProfileManager mProfileManager = getProfileManager();
	private UploadedFile mUploadedFile = null;
	private String mSuccessURL = null;
	private RepositoryItem profile = null;
	private Repository mLockRepository = getLockRepository();	
	
	public UploadedFile getUploadedFile() {
		return mUploadedFile;
	}

	public void setUploadedFile(UploadedFile pUploadedFile) {
		this.mUploadedFile = pUploadedFile;
	}

	public ProfileManager getProfileManager() {
		return mProfileManager;
	}

	public void setProfileManager(ProfileManager pProfileManager) {
		this.mProfileManager = pProfileManager;
	}

	public String getSuccessURL() {
		return mSuccessURL;
	}

	public void setSuccessURL(String pSuccessURL) {
		mSuccessURL = pSuccessURL;
	}

	public Repository getLockRepository() {
		return mLockRepository;
	}

	public void setLockRepository(Repository pLockRepository) {
		this.mLockRepository = pLockRepository;
	}
	 
	void validateImportInput(InputStream is, CsvReader profiles) throws RepositoryException, IOException, FileNotFoundException{		
		try {
			is = getUploadedFile().getInputStream();
			profiles = new CsvReader(is, Charset.forName("UTF-8"));
			profiles.readHeaders();

			while (profiles.readRecord()) {
				if ((profiles.getColumnCount() != 5)) {
					addFormException(new DropletException("Incorrect  count of columns for profile with login "
									+ profiles.get("Login") + " !"));
				}
			}			
		} finally{
			is.close();
			profiles.close();			
		}
	}

	public boolean handleImport(DynamoHttpServletRequest request, DynamoHttpServletResponse response) throws IOException, RepositoryException{
		InputStream is = null;
		CsvReader profiles = null;
		
		try{
			is = getUploadedFile().getInputStream();
			profiles = new CsvReader(is, Charset.forName("UTF-8"));		 
		    validateImportInput(is,profiles);
		    String[] profileProperties = null;

			if (!(getFormError())) {
	
				if (!(Boolean) mLockRepository.getItem("100002", "lock").getPropertyValue("isLocked")) {
					((MutableRepository) mLockRepository).getItemForUpdate("100002", "lock").setPropertyValue("isLocked", true);
	
					try {					
						profiles.readHeaders();					
						while (profiles.readRecord()) {
							
							if ((profiles.getColumnCount() != 5)) {
								addFormException(new DropletException("Incorrect  count of columns for profile with login "
												+ profiles.get("Login") + " !"));
							}
							
							profile = mProfileManager.findProfileByLogin(profiles.get("Login"));
							profileProperties = new String[] {
									profiles.get("FirstName"),
									profiles.get("LastName"),
									profiles.get("Login"),
									profiles.get("Email"),
									profiles.get("PhoneNumber") 
							};
							
							if (profile != null) {
								mProfileManager.updateRepositoryItems(profile,profileProperties);
							} else {
								mProfileManager.createRepositoryItems(profileProperties);
							}
						}
						((MutableRepository) mLockRepository).getItemForUpdate("100002", "lock").setPropertyValue("isLocked",false);
					} catch (RepositoryException e) {
						logError(e);
					} 
					
					if (getSuccessURL() != null) {
						response.sendLocalRedirect(getSuccessURL(), request);
						return false;
					}
				} else {
					addFormException(new DropletException("It is running other import at the same time. Please, wait!"));
				}
			  }	
			}catch (IOException e){			
			
				if(isLoggingError()){
					logError(e);
				}	
			}finally {			
				is.close();
				profiles.close();
				((MutableRepository) mLockRepository).getItemForUpdate("100002", "lock").setPropertyValue("isLocked",false);
			}
		return true;
	}
}
