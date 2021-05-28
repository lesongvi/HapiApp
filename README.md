# HapiApp
## Phát triển
Usecase diagram, ERD diagram, Context diagram, level 0 DFD
  
![usecase_diagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/usecase_diagram.png)
![ContextDiagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/ContextDiagram.png)
![ERD_diagram.png](https://github.com/lesongvi/HapiApp/raw/main/images/ERD_diagram.png)
![0.png](https://github.com/lesongvi/HapiApp/raw/main/images/0.png)

## API Documentation
  
**Internal API**  
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
Endpoint studentDetailData: /api/v1/student/data/detail
Method: GET
```
```
Endpoint routeNationData: /api/v1/student/data/routenation
Method: GET
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
```
Endpoint surveyList: /api/v1/student/survey/list
Method: GET
```
```
Endpoint surveyDetail: /api/v1/student/survey/detail
Method: POST
```
```
Endpoint surveyRequest: /api/v1/student/survey/request
Method: POST
```
  
**External API**
```
Endpoint appVersion: https://api.rqn9.com/data/1.0/dapp/_/182230003154962/version
Method: GET
```

## Lỗi đã biết
### Dữ liệu trả về rỗng sau khi ủy quyền
Trường hợp có thể xảy ra:
1. Kiểm tra lại thông tin sinh viên có sai sót.
1. Server chính (server nhà trường) đang bảo trì để cập nhật thông tin sinh viên, thử lại sau 15 phút.
1. Sinh viên bảo lưu tài khoản hoặc đang bị khóa vì lý do bảo mật
1. Sinh viên chưa hoàn thành khảo sát

## Lưu ý
- Sau mỗi lần cập nhật phải thay đổi giá trị versionCode, versionName của build.gradle dự án và trong file [android.json](https://github.com/lesongvi/HapiApp/blob/main/helper/appVersion/android.json) - Bắt buộc nếu không ứng dụng sẽ không được cập nhật.
