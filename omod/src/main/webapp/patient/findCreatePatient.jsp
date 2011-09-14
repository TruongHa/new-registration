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
	
	/**
	 ** MODEL FROM CONTROLLER
	 **/
	MODEL = {
		patientIdentifier: "${patientIdentifier}",
		districts: _districts,
		tehsils: _tehsils,
		OPDs: "${OPDs}",
		referralHospitals: "${referralHospitals}",
		referralReasons: "${referralReasons}"
	}
</script>

<jsp:include page="../includes/dduPatientRegisterForm.jsp"/>

<%@ include file="/WEB-INF/template/footer.jsp" %>  