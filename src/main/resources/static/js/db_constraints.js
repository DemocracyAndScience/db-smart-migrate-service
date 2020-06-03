function dbConstraints(){
	/**  添加列  **/
	$('body').on('click','.button_insert_constraint',
	  function() {
		var constraintName = $(this).parent().parent().find(
				'.sourceTables4').text();
		var tableName = $(this).parent().parent().parent().parent()
				.find('.tableName_constraint').text();
		if (constraintName != null && constraintName != "" && tableName != null
				&& tableName != "") {
			var constraints = sessionStorage.getItem(key_constraint);
			var constraintsArr = [];
			if (constraints != null) {
				constraintsArr = JSON.parse(constraints);
			}
			var constraint = {
					tableName : tableName,
					sourceConstraintName : constraintName,
					targetConstraintName : '',
					method : 'ADD'
			};
			
			constraintsArr.push(constraint);
			var constraintJson = JSON.stringify(constraintsArr);
			sessionStorage.setItem(key_constraint, constraintJson);
			$(this).parent().parent().remove();
		}
	});
	// 列名更名
	$('body').on('click','.js_button_constraint',
		function() {
			 var pre =  $(this).parent().parent().parent().prev();
			 var targetTableName =	pre.find('.tableName_constraint').text();
			var td = $(this).parent().next();
			if( targetTableName != null && targetTableName != ''){
				$.get('/dbsync/getTargetConstraints/'+targetTableName, function(obj){
					if(obj.code == 200 ){
						var targetObj = obj.obj
						if(targetObj != null && targetObj.length > 0  ){
							var targetConstraints   = targetObj[0].targetConstraints ; 
							var select = '<select>' ;
							for(var i =0 ; i < targetConstraints.length ; i++ ){
								var targetConstraint = targetConstraints[i] ; 
								
								var changeConstraints = getChangeConstraints();
								if( changeConstraints.indexOf(targetConstraint.targetTableName+'|'+ targetConstraint.targetConstraintName) >= 0  ){
									continue;
								}
								
								var option = '<option value="'+targetConstraint.targetConstraintName+'">'+targetConstraint.targetTableName +" | "+ targetConstraint.targetConstraintName+'</option>'
								select += option ; 
							}
							select += '</select>';
							td.html(select);
							// 刷新右侧列表
							flushTargetConstraints();
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
			'.button_constraint',
	  function() {
		var sd = $(this).parent().prev().children();
		var sdnum = sd.size();
		if (sdnum <= 0) {
			alert("未点击更名");
			return false;
		}
		var value = sd.val();
		if(value == null || value == ''){
			alert("该列中没有信息！");
			return false;
		}
		var constraintName = $(this).parent().parent().find(
				'.sourceTables4').text();
		var tableName = $(this).parent().parent().parent().parent()
				.find('.tableName_constraint').text();
		var targetConstraintName = $(this).parent().parent().find('select')
				.val();
		if (constraintName != null && constraintName != "" && tableName != null
				&& tableName != "" && targetConstraintName != null
				&& targetConstraintName != "") {
			var constraints = sessionStorage.getItem(key_constraint);
			var constraintsArr = [];
			if (constraints != null) {
				constraintsArr = JSON.parse(constraints);
			}
			var constraint = {
					tableName : tableName,
					sourceConstraintName : constraintName,
					targetConstraintName : targetConstraintName,
					method : 'CHANGE'
			};
			constraintsArr.push(constraint);
			var constraintJson = JSON.stringify(constraintsArr);
			sessionStorage.setItem(key_constraint, constraintJson);
			$(this).parent().parent().remove();
			flushTargetConstraints();
		}

		$(this).parent().parent().remove();
	});
	
	/**  删除  **/
	$('body').on('click','.button_delete_constraint',
	function() {
		// 校验是否有sourceTables1  
		var num = $('.sourceTables4').size();
		if (num > 0) {
			alert("存在未处理的source列！");
			return false;
		}
		var tbody = $(this).parent().parent().parent() ; 
		var targetTableName = tbody.prev().find('.tableName_constraint').text();
		var  targetConstraintName = $(this).parent().prev().text();
		
		if(confirm("确定要删除"+targetTableName +"表的 " +targetConstraintName+" 列吗？")){
			if ( targetTableName != null && targetTableName != "" && targetConstraintName != null && targetConstraintName != '') {
				var constraints = sessionStorage.getItem(key_constraint);
				var constraintsArr = [];
				if (constraints != null) {
					constraintsArr = JSON.parse(constraints);
				}
				var constraint = {
						tableName : targetTableName,
						sourceConstraintName : '',
						targetConstraintName : targetConstraintName,
						method : 'DELETE'
				};
				constraintsArr.push(constraint);
				var constraintsJson = JSON.stringify(constraintsArr);
				sessionStorage.setItem(key_constraint, constraintsJson);
				$(this).parent().parent().remove();
			}
		}
	});
}


function flushSourceConstraints(){
	$.get('/dbsync/getSourceConstraints',function(obj){
		if(obj.code == 200){
			var sourceConstraints =  obj.obj ; 
			var tables = ''; 
			for(var i =0 ; i < sourceConstraints.length ; i++ ){
				var sourceConstraint = sourceConstraints[i];
				var sourceConstraintSubs =  sourceConstraint.sourceConstraints;
				var table = '<table class="">'   
						+	'<thead>'   
						+		'<tr>'   
						+			'<td>版本</td>'   
						+			'<td style="font-size: 14px; font-weight: 500"'   
						+				'class="tableName_constraint">'+sourceConstraint.tableName+'</td>'   
						+			'<td></td>'   
						+			'<td></td>'   
						+			'<td style="font-size: 14px; font-weight: 500">target列名</td>'   
						+			'<td></td>' 
						+		'</tr>'   
						+	'</thead>'  ; 
				for(var j = 0 ; j< sourceConstraintSubs.length; j ++){
					var sourceConstraintSub = sourceConstraintSubs[j];
					var tr = '<tr>'   
							+	'<td><span class="span ">'+sourceConstraintSub.version+'</span></td>'   
							+	'<td><span class="span sourceTables4">'+sourceConstraintSub.sourceConstraintName+'</span></td>'   
							+	'<td><button class="button_insert_constraint">新增</button></td>'   
							+	'<td><button class="button js_button_constraint">更名</button></td>'
							+	'<td></td>'   	
							+	'<td><button class="button_constraint">确定</button></td>'   
							+ '</tr>'   ;
					table += tr ; 
					
				}
				table += '</table>'  ;
				tables += table ;
				$('#contD').append(tables);
			}
		}else{
			alert("获取列失败");
		}
	});
} 

function flushTargetConstraints(){
	$.get('/dbsync/getTargetConstraints/',function(obj){
		if(obj.code == 200){
			var targetConstraints =  obj.obj ; 
			var tables = ''; 
			for(var i =0 ; i < targetConstraints.length ; i++ ){
				var targetConstraint = targetConstraints[i];
				var targetConstraintSubs =  targetConstraint.targetConstraints;
				var table = '<table >'   
						+	'<thead>'   
						+		'<tr>'   
						+			'<td style="font-size: 14px; font-weight: 500">版本</td>'   
						+			'<td colspan="2" style="font-size: 14px; font-weight: 500"'   
						+				'class="tableName_constraint">'+targetConstraint.tableName+'</td>'   
						+		'</tr>'   
						+	'</thead>'  
						+	'</tbody>'; 
				for(var j = 0 ; j< targetConstraintSubs.length; j ++){
					var targetConstraintSub = targetConstraintSubs[j];
					var changeConstraints = getChangeConstraints();
					if( changeConstraints.indexOf(targetConstraintSub.targetTableName+'|'+ targetConstraintSub.targetConstraintName) >= 0  ){
						continue;
					}
					var tr = '<tr>'   
							+	'<td>'+targetConstraintSub.version+'</td>'   
							+	'<td>'+targetConstraintSub.targetConstraintName+'</td>'   
							+	'<td><button  class="button_delete_constraint">删除</button></td>'   
							+ '</tr>'   ;
					table += tr ; 
				}
				table += '</tbody>'
						+'</table>'  ;
				tables += table ;
				$('#contD .target_table').html(tables);
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
function getChangeConstraints(){
	var constraints = sessionStorage.getItem(key_constraint);
	var constraintsArr = [];
	if (constraints != null) {
		constraintsArr = JSON.parse(constraints);
	}
	var changeConstraints = [];
	for(var i = 0 ; i < constraintsArr.length ; i++ ){
		if( constraintsArr[i].method == 'CHANGE'){
			changeConstraints.push(constraintsArr[i].tableName + "|" + constraintsArr[i].targetConstraintName );
		}
	}
	return changeConstraints ; 
}


