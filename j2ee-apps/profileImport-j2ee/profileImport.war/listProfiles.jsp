<%@ taglib uri="http://www.atg.com/taglibs/daf/dspjspTaglib1_0" prefix="dsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<dsp:page>
<dsp:importbean bean="/profileImport/CustomPagingDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<HTML>
  <HEAD>
    <TITLE>Profiles</TITLE>
  </HEAD>
  <BODY>
	<dsp:droplet name="/profileImport/CustomPagingDroplet">			
        <dsp:oparam name="output">		
			<table border="1" width="100%" cellpadding="5">
			  <tr>
					<th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" value="firstName"/><dsp:param name="recordsPerPage" param="recordsPerPage"/><dsp:valueof value="First Name"/></dsp:a></th>
					<th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" value="lastName"/><dsp:param name="recordsPerPage" param="recordsPerPage"/><dsp:valueof value="Last Name"/></dsp:a></th>
					<th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" value="login"/><dsp:param name="recordsPerPage" param="recordsPerPage"/><dsp:valueof value="Login"/></dsp:a></th>
					<th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" value="email"/><dsp:param name="recordsPerPage" param="recordsPerPage"/><dsp:valueof value="Email"/></dsp:a></th>
					<th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" value="phoneNumber"/><dsp:param name="recordsPerPage" param="recordsPerPage"/><dsp:valueof value="Phone"/></dsp:a></th>			
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
			</table>
			
				  <th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" param="sortProperty"/><dsp:param name="recordsPerPage" value="5"/><dsp:valueof value="Show 5"/></dsp:a></th>
				  <th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" param="sortProperty"/><dsp:param name="recordsPerPage" value="10"/><dsp:valueof value="Show 10"/></dsp:a></th>
				  <th><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="currentPage"/><dsp:param name="sortProperty" param="sortProperty"/><dsp:param name="recordsPerPage" value="15"/><dsp:valueof value="Show 15"/></dsp:a></th>
				
				<dsp:droplet name="Switch">
					<dsp:param param="hasPrevious"  name="value"/>
					<dsp:oparam name="true">
						<table><tr><td><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="previous"/><dsp:param name="sortProperty" param="sortProperty"/><dsp:param name="recordsPerPage" param="recordsPerPage"/>Previous</dsp:a></td></th></table>
					</dsp:oparam>
				</dsp:droplet>
			 	
				<dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" param="pages"/> 					
					<dsp:oparam name="outputStart">
					  <ul>
					</dsp:oparam>
					<dsp:oparam name="outputEnd">
					  </ul>
					</dsp:oparam>
					<dsp:oparam name="output">                  
					  <tr>                    
                       <td><dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="element"/><dsp:param name="sortProperty" param="sortProperty"/><dsp:param name="recordsPerPage" param="recordsPerPage"/><dsp:valueof param="element"/></dsp:a></td>	
                     </tr> 										
					</dsp:oparam>					
					<dsp:oparam name="empty">
					  <p>No pages currently available, sorry.
					</dsp:oparam>
				</dsp:droplet>
				
				<dsp:droplet name="Switch">
					<dsp:param param="hasNext"  name="value"/>
					<dsp:oparam name="true">
						<dsp:a href="listProfiles.jsp"><dsp:param name="currentPage" param="next"/><dsp:param name="sortProperty" param="sortProperty"/><dsp:param name="recordsPerPage" param="recordsPerPage"/>Next</dsp:a>
					</dsp:oparam>					
                    <dsp:oparam name="error">
                        (Unable to find values)
                    </dsp:oparam>
				</dsp:droplet>	
		</dsp:oparam>
	</dsp:droplet>  

	<p><table>
		<tr><td><dsp:a href="homeProfile.jsp">
                   Home  
				</dsp:a></td>
			<td><dsp:a href="importProfiles.jsp">
				   Import profiles  
			</dsp:a></td>			
		</table>	
 </BODY>
</HTML>
</dsp:page>	  