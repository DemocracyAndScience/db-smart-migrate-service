function dbTables(){
			/**  新增  **/
			$('body').on('click','.button_insert_table',
			function() {
				var tableName = $(this).parent().parent()
						.find('.sourceTables1').text();
				if (tableName != null && tableName != "") {
					var tables = sessionStorage.getItem(key_table);
					var tablesArr = [];
					if (tables != null) {
						tablesArr = JSON.parse(tables);
					}

					var table = {
						sourceTableName : tableName,
						targetTableName : '',
						method : 'ADD'
					};
					tablesArr.push(table);
					var tablesJson = JSON.stringify(tablesArr);
					sessionStorage.setItem(key_table, tablesJson);
					$(this).parent().parent().remove();
				}

			});
			// 表名更名
			$('body').on('click','.js_button_table',
				function() {
					var td = $(this).parent().next();
					$('#contA #targetTablesTable tbody').children('tr').remove();
					$.get('/dbsync/getTargetTables', function(obj){
						if(obj.code == 200 ){
							var targetTables = obj.obj ;
							var select = '<select>' ;
							var trs = '';
							for(var i =0 ; i < targetTables.length ; i++ ){
								var targetTableName = targetTables[i].targetTableName ; 
								var changeTables = getChangeTables();
								if( changeTables.indexOf(targetTableName) >= 0  ){
									continue;
								}
								var version = targetTables[i].version ; 
								var option = '<option value="'+targetTableName+'">'+targetTableName+'</option>'
								select += option ; 
								var tr = '<tr><td>'+version+'</td><td>'+targetTableName+'</td><td><button class="button_delete_table">删除</button></td></tr>';
								trs += tr ; 
							}
							select += '</select>';
							td.html(select);
							$('#contA #targetTablesTable tbody').append(trs);
						}else{
							alert('获取target表名信息出错！');
						}
					});
			});

			// 确定
			$('body').on('click', '.button_table',
			function() {
				var sd = $(this).parent().prev().children();
				var sdnum = sd.size();
				if (sdnum <= 0) {
					alert("未点击更名！");
					return false;
				}
				var value = sd.val();
				if(value == null || value == ''){
					alert("该列中没有信息！");
					return false;
				}
				var targetTableName = $(this).parent().parent().find('select')
						.val();
				var tableName = $(this).parent().parent()
						.find('.sourceTables1').text();
				if (tableName != null && tableName != ""
						&& targetTableName != null && targetTableName != "") {
					var tables = sessionStorage.getItem(key_table);
					var tablesArr = [];
					if (tables != null) {
						tablesArr = JSON.parse(tables);
					}
					var table = {
						sourceTableName : tableName,
						targetTableName : targetTableName,
						method : 'CHANGE'
					};
					tablesArr.push(table);
					var tablesJson = JSON.stringify(tablesArr);
					sessionStorage.setItem(key_table, tablesJson);
					$(this).parent().parent().remove();
				}
				$(this).parent().parent().remove();
				flushTargetTables();
			});
			/**  删除  **/
			$('body').on('click','.button_delete_table',
			function() {
				// 校验是否有sourceTables1  
				var num = $('.sourceTables1').size();
				if (num > 0) {
					alert("存在未处理的source表！");
					return false;
				}
				var targetTableName = $(this).parent().prev().text();
				if(confirm("确定要删除"+targetTableName+"吗？")){
					if ( targetTableName != null && targetTableName != "") {
						var tables = sessionStorage.getItem(key_table);
						var tablesArr = [];
						if (tables != null) {
							tablesArr = JSON.parse(tables);
						}
						var table = {
								sourceTableName : '',
								targetTableName : targetTableName,
								method : 'DELETE'
						};
						tablesArr.push(table);
						var tablesJson = JSON.stringify(tablesArr);
						sessionStorage.setItem(key_table, tablesJson);
						$(this).parent().parent().remove();
					}
				}
			});
}

function flushTargetTables (){
	$('#contA #targetTablesTable tbody').children('tr').remove();
		$.get('/dbsync/getTargetTables', function(obj){
			if(obj.code == 200 ){
				var targetTables = obj.obj ;
				var trs = '';
				for(var i =0 ; i < targetTables.length ; i++ ){
					var targetTableName = targetTables[i].targetTableName ;
					var changeTables = getChangeTables();
					
					if( changeTables.indexOf(targetTableName) >= 0  ){
						continue;
					}
					var version = targetTables[i].version ; 
					var tr = '<tr><td>'+version+'</td><td>'+targetTableName+'</td><td><button class="button_delete_table">删除</button></td></tr>';
					trs += tr ; 
				}
				$('#contA #targetTablesTable tbody').append(trs);
			}else{
				alert('获取target表名信息出错！');
			}
		});
}


function flushSourceTables (){
	$.get('/dbsync/getSourceTables',
		function(obj) {
			if (obj.code == 200) {
				var sourceTables = obj.obj;
				if(sourceTables != null){
					var trs = '';
					for (var i = 0; i < sourceTables.length; i++) {
						var sourceTable = sourceTables[i];
						var tr = '<tr>'
								+ '<td><span class="span" >'
								+ sourceTable.version
								+ '</span></td>'
								+ '<td><span class="span sourceTables1">'
								+ sourceTable.sourceTableName
								+ '</span></td>'
								+ '<td><button class="button_insert_table">新增</button></td>'
								+ '<td><button class="button js_button_table">更名</button></td>'
								+ '<td></td>'
								+ '<td><button class="button_table">确定</button></td>'
								+ '</tr>';
						trs += tr;
					}
				}
				
				$('#contA #sourceTablesTable').append(trs);
			} else {
				alert("获取冲突的表名失败");
			}
		});
}


/**
 * 获取 更改的表
 * @returns
 */
function getChangeTables(){
	var tables = sessionStorage.getItem(key_table);
	var tablesArr = [];
	if (tables != null) {
		tablesArr = JSON.parse(tables);
	}
	var changeTables = [];
	for(var i = 0 ; i < tablesArr.length ; i++ ){
		if( tablesArr[i].method == 'CHANGE'){
			
			changeTables.push(tablesArr[i].targetTableName);
		}
	}
	return changeTables ; 
}

	