<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
</head>

<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="height:400px"></div>
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script type="text/javascript">
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
		
		 // 使用
        require(
            [
                'echarts',
                'echarts/chart/map' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main')); 
                
                var option = 
                  {
				   title: {
				       x: "center",
				        text: "各省机构当日保费"
				   },
				   tooltip: {
				       trigger: "item"
				   },
				   legend: {
				       orient: "vertical",
				       x: "left",
				       data: ["保费"]
				   },
				   toolbox: {
				       orient: "vertical",
				       x: "right",
				       y: "bottom",
				       feature: {
				           mark: {
				               show: true
				           },
				           dataView: {
				               show: true,
				               readOnly: false
				           },
				           restore: {
				               show: true
				           },
				           saveAsImage: {
				               show: true
				           }
				       }
				   },
				   roamController: {
				       x: "right",
				       mapTypeControl: {
				           china: true
				       }
				   },
				   series: [
				       {
				           name: "保费",
				           type: "map",
				           mapType: "china",
				           roam: false,
				           mapValueCalculation: "sum",
				           itemStyle: {
				               emphasis: {
				                   label: {
				                       show: true
				                   },
				                   borderColor: "rgb(255, 0, 0)",
				                   borderWidth: 1
				               },
				               normal: {
				                   borderWidth: 1,
				                   borderColor: "rgb(255, 170, 86)",
				                   label: {
				                       show: true
				                   }
				               }
				           },
				           data: [
				               {
				                   value: 234,
				                   name: "北京"
				               },
				               {
				                   value: 532,
				                   name: "天津"
				               },
				               {
				                   value: 134,
				                   name: "上海"
				               },
				               {
				                   value: 983,
				                   name: "重庆"
				               },
				               {
				                   value: 783,
				                   name: "河北"
				               },
				               {
				                   value: 345,
				                   name: "河南"
				               },
				               {
				                   value: 872,
				                   name: "云南"
				               },
				               {
				                   value: 94,
				                   name: "辽宁"
				               },
				               {
				                   value: 342,
				                   name: "黑龙江"
				               },
				               {
				                   value: 989,
				                   name: "湖南"
				               },
				               {
				                   value: 767,
				                   name: "安徽"
				               },
				               {
				                   value: 675,
				                   name: "山东"
				               },
				               {
				                   value: 874,
				                   name: "新疆"
				               },
				               {
				                   value: 874,
				                   name: "江苏"
				               },
				               {
				                   value: 878,
				                   name: "浙江"
				               },
				               {
				                   value: 928,
				                   name: "江西"
				               },
				               {
				                   value: 44,
				                   name: "湖北"
				               },
				               {
				                   value: 448,
				                   name: "广西"
				               },
				               {
				                   value: 887,
				                   name: "甘肃"
				               },
				               {
				                   value: 903,
				                   name: "山西"
				               },
				               {
				                   value: 673,
				                   name: "内蒙古"
				               },
				               {
				                   value: 563,
				                   name: "陕西"
				               },
				               {
				                   value: 747,
				                   name: "吉林"
				               },
				               {
				                   value: 112,
				                   name: "福建"
				               },
				               {
				                   value: 473,
				                   name: "贵州"
				               },
				               {
				                   value: 647,
				                   name: "广东"
				               },
				               {
				                   value: 838,
				                   name: "青海"
				               },
				               {
				                   value: 626,
				                   name: "西藏"
				               },
				               {
				                   value: 515,
				                   name: "四川"
				               },
				               {
				                   value: 172,
				                   name: "宁夏"
				               },
				               {
				                   value: 77,
				                   name: "海南"
				               },
				               {
				                   value: 837,
				                   name: "台湾"
				               },
				               {
				                   value: 677,
				                   name: "香港"
				               },
				               {
				                   value: 43,
				                   name: "澳门"
				               }
				           ],
				           showLegendSymbol: true,
				           markPoint: {
				               data: []
				           }
				       }
				   ],
				   backgroundColor: "rgba(0,0,0,0)"
				};
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );
    </script>
</body>