# SysuLibrary
基于云计算平台实现的学校图书馆的 Android 移动化


`/sysulibrary-cloud/`：  云端的python代码，负责处理请求和解析数据，基于Flask，Beautiful Soup

`/SysuLibrary-android/`： 安卓客户端的eclipse工程代码（为什么还是eclipse！！！~。~。~我也想用AS呀，只是实验考试电脑装的是eclipse好不好，两个都会用就准没错了。_。）

初始idea源于我对图书馆里那些黄金屋，那些颜如玉超越一万年的爱o(￣-￣ﾒ)

#### 后续计划：
- 学习好应用交互设计，重新架构，特别是多级缓存框架
- 在云端建立用户系统
- 在云端的用户系统基础上建立评论系统，持久收藏系统以及基于数据挖掘的推荐系统

总体框架：

![pic](/img/pic1.jpg)

安卓端：

![pic](/img/pic2.jpg)

云端：

![pic](/img/pic3.jpg)


缓存数据库ER图：


![pic](/img/pic0.jpg)

![pic](/img/pic4.jpg)
![pic](/img/pic5.jpg)
![pic](/img/pic6.jpg)
![pic](/img/pic7.jpg)