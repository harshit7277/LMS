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
		$("#button").click(function() {
			var oldpassword = document.getElementById("userpassword").value;
			var newpassword = document.getElementById("userupdatepassword").value; 
		    $.ajax({
		    	url:'http://localhost:8086/LMS/manageEmployees/updatePassword',
		    	type: 'PUT',
		    	headers: {"Authorization": 'Bearer ' + token },
		    	data:{oldpassword:oldpassword,newpassword:newpassword},
	            success : function(response) {
	            	window.alert(response);
	            	location.reload(true);
	            },
	            error: function(xhr){
	            	alert(xhr.responseText);
			    }
		    });
		});
});