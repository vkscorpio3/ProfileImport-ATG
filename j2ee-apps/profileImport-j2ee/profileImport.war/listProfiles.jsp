<%@ taglib uri="http://www.atg.com/taglibs/daf/dspjspTaglib1_0" prefix="dsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<dsp:page>
<dsp:importbean bean="/profileImport/CustomPagingDroplet"/>
<HTML>
  <HEAD>
    <TITLE>Profiles</TITLE>
  </HEAD>
  <BODY>	
	<dsp:droplet name="/profileImport/CustomPagingDroplet">		
        <dsp:oparam name="output">
		<table border="1" width="100%" cellpadding="5">
		  <tr>
				<th><a href="listProfiles.jsp?page=<c:out value="${currentPage}"/>&sort=<c:out value="firstName"/>"/>FirstName</a></th>
				<th><a href="listProfiles.jsp?page=<c:out value="${currentPage}"/>&sort=<c:out value="lastName"/>"/>LastName</a></th>
				<th><a href="listProfiles.jsp?page=<c:out value="${currentPage}"/>&sort=<c:out value="login"/>"/>Login</a></th>
				<th><a href="listProfiles.jsp?page=<c:out value="${currentPage}"/>&sort=<c:out value="email"/>"/>Email</a></th>
				<th><a href="listProfiles.jsp?page=<c:out value="${currentPage}"/>&sort=<c:out value="phoneNumber"/>"/>Phone</a></th>
			</tr>
            <dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" param="element"/> 					
					<dsp:oparam name="outputStart">
					  <ul>
					</dsp:oparam>
					<dsp:oparam name="outputEnd">
					  </ul>
					</dsp:oparam>
					<dsp:oparam name="output">                  
						<tr>
							<td><dsp:valueof param="element.firstName"/></td>	
							<td><dsp:valueof param="element.lastName"/></td>
							<td><dsp:valueof param="element.login"/></td>
							<td><dsp:valueof param="element.email"/></td>
							<td><dsp:valueof param="element.homeAddress.phoneNumber"/></td>			
						</tr>               
					</dsp:oparam>
					<dsp:oparam name="empty">
					  <p>No profiles currently available, sorry.
					</dsp:oparam>
				  </dsp:droplet>
                            </dsp:oparam>
                            <dsp:oparam name="error">
                                 (Unable to find values)
                            </dsp:oparam>
                       </dsp:droplet>
	
   
		  </table>
		  
	<c:if test="${currentPage != 1}">
		<td><a href="listProfiles.jsp?page=<c:out value="${currentPage - 1}"/>">Previous</a></td>
	</c:if>
	
	<table border="1" cellpadding="5" cellspacing="5">
		<tr>
			<c:forEach begin="1" end="${countOfPages}" var="i">
				<c:choose>
					<c:when test="${currentPage eq i}">
						<td><c:out value="${i}"/></td>
					</c:when>
					<c:otherwise>
						<td><a href="listProfiles.jsp?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></td>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</tr>
	</table>	
	
	<c:if test="${currentPage lt countOfPages}">
		<td><a href="listProfiles.jsp?page=<c:out value="${currentPage + 1}"/>">Next</a></td>
	</c:if>

		<p><table>
			<tr>	<td><dsp:a href="homeProfile.jsp">
                Home  
			</dsp:a></td>
			<td><dsp:a href="importProfiles.jsp">
				Import profiles  
			</dsp:a></td>
			<td><dsp:a href="importProfiles.jsp">
				Update profiles  
			</dsp:a></td>
			</tr>
			</table>	
	 </BODY>
</HTML>
</dsp:page>	  