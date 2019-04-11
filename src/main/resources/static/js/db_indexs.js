function dbIndexs(){
	/**  添加索引  **/
	$('body').on('click','.button_insert_index',
	  function() {
		var indexName = $(this).parent().parent().find(
				'.sourceTables3').text();
		var tableName = $(this).parent().parent().parent().parent()
				.find('.tableName_index').text();
		if (indexName != null && indexName != "" && tableName != null
				&& tableName != "") {
			var indexs = sessionStorage.getItem(key_index);
			var indexsArr = [];
			if (indexs != null) {
				indexsArr = JSON.parse(indexs);
			}
			var index = {
					tableName : tableName,
					sourceIndexName : indexName,
					targetIndexName : '',
					method : 'ADD'
			};
			
			indexsArr.push(index);
			var indexJson = JSON.stringify(indexsArr);
			sessionStorage.setItem(key_index, indexJson);
			$(this).parent().parent().remove();
		}
	});
	// 索引名更名为 
	$('body').on('click','.js_button_index',
		function() {
			 var pre =  $(this).parent().parent().parent().prev();
			 var targetTableName =	pre.find('.tableName_index').text();
			var td = $(this).parent().next();
			if( targetTableName != null && targetTableName != ''){
				$.get('/dbsync/getTargetIndexs/'+targetTableName, function(obj){
					if(obj.code == 200 ){
						var targetObj = obj.obj
						if(targetObj != null && targetObj.length > 0  ){
							var targetIndexs   = targetObj[0].targetIndexs ; 
							var select = '<select>' ;
							for(var i =0 ; i < targetIndexs.length ; i++ ){
								var targetIndex = targetIndexs[i] ; 
								
								var changeIndexs = getChangeIndexs();
								if( changeIndexs.indexOf(targetIndex.targetTableName+'|'+ targetIndex.targetIndexName) >= 0  ){
									continue;
								}
								
								var option = '<option value="'+targetIndex.targetIndexName+'">'+targetIndex.targetTableName +" | "+ targetIndex.targetIndexName+'</option>'
								select += option ; 
							}
							select += '</select>';
							td.html(select);
							// 刷新右侧索引表
							flushTargetIndexs();
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
			'.button_index',
	  function() {
		var sd = $(this).parent().prev().children();
		var sdnum = sd.size();
		if (sdnum <= 0) {
			alert("未点击更名为");
			return false;
		}
		var value = sd.val();
		if(value == null || value == ''){
			alert("该索引中没有信息！");
			return false;
		}
		var indexName = $(this).parent().parent().find(
				'.sourceTables3').text();
		var tableName = $(this).parent().parent().parent().parent()
				.find('.tableName_index').text();
		var targetIndexName = $(this).parent().parent().find('select')
				.val();
		if (indexName != null && indexName != "" && tableName != null
				&& tableName != "" && targetIndexName != null
				&& targetIndexName != "") {
			var indexs = sessionStorage.getItem(key_index);
			var indexsArr = [];
			if (indexs != null) {
				indexsArr = JSON.parse(indexs);
			}
			var index = {
					tableName : tableName,
					sourceIndexName : indexName,
					targetIndexName : targetIndexName,
					method : 'CHANGE'
			};
			indexsArr.push(index);
			var indexJson = JSON.stringify(indexsArr);
			sessionStorage.setItem(key_index, indexJson);
			$(this).parent().parent().remove();
			flushTargetIndexs();
		}

		$(this).parent().parent().remove();
	});
	
	/**  删除  **/
	$('body').on('click','.button_delete_index',
	function() {
		// 校验是否有sourceTables1  
		var num = $('.sourceTables3').size();
		if (num > 0) {
			alert("存在未处理的source索引！");
			return false;
		}
		var tbody = $(this).parent().parent().parent() ; 
		var targetTableName = tbody.prev().find('.tableName_index').text();
		var  targetIndexName = $(this).parent().prev().text();
		
		if(confirm("确定要删除"+targetTableName +"表的 " +targetIndexName+" 索引吗？")){
			if ( targetTableName != null && targetTableName != "" && targetIndexName != null && targetIndexName != '') {
				var indexs = sessionStorage.getItem(key_index);
				var indexsArr = [];
				if (indexs != null) {
					indexsArr = JSON.parse(indexs);
				}
				var index = {
						tableName : targetTableName,
						sourceIndexName : '',
						targetIndexName : targetIndexName,
						method : 'DELETE'
				};
				indexsArr.push(index);
				var indexsJson = JSON.stringify(indexsArr);
				sessionStorage.setItem(key_index, indexsJson);
				$(this).parent().parent().remove();
			}
		}
	});
}


function flushSourceIndexs(){
	$.get('/dbsync/getSourceIndexs',function(obj){
		if(obj.code == 200){
			var sourceIndexs =  obj.obj ;
			console.log(sourceIndexs)
			var tables = ''; 
			for(var i =0 ; i < sourceIndexs.length ; i++ ){
				var sourceIndex = sourceIndexs[i];
				var sourceIndexSubs =  sourceIndex.sourceIndexs;
				var table = '<table class="">'   
						+	'<thead>'   
						+		'<tr>'   
						+			'<td>版本</td>'   
						+			'<td style="font-size: 14px; font-weight: 500"'   
						+				'class="tableName_index">'+sourceIndex.tableName+'</td>'   
						+			'<td></td>'   
						+			'<td></td>'   
						+			'<td style="font-size: 14px; font-weight: 500">target索引名</td>'   
						+			'<td></td>' 
						+		'</tr>'   
						+	'</thead>'  ; 
				for(var j = 0 ; j< sourceIndexSubs.length; j ++){
					var sourceIndexSub = sourceIndexSubs[j];
					var tr = '<tr>'   
							+	'<td><span class="span ">'+sourceIndexSub.version+'</span></td>'   
							+	'<td><span class="span sourceTables3">'+sourceIndexSub.sourceIndexName+'</span></td>'   
							+	'<td><button class="button_insert_index">新增</button></td>'   
							+	'<td><button class="button js_button_index">更名为</button></td>'   
							+	'<td></td>'   	
							+	'<td><button class="button_index">确定</button></td>'   
							+ '</tr>'   ;
					table += tr ; 
					
				}
				table += '</table>'  ;
				tables += table ;
			}
			$('#contC').append(tables);
		}else{
			alert("获取索引失败");
		}
	});
} 

function flushTargetIndexs(){
	$.get('/dbsync/getTargetIndexs/',function(obj){
		if(obj.code == 200){
			var targetIndexs =  obj.obj ; 
			var tables = ''; 
			for(var i =0 ; i < targetIndexs.length ; i++ ){
				var targetIndex = targetIndexs[i];
				var targetIndexSubs =  targetIndex.targetIndexs;
				var table = '<table >'   
						+	'<thead>'   
						+		'<tr>'   
						+			'<td style="font-size: 14px; font-weight: 500">版本</td>'   
						+			'<td colspan="2" style="font-size: 14px; font-weight: 500"'   
						+				'class="tableName_index">'+targetIndex.tableName+'</td>'   
						+		'</tr>'   
						+	'</thead>'  
						+	'</tbody>'; 
				for(var j = 0 ; j< targetIndexSubs.length; j ++){
					var targetIndexSub = targetIndexSubs[j];
					var changeIndexs = getChangeIndexs();
					if( changeIndexs.indexOf(targetIndexSub.targetTableName+'|'+ targetIndexSub.targetIndexName) >= 0  ){
						continue;
					}
					var tr = '<tr>'   
							+	'<td>'+targetIndexSub.version+'</td>'   
							+	'<td>'+targetIndexSub.targetIndexName+'</td>'   
							+	'<td><button  class="button_delete_index">删除</button></td>'   
							+ '</tr>'   ;
					table += tr ; 
				}
				table += '</tbody>'
						+'</table>'  ;
				tables += table ;
				$('#contC .target_table').html(tables);
			}
		}else{
			alert("获取索引失败");
		}
	});
} 

/**
 * 获取 更改的索引
 * @returns
 */
function getChangeIndexs(){
	var indexs = sessionStorage.getItem(key_index);
	var indexsArr = [];
	if (indexs != null) {
		indexsArr = JSON.parse(indexs);
	}
	var changeIndexs = [];
	for(var i = 0 ; i < indexsArr.length ; i++ ){
		if( indexsArr[i].method == 'CHANGE'){
			changeIndexs.push(indexsArr[i].tableName + "|" + indexsArr[i].targetIndexName );
		}
	}
	return changeIndexs ; 
}


