# 项目介绍-雨林书城

Vue3+Tomcat



## 项目结构

### front_end

vue-cli开发源码

使用到的技术：element-plus、axios、stylus等

### server_end

javaWeb(Tomcat+maven)开发源码，IDEA项目

使用到的技术：JDBC-commons-utils、数据库连接池(Druid)、JSON转换等

## 运行

见打包文件，放至Tomcat服务器下运行。

也可行进行分离部署，但由于成本问题没有这样做

## GIT同步

```
echo "# yulin-bookstore" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/JustTheice/yulin-bookstore.git
git push -u origin main
```

```
git remote add origin https://github.com/JustTheice/yulin-bookstore.git
git branch -M main
git push -u origin main
```
