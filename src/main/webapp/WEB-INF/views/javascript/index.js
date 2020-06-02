$(document).ready(function() {
		$("#button").click(function() {
			var username = document.getElementById("username").value;
			var password = document.getElementById("password").value;
			if (username === "" || password === "") {
				window.alert("please enter the details");
			}
			else{
				$.ajax({
					url: "/LMS/login",
					type: "POST",
					data: {username:username,password:password},
			        success: function(response){
			        	console.log(response);
			        	if (response == "Executive") {
							location.href = "executive.html";
						}
			        	else if(response == "Lead"){
			        		location.href = "lead.html";
			        	}
			        	else if(response == "Manager"){
			        		location.href = "manager.html";
			        	}
			        	else if(response == "admin"){
			        		location.href = "adminscreen.html";
			        	}
			        	else {
			        		window.alert(response);
			        	}
					},
					error: function(xhr){
						alert(xhr.responseText);
			        }
				 });
			}
		});
});