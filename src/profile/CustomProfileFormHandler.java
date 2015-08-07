package profile;

import java.io.FileNotFoundException;
import java.io.IOException;
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

	void validateImportInput() throws RepositoryException {
		try {
			CsvReader profiles = new CsvReader(getUploadedFile()
					.getInputStream(), Charset.forName("UTF-8"));
			profiles.readHeaders();

			while (profiles.readRecord()) {

				if ((profiles.getColumnCount() != 5)) {
					addFormException(new DropletException(
							"Incorrect  count of columns for profile with login "
									+ profiles.get("Login") + " !"));
				}
			}

			profiles.close();
		} catch (FileNotFoundException e) {
			logError(e);
		} catch (IOException e) {
			logError(e);
		}
	}

	public boolean handleImport(DynamoHttpServletRequest request,
			DynamoHttpServletResponse response) throws java.io.IOException,
			RepositoryException {
		validateImportInput();
		String[] profileProperties = null;

		if (!(getFormError())) {

			if (!(Boolean) mLockRepository.getItem("100002", "lock")
					.getPropertyValue("isLocked")) {
				((MutableRepository) mLockRepository).getItemForUpdate(
						"100002", "lock").setPropertyValue("isLocked", true);

				try {
					CsvReader profiles = new CsvReader(getUploadedFile().getInputStream(), Charset.forName("UTF-8"));
					profiles.readHeaders();

					while (profiles.readRecord()) {
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
				} finally {
					getUploadedFile().getInputStream().close();
					((MutableRepository) mLockRepository).getItemForUpdate("100002", "lock").setPropertyValue("isLocked",false);
				}

				if (getSuccessURL() != null) {
					response.sendLocalRedirect(getSuccessURL(), request);
					return false;
				}
			} else {
				addFormException(new DropletException(
						"It is running other import at the same time. Please, wait!"));
			}
		}
		return true;
	}
}
