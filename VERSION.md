## Version

### 1.0.0

* Add Features
    * language
        * 名称：`语言`
        * 描述：`窗帘输出文本的本地化语言`
        * 类型：`String`
        * 默认：`zh_cn`
        * 建议：`zh_cn`, `en_us`
        * 分类：`特性`
    * viewDistance
        * 名称：`自定义视距`
        * 描述：`改变服务器视距，设为0以使用默认值`
        * 类型：`Integer`
        * 默认：`0`
        * 建议：`0`, `12`, `16`, `32`
        * 分类：`创造`
    * xpNoCooldown
        * 名称：`经验吸收无冷却`
        * 描述：`玩家将会立刻吸收经验球`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * allowSpawningOfflinePlayers
        * 名称：`允许离线模式假人生成`
        * 描述：`是否允许通过/player命令生成离线模式的假人`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`命令`
    * commandPlayer
        * 名称：`玩家控制`
        * 描述：`启用/player命令以控制/召唤玩家`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`命令`
    * commandLog
        * 名称：`游戏数据监视器`
        * 描述：`启用/log命令以在聊天栏或Tab栏监视游戏部分数据`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`命令`
    * missingTools
        * 名称：`工具缺失修复`
        * 描述：`活塞，玻璃和海绵可以用合适的工具更快地破坏`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * flippingCactus
        * 名称：`仙人掌扳手`
        * 描述：`允许使用仙人掌调整方块朝向，并且不会产生更新`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `特性`
    * rotatorBlock
        * 名称：`仙人掌扳手发射器版`
        * 描述：`发射器中的仙人掌可以旋转方块（尽可能以逆时针）`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`自动`, `特性`
    * placementRotationFix
        * 名称：`放置旋转修复`
        * 描述：`修复了玩家放置方块时快速转身造成的问题`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`修复`
    * fillUpdates
        * 名称：`fill更新开关`
        * 描述：`fill/clone/setblock命令以及结构方块执行时是否产生更新`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * interactionUpdates
        * 名称：`放置方块产生更新`
        * 描述：`放置方块是否产生更新的开关，设为false以避免产生更新`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * defaultLoggers
        * 名称：`默认游戏监视器`
        * 描述：`为所有新进入玩家设置部分监视器默认开启`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`, `tps`, `mobcaps`, `mobcaps,tps`
        * 分类：`创造`, `生存`
    * HUDLoggerUpdateInterval
        * 名称：`HUD记录器更新间隔`
        * 描述：`覆写HUD记录器的更新间隔，单位为gametick`
        * 类型：`Integer`
        * 默认：`100`
        * 建议：`1`, `5`, `20`, `100`
        * 分类：`生存`