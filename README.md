# Bloc Template

### Setup
1. Cài đặt IntelliJ
2. Tải project này về, mở bằng IntelliJ

### Các thành phần
1. `GeneratorAction.kt`: Tạo các actions, ở đây mẫu là tạo 1 bloc template.
2. `plugin.xml`: Liên quan đến việc publish plugin, có nhiều thuộc tính nên phải tự vọc.
Quan trong là:
 `add-to-group group-id=""` Cái này quyết định xem action sẽ được hiển thị ở đâu.
 
### Build Plugin
- Tại `IntelliJ`, mở `Gradle` bên góc phải, chọn `Task/intelliJ/buildPlugin`.

![Build Plugin](https://github.com/hominhtuong/BlocTemplate/blob/main/Resources/build_plugin.png)

- Mở `Android Studio`, mở `Settings/ Plugin` chọn `Install plugin from Disk`.

![Import Plugin](https://github.com/hominhtuong/BlocTemplate/blob/main/Resources/open_android_studio.png)


- Chọn đường dẫn đến file vừa build từ IntelliJ, `{Project_Path}/build/distributions/` và chọn file .zip vừa build.

![Select Plugin](https://github.com/hominhtuong/BlocTemplate/blob/main/Resources/import.png)

- Restart Android Studio và thưởng thức. 



