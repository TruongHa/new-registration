<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.openmrs.Patient" %>

<script type="text/javascript">
	
	PATIENTSEARCHRESULT = {
		oldBackgroundColor: "",
		
		/** Click to view patient info */
		visit: function(patientId){			
			window.location.href = openmrsContextPath + "/module/registration/showPatientInfo.form?patientId=" + patientId;
		},
		
		/** Edit a patient */
		editPatient: function(patientId){
			window.location.href = openmrsContextPath + "/module/registration/editPatient.form?patientId=" + patientId;
		}
	};
	
	jQuery(document).ready(function(){
	
		// hover rows
		jQuery(".patientSearchRow").hover(
			function(event){					
				obj = event.target;
				while(obj.tagName!="TR"){
					obj = obj.parentNode;
				}
				PATIENTSEARCHRESULT.oldBackgroundColor = jQuery(obj).css("background-color");
				jQuery(obj).css("background-color", "#00FF99");									
			}, 
			function(event){
				obj = event.target;
				while(obj.tagName!="TR"){
					obj = obj.parentNode;
				}
				jQuery(obj).css("background-color", PATIENTSEARCHRESULT.oldBackgroundColor);				
			}
		);
		
		// insert images
		jQuery(".editImage").each(function(index, value){
			jQuery(this).attr("src", openmrsContextPath + "/images/edit.gif");
		});		
	});
</script>

<c:choose>
	<c:when test="${not empty patients}" >		
	<table style="width:100%">
		<tr>
			<td><b>Edit</b></td>
			<td><b>Identifier</b></td>
			<td><b>Name</b></td>
			<td><b>Age</b></td>
			<td><b>Gender</b></td>			
			<td><b>Birthdate</b></td>
			<td><b>Relative Name</b></td>
		</tr>
		<c:forEach items="${patients}" var="patient" varStatus="varStatus">
			<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } patientSearchRow'>
				<td onclick="PATIENTSEARCHRESULT.editPatient(${patient.patientId});">
					<center>
						<img class="editImage" title="Edit patient information"/>
					</center>
				</td>
				<td onclick="PATIENTSEARCHRESULT.visit(${patient.patientId});">
					${patient.patientIdentifier.identifier}
				</td>
				<td onclick="PATIENTSEARCHRESULT.visit(${patient.patientId});">${patient.givenName} ${patient.middleName} ${patient.familyName}</td>
				<td onclick="PATIENTSEARCHRESULT.visit(${patient.patientId});"> 
                	<c:choose>
                		<c:when test="${patient.age == 0}">&lt 1</c:when>
                		<c:otherwise >${patient.age}</c:otherwise>
                	</c:choose>
                </td>
				<td onclick="PATIENTSEARCHRESULT.visit(${patient.patientId});">
					<c:choose>
                		<c:when test="${patient.gender eq 'M'}">
							<img src="${pageContext.request.contextPath}/images/male.gif"/>
						</c:when>
                		<c:otherwise><img src="${pageContext.request.contextPath}/images/female.gif"/></c:otherwise>
                	</c:choose>
				</td>                
				<td onclick="PATIENTSEARCHRESULT.visit(${patient.patientId});"> 
                	<openmrs:formatDate date="${patient.birthdate}"/>
                </td>
				<td onclick="PATIENTSEARCHRESULT.visit(${patient.patientId});"> 
                	<%
						Patient patient = (Patient) pageContext.getAttribute("patient");
						Map<Integer, Map<Integer, String>> attributes = (Map<Integer, Map<Integer, String>>) pageContext.findAttribute("attributeMap");						
						Map<Integer, String> patientAttributes = (Map<Integer, String>) attributes.get(patient.getPatientId());						
						String relativeName = patientAttributes.get(8); 
						if(relativeName!=null)
							out.print(relativeName);
					%>
                </td>
			</tr>
		</c:forEach>
	</table>
	</c:when>
	<c:otherwise>
	No Patient found.
	</c:otherwise>
</c:choose>