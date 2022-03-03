## 基本操作 



1. 创建索引 

    PUT /索引名称 

   举例: PUT /es_db 

2.  查询索引 

    GET /索引名称

   举例: GET /es_db 

3.  删除索引 

    DELETE /索引名称

   举例: DELETE /es_db 

4. 添加文档 

    PUT /索引名称/类型/id 

5. 修改文档 

   PUT /索引名称/类型/id 

![image-20220301152411935](/Users/madongming/notes/noteImg/image-20220301152411935.png)

![image-20220301152606098](/Users/madongming/notes/noteImg/image-20220301152606098.png)

![image-20220301152701352](/Users/madongming/notes/noteImg/image-20220301152701352.png)

![image-20220301152750854](/Users/madongming/notes/noteImg/image-20220301152750854.png)



## 批量获取文档数据

(1)在URL中不指定index和type

```java
GET _mget
{
"docs": [
{
"_index": "es_db",
"_type": "_doc",
"_id": 1
},
{
"_index": "es_db",
"_type": "_doc",
"_id": 2
}
]
}
```

(2)在URL中指定index

```java
GET /es_db/_mget
{
"docs": [
{
"_type":"_doc",
"_id": 3
},
{
"_type":"_doc",
"_id": 4
}
]
}
```

(3)在URL中指定index和type

```java
GET /es_db/_doc/_mget
{
"docs": [
{
"_id": 1
},
{
"_id": 2
}
]
}
```



## 批量操作文档数据

![image-20220301193818329](/Users/madongming/notes/noteImg/image-20220301193818329.png)

![image-20220301193837786](/Users/madongming/notes/noteImg/image-20220301193837786.png)



## **DSL语言高级查询**（领域专用语言）

![image-20220301194250966](/Users/madongming/notes/noteImg/image-20220301194250966.png)

### 无条件查询

```java
GET /es_db/_doc/_search
{
"query":{
"match_all":{}
}
}
```



### 有查询条件

- match : 通过match关键词模糊匹配条件内容

  match条件还支持以下参数：

  - query : 指定匹配的值
  - operator : 匹配条件类型

  - - and : 条件分词后都要匹配
    - or : 条件分词后有一个匹配即可(默认)

  - minmum_should_match : 指定最小匹配的数量

- prefix : 前缀匹配

- regexp : 通过正则表达式来匹配数据



### 精确匹配

- term : 单个条件相等
- terms : 单个字段属于某个值数组内的值
- range : 字段属于某个范围内的值
- exists : 某个字段的值是否存在
- ids : 通过ID批量查询



### 组合条件查询(多条件查询)

组合条件查询是将叶子条件查询语句进行组合而形成的一个完整的查询条件

- bool : 各条件之间有and,or或not的关系

- - must : 各个条件都必须满足，即各条件是and的关系
  - should : 各个条件有一个满足即可，即各条件是or的关系
  - must_not : 不满足所有条件，即各条件是not的关系
  - filter : 不计算相关度评分，它不计算_score即相关度评分，效率更高

- constant_score : 不计算相关度评分

**must/filter/shoud/must_not** 等的子条件是通过 **term/terms/range/ids/exists/match** 等叶子条件为参数的

注：以上参数，当只有一个搜索条件时，must等对应的是一个对象，当是多个条件时，对应的是一个数组



### 连接查询(多文档合并查询)

- 父子文档查询：parent/child
- 嵌套文档查询: nested





### 多字段模糊匹配查询与精准查询

```java
POST /es_db/_doc/_search
{
"query":{
"multi_match":{
"query":"张三",
"fields":["address","name"]
}
}
}
```

​           

### 未指定字段条件查询 query_string , 含 AND 与 OR 条件

```java
POST /es_db/_doc/_search
{
"query":{
"query_string":{
"query":"广州 OR 长沙"
}
}
}
```



### 指定字段条件查询 query_string , 含 AND 与 OR 条件

```java
POST /es_db/_doc/_search
{
"query":{
"query_string":{
"query":"admin OR 长沙",
"fields":["name","address"]
}
}
}
```



## 范围查询

range：范围关键字

gte 大于等于 Greater than equal

lte  小于等于 low than equal

gt 大于  Greater than

lt 小于 low than

now 当前时间



1.match 模糊查询

2.term 单词查询 全值匹配

3.match_phase 查询结果中顺序匹配

4.query_string 全字段检索



## 文档映射

ES中映射可以分为动态映射和静态映射

在文档写入Elasticsearch时，会根据文档字段自动识别类型，这种机制称之为动态映射。

静态映射是在Elasticsearch中也可以事先定义好映射，包含文档的各字段类型、分词器等，这种方式称之为静态映射。



默认put一个document如果不设置mapping的话那么每个字短就是按照动态映射

也可以手动指定映射

![image-20220301211949140](/Users/madongming/notes/noteImg/image-20220301211949140.png)

### 对已存在的mapping映射进行修改

![image-20220301212141323](/Users/madongming/notes/noteImg/image-20220301212141323.png)

![image-20220301213922458](/Users/madongming/notes/noteImg/image-20220301213922458.png)

## 高级语法

### boost权重控制

搜索document中remark字段中包含java的数据，如果remark中包含developer或architect，则包含architect的document优先显示。（就是将architect数据匹配时的相关度分数增加）。

![image-20220301222204120](/Users/madongming/notes/noteImg/image-20220301222204120.png)



### 基于dis_max实现best fields策略进行多字段搜索

![image-20220301222237600](/Users/madongming/notes/noteImg/image-20220301222237600.png)



### 基于tie_breaker参数优化dis_max搜索效果

![image-20220301222305985](/Users/madongming/notes/noteImg/image-20220301222305985.png)

