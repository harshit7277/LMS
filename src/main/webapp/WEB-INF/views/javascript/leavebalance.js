$(document).ready(function() {
	var token = localStorage.getItem("token");
		$.ajax({
				url: 'http://localhost:8086/LMS/employees/profile',
				type: 'GET',
				headers: {"Authorization": 'Bearer ' + token },
				success: function(response){
					$("#user").text(""+response.username);
				},
				error: function(xhr){
					alert(xhr.responseText);
				}
		});
		$.ajax({
				url: 'http://localhost:8086/LMS/leaves/leaveBalance',
				type: 'GET',
				headers: {"Authorization": 'Bearer ' + token },
				success: function(response){	
					console.log(response);
					$("#leavebalance").append("<tr><td>"+response.casualLeave+"</td><td>"+response.dutyLeave+"</td><td>"+response.earnLeave+"</td><td>"+response.sickLeave+"</td><td>"+response.parentalLeave+"</td><td>"+response.maternityLeave+"</td><td>"+response.leavewithoutpay+"</td></tr>");
				},
				error: function(xhr){
					alert(xhr.responseText);
		        }
		});
});