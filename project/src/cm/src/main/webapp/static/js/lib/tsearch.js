require(["jquery", "zTree","zTreecheck", "zTreeexhide","public"], function ($) {
	
	$(function() {
		var nodeList = [];
		var targetTree = null;
		$(".ztree-search").on("change", function(){
			targetTree = $.fn.zTree.getZTreeObj($(this).attr("data-bind"));
			if (!targetTree) {
				return;
			}
			updateNodes(false);
			targetTree.expandAll(false);
			var searchKey =  $(this).val();
			if (searchKey == "") {
				//targetTree.showNodes(targetTree.getNodes());
				return;
			}
			//alertmsg(targetTree.find("li[treenode]").size());
			//targetTree.hideNodes(targetTree.getNodes());
			nodeList = targetTree.getNodesByParamFuzzy("name", searchKey);
			//targetTree.showNodes(nodeList);
			updateNodes(true);
		});
		
		function updateNodes(highlight) {
			for( var i=0, l=nodeList.length; i<l; i++) {
				nodeList[i].highlight = highlight;
				targetTree.updateNode(nodeList[i]);
				showParentNode(nodeList[i].getParentNode(), highlight);
				targetTree.selectNode(nodeList[0]);
			}
		}
		function showParentNode(parentNode, showFlag) {
			if (parentNode) {
				//targetTree.showNode(parentNode);
				targetTree.expandNode(parentNode, showFlag, null, null, null);
				showParentNode(parentNode.getParentNode(), showFlag);
			}
		}
	});
	
});
