 $(document).ready(function() {
	 var token = localStorage.getItem("token");
		 $.ajax({
				url: 'http://localhost:8086/LMS/employees/details',
				type: 'GET',
				 headers: {"Authorization": 'Bearer ' + token },
				success: function(response){
					for (var i = 0; i < response.length; i++) {						
							 $('#employeeid').append("<option value="+response[i].id+">"+response[i].username+"</option>"); 
							 if(response[i].designation === "Lead"){
								 $('#leadassign').append("<option value="+response[i].id+">"+response[i].username+"</option>"); 
							}
							else if(response[i].designation === "Manager"){
								 $('#managerassign').append("<option value="+response[i].id+">"+response[i].username+"</option>"); 
							}
					}
			    },
			    error: function(xhr){
			    	alert(xhr.responseText);
			    }
		 });
		 $('#employeeid').change(function(){
				selection = document.getElementById("employeeid").value;
				 $.ajax({
						url: 'http://localhost:8086/LMS/employees/userid',
						type: 'POST',
						headers: {"Authorization": 'Bearer ' + token },
						data: {id:selection},
						success: function(response){
							document.getElementById("userid").value= response.id;
							document.getElementById("username").value= response.username;
							document.getElementById("useremailid").value= response.email;
							document.getElementById("designation").value= response.designation;
							if (response.designation == "Executive") {
								$('#leadassign').show();
								 $('#managerassign').show();
							}
							else if(response.designation == "Lead") {
								 $('#leadassign').hide();
								 $('#managerassign').show();
							}
							else if(response.designation=="Manager"){
								 $('#leadassign').hide();
								 $('#managerassign').hide();
							}
					    },
					    error: function(xhr){
					    	alert(xhr.responseText);
					    }
				 });
				
			});
		 $('#designation').change(function(){
				selection = document.getElementById("designation").value;
				switch(selection)
				{
				case 'Executive':
					 $('#leadassign').show();
					 $('#managerassign').show();
					 break;
				case 'Lead':
					 $('#leadassign').hide();
					 $('#managerassign').show();
					 break;
				case 'Manager':
					 $('#leadassign').hide();
					 $('#managerassign').hide();
					 break;
				}
			});
		 $("#button").click(function(){
			 var id = document.getElementById("userid").value;
			 var designation = document.getElementById("designation").value;
			 var assignLeadId = document.getElementById("leadassign").value;
			 var assignManagerId = document.getElementById("managerassign").value;
			
			 if(id=="" || designation=="" || assignLeadId=="" || assignManagerId==""){
				window.alert("please enter the details"); 
			 }
			 else{		 
				 var employee = {id:id,designation:designation,assignLeadId:assignLeadId,assignManagerId:assignManagerId};
				 $.ajax({
						url: "http://localhost:8086/LMS/manageEmployees/update",
						type: "PUT",
						contentType : 'application/json; charset=utf-8',
					    dataType : 'json',
						headers: {"Authorization": 'Bearer ' + token },
						data: JSON.stringify(employee),
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