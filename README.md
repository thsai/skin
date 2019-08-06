# skin
一个插件化换肤框架
# Usage
1. 导入依赖

```Java
implementation 'com.github.thsai:skin:1.0.0'
```
2. 在application中加入一行代码即可开启换肤
```kotlin
SkinManager.init(this)
```
3. 另新建一个module，并在资源文件中（例如res/value/colors.xml）定义与主apk对应的换肤资源值，然后打包生成一个仅包含换肤资源的skin.apk

4. 在换肤时候调用loadSkin(skinpath)方法
   skinpath即为换肤apk的路径
```kotlin
 SkinManager.getInstance().loadSkin(skinpath)
```
 
