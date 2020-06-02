$(document).ready(function() {
	var token = localStorage.getItem("token");
		 $.ajax({
			url: 'http://localhost:8086/LMS/employees/profile',
			type: 'GET',
			headers: {"Authorization": 'Bearer ' + token },
			success: function(response){
				if (response.designation == "Lead") {
					document.getElementById("assignlead").style.display = 'none';
					document.getElementById("lead").style.display = 'none';
				}
				else if (response.designation == "Manager") {
					document.getElementById("assignlead").style.display = 'none';
					document.getElementById("assignmanager").style.display = 'none';
					document.getElementById("lead").style.display = 'none';
					document.getElementById("manager").style.display = 'none';
				}
				document.getElementById("userid").value=response.id;
				document.getElementById("name").value=response.username;
				document.getElementById("useremailid").value=response.email;
				document.getElementById("userdesignation").value=response.designation;
				var leadid = response.assignLeadId;
				var managerid = response.assignManagerId;
				 $.ajax({
						url: 'http://localhost:8086/LMS/employees/details',
						type: 'GET',
						headers: {"Authorization": 'Bearer ' + token },
						success: function(response){
							for (var i = 0; i < response.length; i++) {						
									if(response[i].id == leadid ){
										document.getElementById("assignlead").value=response[i].username;
									}
									if(response[i].id == managerid ){
										document.getElementById("assignmanager").value=response[i].username;
									}
							}
					    },
					    error: function(xhr){
					    	alert(xhr.responseText);
					    }
				 });
			},
			error: function(xhr){
			    	alert(xhr.responseText);
			}
		 });
});