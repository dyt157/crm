<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+
            request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //发送ajax请求获取交易表中各个阶段的名称以及数量
            $.ajax({
                url:'workbench/chart/transaction/queryStageCount',
                type:"post",
                dataType:'json',
                success:function (data) {
                    if (data.code==="1"){
                        let tranStageList = data.returnData.tranStageList
                        let tranStageNameList =data.returnData.tranStageNameList
                        let myChart = echarts.init(document.getElementById("tranChart"))
                        let option = {
                            //标题
                            title: {
                                text: '交易阶段图',
                                //子标题
                                subtext: '统计交易表中各个阶段的数量'
                            },
                            //工具箱
                            toolbox: {
                                feature: {
                                    //数据视图
                                    dataView: {readOnly: false},
                                    //重置，回到最开始的数据
                                    restore: {},
                                    //保存为图片
                                    saveAsImage: {}
                                }
                            },
                            //提示框信息
                            tooltip: {
                                show:true,//是否显示提示框，true表示显示（默认）
                                trigger:'item',//触发方式，item:鼠标移动到数据项上就显示（默认）
                                formatter:'{a}<br/>{b}:{c}'//提示框内容的格式
                                //模板变量有 {a}, {b}，{c}分别表示系列名，数据名，数据值
                            },
                            //图例（漏斗图其实可以不需要图例）
                            legend: {
                                /*data: ['成交',
                                    '确定决策者',
                                    '谈判',
                                    '报案']*/
                                data:tranStageNameList

                            },
                            //系列，可以有多个，也就是x轴上一个值对应多条记录，比如说除了要展示销量，还要展示点击量
                            series: [
                                {
                                    name: '数据量', //这个系列的名字
                                    type: 'funnel',//这个系列的图形类型
                                    //在容器中的位置和大小
                                    left: '10%',
                                    width: '75%',
                                    label: {//标记，说明,这个对象是对各个数据项进行说明的
                                        //formatter: '{b}'（默认值）
                                        formatter: '{b}阶段'
                                    },
                                    labelLine: {//显示数据项前面那条线
                                        show: true //默认值
                                    },
                                    itemStyle: {//数据项风格
                                        opacity: 0.8 //透明度
                                    },
                                    emphasis: {//强调，也就是高亮显示
                                        //鼠标移动到数据项上时，数据项高亮显示并且展示一些其他信息，默认也是开启的
                                        //表示数据项在高亮显示的同时，展示数据项的说明
                                        label: {
                                            formatter: '{b}阶段: {c}' //默认值：'{b}'
                                        }
                                    },
                                    /*data: [
                                        //数据，name表示每个数据项的名称，需要和legend对象的data属性保持一致
                                        //value:数据项的值
                                        {name:'成交',value:4},
                                        {name:'确定决策者',value:6},
                                        {name:'谈判',value:2},
                                        {name:'报案',value:8},
                                    ]*/
                                    data:tranStageList
                                },
                            ]
                        };

                        myChart.setOption(option)
                    }else{
                        alert(data.message)
                    }
                }
            })


        })
    </script>
    <style>
        #tranChart{
            width: 800px;
            height:500px;
            margin-right: auto;
            margin-left: auto;
            margin-top: 100px;

        }
    </style>
</head>
<body>
    <%--准备容器    --%>
    <div id="tranChart" ></div>

</body>
</html>
