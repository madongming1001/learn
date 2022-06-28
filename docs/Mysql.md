# 目录

## Explain（执行计划）

**Table 8.1 EXPLAIN Output Columns**

| Column                                                       | JSON Name       | Meaning                                        |
| :----------------------------------------------------------- | :-------------- | :--------------------------------------------- |
| [`id`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_id) | `select_id`     | The `SELECT` identifier                        |
| [`select_type`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_select_type) | None            | The `SELECT` type                              |
| [`table`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_table) | `table_name`    | The table for the output row                   |
| [`partitions`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_partitions) | `partitions`    | The matching partitions                        |
| [`type`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_type) | `access_type`   | The join type                                  |
| [`possible_keys`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_possible_keys) | `possible_keys` | The possible indexes to choose                 |
| [`key`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_key) | `key`           | The index actually chosen                      |
| [`key_len`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_key_len) | `key_length`    | The length of the chosen key                   |
| [`ref`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_ref) | `ref`           | The columns compared to the index              |
| [`rows`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_rows) | `rows`          | Estimate of rows to be examined                |
| [`filtered`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_filtered) | `filtered`      | Percentage of rows filtered by table condition |
| [`Extra`](https://dev.mysql.com/doc/refman/5.7/en/explain-output.html#explain_extra) | None            | Additional information                         |

explain 通过执行计划可以模拟优化器执行sql语句，查询sql的课优化空间

set session optimizer_switch='derived_merge=off'; #关闭mysql5.7新特性对衍生表的合并优化
set session optimizer_switch='derived_merge=on'; #关闭mysql5.7新特性对衍生表的合并优化

1. **explain**：会在 explain 的基础上额外提供一些查询优化的信息。紧随其后通过 show warnings 命令可 以得到优化后的查询语句，从而看出优化器优化了什么。额外还有 filtered 列，是一个半分比的值，rows filtered/100 可以**估算**出将要和 explain 中前一个表进行连接的行数（前一个表指 explain 中的id值比当前表id值小的 表）
2. **explain partitions**：相比 explain 多了个 partitions 字段，如果查询是基于分区表的话，会显示查询将访问的分 区。

## 不走索引例子

**1、联合索引第一个字段用范围不会走索引*

**2**、**in和or在表数据量比较大的情况会走索引，在表记录不多的情况下会选择全表扫描*

**3**、**like kk% 一般情况下会走索引 5.6所有下推优化**

**4**、**不在索引列上做任何操作（计算、函数、（自动or手动）类型转换），会导致索引失效而转向全表扫描*

**5**、**（！=或者<>），**not in **，**not exists **的时候无法使用索引会导致全表扫描**

**6**、**is null,is not null 一般情况下也无法使用索引*

**7**、**<***小于、***>***大于、***<=**、**>=***这些，mysql内部优化器会根据检索比例、表大小等多个因素整体评估是否使用索引**

**8**、**.like以通配符开头（**'$abc...'）mysql索引失效会变成全表扫描操作



### **数据溢出**

​		如果我们定义一个表，表中只有一个 VARCHAR 字段，如下：CREATE TABLE test_varchar( c VARCHAR(60000) )然后往这个字段插入 60000 个字符，会发生什么？前边说过，MySQL 中磁盘和内存交互的基本单位是页，也就是说 MySQL 是以页为基本单位来管理存储空间的，我们的记录都会被分配到某个页中存储。而一个页的大小一般是 16KB，也就是 16384 字节，而一个 VARCHAR(M)类型的列就最多可以存储 65532 个字节，这样就可能造成一个页存放不了一条记录的情况。

​		在 Compact 和 Redundant 行格式中，对于占用存储空间非常大的列，在记录的真实数据处只会存储该列的该列的前 768 个字节的数据，然后把剩余的数据分散存储在几个其他的页中，记录的真实数据处用 20 个字节存储指向这些页的地址。这个过程也叫做行溢出，存储超出 768 字节的那些页面也被称为溢出页。

​		Dynamic 和 Compressed 行格式，不会在记录的真实数据处存储字段真实数据的前 768 个字节，而是把所有的字节都存储到其他页面中，只在记录的真实数据处存储其他页面的地址。



### **索引页格式**

​		前边我们简单提了一下页的概念，它是 InnoDB 管理存储空间的基本单位，一个页的大小一般是 16KB。

​		InnoDB 为了不同的目的而设计了许多种不同类型的页，存放我们表中记录的那种类型的页自然也是其中的一员，官方称这种存放记录的页为索引（INDEX）页，不过要理解成数据页也没问题，毕竟存在着聚簇索引这种索引和数据混合的东西。

![image-20220208190217690](noteImg/image-20220208190217690.png)

![InnoDB architecture diagram showing in-memory and on-disk structures. In-memory structures include the buffer pool, adaptive hash index, change buffer, and log buffer. On-disk structures include tablespaces, redo logs, and doublewrite buffer files.](noteImg/innodb-architecture.png)

**区（**extent**）**

​		表空间中的页可以达到 2^32个页，实在是太多了，为了更好的管理这些页面，InnoDB 中还有一个区（英文名：extent）的概念。对于 16KB 的页来说，连续的64 个页就是一个区，也就是说一个区默认占用 1MB 空间大小。不论是系统表空间还是独立表空间，都可以看成是由若干个区组。

​		每 256个区又被划分成一个**组**



## Innodb三大特性

### doublewrite buffer

​		 InnoDB 在表空间上的 128 个页（2 个区，extend1 和extend2），大小是 2MB。

​		InnoDB 的页大小一般是 16KB，其数据校验也是针对这 16KB 来计算的，将数据写入到磁盘是以页为单位进行操作的。而操作系统写文件是以 4KB 作为单位的，那么每写一个 InnoDB 的页到磁盘上，操作系统需要写 4 个块。

​		而计算机硬件和操作系统，在极端情况下（比如断电）往往并不能保证这一操作的原子性，16K 的数据，写入 4K 时，发生了系统断电或系统崩溃，只有一部分写是成功的，这种情况下会产生 partial page write（部分页写入）问题。

​		为了解决部分页写入问题，当 MySQL 将脏数据 flush到数据文件的时候, 先使用 memcopy 将脏数据复制到内存中的一个区域（也是2M），之后通过这个内存区域再分 2 次，每次写入 1MB 到系统表空间，然后马上调用 fsync 函数，同步到磁盘上。在这个过程中是顺序写，开销并不大，在完成 doublewrite 写入后，再将数据写入各数据文件文件，这时是离散写入。所以在正常的情况下, MySQL 写数据页时，会写两遍到磁盘上，第一遍是写到doublewrite buffer，第二遍是写到真正的数据文件中。如果发生了极端情况（断电），InnoDB 再次启动后，发现了一个页数据已经损坏，那么此时就可以从doublewrite buffer 中进行数据恢复了。

### Buffer Pool

​		我们知道，对于使用 InnoDB 作为存储引擎的表来说，不管是用于存储用户数据的索引（包括聚簇索引和二级索引），还是各种系统数据，都是以页的形式存放在表空间中的，而所谓的表空间只不过是 InnoDB 对文件系统上一个或几个实际文件的抽象，也就是说我们的数据说到底还是存储在磁盘上的。但是磁盘的速度慢，所以 InnoDB 存储引擎在处理客户端的请求时，当需要访问某个页的数据时，就会把完整的页的数据全部加载到内存中，也就是说即使我们只需要访问一个页的一条记录，那也需要先把整个页的数据加载到内存中。将整个页加载到内存中后就可以进行读写访问了，在进行完读写访问之后并不着急把该页对应的内存空间释放掉，而是将其缓存起来，这样将来有请求再次访问该页面时，就可以省去磁盘 IO 的开销了。

**free链表**	

​	buffer存储格式都是按照每个存储页都对应一个控制块而所有的控制块在前，存储页在后，如果新来一个页如何辨别哪里是空位置可以存放呢，就用到了**free链表**会把空闲的所有控制块都放入到**free链表**

![image-20220208201845717](noteImg/image-20220208201845717.png)

我们其实是根据表空间号 + 页号来定位一个页的，所以我们可以用表空间号 + 页号作为 key，缓存页作为 value 创建一个哈希表，在需要访问某个页的数据时，先从哈希表中根据表空间号 + 页号看看有没有应的缓存页，如果有，直接使用该缓存页就好，如果没有，那就从 free 链表中选一个空闲的缓存页，然后把磁盘中对应的页加载到该缓存页的位置。

**flush链表**

​		如果我们修改了 Buffer Pool 中某个缓存页的数据，那它就和磁盘上的页不一致了，这样的缓存页也被称为脏页（英文名：dirty page）所以，需要再创建一个存储脏页的链表，凡是修改过的缓存页对应的控制块都会作为一个节点加入到一个链表中，因为这个链表节点对应的缓存页都是需要被刷新到磁盘上的，所以也叫 flush 链表。

​		如果非常多的使用频率偏低的页被同时加载到 Buffer Pool 时，可能会把那些使用频率非常高的页从 Buffer Pool 中淘汰掉。

因为有这两种情况的存在，所以 InnoDB 把这个 LRU 链表按照一定比例分成两截，

分别是：

一部分存储使用频率非常高的缓存页，所以这一部分链表也叫做热数据，或者称 young 区域。

另一部分存储使用频率不是很高的缓存页，所以这一部分链表也叫做冷数据，或者称 old 区域。

![image-20220208203449226](noteImg/image-20220208203449226.png)

所以在对某个处在 old 区域的缓存页进行第一次访问时就在它对应的控制块中记录下来这个访问时间，如果后续的访问时间与第一次访问的时间在某个时间间隔内，那么该页面就不会被从 old 区域移动到 young 区域的头部，否则将它移动到 young 区域的头部。上述的这个间隔时间是由系统变量innodb_old_blocks_time 控制的：

![image-20220208203815265](noteImg/image-20220208203815265.png)

这个 innodb_old_blocks_time 的默认值是 1000，它的单位是毫秒，也就意味着对于从磁盘上被加载到 LRU 链表的 old 区域的某个页来说，如果第一次和最后一次访问该页面的时间间隔小于 1s（很明显在一次全表扫描的过程中，多次访问一个页面中的时间不会超过 1s），那么该页是不会被加入到 young 区域的，当然，像 innodb_old_blocks_pct 一样，我们也可以在服务器启动或运行时设置innodb_old_blocks_time 的值，这里需要注意的是，如果我们把innodb_old_blocks_time 的值设置为 0，那么每次我们访问一个页面时就会把该页面放到 young 区域的头部。

​		综上所述，正是因为将 LRU 链表划分为 young 和 old 区域这两个部分，又添加了 innodb_old_blocks_time 这个系统变量，才使得预读机制和全表扫描造成的缓存命中率降低的问题得到了遏制，因为用不到的预读页面以及全表扫描的页面都只会被放到 old 区域，而不影响 young 区域中的缓存页。

**刷新脏页到磁盘**

后台有专门的线程每隔一段时间负责把脏页刷新到磁盘，这样可以不影响用户线程处理正常的请求。主要有两种刷新路径：

1、从 LRU 链表的冷数据中刷新一部分页面到磁盘。

2、从 flush 链表中刷新一部分页面到磁盘。



### 自适应哈希索引

​		InnoDB存储引擎除了我们前面所说的各种索引，还有一种自适应哈希索引，我们知 道B+树的查找次数,取决于B+树的高度,在生产环境中,B+树的高度一般为3~4层,故 需要3~4次的IO查询。 

​		所以在InnoDB存储引擎内部自己去监控索引表，如果监控到某个索引经常用，那么 就认为是热数据，然后内部自己创建一个hash索引，称之为自适应哈希索引( Adaptive Hash Index,AHI)，创建以后，如果下次又查询到这个索引，那么直接通 过hash算法推导出记录的地址，直接一次就能查到数据，比重复去B+tree索引中查 询三四次节点的效率高了不少。 

必须得是等值查询。

### 预读

​		InnoDB 提供了预读（英文名：read ahead）。所谓预读，就是 InnoDB认为执行当前的请求可能之后会读取某些页面，就预先把它们加载到 Buffer Pool中。根据触发方式的不同，预读又可以细分为下边两种：

*线性预读*

InnoDB 提供了一个系统变量 innodb_read_ahead_threshold，如果顺序访问了某个区（extent）的页面超过这个系统变量的值，**就会触发一次异步读取下一个区中全部的页面到 Buffer Pool 的请求**。这个 innodb_read_ahead_threshold 系统变量的值默认是 56，我们可以在服务器启动时通过启动参数或者服务器运行过程中直接调整该系统变量的值，取值范围是 0~64。

*随机预读*

如果 Buffer Pool 中已经缓存了某个区的 13 个连续的页面，不论这些页面是不是顺序读取的，都会触发一次异步读取本区中所有其他的页面到 Buffer Pool 的请求。InnoDB同时提供了innodb_random_read_ahead 系统变量，它的默认值为OFF。

**show variables like '%_read_ahead%';**

## 数据库范式

目前关系数据库有六种范式：第一范式（

1NF）、第二范式（2NF）、第三范式（3NF）、 巴斯-科德范式（BCNF）、第四范式(4NF）和第五范式（5NF，又称完美范式）。

**数据库设计的第一范式*

定义： 属于第一范式关系的所有属性都不可再分，即数据项不可分。 

![image-20220208213231317](noteImg/image-20220208213231317.png)

**数据库设计的第二范式*

第二范式（2NF）要求数据库表中的每个实例或行必须可以被惟一地区分。通常在实现来 

说，需要为表加上一个列，以存储各个实例的惟一标识。主键ID。



**数据库设计的第三范式*

指每一个非主属性既不部分依赖于也不传递依赖于业务主键，也就是在第二范式的基础 上消除了非主键对主键的传递依赖。



### **反范式设计**

​		所谓得反范式化就是为了性能和读取效率得考虑而适当得对数据库设计范式得要求进行 违反。允许存在少量得冗余，换句话来说反范式化就是使用空间来换取时间。



### 三星索引

索引将相关的记录放到一起则获得一星； （索引的扫描范围越小越好）

如果索引中的数据顺序和查找中的排列顺序一致则获得二星； 

如果索引中的列包含了查询中需要的全部列则获得三星。 （覆盖索引）



## Mysql内部优化策略

1、移除不必要的括号 

2、常量传递（constant_propagation） 

3、移除没用的条件（trivial_condition_removal） 

4、表达式计算 



## Mysql性能优化

1、硬件优化

2、Mysql调优

1. 业务层-请求了不需要的数据
2. 选择合适的存储引擎 Innodb MyISAM
3. 查询性能优化 通过慢查询日志 slow_query_log
4. 根据相爱年供应时间

3、架构调优













#### ID列

id列越大执行优先级越高，id相同则从上往下执行，id为NULL最后执行。

#### Select_type列

#simple：简单查询。查询不包含子查询和union
#primary 查询最外层的select
#derived 包含在select中的子查询（不在from子句中）
#subquery 包含在 from 子句中的子查询。MySQL会将结果存放在一个临时表中，也称为派生表（derived的英文含义）
#union 在union中的第二个和随后的select
explain select (select 1 from actor where id = 1) from (select from film where id = 1) der;

#### table列

显示explain正在执行哪一张表

#### type列

#### 8大查询类型

关联类型或访问类型
**null > system > const > eq_ref > ref > range > index > ALL**
一般来说，得保证查询达到range级别，最好达到ref

##### 1.Null

​	mysql能够在优化阶段分解查询语句，在执行阶段用不着再访问表或索引。

##### 2.const,system

​	mysql能对查询的某部分进行优化并将其转化成一个常量,用于 primary key 或 unique key 的所有列与常数比较时，所以表最多有一个匹配行，读取1次，速度比较快。system是 const的特例，表里只有一条元组匹配时为system
explain select from (select from film where id = 1) tmp;

##### 3.eq_ref

​	primary key 或 unique key 索引的所有部分被连接使用 ，最多只会返回一条符合条件的记录。这可能是在 const 之外最好的联接类型了，简单的 select 查询不会出现这种 type。

##### 4.ref

​	相比 eq_ref，不使用唯一索引，而是使用普通索引或者唯一性索引的部分前缀，索引要和某个值相比较，可能会找到多个符合条件的行。

##### 5.range

​	范围扫描通常出现在 in(), between ,> ,<, >= 等操作中。使用一个索引来检索给定范围的行。

##### 6.index

​	扫描全索引就能拿到结果，一般是扫描某个二级索引，这种扫描不会从索引树根节点开始快速查找，而是直接 对二级索引的叶子节点遍历和扫描，速度还是比较慢的，这种查询一般为使用覆盖索引，二级索引一般比较小，所以这种通常比ALL快一些。

##### 7.ALL

​	即全表扫描，扫描你的聚簇索引的所有叶子节点。通常情况下这需要增加索引来进行优化了。

#### possible_keys列

这一列显示查询可能使用哪些索引来查找。 

explain 时可能出现 possible_keys 有列，而 key 显示 NULL 的情况，这种情况是因为表中数据不多，mysql认为索引 

对此查询帮助不大，选择了全表查询。

#### key列

这一列显示mysql实际采用哪个索引来优化对该表的访问。 

如果没有使用索引，则该列是 NULL。如果想强制mysql使用或忽视possible_keys列中的索引，在查询中使用 force 

index、ignore index。 

#### key_len列

这一列显示了mysql在索引里使用的字节数，通过这个值可以算出具体使用了索引中的哪些列。

字符串，char(n)和varchar(n)，5.0.3以后版本中，**n均代表字符数，而不是字节数，**如果是utf-8，一个数字 

或字母占1个字节，一个汉字占3个字节 

char(n)：如果存汉字长度就是 3n 字节 

varchar(n)：如果存汉字则长度是 3n + 2 字节，加的2字节用来存储字符串长度，因为 

varchar是变长字符串 

数值类型

tinyint：1字节 

smallint：2字节 

int：4字节 

bigint：8字节 

时间类型

date：3字节

timestamp：4字节 时间到2039年

datetime：8字节    时间到9999年

如果字段允许为 NULL，需要1字节记录是否为 NULL 

索引最大长度是768字节，当字符串过长时，mysql会做一个类似左前缀索引的处理，将前半部分的字符提取出来做索 

引。

#### ref列

这一列显示了在key列记录的索引中，表查找值所用到的列或常量，常见的有：const（常量），字段名（例：film.id） 

#### rows列

这一列是mysql估计要读取并检测的行数，注意这个不是结果集里的行数。 

#### Extra列

这一列展示的是额外信息。常见的重要值如下： 

using index：使用覆盖索引

Using where：使用 where 语句来处理结果，并且查询的列未被索引覆盖

Using index condition：查询的列不完全被索引覆盖，where条件中是一个前导列的范围； 

Using temporary：mysql需要创建一张临时表来处理查询。出现这种情况一般是要进行优化的，首先是想到用索 

引来优化。 

Using filesort：将用外部排序而不是索引排序，数据较小时从内存排序，否则需要在磁盘完成排序。这种情况下一 

般也是要考虑使用索引来优化的。

Select tables optimized away：使用某些聚合函数（比如 max、min）来访问存在索引的某个字段是



#### mysql组件

##### **连接器**

管理连接与权限校验

客户端如果长时间不发送command到Server端，连接器就会自动将它断开。这个时间是由参数 wait_timeout 控制的，默认值 

是 8 小时。 

 show global variables like "wait_timeout"; 

Lost connection to MySQL server during query

开发当中我们大多数时候用的都是长连接,把连接放在Pool内进行管理，但是长连接有些时候会导致 MySQL 占用内存涨得特别 

快，这是因为 MySQL 在执行过程中临时使用的内存是管理在连接对象里面的。这些资源会在连接断开的时候才释放。所以如 

果长连接累积下来，可能导致内存占用太大，被系统强行杀掉（OOM），从现象看就是 MySQL 异常重启了。 

怎么解决这类问题呢？ 

1、定期断开长连接。使用一段时间，或者程序里面判断执行过一个占用内存的大查询后，断开连接，之后要查询再重连。 

2、如果你用的是 MySQL 5.7 或更新版本，可以在每次执行一个比较大的操作后，通过执行 mysql_reset_connection 来重新初始化连接资 

源。这个过程不需要重连和重新做权限验证，但是会将连接恢复到刚刚创建完时的状态。 

##### 查询缓存（**mysql8.0已经移除了查询缓存功能**）

查询缓存按照理想来说是对于效率提升很好的一个手段，但是由于缓存失效的非常频繁，只要对一个表的更新，那这张表的所有缓存都会被清空

一般也就长时间不被修改的表才用到缓存。

而对于8.0之前有三个参数来决定query_cache_type缓存的使用

0代表关闭查询缓存 1代表开启缓存，2代表只有遇到关键字sql_cache关键字时才缓存

##### 分析器

###### 词法分析

对于一个个空格的sql需要区分出每个单词所代表的含义，比如select id from user 各个区分 作用 select 查询 id 列名 from user 从user表

###### 语法分析

根据词法分析的结果，语法分析器会根据语法规则，判断你输入的这个 SQL 语句 

是否满足 MySQL 语法。 

如果你的语句不对，就会收到“You have an error in your SQL syntax”的错误提醒，比如下面这个语句 from 写成了 

"rom"。

而其中使用到的就是词法分析器

主要分为6个步骤

1.词法分析

2.语法分析

3.语义分析

4.构建执行树

5.生成执行计划

6.计划的执行

SQL语句的分析分为词法分析与语法分析，mysql的词法分析由MySQLLex[MySQL自己实现的]完成，语法分析由Bison生 

成。关于语法树大家如果想要深入研究可以参考这篇wiki文章：https://en.wikipedia.org/wiki/LR_parser。那么除了Bison 

外，Java当中也有开源的词法结构分析工具例如Antlr4，ANTLR从语法生成一个解析器，可以构建和遍历解析树，可以在IDEA 

工具当中安装插件：**antlr v4 grammar plugin。插件使用详见课程*



##### **优化器**

经过了分析器，MySQL 就知道你要做什么了。在开始执行之前，还要先经过优化器的处理。 

优化器是在表里面有多个索引的时候，决定使用哪个索引；或者在一个语句有多表关联（join）的时候，决定各个表的连接 

顺序。

##### **执行器**

先判断你对于这张表又没有权限。

然后再执行sql判断是否符合条件的数据，最后组成一个结果集返回给客户端。



#### bin-log

binlog是Server层实现的二进制日志,他会记录我们的cud操作。Binlog有以下几个特点： 

1、Binlog在MySQL的Server层实现（引擎共用） 

2、Binlog为逻辑日志,记录的是一条语句的原始逻辑 

3、Binlog不限大小,追加写入,不会覆盖以前的日志 

使用bin-log需要先配置

配置my.cnf

log-bin=地址

格式有三种 statement，row，mixed

binlog-format=row（默认）

show variables like '%log_bin%'; 查看bin‐log是否开启 

flush logs; 会多一个最新的bin‐log日志 

show master status; 查看最后一个bin‐log日志的相关信息 

reset master; 清空所有的bin‐log日志 

1. **statement**

   当binlog=statement时，binlog记录的是SQL本身的语句，语句中可能有函数，比如uuid每次获取都是不一样的，这样同步slave的时候就会出现数据不一致问题

2. **row**会记录比如删除delete from table where id < 100,会记录100条删除每条id的语句，内容占用空间大

3. **mixed**

​		statement格式记录sql原句，可能会导致主备不一致，所以出现了row格式
但是row格式也有一个缺点，就是很占空间，比如你delete语句删除1万行记录，statement格式会记录一个sql删除1万行就没了；但是使用row格式会把这1万要删除的记录都写到binlog中，这样会导致binlog占用了大量空间，同时写binlog也要耗费大量IO，影响mysql的整体速度
所以MySQL出了个mixed格式，它是前面两种格式的混合。意思是MySQL自己会判断这条SQL语句是否会引起主备不一致，是的话就会使用row，否则就用statement格式
也就是说上面delete语句加上了limit 1，MySQL认为会引起主备不一致，它就会使用row格式记录到binlog；如果delete 1万行记录，MySQL认为不会引起主备不一致，它就会使用statement格式记录到binlog。

#### 索引使用情况（mysql5.6引入索引下推）

1、联合索引第一个字段用范围不会走索引

2、强制走索引  force index（）

3、覆盖索引优化

4、in和or在表数据量比较大的情况会走索引，在表记录不多的情况下会选择全表扫描

5、like KK% 一般情况都会走索引

​		索引下推（Index Condition Pushdown）可以在索引遍历过程中，对索引中包含的所有字段先做判断，过滤掉不符合条件的记录之后再回表，可 以有效的减少回表次数。

**为什么范围查找Mysql没有用索引下推优化？*

估计应该是Mysql认为**范围查找过滤的结果集过大**，like KK% 在绝大多数情况来看，过滤后的结果集比较小，所以这里Mysql选择给 like 

KK% 用了索引下推优化，当然这也不是绝对的，有时like KK% 也不一定就会走索引下推。 

order by 与group by

order by根据where和order by字段的情况 再根据联合索引或者其他耳机索引的创建情况决定是否走了index和filesort

排序方式也是决定一部分因素之一

```mysql
explain select from employees where name in ('LiLei','zhuge')  order by age,position;
```

对于排序来说多个相等条件也是范围查询

1、MySQL支持两种方式的排序filesort和index，Using index是指MySQL扫描索引本身完成排序。index 

效率高，filesort效率低。 

2、order by满足两种情况会使用Using index。 

​       1) order by语句使用索引最左前列。 

​       2) 使用where子句与order by子句条件列组合满足索引最左前列。 

3、尽量在索引列上完成排序，遵循索引建立（索引创建的顺序）时的最左前缀法则。 

4、如果order by的条件不在索引列上，就会产生Using filesort。 

5、能用覆盖索引尽量用覆盖索引 

6、group by与order by很类似，其实质是先排序后分组，遵照索引创建顺序的最左前缀法则。对于group 

by的优化如果不需要排序的可以加上**order by null禁止排序**。注意，where高于having，能写在where中 

的限定条件就不要去having限定了。

#### **filesort文件排序方式**

##### 单路排序：

是一次性取出满足条件行的所有字段，然后在sort buffer中进行排序；用trace工具可以看到sort_mode信息里显示< sort_key, additional_fields >或者< sort_key, packed_additional_fields >

##### 双路排序（又叫**回表**排序模式）：

是首先根据相应的条件取出相应的**排序字段**和**可以直接定位行数据的行 ID**，然后在 sort buffer 中进行排序，排序完后需要再次取回其它需要的字段；用trace工具可以看到sort_mode信息里显示< sort_key, rowid >

mysql通过比较系统变量 max_length_for_sort_data(默认4096字节，mysql 8.0.27)的大小和需要查询的字段总大小判断使用哪种排序方式

如果 字段的总长度小于max_length_for_sort_data ，那么使用 单路排序模式；

如果 字段的总长度大于max_length_for_sort_data ，那么使用 双路排序模式



#### 索引原则

1、代码先行，索引后上

2、联合索引尽量覆盖条件

3、不要在小基数字段上建立索引

4、长字符串我们可以采用前缀索引

5、where与order by冲突时优先where

6、基于慢sql查询做优化



#### sql 慢查询

MySQL的慢查询，全名是**慢查询日志**，是MySQL提供的一种日志记录，用来记录在MySQL中**响应时间超过阀值**的语句。

long_query_time（默认是10秒）

![image-20211111142906400](noteImg/image-20211111142906400.png)

设置完成后需要重启数据库 设置方式**https://blog.csdn.net/qq_40884473/article/details/89455740**

`set global slow_query_log=1`开启了慢查询日志只对当前数据库生效，MySQL重启后则会失效。

关于运行时间**正好等于**`long_query_time`的情况，并不会被记录下来。

##### log-queries-not-using-indexes

该系统变量指定**未使用索引的查询**也被记录到慢查询日志中（可选项）。

##### log_slow_admin_statements

这个系统变量表示，是否将慢管理语句例如`ANALYZE TABLE`和`ALTER TABLE`等记入慢查询日志。

##### Slow_queries

如果你想查询有多少条慢查询记录，可以使用`Slow_queries`系统变量。

##### mysqldumpslow工具

在生产环境中，如果要手工分析日志，查找、分析SQL，显然是个体力活。

MySQL提供了日志分析工具`mysqldumpslow`

#### **mysql的表关联常见有两种算法**

#####  Nested-Loop Join 算法（嵌套循环连接 Nested—loop join（NLJ）算法 磁盘扫描）

当使用left join时，左表是驱动表，右表是被驱动表，当使用right join时，右表时驱动表，左表是被驱动表，当使用join时，mysql会选择数据量比较小的表作为驱动表，大表作为被驱动表。

使用了 NLJ算法。一般 join 语句中，如果执行计划 Extra 中未出现 **Using join buffer*则表示使用的 join 算法是 NLJ。

**上面sql的大致流程如下：**

1. 从表 t2 中读取一行数据（如果t2表有查询过滤条件的，用先用条件过滤完，再从过滤结果里取出一行数据）；
2. 从第 1 步的数据中，取出关联字段 a，到表 t1 中查找；
3. 取出表 t1 中满足条件的行，跟 t2 中获取到的结果合并，作为结果返回给客户端；
4. 重复上面 3 步。

整个过程会读取 t2 表的所有数据(**扫描100行**)，然后遍历这每行数据中字段 a 的值，根据 t2 表中 a 的值索引扫描 t1 表中的对应行(**扫描100次 t1 表的索引，1次扫描可以认为最终只扫描 t1 表一行完整数据，也就是总共 t1 表也扫描了100行**)。因此整个过程扫描了 **200 行**。

如果被驱动表的关联字段没索引，**使用NLJ算法性能会比较低(下面有详细解释)**，mysql会选择Block Nested-Loop Join算法。



 



##### Block Nested-Loop Join 算法（基于块的嵌套循环连接Block Nested-loop join（BNL）算法 基于sort buffer内存计算）

把**驱动表**的数据读入到 join_buffer 中，然后扫描**被驱动表**，把**被驱动表**每一行取出来跟 join_buffer 中的数据做对比。

Extra 中 的Using join buffer (Block Nested Loop)说明该关联查询使用的是 BNL 算法。

**上面sql的大致流程如下：**

1. 把 t2 的所有数据放入到 **join_buffer*中
2. 把表 t1 中每一行取出来，跟 join_buffer 中的数据做对比
3. 返回满足 join 条件的数据

整个过程对表 t1 和 t2 都做了一次全表扫描，因此扫描的总行数为10000(表 t1 的数据总量) + 100(表 t2 的数据总量) = **10100**。并且 join_buffer 里的数据是无序的，因此对表 t1 中的每一行，都要做 100 次判断，所以内存中的判断次数是 100 10000= **100 万次**。

这个例子里表 t2 才 100 行，要是表 t2 是一个大表，join_buffer 放不下怎么办呢？·

join_buffer 的大小是由参数 join_buffer_size 设定的，默认值是 262144。如果放不下表 t2 的所有数据话，策略很简单，就是**分段放**。

比如 t2 表有1000行记录， join_buffer 一次只能放800行数据，那么执行过程就是先往 join_buffer 里放800行记录，然后从 t1 表里取数据跟 join_buffer 中数据对比得到部分结果，然后清空  join_buffer ，再放入 t2 表剩余200行记录，再次从 t1 表里取数据跟 join_buffer 中数据对比。所以就多扫了一次 t1 表。



#### 对于小表定义的明确

在决定哪个表做驱动表的时候，应该是两个表按照各自的条件过滤，**过滤完成之后**，计算参与 join 的各个字段的总数据量，**数据量小的那个表，就是“小表”**，应该作为驱动表。



#### in和exsits优化

原则：**小表驱动大表**，即小的数据集驱动大的数据集

**in：**当B表的数据集小于A表的数据集时，in优于exists 

**exists：**当A表的数据集小于B表的数据集时，exists优于in

　　将主查询A的数据，放到子查询B中做条件验证，根据验证结果（true或false）来决定主查询的数据是否保留



#### count(\*)查询优化

**四个sql的执行计划一样，说明这四个sql执行效率应该差不多**

**字段有索引：count(\*)≈count(1)>count(字段)>count(主键 id)    //字段有索引，count(字段)统计走二级索引，二级索引存储数据比主键索引少，所以count(字段)>count(主键 id)*

**字段无索引：count(\*)≈count(1)>count(主键 id)>count(字段)    //字段没有索引count(字段)统计走不了索引，count(主键 id)还可以走主键索引，所以count(主键 id)>count(字段)**

count(1)跟count(字段)执行过程类似，不过count(1)不需要取出字段统计，就用常量1做统计，count(字段)还需要取出字段，所以理论上count(1)比count(字段)会快一点。

count(**) 是例外，mysql并不会把全部字段取出来，而是专门做了优化，不取值，按行累加，效率很高，所以不需要用count(列名)或count(常量)来替代 count(*)。

为什么对于count(id)，mysql最终选择辅助索引而不是主键聚集索引？因为二级索引相对主键索引存储数据更少，检索性能应该更高，mysql内部做了点优化(应该是在5.7版本才优化)。



#### 事务

事务是由一组SQL语句组成的逻辑处理单元,事务具有以下4个属性,通常简称为事务的ACID属性。

- 原子性(Atomicity) ：事务是一个原子操作单元,其对数据的修改,要么全都执行,要么全都不执行。

- 一致性(Consistent) ：在事务开始和完成时,数据都必须保持一致状态。这意味着所有相关的数据规则都必须应用于事务的修改,以保持数据的完整性。

- 隔离性(Isolation) ：数据库系统提供一定的隔离机制,保证事务在不受外部并发操作影响的“独立”环境执行。这意味着事务处理过程中的中间状态对外部是不可见的,反之亦然。

- 持久性(Durable) ：事务完成之后,它对于数据的修改是永久性的,即使出现系统故障也能够保持。

  

  并发事务处理带来的问题

  ##### 更新丢失（Lost update）或脏写

  ​	当两个或多个事务选择同一行，然后基于最初选定的值更新该行时，由于每个事务都不知道其他事务的存在，就会发生丢失更新问题–**最后的更新覆盖了由其他事务所做的更新**。

  ##### 脏读（Dirty Reads）

  ​	一个事务正在对一条记录做修改，在这个事务完成并提交前，这条记录的数据就处于不一致的状态；这时，另一个事务也来读取同一条记录，如果不加控制，第二个事务读取了这些“脏”数据，并据此作进一步的处理，就会产生未提交的数据依赖关系。这种现象被形象的叫做“脏读”。

  　　一句话：**事务A读取到了事务B已经修改但尚未提交的数据**，还在这个数据基础上做了操作。此时，如果B事务回滚，A读取的数据无效，不符合一致性要求。

  ##### 不可重读（Non-Repeatable Reads） 

  　　一个事务在读取某些数据后的某个时间，再次读取以前读过的数据，却发现其读出的数据已经发生了改变、或某些记录已经被删除了！这种现象就叫做“不可重复读”。

  　　一句话：**事务A内部的相同查询语句在不同时刻读出的结果不一致，不符合隔离性**

  ##### 幻读（Phantom Reads）

  　　一个事务按相同的查询条件重新读取以前检索过的数据，却发现其他事务插入了满足其查询条件的新数据，这种现象就称为“幻读”。

  　　一句话：**事务A读取到了事务B提交的新增数据，不符合隔离性**

  ![https://note.youdao.com/yws/public/resource/354ae85f3519bac0581919a458278a59/xmlnote/74624CB778F948349A31BA0A40430F51/98786](https://note.youdao.com/yws/public/resource/354ae85f3519bac0581919a458278a59/xmlnote/74624CB778F948349A31BA0A40430F51/98786)

  **不可重复度和幻读区别：**

  不可重复读的重点是修改，幻读的重点在于新增或者删除

  数据库的事务隔离越严格,并发副作用越小,但付出的代价也就越大,因为事务隔离实质上就是使事务在一定程度上“串行化”进行,这显然与“并发”是矛盾的。

  同时,不同的应用对读一致性和事务隔离程度的要求也是不同的,比如许多应用对“不可重复读"和“幻读”并不敏感,可能更关心数据并发访问的能力。

  **常看当前数据库的事务隔离级别: show variables like 'tx_isolation';**

  **设置事务隔离级别：****set tx_isolation='REPEATABLE-READ';**

  **Mysql默认的事务隔离级别是可重复读，用Spring开发程序时，如果不设置隔离级别默认用Mysql设置的隔离级别，如果Spring设置了就用已经设置的隔离级别**

  ##### **锁详解**

  锁是计算机协调多个进程货线程并发访问某一资源的机制。

  ##### **锁分类*

  - 从性能上分为乐观锁(用版本对比来实现)和悲观锁

  - 从对数据库操作的类型分，分为读锁和写锁(都属于悲观锁)

    读锁（共享锁，S锁(**S**hared)）：针对同一份数据，多个读操作可以同时进行而不会互相影响

    写锁（排它锁，X锁(Exclusive)）：当前写操作没有完成前，它会阻断其他写锁和读锁

  - 从对数据操作的粒度分，分为表锁和行锁

  

  **表锁**

  每次操作锁住整张表。开销小，加锁快；不会出现死锁；锁定粒度大，发生锁冲突的概率最高，并发度最低；一般用在整表数据迁移的场景。

  lock table 表名称 read（write），表名称2 read（write）
  
  show open tables；
  
  unlock tables
  **案例分析（加读锁）**

  lock table mylock read

  当前session和其他session都可以读该表

  当前session中插入或者更新锁定的表都会报错，其他session插入或更新则会等待

  **案例分析(加写锁）**

  lock table mylock write；

  当前session对该表的增删改查都没有问题，其他session对该表的所有操作被阻塞

  

  ##### **行锁**

  每次操作锁住一行数据。开销大，加锁慢；会出现死锁；锁定粒度最小，发生锁冲突的概率最低，并发度最高。
  
  InnoDB与MYISAM的最大不同有两点：
  
  - **InnoDB支持事务（TRANSACTION）**
  - **InnoDB支持行级锁**
  
  **总结：**

MyISAM在执行查询语句SELECT前，会自动给涉及的所有表加读锁,在执行update、insert、delete操作会自动给涉及的表加写锁。

InnoDB在执行查询语句SELECT时(非串行隔离级别)，不会加锁。但是update、insert、delete操作会加行锁。

简而言之，就是**读锁会阻塞写，但是不会阻塞读。而写锁则会把读和写都阻塞**。



**间隙锁(Gap Lock) 间隙锁是在可重复读隔离级别下才会生效。**

间隙锁，锁的就是两个值之间的空隙。Mysql默认级别是repeatable-read，有办法解决幻读问题吗？间隙锁在某些情况下可以解决幻读问题。



**临键锁(Next-key Locks)**

Next-Key Locks是行锁与间隙锁的组合。像上面那个例子里的这个(3,20]的整个区间可以叫做临键锁。



**无索引行锁会升级为表锁(RR级别会升级为表锁，RC级别不会升级为表锁)**

锁主要是加在索引上，如果对非索引字段更新，行锁可能会变表锁

session1 执行：update account set balance = 800 where name = 'lilei';

session2 对该表任一行操作都会阻塞住

**InnoDB的行锁是针对索引加的锁，不是针对记录加的锁。并且该索引不能失效，否则都会从行锁升级为表锁**。

锁定某一行还可以用lock in share mode(共享锁) 和for update(排它锁)，例如：select from test_innodb_lock where a = 2 for update; 这样其他session只能读这行数据，修改则会被阻塞，直到锁定行的session提交



**死锁**

**set tx_isolation='**repeatable-read**';**

Session_1执行：select from account where id=1 for update;

Session_2执行：select from account where id=2 for update;

Session_1执行：select from account where id=2 for update;

Session_2执行：select from account where id=1 for update;

查看近期死锁日志信息：show engine innodb status\G; 

大多数情况mysql可以自动检测死锁并回滚产生死锁的那个事务，但是有些情况mysql没法自动检测死锁



**MVCC多版本并发控制机制**

Mysql在可重复读隔离级别下如何保证事务较高的隔离性，我们上节课给大家演示过，同样的sql查询语句在一个事务里多次执行查询结果相同，就算其它事务对数据有修改也不会影响当前事务sql语句的查询结果。

这个隔离性就是靠MVCC(**Multi-Version Concurrency Control**)机制来保证的，对一行数据的读和写两个操作默认是不会通过加锁互斥来保证隔离性，避免了频繁加锁互斥，而在串行化隔离级别为了保证较高的隔离性是通过将所有操作加锁互斥来实现的。

Mysql在读已提交和可重复读隔离级别下都实现了MVCC机制。



**undo日志版本链与read view机制详解**

undo日志版本链是指一行数据被多个事务依次修改过后，在每个事务修改完后，Mysql会保留修改前的数据undo回滚日志，并且用两个隐藏字段trx_id和roll_pointer把这些undo日志串联起来形成一个历史记录版本链(见下图，需参考视频里的例子理解)

​    ![0](https://note.youdao.com/yws/public/resource/b36b975188fadf7bfbfd75c0d2d6b834/xmlnote/8C9D98D4BB9B49BEB2822BCE98A37DDE/99285)



在**可重复读隔离级别**，当事务开启，执行任何（select）查询sql时会生成当前事务的**一致性视图read-view，**该视图在事务结束之前都不会变化(**如果是读已提交隔离级别在每次执行查询sql时都会重新生成**)，这个视图由执行查询时所有**未提交事务id数组**（**数组里最小的事务id为min_id**）和**已创建的最大事务id（max_id）**组成，事务里的任何sql查询结果需要从对应版本链里的最新数据开始逐条跟read-view做比对从而得到最终的快照结果。

**版本链比对规则：**

1. 如果 row 的 trx_id 落在绿色部分( trx_id<min_id),表示这个版本是已提交的事务生成的，这个数据是可见的。

2. 如果 row 的 trx_id 落在红色部分( trx_id>max_id )，表示这个版本是由将来启动的事务生成的，是不可见的(若 row 的 trx_id 就是当前自己的事务是可见的）；

3. 如果 row 的 trx_id 落在黄色部分(min_id <=trx_id<= max_id)，那就包括两种情况

  a. 若 row 的 trx_id 在视图数组中，表示这个版本是由还没提交的事务生成的，不可见(若 row 的 trx_id 就是当前自己的事务是可见的)；

  b. 若 row 的 trx_id 不在视图数组中，表示这个版本是已经提交了的事务生成的，可见。

对于删除的情况可以认为是update的特殊情况，会将版本链上最新的数据复制一份，然后将trx_id修改成删除操作的trx_id，同时在该条记录的头信息（record header）里的（deleted_flag）标记位写上true，来表示当前记录已经被删除，在查询时按照上面的规则查到对应的记录如果delete_flag标记位为true，意味着记录已被删除，则不返回数据。

**注意：**begin/start transaction 命令并不是一个事务的起点，在执行到它们之后的第一个修改操作InnoDB表的语句，事务才真正启动，才会向mysql申请事务id，mysql内部是严格按照事务的启动顺序来分配事务id的。

**总结：**

MVCC机制的实现就是通过read-view机制与undo版本链比对机制，使得不同的事务会根据数据版本链对比规则读取同一条数据在版本链上的不同版本数据。

#### **Innodb引擎SQL执行的BufferPool缓存机制**

​    ![0](https://note.youdao.com/yws/public/resource/b36b975188fadf7bfbfd75c0d2d6b834/xmlnote/9C296B9BBF3C4C0389F470357FC55FE9/99001)

**为什么Mysql不能直接更新磁盘上的数据而且设置这么一套复杂的机制来执行SQL了？**

因为来一个请求就直接对磁盘文件进行随机读写，然后更新磁盘文件里的数据性能可能相当差。因为磁盘随机读写的性能是非常差的，所以直接更新磁盘文件是不能让数据库抗住很高并发的。Mysql这套机制看起来复杂，但它可以保证每个更新请求都是**更新内存BufferPool**，然后**顺序写日志文件**，同时还能保证各种异常情况下的数据一性。更新内存的性能是极高的，然后顺序写磁盘上的日志文件的性能也是非常高的，要远高于随机读写磁盘文件。正是通过这套机制，才能让我们的MySQL数据库在较高配置的机器上每秒可以抗下几干甚至上万的读写请求。



#### 执行一个SQL的步骤：

undo日志文件、redo日志文件（Innodb引擎持有）、binlog（server层，binlog主要用来恢复数据库磁盘里的数据）

redo日志文件：如果事务提交成功，buffer pool里的数据还没来得及写入磁盘，此时系统宕机了，可以用redo日志里的数据恢复buffer pool里的缓存数据

1.加载缓存数据加载id为1的记录所在的整页数据（16kb）

2.写入更新数据的旧值便于回滚（如果事务提交失败要回滚数据，可以用undo日志里的数据恢复buffer pool里的缓存数据）

3.更新buffer poll 缓存池的数据

4.写redo日志

5.准备提交事务 redo日志写入磁盘

6.准备提交事务 binlog日志吸入磁盘

7.写入commit标记到redo日志文件里，提交事务完成，该标记为了保证事务提交后redo于binlog数据一致

8.随机写入磁盘，以page为单位写入，这步做完磁盘里的数据就是最新的了





mysql主从

1.数据安全

2.读写分离 缓解压力

3.高可用 故障转移



#### 索引失效

1.mysql在使用不等于**（！=，<>，not in **，**not exists,（is null,is not null）一般不走索引 ）*的时候无法使用索引会导致全表扫描 

2.不在索引列上做任何操作（计算、函数、（自动or手动）类型转换），会导致索引失效而转向全表扫描

EXPLAIN SELECT FROM employees WHERE **left(name,3)*= 'LiLei'; 

3.存储引擎不能使用索引中范围条件右边的列

4.**<***小于、***>***大于、***<=**、**>=*这些，mysql内部优化器会根据检索比例、表大小等多个因素整体评估是否使用索引

5.**字符串不加单引号索引失效**

6.**少用or或in，用它查询时，mysql不一定使用索引，mysql内部优化器会根据检索比例、表大小等多个因素整体评*

**估是否使用索引，详见范围查询优化**

## 面试题

### 线上怎么修改列的数据类型的？

方式一:使用mysql5.6 提供的在线修改功能。

ALTER TABLE table_name change old_field_name new_field_name field_type;

那么，在mysql5.5这个版本之前，这是通过临时表拷贝的方式实现的。执行ALTER语句后，会**新建**一个带有新结构的**临时表**，将原表数据全部拷贝到临时表，然后Rename，完成创建操作。这个方式过程中，原表是可读的，不可写。



## 数据库设计范式

第一范式

每一列属性都是不可在分的属性值，确保每一列的原子性

第二范式

有一个主键唯一标识

第三范式

要求一个数据库表中不包含已在其他表中包含的非主关键字信息，即数据不能存在传递关系，即每个属性都跟主键有直接关系而不是间接关系



范式化和反范式话

1.性能提升-冗余，缓存和汇总

2.性能提升-计数器表

3.反范式设计-分库分表中的查询



回表优化MRR

只为用于搜索，排序或分组的列创建索引

三星索引

1.索引将相关的记录放到一起则获得一星 离得越近比如在同一个页，扫瞄的范围设置的很窄

2.如果索引中的数据顺序和查找的排序顺序一致则获得二星（排序星）

3.如果索引种的列包含了查询中需要的全部列则获得三星（宽索引星）可以理解为覆盖索引



磁盘读取512字节 有的会是4kb

1.寻道时间 要把磁头移动到某一个确认的硬盘之上 最长

2.旋转时间 寻找匹配的数据

3.传输时间

寻道时间+旋转时间+传输时间

一般是9到10ms左右



磁盘4k 预读 往往是整数位

成本

I/O成本 1.0

CPU成本 0.2

show table status 表名 表估算值

单表查询的成本

基于成本的优化步骤

1、根据搜索条件，找出所有可能使用的索引

2、计算全表扫描的代价

3、计算使用不同索引执行查询的代价

4、对比各种执行方案的代价，找出成本最低的那一个



预读和局部性原理：

当一个数据被用到时，其附近的数据也通常会马上被使用。

程序运行期间所需要的数据通常比较集中。

in

```
# index dive
show variables like '%dive%';
200
大于两个估算
小于实际计算
```

explain format=json <SQL语句>  /G

![image-20220109155137536](noteImg/image-20220109155137536.png)

索引合并

MySQL在一般情况下执行一个查询时最多只会用到单个二级索引，但存在有特殊情况，在这样特殊情况下也可能在一个查询中使用到多个二级索引，MySQL种这种使用到多个索引来完成一次查询的执行方法称之为：索引合并/index merge

1. intersection合并 select from order_exp WHERE order_no = 'a' AND expire_time = 'b'

   等值匹配 select from order_exp WHERE order_no>'a' AND insert_time = 'a' AND order_status = 'b' AND expire_time = 'c'

   主键列可以是范围匹配 select from order_exp WHERE id > 100 AND insert_time = 'a'

2. Union合并 select from order_exp WHERE insert_time = 'a' AND order_status = 'b' AND expire_time = 'c' OR (order_no = 'a' AND expire_time = 'b')

3. Sort—Union合并 select from order_exp WHERE order_no < 'a' OR expire_time > 'z'



## MySQL查询优化规则详解

![image-20220110095426383](noteImg/image-20220110095426383.png)

![image-20220110210140515](noteImg/image-20220110210140515.png)

物化表是指在内存或者磁盘建立一张临时表以查出的列为基础，为临时表建立索引，去掉重复数据后，和外层数据进行关联





File Header

Page Header

Infimum + Supremum

User Records

Free Space

Page Directory

File Tailer





独立表空间结构

区 （64个页）

段（256个区）用两个段来区分是叶子结点 还是非叶子结点 页节点段 非页节点段

## Innodb三个特性

Adaptive Hash Index

Buffer pool

Double write buffer

预读不是特性



mysql官方推荐的设置为80% 加上其他一些使用额外还有10% 比较危险

show engine innodb status;查看innodb使用情况 hit rate命中率

buffer pool 如果再没有专人管理和实时监控的情况下 设置为内存的60%

如果有专人管理和实时监控的情况下 设置为内存的75% 并持续根据业务和数据情况进行增大和减小

以上都是推荐值

free链表

![image-20220111155228919](noteImg/image-20220111155228919.png)

每一个空白页都有一个控制块

被修改过的页又叫做脏页

flush链表 被修改过的页都生成一个链表 和free链表差不多

LRU（Least recently used）链表的管理

热区young 冷 old

热区域63% 37%

刷新脏页到磁盘

BUF_FLUSH_LRU

BUF_FLUSH_LIST 异步

BUF_FLUSH_SINGLE_PAGE

事务原子性是通过undo日志来实现的 

持久性 redo  wal机制

隔离性读写锁 mvcc

acid

c是目的 aid是手段

redo

1.logbuffer快满的时候

2.事务提交的时候

3.专门线程刷盘

4.系统推出的时候

ib_logfile0

ib_logfile1

覆盖写

innodb_flush_log_at_trx_commit(0,1,2)

0.事务提交的时候不立即同步redo日志 由后台线程同步

1.默认值 commit的时候强制刷新到磁盘

2.当事务commit时候，将redo日志写到操作系统的缓冲区，并不保证将日志刷新到磁盘之上

redo log 那个数据页在某个位置进行了修改

binlog逻辑日志 sql的原始逻辑 记录数据变化的业务逻辑 binlog无法判断哪些数据已经落盘 哪些没有落盘的

redo和binlog通过2pc来保证数据一致性





## mybatis的一级缓存失效 

**会现在自己的threadlocal里面找sqlsession 如果没有创建一个新的 创建完成之后在设置到threadloca变量里面，如果没有开启事务就会一级缓存失效。**

```java
@Override
public <T> T selectOne(String statement, Object parameter) {
  return this.sqlSessionProxy.selectOne(statement, parameter);
}
```

```java
public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
    PersistenceExceptionTranslator exceptionTranslator) {

  notNull(sqlSessionFactory, "Property 'sqlSessionFactory' is required");
  notNull(executorType, "Property 'executorType' is required");

  this.sqlSessionFactory = sqlSessionFactory;
  this.executorType = executorType;
  this.exceptionTranslator = exceptionTranslator;
  this.sqlSessionProxy = (SqlSession) newProxyInstance(
      SqlSessionFactory.class.getClassLoader(),
      new Class[] { SqlSession.class },
      new SqlSessionInterceptor());
}
```

![image-20220115165113769](noteImg/image-20220115165113769.png)

![image-20220115165125624](noteImg/image-20220115165125624.png)



![image-20220115165225216](noteImg/image-20220115165225216.png)







## jdbc四大核心对象

Connection

DriverManager

PreparedStatement

ResultSet



![image-20220115190839410](noteImg/image-20220115190839410.png)

maven-source-plugin maven插件 生成jar包的同时 生成source包



```java
XMLConfigBuilder.java //解析xml文件

XmlMapperBuilder.java //解析mapper文件
```

### 添加唯一索引 

alter table account add unique (appId, accountId)

### 添加字段

alter table 表名 add字段 类型 其他;



### MySQL与redis缓存的同步方案

参考文章：https://blog.csdn.net/androidstarjack/article/details/115191588
方案1：通过MySQL自动同步刷新Redis，MySQL触发器+UDF函数实现

- 在MySQL中对要操作的数据设置触发器Trigger，监听操作
- 客户端（NodeServer）向MySQL中写入数据时，触发器会被触发，触发之后调用MySQL的UDF函数
- UDF函数可以把数据写入到Redis中，从而达到同步的效果

- 这种方案适合于读多写少，并且不存并发写的场景
- 因为MySQL触发器本身就会造成效率的降低，如果一个表经常被操作，这种方案显示是不合适的

方案2：解析MySQL的binlog实现，将数据库中的数据同步到Redis
canal是阿里巴巴旗下的一款开源项目，纯Java开发。基于数据库增量日志解析提供增量数据订阅&消费，目前主要支持了MySQL（也支持mariaDB）

### **Mysql分页**

总页数公式：totalRecord是总记录数；pageSize是一页分多少条记录

```sql
int totalPageNum = (totalRecord + pageSize - 1) / pageSize;
```

参考文章：https://blog.csdn.net/m0_45899013/article/details/10735764



### 查看SQL执行时间

```sql
show variables like '%profiling%';

set profiling = on;

执行语句

show profiles;查看执行时间
```



### 生成数据脚本

```sql
truncate department;

SET GLOBAL log_bin_trust_function_creators=TRUE; -- 创建函数一定要写这个
DELIMITER $$   -- 写函数之前必须要写，该标志

CREATE FUNCTION mock_data()     -- 创建函数（方法）
RETURNS INT                 -- 返回类型
BEGIN                         -- 函数方法体开始
 DECLARE num INT DEFAULT 1000000;      -- 定义一个变量num为int类型。默认值为100 0000
 DECLARE i INT DEFAULT 0;

 WHILE i < num DO            -- 循环条件
     INSERT INTO department(depno,depname,memo)
     VALUES(i,concat('depname',i),concat('memo',i));
    SET i =  i + 1;    -- i自增
 END WHILE;    -- 循环结束
 RETURN i;
END;

# drop function mock_data;

select mock_data();
```





然而，当查询的索引含有唯一属性时，InnoDB存储引擎对Next-Key Lock进行优化，将其降级为Record Lock，即仅锁住索引本身，而不是范围。

很明显，这时SQL语句通过索引列b进行查询，因此其使用传统的Next-Key Locking技术加锁，并且由于有两个索引，其需要分别进行锁定。对于聚集索引，其仅对列a等于5的索引加上Record Lock。而对于辅助索引，其加上的是Next-Key Lock，锁定的范围是(1，3)，特别需要注意的是，InnoDB存储引擎还会对辅助索引下一个键值加上Gap Lock，既还有一个辅助索引范围为(3，6)



## 锁

![img](https://upload-images.jianshu.io/upload_images/18392321-0cea8be39189fb12.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/598/format/webp)

参考文章：https://www.jianshu.com/p/478bc84a7721

**读锁会阻塞写(X)，但是不会堵塞读(S)。而写锁则会把读(S)和写(X)都堵塞**

共享锁：

```sql
select * from table_name where ... lock in share mode
```

​		**对于共享锁而言，对当前行加共享锁，不会阻塞其他事务对同一行的读请求，但会阻塞对同一行的写请求。只有当读锁释放后，才会执行其它事物的写操作。**

排他锁：

```sql
select * from table_name where ... for update
```

​		**对于排它锁而言，会阻塞其他事务对同一行的读和写操作，只有当写锁释放后，才会执行其它事务的读写操作。**

#### 记录锁（Record Locks）

```sql
SELECT * FROM `test` WHERE `id`=1 FOR UPDATE;
```

- **id 列必须为唯一索引列或主键列**，否则上述语句加的锁就会变成临键锁(有关临键锁下面会讲)。
- **同时查询语句必须为精准匹配（=）**，不能为 >、<、like等，否则也会退化成临键锁。

​	在通过 **主键索引** 与 **唯一索引** 对数据行进行 UPDATE 操作时，也会对该行数据加记录锁，`记录锁也是排它(X)锁`,所以会阻塞其他事务对其**插入、更新、删除**。

#### 间隙锁（Gap Locks）

间隙锁 是 **Innodb 在 RR(可重复读) 隔离级别** 下为了解决`幻读问题`时引入的锁机制。**间隙锁是innodb中行锁的一种**。左开右开

#### 临键锁（Next-Key Locks）

**Next-key总是会去锁定索引记录，如果InnoDB存储引擎表在建立的时候没有设置任何一个索引，那么这时InnoDB存储引擎会使用隐式的主键来进行锁定。**

**锁的是一个区间，例如一个索引有10，11，13和20这四个值，那么该索引可能被Next-Key Locking的区间为：**

【Negative infinity，10】

【10，11】

【11，13】

【13，20】

【20，Positive infinity】

采用Next-Key Lock的锁定技术称为Next-key Locking。其设计的目的是为了解决Phantom Problem，锁定的不是单个值，而是一个范围，是谓词锁（predict lock）的一种改进。除了next-key locking，还有previous-key locking技术。同样上述的索引10、11、13和20，若采用previous-key locking技术，那么可锁定的区间为：

【negative infinity，10】

【10，11】

【11，13】

【13，20】

【20，positive infinity】

当查询的索引还有唯一属性时，InnoDB存储引擎会对Next-key Lock进行优化，将其降级为Record Lock，即仅锁住索引本身，而不是范围。

```sql
DROP TABLE IF EXISTS t；
CREATE TABLE T (a INT PRIMARY KEY);
INSERT INTO t SELECT 1；
INSERT INTO t SELECT 2；
INSERT INTO t SELECT 5；
```

| 时间 | 会话A                                 | 会话B                    |
| ---- | ------------------------------------- | ------------------------ |
| 1    | BEGIN;                                |                          |
| 2    | SELECT * FROM t WHERE a=5 FOR UPDATE; |                          |
| 3    |                                       | BEGIN；                  |
| 4    |                                       | INSERT INTO t SELECT 4； |
| 5    |                                       | COMMIT；成功，不需要等待 |
| 6    | COMMIT                                |                          |

**由于a是主键且唯一，Next-Key Lock算法降级为了Record Lock，从而提高应用的并发性。正如前面所介绍的，Next-Key Lock降级为Record Lock仅在查询的列是唯一索引的情况下。若是辅助索引，则情况会完全不同。同样，首先根据如下代码创建测试表z：**

```sql
CREATE TABLE z （a INT，b INT，PRIMARY KEY（a），KEY（b））；
INSERT INTO z SELECT 1，1；
INSERT INTO z SELECT 3，1；
INSERT INTO z SELECT 5，3；
INSERT INTO z SELECT 7，6；
INSERT INTO z SELECT 10，8；
```

表z的列b是辅助索引，若在会话A中执行下面的SQL语句：

```sql
SELECT * FROM z WHERE b =3 FOR UPDATE；
```

很明显，这时SQL语句通过索引列b进行查询，因此其使用传统的Next-Key Locking技术加锁，并且由于有两个索引，其需要分别进行锁定。对于聚集索引，其仅对列a等于5的索引加上Record Lock。而对于辅助索引，其加上的是Next-Key Lock，锁定的范围是（1，3），特别注意的是，**InnoDB存储引擎还会对辅助索引下一个键值加上Gap Lock**，既还有一个辅助索引范围为（3，6）的锁。因此，若在新会话B中运行下面的SQL语句，都会被阻塞：

```sql
SELECT * FROM z WHERE a = 5 LOCK IN SHARE MODE;
INSERT INTO z SELECT 4,2;
INSERT INTO z SELECT 6,5;
```

第一个SQL语句不能执行，因为在会话A种执行的SQL语句已经对聚集索引中列a = 5的值加上X锁，因此执行会被阻塞。第二个SQL语句，主键插入4，没有问题，但是插入的辅助索引值2在锁定的范围（1，3）中，因此执行同样会被阻塞。第三个SQL语句，插入的主键6没有被锁定，5也不在范围（1，3）之间。但插入的值在另一个锁定的范围（3，6）中，故同样需要等待。而下面的SQL语句，不会被阻塞，可以立即执行：

```sql
INSERT INTO z SELECT 8,6;
INSERT INTO z SELECT 2,0;
INSERT INTO z SELECT 6,7;
```

在InnoDB存储引擎中，对于INSERT的操作，其会检查插入记录的下一条记录是否被锁定，若已经被锁定，则不允许查询。对于上面的例子，会话A已经锁定了表z中b=3的记录，即已经锁定了（1，3）的范围，这时若在其他会话中进行如下的插入同样会导致阻塞：

```sql
INSERT INTO z SELECT 2,2;
```

因为在辅助索引列b上插入值为2的记录时，会检测到下一个记录3已经被索引。而将插入修改为如下的值，可以立即执行：

```sql
INSERT INTO z SELECT 2，0；
```

最后需再次提醒的是，对于唯一键值的锁定，Next-Key Lock降级为Record Lock仅存于查询所有的唯一索引列。**若唯一索引有多个列组成，而查询是查找多个唯一索引列中的其中一个，**那么查询其实是range类型查询，而不是point类型查询，故InnoDB存储引擎**依然适用Next-Key Lock进行锁定。**

#### **总结：**

1、锁唯一索引会降级为Record Lock

2、锁辅助索引会先锁定聚集索引一条记录用**Record Lock**，对于辅助索引其加上的是**Next-Key Lock**，还有对于辅助索引下一键值加上**Gap Lock**

3、唯一索引有多个列组成，查询唯一索引列中的其中一个，依然会使用Next-Key Lock进行锁定