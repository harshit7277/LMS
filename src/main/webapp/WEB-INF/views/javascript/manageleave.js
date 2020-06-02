$(document).ready(function() {
	var token = localStorage.getItem("token");
	var options = { day: 'numeric', month: 'long', year: 'numeric' };
				$.ajax({
				url : 'http://localhost:8086/LMS/employees/profile',
				type : 'GET',
				headers: {"Authorization": 'Bearer ' + token },
				success: function(response){
					$("#user").text(""+response.username);
				},
				error: function(xhr){
					alert(xhr.responseText);
				}
				});
				$.ajax({
				url : 'http://localhost:8086/LMS/leaves/details',
				type : 'GET',
				headers: {"Authorization": 'Bearer ' + token },
				success : function(response) {
							for (var i = 0; i < response.length; i++) {
								var leaveFrom = new Date(Number(response[i].leaveFrom));
								var leaveTill = new Date(Number(response[i].leaveTill));
								$("#leavestatus").append(
												"<tr><td class='nr'><span>"
														+ response[i].leaveId
														+ "</span></td><td>"
														+ response[i].username
														+ "</td><td>"
														+ response[i].leavetype
														+ "</td><td>"
														+ leaveFrom.toLocaleDateString("en-US",options)
														+ "</td><td>"
														+ leaveTill.toLocaleDateString("en-US",options)
														+ "</td><td><input type='submit' class='approve-button' value='APPROVE'></td>"
														+ "<td><input type='submit' class='reject-button' value='REJECT'></td></tr>");
							}
						    $(".approve-button").click(function() {
							var $row = $(this).closest("tr");   
							var $id = $row.find(".nr").text(); 	    
								$.ajax({
									   	url:'http://localhost:8086/LMS/manageLeaves/approve',
									    data:{leaveid: $id},
									    type:"POST",
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
						    $(".reject-button").click(function() {
								var $row = $(this).closest("tr");    
								var $id = $row.find(".nr").text(); 	    
									$.ajax({
										   	url:'http://localhost:8086/LMS/manageLeaves/reject',
										    data:{leaveid: $id},
										    type:"POST",
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