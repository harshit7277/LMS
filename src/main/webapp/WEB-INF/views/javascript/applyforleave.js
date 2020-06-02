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
		 $("#button").click(function(){
			 var leaveFrom = document.getElementById("applyforleave").value;
			 var leavetype = document.getElementById("leavetype").value;
			 var leavenumber = document.getElementById("leavenumber").value;
			
			 if(leaveFrom=="" || leavetype=="" || leavenumber==""){
				window.alert("please enter the details"); 
			 }
			 else{		 
			 if(leavenumber<1){
				 window.alert("Enter leave number properly ");
			 }
			 else{
			 $.ajax({
					url: "http://localhost:8086/LMS/manageLeaves/leaveRequest",
					type: "POST",
					headers: {"Authorization": 'Bearer ' + token },
					data: {leaveFrom:leaveFrom,leavetype:leavetype,leavenumber:leavenumber},
					beforeSend: function(){
					    $("#wait").css("display", "block");
				    },
				    complete: function(){
				    	$("#wait").css("display", "none");
				    },
			        success: function(response){
			        	window.alert(response);
			        	location.reload(true);
					},
					error: function(xhr){
		    	        alert(xhr.responseText);
					}	
				 });
			 }
			 } 
		 });
	 });