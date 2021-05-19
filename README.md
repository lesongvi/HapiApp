# HapiApp
## Phát triển
Usecase 27/03/2021  
  
![usecase_diagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/usecase_diagram.png)

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

## Lỗi đã biết
### Dữ liệu trả về rỗng sau khi ủy quyền
Trường hợp có thể xảy ra:
1. Kiểm tra lại thông tin sinh viên có sai sót.
1. Server chính (server nhà trường) đang bảo trì để cập nhật thông tin sinh viên, thử lại sau 15 phút.
