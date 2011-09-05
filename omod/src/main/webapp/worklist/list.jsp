<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Manage Laboratory Worklist" otherwise="/login.htm" redirect="/module/laboratory/worklist.form" />
<openmrs:globalProperty key="laboratory.worklist.findAllInvestigation" defaultValue="false" var="findAllInvestigation" />
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
	
	.chosen {		
		background-color:yellow;
	}
</style>

<script type="text/javascript">

	var GLOBAL = {
		findAllInvestigation: ${findAllInvestigation}
	};

	jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});		
    });
	
	/**
	 * GET ALL TESTS
	 */
	function getTests(){
		var date = jQuery("#date").val();
		var phrase = jQuery("#phrase").val();
		var investigation = jQuery("#investigation").val();
		
		validation = validate(investigation);		
		
		// GETTING THE TESTS
		if(validation.status){			
			jQuery.ajax({
				type : "GET",
				url : getContextPath() + "/module/laboratory/searchWorklist.form",
				data : ({
					date			: date,
					phrase			: phrase,
					investigation	: investigation
				}),
				success : function(data) {
					jQuery("#tests").html(data);	
					if(testNo>0){					
						tb_init("a.thickbox"); // init to show thickbox
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert(thrownError);
				}
			});
		} else {
			alert(validation.message);
		}		
	}
	
	/*
	 * Check whether user is allowed to get tests from all investigations
	 * return @validated {status, message}
	 */
	function validate(investigation){
	
		var validation = {
			status: "true",
			message: "fine"
		};
		
		if(investigation>0){			
			validation.status = true;
		} else {
			
			if(GLOBAL.findAllInvestigation){
				validation.status = true;
			} else {
				validation.status = false;
				validation.message = "Please select an investigation!";
			}
		}		
		
		return validation;
	}
	
	// reschedule a test
	function rescheduleTest(orderId, rescheduledDate) {
		jQuery.ajax({
			type : "POST",
			url : getContextPath() + "/module/laboratory/rescheduleTest.form",
			data : ({
				orderId : orderId,
				rescheduledDate : rescheduledDate
			}),
			success : function(data) {				
				if (data == 'success') {
					getTests();
				} else {
					alert(data);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(thrownError);
			}
		});
	}
	
	// complete a test
	function completeTest(testId) {		

		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/laboratory/ajax/completeTest.htm",
			data : ({
				testId : testId
			}),
			success : function(data) {
				if (data == 'success') {
					getTests();
				} else {
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(thrownError);
			}
		});
	}
	
	//  enter result for a test
	function enterResult(testId){
		
		jQuery(".resultParameter").hide();
		
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/laboratory/enterResult.form",
			data : ({
				testId : testId
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
	
</script> 

<div class="boxHeader"> 
	<strong>See patient List by choosing lab</strong>
</div>
<div class="box">
	Date:
	<input id="date" value="${currentDate}" style="text-align:right;"/>
	Patient ID/Name:
	<input id="phrase"/>
	Investigation:
	<select name="investigation" id="investigation">
		<option value="0">Select an investigation</option>
		<c:forEach var="investigation" items="${investigations}">
			<option value="${investigation.id}">${investigation.name.name}</option>
		</c:forEach>	
	</select>
	<br/>
	<input type="button" value="Get worklist" onClick="getTests();"/>
</div>

<div id="tests">
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>  