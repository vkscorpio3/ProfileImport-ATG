<%@ taglib uri="/dspTaglib" prefix="dsp" %>
<dsp:importbean bean="/profileImport/CustomProfileFormHandler"/>
<dsp:page>
<HTML>
  <HEAD>
    <TITLE>Profiles</TITLE>
  </HEAD>
  <BODY>
	 <dsp:form enctype="multipart/form-data" action="importProfiles.jsp" method="post" >
		<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
					   <dsp:oparam name="output">
						 <b><dsp:valueof param="message"/></b><br>
					   </dsp:oparam>
					   <dsp:oparam name="outputStart">
						 <LI>
					   </dsp:oparam>
					   <dsp:oparam name="outputEnd">
						 </LI>
					   </dsp:oparam>
					 </dsp:droplet>
	   Pick a file to upload:
		<dsp:input type="file" bean="/profileImport/CustomProfileFormHandler.uploadedFile" value=""/>
		<dsp:input bean="CustomProfileFormHandler.successURL" type="hidden" value="listProfiles.jsp"/>
		<dsp:input type="submit" bean="/profileImport/CustomProfileFormHandler.create" value="Import"/>
		<dsp:input type="submit" bean="/profileImport/CustomProfileFormHandler.update" value="Update"/>   
	 </dsp:form>
  </BODY>
</HTML>
</dsp:page>	 