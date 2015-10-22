package profile;

import java.util.ArrayList;
import java.util.List;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class CustomPagingDroplet extends atg.servlet.DynamoServlet {

	private ProfileManager mProfileManager = getProfileManager();

	public ProfileManager getProfileManager() {
		return mProfileManager;
	}

	public void setProfileManager(ProfileManager pProfileManager) {
		this.mProfileManager = pProfileManager;
	}

	public void service(DynamoHttpServletRequest request,DynamoHttpServletResponse response)
			throws javax.servlet.ServletException, java.io.IOException {
		String currentPage = "1";
		String recordsPerPage = "5";
		String sortProperty = "login";
		Boolean hasPrevious = false;
		Boolean hasNext = false;
		String previous = null;
		String next = null;

		if (request.getParameter("currentPage") != null)
			currentPage = request.getParameter("currentPage");
		request.setParameter("currentPage", currentPage);

		if (request.getParameter("recordsPerPage") != null)
			recordsPerPage = request.getParameter("recordsPerPage");
		request.setParameter("recordsPerPage", recordsPerPage);

		if (request.getParameter("sortProperty") != null)
			sortProperty = request.getParameter("sortProperty");
		request.setParameter("sortProperty", sortProperty);

		ProfileManager mProfileManager = getProfileManager();
		RepositoryItem[] list = null;

		int curPage = Integer.parseInt(currentPage);
		int recordsPP = Integer.parseInt(recordsPerPage);
		int offset = ((curPage * recordsPP) - recordsPP);

		try {
			list = mProfileManager.viewProfiles(sortProperty, offset, recordsPP);

			if (list != null) {
				request.setParameter("element", list);
				request.serviceParameter("output", request, response);
			} else {
				request.serviceParameter("error", request, response);
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

		int count = (((countOfRecords - 1) / (Integer.parseInt(recordsPerPage))) + 1);
		List<String> pages = new ArrayList<String>();
		
		for (Integer i = 1; i <= count; i++) {
			pages.add(String.valueOf(i));
		}
		
		pages.toArray();
		request.setParameter("pages", pages);

		if (Integer.parseInt(request.getParameter("currentPage")) != 1) {
			hasPrevious = true;
			previous = String.valueOf(Integer.parseInt(currentPage) - 1);
			request.setParameter("hasPrevious", hasPrevious);
			request.setParameter("previous", previous);
		}

		if (Integer.parseInt(currentPage) < count) {
			hasNext = true;
			next = String.valueOf(Integer.parseInt(currentPage) + 1);
			request.setParameter("hasNext", hasNext);
			request.setParameter("next", next);
		}
	}
}