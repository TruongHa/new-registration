<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Edit Laboratory Result" otherwise="/login.htm" redirect="/module/laboratory/editResult.form" />
<%@ include file="../localHeader.jsp" %>

<style>
	.parameter {
		width: 280px;
		padding: 5px;
		margin: 5px;
		float: left;
		text-align: center;
		border: 1px solid black;
	}
</style>

<script type="text/javascript">

	jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
    });
	
	/**
	 * GET ALL TESTS
	 * @patientIdentifier patient identifier
	 */	
	function getTests(patientIdentifier){
		var date = jQuery("#date").val();
		var phrase = patientIdentifier;
		var investigation = jQuery("#investigation").val();
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/laboratory/searchCompletedTests.form",
			data : ({
				date			: date,
				phrase			: phrase,
				investigation	: investigation
			}),
			success : function(data) {
				jQuery("#tests").html(data);
				jQuery("#patientResult").hide();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(thrownError);
			}
		});
	}
	
	//  enter result for a test
	function enterResult(testId, conceptId, encounterId){
	
		jQuery(".resultParameter").hide();
		
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/laboratory/showForm.form",
			data : ({
				encounterId : encounterId,
				conceptId	: conceptId
			}),
			success : function(data) {
				jQuery("#contentForm" + testId).html(data);				
				jQuery("#row" + testId).show();
				formatParameters();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(thrownError);
			}
		});
	}
	
	// format parameters for entering results
	function formatParameters(){
		jQuery(".parameter").hover(
			function(){
				jQuery(this).css("background-color", "#3399FF");
			}, 
			function(){
				jQuery(this).css("background-color", "#FFFFFF");
			}
		);
	}
	
	// submit form
	function submit(testId){
		validationResult = jQuery("#contentForm" + testId).valid();
		if(validationResult){
			jQuery("#contentForm" + testId).ajaxSubmit({
				success: function (responseText, statusText, xhr){					
					if(responseText.indexOf('success')>0){						
						getTests();
						completeTest(testId);						
					}
				}
			});					
		}
	}
	
	/**
	 * SHOW PATIENT SEARCH RESULT
	 */
	function showPatientSearchResult(){
		jQuery('#patientResult').show();
	}
</script> 

<div class="boxHeader"> 
	<strong>See patient List by choosing lab</strong>
</div>
<div class="box">
	Date:
	<input id="date" value="${currentDate}" style="text-align:right;"/>	
	Investigation:
	<select name="investigation" id="investigation">
		<option value="0">Select an investigation</option>
		<c:forEach var="investigation" items="${investigations}">
			<option value="${investigation.id}">${investigation.name.name}</option>
		</c:forEach>	
	</select>
	<a href="javascript:showPatientSearchResult()">Show search results</a>
	
	<div id="advSearch"></div>	
</div>

<br/>
<div id="patientResult"></div>
<script>
	jQuery("#advSearch").toggleSearchBox({
		view:'laboratory_editresult',
		beforeSearch: function(){
			showPatientSearchResult();
		}
	});
</script>

<div id="tests">
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>  