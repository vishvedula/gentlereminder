<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- <script type="text/javascript" language="javascript" src="http://www.technicalkeeda.com/js/javascripts/plugin/jquery.js"></script> -->
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<!--Data Table-->
    <script type="text/javascript"  src=" https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript"  src=" https://cdn.datatables.net/buttons/1.2.4/js/dataTables.buttons.min.js"></script>

<script type="text/javascript" src="http://www.technicalkeeda.com/js/javascripts/plugin/json2.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<title>Gentle Reminder with Ajax</title>
 <h1>Gentle Reminder with Ajax </h1>
<style>
Table.GridOne {
	padding: 3px;
	margin: 0;
	background: lightyellow;
	border-collapse: collapse;	
	width:35%;
}
Table.GridOne Td {	
	padding:2px;
	border: 1px solid #ff9900;
	border-collapse: collapse;
}
</style>
<script type="text/javascript">
function addAjaxCall(){
	$.ajax({
		type: "post",
		url: "http://localhost:8080/GentleReminder/reminder/add.htm",
		cache: false,				
		data:'addReminder=' + $("#addReminder").val(),
		success: function(response){
			$('#result').html("");
			var obj = JSON.parse(response);
			$('#result').html("Added Reminder is:- " + obj.reminder);
		},
		error: function(){						
			alert('Error while request..');
		}
	});
}

 function fetchAjaxCall() {
		$.ajax({
			type: "get",
			url: "http://localhost:8080/GentleReminder/reminder/fetch.htm",
			cache: false,				
			//data:'fetchReminder=' + $("#fetchReminder").val(),
			success: function(response){
				$('#result').html("");
				    var rowData;
			        var rowHTML;
			        var tableHTML = "<table>";
			        for (var i = response.length - 1; i >= 0; i--) {
			            rowData = response[i];
			            rowHTML = "<tr>";
			            rowHTML += "<td>" + rowData + "</td>";
			            rowHTML += "</tr>";
			            tableHTML += rowHTML;
			        }

			        tableHTML += "</table>";

			        $('#result').append(tableHTML);
			},
			error: function(){						
				alert('Error while request..');
			}
		});
	} 

function deleteAjaxCall() {
	$.ajax({
		type: "post",
		url: "http://localhost:8080/GentleReminder/reminder/delete.htm",
		cache: false,				
		data:'deleteReminder=' + $("#deleteReminder").val(),
		success: function(response){
			$('#result').html("");
			//var obj = JSON.parse(response);
			$('#result').html("The Deleted Reminder is:- " + response);
		},
		error: function(){						
			alert('Error while request..');
		}
	});
}

function updateAjaxCall() {
	$.ajax({
		type: "post",
		url : "http://localhost:8080/GentleReminder/reminder/update.htm",
		cache: false,
		data: 'oldReminder=' +$("#oldReminder").val() + "&newReminder=" +$("#newReminder").val(),
		success: function(response){
			$('#result').html("");
			//var obj = JSON.parse(response);
			$('#result').html("Reminder updated successfully");
		},
		error: function(){
			alert('Could not update the reminder');
		}
	});
}

/* To refresh the page */
function refreshPage() {
	window.location.reload();
}
</script>

</head>
<body>
	<form name="employeeForm" method="post">	
		<table cellpadding="0" cellspacing="0" border="1" class="GridOne">
			<tr>
				<td>Reminder</td>
				<td><input type="text" name="addReminder" id="addReminder" value=""></td>
				<!-- <td colspan="2" align="center"><input type="button" class="btn btn-primary" value="Add Reminder" onclick="addAjaxCall();"></td> -->
				<td align="center"><input type="button" class="btn btn-primary" value="Add Reminder" onclick="addAjaxCall();"></td>
			</tr>
			<tr>
				<td>Fetch Reminder</td>
				<td  align="center"><input type="button" class="btn btn-primary" value="Fetch Reminder" onclick="fetchAjaxCall();"></td>
			</tr>
			<tr>
				<td>Delete Reminder</td>
				<td><input type="text" name="deleteReminder" id="deleteReminder" value=""></td>
				<td  align="center"><input type="button" class="btn btn-primary" value="Delete Reminder" onclick="deleteAjaxCall();"></td>
			</tr>

			<tr>
				<td>Old Reminder</td>
				<td><input type="text" name="oldReminder" id="oldReminder" value=""></td>
				
				<td>New Reminder</td>
				<td><input type="text" name="newReminder" id="newReminder" value=""></td>
				
				<td colspan="2" align="center"><input type="button" class="btn btn-primary" value="Update Reminder" onclick="updateAjaxCall();"></td>
			</tr>
		</table>
			<tr>
			<br>
			<td colspan="2" align="center"><input type="button" class ="btn btn-primary" value="Refresh" onclick = "refreshPage()"/></td>
			</br>
			</tr>
			
			<tr>
			<br>
			<td colspan="2" align="center"><input type="button" class ="btn btn-primary" value="JobSchedule" onclick = "refreshPage()"/></td>
			</br>
			</tr>
	</form>
	<!-- <table cellpadding="1" cellspacing="1" border="1" > -->
		<div id="result"></div>
	<!-- </table> -->
</body>
</html>