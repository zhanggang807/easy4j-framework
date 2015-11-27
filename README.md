# easy4j-framework
### easy4j-framework

javaweb , 开源框架很多， 使用起来需要进行很多配置才能满足需求，那么能不能避免这种麻烦 ，然后还能使用这些框架带来的便利
，那么easy4j可以满足你的愿望，easy4j深度定制springmvc ，让开发人员还按照springmvc模式开发应用系统，让您在一分钟内搭建一个
基于springmvc的web构建

### Quick starter

环境： jdk7.0+ ,tomcat7.0+

遵循约定大于配置 ，特殊定制需在easy4j.properties配置

默认约定如下：

1. 自动读取 classpath 下 spring-**.xml
2. 视图采用自动适配 ,根据引入的jar包适配， 类似slf4j ， 例如 使用velocity， 只需要引用velocity 所需要的jar包 ，如果未引入velocity，
侧默认使用jsp， jsp查找路径"src/main/webapp"
    若使用velocity视图 ，默认配置如下：
    velocity模板路劲，首先自动从WEB-INF/vm 下的文件查找， 然后从classpath:tpl查找 ，自动读取velocityTools bean 当做velocity 工具
，velocityTools bean 必须为Map的实例

3. 静态资源文件

   静态资源文件 ，处理方式，默认匹配配置/** , 位置配置包含 "/","/www/","classpath:META-INF/www/" , 默认会从这三个目录去读取
如果还是读不到 ， 就返回404啦 ，其中"/","/www/" 分别指向工程webapp/ ， 以及webapp/www/目录  ，最佳实践， 不变的可共享的，
提供第三方用的资源 ， 建议放到"classpath:META-INF/www/"中






