<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
    });
	
	function save(){
		jQuery("#confidentialTest").ajaxSubmit({
			success: function(responseText, statusText, xhr){
				if(responseText == "success") {
					tb_remove();
				} else {
					jQuery("#errorSection").html(responseText);
					jQuery("#errorSection").show();
				}
			}
		});				
	}
</script>

<style>
	.info {
		font-weight: bold;
		text-align:right
	}
</style>

<div id="errorSection" class="error" style="display:none;">
	
</div>

<form id="confidentialTest" action="addConfidentialTest.form" method="POST">
	<input type='hidden' name='patientId' value='${patient.patientId}'/>
	<center>
		<table class="testInfo" cellspacing="15">
			<tr>
				<td class='info'>Patient Name</td>
				<td>${patient.givenName} ${patient.middleName} ${patient.familyName}</td>
				<td></td>
				<td class='info'>Patient Identifier</td>
				<td>${patient.patientIdentifier.identifier}</td>
			</tr>
			<tr>
				<td class='info'>Gender</td>
				<td><c:choose>
						<c:when test="${patient.gender eq 'M'}">
							<img src="${pageContext.request.contextPath}/images/male.gif"/>
						</c:when>
						<c:otherwise><img src="${pageContext.request.contextPath}/images/female.gif"/></c:otherwise>
					</c:choose></td>
				<td></td>
				<td class='info'>Age</td>
				<td>${patient.age}</td>
			</tr>							
		</table>
		
	</center>
	<div style="margin: 25px;">
		<b>Date of sample</b> <input id="date" name="date" value="${currentDate}" style="text-align:right;"/><br/>
		<b>Confidential tests</b><br/>			
		<div style="margin: 15px;">
			<c:forEach var='test' items='${confidentialTests}'>
				<input type="checkbox" name="confidentialTests" value="${test.conceptId}" />&nbsp;&nbsp;${test.name.name}<br/>
			</c:forEach>
		</div>
		<center>				
			<input type='button' value='Save' onClick='save();'/>
			<input type='button' value='Cancel' onClick='tb_remove();'/>
		</center>			
	</div>
	
</form>

