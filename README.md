# Insomnia-android
Insomnia-android 是全栈项目 ** Insomnia ** 的 Android 端代码。
项目深度依赖 笔者另一开源库 [simpleLib](https://github.com/cchao1024/simpleLib) 开发

基于

* [data-binding](https://developer.android.com/topic/libraries/data-binding)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [glide](https://github.com/bumptech/glide)
* [okHttp](https://github.com/square/okhttp)
* [Gson](https://github.com/google/gson)

等主流框架。

# 主体功能

```
Insomnia-server
├── 用户相关
|   ├── 游客邮箱绑定
|   ├── 用户登录
|   └── 信息查看/修改
|
├── 催眠图片
|   ├── 图片列表
|   └── 图片查看、点赞、下载 
|
├── 音乐相关
|   ├── 音乐列表
|   └── 音乐播放、点赞、下载 
|
└── 说说
    ├── 说说图片上传  
    ├── 发布说说
    ├─- 评论/点赞说说
    └─- 回复/点赞他人评论及回复  
```

# 页面截图