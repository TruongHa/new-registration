<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>

<script type="text/javascript">

	// Districts
	var _districts = new Array();
	<c:forEach var="district" items="${districts}" varStatus="status">
		_districts[${status.index}] = "${district}";
	</c:forEach>
	
	// Tehsils
	var _tehsils = new Array();
	<c:forEach var="tehsil" items="${tehsils}" varStatus="status">
		_tehsils[${status.index}] = "${tehsil}";
	</c:forEach>	
	
	// Patient Attribute
	var _attributes = new Array();
	<c:forEach var="entry" items="${patient.attributes}">
		_attributes[${entry.key}] = "${entry.value}";
	</c:forEach>
	
	/**
	 ** MODEL FROM CONTROLLER
	 **/
	MODEL = {
		patientId: "${patient.patientId}",
		patientIdentifier: "${patient.identifier}",
		patientName: "${patient.fullname}",
		patientAge: "${patient.age}",
		patientGender: "${patient.gender}",
		patientAddress: "${patient.address}",
		patientBirthdate: "${patient.birthdate}",
		patientAttributes: _attributes,
		districts: _districts,
		tehsils: _tehsils
	};
</script>

<jsp:include page="../includes/dduEditPatientForm.jsp"/>

<%@ include file="/WEB-INF/template/footer.jsp" %>  