 $(document).ready(function() {	
	 var token = localStorage.getItem("token");
	 $.ajax({
				url: 'http://localhost:8086/LMS/employees/details',
				type: 'GET',
				headers: {"Authorization": 'Bearer ' + token },
				success : function(response) {
					for (var i = 0; i < response.length; i++) {
						$("#employeedetails").append(
										"<tr><td class='nr'><span>"
												+ response[i].id
												+ "</span></td><td>"
												+ response[i].username
												+ "</td><td>"
												+ response[i].email
												+ "</td><td>"
												+ response[i].designation
												+ "</td><td>"
												+ response[i].gender
												+ "</td><td><input type='submit' class='delete-button' value='DELETE'></td></tr>");
	
					}
				    $(".delete-button").click(function() {
				    	var $row = $(this).closest("tr");   
						var $employeeId = $row.find(".nr").text(); 	    
							$.ajax({
								   	url:'http://localhost:8086/LMS/manageEmployees/delete',
								    data:{id: $employeeId},
								    type:"DELETE",
								    headers: {"Authorization": 'Bearer ' + token },
							        success : function(response) {
							            	window.alert(response);
							            	location.reload(true);
							        },
							        error: function(xhr){
							        	alert(xhr.responseText);
							        }
							});
					});
				},
				error: function(xhr){
					alert(xhr.responseText);
		        }
	});
		
});