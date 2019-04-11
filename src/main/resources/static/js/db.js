window.onbeforeunload = function() {
	sessionStorage.clear();
};

var key_table = 'key_table';
var key_column = 'key_column';
var key_index = 'key_index';
var key_constraint = 'key_constraint';

$(function() {
	getDatabases();
	getInfo();
	deleteFile();
	getFileNames();
	uploadSubmit();
	$("#migrate").click(function() {
		migrate();
	});
	/* 同步表结构的弹窗 */
	startConsSync();

});

function getDatabases() {

	$.get("/databasesNames", function(obj) {
		for (var i = 0; i < obj.length; i++) {
			var sub = obj[i];
			var option = "<option value=\"" + sub + "\">" + sub + "</option>"
			$('#databases').append(option);
		}
	});

	$.get("/currentDatabaseName", function(obj) {
		$('#databases option').each(function() {
			// 遍历所有option
			var value = $(this).val(); // 获取option值
			if (value == obj) {
				$('#databases').val(obj);
			}
		});
	});
	$('#databases').change(function() {
		var setDb = $(this).val();
		$.get("/setDatabaseName/" + setDb, function(obj) {
			if (obj.code == 200) {
				alert("设置成功！ 数据库设置为" + obj.obj);
				location.reload();
			} else {
				alert("设置失败！" + obj.message);
			}
		});
	});
}

/**
 * 获取信息；
 */
function getInfo() {

	$.get("/db/info", function(obj) {
		var code = obj.code;
		if (code == 200) {
			var migrationInfos = obj.obj;
			if (migrationInfos.length != 0) {
				for (var i = 0; i < migrationInfos.length; i++) {
					var script = migrationInfos[i].script ; 
					var state = migrationInfos[i].state;
					var tr = "<tr>" + "<td>"
							+ migrationInfos[i].version + "</td><td>"
							+ migrationInfos[i].checksum + "</td><td>"
							+ migrationInfos[i].type + "</td><td>"
							+ script + "</td>"
							+ "<td>" + state
							+ "</td><td>"
							+ migrationInfos[i].installedOn
							+ "</td><td>"
							+ migrationInfos[i].executionTime
							+ "</td><td>"
							+ migrationInfos[i].description + "</td>" ; 
					if(state =='Baselin' || state =='Success' ){
						tr += '<td></td>';
					}else{
						tr += '<td><a class="delete_file" data-script="'+script+'"  href="javascript:void(0);">删除</a></td>';
					}
					tr += "</tr>";

					$("#getInfo").append(tr);
				}

			}

		} else {
			alert(obj.message);
		}
	});
}
/**
 * 删除文件
 */
function deleteFile(){
	$('body').on('click','.delete_file', function(e){
		if(!confirm("确定要删除该文件吗？")){
			e.preventDefault();
			return false; 
		}
		var script = $(this).attr('data-script'); 
		$.ajaxSettings.async = false;
		$.get('/db/deleteFile/'+script+'/', function(obj){
			if(obj.code == 200 ){
				alert('删除成功！');
			}else{
				alert('删除失败！');
			}
		}); 
		$.ajaxSettings.async = true;
		location.reload();
	});
}

/**
 * 获取文件名s
 */
function getFileNames() {
	$.get("/db/filenames/sql", function(obj) {
		var code = obj.code;
		if (code == 200) {
			var filenames = obj.obj;
			if (filenames.length != 0) {
				for (var i = 0; i < filenames.length; i++) {
					var tr = "<dd style=\" text-align: left; width: 210px; float: left;\" > <span style=\"width: 160px; display: inline-block;\"> "
							+ filenames[i]
							+ "</span>   <a href='/db/download/unauth/sql/"
							+ filenames[i] + "/'>下载</a></dd>";
					$("#getFileNames").append(tr);
				}
			}

		} else {
			alert(obj.message);
		}
	});
}

/**
 * ajax 上传文件
 * 
 */

function ajaxSubmitForm() {
	var formData = new FormData();
	formData.append("file", document.getElementById("sqlFile").files[0]);
	$.ajax({
		url : "/db/upload/sql",
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
				alert("上传成功！");
				location.reload();
			} else {
				alert(data.message);
			}

		},
		error : function(data) {
			alert(data.message);
		}
	});
}

/**
 * 迁移
 */
function migrate() {
	$.get("/db/migrate", function(obj) {
		if (200 == obj.code) {
			if (obj.obj > 0) {
				alert("迁移成功！");
				location.reload();
			} else {
				alert("无须迁移！");
			}
		} else {
			alert(obj.message);
		}

	});
}
/**
 * 上传
 * 
 * @returns
 */
function uploadSubmit() {
	$("#uploadSubmit").click(function() {
		if (window.confirm('你确定要上传吗？')) {
			ajaxSubmitForm();
			return false;
		} else {
			// alert("取消");
			return false;
		}
	});
}
/**
 * 同步表结构
 * 
 * @returns
 */
function startConsSync() {
	// 弹窗
	$('#startConsSync').click(function() {
		$.get('/dbsync/syncFramework', function(obj) {
			if (obj.code != 200) {
				alert("同步表结构失败！");
				return false;
			} else {
				$('#alert').show();
				$('#mask').show();
				// 准备数据
				consSyncAlert();
			}
		});
	});
}

/**
 * 同步表结构弹窗里面的其他按钮
 */
function consSyncAlert() {
	// 关闭
	$('#close').click(function() {
		for (var i = 1; i <= 4; i++) {
			var num = $('.sourceTables' + i).size();
			if (num > 0) {
				alert("未完成的记录存在！");
				return false;
			}
		}
		$('#alert').hide();
		$('#mask').hide();
		location.reload();
	});
	// 完成
	$('#btnok').click(function() {
		saveConstraints();
		$('#close').trigger('click');
	});
	//绑定table 事件
	dbTables();
	flushSourceTables();
	flushTargetTables();
	dbColumns();
	dbIndexs();
	dbConstraints();
}
/** 保存数据 **/
function saveTablesAndFlushColumnsInfo(){
	// 将数据，更新到 数据库的状态中，  
	var tables = sessionStorage.getItem(key_table);
	$.ajaxSettings.async = false;
	if (tables != null) {
		$.ajax({
            type: "POST",
            url: "/dbsync/readyTables",
            contentType: 'application/json;charset=utf-8',
            data: tables,
            dataType: 'json',
            success: function (obj) {
            	if(obj.code == 200){
    				console.log("readyTables： "+obj);
    				
    			}else{
    				alert("保存失败");
    			}
            }
		});
	}
	$.ajaxSettings.async = true; 
	flushSourceColumns();
	flushTargetColumns();
}
/**
 * 保存列
 * @returns
 */
function saveColumnsAndFlushIndexsInfo(){
	// 将数据，更新到 数据库的状态中，  
	var columns = sessionStorage.getItem(key_column);
	$.ajaxSettings.async = false;
	if (columns != null) {
		$.ajax({
            type: "POST",
            url: "/dbsync/readyColumns",
            contentType: 'application/json;charset=utf-8',
            data: columns,
            dataType: 'json',
            success: function (obj) {
            	if(obj.code == 200){
    				console.log("readyColumns： "+obj);
    			}else{
    				alert("保存失败");
    			}
            }
		});
	}
	$.ajaxSettings.async = true; 
	flushSourceIndexs();
	flushTargetIndexs();
}

/**
 * 保存索引
 * @returns
 */
function saveIndexsAndFlushConstraintsInfo(){
	// 将数据，更新到 数据库的状态中，  
	var indexs = sessionStorage.getItem(key_index);
	$.ajaxSettings.async = false;
	if (indexs != null) {
		$.ajax({
            type: "POST",
            url: "/dbsync/readyIndexs",
            contentType: 'application/json;charset=utf-8',
            data: indexs,
            dataType: 'json',
            success: function (obj) {
            	if(obj.code == 200){
    				console.log("readyIndexs： "+obj);
    			}else{
    				alert("保存失败");
    			}
            }
		});
	}
	$.ajaxSettings.async = true; 
	flushSourceConstraints();
	flushTargetConstraints();
}

/**
 * 保存约束
 * @returns
 */
function saveConstraints(){
	// 将数据，更新到 数据库的状态中，  
	var indexs = sessionStorage.getItem(key_constraint);
	$.ajaxSettings.async = false;
	if (indexs != null) {
		$.ajax({
            type: "POST",
            url: "/dbsync/readyConstraints",
            contentType: 'application/json;charset=utf-8',
            data: indexs,
            dataType: 'json',
            success: function (obj) {
            	if(obj.code == 200){
    				console.log("readyConstraints： "+obj);
    			}else{
    				alert("保存失败");
    			}
            }
		});
	}
	$.ajaxSettings.async = true; 
}




