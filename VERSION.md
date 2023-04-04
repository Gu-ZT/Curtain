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

# 1.2.0

* Add Features
    * openFakePlayerInventory
        * 名称：`假人背包`
        * 描述：`允许玩家打开假人背包`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`
    * openFakePlayerEnderChest
        * 名称：`假人末影箱`
        * 描述：`允许玩家打开假人末影箱`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`
    * stackableShulkerBoxes
        * 名称：`潜影盒堆叠`
        * 描述：`空潜影盒可在地上堆叠到 64`
        * 类型：`String`
        * 默认：`false`
        * 建议：`false`, `true`, `16`
        * 分类：`生存`, `特性`
    * superLead
        * 名称：`超级拴绳`
        * 描述：`村民和怪物可以被拴绳拴住（拴怪物需要客户端也安装窗帘）`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`, `客户端`
    * desertShrubs
        * 名称：`树苗在沙漠干枯`
        * 描述：`炎热无水的气候下树苗变成枯萎的灌木`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`
    * xpFromExplosions
        * 名称：`爆炸掉落经验`
        * 描述：`任何类型的爆炸都能使能掉落经验的方块掉落经验`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`, `特性`
    * explosionNoBlockDamage
        * 名称：`爆炸不摧毁方块`
        * 描述：`爆炸不摧毁方块`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `TNT`
    * creativeNoClip
        * 名称：`创造玩家无碰撞检测`
        * 描述：`创造玩家可以穿过方块`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `客户端`
    * optimizedTNT
        * 名称：`TNT优化`
        * 描述：`TNT在相同的地点或在流体里，爆炸造成更小的延迟`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`TNT`
    * TNTRandomRange
        * 名称：`TNT爆炸范围设置`
        * 描述：`设置TNT随机爆炸范围为一个固定的值，设为-1以禁用`
        * 类型：`Double`
        * 默认：`-1`
        * 建议：`-1`
        * 分类：`TNT`
    * TNTPrimerMomentumRemoved
        * 名称：`移除TNT点燃时随机动量`
        * 描述：`TNT点燃时的随机动量将被移除`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`TNT`, `创造`
    * hardCodeTNTAngle
        * 名称：`硬编码TNT角度`
        * 描述：`把TNT水平随机角度设为固定值，可用于测试机器`
        * 类型：`Double`
        * 默认：`-1`
        * 建议：`0`
        * 分类：`TNT`
    * mergeTNT
        * 名称：`合并TNT`
        * 描述：`合并静止的点燃的TNT实体`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`TNT`
    * ctrlQCraftingFix
        * 名称：`Ctrl+Q合成修复`
        * 描述：`在合成时允许使用Ctrl+Q快捷键扔出结果格内所有物品`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`修复`, `生存`
    * quickLeafDecay
        * 名称：`快速叶子腐烂`
        * 描述：`在砍树后树叶会快速掉落`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * customMOTD
        * 名称：`自定义MOTD`
        * 描述：`在试图连接到服务器的客户端上设置一个不同的MOTD信息`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`
        * 分类：`创造`
    * turtleEggTrampledDisabled
        * 名称：`禁用海龟蛋被践踏`
        * 描述：`阻止海龟蛋因实体踩踏而破坏`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * farmlandTrampledDisabled
        * 名称：`禁用耕地被踩踏`
        * 描述：`阻止耕地被生物踩成泥土`
        * 类型：`Boolen`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * fakePlayerNamePrefix
        * 名称：`假人名称前缀`
        * 描述：`为/player指令召唤出来的假人名称添加指定前缀`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`, `bot_`
        * 分类：`命令`
    * fakePlayerNameSuffix
        * 名称：`假人名称后缀`
        * 描述：`为/player指令召唤出来的假人名称添加指定后缀`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`, `_fake`
        * 分类：`命令`
