<html>
<#--<head>
    <meta charset="UTF-8">
    <title>卖家商品列表</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/sell/css/style.css"/>
</head>-->
    <#include "../common/header.ftl">
<body>

<div id="wrapper" class="toggled">
<#--边栏sideBar-->
    <#include "../common/nav.ftl">

<#--主要内容区域-->
    <div id="page-content-wrapper">
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <form role="form" method="post" action="/sell/seller/category/save">
                        <div class="form-group">
                            <label>名字</label>
                            <input name="categoryName" type="text" class="form-control" value="${(category.categoryName)!''}" />
                        </div>
                        <div class="form-group">
                            <label>type</label>
                            <input name="categoryType" type="number" class="form-control" value="${(category.categoryType)!''}" />
                        </div>

                        <input hidden type="text" name="categoryId" value="${(category.categoryId)!''}">

                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>