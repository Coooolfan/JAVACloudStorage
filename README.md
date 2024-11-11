# JAVACloudStorage / 基于SpringBoot的云存储系统

> [!CAUTION]
> 
> 这不是一个完整的项目，只是用于完成课程作业
> 
> 不要将此项目用于生产环境
>
> 如果你正在寻求轻量化的文件 存储、分享、管理 系统，可以考虑[UniBoard](https://github.com/Coooolfan/UniBoard/)
> 
> 如果你正在寻求一个完整的云存储系统，可以考虑[Cloudreve](https://cloudreve.org/)

# 截图
<table>
    <tr>
        <td><img src="static/1.png" alt="登录"></td>
        <td><img src="static/2.jpg.png" alt="注册"></td>
    </tr>
    <tr>
        <td><img src="static/3.png" alt="主页面"></td>
        <td><img src="static/4.png" alt="新建立文件夹弹窗"></td>
    </tr>
</table>

# 技术栈

- 前端
  - Thymeleaf （仅登录注册页面）
  - Vue （主页面）
    - Primevue 组件库
    - tailwindcss 样式库
- 后端
  - SpringBoot
    - sa-token 权限管理
    - mybatis plus 数据库操作
  - MySQL 数据库

# 功能

- [✔] 登录
- [✔] 注册
- [✔] 上传文件
- [✔] 下载文件
- [✔] 删除文件
- [✔] 新建文件夹
- [✔] 重命名文件
- [✔] **文件秒传**

# 部署

> [!CAUTION]
> 
> **再次警告！**这不是一个完整的项目，缺乏生产环境所需的安全性、稳定性、性能等方面的保障
> 
> 更多事项需求请联系在Git提交记录中的邮件或者提Issue

## 下载项目

```shell
git clone https://github.com/Coooolfan/JAVACloudStorage.git
```

## 创建数据库

```shell
cd JAVACloudStorage
# 启动根目录下的docker-compose编排文件，默认使用mysql，会自动创建名为javacloudstorage的数据库，账号为root，密码为123456，开放于本地3306端口
sudo docker compose up -d
```

## 创建表

使用IDEA或者DBeaver等工具连接到数据库，运行`db.sql`中的SQL语句，创建俩个表

## 启动后端项目

打开使用IDEA打开项目根目录，等待Maven下载依赖，然后运行`JavaCloudStorageApplication`类

## 启动前端项目

```shell
cd JAVACloudStorage
# 安装依赖
yarn
# 启动
yarn dev
```

## 从浏览器打开

前端默认开放于`http://localhost:5173`，后端默认开放于`http://localhost:8080`。

直接访问<http://localhost:5173>即可

# 跨域与反向代理

项目没有考虑跨域问题，在开发环境中，通过配置Vite开发服务器的反向代理服务器来处理跨域问题。

见`/JAVACloudStorage/vite.config.ts`文件

```ts
const backhandURL = "http://localhost:8080";
// https://vite.dev/config/
export default defineConfig({
……
  server: {
    proxy: {
      "/api": backhandURL,
      "/register": backhandURL,
      "/login": backhandURL,
    },
  },
});
```