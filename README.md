# HapiApp
## Phát triển
Usecase diagram, ERD diagram, Context diagram
  
![usecase_diagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/usecase_diagram.png)
![ERD_diagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/ERD_diagram.png)
![ContextDiagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/ContextDiagram.png)

## API Documentation
API prefix: **/api/v1**
```
Endpoint authenticate: /api/v1/auth
Method: POST
```
```
Endpoint semesterView: /api/v1/schedule/semester
Method: POST
```
```
Endpoint weekView: /api/v1/schedule/week
Method: POST
```
```
Endpoint scheduleView: /api/v1/schedule/detail
Method: POST
```
```
Endpoint currentPointView: /api/v1/point/current
Method: POST
```
```
Endpoint semesterListView: /api/v1/point/semester
Method: POST
```
```
Endpoint specificPointView: /api/v1/point/view
Method: POST
```
```
Endpoint reautheticate: /api/v1/student/info
Method: POST
```
```
Endpoint evaluateStudent: /api/v1/student/evaluate/list
Method: POST
```
```
Endpoint evaluateStudentDetail: /api/v1/student/evaluate/view
Method: POST
```
```
Endpoint starestRequest: /api/v1/student/tarest/request
Method: POST
```
```
Endpoint starestListRequest: /api/v1/student/tarest/list
Method: POST
```
```
Endpoint CExamView: /api/v1/schedule/cexam
Method: POST
```
```
Endpoint cnotification: /api/v1/snapshot/notification
Method: GET
```
```
Endpoint anotification: /api/v1/snapshot/notifications
Method: GET
```

## Lỗi đã biết
### Dữ liệu trả về rỗng sau khi ủy quyền
Trường hợp có thể xảy ra:
1. Kiểm tra lại thông tin sinh viên có sai sót.
1. Server chính (server nhà trường) đang bảo trì để cập nhật thông tin sinh viên, thử lại sau 15 phút.
1. Sinh viên bảo lưu tài khoản hoặc đang bị khóa vì lý do bảo mật
