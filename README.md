某些场景下，我们需要根据用户的手机号，查询归属地。 我们有一个文件， `data/area.txt` ， 其格式如下：

```
1300000	354	测试地区.354
1300001	143	测试地区.143
1300002	393	测试地区.393
```

三列分别为prefix, area, name

请实现 `AreaService.java` ， 实现手机号查询归属地的功能。 要求与说明：
1. prefix不重复， 生产环境中， prefix最小为1300000， 最大1999999， 其中有些是没有的， 因此数据最大70万左右；
1. **code 与 name 一一对应**，表示地区， 生产环境去重后，约有400个地区；
1. 这个查询并发要求很高， 需要每秒最少上万次， 因此不能使用通过 redis 、mysql、ES等需要网络访问的存储方案；
1. 该代码最终会内嵌到其它业务系统， 因此要保证内存占用少 ， **常驻内存不能超过3 MB**；
1.  **提交代码前请确保测试 用例通过**。

