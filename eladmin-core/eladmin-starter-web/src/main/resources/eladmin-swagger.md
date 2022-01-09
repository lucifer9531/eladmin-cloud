ElAdminCloud是一款基于Spring Cloud Alibaba的微服务架构。旨在为大家提供技术框架的基础能力的封装，减少开发工作，让您只关注业务。

### 👉 技术栈
- Vue 3.2.2
- Pinia 2.0.0-rc.4
- vue-i18n 9.1.7
- typescript 4.29.1
- ant-design-vue 2.2.6
- axios 0.21.1
- vue-router 4.1.8
- vite 2.5.0-beta.2

## 🍪 技术架构
<p align="center"> 
    <img src="https://cdn.mate.vip/matecloud-framework.jpg" />
</p>

## 🔧 功能特点
- 主体框架：采用最新的`Spring Cloud 2021.0.0`, `Spring Boot 2.6.2`, `Spring Cloud Alibaba 2021.1`版本进行系统设计；

- 统一注册：支持`Nacos`作为注册中心，实现多配置、分群组、分命名空间、多业务模块的注册和发现功能；

- 统一认证：统一`Oauth2`认证协议，采用jwt的方式，实现统一认证，并支持自定义grant_type实现手机号码登录，第三方登录集成JustAuth实现微信、支付宝等多种登录模式；

- 业务监控：利用`Spring Boot Admin`来监控各个独立Service的运行状态。

- 内部调用：集成了`Feign`和`Dubbo`两种模式支持内部调用，并且可以实现无缝切换，适合新老程序员，快速熟悉项目；

- 业务熔断：采用`Sentinel`实现业务熔断处理，避免服务之间出现雪崩;

- 身份注入：通过注解的方式，实现用户登录信息的快速注入；

- 在线文档：通过接入`Knife4j`，实现在线API文档的查看与调试;

- 代码生成：基于`Mybatis-plus-generator`自动生成代码，提升开发效率，生成模式不断优化中，暂不支持前端代码生成；

- 消息中心：集成消息中间件`RocketMQ`和`Kafka`，对业务进行异步处理;

- 业务分离：采用前后端分离的框架设计，前端采用`vue-element-admin`,商业版采用`antd-pro-vue`

- 链路追踪：自定义traceId的方式，实现简单的链路追踪功能

- 多租户功能：集成`Mybatis Plus`,实现SAAS多租户功能

## 🗿 文件结构

## 🌭 项目源码
