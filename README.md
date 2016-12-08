----------
# ScrollingImageListView说明及示例

**[ScrollingImageListView 的 Demo已分享在Github，源码注释清晰，欢迎查看指正。](https://github.com/zwJeff/RunningImageListDemo)**


###1、功能说明
 
 ScrollingImageListView实现了多图滚动循环轮播，可以在xml中声明并配置轮播方向、单图还是多图、是否乱序 以及 滚动速度，当然 这些也可以在代码里动态配置。
 如果是单图滚动 可以直接在xml中配置图片，如果是多图模式，则需要在代码里设置。

###2、使用示例

####1>XML布局示例
首先在xml中声明自定义view的属性标签：
```xml
xmlns:app="http://schemas.android.com/apk/res-auto" 
```

在需要用到控件的地方如下声明，是否从左到右滚动(true)、是否乱序(true)、是否单图滚动(false)、滚动速度(1dp)，如下：
```xml
<com.jeff.runningimagelistdemo.ScrollingImageListView
        android:id="@+id/test"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:isLeftToRight="true"
        app:isOutOfOrder="true"
        app:isSingleImg="false"
        app:speed="1dp" />
```

####2>代码中的配置
在代码中初始化ScrollingImageListView方法如下：

```java
mScrollingImageListView = (ScrollingImageListView) findViewById(R.id.test);

List<Integer> list = new ArrayList<>();
list.add(R.mipmap.a1);
list.add(R.mipmap.a2);
list.add(R.mipmap.a3);
//设置轮播图片列表
mScrollingImageListView.setImgSrcList(list);
//设置方向是否从左至右
mScrollingImageListView.setLeftToRight(true);
//设置图片是否乱序
mScrollingImageListView.setOutOfOder(false);
//设置播放速度（此处的速度和xml中的速度有三倍的关系 3=1dp ）
mScrollingImageListView.setSpeed(5);
//播放开始
mScrollingImageListView.startRun();
```

出上述之外，还可以在需要的地方，查看当前状态、动态停止、改变速度、改变方向等，具体代码如下：
```java
//获取当前view的状态（State.STOP、State.LEFT_RUNNING、State.RIGHT_RUNNING）
mScrollingImageListView.getmRunningmState();

//停止滚动（两种方法都可以）
mScrollingImageListView.stopRun();
mScrollingImageListView.changeDirection(ScrollingImageListView.State.STOP);

//改变滚动状态
//向右滚动
mScrollingImageListView.changeDirection(ScrollingImageListView.State.RIGHT_RUNNING);
//向左滚动
mScrollingImageListView.changeDirection(ScrollingImageListView.State.LEFT_RUNNING);
//停止滚动
mScrollingImageListView.changeDirection(ScrollingImageListView.State.STOP);

//改变速度
//加速（每次加3）
mScrollingImageListView.speedUp();
//加速（每次减3）
mScrollingImageListView.speedDown();
//指定速度变速
mScrollingImageListView.changeSpeed(5);
```

##3、结束语

**当前多图功能比较完善，单图还未调试，欢迎批评指正。**
