# CS61B-sp26-proj5-BYOW
UCB CS61B的结课项目project5 BYOW，用JAVA写一个小游戏。采用的是sp24的模板，遵照sp26要求，花了四天不知道多少小时。

## 实现内容
-随机地图生成
-WSAD控制玩家移动，并且不能穿过墙壁
-简单的存档和读取功能，保留种子与移动历史
-完善模板附带的测试文件

## 操作说明
运行后，输入N开始游戏，Q退出。
输入种子后S键结束。
进入游戏后，W向上，S向下，A向左，D向右。

## 项目结构
```text
proj5/
├── src/
│   ├── core/
│   │   ├── Main.java
│   │   ├── Engine.java
│   │   ├── InteractiveWorld.java
│   │   ├── World.java
│   │   ├── Region.java
│   │   ├── RoomTile.java
│   │   └── AutograderBuddy.java
│   │
│   ├── tileengine/
│   │   ├── TERenderer.java
│   │   ├── TETile.java
│   │   └── Tileset.java
│   │
│   └── utils/
│       ├── DirectionUtils.java
│       ├── DrawUtils.java
│       ├── FileUtils.java
│       ├── Hexagon.java
│       ├── IndexUtils.java
│       └── RandomUtils.java
│
└── tests/
    └── WorldGenTests.java
```



## 使用技术
-JAVA类设计
-BFS
-图的遍历
-概率方法
-简单文件读写

## 感谢AI以及参考项目
使用AI：除修正大篇幅代码和改善缩进排版以外，帮我构思了generateHallway函数和解决了长按键盘画面掉帧的问题，以及上面的项目结构和无数的小提示😜
也参考了不少别人的完整项目🙏

## 注
目前为v1.0版本，后续不知道会不会更新完善了。
玩游戏前要配置Princeton algs4库。
