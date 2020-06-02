 $(document).ready(function() {
	 var token = localStorage.getItem("token");
		 $.ajax({
				url: 'http://localhost:8086/LMS/employees/details',
				type: 'GET',
				 headers: {"Authorization": 'Bearer ' + token },
				success: function(response){
					for (var i = 0; i < response.length; i++) {						
							 $('#employeeid').append("<option value="+response[i].id+">"+response[i].username+"</option>"); 
					}
			    },
			    error: function(xhr){
			    	alert(xhr.responseText);
			    }
		 });
		 $('#employeeid').change(function(){
				selection = document.getElementById("employeeid").value;
				 $.ajax({
						url: 'http://localhost:8086/LMS/manageEmployees/leaveBalance',
						type: 'POST',
						headers: {"Authorization": 'Bearer ' + token },
						data: {id:selection},
						success: function(response){
							document.getElementById("casual").value= response.casualLeave;
							document.getElementById("earn").value= response.earnLeave;
							document.getElementById("duty").value= response.dutyLeave;
							document.getElementById("sick").value= response.sickLeave;
							document.getElementById("parental").value= response.parentalLeave;
							document.getElementById("maternity").value= response.maternityLeave;
							document.getElementById("leavewithoutpay").value= response.leavewithoutpay;
					    },
					    error: function(xhr){
					    	alert(xhr.responseText);
					    }
				 });
				
			});
		 $("#button").click(function(){
			 var employeeid = document.getElementById("employeeid").value;
			 var casualLeave = document.getElementById("casual").value;
			 var earnLeave = document.getElementById("earn").value;
			 var dutyLeave = document.getElementById("duty").value;
			 var sickLeave = document.getElementById("sick").value;
			 var parentalLeave = document.getElementById("parental").value;
			 var maternityLeave = document.getElementById("maternity").value;
			 var leavewithoutpay = document.getElementById("leavewithoutpay").value;
			
			 if(employeeid === "head"){
				window.alert("please select employee"); 
			 }
			 else if(casualLeave=="" || earnLeave=="" || dutyLeave=="" || sickLeave=="" || parentalLeave=="" || maternityLeave=="" || leavewithoutpay=="" ){
				window.alert("please enter the details"); 
			 }
			 else if(casualLeave <= 0 || earnLeave <= 0 || dutyLeave <= 0 || sickLeave <= 0 || parentalLeave <= 0 || maternityLeave <= 0 || leavewithoutpay <= 0 ){
					window.alert("please enter the details"); 
			 }
			 else{		 
				 var employeeLeaveBalance = {
							employeeid : employeeid,
							casualLeave : casualLeave,
							earnLeave : earnLeave,
							dutyLeave : dutyLeave,
							sickLeave : sickLeave,
							parentalLeave : parentalLeave,
							maternityLeave : maternityLeave,
							leavewithoutpay : leavewithoutpay
						};
															
				 $.ajax({
						url: "http://localhost:8086/LMS/manageLeaves/updateLeaveBalance",
						type: "PUT",
						contentType : 'application/json; charset=utf-8',
					    dataType : 'json',
						headers: {"Authorization": 'Bearer ' + token },
						data: JSON.stringify(employeeLeaveBalance),
				        success: function(response){
				        	window.alert(response);
				        	location.reload(true);
						},
						error: function(xhr){
							alert(xhr.responseText);
						}	
					 });
			 }
		 });
	 });