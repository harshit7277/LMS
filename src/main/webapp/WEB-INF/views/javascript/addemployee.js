$(document).ready(function () {
  var token = localStorage.getItem("token");
  $.ajax({
    url: 'http://localhost:8086/LMS/employees/details',
    type: 'GET',
    headers: { "Authorization": 'Bearer ' + token },
    success: function (response) {
      for (var i = 0; i < response.length; i++) {
        if (response[i].designation === "Lead") {
          $('#leadassign').append("<option value=" + response[i].id + ">" + response[i].username + "</option>");
        }
        else if (response[i].designation === "Manager") {
          $('#managerassign').append("<option value=" + response[i].id + ">" + response[i].username + "</option>");
        }
      }
    },
    error: function (xhr) {
      alert(xhr.responseText);
    }
  });
  $('#designation').change(function () {
    selection = document.getElementById("designation").value;
    switch (selection) {
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
  $("#button").click(function () {
    var userId = document.getElementById("id").value;
    var firstname = document.getElementById("firstname").value;
    var lastname = document.getElementById("lastname").value;
    var userName = document.getElementById("username").value;
    var designation = document.getElementById("designation").value;
    var gender = document.getElementById("gender").value;
    var assignLeadId = document.getElementById("leadassign").value;
    var assignManagerId = document.getElementById("managerassign").value;

    if (userId === "" || userName === "" || designation === "" || firstname === "" || lastname === "") {
      window.alert("please enter the details");
    }
    else {
      if (designation=== "Executive" && assignLeadId === "head") {
    	  window.alert("please enter user Lead");
      }
      else if (designation=== "Executive" && assignManagerId === "head") {
    	  window.alert("please enter user Manager");
      }
      else if (designation=== "Lead" && assignManagerId === "head") {
    	  window.alert("please enter user Manager");
      }
      else {
        var employee = { firstname: firstname, lastname: lastname, id: userId, designation: designation, username: userName, gender: gender, assignLeadId: assignLeadId, assignManagerId: assignManagerId }
        $.ajax({
          url: "http://localhost:8086/LMS/manageEmployees/add",
          type: "POST",
          contentType: 'application/json; charset=utf-8',
          dataType: 'json',
          headers: { "Authorization": 'Bearer ' + token },
          data: JSON.stringify(employee),
          success: function (response) {
            window.alert(response);
            location.reload(true);
          },
          error: function (xhr) {
            alert(xhr.responseText);
          }
        });
      }
    }
  });
});