<script type="text/javascript">
	jQuery(document).ready(function(){		
		jQuery("#patientId").val(MODEL.patientId);
		jQuery("#revisit").val(MODEL.revisit);
		jQuery("#identifier").html(MODEL.patientIdentifier);
		jQuery("#age").html(MODEL.patientAge);
		jQuery("#name").html(MODEL.patientName);
		jQuery("#category").html(MODEL.patientAttributes[14]);
		jQuery("#gender").html(MODEL.patientGender);
		jQuery("#datetime").html(MODEL.currentDateTime);
		PAGE.fillOptions("#opdWard", {
			data:MODEL.OPDs,
			delimiter: ",",
			optionDelimiter: "|"
		});
		if(MODEL.dueDate.length>0){
			jQuery("#buySlip").hide();
			
			value = "Validate until " + MODEL.dueDate;
			if(parseInt(MODEL.daysLeft)>1){
				value += " (" + MODEL.daysLeft + " days left).";				
			} else {
				value += " (1 day left).";
			}
			jQuery("#validationDate").html(value);
				
		} else {
			jQuery("#printSlip").hide();
		}
	});
	
	/**
	 ** PAGE METHODS
	 **/
	PAGE = {
		/** Validate and submit */
		submit: function(){
			
			// change OPDWard dropdown to printable format
			jQuery("#opdWard").hide();
			jQuery("#opdWard").after("<span>" + jQuery("#opdWard option:checked").html() +  "</span>");			
			
			// submit form and print
			jQuery("#patientInfoForm").ajaxSubmit({
				success: function (responseText, statusText, xhr){
					if(responseText=="success"){						
						jQuery("#patientInfoPrintArea").printArea({
							mode : "popup",
							popClose : true
						});
						window.location.href = getContextPath() + "/findPatient.htm";
					}					
				}
			});
		},
		
		/** FILL OPTIONS INTO SELECT 
		 * option = {
		 * 		data: list of values or string
		 *		index: list of corresponding indexes
		 *		delimiter: seperator for value and label
		 *		optionDelimiter: seperator for options
		 * }
		 */
		fillOptions: function(divId, option) {
			jQuery(divId).empty(); 
			if(option.delimiter == undefined){
				if(option.index == undefined){
					jQuery.each(option.data, function(index, value){	
						if(value.length>0){
							jQuery(divId).append("<option value='" + value + "'>" + value + "</option>");
						}
					});				
				} else {
					jQuery.each(option.data, function(index, value){	
						if(value.length>0){
							jQuery(divId).append("<option value='" + option.index[index] + "'>" + value + "</option>");
						}
					});
				}
			} else {
				options = option.data.split(option.optionDelimiter);
				jQuery.each(options, function(index, value){
					values = value.split(option.delimiter);
					optionValue = values[0];
					optionLabel = values[1];
					if(optionLabel != undefined){
						if(optionLabel.length>0){
							jQuery(divId).append("<option value='" + optionValue + "'>" + optionLabel + "</option>");
						}
					}					
				});
			}
		},
		
		/** Buy A New Slip */		
		buySlip: function(){
			jQuery.ajax({
				type : "GET",
				url : openmrsContextPath + "/module/registration/ajax/buySlip.htm",
				data : ({
					patientId: MODEL.patientId
				}),				
				success : function(data) {										
					window.location.href = window.location.href;
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert(thrownError);
				}
			});
		}
	};
</script>
<input id="printSlip" type="button" value="Print" onClick="PAGE.submit();"/>
<input id="buySlip" type="button" value="Buy a new slip" onClick="PAGE.buySlip();"/>
<span id="validationDate"></span>
<div id="patientInfoPrintArea">
	<center>
		<img src="${pageContext.request.contextPath}/moduleResources/registration/header.jpg"/>
		<form id="patientInfoForm" method="POST">	
			<table cellspacing="10">
				<tr>
					<td><b>ID.NO:</b></td>
					<td>
						<span id="identifier"/>
					</td>
					<td></td>
					<td></td>
					<td><b>Age:</b></td>
					<td>
						<span id="age"/>
					</td>
				</tr>
				<tr>
					<td><b>Name:</b></td>
					<td colspan="5">
						<span id="name"/>
					</td>
				</tr>
				<tr>
					<td><b>OPD room to visit:</b></td>
					<td colspan="5">
						<select id="opdWard" name="patient.opdWard">
						</select>
					</td>
				</tr>
				<tr>
					<td><b>Category:</b></td>
					<td>
						<span id="category"/>
					</td>
					<td><b>Gender:</b></td>
					<td>
						<span id="gender"/>
					</td>
					<td><b>Date/Time:</b></td>
					<td>
						<span id="datetime"/>
					</td>
				</tr>
				<tr>
					<td valign="top"><b>Temporary Categories:</b></td>
					<td colspan="5">
						<input type="checkbox" name="temporary.attribute.7100" value="MLC"/> MLC <br/>
					</td>
				</tr>
			</table>
		</form>
	</center>	
	<img src="${pageContext.request.contextPath}/moduleResources/registration/investigations.jpg"/>
</div>
