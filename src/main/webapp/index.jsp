<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>INDEX</title>
</head>
<body>

springmvc 上传文件测试
<form name="form" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="springMVC上传文件">
</form>

富文本编辑器simditor 上传文件测试
<form name="form" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="富文本编辑器上传文件">
</form>


</body>
</html>