# easy4j-framework
easy4j-framework

遵循约定大于配置 ，特殊定制需在easy4j.properties配置

默认约定如下：

1. 自动读取 classpath 下 spring-**.xml
2. 视图自动配置 ,根据引入的jar包适配， 类型slf4j ， 例如 使用velocity， 只需要引用velocity 所需要的jar包
    velocity模板路劲，首先自动从WEB-INF/vm 下的文件查找， 然后从classpath:tpl




