# Lib

这是个人总结的`lib`包，并且快捷实现`WanAndroid`客户端

## dependency

* ViewModel
* AndroidUtilCode
* GSON
* OkHttp
* Okio
* Retrofit
* RxJava3
* Glide
* BaseRecyclerViewAdapterHelper
* SmartRefreshLayout
* RxPermissions
* ImmersionBar
* PrettyTime
* FormattedEditText

## 添加的功能

1. 封装了`AppCompatActivity`, `Fragment`, 更有条理的实现
2. 简化了`retrofit`的使用，同时解耦了`retrofit`, `okhttp`的实现
3. 添加了`glide`的`okhttp``module`的实现，不使用官方实现，重用了`okhttp`
4. 添加了`BaseQuickAdapter`工具类，避免内存泄漏（在`Fragment`中使用时出现）
5. 添加了`RxJava`的工具类，避免内存泄漏
6. 实现了页面状态加载，刚进入页面没有数据时实现加载状态

## 使用方式
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.MycroftWong:lib:v1.9'
}


// 使用了rxjava3, 在app build.gradle中添加
android {
    ...
    configurations {
        compile.exclude group: 'io.reactivex.rxjava2', module: 'rxjava'
        all {
            resolutionStrategy {
                eachDependency { DependencyResolveDetails details ->
                    if (details.requested.group == 'io.reactivex.rxjava3' &&
                            details.requested.name == 'rxjava') {
                        details.useVersion '3.0.0-RC1'
                        details.because 'Unified the version of RxJava3'
                    }
                }
            }
        }
    }
}
```

## 展望

`ViewModel`的更有利于配合`Activity`和`Fragment`的生命周期使用，极大程度上避免了内存泄漏，在考虑封装使用，减少`RxJava`的使用

## ChangeLog

* 1.9.1 更新依赖库，删除过时代码 
