# wx-baobao-risk

微信小程序，包包大冒险
红包社交程序

1\. 运行应用程序时需要在添加program arguments运行参数(为区分生产和本地开发环境)
```
--spring.profiles.active=local
-Dspring.profiles.active=local(JUnit test VM arguments:)
```

2\. Maven快速打包，忽略测试
```
mvn -Dmaven.test.skip=true clean package
```