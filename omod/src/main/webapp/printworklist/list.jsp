<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Print Laboratory Worklist" otherwise="/login.htm" redirect="/module/laboratory/printWorklist.form" />
<%@ include file="../localHeader.jsp" %>

<script type="text/javascript">

	jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
    });
	
	// get all tests
	function getTests(){
		var date = jQuery("#date").val();
		var phrase = jQuery("#phrase").val();
		var investigation = jQuery("#investigation").val();
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/laboratory/searchPrintWorklist.form",
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
	}
	
	function printWorklist(){
		jQuery("#printArea").printArea({
			mode : "popup",
			popClose : true
		});
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
	<input type="button" value="Print worklist" onClick="printWorklist();"/> 
</div>

<div id="tests">
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>  