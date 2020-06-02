var token;
$(document).ready(function() {
	$.ajax({
			url : 'http://localhost:8086/LMS/employees/token',
			type : 'GET',
			success: function(response){
				token = response;
				localStorage.setItem("token",token);
				profileCall();
	  		},
	  		error: function(xhr){
	  			alert(xhr.responseText);
	        }
	});
});

function profileCall(){
	$.ajax({
		url : 'http://localhost:8086/LMS/employees/profile',
		type : 'GET',
		headers: {"Authorization": 'Bearer ' + localStorage.getItem("token") },
		success: function(response){
			console.log(response.username);
			$("#user").text(""+response.username);
  		},
  		error: function(xhr){
  			alert(xhr.responseText);
        }
	});
}