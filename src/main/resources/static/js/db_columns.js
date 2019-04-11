function dbColumns(){
	/**  添加列  **/
	$('body').on('click','.button_insert_column',
	  function() {
		var columnName = $(this).parent().parent().find(
				'.sourceTables2').text();
		var tableName = $(this).parent().parent().parent().parent()
				.find('.tableName_column').text();
		if (columnName != null && columnName != "" && tableName != null
				&& tableName != "") {
			var columns = sessionStorage.getItem(key_column);
			var columnsArr = [];
			if (columns != null) {
				columnsArr = JSON.parse(columns);
			}
			var column = {
					tableName : tableName,
					sourceColumnName : columnName,
					targetColumnName : '',
					method : 'ADD'
			};
			
			columnsArr.push(column);
			var columnJson = JSON.stringify(columnsArr);
			sessionStorage.setItem(key_column, columnJson);
			$(this).parent().parent().remove();
		}
	});
	// 列名更名为 
	$('body').on('click','.js_button_column',
		function() {
			 var pre =  $(this).parent().parent().parent().prev();
			 var targetTableName =	pre.find('.tableName_column').text();
			var td = $(this).parent().next();
			if( targetTableName != null && targetTableName != ''){
				$.get('/dbsync/getTargetColumns/'+targetTableName, function(obj){
					if(obj.code == 200 ){
						var targetObj = obj.obj
						if(targetObj != null && targetObj.length > 0  ){
							var targetColumns   = targetObj[0].targetColumns ; 
							var select = '<select>' ;
							for(var i =0 ; i < targetColumns.length ; i++ ){
								var targetColumn = targetColumns[i] ; 
								
								var changeColumns = getChangeColumns();
								if( changeColumns.indexOf(targetColumn.targetTableName+'|'+ targetColumn.targetColumnName) >= 0  ){
									continue;
								}
								
								var option = '<option value="'+targetColumn.targetColumnName+'">'+targetColumn.targetTableName +" | "+ targetColumn.targetColumnName+'</option>'
								select += option ; 
							}
							select += '</select>';
							td.html(select);
							// 刷新右侧列表
							flushTargetColumns();
						}
						
					}else{
						alert('获取target表名信息出错！');
					}
				});
			}
			
	});
	
	// 确定
	$('body').on(
			'click',
			'.button_column',
	  function() {
		var sd = $(this).parent().prev().children();
		var sdnum = sd.size();
		if (sdnum <= 0) {
			alert("未点击更名为");
			return false;
		}
		var value = sd.val();
		if(value == null || value == ''){
			alert("该列中没有信息！");
			return false;
		}
		var columnName = $(this).parent().parent().find(
				'.sourceTables2').text();
		var tableName = $(this).parent().parent().parent().parent()
				.find('.tableName_column').text();
		var targetColumnName = $(this).parent().parent().find('select')
				.val();
		if (columnName != null && columnName != "" && tableName != null
				&& tableName != "" && targetColumnName != null
				&& targetColumnName != "") {
			var columns = sessionStorage.getItem(key_column);
			var columnsArr = [];
			if (columns != null) {
				columnsArr = JSON.parse(columns);
			}
			var column = {
					tableName : tableName,
					sourceColumnName : columnName,
					targetColumnName : targetColumnName,
					method : 'CHANGE'
			};
			columnsArr.push(column);
			var columnJson = JSON.stringify(columnsArr);
			sessionStorage.setItem(key_column, columnJson);
			$(this).parent().parent().remove();
			flushTargetColumns();
		}

		$(this).parent().parent().remove();
	});
	
	/**  删除  **/
	$('body').on('click','.button_delete_column',
	function() {
		// 校验是否有sourceTables1  
		var num = $('.sourceTables2').size();
		if (num > 0) {
			alert("存在未处理的source列！");
			return false;
		}
		var tbody = $(this).parent().parent().parent() ; 
		var targetTableName = tbody.prev().find('.tableName_column').text();
		var  targetColumnName = $(this).parent().prev().text();
		
		if(confirm("确定要删除"+targetTableName +"表的 " +targetColumnName+" 列吗？")){
			if ( targetTableName != null && targetTableName != "" && targetColumnName != null && targetColumnName != '') {
				var columns = sessionStorage.getItem(key_column);
				var columnsArr = [];
				if (columns != null) {
					columnsArr = JSON.parse(columns);
				}
				var column = {
						tableName : targetTableName,
						sourceColumnName : '',
						targetColumnName : targetColumnName,
						method : 'DELETE'
				};
				columnsArr.push(column);
				var columnsJson = JSON.stringify(columnsArr);
				sessionStorage.setItem(key_column, columnsJson);
				$(this).parent().parent().remove();
			}
		}
	});
}


function flushSourceColumns(){
	$.get('/dbsync/getSourceColumns',function(obj){
		if(obj.code == 200){
			var sourceColumns =  obj.obj ; 
			var tables = ''; 
			for(var i =0 ; i < sourceColumns.length ; i++ ){
				var sourceColumn = sourceColumns[i];
				var sourceColumnSubs =  sourceColumn.sourceColumns;
				var table = '<table class="">'   
						+	'<thead>'   
						+		'<tr>'   
						+			'<td>版本</td>'   
						+			'<td style="font-size: 14px; font-weight: 500"'   
						+				'class="tableName_column">'+sourceColumn.tableName+'</td>'   
						+			'<td></td>'   
						+			'<td></td>'   
						+			'<td style="font-size: 14px; font-weight: 500">target列名</td>'   
						+			'<td></td>' 
						+		'</tr>'   
						+	'</thead>'  ; 
				for(var j = 0 ; j< sourceColumnSubs.length; j ++){
					var sourceColumnSub = sourceColumnSubs[j];
					var tr = '<tr>'   
							+	'<td><span class="span ">'+sourceColumnSub.version+'</span></td>'   
							+	'<td><span class="span sourceTables2">'+sourceColumnSub.sourceColumnName+'</span></td>'   
							+	'<td><button class="button_insert_column">新增</button></td>'   
							+	'<td><button class="button js_button_column">更名为</button></td>'   
							+	'<td></td>'   	
							+	'<td><button class="button_column">确定</button></td>'   
							+ '</tr>'   ;
					table += tr ; 
					
				}
				table += '</table>'  ;
				tables += table ;
			}
			$('#contB').append(tables);
		}else{
			alert("获取列失败");
		}
	});
} 

function flushTargetColumns(){
	$.get('/dbsync/getTargetColumns/',function(obj){
		if(obj.code == 200){
			var targetColumns =  obj.obj ; 
			var tables = ''; 
			for(var i =0 ; i < targetColumns.length ; i++ ){
				var targetColumn = targetColumns[i];
				var targetColumnSubs =  targetColumn.targetColumns;
				var table = '<table >'   
						+	'<thead>'   
						+		'<tr>'   
						+			'<td style="font-size: 14px; font-weight: 500">版本</td>'   
						+			'<td colspan="2" style="font-size: 14px; font-weight: 500"'   
						+				'class="tableName_column">'+targetColumn.tableName+'</td>'   
						+		'</tr>'   
						+	'</thead>'  
						+	'</tbody>'; 
				for(var j = 0 ; j< targetColumnSubs.length; j ++){
					var targetColumnSub = targetColumnSubs[j];
					var changeColumns = getChangeColumns();
					if( changeColumns.indexOf(targetColumnSub.targetTableName+'|'+ targetColumnSub.targetColumnName) >= 0  ){
						continue;
					}
					var tr = '<tr>'   
							+	'<td>'+targetColumnSub.version+'</td>'   
							+	'<td>'+targetColumnSub.targetColumnName+'</td>'   
							+	'<td><button  class="button_delete_column">删除</button></td>'   
							+ '</tr>'   ;
					table += tr ; 
				}
				table += '</tbody>'
						+'</table>'  ;
				tables += table ;
				$('#contB .target_table').html(tables);
			}
		}else{
			alert("获取列失败");
		}
	});
} 

/**
 * 获取 更改的列
 * @returns
 */
function getChangeColumns(){
	var columns = sessionStorage.getItem(key_column);
	var columnsArr = [];
	if (columns != null) {
		columnsArr = JSON.parse(columns);
	}
	var changeColumns = [];
	for(var i = 0 ; i < columnsArr.length ; i++ ){
		if( columnsArr[i].method == 'CHANGE'){
			changeColumns.push(columnsArr[i].tableName + "|" + columnsArr[i].targetColumnName );
		}
	}
	return changeColumns ; 
}


