$(function(){
	
	$('#btn-login').click(function(){
		loginSubmitForm();
		return false; 
	})
});
function loginSubmitForm() {
	var formData = new FormData();
	formData.append("username", $('#username').val());
	formData.append("password", $('#password').val()); 
	$.ajax({
		url : "/toIndex",
		type : "POST",
		data : formData,
		/**
		 * 必须false才会自动加上正确的Content-Type
		 */
		contentType : false,
		/**
		 * 必须false才会避开jQuery对 formdata 的默认处理 XMLHttpRequest会对 formdata 进行正确的处理
		 */
		processData : false,
		success : function(data) {
			if (data.code == 200) {
				alert("登陆成功！");
				location.href="/";
			} else {
				alert(data.message);
			}

		},
		error : function(data) {
			alert(data.message);
		}
	});
}
