<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Manage Laboratory Confidential Test" otherwise="/login.htm" redirect="/module/laboratory/confidentialTest.form" />
<%@ include file="../localHeader.jsp" %>



<div class="boxHeader"> 
	<strong>Search patient</strong>
</div>
<div class="box">
	<div id="advSearch"></div>
</div>
<br/>
<div id="patientResult"></div>

<script>
	function showConfidentialTest(patientIdentifier){
		tb_show("confidential test", "addConfidentialTest.form?modal=true&width=600&height=320&patientIdentifier=" + patientIdentifier);
	}
	
	jQuery("#advSearch").toggleSearchBox({
		view:'laboratory_confidentialTest'
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp" %>  