package profile;

import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class CustomPagingDroplet extends atg.servlet.DynamoServlet{
	
	private ProfileManager mProfileManager = getProfileManager();

	public CustomPagingDroplet(){}
	
	public ProfileManager getProfileManager() {
		return mProfileManager;
	}
	
	public void setProfileManager(ProfileManager pProfileManager) {
		this.mProfileManager = pProfileManager;
	}
	
	public void service (DynamoHttpServletRequest request, DynamoHttpServletResponse response)
			throws javax.servlet.ServletException, java.io.IOException{
		
		int page = 1;
		int recordsPerPage = 10;
		if(request.getParameter("page") != null)
			page = Integer.parseInt(request.getParameter("page"));
		ProfileManager mProfileManager = getProfileManager();
		RepositoryItem[] list = null;
		try {
			list = mProfileManager.viewProfiles((page*10)-10,(String)request.getParameter("sort"));
			
			if (list != null) {  	     	       
			   request.setParameter("element", list);
			   request.serviceParameter ("output", request, response);	     	      
	        }else {
	               request.serviceParameter ("error", request, response);
	         }
		} catch (RepositoryException e) {			
			logError(e);
		}
		
		int countOfRecords = 0;
		
		try {
			countOfRecords = mProfileManager.profilesCount();
		} catch (RepositoryException e) {
			logError(e);			
		}
		
		int countOfPages = (((countOfRecords - 1) / recordsPerPage) + 1);		
		request.setAttribute("countOfPages", countOfPages);
		request.setAttribute("currentPage", page);			
	}
}
