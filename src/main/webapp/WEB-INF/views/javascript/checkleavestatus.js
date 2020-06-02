 $(document).ready(function() {
	 var token = localStorage.getItem("token");
	 var options = { day: 'numeric', month: 'long', year: 'numeric' };
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
				url: 'http://localhost:8086/LMS/leaves/leaveStatus',
				type: 'GET',
				headers: {"Authorization": 'Bearer ' + token },
				success : function(response) {
					for (var i = 0; i < response.length; i++) {
						var leaveFrom = new Date(Number(response[i].leaveFrom));
						var leaveTill = new Date(Number(response[i].leaveTill));
						$("#leavestatus").append(
										"<tr><td class='nr'><span>"
												+ response[i].leaveId
												+ "</span></td><td>"
												+ response[i].leavetype
												+ "</td><td>"
												+ leaveFrom.toLocaleDateString("en-US",options)
												+ "</td><td>"
												+ leaveTill.toLocaleDateString("en-US",options)
												+ "</td><td>"
												+ response[i].approval
												+ "</td><td><input type='submit' class='cancel-button' value='CANCEL'></td></tr>");
					}
				    $(".cancel-button").click(function() {
				    	var $row = $(this).closest("tr");   
						var $id = $row.find(".nr").text(); 	    
							$.ajax({
								   	url:"http://localhost:8086/LMS/manageLeaves/cancel",
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