构建电商Spring-Boot

逻辑结构图：

![image-20251030155444969](C:\Users\forsa\AppData\Roaming\Typora\typora-user-images\image-20251030155444969.png)



![image-20251030155603957](C:\Users\forsa\AppData\Roaming\Typora\typora-user-images\image-20251030155603957.png)



![image-20251030161523490](C:\Users\forsa\AppData\Roaming\Typora\typora-user-images\image-20251030161523490.png)



梳理思路：

这是我现在的包结构：

![image-20251030173509382](构建电商Spring-Boot.assets/image-20251030173509382.png)



model:

```java
public class Category {

    private Long categoryId;
    private String categoryName;

    // 无参构造
    public Category() {
    }

    // 有参构造
    public Category(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getter & Setter
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

```



CategoryController:

```java
@RestController
public class CategoryColler {

	private List<Category> categories = new ArrayList<>();
	
	@GetMapping("/api/public/categories")
	public List<Category> getAllCategories() {
		return categories;
	}
    
    @PostMapping("/api/public/categories")
    public String addCategory(@RequestBody  Category category) {
        categories.add(category);
        return "Cateory added successfully";
    }
}
```

**这里注意下 @GetMapping 的return值，因为我传错了**





2. 引入接口

首先在`service`层

创建接口，明确我传的对象是谁，一个查询所有的表单`getAllCategories`和创建`addCategory`

注：写到这发现命名不太规范，后面修改成`createCategory`

```java
public interface CategoryService {
	
	List<Category> getAllCategories();
	
	void createCategory(Category category);
}
```



第二步创建对于接口的实现类`CategoryServiceImpl`

```java
@Service
public class CategoryServiceImpl implements  CategoryService {
	
	private List<Category> categories = new ArrayList<>();
	
	public CategoryServiceImpl(List<Category> categories) {
        this.categories = categories;
    }
    
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }
    
    @Override
    public void createCategory(Category category) {
        categories.add(category);
    }
}
```



最后修改控制层`CategoryController`

```java
public class CategoryController {
	
    private CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping("/api/public/categories")
    public List<Category> getAllCategories() {
        return categories.getAllCategories();
    }
    
    @PostMapping("/api/public/categories")
    public String createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return "Category added successfully";
    }
}
```



### 个人注意的点：

**@Autowired我是因为添加错误地方，应该添加到controller?所以为什么在service会出错**

Spring 容器会理解为：

> “帮我注入一个类型是 `List` 的 Bean。”

但是你 **根本没有在容器里定义 `List` 的 Bean**，所以 Spring 报错

💡 **关键点**：`@Autowired` 只会注入 **Spring 容器管理的 Bean**，而普通的 `new ArrayList<>()` 并不是 Bean。

| 对象类型                                     | Spring 能否注入（@Autowired） | 说明                      |
| -------------------------------------------- | ----------------------------- | ------------------------- |
| `@Service`、`@Component`、`@Bean` 创建的对象 | ✅ 可以注入                    | 容器管理的 Bean           |
| 普通 `new ArrayList<>()`                     | ❌ 不能注入                    | Spring 容器不知道它的存在 |

```java
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/api/public/categories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping("/api/public/categories")
    public String createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return "Category added successfully!";
    }
}

```





### Challenge: Managing ID’s

[05:56:18](https://www.youtube.com/watch?v=m559BxR30ls&t=21378s) 

问题：在springBoot,例如用户在创建数据时候，Id永远不会被用户输入是什么意思？

在Spring Boot中使用JPA（Java Persistence API）来处理数据持久化时（例如构建电商应用，如视频中提到的那个项目），ID字段通常被设计为自动生成，而不是由用户手动输入。这是一个常见的数据库设计实践，用于确保数据的唯一性和安全性。

##### 1. **ID自动生成的含义**

- 用户创建数据时不输入ID

  ：当用户通过API（如POST请求）创建新记录（例如创建一个新的“Category”或“Product”）时，不需要在请求体（JSON数据）中提供ID值。ID会由系统（数据库或JPA）自动分配。

  - 例如：在视频的电商项目中，当你添加一个新类别（Category）时，用户只需提供如“name”等字段，ID会自动生成（如1、2、3...）。
  - 如果用户试图在请求中输入ID（例如{"id": 100, "name": "Books"}），系统通常会忽略它或抛出错误，因为ID是系统管理的。

- 为什么这样设计？

  - **唯一性保证**：ID是数据库表的主键（Primary Key），必须全局唯一。如果允许用户输入，可能会导致重复ID、数据冲突或安全问题（如用户恶意输入已存在的ID覆盖数据）。
  - **自动化管理**：数据库（如MySQL、H2）可以自动递增ID（例如从1开始，每次+1），这简化了开发，避免手动跟踪ID。
  - **安全性**：防止用户篡改ID，导致数据泄露或非法操作。
  - **视频中的上下文**：转录中提到“Challenge: Managing ID’s”和“Generation Types For Identity”，这正是讨论如何处理ID的挑战。视频强调使用JPA将Java类转换为数据库表时，ID应自动生成，以优化应用（如电商中的订单ID、产品ID）。

如果用户输入ID，可能会出现：

- 数据库错误（如主键冲突）。

- JPA忽略用户提供的ID，并生成新ID

  

这里我先谈谈我的解决办法：

因为我想到此时案例中的数据是通过接口的实例化传入的数据，所以要让框架自动生成id

所以我要对`service`层的`CategoryServiceImpl.class`中进行修改，

思路是：

​	我想要通过`if`和`for`,利用`if`方法，去判断如果我传入的数据`ID=null`，就自增。

​	`for`方法去判断，我传入的数据是重复

最后实现确实实现保证不会重复，但是并不会自增。

```java
@Override
    public void createCategory(Category category) {
       if (category.getCategoryId() == null) {
           category.setCategoryId(nextId++);
}
        //目前实现不了，因为当你创建一个类别的时候，categoryId是null，所以会报空指针异常
        // nextId根本没有数据走进去
        // 解决办法：要不@Id,要不配置类里构造List

       for (Category c : categories) {
            if (c.getCategoryName().equals(category.getCategoryName())) {
                throw new RuntimeException("Category name already exists!");
        }
        }
        categories.add(category);
    }
```



最佳解决部分，我不用去判断用户传入的id是否会重复，我强制的将传入的数据实现自增即可。

```java
 private Long nextId = 1L;

@Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }
```





Delete Category

[06:02:05](https://www.youtube.com/watch?v=m559BxR30ls&t=21725s) 

Controller:

```java
@DeleteMapping("api/admin/categories/{categoryId}")
public String deleteCategory(@PathVariable Long categoryId) {
    String status = categoryService.deleteCategory(categoryId);
    return status;
}
```



ICategoryService:

新添加的数据：（add data about delete）

```java
String deleteCategory(Long categoryId)
```



CategoryServiceImpl:

```java
@Override
public String deleteCategory(Long categoryId){
    // 把model的Category转化成stream, 然后根据id过滤出category,再equals进行比对，找到对应的category
	Category category = categories.stream()
			 .filter(c -> c.getCategoryId().equals(categoryId))
			 .findFirst().get();
	
	categories.remove(category);
	return " Category with categoryId: " + categoryId + " deleted successfully";
}
```

但是可能存在的问题：

例如我如果已经删除了`Id=1`这个数据，如果我再次删除会抛出异常。

![image-20251031144142855](构建电商Spring-Boot.assets/image-20251031144142855.png)

为了解决这个，我希望如果删除类别中没有的数据，我希望抛出错误信息，such:"no find Id"

这是需要加入`orElse`

即：先用 `stream(...).filter(...)` 找到 `Category category`，如果没有对应的 `categoryId` 的条目，那么 `category` 会是 `null`（用了 `orElse(null)`），此时就应该返回 “未找到” 的提示。

CategoryServiceImpl:

```java
@Override
public String deleteCategory(Long categoryId) {
    Category category = categories.stream()
        .filter(c -> c.getCategoryId().equals(categoryId))
        .findFirst().orElse(null);
    
    if (category == null) {
        return "Category not found";
    }
    
    categories.remove(category);
    return " Category with categoryId: " + categoryId + " deleted successfully";
}
```



<img src="构建电商Spring-Boot.assets/image-20251031145201580.png" alt="image-20251031145201580" style="zoom:50%;" />

个人错误：

```
if (categoryId == null) {
        return "Category not found";
    }
    
```

直接逻辑错误！！！！

现在的意思是：如果传入的方法参数 `categoryId` 本身为 `null`，就返回 ‘未找到’”——而不是判断查询结果是否为空。





### ResponseEntity Class

[06:12:26](https://www.youtube.com/watch?v=m559BxR30ls&t=22346s) 

即使是上述方法，也有很大的问题，

![image-20251031152232104](构建电商Spring-Boot.assets/image-20251031152232104.png)

例如这里我删除111,这个数据当然不存在，但是成功抛出`Category not found `的异常，但是问题是它返回的状态码很有问题？

200ok，不应该是404没有找到吗？

所以我们需要用的ResponseEntity Class，来自定义状态



```java
   @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus. NOT_FOUND, "Category not found"));

        categories.remove(category);
        return " Category with categoryId: " + categoryId + " deleted successfully";
    }

```

![image-20251031155128292](构建电商Spring-Boot.assets/image-20251031155128292.png)

上面图片的问题是因为在Controller没有捕获异常引起的问题：

```java
@DeleteMapping("api/admin/categories/{categoryId}")
public String deleteCategory(@PathVariable Long categoryId) {
    String status = categoryService.deleteCategory(categoryId);
    return status;
}
```

修正后：

```java
 @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<String> deteleCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
        }
    }
```

![image-20251031154806419](构建电商Spring-Boot.assets/image-20251031154806419.png)



这样当你要删除一个不存在的Id数时,就可以返回正常的状态码和自定义语句。



### Using ResponseEntity for all Endpoints

try catch 返回的相似的方法

```
return new ResponseEntity<>(status, HttpStatus.OK);
return ResponseEntity.ok(status);
return ResponseEntity.status(HttpStatus.OK).body(status);
```

这三种写法其实都是在使用 ResponseEntity 来构造和返回 HTTP 响应

##### 三者差别（什么时候用哪一个）

- 如果你确定：响应状态就是 200 OK，且只返回一个简单 body，没有额外头部或特殊状态 → 使用 `ResponseEntity.ok(status)` 足够、简洁。
- 如果你需要指定状态（哪怕只是 OK）但可能以后扩展，比如添加头、改变状态 → 使用 `ResponseEntity.status(HttpStatus.OK).body(status)` 更灵活。
- 使用 `new ResponseEntity<>(status, HttpStatus.OK)` 通常在你习惯“构造器方式”或在某些老代码里，功能与第一种类似，但可读性稍逊于静态方法形式。
- 从维护性/可读性角度，推荐第二或第三种写法。



### 使用 ResponseEntity 的好处

1. **完整控制 HTTP 响应**
    ResponseEntity 表示 **整个 HTTP 响应**：状态码（status）、响应头（headers）、响应体（body）。[Baeldung on Kotlin+2Stack Overflow+2](https://www.baeldung.com/spring-response-entity?utm_source=chatgpt.com)
    例如：你可以返回 404（Not Found）而不仅仅是默认 200，或者给客户端返回特定 header。[Medium+1](https://medium.com/nerd-for-tech/importance-of-using-responseentity-5e37da704e88?utm_source=chatgpt.com)
2. **更精确表达业务结果**
    不同操作可能有不同结果、不同状态码：
   - 查询成功 → 200 OK（并返回数据）
   - 查询找不到 → 404 Not Found
   - 新增成功 → 201 Created
   - 删除成功但无返回内容 → 204 No Content
      用 ResponseEntity 可以明确返回这些。[DEV Community](https://dev.to/debeshpg90/responseentity-in-spring-boot-httpstatus-rest-api-java-spring-2kg7?utm_source=chatgpt.com)
      如果只返回一个普通对象，Spring 默认状态为 200，可能无法表达“资源不存在”、“创建成功并返回位置”这些细节。
3. **增加响应头／自定义 header**
    有时候你需要在响应里加 header：例如 `Location`（新创建资源的 URL）、`ETag`、缓存指令、自定义 header。ResponseEntity 让这变得容易。[Baeldung on Kotlin](https://www.baeldung.com/spring-response-entity?utm_source=chatgpt.com)
4. **统一错误/特殊场景处理**
    对于“操作失败”、“资源不存在”的情况，你可以用 ResponseEntity 返回合适的状态码、错误消息、结构化体（比如一个 JSON 错误对象），而不是仅仅返回一个普通对象或抛异常让框架默认为 500。这个让 API 更清晰、客户端更好理解。



**但是并不是都使用ResponseEntity ！**

虽然很多场景用 ResponseEntity 很合适，但并非所有方法都必须它。比如：

- 如果你的 Controller 方法只做“查询并返回对象”，并且逻辑很简单：总是成功、有数据返回且始终状态为 200，那你可以直接返回实体对象（比如 `public Category getCategory(...)` 而不是 `ResponseEntity`）。Spring 会自动把返回体放入响应，状态默认为 200。[Stack Overflow+1](https://stackoverflow.com/questions/61138943/what-is-responseentity-for-and-why-should-i-keep-it?utm_source=chatgpt.com)
- 如果你用的是全局异常处理（比如 ControllerAdvice + ExceptionHandler）来统一处理错误状态码、错误消息，你的方法就可以更简洁。只有在需要“特殊状态码”或“头部”或“没有返回体”的时候才用 ResponseEntity。

优化之后：

```java
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<String> deteleCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
        }
    }
}
```



Challenge: Update Category

[06:27:04](https://www.youtube.com/watch?v=m559BxR30ls&t=23224s) 

```java
@PutMapping()
public ResponseEntity<> updateCategory (@Resquest Category category,@Pathvariable Long categoryId) {
    
    try{
        Catetory category = CatetoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("Category with Id: " + categoryId, HttpStatus.OK);
    } catch(ResponseStatusException e) {
        return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
    }
}
```



```java
@Override
public Category updateCategory(Category category, Long categoryId) {
	Optional<Category> optionalCategory = categories.stream()
        .filter(c -> c.getCategoryId().equals(categoryId))
        .findFirst();
    if (OptionalCategory.isPresent()) {
        Category existingCategory = optionalCategory.get();
        existingCategory.setCategoryName(category.getCategoryName());
        return existingCategory;
    } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
    }
}
```





### @RequestMapping Annotation

[06:36:18](https://www.youtube.com/watch?v=m559BxR30ls&t=23778s) 

映射注解可以在哪些地方使用，有哪些使用场景？


| 位置                   | 说明                                                         |
| ---------------------- | ------------------------------------------------------------ |
| **类（Class）级别**    | 通常用于定义控制器的**基础路径**，所有该类中的请求处理方法都会继承这个路径。 |
| **方法（Method）级别** | 用于定义具体的请求路径、HTTP 方法、参数等，精确匹配一个请求处理逻辑。 |



for example:

You try it:

```java
@RestController
// 尝试添加新的@RequsetMapping
@RequestMapping
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // @GetMapping("/api/public/categories")
    @RequestMapping(value="/api/public/categories", method=RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // @PostMapping("/api/public/categories")
    @RequestMapping(value="/api/public/categories", method=RequestMethod.POST)
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }
}
```



```java
//按照原来进行优化
@RequestMapping("/api")
public class CategoryController {

	@GetMapping("/public/categories")
	
	@PostMapping("/public/categories")
}
```



Basics: Understanding Data & Databases

[06:42:55](https://www.youtube.com/watch?v=m559BxR30ls&t=24175s) 

![image-20251101154131302](构建电商Spring-Boot.assets/image-20251101154131302.png)



![image-20251101154135058](构建电商Spring-Boot.assets/image-20251101154135058.png)



总之，应用程序实现持久化和动态属性，是因为数据库的存在。

你可以试想一下，如果一个房间，他堆满大量的文件，而你只有一张桌子可以使用，那么此时你再次寻找数据，或者是寻找文件，你不得不把他分类**。数据库就是帮助你管理这些文件或者叫做数据的工具。**

![image-20251101154450809](构建电商Spring-Boot.assets/image-20251101154450809.png)



数据库的工作流程如下：

![image-20251101154520284](构建电商Spring-Boot.assets/image-20251101154520284.png)





### What is DBMS

[06:43:57](https://www.youtube.com/watch?v=m559BxR30ls&t=24237s) 

**DBMS** 是一套用于**创建、存储、管理、维护和查询数据库**的软件系统。它是用户（或应用程序）与操作系统之间的**中间层**，提供了一套标准化的方式来操作数据。

> 简单说：**DBMS 就是“管数据库的软件”**。

![image-20251101155145755](构建电商Spring-Boot.assets/image-20251101155145755.png)



#### 一、总览对比表

| 对比维度     | **关系型数据库（SQL）**                | **NoSQL 数据库**                          |
| ------------ | -------------------------------------- | ----------------------------------------- |
| **全称**     | Relational Database Management System  | Not Only SQL                              |
| **数据模型** | 表格（Table）、行（Row）、列（Column） | 键值、文档、宽列、图                      |
| **Schema**   | **固定 Schema**（先定义表结构）        | **动态 Schema**（灵活，无需预定义）       |
| **查询语言** | SQL（结构化查询语言）                  | 各异（如 CQL、MongoDB Query、Redis 命令） |
| **事务支持** | 强一致性，**ACID**                     | 多为 **BASE**（最终一致性）               |
| **扩展方式** | 垂直扩展（升级硬件）                   | 水平扩展（加机器）                        |
| **数据关系** | 支持外键、JOIN                         | 一般不建议 JOIN，数据冗余设计             |

------

#### 二、常见系统分类

##### **1. 关系型数据库（SQL）**

| 数据库         | 特点                           |
| -------------- | ------------------------------ |
| **MySQL**      | 开源、社区活跃、适合中小型应用 |
| **PostgreSQL** | 功能强大、支持 JSON、GIS       |
| **Oracle**     | 企业级、稳定、昂贵             |
| **SQL Server** | 微软生态、Windows 集成好       |
| **SQLite**     | 嵌入式、单文件、无服务器       |

------

##### **2. NoSQL 数据库（按数据模型分）**

| 类型                    | 代表数据库            | 典型数据格式           |
| ----------------------- | --------------------- | ---------------------- |
| **键值（Key-Value）**   | Redis, Memcached      | { "user:1001": "Tom" } |
| **文档（Document）**    | MongoDB, CouchDB      | JSON/BSON 文档         |
| **列族（Wide Column）** | Cassandra, HBase      | 超大宽表，按列存储     |
| **图（Graph）**         | Neo4j, Amazon Neptune | 节点 + 边（关系）      |

------

#### 三、典型运用场景 & 存储数据

| 场景                      | 推荐数据库           | 存储的数据             | 原因                 |
| ------------------------- | -------------------- | ---------------------- | -------------------- |
| **用户登录、权限管理**    | MySQL / PostgreSQL   | 用户表、角色表、权限表 | 需要事务、强一致性   |
| **电商订单、库存**        | MySQL + Redis        | 订单、商品、库存快照   | 事务 + 高并发读写    |
| **日志、时序数据**        | Cassandra / InfluxDB | 日志行、时间戳 + 值    | 海量写、低延迟       |
| **缓存、会话（Session）** | Redis                | Key-Value              | 超高性能、内存存储   |
| **内容管理、博客、CMS**   | MongoDB              | 文章 JSON、评论嵌套    | 结构灵活，易扩展字段 |
| **社交关系、推荐系统**    | Neo4j                | 用户-关注-用户         | 图遍历效率高         |
| **实时排行榜、计数器**    | Redis (Sorted Set)   | 用户ID + 分数          | ZINCRBY 原子操作     |
| **移动端离线数据**        | SQLite               | 本地用户设置、缓存     | 轻量、嵌入式         |

------

#### 四、什么时候选 SQL？什么时候选 NoSQL？

| 选择 SQL（关系型）                  | 选择 NoSQL                         |
| ----------------------------------- | ---------------------------------- |
| 数据结构**固定**（如订单、用户）    | 数据结构**频繁变化**（如产品属性） |
| 需要**复杂查询**（多表 JOIN、统计） | 查询简单，主要**按 ID 查**         |
| 强调**事务一致性**（转账不能错）    | 接受**最终一致性**（日志可稍晚）   |
| 数据量**中等**（百万级）            | 数据量**超大**（百亿级）           |
| 业务逻辑复杂，报表多                | 高并发写，读模式简单               |

------

#### 五、优缺点总结

| 类型      | 优点                                            | 缺点                                       |
| --------- | ----------------------------------------------- | ------------------------------------------ |
| **SQL**   | - 成熟标准（SQL） - 强事务（ACID） - 复杂查询强 | - 扩展性差（难水平扩展） - Schema 变更麻烦 |
| **NoSQL** | - 水平扩展容易 - 灵活性高 - 高并发写            | - 无统一查询语言 - 事务弱 - 学习成本高     |



实际生产最常用的是：

```
后端：MySQL（主数据） + Redis（缓存） + MongoDB（日志/内容） + Elasticsearch（搜索）
```





### “组织形式”是关键？

1. **MySQL → SQL Server（同构迁移）**：

   - **数据组织类似**：都用表存储，迁移主要是“翻译”差异。
   - **工具友好**：SSMA、DBConvert 等自动转换 Schema 和数据。
   - **痛点**：外键需手动调整，性能调优。

   **示例**：

   sql

   ```
   -- MySQL
   CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50));
   
   -- SQL Server（类似，但类型微调）
   CREATE TABLE users (id INT PRIMARY KEY, name NVARCHAR(50));
   ```

2. **MySQL → MongoDB（异构迁移）**：

   - **组织形式天差地别**：多表 JOIN → 单文档嵌套；固定 Schema → 动态文档。
   - **需重设计**：规范化数据变“反规范化”（冗余存储以加速读）。
   - **应用重构**：SQL 查询 → Mongo 查询；ORM（如 Hibernate） → ODM（如 Mongoose）。



可能需要是数据库迁移工具的帮助：

| 路径                   | 工具                                                         | 免费？   |
| ---------------------- | ------------------------------------------------------------ | -------- |
| **MySQL → SQL Server** | SSMA、DBConvert、Skyvia                                      | 部分免费 |
| **MySQL → MongoDB**    | MongoDB Relational Migrator、Tapdata、Python 脚本（pymongo + mysql.connector） | 大多免费 |







### JPA 和 H2



添加完成相关依赖后，通过更改配置文件：

```
spring.h2.console.enabled=true
```

成功运行程序后：

```
http://localhost:8080/h2-console
```

输入后你可以得到：

![image-20251101173923808](构建电商Spring-Boot.assets/image-20251101173923808.png)

JDBC URL 就是当你添加H2依赖运行后的程序（一般写在日志里面）：

![image-20251101174104298](构建电商Spring-Boot.assets/image-20251101174104298.png)



![image-20251101174141929](构建电商Spring-Boot.assets/image-20251101174141929.png)

填入后直接链接即可使用：

但是这样每次重新启用服务是不确定的`url`的，因为他是动态生成。

这时要加入相关配置：就可以自定义路径

```
spring.datasource.url=jdbc:h2:men:test
```

![image-20251101174617127](构建电商Spring-Boot.assets/image-20251101174617127.png)

发现路径被修改

成功链接:

![image-20251101174810368](构建电商Spring-Boot.assets/image-20251101174810368.png)







### Understanding Entities in JPA

[08:22:41](https://www.youtube.com/watch?v=m559BxR30ls&t=30161s) 

核心问题：实体究竟是什么？在JPA下，实体代表什么？

**实体 = 一条数据库记录的 Java 对象表示**

具体说明：实体代表：

| 层面           | 代表的内容                                              |
| -------------- | ------------------------------------------------------- |
| **数据库层面** | **一张表中的一行记录（Row）**                           |
| **Java 层面**  | **一个带有 `@Entity` 注解的普通 Java 类（POJO）的实例** |
| **ORM 映射**   | **对象 ↔ 关系表** 的桥梁                                |



#### JPA的生命周期：

JPA 会跟踪实体的状态，决定如何与数据库交互：

| 状态                       | 含义                          | 说明                                       |
| -------------------------- | ----------------------------- | ------------------------------------------ |
| **New（新建）**            | 对象刚 new 出来，还没持久化   | entityManager.persist(user) 后才进入持久化 |
| **Managed（托管/持久化）** | 已被 EntityManager 管理       | 修改属性会自动同步到 DB（脏检查）          |
| **Detached（游离）**       | 曾经持久化过，但当前会话结束  | 需要 merge() 重新关联                      |
| **Removed（已删除）**      | 调用了 remove()，等待提交删除 | 事务提交时执行 DELETE                      |



#### JPA的使用要求：

| 要求                                | 说明                             |
| ----------------------------------- | -------------------------------- |
| 1. 使用 `@Entity` 注解              | 标记为实体                       |
| 2. 有 `@Id` 字段                    | 必须有主键                       |
| 3. 有无参构造器                     | 反射创建实例（`protected` 也行） |
| 4. 不是 `final` 类                  | 否则无法代理（延迟加载需要）     |
| 5. 字段是 `private` + getter/setter | JPA 通过方法访问                 |



实际案例：

![image-20251101181312470](构建电商Spring-Boot.assets/image-20251101181312470.png)

这里我通过两个注解`@Entity`和`@Id`,将`categoryId`这个私有变量被标记成数据库的主键，作为唯一标识

此时我们再次访问H2

![image-20251101181539829](构建电商Spring-Boot.assets/image-20251101181539829.png)

#### 自动建表的原因：

| 原因                                             | 说明                                                |
| ------------------------------------------------ | --------------------------------------------------- |
| 1. 你用的是 **H2 内存数据库**（常见于测试/开发） | 默认配置会开启自动建表                              |
| 2. 你 **没有显式设置 `ddl-auto=none`**           | Spring Boot **默认值是 `create-drop`**（H2 特例！） |
| 3. 你加了 `@Entity` 和 `@Id`                     | 满足 JPA 实体最低要求，Hibernate 就能识别           |

也可以通过直接在实体`@Entity`注解后添加，实现修改表名：

<img src="构建电商Spring-Boot.assets/image-20251101182216613.png" alt="image-20251101182216613" style="zoom:50%;" />



###  Behind the Scenes & Additional Properties

[08:29:23](https://www.youtube.com/watch?v=m559BxR30ls&t=30563s)

#### 如果进行一些行为配置：

我们就可以看到`JPA`自动数据库建表等操作

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=none
```



**spring.jpa.show-sql=true 运行后，对原本系统行为的** **唯一改变** **是：**

> **在控制台（或日志文件）额外打印出 Hibernate 发出的所有 SQL 语句（不含参数值）**。

<img src="构建电商Spring-Boot.assets/image-20251101183324067.png" alt="image-20251101183324067" style="zoom:50%;" />

**spring.jpa.properties.hibernate.format_sql=true**

> **将 show-sql=true 打印出来的 SQL 语句进行“美化格式化”（换行 + 缩进），让复杂 SQL 更易读。**



**spring.jpa.hibernate.ddl-auto=none**

| 配置            | 价值                                        |
| --------------- | ------------------------------------------- |
| show-sql=true   | **开发调试神器**：一眼看穿 JPA 发了什么 SQL |
| format_sql=true | **提升可读性**：复杂查询不乱                |
| ddl-auto=none   | **生产安全锁**：杜绝意外改表                |

> **一句话总结**： **“只看 SQL，不改表” —— 开发透明，生产安全。**

<img src="构建电商Spring-Boot.assets/image-20251101182700735.png" alt="image-20251101182700735" style="zoom:50%;" />

一般取值表:

| 取值          | 作用                               | 是否建表/改表  | 适用场景              |
| ------------- | ---------------------------------- | -------------- | --------------------- |
| `create`      | **删旧表 → 重建表**                | 是（全删全建） | 单元测试、快速原型    |
| `create-drop` | **启动建表，关闭删表**             | 是（临时表）   | 集成测试（H2 内存库） |
| `update`      | **根据实体同步表结构，不删数据**   | 是（增量改）   | 本地开发              |
| `validate`    | **校验实体与表结构一致性，不改表** | 否             | 预生产验证            |
| `none`        | **什么都不做**                     | 否             | 生产环境（默认）      |

上述操作均属于：DDL



#### DDL和DML的区别：

| 维度                             | `spring.jpa.hibernate.ddl-auto`               | **数据库事务（@Transactional）**                 |
| -------------------------------- | --------------------------------------------- | ------------------------------------------------ |
| **操作类型**                     | **DDL**（定义语言） `CREATE`, `ALTER`, `DROP` | **DML**（操作语言） `INSERT`, `UPDATE`, `DELETE` |
| **作用对象**                     | **表结构**（Schema）                          | **表数据**（Data）                               |
| **是否可回滚**                   | **不可回滚**（DDL 自动提交）                  | **可回滚**（事务失败可撤销）                     |
| **执行时机**                     | **应用启动时**（一次性）                      | **方法运行时**（每次调用）                       |
| **是否受 `@Transactional` 控制** | **不受**                                      | **受**                                           |
| **是否影响业务逻辑**             | **不影响**（只管结构）                        | **直接影响**（增删改查）                         |





### Generation Types For Identity

[08:38:26](https://www.youtube.com/watch?v=m559BxR30ls&t=31106s) 

```java
@GeneratedValue(strategy = GenerationType.)
```

| 策略 (GenerationType) | 说明                                                         | 常见数据库支持                                               | 优点                                                         | 注意事项／适用场景                                           |
| --------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `AUTO`                | 让 JPA 提供者（如 Hibernate）自动选择最合适的主键生成策略。 ([Baeldung on Kotlin](https://www.baeldung.com/hibernate-identifiers?utm_source=chatgpt.com)) | 几乎所有，但具体行为依赖数据库 + JPA 实现。                  | 配置简单，适合数据库切换或不想关心细节的场景。               | 可能造成在不同数据库下行为不一致（例如从 MySQL 转 PostgreSQL 时策略变了）。 ([Stackademic](https://stackademic.com/blog/avoid-using-generationtype-auto-strategy-for-id-generation-in-spring-boot?utm_source=chatgpt.com)) |
| `IDENTITY`            | 使用数据库的“自动增长／自增列”（如 MySQL 的 AUTO_INCREMENT、SQL Server 的 IDENTITY）来生成主键。 ([Stack Overflow](https://stackoverflow.com/questions/33096466/generationtype-auto-vs-generationtype-identity-in-hibernate?utm_source=chatgpt.com)) | MySQL、SQL Server、PostgreSQL（部分支持）、Oracle 12c 以后支持 IDENTITY 列。 ([tutorialspoint.com](https://www.tutorialspoint.com/difference-between-sequence-and-identity-in-hibernate?utm_source=chatgpt.com)) | 配置最简单；插入操作数据库即可生成主键。                     | 不支持批量插入优化（batch insert）／JPA 对延迟获取 ID 限制。若大量插入、高性能场景可能不是最佳。 ([vladmihalcea.com](https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/?utm_source=chatgpt.com)) |
| `SEQUENCE`            | 使用数据库的“序列对象”（Sequence）生成主键值，例如 Oracle、PostgreSQL 的 sequence。 ([Stack Overflow](https://stackoverflow.com/questions/8955074/generatedvaluestrategy-identity-vs-generatedvaluestrategy-sequence?utm_source=chatgpt.com)) | Oracle、PostgreSQL、DB2 等支持序列。 MySQL 普通版本不支持原生序列。 ([Stack Overflow](https://stackoverflow.com/questions/33096466/generationtype-auto-vs-generationtype-identity-in-hibernate?utm_source=chatgpt.com)) | 性能较好，支持批量插入、JPA 优化；灵活可配置。 ([Thorben Janssen](https://thorben-janssen.com/jpa-generate-primary-keys/?utm_source=chatgpt.com)) | 若数据库**不支持**序列（如旧版 MySQL）需额外配置或回退策略。需定义 `@SequenceGenerator` 等。 |
| `TABLE`               | 使用一个专门的数据库表来模拟主键生成（即自己维护一个“计数器”表）。 ([vladmihalcea.com](https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/?utm_source=chatgpt.com)) | 所有数据库都能支持（因为只是一个普通表），但性能较差。       | 最具可移植性（在所有数据库上都能工作）。                     | 性能最弱：每次生成 ID 都可能访问计数器表、并发性能差。一般只有在其他策略不可用时才考虑。 ([vladmihalcea.com](https://vladmihalcea.com/why-you-should-never-use-the-table-identifier-generator-with-jpa-and-hibernate/?utm_source=chatgpt.com)) |

例如`SEQUENCE`和`TABLE`

![image-20251101185610948](构建电商Spring-Boot.assets/image-20251101185610948.png)

![image-20251101185614258](构建电商Spring-Boot.assets/image-20251101185614258.png)



#### `@GeneratedValue` 的使用原因：

使用 `@GeneratedValue` 的主要原因包括以下几点：

1. **简化主键管理**
    使用 `@GeneratedValue` 可让 ORM（如 Hibernate／EclipseLink）框架自动为实体类的主键字段生成唯一值，而不需要开发人员手动设置或维护。比如当你加一个新实体并保存时，框架会帮你产生 `id`。 [GeeksforGeeks+2Home+2](https://www.geeksforgeeks.org/advance-java/hibernate-generatedvalue-annotation-in-jpa/?utm_source=chatgpt.com)
2. **保证唯一性与一致性**
    主键必须唯一，并且通常是系统内部使用的“技术主键”（而不是来自业务逻辑的“天然键”）。通过 `@GeneratedValue`，你可以使用数据库或 ORM 提供的机制确保每条记录都有一个唯一且高效的标识。 [Thorben Janssen+1](https://thorben-janssen.com/jpa-generate-primary-keys/?utm_source=chatgpt.com)
3. **数据库性能与扩展考虑**
    合适的生成策略（`IDENTITY`、`SEQUENCE`、`TABLE` 等）可利用数据库内建机制（如自增列、序列）来生成主键，从而提高插入效率、减少冲突。比如使用 `GenerationType.SEQUENCE` 时，可以预分配一批 ID，从而减少每次插入时访问数据库序列的开销。 [Baeldung on Kotlin+1](https://www.baeldung.com/hibernate-identifiers?utm_source=chatgpt.com)
4. **让实体映射逻辑与数据库细节解耦**
    通过注解方式指定主键生成策略，实体类不需要硬编码具体的自增逻辑或业务编号生成逻辑，从而让代码更清晰、易维护、数据库迁移（换一种 DB）时调整更容易。 [medium.com+1](https://medium.com/%40gaddamnaveen192/complete-guide-to-jpa-id-generation-auto-identity-sequence-and-table-3044891e88af?utm_source=chatgpt.com)





### JPA Repositories

概念图：

![image-20251101211719298](构建电商Spring-Boot.assets/image-20251101211719298.png)

![image-20251101202704848](构建电商Spring-Boot.assets/image-20251101202704848.png)

新建一个**负责数据访问（操作数据库）`CategoryRepository`**，通常继承 Spring Data JPA 的接口

```java
// JapRepository <实体， 主键的字符类型>
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
```

| 特性         | `CrudRepository`                      | `JpaRepository`                                         |
| ------------ | ------------------------------------- | ------------------------------------------------------- |
| 继承关系     | 基础接口                              | 继承 `CrudRepository` + `PagingAndSortingRepository`    |
| 核心方法     | `save`, `findById`, `delete`, `count` | **全部 + 额外高级功能**                                 |
| 额外方法     | 无                                    | `findAll()`, `deleteAll()`, `flush()`, `saveAndFlush()` |
| 分页排序     | 无                                    | 支持 `Pageable`                                         |
| **是否推荐** | **仅用于极简场景**                    | **99% 项目用这个**                                      |



```java
@Service
public class CategoryServiceImpl implements CategoryService{



    @Autowired
    private CategoryRepository categoryRepository;


//    public CategoryServiceImpl(List<Category> categories) {
//        this.categories = categories;
//    }


//    @Override
//    public List<Category> getAllCategories() {
//        return categories;
//    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


//    @Override
//    public void createCategory(Category category) {
//        category.setCategoryId(nextId++);
//        categories.add(category);
//    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

//    @Override
//    public String deleteCategory(Long categoryId) {
//        Category category = categories.stream()
//                .filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus. NOT_FOUND, "Category not found"));
//
//        categories.remove(category);
//        return " Category with categoryId: " + categoryId + " deleted successfully";
//    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
        return " Category with categoryId: " + categoryId + " deleted successfully";
    }



//    @Override
//    public Category updateCategory(Category category, Long categoryId) {
//        // 使用 Stream 查找 ID 匹配的分类，返回 Optional 包装的结果
//        Optional<Category> optionalCategory = categories.stream()
//                .filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst();
//
//        // 判断查询结果是否为空
//        if (optionalCategory.isPresent()) {
//            Category existingCategory = optionalCategory.get();
//            existingCategory.setCategoryName(category.getCategoryName());
//            return existingCategory;
//        } else {
//
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
//        }
//    }

    @Override
    public Category updateCategory(Category category, Long categoryId){

        List<Category> categories = categoryRepository.findAll();

        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();

        // 判断查询结果是否为空
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepository.save(existingCategory);
            return savedCategory;
        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }


    }

}
```



#### 为什么 **POST（新增）** 会变成 **UPDATE（修改）**？

------

核心原因：**save() 方法的行为取决于 id 是否为 null**

问题原因：

| 原因                                       | 解释     |
| ------------------------------------------ | -------- |
| **1. `save()` 看 `id` 决定 INSERT/UPDATE** | JPA 规范 |
| **2. 你在 POST 里传了 `categoryId`**       | 人为错误 |
| **3. 以为“新增要传 ID”**                   | 误解     |
| **4. 复制了 GET/PUT 的请求体**             | 操作失误 |

过程说明：

| 步骤 | 说明                                                         |
| ---- | ------------------------------------------------------------ |
| 1    | 你发 POST 请求，带了 `{"categoryId": 1, "categoryName": "Phones"}` |
| 2    | Spring 反序列化成 `Category` 对象，`categoryId = 1`          |
| 3    | `createCategory(category)` 调用 `save(category)`             |
| 4    | Hibernate 看到 `id = 1` → 认为这是一个 **“已存在的实体”**    |
| 5    | 去数据库查 `WHERE category_id = 1`                           |
| 6    | 如果查到 → **执行 UPDATE** 如果查不到 → **执行 INSERT**（但 ID 仍是你传的 `1`） |
| 7    | 你以为是“新增”，其实可能是 **修改了别人** 或 **跳过了自增**  |

核心设计：

| 好处          | 说明                                          |
| ------------- | --------------------------------------------- |
| **统一接口**  | 一个 `save()` 同时支持新增和修改              |
| **简化代码**  | 不需要写 `if (id == null) insert else update` |
| **符合 REST** | POST = 创建新资源，PUT = 更新资源             |

比喻：

| 场景                                    | 对应                             |
| --------------------------------------- | -------------------------------- |
| 你去饭店点餐                            | POST                             |
| 你说：“我要一份宫保鸡丁”                | `{ "categoryName": "宫保鸡丁" }` |
| 服务员不会问：“你要第几桌？”            | 不需要 `categoryId`              |
| 但如果你说：“我要 **3号桌** 的宫保鸡丁” | 带了 `categoryId`                |
| 服务员会以为你要 **改单**               | → 执行 UPDATE                    |



###  Using Optionals in Services [Optimizing]

[09:00:21](https://www.youtube.com/watch?v=m559BxR30ls&t=32421s)

```java
    @Override
    public Category updateCategory(Category category, Long categoryId) {

        // “查找 + 判断是否存在” 的标准模式，根据categoryId 去调用categoryRepository的JPA data的findById
        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        // 2. 若不存在，抛出 404
        Category savedCategory = savedCategoryOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));

        // 把前端传进来的 id 设回去（防止前端漏传或被篡改）
        category.setCategoryId(categoryId);

        // JPA 会根据主键判断是 UPDATE 还是 INSERT
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }
```



旧的写法

```java
    @Override
    public Category updateCategory(Category category, Long categoryId){

        List<Category> categories = categoryRepository.findAll();

        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();

        // 判断查询结果是否为空
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepository.save(existingCategory);
            return savedCategory;
        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

```



| 维度               | 当前写法（`findById` + `save`）                              | 旧写法（`findAll` + 手动 set）                        |
| ------------------ | ------------------------------------------------------------ | ----------------------------------------------------- |
| **查询效率**       | **O(1)**：直接走数据库主键索引                               | **O(n)**：全表扫描，数据量大时极慢                    |
| **内存占用**       | 只加载 **1 条记录**                                          | 加载 **全部记录**，容易 OOM                           |
| **SQL 语句**       | `SELECT ... WHERE id = ?` → `UPDATE ... SET ... WHERE id = ?` | `SELECT * FROM category` → 可能一次 `UPDATE`          |
| **字段更新灵活性** | **全字段覆盖**（只要前端传了非 null 就更新） 若想部分更新需额外处理 | **硬编码只更新 `categoryName`**，想加新字段必须改代码 |
| **代码简洁度**     | **极简**，JPA 自动处理 merge                                 | 冗长，手动遍历 + 手动复制字段                         |
| **事务/并发安全**  | 直接在同一个事务内完成查找+更新                              | 同上（但因全表加载可能引发更大锁竞争）                |
| **扩展性**         | 易于配合 `BeanUtils.copyProperties(category, savedCategory, "categoryId")` 实现 **部分更新** | 每新增字段都要改 `setXxx`                             |
| **是否推荐**       | **强烈推荐**（生产环境标准做法）                             | **不推荐**，仅适合极小表或学习演示                    |



##### update个人的疑问：

**为什么`JPA`在设计时候不直接将`update`和`insert`分开？**

可以看看他的底层：

```java
if (entity.isNew()) {
    entityManager.persist(entity);   // INSERT
} else {
    entityManager.merge(entity);     // UPDATE (或 INSERT 如果 ID 不存在)
}
```

- isNew() 默认判断：**@Id 是否为 null**（可通过 @Entity 的 org.hibernate.annotations.Entity 自定义）

- merge()

   会：

  1. SELECT 查出数据库中的实体
  2. 把传进来对象的字段 **复制过去**
  3. UPDATE 回去

> 所以 save() **一定会触发一次 SELECT**（除非在同一个 EntityManager 会话中已加载）



**总结为什么不分开写**

| 优点               | 说明                                      |
| ------------------ | ----------------------------------------- |
| **统一接口**       | 调用者只关心“保存”，不关心新旧            |
| **减少样板代码**   | 无需 `if (id == null) insert else update` |
| **符合 JPA 规范**  | 基于 `persist` / `merge` 设计             |
| **易于测试和维护** | 所有 CRUD 走同一套流程                    |



或者更简洁的写法

```java
  @Override
    public Category updateCategory(Category category, Long categoryId) {
        
		Category savedCategory = CategoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        
         // 确保Id传回来，保护数据
         savedCategory.setCategoryId(categoryId);
         // 让JPA自行判断是Update还是Insert
         savedCategory = CategoryRepository(category);
         return savedCategory;
    }

```



再次完善下Delete

```java
    @Override
    public String deleteCategory(Long categoryId) {

        // 查找+判断 基本模式
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        // 如果不存在，返回404
        Category Category = optionalCategory
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(Category);
        return " Category with categoryId: " + categoryId + " deleted successfully";
    }
```



旧的：

```java
    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
        return " Category with categoryId: " + categoryId + " deleted successfully";
    }
```



或者简单模式：

```java
  @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
        return "Category deleted" + categoryId + "successfully!";

    }
```



##### 删除的问题？

> **“为什么 deleteCategory 只需要传 categoryId，而 updateCategory 却要在 category 对象上 setCategoryId(id)？”**

答案就在于：**delete 是“按 ID 删除”，而 update 是“按实体保存”** —— 两者的 **JPA 机制完全不同**。



##### 总结：

| 项目             | 删除                      | 更新                              |
| ---------------- | ------------------------- | --------------------------------- |
| 是否需要实体     | 不需要                    | 必须                              |
| 是否需要 `setId` | 不需要                    | **必须**                          |
| 推荐方法         | `deleteById(id)`          | `save(entity)` + `setId(id)`      |
| 底层 SQL         | `DELETE ... WHERE id = ?` | `UPDATE ... SET ... WHERE id = ?` |
| 是否需要先查     | `existsById`（可选）      | `findById`（必须，防覆盖）        |



### Experimenting Beyond

[09:08:28](https://www.youtube.com/watch?v=m559BxR30ls&t=32908s) 

实验小结：

实验一：

如果我在我的`model`实体类中删除了`getter` ` setter`方法会发生什么？



![image-20251102165009259](构建电商Spring-Boot.assets/image-20251102165009259.png)

这可以发现，将name的`getter setter`方法取消，不会报错，但是当你插入数据的时候，会显示`NULL`



实验二：

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

善用hibernate，当我们执行插入操作或者其他CRUD的操作时候：

我们可以再终端中看到`JPA`到底执行了什么样的`sql`语句，for example:

<img src="构建电商Spring-Boot.assets/image-20251102165311542.png" alt="image-20251102165311542" style="zoom:50%;" />



实验三：

<img src="构建电商Spring-Boot.assets/image-20251102165635803.png" alt="image-20251102165635803" style="zoom:50%;" />



<img src="构建电商Spring-Boot.assets/image-20251102165640486.png" alt="image-20251102165640486" style="zoom:50%;" />



这些都表明：通过继承JPA,一些类型是可自定义的：例如ID,

还有案例中的`category`已经定义成为一个实体类。







Lombok

旧的代码：

```java
package com.ecommerce.project.model;

import jakarta.persistence.*;

@Entity(name = "CATEGORIES")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;

    public Category() {
    }

    public Category(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
```



加入Lombok注解后

```java
@Entity(name = "CATEGORIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;

}
```



来自Gork的总结：

你用的是 **Lombok**，一个能大幅减少 Java 样板代码的库。下面我 **逐行解释你代码中每个 Lombok 注解的作用**，并说明 **它们背后生成了哪些代码**。

------

##### 实体类

```java
@Entity(name = "CATEGORIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    
    private String categoryName;
}
```

------

##### 一、@Entity(name = "CATEGORIES")

| 注解                | 作用                                                       |
| ------------------- | ---------------------------------------------------------- |
| @Entity             | 标记这是一个 **JPA 实体类**，会被 Hibernate 映射到数据库表 |
| name = "CATEGORIES" | 显式指定 **逻辑表名** 为 CATEGORIES                        |

> **默认行为**：如果不写 name，表名 = 类名 = Category → 数据库表名 category **你写 name = "CATEGORIES"** → 数据库表名强制为 **CATEGORIES**（大写）

------

##### 二、@Data

> **最强大、最常用的 Lombok 注解！**

###### @Data = 以下 6 个注解的**合集**：

| 注解                     | 生成的代码                              |
| ------------------------ | --------------------------------------- |
| @Getter                  | 所有字段自动生成 getXxx()               |
| @Setter                  | 所有字段自动生成 setXxx()               |
| @ToString                | 生成 toString()                         |
| @EqualsAndHashCode       | 生成 equals() 和 hashCode()             |
| @RequiredArgsConstructor | 为 **final 或 @NonNull 字段**生成构造器 |

> **注意**：你没有 final 字段，所以 @RequiredArgsConstructor **不会生成任何构造器**

------

###### @Data 实际为你生成了：

```java
// getter
public Long getCategoryId() { return categoryId; }
public String getCategoryName() { return categoryName; }

// setter
public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

// toString
@Override
public String toString() {
    return "Category(categoryId=" + categoryId + ", categoryName=" + categoryName + ")";
}

// equals & hashCode
@Override
public boolean equals(Object o) { ... }
@Override
public int hashCode() { ... }
```

------

##### 三、@NoArgsConstructor

###### 生成 **无参构造器**（JPA **必须要求**！）

```
public Category() {}
```

> **为什么 JPA 需要无参构造器？** 因为 Hibernate 在加载实体时，先 new Category()，再通过反射 setXxx() 赋值。

------

##### 四、@AllArgsConstructor

###### 生成 **全参构造器**

```java
public Category(Long categoryId, String categoryName) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
}
```

> 常用于 **测试、Builder 模式、DTO 转换** 等场景