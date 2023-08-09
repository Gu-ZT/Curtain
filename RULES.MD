* Add Features
    * HUDLoggerUpdateInterval
        * 名称：`HUD记录器更新间隔`
        * 描述：`覆写HUD记录器的更新间隔，单位为gametick`
        * 类型：`Integer`
        * 默认：`20`
        * 建议：`1`, `5`, `20`, `100`
        * 分类：`生存`
    * openFakePlayerInventory
        * 名称：`假人背包`
        * 描述：`允许玩家打开假人背包`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `假人`
    * commandLog
        * 名称：`游戏数据监视器`
        * 描述：`启用/log命令以在聊天栏或Tab栏监视游戏部分数据`
        * 类型：`String`
        * 默认：`true`
        * 建议：`ops`, `true`, `false`
        * 分类：`命令`
    * turtleEggTrampledDisabled
        * 名称：`禁用海龟蛋被践踏`
        * 描述：`阻止海龟蛋因实体踩踏而破坏`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * tntPrimerMomentumRemoved
        * 名称：`移除TNT点燃时随机动量`
        * 描述：`TNT点燃时的随机动量将被移除`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`TNT`, `创造`
    * xpNoCooldown
        * 名称：`经验吸收无冷却`
        * 描述：`玩家将会立刻吸收经验球`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * language
        * 名称：`语言`
        * 描述：`窗帘输出文本的本地化语言`
        * 类型：`String`
        * 默认：`zh_cn`
        * 建议：`zh_cn`, `en_us`
        * 分类：`特性`
    * ctrlQCraftingFix
        * 名称：`Ctrl+Q合成修复`
        * 描述：`在合成时允许使用Ctrl+Q快捷键扔出结果格内所有物品`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`修复`, `生存`
    * fakePlayerAutoReplenishment
        * 名称：`假人补货`
        * 描述：`让假人自动补货`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `假人`
    * blockPlacementIgnoreEntity
        * 名称：`方块放置忽略实体`
        * 描述：`方块可放置时无视实体碰撞检测，也就是你可以将方块放在实体内`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`
    * fakePlayerAutoFish
        * 名称：`假人自动钓鱼`
        * 描述：`让假人自动钓鱼`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `假人`
    * superLead
        * 名称：`超级拴绳`
        * 描述：`村民和怪物可以被拴绳拴住（拴怪物需要客户端也安装窗帘）`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`, `客户端`
    * rotatorBlock
        * 名称：`仙人掌扳手发射器版`
        * 描述：`发射器中的仙人掌可以旋转方块（尽可能以逆时针）`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`
    * commandPlayer
        * 名称：`玩家控制`
        * 描述：`启用/player命令以控制/召唤玩家`
        * 类型：`String`
        * 默认：`ops`
        * 建议：`ops`, `true`, `false`
        * 分类：`命令`
    * xpFromExplosions
        * 名称：`爆炸掉落经验`
        * 描述：`任何类型的爆炸都能使能掉落经验的方块掉落经验`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`, `特性`
    * desertShrubs
        * 名称：`树苗在沙漠干枯`
        * 描述：`炎热无水的气候下树苗变成枯萎的灌木`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`
    * antiCheatDisabled
        * 名称：`禁用反作弊移动监测`
        * 描述：`防止玩家因为“本服务器未启用飞行”，“移动过快”等原因被踢出`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`
    * missingTools
        * 名称：`工具缺失修复`
        * 描述：`活塞，玻璃和海绵可以用合适的工具更快地破坏`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * emptyShulkerBoxStackAlways
        * 名称：`空潜影盒总能堆叠`
        * 描述：`让空潜影盒总能堆叠起来，包括在物品栏中`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`
    * stackableShulkerBoxes
        * 名称：`潜影盒堆叠`
        * 描述：`空潜影盒可在地上堆叠到 64
在物品栏中转移时，按住 shift 点击整组移动`
        * 类型：`String`
        * 默认：`false`
        * 建议：`false`, `true`, `16`
        * 分类：`生存`, `特性`
    * fakePlayerNamePrefix
        * 名称：`假人名称前缀`
        * 描述：`为/player指令召唤出来的假人名称添加指定前缀`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`, `bot_`
        * 分类：`命令`, `假人`
    * tntRandomRange
        * 名称：`TNT爆炸范围设置`
        * 描述：`设置TNT随机爆炸范围为一个固定的值，设为-1以禁用
设为-1以使用默认值`
        * 类型：`Double`
        * 默认：`-1.0`
        * 建议：`-1`
        * 分类：`TNT`
    * scaffoldingDistance
        * 名称：`脚手架距离上限`
        * 描述：`脚手架水平拓展的距离上限`
        * 类型：`Integer`
        * 默认：`7`
        * 建议：`2`, `3`, `5`, `7`
        * 分类：`创造`
    * openFakePlayerEnderChest
        * 名称：`假人末影箱`
        * 描述：`允许玩家打开假人末影箱`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `假人`
    * fakePlayerResident
        * 名称：`假人驻留`
        * 描述：`退出存档时保留假人`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `假人`
    * quickLeafDecay
        * 名称：`快速叶子腐烂`
        * 描述：`在砍树后树叶会快速掉落`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * farmlandTrampledDisabled
        * 名称：`禁用耕地被踩踏`
        * 描述：`阻止耕地被生物踩成泥土`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`
    * fakePlayerNameSuffix
        * 名称：`假人名称后缀`
        * 描述：`为/player指令召唤出来的假人名称添加指定后缀`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`, `_fake`
        * 分类：`命令`, `假人`
    * creativeNoClip
        * 名称：`创造玩家无碰撞检测`
        * 描述：`创造玩家可以穿过方块
这个选项需要服务端和客户端同时支持才生效。
仅在服务端设置是无效的，而仅在客户端设置的情况下
你可以利用一些和活板门有关的技巧来穿墙`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `客户端`
    * betterWoodStrip
        * 名称：`更好的原木去皮`
        * 描述：`仅允许名称中包含“去皮”的斧头对原木去皮`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * allowSpawningOfflinePlayers
        * 名称：`允许离线模式假人生成`
        * 描述：`是否允许通过/player命令生成离线模式的假人`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`命令`
    * flippingCactus
        * 名称：`仙人掌扳手`
        * 描述：`允许使用仙人掌调整方块朝向，并且不会产生更新`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `特性`
    * explosionNoBlockDamage
        * 名称：`爆炸不摧毁方块`
        * 描述：`爆炸不摧毁方块`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `TNT`
    * betterSignInteraction
        * 名称：`更好的告示牌交互`
        * 描述：`右键涂蜡的告示牌时与之附着的方块产生交互`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * fakePlayerAutoReplaceTool
        * 名称：`假人切换工具`
        * 描述：`让假人自动切换快损坏的工具`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`创造`, `生存`, `假人`
    * betterFenceGatePlacement
        * 名称：`更好的栅栏门放置`
        * 描述：`让放置的栅栏门与你点击的栅栏门拥有相同的方块状态`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`生存`
    * customMOTD
        * 名称：`自定义MOTD`
        * 描述：`在试图连接到服务器的客户端上设置一个不同的MOTD信息
使用'_'来使用server.properties中的启动设置`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`
        * 分类：`创造`
    * defaultLoggers
        * 名称：`默认游戏监视器`
        * 描述：`为所有新进入玩家设置部分监视器默认开启`
        * 类型：`String`
        * 默认：`none`
        * 建议：`none`, `tps`, `mobcaps`, `mobcaps,tps`
        * 分类：`创造`, `生存`
    * placementRotationFix
        * 名称：`放置旋转修复`
        * 描述：`修复了玩家放置方块时快速转身造成的问题`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`修复`
    * optimizedTNT
        * 名称：`TNT优化`
        * 描述：`TNT在相同的地点或在流体里，爆炸造成更小的延迟`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`TNT`
    * hardcodeTNTAngle
        * 名称：`硬编码TNT角度`
        * 描述：`把TNT水平随机角度设为固定值，可用于测试机器
设为-1以使用默认值`
        * 类型：`Double`
        * 默认：`-1.0`
        * 建议：`0`
        * 分类：`TNT`
    * mergeTNT
        * 名称：`合并TNT`
        * 描述：`合并静止的点燃的TNT实体`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`TNT`
    * chickenShearing
        * 名称：`剪鸡毛`
        * 描述：`你可以用剪刀从鸡的身上剪下羽毛。注意！每次你剪鸡毛时鸡都会受伤！
未成年的鸡不能被剪下羽毛`
        * 类型：`Boolean`
        * 默认：`false`
        * 建议：`true`, `false`
        * 分类：`特性`
    * viewDistance
        * 名称：`自定义视距`
        * 描述：`改变服务器视距，设为0以使用默认值`
        * 类型：`Integer`
        * 默认：`0`
        * 建议：`0`, `12`, `16`, `32`
        * 分类：`创造`
