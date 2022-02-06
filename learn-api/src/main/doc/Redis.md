Redis: SDS,simple dynamic string

1.二进制安全的数据结构
2.提拱了内存与分配机制，避免了频繁的内存分配
3.兼容C语言函数库

C: char data[] ="guojia\0" 读取\0就结尾了

sdc：
 free：9
 len：6
 char buf[]="guojia" -> “guojia123”
 addlen:3
 (len+addlen) * 2 = 18



## Redis的五种数据结构

![image-20211112104553376](noteImg/image-20211112104553376.png)

### String

字符串常用操作
SET  key  value 			//存入字符串键值对
MSET  key  value [key value ...] 	//批量存储字符串键值对
SETNX  key  value 		//存入一个不存在的字符串键值对
GET  key 			//获取一个字符串键值
MGET  key  [key ...]	 	//批量获取字符串键值
DEL  key  [key ...] 		//删除一个键
EXPIRE  key  seconds 		//设置一个键的过期时间(秒)

原子加减
INCR  key 			//将key中储存的数字值加1
DECR  key 			//将key中储存的数字值减1
INCRBY  key  increment 		//将key所储存的值加上increment
DECRBY  key  decrement 	//将key所储存的值减去decrement

单值缓存
SET  key  value 	
GET  key 	

对象缓存
1) SET  user:1  value(json格式数据)
2) MSET  user:1:name  zhuge   user:1:balance  1888
    MGET  user:1:name   user:1:balance 
    分布式锁
    SETNX  product:10001  true 		//返回1代表获取锁成功
    SETNX  product:10001  true 		//返回0代表获取锁失败
    。。。执行业务操作
    DEL  product:10001			//执行完业务释放锁
    SET product:10001 true  ex  10  nx	//防止程序意外终止导致死锁

计数器
INCR article:readcount:{文章id}  	
GET article:readcount:{文章id} 

Web集群session共享
spring session + redis实现session共享

分布式系统全局序列号	
INCRBY  orderId  1000		//redis批量生成序列号提升性能



### Hash

Hash常用操作
HSET  key  field  value 			//存储一个哈希表key的键值
HSETNX  key  field  value 		//存储一个不存在的哈希表key的键值
HMSET  key  field  value [field value ...] 	//在一个哈希表key中存储多个键值对
HGET  key  field 				//获取哈希表key对应的field键值
HMGET  key  field  [field ...] 		//批量获取哈希表key中多个field键值
HDEL  key  field  [field ...] 		//删除哈希表key中的field键值
HLEN  key				//返回哈希表key中field的数量
HGETALL  key				//返回哈希表key中所有的键值

HINCRBY  key  field  increment 		//为哈希表key中field键的值加上增量increment

对象缓存
HMSET  user  {userId}:name  zhuge  {userId}:balance  1888
HMSET  user  1:name  zhuge  1:balance  1888
HMGET  user  1:name  1:balance  



电商购物车
1）以用户id为key
2）商品id为field
3）商品数量为value

购物车操作
添加商品hset cart:1001 10088 1
增加数量hincrby cart:1001 10088 1
商品总数hlen cart:1001
删除商品hdel cart:1001 10088
获取购物车所有商品hgetall cart:1001

优点
1）同类数据归类整合储存，方便数据管理
2）相比string操作消耗内存与cpu更小
3）相比string储存更节省空间

缺点
过期功能不能使用在field上，只能用在key上
Redis集群架构下不适合大规模使用



![image-20211112112216161](noteImg/image-20211112112216161.png)

### List

List常用操作
LPUSH  key  value [value ...] 		//将一个或多个值value插入到key列表的表头(最左边)
RPUSH  key  value [value ...]	 	//将一个或多个值value插入到key列表的表尾(最右边)
LPOP  key			//移除并返回key列表的头元素
RPOP  key			//移除并返回key列表的尾元素
LRANGE  key  start  stop		//返回列表key中指定区间内的元素，区间以偏移量start和stop指定

BLPOP  key  [key ...]  timeout	//从key列表表头弹出一个元素，若列表中没有元素，阻塞等待					timeout秒,如果timeout=0,一直阻塞等待
BRPOP  key  [key ...]  timeout 	//从key列表表尾弹出一个元素，若列表中没有元素，阻塞等待					timeout秒,如果timeout=0,一直阻塞等待

#### 常用数据结构

Stack(栈) = LPUSH + LPOP
Queue(队列）= LPUSH + RPOP
Blocking MQ(阻塞队列）= LPUSH + BRPOP



##### 微博消息和微信公号消息

诸葛老师关注了MacTalk，备胎说车等大V
1）MacTalk发微博，消息ID为10018
LPUSH  msg:{诸葛老师-ID}  10018
2）备胎说车发微博，消息ID为10086
LPUSH  msg:{诸葛老师-ID} 10086
3）查看最新微博消息
LRANGE  msg:{诸葛老师-ID}  0  4



### Set

SADD  key  member  [member ...]			//往集合key中存入元素，元素存在则忽略，
							若key不存在则新建
SREM  key  member  [member ...]			//从集合key中删除元素
SMEMBERS  key					//获取集合key中所有元素
SCARD  key					//获取集合key的元素个数
SISMEMBER  key  member			//判断member元素是否存在于集合key中
SRANDMEMBER  key  [count]			//从集合key中选出count个元素，元素不从key中删除
SPOP  key  [count]				//从集合key中选出count个元素，元素从key中删除

Set运算操作
SINTER  key  [key ...] 				//交集运算
SINTERSTORE  destination  key  [key ..]		//将交集结果存入新集合destination中
SUNION  key  [key ..] 				//并集运算
SUNIONSTORE  destination  key  [key ...]		//将并集结果存入新集合destination中
SDIFF  key  [key ...] 				//差集运算
SDIFFSTORE  destination  key  [key ...]		//将差集结果存入新集合destination中

集合操作实现微博微信关注模型

关住的人

我关注的人也关注他（共同关注的人）

集合操作实现电商商品筛选

选择cpu 还选择brand（品牌）



### ZSet

ZSet常用操作
ZADD key score member [[score member]…]	//往有序集合key中加入带分值元素
ZREM key member [member …]		//从有序集合key中删除元素
ZSCORE key member 			//返回有序集合key中元素member的分值
ZINCRBY key increment member		//为有序集合key中元素member的分值加上increment 
ZCARD key				//返回有序集合key中元素个数
ZRANGE key start stop [WITHSCORES]	//正序获取有序集合key从start下标到stop下标的元素
ZREVRANGE key start stop [WITHSCORES]	//倒序获取有序集合key从start下标到stop下标的元素

Zset集合操作
ZUNIONSTORE destkey numkeys key [key ...] 	//并集计算
ZINTERSTORE destkey numkeys key [key …]	//交集计算

![image-20211112151422170](noteImg/image-20211112151422170.png)

Zset集合操作实现排行榜
1）点击新闻
ZINCRBY  hotNews:20190819  1  守护香港
2）展示当日排行前十
ZREVRANGE  hotNews:20190819  0  9  WITHSCORES 
3）七日搜索榜单计算
ZUNIONSTORE  hotNews:20190813-20190819  7 
hotNews:20190813  hotNews:20190814... hotNews:20190819
4）展示七日排行前十
ZREVRANGE hotNews:20190813-20190819  0  9  WITHSCORES



遵循空字符结尾这一惯例的好处是，SDS可以直接重用一部分C字符串函数库里面的函数。

## Redis锁

![image-20211124230403632](noteImg/image-20211124230403632.png)

**redisSesson故障转移redis的线程1在master加了锁，转移到slave的时候数据没有同步，线程2在新的master节点上又加锁成功了。redlock超过半数redis节点加锁成功才算加锁成功**

![image-20211124231910627](noteImg/image-20211124231910627.png)

Redlock问题：

1.性能

2.数据回滚问题 网络抖动

3.redis设计之初就是为了高并发而准备的，一秒QPS可以达到10万，设计理念为背



高性能分布式锁实现：

利用分段式方法，concurrenthashmap方式



**集群模式下一个节点配置的主没有从 宕机了怎么办 默认无不可用**

![image-20211127185317154](noteImg/image-20211127185317154.png)



## ZskipList

层（level）：每次创建一个新跳跃表节点的时候，程序都根据**幂次定律**（power law，越大的数出现的概率越小）随机生成一个介于1和32之间的值作为level数组的大小，这个大小就是高度。

跨度（span）：是用来计算排位（rank）的：在查找某个节点的过程中，将沿途访问过的所有层的跨度累记起来，得到的结果就是目标节点在跳跃表中的排位。

```C++
/* ZSETs use a specialized version of Skiplists */
typedef struct zskiplistNode {
    sds ele;
    //按照分值从小到大排列
    double score;
    //指向当前节点的前一个节点
    struct zskiplistNode *backward;
    struct zskiplistLevel {
        //前进指针，用于访问位于表为方向的其他节点
        struct zskiplistNode *forward;
        //跨度，记录了前进指针所指向节点和当前节点的距离
        unsigned long span;
    } level[];
} zskiplistNode;

typedef struct zskiplist {
    struct zskiplistNode *header, *tail;
    //记录跳跃表的长度，也即是，跳跃表当前包含节点的数量（表头节点不计算在内）
    unsigned long length;
    //记录目前跳跃表里面，层数最大的那个节点的层数（表头节点的层数不计算在内）
    int level;
} zskiplist;

typedef struct zset {
    dict *dict;
    zskiplist *zsl;
} zset;
```

<img src="noteImg/image-20211206185332038.png" alt="image-20211206185332038" style="zoom:50%;" />





### 使用SDS字符串的好处

1. 常数复杂度获取字符串长度
2. 杜绝缓存区溢出（C字符串不记录自身的长度，如果把一个字符串添加到另一个字符串，第一个字符串空间大小不足以支撑两个字符串的话就会出现溢出）
3. 减少修改字符串时带来的内存重分配次数
4. 通过未使用空间，SDS实现了**空间预分配**和**惰性空间**释放两种优化策略
5. 二进制安全
6. 兼容部分C字符串函数

#### 空间预分配

SDS修改之后长度

小于1MB free为len属性同样大小的未使用空间

大于1MB free为固定1MB

#### 惰性空间释放

SDS避免了缩短字符串时所需的内存冲分配操作，并未将来可能有的增长操作提供了优化。

#### 二进制安全

C字符串中的字符必须符合某种编码（比如ASCII），并且除了字符串的末尾以外，字符串里面不能包含空字符，否则最先被程序读入的空字符将误认为是字符串结尾，这些限制使得C字符串只能保存文本数据，而不能保存图片，音频，视频，压缩文件这样的二进制数据 。

```java
int zslRandomLevel(void) { 

	int level = 1; 

	while ((random()&0xFFFF) < (ZSKIPLIST_P * 0xFFFF)) 

	level += 1; 

	return (level<ZSKIPLIST_MAXLEVEL) ? level : ZSKIPLIST_MAXLEVEL; 

}
```



## 脑裂问题

https://www.jianshu.com/p/8d045424042f

![img](https://upload-images.jianshu.io/upload_images/6618542-13a7c9d2b2568203.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)



## 字典

```c++
typedef struct dictEntry {
    void *key;
    union {
        void *val;
        uint64_t u64;
        int64_t s64;
        double d;
    } v;
    struct dictEntry *next;
} dictEntry;

typedef struct dictht {
    //哈希表数组
    dictEntry **table;
    //    hash表大小
    unsigned long size; //  hashtable 容量
    //    用于计算索引值
    unsigned long sizemask;  // size -1
    //    该哈希表已有节点的数量
    unsigned long used;
} dictht;

//字典结构
typedef struct dict {
    dictType *type;
    void *privdata;
//    渐进式的rehash
    dictht ht[2];// ht[0] , ht[1] =null
    long rehashidx; /* rehashing not in progress if rehashidx == -1 */
    unsigned long iterators; /* number of iterators currently running */
} dict;

typedef struct dictType {
    uint64_t (*hashFunction)(const void *key);
    void *(*keyDup)(void *privdata, const void *key);
    void *(*valDup)(void *privdata, const void *obj);
    int (*keyCompare)(void *privdata, const void *key1, const void *key2);
    void (*keyDestructor)(void *privdata, void *key);
    void (*valDestructor)(void *privdata, void *obj);
} dictType;
```

maxmemory <bytes>默认不开启

## rehash

### 哈希表的扩展与所容

1.服务器目前没有在执行bgsave或者bgrewriteaof命令，并且哈希表的负载因子大于等于1

2.服务器目前正在执行bgsave或者bgrewriteaof命令，并且哈希表的负载因子大于等于5

其中哈希表的负载因子可以通过公示：

load_factor=ht[0].used / ht[0].size



### 渐进式rehash

1.为ht[1]分配空间，让字典同时持有ht[0]和ht[1]两个哈希表

2.在字典中维持一个索引计数器变量rehashidx，并将他的值设置为0，表示rehash工作正式开始

3.在rehash进行期间，每次对字典执行添加、删除、查找或者更新操作时，程序除了执行指定的操作以外，还会顺带将ht[0]哈希表在rehashidx索引上的所有键值对rehash到ht[1]，当rehash工作完成之后，程序将rehashidx属性的值增一

4.随着字典操作的不断执行，最终在某个时间点上，ht[0]的所有键值对都会被rehash到ht[1]，这是程序将rehashidx属性的值设为1，表示rehash操作已完成

渐进式rehash的好处在于它采取分而治之的方式，将rehash键值对所需的计算工作均摊到对字典的每个添加、删除、查找和更新操作上，从而避免了集中式rehash而带来的庞大计算量



### 渐进式rehash执行期间的哈希表操作

​		因为在进行渐进式rehash的过程中，字典会同时使用ht[0]和ht[1]两个哈希表，所以在渐进式rehash进行期间，字典的删除、查找、更新等操作会在两个哈希表上进行。例如，要在字典里面查找一个键的话，程序会现在ht[0]里面进行查找，如果没找到的话，就会继续到ht[1]里面进行查找。

​		另外，在渐进式rehash执行期间，新添加到字典的键值对一律会被保存到ht[1]里面，而ht[0]则不再进行任何添加操作，这一措施保证了ht[0]包含的键值对数量会只减不增，并随着rehash操作的执行而最终变成空表。



## 跳跃表

```c#
/* ZSETs use a specialized version of Skiplists */
typedef struct zskiplistNode {
    sds ele;
    //按照分值从小到大排列
    double score;
    //指向当前节点的前一个节点
    struct zskiplistNode *backward;
    struct zskiplistLevel {
        //前进指针，用于访问位于表为方向的其他节点
        struct zskiplistNode *forward;
        //跨度，记录了前进指针所指向节点和当前节点的距离
        unsigned long span;
    } level[];
} zskiplistNode;

typedef struct zskiplist {
    struct zskiplistNode *header, *tail;
    //记录跳跃表的长度，也即是，跳跃表当前包含节点的数量（表头节点不计算在内）
    unsigned long length;
    //记录目前跳跃表里面，层数最大的那个节点的层数（表头节点的层数不计算在内）
    int level;
} zskiplist;
```



## 整数集合

整数集合（intset）是集合键的底层实现之一，当一个集合只包含整数值元素，并且这个集合的元素数量不多时，Redis就会使用整数集合作为集合键的底层实现。保存类型为，encoding（int16_t、int32_t、int64_t）。

第一次添加元素的时候会按照encoding类型和length属性决定contents数组大小。

```c#
typedef struct intset {
    uint32_t encoding;
    uint32_t length;
    int8_t contents[];
} intset;
```

### 升级

每当我们要将一个新元素添加到整数集合里面，并且新元素的类型比整数集合现有所有元素的类型都要长时，整数集合需要先进行升级（upgrade），然后才能将新元素添加到整数集合里面。

升级证书集合并添加新元素共分为三步进行：

1、根据新元素的类型，扩展整数集合底层数组的空间大小，并为新元素分配空间。

2、将底层数组现有的所有元素都转换成与新元素相同的类型，并将类型转换后的元素继续维持底层数组的有序性不变。

3、将新元素添加到底层数组里面。

### 好处

1、提高灵活性，可以任意插入不同类型的元素 int16_t、int32_t、int64_t

2、节约内存，对于只插入单一类型少位数的元素来说 完全可以使用更省内存的元素类型



## 压缩列表

压缩列表是Redis为了节约内存而开发的，是由一系列特殊编码的连续内存块组成的顺序型数据结构，一个压缩列表可以包含热议多个节点，每个节点保存一个字节数组或者一个整数值。



| 属性    | 类型     | 长度  | 用途                                                         |
| ------- | -------- | ----- | ------------------------------------------------------------ |
| zlbytes | uint32_t | 4字节 | 记录整个压缩列表占用的内存字节数：在对压缩列表进行内存重分配，或者计算zlend的位置时使用。 |
| zltail  | uint32_t | 4字节 | 记录压缩列表尾节点距离压缩列表的起始地址有多少字节：通过这个偏移量，程序无须遍历整个压缩列表就可以确定表尾节点的地址。 |
| zllen   | uint16_t | 2字节 | 记录了压缩列表包含的节点数量：当这个属性的值小于UINT16_MAX（65535）时，这个属性的值就是压缩列表包含节点的数量：当这个值等于UINT16_MAX时，节点的真实数量需要遍历整个压缩列表才能计算得出。 |
| entryX  | 列表节点 | 不定  | 压缩列表包含的各个节点，节点的长度由节点保存的内容决定。     |
| zlend   | uint8_t  | 1字节 | 特殊值0XFF（十进制255），用于标记压缩力表的末端。            |

每个压缩列表节点由previous_entry_length、encoding、content

previous_entry_length记录的是压缩列表中前一个节点的长度，小于254字节用1字节长度保存，添加节点和删除节点可能引起连锁更新（多个节点长度都是介于250危险空间大小，突然头插入了一个需要5字节保存的数值，导致后边的所有前指针都需要改变）

encoding属性记录了节点的content属性所保存数据的类型以及长度，大于254字节用5字节长度保存

content属性负责保存节点的值，节点值可以是一个字节数组或整数，值的类型和长度由节点的encoding属性决定



## 对象

Redis 使用对象来表示数据库中的键和值， 每次当我们在 Redis 的数据库中新创建一个键值对时， 我们至少会创建两个对象， 一个对象用作键值对的键（键对象）， 另一个对象用作键值对的值（值对象）。

```c#
//  redisObject对象 :  string , list ,set ,hash ,zset ...
typedef struct redisObject {
    //类型
    unsigned type:4;        //  4 bit, sting , hash
    //编码
    unsigned encoding:4;    //  4 bit 
    unsigned lru:LRU_BITS; /* LRU time (relative to global lru_clock) or
                            * LFU data (least significant 8 bits frequency
                            * and most significant 16 bits access time). 
                            *    24 bit 
                            * */
    int refcount;           // 4 byte  
  	//指向底层数据结构实现的指针
    void *ptr;              // 8 byte  总空间:  4 bit + 4 bit + 24 bit + 4 byte + 8 byte = 16 byte  
} robj;
```

https://github.com/Snailclimb/JavaGuide/blob/main/docs/database/redis/redis%E7%9F%A5%E8%AF%86%E7%82%B9&%E9%9D%A2%E8%AF%95%E9%A2%98%E6%80%BB%E7%BB%93.md

![image-20220119225736048](./noteImg/image-20220119225736048.png)

![image-20220119225835339](./noteImg/image-20220119225835339.png)

type命令相当于输出值的类型

object encoding 相当于输出的是底层具体的实现

![image-20220119230320108](./noteImg/image-20220119230320108.png)



## 字符串对象

字符串对象的编码可以是int，raw或者embstr

如果字符串对象保存的是一个字符串值， 并且这个字符串值的长度小于等于 `39` 字节， 那么字符串对象将使用 `embstr` 编码的方式来保存这个字符串值。

`embstr` 编码是专门用于保存短字符串的一种优化编码方式， 这种编码和 `raw` 编码一样， 都使用 `redisObject` 结构和 `sdshdr` 结构来表示字符串对象， 但 `raw` 编码会调用两次内存分配函数来分别创建 `redisObject` 结构和 `sdshdr` 结构， 而 `embstr` 编码则通过调用一次内存分配函数来分配一块连续的空间， 空间中依次包含 `redisObject` 和 `sdshdr` 两个结构

如果字符串对象保存的是一个字符串值， 并且这个字符串值的长度大于 `39` 字节， 那么字符串对象将使用一个简单动态字符串（SDS）来保存这个字符串值， 并将对象的编码设置为 `raw` 。

对于longdouble类型浮点数也是用字符串表示的，如果对浮点数有操作的时候先转换为浮点数做运算，然后再转换为字符串。

![image-20220120103501737](./noteImg/image-20220120103501737.png)



## 列表对象

列表对象的编码可以是 `ziplist` 或者 `linkedlist` 。

```c#
typedef struct listNode {
    struct listNode *prev;
    struct listNode *next;
    void *value;
} listNode;

typedef struct listIter {
    listNode *next;
    int direction;
} listIter;

typedef struct list {
    listNode *head;
    listNode *tail;
    void *(*dup)(void *ptr);
    void (*free)(void *ptr);
    int (*match)(void *ptr, void *key);
    unsigned long len;
} list;
```



### 编码转换

当列表对象可以同时满足以下两个条件时， 列表对象使用 `ziplist` 编码：

1. 列表对象保存的所有字符串元素的长度都小于 `64` 字节；
2. 列表对象保存的元素数量小于 `512` 个；

不能满足这两个条件的列表对象需要使用 `linkedlist` 编码。

List-max-ziplist-value	

List-max-ziplist-entries

![image-20220120105433559](./noteImg/image-20220120105433559.png)



## 哈希对象

哈希对象的编码可以是 `ziplist` 或者 `hashtable` 。

`ziplist` 编码的哈希对象使用压缩列表作为底层实现， 每当有新的键值对要加入到哈希对象时， 程序会先将保存了键的压缩列表节点推入到压缩列表表尾， 然后再将保存了值的压缩列表节点推入到压缩列表表尾， 因此：

- 保存了同一键值对的两个节点总是紧挨在一起， 保存键的节点在前， 保存值的节点在后；

- 先添加到哈希对象中的键值对会被放在压缩列表的表头方向， 而后来添加到哈希对象中的键值对会被放在压缩列表的表尾方向。

  

### 编码转换

****

当哈希对象可以同时满足以下两个条件时， 哈希对象使用 `ziplist` 编码：

1. 哈希对象保存的所有键值对的键和值的字符串长度都小于 `64` 字节；
2. 哈希对象保存的键值对数量小于 `512` 个；

不能满足这两个条件的哈希对象需要使用 `hashtable` 编码。

hash-max-ziplist-entries 512
hash-max-ziplist-value 64

![image-20220120112243824](./noteImg/image-20220120112243824.png)



## 集合对象

集合对象的编码可以是 `intset` 或者 `hashtable` 。

`intset` 编码的集合对象使用整数集合作为底层实现， 集合对象包含的所有元素都被保存在整数集合里面。

### 编码的转换

****

当集合对象可以同时满足以下两个条件时， 对象使用 `intset` 编码：

1. 集合对象保存的所有元素都是整数值；
2. 集合对象保存的元素数量不超过 `512` 个；

不能满足这两个条件的集合对象需要使用 `hashtable` 编码。

set-max-intset-entries 512

![image-20220120113626025](./noteImg/image-20220120113626025.png)



## 有序集合对象

有序集合的编码可以是 `ziplist` 或者 `skiplist` 。

`ziplist` 编码的有序集合对象使用压缩列表作为底层实现， 每个集合元素使用两个紧挨在一起的压缩列表节点来保存， 第一个节点保存元素的成员（member）， 而第二个元素则保存元素的分值（score）。

压缩列表内的集合元素按分值从小到大进行排序， 分值较小的元素被放置在靠近表头的方向， 而分值较大的元素则被放置在靠近表尾的方向。

### 编码的转换

*****

当有序集合对象可以同时满足以下两个条件时， 对象使用 `ziplist` 编码：

1. 有序集合保存的所有元素成员的长度都小于 `64` 字节；
2. 有序集合保存的元素数量小于 `128` 个；

不能满足以上两个条件的有序集合对象将使用 `skiplist` 编码。

zset-max-ziplist-entries 128
zset-max-ziplist-value 64

![image-20220120115248702](./noteImg/image-20220120115248702.png)





# 文件事件处理器的构成

分别为套接字、I/O多路复用程序、文件时间分排器、事件处理器

I/O多路复用器程序总是会将所有产生事件的套接字都放到一个队列里面，然后通过这个队列，以有序（sequentially），同步（synchronously），每次一个套接字的方式向文件时间分派器传送套接字。



## I/O多路复用程序的实现

​		Redis的I/O多路复用程序的所有功能都是通过包装常见的select、epoll、evport、和kqueue这些I/O多路复用函数库实现的，每个I/O多路复用函数在Redis源码中都对应一个单独的文件，比如as_select.c ae_epoll.c ae_kqueue.c

​		程序编译时自动选择系统中性能最高的I/O多路复用函数库来作为Redis的I/O多路复用程序的底层实现。



## 中断

​		中断使得硬件得以发出通知给处理器。例如在你敲击键盘的时候，键盘控制器（控制键盘的硬件设备）会发出一个中断，通知操作系统有按键按下，中断本质上是一种特殊的电信号，由硬件设备发出向处理器。处理器接收到中断后，会马上向操作系统反映此信号的到来，然后就有操作系统负责处理这些新到来的数据。硬件设备生成中断的时候并不需要考虑与处理器的时钟同步，换句话说就是中断随时可以产生。因此，内核随时可能因为新到来的中断而被打断。

​		在响应一个特定中断的时候，内核会执行一个函数，该函数叫做中断处理程序或者中断服务程序，产生中断的每个设备都有一个响应的中断处理程序。





# 数据库

```c#
typedef struct redisDb {
    dict *dict;                 /* The keyspace for this DB    */
    dict *expires;              /* Timeout of keys with a timeout set    过期时间字典 */
    dict *blocking_keys;        /* Keys with clients waiting for data (BLPOP)*/
    dict *ready_keys;           /* Blocked keys that received a PUSH */
    dict *watched_keys;         /* WATCHED keys for MULTI/EXEC CAS */
    int id;                     /* Database ID */
    long long avg_ttl;          /* Average TTL, just for stats */
    unsigned long expires_cursor; /* Cursor of the active expire cycle. */
    list *defrag_later;         /* List of key names to attempt to defrag one by one, gradually. */
} redisDb;

struct redisServer {
  //...
  redisDb *db
  int dbnum;根据配置文件来决定创建多少个db
  //...
}
```



## Sentinel

当server1的下线时长超过用户设定的下线时长上限时，Sentinel系统就会对server1执行故障转移操作：

- 首先，sentinel系统就会挑选server1属下的其中一个从服务器，并将这个被选中的从服务器升级为新的主服务器。
- 之后，sentinel系统会向server1属下的所有从服务器发送新的复制指令，让他们成为新的主服务器的从服务器，当所有从服务器都开始复制新的主服务器时，故障转移操作执行完毕。
- 另外，sentinel还会继续见识已下线的server1，并在他重新上线时，将它设置为新的主服务器的从服务器。



### 获取主服务器信息

Sentinel默认会以每10秒一次的频率，通过命令连接向被监视的主服务器发送info命令，并通过分析info命令的回复来获取主服务器的当前信息。通过主服务器返回的info命令的回复，sentinel可以获取以下两方面的信息

- 一是关于主服务器本身的信息
- 二是关于主服务器属下所有从服务器的信息



### 向主服务器和从服务器发送信息

默认情况下，sentinel会以每两秒一次的频率，通过命令连接向所有被监视的主服务器和从服务器发送以下格式的命令

publsh __sentinel__ hello"<s_ip>,<s_port>,<s_runid>,<s_epoch>,<m_name>,<m_ip>,<m_port>,<m_epoch>"

- 其中以s_开头的参数记录的是sentinel本身的信息，各个参数的意义
- 而m_开头的参数记录的则是sentinel本身的信息，如果sentienl正在监视的是主服务器，那么这些参数记录的就是主服务器的信息，如果sentinel正在监视的是从服务器，那么这些参数记录的就是从服务器正在复制的主服务器的信息

| 参数    |          意义          |
| :------ | :--------------------: |
| s_ip    |    sentinel的ip地址    |
| s_port  |    sentinel的端口号    |
| s_runid |    sentinel的运行id    |
| s_epoch | sentinel当前的配置纪元 |
| m_name  |     主服务器的名字     |
| m_ip    |    主服务器的ip地址    |
| m_port  |    主服务器的端口号    |
| m_epoch | 主服务器当前的配置纪元 |



### 检测主观下线

默认情况下，sentinel会以每秒一次的频率向所有与它创建了命令连接的实例（包括主服务器，从服务器，其他sentinel在内）发送ping命令

sentinel配置文件中的down-after-milliseconds选项指定了sentinel判断实例进入主观下线所需的时间长度，如果一个实例在down-after-milliseconds毫秒内，连续向sentienl返回无效回复，那么sentienl会修改这个实例所对应的实例结构。



### 检测客观下线

当sentinel将一个主服务器判断为主观下线之后，为了确认这个主服务器是否真的下线了，他会想同样监视这 一主服务器的其他sentinel进行询问，看他们是否也认为主服务器已经经入了下线状态（可以是主观下线或者客观下线）。当sentinel从其他sentinel哪里接收到足够数量的已下线判断之后，sentienl就会将从服务器判定为客观下线，并对主服务器执行故障转移操作。



### 发送sentinel is-master-down-by-add命令

sentienl使用：

sentienl is-master-down-by-addr <ip> <port> <current_epoch> <runid>

命令询问其他sentinel是否同意主服务器已下线



| 参数          |                             意义                             |
| :------------ | :----------------------------------------------------------: |
| ip            |          被sentinel判断为主观下线的主服务器的ip地址          |
| port          |          被sentinel判断为主观下线的主服务器的端口号          |
| current_epoch |         sentinel当前的配置纪元，用于选举领头sentinel         |
| runid         | 可以是*符号或者sentinel的运行id，*符号代表的是检测主观下线状态，sentienl的运行id则用于选举领头sentienl |

### 接收sentinel is-master-dow-by-add命令

当一个sentinel（目标sentienl）接收到另一个sentinel（源sentinel）发来的sentinel is-master-down-by-add命令时，目标sentinel会分析并取出命令请求中包含的各个参数，并根据其中的主服务器ip和端口号，检查主服务器是否已经下线，然后向源sentienl返回一条包含三个参数的multi bulk回复作为sentinel is-master-down-by命令的回复：

1. <down_state>
2. <leader_runid>
3. <leader_epoch>

| 参数         | 意义                                                         |
| ------------ | ------------------------------------------------------------ |
| dwon_state   | 返回目标sentienl对主服务器的检查结果，1代表主服务器已下线 0代表主服务器未下线 |
| leader_runid | 可以是*符号或者目标sentinel的局部领头sentinel的运行id：符号代表命令仅仅用于检测主服务器的下线状态，而局部领头sentinel的运行id则用于选举领头sentinel |
| leader_epoch | 目标sentinel的局部领头sentinel的配置纪元，用于选举领头sentinel，仅用于leader_runid的值不为*时，如果为,那么leader_epoch总为0 |

当这一数量达到配置指定的判断客观下线所需的数量时，sentinel就会将主服务器实例结构flags属性的STR—O—DOWN标识打开，表示主服务器已经进入客观下线状态。

当认为主服务器已经进入下线状态的sentinel的数量，超过sentinel配置中心配置的quorum参数的值，那么该sentinel就会认为主服务器已经进入客观下线状态，比如，如果sentinel在启动时载入了以下配置

sentinel monitor master 127.0.0.1 6379 2

那么包括当前sentienl在内，只要总共有两个sentienl认为主服务器已经进入下线状态，那么当前sentienl就会将主服务器判断为客观下线



## 选举领头sentinel

当一个主服务器被判断为客观下线时，监视这个下线服务器的各个sentinel会进行协商，选举出一个领头sentinel，并由领头sentinel对下线主服务器执行故障转移操作。

以下是redis选举领头sentinel的规则和方法。

- 所有在线的sentinel都有被选为领头sentinel的资格，换句话说监视同一个主服务器的多个在线sentinel中的任意的一个都有可能成为领头sentinel。
- 每次进行领头sentinel选举之后，不论选举是否成功，所有sentinel的配置纪元的值都会自增一次，配置纪元实际就是一个计数器，并没有什么特别的。
- 在一个配置纪元里面，所有sentinel都有一次将某个sentinel设置为局部领头sentinel的机会，并且局部领头一旦设置，在这个纪元里面就不能再更改。
- 每个发现主服务器进入客观下线的sentinel都会要求其他sentinel将自己设置为局部领头sentinel。
- 当一个sentinel（源sentinel）向另一个sentinel（目标sentinel）发送sentinel is-master-down-by-add命令，并且命令中的runid参数不是*符号而是源sentinel的运行id时，这表示源sentinel要求目标sentinel将前者设置为后者的局部领头sentinel。
- sentinel设置局部领头sentinel的规则是先到先得：最先向目标sentinel发送设置要求的源sentinel将成为目标sentinel的局部领头sentinel，而之后接收到的所有设置要求都会被目标sentinel拒绝。
- 目标sentinel在接收到sentinel is-master-down-by-addr命令之后，将向源sentinel返回一条命令回复，回复中的leader_runid参数和leader_epoch参数分别记录了目标sentinel的局部sentinel的运行id和配置纪元。
- 源sentinel在接收到目标sentinel返回的命令回复之后，会检查回复中leader_epoch参数的值和自己的配置纪元是否相同，如果相同的话，那么源sentinel继续取出回复中的leader_runid参数，如果ledaer_runid参数的值和源sentinel的运行id一直，那么表示目标sentinel将源sentinel设置成了局部领头sentinel。
- 如果有某个sentinel被半数以上的sentinel设置成了局部领头sentienl，那么这个sentinel称为领头sentinel。举个例子，在一个由10个sentinel组成的sentinel系统里面，只要有大于等于10/2+1=6个sentinel将某个sentienl设置为局部领头sentienl，那么被设置的那个sentinel就会成为领头sentienl。
- 因为领头sentinel的产生需要半数以上sentinel的支持，并且每个sentienl在每个配置纪元里面只能设置一次局部领头sentinel，所以在一个配置纪元里面，只会出现一个领头sentinel。
- 如果在给定时限内，没有一个sentinel被选举为领头sentinel，那么各个sentinel将在一段时间之后再次进行选举，知道选出领头sentinel为止。

### 故障转移

在选举产生出领头sentinel之后，领头sentinel将对已下线的主服务器执行故障转移操作，该操作包含以下三个步骤：

1. 在已下线主服务器属下的所有从服务器里面，挑选出一个从服务器，并将其转换为主服务器。
2. 让已下线主服务器属下的所有从服务器改为复制新的主服务器。
3. 将已下线主服务器设置为新的主服务器的从服务器，当这个旧的主服务器重新上线时，他就会成为新的主服务器的从服务器。

### 选出新的主服务器

故障转移第一步要做的就是在已下线主服务器属下的所有从给服务器中，挑选一个状态良好，数据完整的从服务器，然后向这个从服务器发送slaveof no one命令，将这个从服务器转换为主服务器。

#### 新的主服务器时怎样挑选出来的

领头sentinel会将已下线主服务器的所有从服务器保存到一个列表里面，然后按照一下规则，一项一项地对列表进行过滤:

1. 删除列表中所有处于下线或者断线状态的从服务器，这可以保证列表中剩余的从服务器都是正常在线的

2. 删除列表中所有最近5秒内没有回复过领头sentinel的info命令的从服务器，这可以保证列表剩余的从服务器 都是最近成功进行通信的。

3. 删除所有与已下线主服务器连接断开超过down-after-milliseconds * 10 毫秒的从服务器：down-after-milliseconds 选项指定了判断主服务器下线所需的时间，而删除断开时常超过down-after-milliseconds * 10 毫秒的从服务器，则可以保证列表中剩余的从服务器都没有过早地与主服务器断开连接，换句话说，列表中剩余的从服务器保存的数据都是比较新的。之后，领头sentinel将根据服务器的优先级，对列表中的剩余从服务器进行排序，并选出其中优先级最高的从服务器。

   ​		如果有多个具有相同最高优先级的从服务器，那么领头sentinel将按照从服务器的复制偏移量，对具有相同最高优先级的所有从服务器进行排序，并选出其中偏移量最大的从服务器（复制偏移量最大的从服务器就是保存着最新数据的从服务器）

   ​		最后，如果有多个优先级最高、复制偏移量最大的从服务器，那么领头sentinel将按照运行id对这些从服务器进行排序，并选出其中运行id最小的从服务器。



## 集群选举新的主节点

新的主节点是通过选举产生的，以下是集群选举新的主节点的方法。

1. 集群的配置纪元是一个自增计数器，它的初始值为0

2. 当集群里的某个节点开始一次故障转移操作时，集群配置纪元的值会被增1

3. 对于每个配置纪元，集群里每个负责处理槽的主节点都有一次投票的机会，而第一个向主节点要求投票的从节点将获得主节点的投票。

4. 当从节点发现自己正在复制的主节点进入已下线状态时，从节点会向集群广播一条clustermsg_type_failover_auth_request消息，要求所有收到这条消息，并且具有投票权的主节点想这个从节点投票。

5. 如果一个主节点具有投票权（它正在负责处理槽），并且这个主节点尚未投票给其他从节点，那么主节点将向要求投票的从节点返回一条clustermas_type_failover_auth_ack消息，表示这个主节点支持从节点称为新的主节点。

6. 每个参与选举的从节点都会接收clustermsg_type_failover_auth_ack消息，并根据自己收到了多少条这种消息来统计自己获得了多少主节点的支持。

7. 如果集群有N个具有投票权的主节点，那么当一个从节点收集到大于等于n/2+1张支持票时，这个从节点就会当选称为新的主节点。

8. 因为在每一个配置纪元里面，每个具有投票权的主节点只能头一次票，所以如果有N个主节点进行投票，那么具有大于等于N/2+1支持票的从节点只会有一个，这确保了新的主节点只会有一个。

9. 如果在一个配置纪元里面没有从节点能收集到足够多的支持票，那么集群进入一个新的配置纪元，并再次进行选举，知道选出新的主节点为止。

   





### 设置键的生存时间或过期时间

****

通过EXPIRE命令或者PEXPIRE命令，客户端可以以秒或者毫秒精度为数据库中的某个键设置生存时间（Time To Live，TTL），在经过指定的秒数或者毫秒数之后，服务器就会自动删除生存时间为0的键。

与EXPIRE命令和PEXPIRE命令类似，客户端可以通过EXPIREAT命令或PEXPIREAT命令，以秒或者毫秒精度给数据库中的某个键设置过期时间（expire time）。

过期时间是一个UNIX时间戳，当键的过期时间来临时，服务器就会自动从数据库中删除这个键。

TTL命令和PTTL命令接受一个带有生存时间或者过期时间的键，返回这个键的剩余生存时间，也就是，返回距离这个键被服务器自动删除还有多长时间。

Redis有四种不同的命令可以用于设置键生存时间（键可以存在多久）或过期时间（键什么时候被删除）

expire key ttl 命令用于将键key的生存时间设置为ttl秒

pexpire key ttl 命令用于将键key的生存时间设置为ttl毫秒

expireat key timestamp 命令用于将键key的过期时间设置为timestamp所指定的秒数时间戳

pexpireat key timestamp 命令用于将键key的过期时间设置为timestamp所指定的毫秒数时间戳

虽然有多种不同单位和不同形式的设置命令，但实际上EXPIRE、PEXPIRE、EXPIREAT 三个命令都是使用PEXPIREAT命令来实现的：无论客户端执行的是以上四个命令中的哪一个，经过转换之后，最终的执行效果都和执行PEXPIREAT命令一样。



### 保存过期时间

****

redisDb结构的expires字典保存了数据库中所有键的过期时间，我们称这个字典为过期字典：

- 过期字典的键是一个指针，这个指针指向键空间中的某个键对象（也即是某个数据库键）
- 过期字典的值是一个long long类型的整数，这个整数保存了键所指向的数据库键的过期时间——一个毫秒精度的UNIX时间戳



### 移除过期时间

****

PERSIST命令可以移除一个键的过期时间

惰性删除策略的实现

```c#
int expireIfNeeded(redisDb *db, robj *key) {

    if (!keyIsExpired(db,key)) return 0; // 未过期 

    /* If we are running in the context of a slave, instead of
     * evicting the expired key from the database, we return ASAP:
     * the slave key expiration is controlled by the master that will
     * send us synthesized DEL operations for expired keys.
     *
     * Still we try to return the right information to the caller,
     * that is, 0 if we think the key should be still valid, 1 if
     * we think the key is expired at this time. */
    if (server.masterhost != NULL) return 1;

    /* Delete the key */
    server.stat_expiredkeys++;
    propagateExpire(db,key,server.lazyfree_lazy_expire);
    notifyKeyspaceEvent(NOTIFY_EXPIRED,
        "expired",key,db->id);
    int retval = server.lazyfree_lazy_expire ? dbAsyncDelete(db,key) :   // 异步删除
                                               dbSyncDelete(db,key);     // 同步删除
    if (retval) signalModifiedKey(NULL,db,key);
    return retval;
}
```



定期删除策略的实现

过期键的定期删除策略由expire.c/activeExpireCycle函数实现，每当Redis的服务器周期性操作redis.c/serverCron函数执行时，activeExpireCycle函数就会被调用，他在规定的时间内，分多次遍历服务器中的各个数据库，从数据库的expire字典中随机检查一部分键的过期时间，并删除其中的键。



### 生成RDB文件

****

在执行SAVE命令或者BGSAVE命令创建一个新的RDB文件时，程序会对数据库中的键进行检查，已经过期的键不会被保存到新创建的RDB文件中。

### 载入RDB文件

****

在启动Redis服务时，如果服务器开启了RDB功能，那么服务器将对RDB文件进行载入：

1、如果服务器以主服务器模式运行，那么在载入RDB文件时，程序会对文件中保存的键进行检查，未过期的键会被载入到数据库中，而过期键则会被忽略，所以过期键对载入RDB文件的主服务器不会造成影响。

2、如果服务器以从服务器模式运行，那么在载入RDB文件时，文件中保存的所有键，不论是否过期，都会被载入到数据库中。不过，因为主从服务器在进行数据同步的时候，从服务器的数据就会被清空，所以一般来讲，过期键对载入RDB文件的从服务器也不会造成影响。



## BGSAVE命令执行时的服务器状态

BGSAVE命令执行期间，服务器处理SAVE，BGSAVE，BGREWRITEAOF三个命令的方式回合平时有所不同。

首先在BGSAVE命令执行期间，客户端发送的save和bgsave命令会被服务器拒绝，服务器禁止save命令和bgsave命令同时执行是为了避免父进程和子进程同时执行两个调用防止竞争条件。

bgrewirteaof命令和bgsave两个命令不能同时执行。

- 如果bgsave命令正在执行，那么客户端发送的bgrewirteaof命令会被延迟到bgsave命令执行完毕之后在执行。
- 如果bgrewireaof命令正在执行，那么客户端发送的bgsave命令会被服务器拒绝。

服务器程序会根据save选项的保存条件 设置服务器状态的redisserver结构的redisparams属性，他里面的每一个saveparam都是一个保存条件。

```c#

struct redisServer {
    ……
struct saveparam *saveparams;   /* Save points array for RDB */
}

struct saveparam {
    //秒数
    time_t seconds;
    //修改数
    int changes;
};
```

### ditry计数器和lastsave属性

****

除了saveparams数组之外，服务器状态还维持着一个dirty计数器，以及一个lastsave属性。

- dirty计数器纪录距离上一次成功执行save命令或者bgsave命令之后，服务器对数据库状态（服务器中的所有数据库）进行了多少次修改（包括写入、删除、更新等操作）
- lastsave属性是一个时间戳，记录了服务器上一次成功执行save命令或者bgsave命令的时间。



### 检查保存条件是否满足

****

redis的服务器周期性操作函数servercron默认每隔100毫秒就会执行一次，该函数用于对正在运行的服务器进行维护，他的其中一项工作就是检查save选项所设置的保存条件是否已经满足，如果满足的话就会执行bgsave命令。

关于分析RDB文件的说明，因为Redis本身带有RDB文件检查工具redis-check-dump。



### AOF文件写入

****

当过期键被惰性删除或者定期删除之后，程序会向AOF文件追加（append）一条DEL命令，来显式地记录该键已被删除。

### AOF重写

****

在执行AOF重写的过程中，程序会对数据库中的键进行检查，已过期的键不会保存到重写后的AOF文件中。

## AOF持久化

AOF持久化功能的实现可以分为命令追加、文件写入、文件同步三个步骤。

### 1、命令追加

当AOF持久化功能处于打开状态时，服务器在执行完一个写命令之后，会以协议格式将被执行的写命令追加到服务器状态的aof_buf缓冲区的末尾。

### 2、AOF文件的写入与同步

Redis 的服务器进程就是一个事件循环（loop）， 这个循环中的文件事件负责接收客户端的命令请求， 以及向客户端发送命令回复， 而时间事件则负责执行像 `serverCron` 函数这样需要定时运行的函数。

因为服务器在处理文件事件时可能会执行写命令， 使得一些内容被追加到 `aof_buf` 缓冲区里面， 所以在服务器每次结束一个事件循环之前， 它都会调用 `flushAppendOnlyFile` 函数， 考虑是否需要将 `aof_buf` 缓冲区中的内容写入和保存到 AOF 文件里面。

| `appendfsync` 选项的值 | `flushAppendOnlyFile` 函数的行为                             |
| :--------------------- | :----------------------------------------------------------- |
| `always`               | 将 `aof_buf` 缓冲区中的所有内容写入并同步到 AOF 文件。       |
| `everysec`             | 将 `aof_buf` 缓冲区中的所有内容写入到 AOF 文件， 如果上次同步 AOF 文件的时间距离现在超过一秒钟， 那么再次对 AOF 文件进行同步， 并且这个同步操作是由一个线程专门负责执行的。 |
| `no`                   | 将 `aof_buf` 缓冲区中的所有内容写入到 AOF 文件， 但并不对 AOF 文件进行同步， 何时同步由操作系统来决定。 |

```text
文件的写入和同步

为了提高文件的写入效率， 在现代操作系统中， 当用户调用 `write` 函数， 将一些数据写入到文件的时候， 操作系统通常会将写入数据暂时保存在一个内存缓冲区里面， 等到缓冲区的空间被填满、或者超过了指定的时限之后， 才真正地将缓冲区中的数据写入到磁盘里面。

这种做法虽然提高了效率， 但也为写入数据带来了安全问题， 因为如果计算机发生停机， 那么保存在内存缓冲区里面的写入数据将会丢失。

为此， 系统提供了 `fsync` 和 `fdatasync` 两个同步函数， 它们可以强制让操作系统立即将缓冲区中的数据写入到硬盘里面， 从而确保写入数据的安全性。
```



## 文件事件

Redis 基于 [Reactor 模式](http://en.wikipedia.org/wiki/Reactor_pattern)开发了自己的网络事件处理器： 这个处理器被称为文件事件处理器（file event handler）：

- 文件事件处理器使用 [I/O 多路复用（multiplexing）](http://en.wikipedia.org/wiki/Multiplexing)程序来同时监听多个套接字， 并根据套接字目前执行的任务来为套接字关联不同的事件处理器。
- 当被监听的套接字准备好执行连接应答（accept）、读取（read）、写入（write）、关闭（close）等操作时， 与操作相对应的文件事件就会产生， 这时文件事件处理器就会调用套接字之前关联好的事件处理器来处理这些事件。

虽然文件事件处理器以单线程方式运行， 但通过使用 I/O 多路复用程序来监听多个套接字， 文件事件处理器既实现了高性能的网络通信模型， 又可以很好地与 Redis 服务器中其他同样以单线程方式运行的模块进行对接， 这保持了 Redis 内部单线程设计的简单性。

![image-20220121170554295](./noteImg/image-20220121170554295.png)



## 命令执行器

​		命令执行器要做的第一件事情就是根据客户端状态的argv[0]参数，在命令表（command table）中查找参数所指定的命令，并将找到的命令保存到客户端状态的cmd属性里面。

​		命令表是一个字典，字典的健是一个个命令名字，比如“set” “get” “del”等等；而字典的值是一个个redisCommand结构，每个redisCommand结构记录了一个Redis命令的实现信息。

```c#
struct redisCommand {
    char *name;
    redisCommandProc *proc;
    int arity;
    char *sflags;   /* Flags as string representation, one char per flag. */
    uint64_t flags; /* The actual flags, obtained from the 'sflags' field. */
    /* Use a function to determine keys arguments in a command line.
     * Used for Redis Cluster redirect. */
    redisGetKeysProc *getkeys_proc;
    /* What keys should be loaded in background when calling this command? */
    int firstkey; /* The first argument that's a key (0 = no keys) */
    int lastkey;  /* The last argument that's a key */
    int keystep;  /* The step between first and last key */
    long long microseconds, calls;
    int id;     /* Command ID. This is a progressive ID starting from 0 that
                   is assigned at runtime, and is used in order to check
                   ACLs. A connection is able to execute a given command if
                   the user associated to the connection has this command
                   bit set in the bitmap of allowed commands. */
};
```



## 主从复制

Redis具有高可靠性，主要体现在两方面，一是数据尽量少丢失，二是服务尽量少中断。AOF和RDB保证了第一点，而第二点的实现，Redis采用增加副本冗余量的方式。

Redis提供主从库模式来保证数据副本一致，主从库采用读写分离。读操作可以通过主库或从库，而写操作首先到主库执行，再由主库将写操作同步给从库。

为什么要采用读写分离的方式呢？

如果客户端对同一个数据进行多次修改，每次写操作都发送到不同的实例上，会导致实例副本数据不一致。这种情况下如果要做到数据一致，就会涉及到加锁、实例之间协商是否完成修改等一系列操作，带来巨额开销。而读写分离把所有写操作的请求都发送到主库上，然后同步给从库，多个实例之间就不用协调了。

主从库同步是如何完成的？

启动多个Redis实例的时候，通过replicaof命令形成主从库关系。（Redis 5.0以前使用slaveof）

![img](https://img-blog.csdnimg.cn/img_convert/1c92d91323819347b52cae093008650a.png)

第一阶段，从库和主库建立连接，告诉主库即将进行同步，主库确认恢复后，主从库间就可以开始同步了，psync命令包含runID和offset两个参数，runID是主库实例的随机ID，由于第一次连接时不知道主库runID，所以是“?”，offset是复制进度，-1表示第一次复制。主库收到psync命令后，使用FULLRESYNC命带上runID和offset返回给从库，FULLRESYNC表示第一次复制采用全量复制。

第二阶段，主库将所有数据同步给从库，从库收到数据后，在本地完成清空当前数据并重新进行数据加载。

第三阶段，主库把第二阶段实行过程中收到的写命令记录在replication buffer中，当主库完成RDB文件发送后，再把replication buffer中的写操作发送给从库。这样就实现主从同步了。

如果有多个从库，而且都要和主库进行全量复制的话，就会导致主库相应应用程序的请求变慢，可以通过“主-从-从”模式，将主库生成RDB和传输RDB的压力，以级联的方式分散到从库上。

![img](https://img-blog.csdnimg.cn/img_convert/f4d88b7c7a37244a2bb8edd801ceb4db.png)

一旦从库完成了全量复制，他们会一直维护一个网络连接，主库会通过这个连接将后续收到的命令操作同步给从库。这个过程称为基于长连接的命令传播。

主从库间网络断了怎么办？

Redis 2.8开始，网络中断后，主从库采用增量复制的方式继续同步，把主从库网络断开期间主库收到的命令，同步给从库。

当主从断连后，主库会把断连期间收到的写操作命令，写入repl_backlog_buffer这个缓冲区。repl_backlog_buffer是一个环形缓冲区，主库会记录自己写到的位置，从库会记录自己已经读到的位置。

![img](https://img-blog.csdnimg.cn/img_convert/4a70144751e3aebb1fc334e471cdedfe.png)

![img](https://img-blog.csdnimg.cn/img_convert/526c5959ada2041c0acd9d141390012c.png)

由于repl_backlog_bugger是环形缓冲区，所以缓冲区写满后，主库还会继续写入，就会覆盖之前的写入操作。如果从库读取的速度比较慢，可能会导致从库还未读取的操作被主库写的操作覆盖了，这会导致主从库间的数据不一致。

为了避免这种情况，可以调整repl_backlog_size这个参数调整缓冲空间。缓冲空间大小 = 主库写入命令速度 * 操作大小 - 主从库间网络传输命令速度 * 操作大小。在实际应用中，还需考虑可能存在的突发请求压力，通常需要把缓冲空间扩大一倍，即repl_backlog_sieze = 缓冲空间大小 * 2；如果并发请求量非常大，两倍缓冲空间都存不下新操作的话，可以考虑继续增大缓冲空间，并使用切片集群来分担单个主库的请求压力。

全量同步为什么使用RDB而不用AOF？

RDB是压缩的二进制数据，AOF文件记录的是操作命令，使用RDB进行主从同步的成本最低；
如果使用AOF做主从同步，就必须打开AOF功能，选择文件刷盘策略，选择不当会影响性能，而RDB只有在需要定时备份和主从全量同步数据时才处罚生成快照；
repl_backlog_buffer和replication buffer的区别？  

**repl_backlog_buffer**：它是为了从库断连后找到主从差异而设计的环形缓冲区。如果从库断连时间太久，repl_backlog_buffer的环形缓冲区被主库的写命令覆盖了，那么从库也只能进行一次全量同步，所以将repl_backlog_buffer配置尽量大一些，可以降低主从断开后全量同步的概率。而在repl_backlog_buffer中找到差异数据后，就用到了replication buffer；

复制积压缓冲区的最小大小可以根据公式 disconnection_reconnection_second * write_size_per_second来估算

disconnection_reconnection_second：从服务器断线后重新连接上主服务器所需的平均时间（以秒计算）

write_size_per_second：主服务器平均每秒产生的写命令数据量

**replication buffer**：Redis和客户端通信或者和从库通信，都需要分配一个内存buffer进行数据交互，实际上客户端和从库都可以看作是一个client，每一个client连接Redis后，Redis都会分配一个client buffer，所有数据交互都是通过这个buffer进行的。主从增量同步时，从库作为一个client，也会分配一个buffer，只不过这个buffer专门用来传播用户的写命令到从库，保证主从一致，这个buffer通常把它叫做replication buffer；
如果主从在传播命令时，从库处理得非常慢，那么主库的replication buffer就会持续增长，消耗大量内存资源，甚至OOM。所以Redis提供了client-output-buffer-limit参数限制这个buffer的大小，如果超过限制，主库会强制断开这个client连接，复制中断，中断后如果从库再次发起复制请求，可能会造成恶行循环，引发复制风暴。



### 服务器运行ID

****



除了复制偏移量和复制积压缓冲区之外，实现部分冲同步还需要用到服务器运行ID（run ID）：

1、每个Redis服务器，不论主服务器还是从服务器，都会有自己的运行ID。

2、运行ID在服务器启动时自动生成，由40个随机的十六进制字符组成。

​		当从服务器对主服务器进行初次复制时，主服务器会将自己的运行ID传送给从服务器，而从服务器则会将这个运行ID保存起来。

当从服务断线并重新连上一个主服务器时，从服务器将向当前连接的主服务器发送之前保存的运行ID：

- 如果从服务保存的运行ID和当前连接的主服务器的运行ID相同，那么说明从服务器断线之前复制的就是当前连接的这个主服务器，主服务器可以继续尝试执行部分重同步操作。

- 相反地，如果从服务器保存的运行ID和当前连接的主服务器的运行ID并不相同，那么说明从服务器断线之前复制的主服务器并不是当前连接的这个主服务器，主服务器将对从服务器执行完整重同步操作。

  

## 发布与订阅

​		Redis发布与订阅功能由PUBLISH、SUBSCRIBE、PSUBSCRIBE等命令组成。

​		当一个客户端执行SUBSCRIBE命令订阅某个或者某些频道的时候，这个客户端与被订阅频道之间就建立了一种订阅关系。

​		Redis将所有频道的订阅关系都保存在服务状态的pubsub_channels字典里面，这个字典的键是某个被订阅的频道，而键的值则是一个链表，链表里面记录了所有订阅这个频道的客户端。

### 模式的订阅

****

​		服务器将所有频道的订阅关系都保存在服务器状态的pubsub_channels属性里面，与此类似，服务器也将所有模式的订阅关系都保存在服务器状态的pubsub_patterns属性里面。

​		pubsub_patterns属性是一个链表，链表中的每个节点都包含着一个pubsub_pattern结构，这个结构的pattern属性记录了被订阅的模式，而client属性则记录了订阅模式的客户端。



### 命令集合

PUBSUB可以查看频道或者模式的相关信息。

**PUBSUB CHANNELS 【pattern】** 用于返回服务器当前被订阅的频道，其中patter参数是可选的：

- 如果不给定pattern参数，那么命令返回服务器当前被订阅的所有频道。
- 如果给定pattern参数，那么命令返回服务器当前被订阅的频道中那些与pattern模式相匹配的频道。

**PUBSUB NUMSUB 【channel-1】 【channel-2】…… 【channel-n】**接受任意多个频道作为输入参数，并返回这些频道的订阅者数量。

**PUBSUB NUMPAT** 用于返回服务器当前被订阅模式的数量。



## 事务

Redis通过MULTI、EXEC、WATCH、DIRTY等命令来实现事务功能。

### 原子性

数据库将事务中的多个操作当作一个整体来执行，服务器要么就执行事务中的所有操作，要么就一个操作也不执行。

### 一致性

如果数据库在执行事务之前是一致的，那么在事务执行之后，无论事务是否执行成功，数据库也应该仍然是一致的。

"一致"指的是数据符合数据库本身的定义和要求，没有包含非法或者无效的错误数据。

### 隔离性

即使数据库中有多个事务并发地执行，各个事务之间也不会互相影响，并且在并发状态下执行的事务和串行执行的事务产生的结果相同。

### 持久性

当一个事务执行完毕时，执行这个事务所得的结果已经被保存到永久性存储介质（比如硬盘里面了），即使服务器在事务执行完毕之后停机，执行事务所得的结果也不会丢失。



## 排序

Redis的SORT命令可以对列表键（List）、集合键（Set）、或者有序集合键（SortList）的值进行排序。

**SORT <key>** 命令的实现 对一个包含数字值的键key进行排序

服务器执行SORT numbers命令的详细步骤如下：

1）创建一个和numbers列表长度相同的数组，该数组的每个项都是一个redis.h/redisSortObject结构

2）遍历数组，将各个数组项的obj指针分别指向numbers列表的各个项，构成obj指针和列表项之间的一对一的关系。

3）遍历数组，将各个obj指针所指向的列表项转换成一个double类型的浮点数，并将这个浮点数保存在相应数组项的u.score属性里面。。

4）根据数组项u.score属性的值，对数组进行数字值排序，排序后的数组项按u.score属性的值从小到大排序。

5）遍历数组，将各个数组项的obj指针所指向的列表项作为排序结果返回给客户端，程序首先会访问数组的索引0，返回u.score值为1.0的列表项”1“；然后方位数组的索引”1“，返回u.score值为2.0的列表项”2“；最后访问数组的索引”2“,返回u.score值为3.0的列表项”3“。

**ALPHA（ALPHABET 字母表）**

通过使用ALPHA选项，SORT命令可以对包含字符串值的键进行排序。

**ASC选项和DESC选项的实现**

默认情况下，SORT命令执行升序排序，排序后的结果按值的大小从小到大排列

**By选项的实现**

默认情况下，SORT命令使用被排序键所包含的元素作为排序的权重（weight），元素本身决定了元素在排序之后所处的位置。

**LIMIT选项的实现**

默认情况下，SORT命令总会将排序后的所有元素都返回给客户端。
