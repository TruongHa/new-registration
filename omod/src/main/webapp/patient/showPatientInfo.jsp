<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<script type="text/javascript">
	var _attributes = new Array();
	<c:forEach var="entry" items="${patient.attributes}">
		_attributes[${entry.key}] = "${entry.value}";
	</c:forEach>
	
	/**
	 ** VALUES FROM MODEL
	 **/
	MODEL = {
		patientId: "${patient.patientId}",
		patientIdentifier: "${patient.identifier}",
		patientName: "${patient.fullname}",
		patientAge: "${patient.age}",
		patientGender: "${patient.gender}",
		patientAddress: "${patient.address}",
		patientAttributes: _attributes,
		currentDateTime: "${currentDateTime}",		
		OPDs: "${OPDs}",
		dueDate: "${dueDate}",
		daysLeft: "${daysLeft}"
	};
</script>

<jsp:include page="../includes/dduPatientInfoForm.jsp"/>

<%@ include file="/WEB-INF/template/footer.jsp" %>  