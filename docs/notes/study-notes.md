鏋勫缓鐢靛晢Spring-Boot

閫昏緫缁撴瀯鍥撅細

![image-20251030155444969](C:\Users\forsa\AppData\Roaming\Typora\typora-user-images\image-20251030155444969.png)



![image-20251030155603957](C:\Users\forsa\AppData\Roaming\Typora\typora-user-images\image-20251030155603957.png)



![image-20251030161523490](C:\Users\forsa\AppData\Roaming\Typora\typora-user-images\image-20251030161523490.png)



姊崇悊鎬濊矾锛?

杩欐槸鎴戠幇鍦ㄧ殑鍖呯粨鏋勶細

![image-20251030173509382](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251030173509382.png)



model:

```java
public class Category {

    private Long categoryId;
    private String categoryName;

    // 鏃犲弬鏋勯€?
    public Category() {
    }

    // 鏈夊弬鏋勯€?
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

**杩欓噷娉ㄦ剰涓?@GetMapping 鐨剅eturn鍊硷紝鍥犱负鎴戜紶閿欎簡**





2. 寮曞叆鎺ュ彛

棣栧厛鍦╜service`灞?

鍒涘缓鎺ュ彛锛屾槑纭垜浼犵殑瀵硅薄鏄皝锛屼竴涓煡璇㈡墍鏈夌殑琛ㄥ崟`getAllCategories`鍜屽垱寤篳addCategory`

娉細鍐欏埌杩欏彂鐜板懡鍚嶄笉澶鑼冿紝鍚庨潰淇敼鎴恅createCategory`

```java
public interface CategoryService {
	
	List<Category> getAllCategories();
	
	void createCategory(Category category);
}
```



绗簩姝ュ垱寤哄浜庢帴鍙ｇ殑瀹炵幇绫籤CategoryServiceImpl`

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



鏈€鍚庝慨鏀规帶鍒跺眰`CategoryController`

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



### 涓汉娉ㄦ剰鐨勭偣锛?

**@Autowired鎴戞槸鍥犱负娣诲姞閿欒鍦版柟锛屽簲璇ユ坊鍔犲埌controller?鎵€浠ヤ负浠€涔堝湪service浼氬嚭閿?*

Spring 瀹瑰櫒浼氱悊瑙ｄ负锛?

> 鈥滃府鎴戞敞鍏ヤ竴涓被鍨嬫槸 `List` 鐨?Bean銆傗€?

浣嗘槸浣?**鏍规湰娌℃湁鍦ㄥ鍣ㄩ噷瀹氫箟 `List` 鐨?Bean**锛屾墍浠?Spring 鎶ラ敊

馃挕 **鍏抽敭鐐?*锛歚@Autowired` 鍙細娉ㄥ叆 **Spring 瀹瑰櫒绠＄悊鐨?Bean**锛岃€屾櫘閫氱殑 `new ArrayList<>()` 骞朵笉鏄?Bean銆?

| 瀵硅薄绫诲瀷                                     | Spring 鑳藉惁娉ㄥ叆锛園Autowired锛?| 璇存槑                      |
| -------------------------------------------- | ----------------------------- | ------------------------- |
| `@Service`銆乣@Component`銆乣@Bean` 鍒涘缓鐨勫璞?| 鉁?鍙互娉ㄥ叆                    | 瀹瑰櫒绠＄悊鐨?Bean           |
| 鏅€?`new ArrayList<>()`                     | 鉂?涓嶈兘娉ㄥ叆                    | Spring 瀹瑰櫒涓嶇煡閬撳畠鐨勫瓨鍦?|

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





### Challenge: Managing ID鈥檚

[05:56:18](https://www.youtube.com/watch?v=m559BxR30ls&t=21378s) 

闂锛氬湪springBoot,渚嬪鐢ㄦ埛鍦ㄥ垱寤烘暟鎹椂鍊欙紝Id姘歌繙涓嶄細琚敤鎴疯緭鍏ユ槸浠€涔堟剰鎬濓紵

鍦⊿pring Boot涓娇鐢↗PA锛圝ava Persistence API锛夋潵澶勭悊鏁版嵁鎸佷箙鍖栨椂锛堜緥濡傛瀯寤虹數鍟嗗簲鐢紝濡傝棰戜腑鎻愬埌鐨勯偅涓」鐩級锛孖D瀛楁閫氬父琚璁′负鑷姩鐢熸垚锛岃€屼笉鏄敱鐢ㄦ埛鎵嬪姩杈撳叆銆傝繖鏄竴涓父瑙佺殑鏁版嵁搴撹璁″疄璺碉紝鐢ㄤ簬纭繚鏁版嵁鐨勫敮涓€鎬у拰瀹夊叏鎬с€?

##### 1. **ID鑷姩鐢熸垚鐨勫惈涔?*

- 鐢ㄦ埛鍒涘缓鏁版嵁鏃朵笉杈撳叆ID

  锛氬綋鐢ㄦ埛閫氳繃API锛堝POST璇锋眰锛夊垱寤烘柊璁板綍锛堜緥濡傚垱寤轰竴涓柊鐨勨€淐ategory鈥濇垨鈥淧roduct鈥濓級鏃讹紝涓嶉渶瑕佸湪璇锋眰浣擄紙JSON鏁版嵁锛変腑鎻愪緵ID鍊笺€侷D浼氱敱绯荤粺锛堟暟鎹簱鎴朖PA锛夎嚜鍔ㄥ垎閰嶃€?

  - 渚嬪锛氬湪瑙嗛鐨勭數鍟嗛」鐩腑锛屽綋浣犳坊鍔犱竴涓柊绫诲埆锛圕ategory锛夋椂锛岀敤鎴峰彧闇€鎻愪緵濡傗€渘ame鈥濈瓑瀛楁锛孖D浼氳嚜鍔ㄧ敓鎴愶紙濡?銆?銆?...锛夈€?
  - 濡傛灉鐢ㄦ埛璇曞浘鍦ㄨ姹備腑杈撳叆ID锛堜緥濡倇"id": 100, "name": "Books"}锛夛紝绯荤粺閫氬父浼氬拷鐣ュ畠鎴栨姏鍑洪敊璇紝鍥犱负ID鏄郴缁熺鐞嗙殑銆?

- 涓轰粈涔堣繖鏍疯璁★紵

  - **鍞竴鎬т繚璇?*锛欼D鏄暟鎹簱琛ㄧ殑涓婚敭锛圥rimary Key锛夛紝蹇呴』鍏ㄥ眬鍞竴銆傚鏋滃厑璁哥敤鎴疯緭鍏ワ紝鍙兘浼氬鑷撮噸澶岻D銆佹暟鎹啿绐佹垨瀹夊叏闂锛堝鐢ㄦ埛鎭舵剰杈撳叆宸插瓨鍦ㄧ殑ID瑕嗙洊鏁版嵁锛夈€?
  - **鑷姩鍖栫鐞?*锛氭暟鎹簱锛堝MySQL銆丠2锛夊彲浠ヨ嚜鍔ㄩ€掑ID锛堜緥濡備粠1寮€濮嬶紝姣忔+1锛夛紝杩欑畝鍖栦簡寮€鍙戯紝閬垮厤鎵嬪姩璺熻釜ID銆?
  - **瀹夊叏鎬?*锛氶槻姝㈢敤鎴风鏀笽D锛屽鑷存暟鎹硠闇叉垨闈炴硶鎿嶄綔銆?
  - **瑙嗛涓殑涓婁笅鏂?*锛氳浆褰曚腑鎻愬埌鈥淐hallenge: Managing ID鈥檚鈥濆拰鈥淕eneration Types For Identity鈥濓紝杩欐鏄璁哄浣曞鐞咺D鐨勬寫鎴樸€傝棰戝己璋冧娇鐢↗PA灏咼ava绫昏浆鎹负鏁版嵁搴撹〃鏃讹紝ID搴旇嚜鍔ㄧ敓鎴愶紝浠ヤ紭鍖栧簲鐢紙濡傜數鍟嗕腑鐨勮鍗旾D銆佷骇鍝両D锛夈€?

濡傛灉鐢ㄦ埛杈撳叆ID锛屽彲鑳戒細鍑虹幇锛?

- 鏁版嵁搴撻敊璇紙濡備富閿啿绐侊級銆?

- JPA蹇界暐鐢ㄦ埛鎻愪緵鐨処D锛屽苟鐢熸垚鏂癐D

  

杩欓噷鎴戝厛璋堣皥鎴戠殑瑙ｅ喅鍔炴硶锛?

鍥犱负鎴戞兂鍒版鏃舵渚嬩腑鐨勬暟鎹槸閫氳繃鎺ュ彛鐨勫疄渚嬪寲浼犲叆鐨勬暟鎹紝鎵€浠ヨ璁╂鏋惰嚜鍔ㄧ敓鎴恑d

鎵€浠ユ垜瑕佸`service`灞傜殑`CategoryServiceImpl.class`涓繘琛屼慨鏀癸紝

鎬濊矾鏄細

鈥?鎴戞兂瑕侀€氳繃`if`鍜宍for`,鍒╃敤`if`鏂规硶锛屽幓鍒ゆ柇濡傛灉鎴戜紶鍏ョ殑鏁版嵁`ID=null`锛屽氨鑷銆?

鈥?`for`鏂规硶鍘诲垽鏂紝鎴戜紶鍏ョ殑鏁版嵁鏄噸澶?

鏈€鍚庡疄鐜扮‘瀹炲疄鐜颁繚璇佷笉浼氶噸澶嶏紝浣嗘槸骞朵笉浼氳嚜澧炪€?

```java
@Override
    public void createCategory(Category category) {
       if (category.getCategoryId() == null) {
           category.setCategoryId(nextId++);
}
        //鐩墠瀹炵幇涓嶄簡锛屽洜涓哄綋浣犲垱寤轰竴涓被鍒殑鏃跺€欙紝categoryId鏄痭ull锛屾墍浠ヤ細鎶ョ┖鎸囬拡寮傚父
        // nextId鏍规湰娌℃湁鏁版嵁璧拌繘鍘?
        // 瑙ｅ喅鍔炴硶锛氳涓岪Id,瑕佷笉閰嶇疆绫婚噷鏋勯€燣ist

       for (Category c : categories) {
            if (c.getCategoryName().equals(category.getCategoryName())) {
                throw new RuntimeException("Category name already exists!");
        }
        }
        categories.add(category);
    }
```



鏈€浣宠В鍐抽儴鍒嗭紝鎴戜笉鐢ㄥ幓鍒ゆ柇鐢ㄦ埛浼犲叆鐨刬d鏄惁浼氶噸澶嶏紝鎴戝己鍒剁殑灏嗕紶鍏ョ殑鏁版嵁瀹炵幇鑷鍗冲彲銆?

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

鏂版坊鍔犵殑鏁版嵁锛氾紙add data about delete锛?

```java
String deleteCategory(Long categoryId)
```



CategoryServiceImpl:

```java
@Override
public String deleteCategory(Long categoryId){
    // 鎶妋odel鐨凜ategory杞寲鎴恠tream, 鐒跺悗鏍规嵁id杩囨护鍑篶ategory,鍐峞quals杩涜姣斿锛屾壘鍒板搴旂殑category
	Category category = categories.stream()
			 .filter(c -> c.getCategoryId().equals(categoryId))
			 .findFirst().get();
	
	categories.remove(category);
	return " Category with categoryId: " + categoryId + " deleted successfully";
}
```

浣嗘槸鍙兘瀛樺湪鐨勯棶棰橈細

渚嬪鎴戝鏋滃凡缁忓垹闄や簡`Id=1`杩欎釜鏁版嵁锛屽鏋滄垜鍐嶆鍒犻櫎浼氭姏鍑哄紓甯搞€?

![image-20251031144142855](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251031144142855.png)

涓轰簡瑙ｅ喅杩欎釜锛屾垜甯屾湜濡傛灉鍒犻櫎绫诲埆涓病鏈夌殑鏁版嵁锛屾垜甯屾湜鎶涘嚭閿欒淇℃伅锛宻uch:"no find Id"

杩欐槸闇€瑕佸姞鍏orElse`

鍗筹細鍏堢敤 `stream(...).filter(...)` 鎵惧埌 `Category category`锛屽鏋滄病鏈夊搴旂殑 `categoryId` 鐨勬潯鐩紝閭ｄ箞 `category` 浼氭槸 `null`锛堢敤浜?`orElse(null)`锛夛紝姝ゆ椂灏卞簲璇ヨ繑鍥?鈥滄湭鎵惧埌鈥?鐨勬彁绀恒€?

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



<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251031145201580.png" alt="image-20251031145201580" style="zoom:50%;" />

涓汉閿欒锛?

```
if (categoryId == null) {
        return "Category not found";
    }
    
```

鐩存帴閫昏緫閿欒锛侊紒锛侊紒

鐜板湪鐨勬剰鎬濇槸锛氬鏋滀紶鍏ョ殑鏂规硶鍙傛暟 `categoryId` 鏈韩涓?`null`锛屽氨杩斿洖 鈥樻湭鎵惧埌鈥欌€濃€斺€旇€屼笉鏄垽鏂煡璇㈢粨鏋滄槸鍚︿负绌恒€?





### ResponseEntity Class

[06:12:26](https://www.youtube.com/watch?v=m559BxR30ls&t=22346s) 

鍗充娇鏄笂杩版柟娉曪紝涔熸湁寰堝ぇ鐨勯棶棰橈紝

![image-20251031152232104](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251031152232104.png)

渚嬪杩欓噷鎴戝垹闄?11,杩欎釜鏁版嵁褰撶劧涓嶅瓨鍦紝浣嗘槸鎴愬姛鎶涘嚭`Category not found `鐨勫紓甯革紝浣嗘槸闂鏄畠杩斿洖鐨勭姸鎬佺爜寰堟湁闂锛?

200ok锛屼笉搴旇鏄?04娌℃湁鎵惧埌鍚楋紵

鎵€浠ユ垜浠渶瑕佺敤鐨凴esponseEntity Class锛屾潵鑷畾涔夌姸鎬?



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

![image-20251031155128292](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251031155128292.png)

涓婇潰鍥剧墖鐨勯棶棰樻槸鍥犱负鍦–ontroller娌℃湁鎹曡幏寮傚父寮曡捣鐨勯棶棰橈細

```java
@DeleteMapping("api/admin/categories/{categoryId}")
public String deleteCategory(@PathVariable Long categoryId) {
    String status = categoryService.deleteCategory(categoryId);
    return status;
}
```

淇鍚庯細

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

![image-20251031154806419](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251031154806419.png)



杩欐牱褰撲綘瑕佸垹闄や竴涓笉瀛樺湪鐨処d鏁版椂,灏卞彲浠ヨ繑鍥炴甯哥殑鐘舵€佺爜鍜岃嚜瀹氫箟璇彞銆?



### Using ResponseEntity for all Endpoints

try catch 杩斿洖鐨勭浉浼肩殑鏂规硶

```
return new ResponseEntity<>(status, HttpStatus.OK);
return ResponseEntity.ok(status);
return ResponseEntity.status(HttpStatus.OK).body(status);
```

杩欎笁绉嶅啓娉曞叾瀹為兘鏄湪浣跨敤 ResponseEntity 鏉ユ瀯閫犲拰杩斿洖 HTTP 鍝嶅簲

##### 涓夎€呭樊鍒紙浠€涔堟椂鍊欑敤鍝竴涓級

- 濡傛灉浣犵‘瀹氾細鍝嶅簲鐘舵€佸氨鏄?200 OK锛屼笖鍙繑鍥炰竴涓畝鍗?body锛屾病鏈夐澶栧ご閮ㄦ垨鐗规畩鐘舵€?鈫?浣跨敤 `ResponseEntity.ok(status)` 瓒冲銆佺畝娲併€?
- 濡傛灉浣犻渶瑕佹寚瀹氱姸鎬侊紙鍝€曞彧鏄?OK锛変絾鍙兘浠ュ悗鎵╁睍锛屾瘮濡傛坊鍔犲ご銆佹敼鍙樼姸鎬?鈫?浣跨敤 `ResponseEntity.status(HttpStatus.OK).body(status)` 鏇寸伒娲汇€?
- 浣跨敤 `new ResponseEntity<>(status, HttpStatus.OK)` 閫氬父鍦ㄤ綘涔犳儻鈥滄瀯閫犲櫒鏂瑰紡鈥濇垨鍦ㄦ煇浜涜€佷唬鐮侀噷锛屽姛鑳戒笌绗竴绉嶇被浼硷紝浣嗗彲璇绘€х◢閫婁簬闈欐€佹柟娉曞舰寮忋€?
- 浠庣淮鎶ゆ€?鍙鎬ц搴︼紝鎺ㄨ崘绗簩鎴栫涓夌鍐欐硶銆?



### 浣跨敤 ResponseEntity 鐨勫ソ澶?

1. **瀹屾暣鎺у埗 HTTP 鍝嶅簲**
    ResponseEntity 琛ㄧず **鏁翠釜 HTTP 鍝嶅簲**锛氱姸鎬佺爜锛坰tatus锛夈€佸搷搴斿ご锛坔eaders锛夈€佸搷搴斾綋锛坆ody锛夈€俒Baeldung on Kotlin+2Stack Overflow+2](https://www.baeldung.com/spring-response-entity?utm_source=chatgpt.com)
    渚嬪锛氫綘鍙互杩斿洖 404锛圢ot Found锛夎€屼笉浠呬粎鏄粯璁?200锛屾垨鑰呯粰瀹㈡埛绔繑鍥炵壒瀹?header銆俒Medium+1](https://medium.com/nerd-for-tech/importance-of-using-responseentity-5e37da704e88?utm_source=chatgpt.com)
2. **鏇寸簿纭〃杈句笟鍔＄粨鏋?*
    涓嶅悓鎿嶄綔鍙兘鏈変笉鍚岀粨鏋溿€佷笉鍚岀姸鎬佺爜锛?
   - 鏌ヨ鎴愬姛 鈫?200 OK锛堝苟杩斿洖鏁版嵁锛?
   - 鏌ヨ鎵句笉鍒?鈫?404 Not Found
   - 鏂板鎴愬姛 鈫?201 Created
   - 鍒犻櫎鎴愬姛浣嗘棤杩斿洖鍐呭 鈫?204 No Content
      鐢?ResponseEntity 鍙互鏄庣‘杩斿洖杩欎簺銆俒DEV Community](https://dev.to/debeshpg90/responseentity-in-spring-boot-httpstatus-rest-api-java-spring-2kg7?utm_source=chatgpt.com)
      濡傛灉鍙繑鍥炰竴涓櫘閫氬璞★紝Spring 榛樿鐘舵€佷负 200锛屽彲鑳芥棤娉曡〃杈锯€滆祫婧愪笉瀛樺湪鈥濄€佲€滃垱寤烘垚鍔熷苟杩斿洖浣嶇疆鈥濊繖浜涚粏鑺傘€?
3. **澧炲姞鍝嶅簲澶达紡鑷畾涔?header**
    鏈夋椂鍊欎綘闇€瑕佸湪鍝嶅簲閲屽姞 header锛氫緥濡?`Location`锛堟柊鍒涘缓璧勬簮鐨?URL锛夈€乣ETag`銆佺紦瀛樻寚浠ゃ€佽嚜瀹氫箟 header銆俁esponseEntity 璁╄繖鍙樺緱瀹规槗銆俒Baeldung on Kotlin](https://www.baeldung.com/spring-response-entity?utm_source=chatgpt.com)
4. **缁熶竴閿欒/鐗规畩鍦烘櫙澶勭悊**
    瀵逛簬鈥滄搷浣滃け璐モ€濄€佲€滆祫婧愪笉瀛樺湪鈥濈殑鎯呭喌锛屼綘鍙互鐢?ResponseEntity 杩斿洖鍚堥€傜殑鐘舵€佺爜銆侀敊璇秷鎭€佺粨鏋勫寲浣擄紙姣斿涓€涓?JSON 閿欒瀵硅薄锛夛紝鑰屼笉鏄粎浠呰繑鍥炰竴涓櫘閫氬璞℃垨鎶涘紓甯歌妗嗘灦榛樿涓?500銆傝繖涓 API 鏇存竻鏅般€佸鎴风鏇村ソ鐞嗚В銆?



**浣嗘槸骞朵笉鏄兘浣跨敤ResponseEntity 锛?*

铏界劧寰堝鍦烘櫙鐢?ResponseEntity 寰堝悎閫傦紝浣嗗苟闈炴墍鏈夋柟娉曢兘蹇呴』瀹冦€傛瘮濡傦細

- 濡傛灉浣犵殑 Controller 鏂规硶鍙仛鈥滄煡璇㈠苟杩斿洖瀵硅薄鈥濓紝骞朵笖閫昏緫寰堢畝鍗曪細鎬绘槸鎴愬姛銆佹湁鏁版嵁杩斿洖涓斿缁堢姸鎬佷负 200锛岄偅浣犲彲浠ョ洿鎺ヨ繑鍥炲疄浣撳璞★紙姣斿 `public Category getCategory(...)` 鑰屼笉鏄?`ResponseEntity`锛夈€係pring 浼氳嚜鍔ㄦ妸杩斿洖浣撴斁鍏ュ搷搴旓紝鐘舵€侀粯璁や负 200銆俒Stack Overflow+1](https://stackoverflow.com/questions/61138943/what-is-responseentity-for-and-why-should-i-keep-it?utm_source=chatgpt.com)
- 濡傛灉浣犵敤鐨勬槸鍏ㄥ眬寮傚父澶勭悊锛堟瘮濡?ControllerAdvice + ExceptionHandler锛夋潵缁熶竴澶勭悊閿欒鐘舵€佺爜銆侀敊璇秷鎭紝浣犵殑鏂规硶灏卞彲浠ユ洿绠€娲併€傚彧鏈夊湪闇€瑕佲€滅壒娈婄姸鎬佺爜鈥濇垨鈥滃ご閮ㄢ€濇垨鈥滄病鏈夎繑鍥炰綋鈥濈殑鏃跺€欐墠鐢?ResponseEntity銆?

浼樺寲涔嬪悗锛?

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

鏄犲皠娉ㄨВ鍙互鍦ㄥ摢浜涘湴鏂逛娇鐢紝鏈夊摢浜涗娇鐢ㄥ満鏅紵


| 浣嶇疆                   | 璇存槑                                                         |
| ---------------------- | ------------------------------------------------------------ |
| **绫伙紙Class锛夌骇鍒?*    | 閫氬父鐢ㄤ簬瀹氫箟鎺у埗鍣ㄧ殑**鍩虹璺緞**锛屾墍鏈夎绫讳腑鐨勮姹傚鐞嗘柟娉曢兘浼氱户鎵胯繖涓矾寰勩€?|
| **鏂规硶锛圡ethod锛夌骇鍒?* | 鐢ㄤ簬瀹氫箟鍏蜂綋鐨勮姹傝矾寰勩€丠TTP 鏂规硶銆佸弬鏁扮瓑锛岀簿纭尮閰嶄竴涓姹傚鐞嗛€昏緫銆?|



for example:

You try it:

```java
@RestController
// 灏濊瘯娣诲姞鏂扮殑@RequsetMapping
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
//鎸夌収鍘熸潵杩涜浼樺寲
@RequestMapping("/api")
public class CategoryController {

	@GetMapping("/public/categories")
	
	@PostMapping("/public/categories")
}
```



Basics: Understanding Data & Databases

[06:42:55](https://www.youtube.com/watch?v=m559BxR30ls&t=24175s) 

![image-20251101154131302](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101154131302.png)



![image-20251101154135058](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101154135058.png)



鎬讳箣锛屽簲鐢ㄧ▼搴忓疄鐜版寔涔呭寲鍜屽姩鎬佸睘鎬э紝鏄洜涓烘暟鎹簱鐨勫瓨鍦ㄣ€?

浣犲彲浠ヨ瘯鎯充竴涓嬶紝濡傛灉涓€涓埧闂达紝浠栧爢婊″ぇ閲忕殑鏂囦欢锛岃€屼綘鍙湁涓€寮犳瀛愬彲浠ヤ娇鐢紝閭ｄ箞姝ゆ椂浣犲啀娆″鎵炬暟鎹紝鎴栬€呮槸瀵绘壘鏂囦欢锛屼綘涓嶅緱涓嶆妸浠栧垎绫?*銆傛暟鎹簱灏辨槸甯姪浣犵鐞嗚繖浜涙枃浠舵垨鑰呭彨鍋氭暟鎹殑宸ュ叿銆?*

![image-20251101154450809](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101154450809.png)



鏁版嵁搴撶殑宸ヤ綔娴佺▼濡備笅锛?

![image-20251101154520284](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101154520284.png)





### What is DBMS

[06:43:57](https://www.youtube.com/watch?v=m559BxR30ls&t=24237s) 

**DBMS** 鏄竴濂楃敤浜?*鍒涘缓銆佸瓨鍌ㄣ€佺鐞嗐€佺淮鎶ゅ拰鏌ヨ鏁版嵁搴?*鐨勮蒋浠剁郴缁熴€傚畠鏄敤鎴凤紙鎴栧簲鐢ㄧ▼搴忥級涓庢搷浣滅郴缁熶箣闂寸殑**涓棿灞?*锛屾彁渚涗簡涓€濂楁爣鍑嗗寲鐨勬柟寮忔潵鎿嶄綔鏁版嵁銆?

> 绠€鍗曡锛?*DBMS 灏辨槸鈥滅鏁版嵁搴撶殑杞欢鈥?*銆?

![image-20251101155145755](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101155145755.png)



#### 涓€銆佹€昏瀵规瘮琛?

| 瀵规瘮缁村害     | **鍏崇郴鍨嬫暟鎹簱锛圫QL锛?*                | **NoSQL 鏁版嵁搴?*                          |
| ------------ | -------------------------------------- | ----------------------------------------- |
| **鍏ㄧО**     | Relational Database Management System  | Not Only SQL                              |
| **鏁版嵁妯″瀷** | 琛ㄦ牸锛圱able锛夈€佽锛圧ow锛夈€佸垪锛圕olumn锛?| 閿€笺€佹枃妗ｃ€佸鍒椼€佸浘                      |
| **Schema**   | **鍥哄畾 Schema**锛堝厛瀹氫箟琛ㄧ粨鏋勶級        | **鍔ㄦ€?Schema**锛堢伒娲伙紝鏃犻渶棰勫畾涔夛級       |
| **鏌ヨ璇█** | SQL锛堢粨鏋勫寲鏌ヨ璇█锛?                 | 鍚勫紓锛堝 CQL銆丮ongoDB Query銆丷edis 鍛戒护锛?|
| **浜嬪姟鏀寔** | 寮轰竴鑷存€э紝**ACID**                     | 澶氫负 **BASE**锛堟渶缁堜竴鑷存€э級               |
| **鎵╁睍鏂瑰紡** | 鍨傜洿鎵╁睍锛堝崌绾х‖浠讹級                   | 姘村钩鎵╁睍锛堝姞鏈哄櫒锛?                       |
| **鏁版嵁鍏崇郴** | 鏀寔澶栭敭銆丣OIN                         | 涓€鑸笉寤鸿 JOIN锛屾暟鎹啑浣欒璁?            |

------

#### 浜屻€佸父瑙佺郴缁熷垎绫?

##### **1. 鍏崇郴鍨嬫暟鎹簱锛圫QL锛?*

| 鏁版嵁搴?        | 鐗圭偣                           |
| -------------- | ------------------------------ |
| **MySQL**      | 寮€婧愩€佺ぞ鍖烘椿璺冦€侀€傚悎涓皬鍨嬪簲鐢?|
| **PostgreSQL** | 鍔熻兘寮哄ぇ銆佹敮鎸?JSON銆丟IS       |
| **Oracle**     | 浼佷笟绾с€佺ǔ瀹氥€佹槀璐?            |
| **SQL Server** | 寰蒋鐢熸€併€乄indows 闆嗘垚濂?      |
| **SQLite**     | 宓屽叆寮忋€佸崟鏂囦欢銆佹棤鏈嶅姟鍣?      |

------

##### **2. NoSQL 鏁版嵁搴擄紙鎸夋暟鎹ā鍨嬪垎锛?*

| 绫诲瀷                    | 浠ｈ〃鏁版嵁搴?           | 鍏稿瀷鏁版嵁鏍煎紡           |
| ----------------------- | --------------------- | ---------------------- |
| **閿€硷紙Key-Value锛?*   | Redis, Memcached      | { "user:1001": "Tom" } |
| **鏂囨。锛圖ocument锛?*    | MongoDB, CouchDB      | JSON/BSON 鏂囨。         |
| **鍒楁棌锛圵ide Column锛?* | Cassandra, HBase      | 瓒呭ぇ瀹借〃锛屾寜鍒楀瓨鍌?    |
| **鍥撅紙Graph锛?*         | Neo4j, Amazon Neptune | 鑺傜偣 + 杈癸紙鍏崇郴锛?     |

------

#### 涓夈€佸吀鍨嬭繍鐢ㄥ満鏅?& 瀛樺偍鏁版嵁

| 鍦烘櫙                      | 鎺ㄨ崘鏁版嵁搴?          | 瀛樺偍鐨勬暟鎹?            | 鍘熷洜                 |
| ------------------------- | -------------------- | ---------------------- | -------------------- |
| **鐢ㄦ埛鐧诲綍銆佹潈闄愮鐞?*    | MySQL / PostgreSQL   | 鐢ㄦ埛琛ㄣ€佽鑹茶〃銆佹潈闄愯〃 | 闇€瑕佷簨鍔°€佸己涓€鑷存€?  |
| **鐢靛晢璁㈠崟銆佸簱瀛?*        | MySQL + Redis        | 璁㈠崟銆佸晢鍝併€佸簱瀛樺揩鐓?  | 浜嬪姟 + 楂樺苟鍙戣鍐?   |
| **鏃ュ織銆佹椂搴忔暟鎹?*        | Cassandra / InfluxDB | 鏃ュ織琛屻€佹椂闂存埑 + 鍊?   | 娴烽噺鍐欍€佷綆寤惰繜       |
| **缂撳瓨銆佷細璇濓紙Session锛?* | Redis                | Key-Value              | 瓒呴珮鎬ц兘銆佸唴瀛樺瓨鍌?  |
| **鍐呭绠＄悊銆佸崥瀹€丆MS**   | MongoDB              | 鏂囩珷 JSON銆佽瘎璁哄祵濂?   | 缁撴瀯鐏垫椿锛屾槗鎵╁睍瀛楁 |
| **绀句氦鍏崇郴銆佹帹鑽愮郴缁?*    | Neo4j                | 鐢ㄦ埛-鍏虫敞-鐢ㄦ埛         | 鍥鹃亶鍘嗘晥鐜囬珮         |
| **瀹炴椂鎺掕姒溿€佽鏁板櫒**    | Redis (Sorted Set)   | 鐢ㄦ埛ID + 鍒嗘暟          | ZINCRBY 鍘熷瓙鎿嶄綔     |
| **绉诲姩绔绾挎暟鎹?*        | SQLite               | 鏈湴鐢ㄦ埛璁剧疆銆佺紦瀛?    | 杞婚噺銆佸祵鍏ュ紡         |

------

#### 鍥涖€佷粈涔堟椂鍊欓€?SQL锛熶粈涔堟椂鍊欓€?NoSQL锛?

| 閫夋嫨 SQL锛堝叧绯诲瀷锛?                 | 閫夋嫨 NoSQL                         |
| ----------------------------------- | ---------------------------------- |
| 鏁版嵁缁撴瀯**鍥哄畾**锛堝璁㈠崟銆佺敤鎴凤級    | 鏁版嵁缁撴瀯**棰戠箒鍙樺寲**锛堝浜у搧灞炴€э級 |
| 闇€瑕?*澶嶆潅鏌ヨ**锛堝琛?JOIN銆佺粺璁★級 | 鏌ヨ绠€鍗曪紝涓昏**鎸?ID 鏌?*         |
| 寮鸿皟**浜嬪姟涓€鑷存€?*锛堣浆璐︿笉鑳介敊锛?   | 鎺ュ彈**鏈€缁堜竴鑷存€?*锛堟棩蹇楀彲绋嶆櫄锛?  |
| 鏁版嵁閲?*涓瓑**锛堢櫨涓囩骇锛?           | 鏁版嵁閲?*瓒呭ぇ**锛堢櫨浜跨骇锛?          |
| 涓氬姟閫昏緫澶嶆潅锛屾姤琛ㄥ                | 楂樺苟鍙戝啓锛岃妯″紡绠€鍗?              |

------

#### 浜斻€佷紭缂虹偣鎬荤粨

| 绫诲瀷      | 浼樼偣                                            | 缂虹偣                                       |
| --------- | ----------------------------------------------- | ------------------------------------------ |
| **SQL**   | - 鎴愮啛鏍囧噯锛圫QL锛?- 寮轰簨鍔★紙ACID锛?- 澶嶆潅鏌ヨ寮?| - 鎵╁睍鎬у樊锛堥毦姘村钩鎵╁睍锛?- Schema 鍙樻洿楹荤儲 |
| **NoSQL** | - 姘村钩鎵╁睍瀹规槗 - 鐏垫椿鎬ч珮 - 楂樺苟鍙戝啓            | - 鏃犵粺涓€鏌ヨ璇█ - 浜嬪姟寮?- 瀛︿範鎴愭湰楂?    |



瀹為檯鐢熶骇鏈€甯哥敤鐨勬槸锛?

```
鍚庣锛歁ySQL锛堜富鏁版嵁锛?+ Redis锛堢紦瀛橈級 + MongoDB锛堟棩蹇?鍐呭锛?+ Elasticsearch锛堟悳绱級
```





### 鈥滅粍缁囧舰寮忊€濇槸鍏抽敭锛?

1. **MySQL 鈫?SQL Server锛堝悓鏋勮縼绉伙級**锛?

   - **鏁版嵁缁勭粐绫讳技**锛氶兘鐢ㄨ〃瀛樺偍锛岃縼绉讳富瑕佹槸鈥滅炕璇戔€濆樊寮傘€?
   - **宸ュ叿鍙嬪ソ**锛歋SMA銆丏BConvert 绛夎嚜鍔ㄨ浆鎹?Schema 鍜屾暟鎹€?
   - **鐥涚偣**锛氬閿渶鎵嬪姩璋冩暣锛屾€ц兘璋冧紭銆?

   **绀轰緥**锛?

   sql

   ```
   -- MySQL
   CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50));
   
   -- SQL Server锛堢被浼硷紝浣嗙被鍨嬪井璋冿級
   CREATE TABLE users (id INT PRIMARY KEY, name NVARCHAR(50));
   ```

2. **MySQL 鈫?MongoDB锛堝紓鏋勮縼绉伙級**锛?

   - **缁勭粐褰㈠紡澶╁樊鍦板埆**锛氬琛?JOIN 鈫?鍗曟枃妗ｅ祵濂楋紱鍥哄畾 Schema 鈫?鍔ㄦ€佹枃妗ｃ€?
   - **闇€閲嶈璁?*锛氳鑼冨寲鏁版嵁鍙樷€滃弽瑙勮寖鍖栤€濓紙鍐椾綑瀛樺偍浠ュ姞閫熻锛夈€?
   - **搴旂敤閲嶆瀯**锛歋QL 鏌ヨ 鈫?Mongo 鏌ヨ锛汷RM锛堝 Hibernate锛?鈫?ODM锛堝 Mongoose锛夈€?



鍙兘闇€瑕佹槸鏁版嵁搴撹縼绉诲伐鍏风殑甯姪锛?

| 璺緞                   | 宸ュ叿                                                         | 鍏嶈垂锛?  |
| ---------------------- | ------------------------------------------------------------ | -------- |
| **MySQL 鈫?SQL Server** | SSMA銆丏BConvert銆丼kyvia                                      | 閮ㄥ垎鍏嶈垂 |
| **MySQL 鈫?MongoDB**    | MongoDB Relational Migrator銆乀apdata銆丳ython 鑴氭湰锛坧ymongo + mysql.connector锛?| 澶у鍏嶈垂 |







### JPA 鍜?H2



娣诲姞瀹屾垚鐩稿叧渚濊禆鍚庯紝閫氳繃鏇存敼閰嶇疆鏂囦欢锛?

```
spring.h2.console.enabled=true
```

鎴愬姛杩愯绋嬪簭鍚庯細

```
http://localhost:8080/h2-console
```

杈撳叆鍚庝綘鍙互寰楀埌锛?

![image-20251101173923808](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101173923808.png)

JDBC URL 灏辨槸褰撲綘娣诲姞H2渚濊禆杩愯鍚庣殑绋嬪簭锛堜竴鑸啓鍦ㄦ棩蹇楅噷闈級锛?

![image-20251101174104298](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101174104298.png)



![image-20251101174141929](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101174141929.png)

濉叆鍚庣洿鎺ラ摼鎺ュ嵆鍙娇鐢細

浣嗘槸杩欐牱姣忔閲嶆柊鍚敤鏈嶅姟鏄笉纭畾鐨刞url`鐨勶紝鍥犱负浠栨槸鍔ㄦ€佺敓鎴愩€?

杩欐椂瑕佸姞鍏ョ浉鍏抽厤缃細灏卞彲浠ヨ嚜瀹氫箟璺緞

```
spring.datasource.url=jdbc:h2:men:test
```

![image-20251101174617127](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101174617127.png)

鍙戠幇璺緞琚慨鏀?

鎴愬姛閾炬帴:

![image-20251101174810368](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101174810368.png)







### Understanding Entities in JPA

[08:22:41](https://www.youtube.com/watch?v=m559BxR30ls&t=30161s) 

鏍稿績闂锛氬疄浣撶┒绔熸槸浠€涔堬紵鍦↗PA涓嬶紝瀹炰綋浠ｈ〃浠€涔堬紵

**瀹炰綋 = 涓€鏉℃暟鎹簱璁板綍鐨?Java 瀵硅薄琛ㄧず**

鍏蜂綋璇存槑锛氬疄浣撲唬琛細

| 灞傞潰           | 浠ｈ〃鐨勫唴瀹?                                             |
| -------------- | ------------------------------------------------------- |
| **鏁版嵁搴撳眰闈?* | **涓€寮犺〃涓殑涓€琛岃褰曪紙Row锛?*                           |
| **Java 灞傞潰**  | **涓€涓甫鏈?`@Entity` 娉ㄨВ鐨勬櫘閫?Java 绫伙紙POJO锛夌殑瀹炰緥** |
| **ORM 鏄犲皠**   | **瀵硅薄 鈫?鍏崇郴琛?* 鐨勬ˉ姊?                               |



#### JPA鐨勭敓鍛藉懆鏈燂細

JPA 浼氳窡韪疄浣撶殑鐘舵€侊紝鍐冲畾濡備綍涓庢暟鎹簱浜や簰锛?

| 鐘舵€?                      | 鍚箟                          | 璇存槑                                       |
| -------------------------- | ----------------------------- | ------------------------------------------ |
| **New锛堟柊寤猴級**            | 瀵硅薄鍒?new 鍑烘潵锛岃繕娌℃寔涔呭寲   | entityManager.persist(user) 鍚庢墠杩涘叆鎸佷箙鍖?|
| **Managed锛堟墭绠?鎸佷箙鍖栵級** | 宸茶 EntityManager 绠＄悊       | 淇敼灞炴€т細鑷姩鍚屾鍒?DB锛堣剰妫€鏌ワ級          |
| **Detached锛堟父绂伙級**       | 鏇剧粡鎸佷箙鍖栬繃锛屼絾褰撳墠浼氳瘽缁撴潫  | 闇€瑕?merge() 閲嶆柊鍏宠仈                      |
| **Removed锛堝凡鍒犻櫎锛?*      | 璋冪敤浜?remove()锛岀瓑寰呮彁浜ゅ垹闄?| 浜嬪姟鎻愪氦鏃舵墽琛?DELETE                      |



#### JPA鐨勪娇鐢ㄨ姹傦細

| 瑕佹眰                                | 璇存槑                             |
| ----------------------------------- | -------------------------------- |
| 1. 浣跨敤 `@Entity` 娉ㄨВ              | 鏍囪涓哄疄浣?                      |
| 2. 鏈?`@Id` 瀛楁                    | 蹇呴』鏈変富閿?                      |
| 3. 鏈夋棤鍙傛瀯閫犲櫒                     | 鍙嶅皠鍒涘缓瀹炰緥锛坄protected` 涔熻锛?|
| 4. 涓嶆槸 `final` 绫?                 | 鍚﹀垯鏃犳硶浠ｇ悊锛堝欢杩熷姞杞介渶瑕侊級     |
| 5. 瀛楁鏄?`private` + getter/setter | JPA 閫氳繃鏂规硶璁块棶                 |



瀹為檯妗堜緥锛?

![image-20251101181312470](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101181312470.png)

杩欓噷鎴戦€氳繃涓や釜娉ㄨВ`@Entity`鍜宍@Id`,灏哷categoryId`杩欎釜绉佹湁鍙橀噺琚爣璁版垚鏁版嵁搴撶殑涓婚敭锛屼綔涓哄敮涓€鏍囪瘑

姝ゆ椂鎴戜滑鍐嶆璁块棶H2

![image-20251101181539829](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101181539829.png)

#### 鑷姩寤鸿〃鐨勫師鍥狅細

| 鍘熷洜                                             | 璇存槑                                                |
| ------------------------------------------------ | --------------------------------------------------- |
| 1. 浣犵敤鐨勬槸 **H2 鍐呭瓨鏁版嵁搴?*锛堝父瑙佷簬娴嬭瘯/寮€鍙戯級 | 榛樿閰嶇疆浼氬紑鍚嚜鍔ㄥ缓琛?                             |
| 2. 浣?**娌℃湁鏄惧紡璁剧疆 `ddl-auto=none`**           | Spring Boot **榛樿鍊兼槸 `create-drop`**锛圚2 鐗逛緥锛侊級 |
| 3. 浣犲姞浜?`@Entity` 鍜?`@Id`                     | 婊¤冻 JPA 瀹炰綋鏈€浣庤姹傦紝Hibernate 灏辫兘璇嗗埆           |

涔熷彲浠ラ€氳繃鐩存帴鍦ㄥ疄浣揱@Entity`娉ㄨВ鍚庢坊鍔狅紝瀹炵幇淇敼琛ㄥ悕锛?

<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101182216613.png" alt="image-20251101182216613" style="zoom:50%;" />



###  Behind the Scenes & Additional Properties

[08:29:23](https://www.youtube.com/watch?v=m559BxR30ls&t=30563s)

#### 濡傛灉杩涜涓€浜涜涓洪厤缃細

鎴戜滑灏卞彲浠ョ湅鍒癭JPA`鑷姩鏁版嵁搴撳缓琛ㄧ瓑鎿嶄綔

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=none
```



**spring.jpa.show-sql=true 杩愯鍚庯紝瀵瑰師鏈郴缁熻涓虹殑** **鍞竴鏀瑰彉** **鏄細**

> **鍦ㄦ帶鍒跺彴锛堟垨鏃ュ織鏂囦欢锛夐澶栨墦鍗板嚭 Hibernate 鍙戝嚭鐨勬墍鏈?SQL 璇彞锛堜笉鍚弬鏁板€硷級**銆?

<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101183324067.png" alt="image-20251101183324067" style="zoom:50%;" />

**spring.jpa.properties.hibernate.format_sql=true**

> **灏?show-sql=true 鎵撳嵃鍑烘潵鐨?SQL 璇彞杩涜鈥滅編鍖栨牸寮忓寲鈥濓紙鎹㈣ + 缂╄繘锛夛紝璁╁鏉?SQL 鏇存槗璇汇€?*



**spring.jpa.hibernate.ddl-auto=none**

| 閰嶇疆            | 浠峰€?                                       |
| --------------- | ------------------------------------------- |
| show-sql=true   | **寮€鍙戣皟璇曠鍣?*锛氫竴鐪肩湅绌?JPA 鍙戜簡浠€涔?SQL |
| format_sql=true | **鎻愬崌鍙鎬?*锛氬鏉傛煡璇笉涔?               |
| ddl-auto=none   | **鐢熶骇瀹夊叏閿?*锛氭潨缁濇剰澶栨敼琛?               |

> **涓€鍙ヨ瘽鎬荤粨**锛?**鈥滃彧鐪?SQL锛屼笉鏀硅〃鈥?鈥斺€?寮€鍙戦€忔槑锛岀敓浜у畨鍏ㄣ€?*

<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101182700735.png" alt="image-20251101182700735" style="zoom:50%;" />

涓€鑸彇鍊艰〃:

| 鍙栧€?         | 浣滅敤                               | 鏄惁寤鸿〃/鏀硅〃  | 閫傜敤鍦烘櫙              |
| ------------- | ---------------------------------- | -------------- | --------------------- |
| `create`      | **鍒犳棫琛?鈫?閲嶅缓琛?*                | 鏄紙鍏ㄥ垹鍏ㄥ缓锛?| 鍗曞厓娴嬭瘯銆佸揩閫熷師鍨?   |
| `create-drop` | **鍚姩寤鸿〃锛屽叧闂垹琛?*             | 鏄紙涓存椂琛級   | 闆嗘垚娴嬭瘯锛圚2 鍐呭瓨搴擄級 |
| `update`      | **鏍规嵁瀹炰綋鍚屾琛ㄧ粨鏋勶紝涓嶅垹鏁版嵁**   | 鏄紙澧為噺鏀癸級   | 鏈湴寮€鍙?             |
| `validate`    | **鏍￠獙瀹炰綋涓庤〃缁撴瀯涓€鑷存€э紝涓嶆敼琛?* | 鍚?            | 棰勭敓浜ч獙璇?           |
| `none`        | **浠€涔堥兘涓嶅仛**                     | 鍚?            | 鐢熶骇鐜锛堥粯璁わ級      |

涓婅堪鎿嶄綔鍧囧睘浜庯細DDL



#### DDL鍜孌ML鐨勫尯鍒細

| 缁村害                             | `spring.jpa.hibernate.ddl-auto`               | **鏁版嵁搴撲簨鍔★紙@Transactional锛?*                 |
| -------------------------------- | --------------------------------------------- | ------------------------------------------------ |
| **鎿嶄綔绫诲瀷**                     | **DDL**锛堝畾涔夎瑷€锛?`CREATE`, `ALTER`, `DROP` | **DML**锛堟搷浣滆瑷€锛?`INSERT`, `UPDATE`, `DELETE` |
| **浣滅敤瀵硅薄**                     | **琛ㄧ粨鏋?*锛圫chema锛?                         | **琛ㄦ暟鎹?*锛圖ata锛?                              |
| **鏄惁鍙洖婊?*                   | **涓嶅彲鍥炴粴**锛圖DL 鑷姩鎻愪氦锛?                 | **鍙洖婊?*锛堜簨鍔″け璐ュ彲鎾ら攢锛?                    |
| **鎵ц鏃舵満**                     | **搴旂敤鍚姩鏃?*锛堜竴娆℃€э級                      | **鏂规硶杩愯鏃?*锛堟瘡娆¤皟鐢級                       |
| **鏄惁鍙?`@Transactional` 鎺у埗** | **涓嶅彈**                                      | **鍙?*                                           |
| **鏄惁褰卞搷涓氬姟閫昏緫**             | **涓嶅奖鍝?*锛堝彧绠＄粨鏋勶級                        | **鐩存帴褰卞搷**锛堝鍒犳敼鏌ワ級                         |





### Generation Types For Identity

[08:38:26](https://www.youtube.com/watch?v=m559BxR30ls&t=31106s) 

```java
@GeneratedValue(strategy = GenerationType.)
```

| 绛栫暐 (GenerationType) | 璇存槑                                                         | 甯歌鏁版嵁搴撴敮鎸?                                              | 浼樼偣                                                         | 娉ㄦ剰浜嬮」锛忛€傜敤鍦烘櫙                                           |
| --------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `AUTO`                | 璁?JPA 鎻愪緵鑰咃紙濡?Hibernate锛夎嚜鍔ㄩ€夋嫨鏈€鍚堥€傜殑涓婚敭鐢熸垚绛栫暐銆?([Baeldung on Kotlin](https://www.baeldung.com/hibernate-identifiers?utm_source=chatgpt.com)) | 鍑犱箮鎵€鏈夛紝浣嗗叿浣撹涓轰緷璧栨暟鎹簱 + JPA 瀹炵幇銆?                 | 閰嶇疆绠€鍗曪紝閫傚悎鏁版嵁搴撳垏鎹㈡垨涓嶆兂鍏冲績缁嗚妭鐨勫満鏅€?              | 鍙兘閫犳垚鍦ㄤ笉鍚屾暟鎹簱涓嬭涓轰笉涓€鑷达紙渚嬪浠?MySQL 杞?PostgreSQL 鏃剁瓥鐣ュ彉浜嗭級銆?([Stackademic](https://stackademic.com/blog/avoid-using-generationtype-auto-strategy-for-id-generation-in-spring-boot?utm_source=chatgpt.com)) |
| `IDENTITY`            | 浣跨敤鏁版嵁搴撶殑鈥滆嚜鍔ㄥ闀匡紡鑷鍒椻€濓紙濡?MySQL 鐨?AUTO_INCREMENT銆丼QL Server 鐨?IDENTITY锛夋潵鐢熸垚涓婚敭銆?([Stack Overflow](https://stackoverflow.com/questions/33096466/generationtype-auto-vs-generationtype-identity-in-hibernate?utm_source=chatgpt.com)) | MySQL銆丼QL Server銆丳ostgreSQL锛堥儴鍒嗘敮鎸侊級銆丱racle 12c 浠ュ悗鏀寔 IDENTITY 鍒椼€?([tutorialspoint.com](https://www.tutorialspoint.com/difference-between-sequence-and-identity-in-hibernate?utm_source=chatgpt.com)) | 閰嶇疆鏈€绠€鍗曪紱鎻掑叆鎿嶄綔鏁版嵁搴撳嵆鍙敓鎴愪富閿€?                    | 涓嶆敮鎸佹壒閲忔彃鍏ヤ紭鍖栵紙batch insert锛夛紡JPA 瀵瑰欢杩熻幏鍙?ID 闄愬埗銆傝嫢澶ч噺鎻掑叆銆侀珮鎬ц兘鍦烘櫙鍙兘涓嶆槸鏈€浣炽€?([vladmihalcea.com](https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/?utm_source=chatgpt.com)) |
| `SEQUENCE`            | 浣跨敤鏁版嵁搴撶殑鈥滃簭鍒楀璞♀€濓紙Sequence锛夌敓鎴愪富閿€硷紝渚嬪 Oracle銆丳ostgreSQL 鐨?sequence銆?([Stack Overflow](https://stackoverflow.com/questions/8955074/generatedvaluestrategy-identity-vs-generatedvaluestrategy-sequence?utm_source=chatgpt.com)) | Oracle銆丳ostgreSQL銆丏B2 绛夋敮鎸佸簭鍒椼€?MySQL 鏅€氱増鏈笉鏀寔鍘熺敓搴忓垪銆?([Stack Overflow](https://stackoverflow.com/questions/33096466/generationtype-auto-vs-generationtype-identity-in-hibernate?utm_source=chatgpt.com)) | 鎬ц兘杈冨ソ锛屾敮鎸佹壒閲忔彃鍏ャ€丣PA 浼樺寲锛涚伒娲诲彲閰嶇疆銆?([Thorben Janssen](https://thorben-janssen.com/jpa-generate-primary-keys/?utm_source=chatgpt.com)) | 鑻ユ暟鎹簱**涓嶆敮鎸?*搴忓垪锛堝鏃х増 MySQL锛夐渶棰濆閰嶇疆鎴栧洖閫€绛栫暐銆傞渶瀹氫箟 `@SequenceGenerator` 绛夈€?|
| `TABLE`               | 浣跨敤涓€涓笓闂ㄧ殑鏁版嵁搴撹〃鏉ユā鎷熶富閿敓鎴愶紙鍗宠嚜宸辩淮鎶や竴涓€滆鏁板櫒鈥濊〃锛夈€?([vladmihalcea.com](https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/?utm_source=chatgpt.com)) | 鎵€鏈夋暟鎹簱閮借兘鏀寔锛堝洜涓哄彧鏄竴涓櫘閫氳〃锛夛紝浣嗘€ц兘杈冨樊銆?      | 鏈€鍏峰彲绉绘鎬э紙鍦ㄦ墍鏈夋暟鎹簱涓婇兘鑳藉伐浣滐級銆?                    | 鎬ц兘鏈€寮憋細姣忔鐢熸垚 ID 閮藉彲鑳借闂鏁板櫒琛ㄣ€佸苟鍙戞€ц兘宸€備竴鑸彧鏈夊湪鍏朵粬绛栫暐涓嶅彲鐢ㄦ椂鎵嶈€冭檻銆?([vladmihalcea.com](https://vladmihalcea.com/why-you-should-never-use-the-table-identifier-generator-with-jpa-and-hibernate/?utm_source=chatgpt.com)) |

渚嬪`SEQUENCE`鍜宍TABLE`

![image-20251101185610948](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101185610948.png)

![image-20251101185614258](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101185614258.png)



#### `@GeneratedValue` 鐨勪娇鐢ㄥ師鍥狅細

浣跨敤 `@GeneratedValue` 鐨勪富瑕佸師鍥犲寘鎷互涓嬪嚑鐐癸細

1. **绠€鍖栦富閿鐞?*
    浣跨敤 `@GeneratedValue` 鍙 ORM锛堝 Hibernate锛廍clipseLink锛夋鏋惰嚜鍔ㄤ负瀹炰綋绫荤殑涓婚敭瀛楁鐢熸垚鍞竴鍊硷紝鑰屼笉闇€瑕佸紑鍙戜汉鍛樻墜鍔ㄨ缃垨缁存姢銆傛瘮濡傚綋浣犲姞涓€涓柊瀹炰綋骞朵繚瀛樻椂锛屾鏋朵細甯綘浜х敓 `id`銆?[GeeksforGeeks+2Home+2](https://www.geeksforgeeks.org/advance-java/hibernate-generatedvalue-annotation-in-jpa/?utm_source=chatgpt.com)
2. **淇濊瘉鍞竴鎬т笌涓€鑷存€?*
    涓婚敭蹇呴』鍞竴锛屽苟涓旈€氬父鏄郴缁熷唴閮ㄤ娇鐢ㄧ殑鈥滄妧鏈富閿€濓紙鑰屼笉鏄潵鑷笟鍔￠€昏緫鐨勨€滃ぉ鐒堕敭鈥濓級銆傞€氳繃 `@GeneratedValue`锛屼綘鍙互浣跨敤鏁版嵁搴撴垨 ORM 鎻愪緵鐨勬満鍒剁‘淇濇瘡鏉¤褰曢兘鏈変竴涓敮涓€涓旈珮鏁堢殑鏍囪瘑銆?[Thorben Janssen+1](https://thorben-janssen.com/jpa-generate-primary-keys/?utm_source=chatgpt.com)
3. **鏁版嵁搴撴€ц兘涓庢墿灞曡€冭檻**
    鍚堥€傜殑鐢熸垚绛栫暐锛坄IDENTITY`銆乣SEQUENCE`銆乣TABLE` 绛夛級鍙埄鐢ㄦ暟鎹簱鍐呭缓鏈哄埗锛堝鑷鍒椼€佸簭鍒楋級鏉ョ敓鎴愪富閿紝浠庤€屾彁楂樻彃鍏ユ晥鐜囥€佸噺灏戝啿绐併€傛瘮濡備娇鐢?`GenerationType.SEQUENCE` 鏃讹紝鍙互棰勫垎閰嶄竴鎵?ID锛屼粠鑰屽噺灏戞瘡娆℃彃鍏ユ椂璁块棶鏁版嵁搴撳簭鍒楃殑寮€閿€銆?[Baeldung on Kotlin+1](https://www.baeldung.com/hibernate-identifiers?utm_source=chatgpt.com)
4. **璁╁疄浣撴槧灏勯€昏緫涓庢暟鎹簱缁嗚妭瑙ｈ€?*
    閫氳繃娉ㄨВ鏂瑰紡鎸囧畾涓婚敭鐢熸垚绛栫暐锛屽疄浣撶被涓嶉渶瑕佺‖缂栫爜鍏蜂綋鐨勮嚜澧為€昏緫鎴栦笟鍔＄紪鍙风敓鎴愰€昏緫锛屼粠鑰岃浠ｇ爜鏇存竻鏅般€佹槗缁存姢銆佹暟鎹簱杩佺Щ锛堟崲涓€绉?DB锛夋椂璋冩暣鏇村鏄撱€?[medium.com+1](https://medium.com/%40gaddamnaveen192/complete-guide-to-jpa-id-generation-auto-identity-sequence-and-table-3044891e88af?utm_source=chatgpt.com)





### JPA Repositories

姒傚康鍥撅細

![image-20251101211719298](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101211719298.png)

![image-20251101202704848](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251101202704848.png)

鏂板缓涓€涓?*璐熻矗鏁版嵁璁块棶锛堟搷浣滄暟鎹簱锛塦CategoryRepository`**锛岄€氬父缁ф壙 Spring Data JPA 鐨勬帴鍙?

```java
// JapRepository <瀹炰綋锛?涓婚敭鐨勫瓧绗︾被鍨?
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
```

| 鐗规€?        | `CrudRepository`                      | `JpaRepository`                                         |
| ------------ | ------------------------------------- | ------------------------------------------------------- |
| 缁ф壙鍏崇郴     | 鍩虹鎺ュ彛                              | 缁ф壙 `CrudRepository` + `PagingAndSortingRepository`    |
| 鏍稿績鏂规硶     | `save`, `findById`, `delete`, `count` | **鍏ㄩ儴 + 棰濆楂樼骇鍔熻兘**                                 |
| 棰濆鏂规硶     | 鏃?                                   | `findAll()`, `deleteAll()`, `flush()`, `saveAndFlush()` |
| 鍒嗛〉鎺掑簭     | 鏃?                                   | 鏀寔 `Pageable`                                         |
| **鏄惁鎺ㄨ崘** | **浠呯敤浜庢瀬绠€鍦烘櫙**                    | **99% 椤圭洰鐢ㄨ繖涓?*                                      |



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
//        // 浣跨敤 Stream 鏌ユ壘 ID 鍖归厤鐨勫垎绫伙紝杩斿洖 Optional 鍖呰鐨勭粨鏋?
//        Optional<Category> optionalCategory = categories.stream()
//                .filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst();
//
//        // 鍒ゆ柇鏌ヨ缁撴灉鏄惁涓虹┖
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

        // 鍒ゆ柇鏌ヨ缁撴灉鏄惁涓虹┖
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



#### 涓轰粈涔?**POST锛堟柊澧烇級** 浼氬彉鎴?**UPDATE锛堜慨鏀癸級**锛?

------

鏍稿績鍘熷洜锛?*save() 鏂规硶鐨勮涓哄彇鍐充簬 id 鏄惁涓?null**

闂鍘熷洜锛?

| 鍘熷洜                                       | 瑙ｉ噴     |
| ------------------------------------------ | -------- |
| **1. `save()` 鐪?`id` 鍐冲畾 INSERT/UPDATE** | JPA 瑙勮寖 |
| **2. 浣犲湪 POST 閲屼紶浜?`categoryId`**       | 浜轰负閿欒 |
| **3. 浠ヤ负鈥滄柊澧炶浼?ID鈥?*                   | 璇В     |
| **4. 澶嶅埗浜?GET/PUT 鐨勮姹備綋**             | 鎿嶄綔澶辫 |

杩囩▼璇存槑锛?

| 姝ラ | 璇存槑                                                         |
| ---- | ------------------------------------------------------------ |
| 1    | 浣犲彂 POST 璇锋眰锛屽甫浜?`{"categoryId": 1, "categoryName": "Phones"}` |
| 2    | Spring 鍙嶅簭鍒楀寲鎴?`Category` 瀵硅薄锛宍categoryId = 1`          |
| 3    | `createCategory(category)` 璋冪敤 `save(category)`             |
| 4    | Hibernate 鐪嬪埌 `id = 1` 鈫?璁や负杩欐槸涓€涓?**鈥滃凡瀛樺湪鐨勫疄浣撯€?*    |
| 5    | 鍘绘暟鎹簱鏌?`WHERE category_id = 1`                           |
| 6    | 濡傛灉鏌ュ埌 鈫?**鎵ц UPDATE** 濡傛灉鏌ヤ笉鍒?鈫?**鎵ц INSERT**锛堜絾 ID 浠嶆槸浣犱紶鐨?`1`锛?|
| 7    | 浣犱互涓烘槸鈥滄柊澧炩€濓紝鍏跺疄鍙兘鏄?**淇敼浜嗗埆浜?* 鎴?**璺宠繃浜嗚嚜澧?*  |

鏍稿績璁捐锛?

| 濂藉          | 璇存槑                                          |
| ------------- | --------------------------------------------- |
| **缁熶竴鎺ュ彛**  | 涓€涓?`save()` 鍚屾椂鏀寔鏂板鍜屼慨鏀?             |
| **绠€鍖栦唬鐮?*  | 涓嶉渶瑕佸啓 `if (id == null) insert else update` |
| **绗﹀悎 REST** | POST = 鍒涘缓鏂拌祫婧愶紝PUT = 鏇存柊璧勬簮             |

姣斿柣锛?

| 鍦烘櫙                                    | 瀵瑰簲                             |
| --------------------------------------- | -------------------------------- |
| 浣犲幓楗簵鐐归                            | POST                             |
| 浣犺锛氣€滄垜瑕佷竴浠藉淇濋浮涓佲€?               | `{ "categoryName": "瀹繚楦′竵" }` |
| 鏈嶅姟鍛樹笉浼氶棶锛氣€滀綘瑕佺鍑犳锛熲€?           | 涓嶉渶瑕?`categoryId`              |
| 浣嗗鏋滀綘璇达細鈥滄垜瑕?**3鍙锋** 鐨勫淇濋浮涓佲€?| 甯︿簡 `categoryId`                |
| 鏈嶅姟鍛樹細浠ヤ负浣犺 **鏀瑰崟**               | 鈫?鎵ц UPDATE                    |



###  Using Optionals in Services [Optimizing]

[09:00:21](https://www.youtube.com/watch?v=m559BxR30ls&t=32421s)

```java
    @Override
    public Category updateCategory(Category category, Long categoryId) {

        // 鈥滄煡鎵?+ 鍒ゆ柇鏄惁瀛樺湪鈥?鐨勬爣鍑嗘ā寮忥紝鏍规嵁categoryId 鍘昏皟鐢╟ategoryRepository鐨凧PA data鐨刦indById
        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        // 2. 鑻ヤ笉瀛樺湪锛屾姏鍑?404
        Category savedCategory = savedCategoryOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));

        // 鎶婂墠绔紶杩涙潵鐨?id 璁惧洖鍘伙紙闃叉鍓嶇婕忎紶鎴栬绡℃敼锛?
        category.setCategoryId(categoryId);

        // JPA 浼氭牴鎹富閿垽鏂槸 UPDATE 杩樻槸 INSERT
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }
```



鏃х殑鍐欐硶

```java
    @Override
    public Category updateCategory(Category category, Long categoryId){

        List<Category> categories = categoryRepository.findAll();

        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();

        // 鍒ゆ柇鏌ヨ缁撴灉鏄惁涓虹┖
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



| 缁村害               | 褰撳墠鍐欐硶锛坄findById` + `save`锛?                             | 鏃у啓娉曪紙`findAll` + 鎵嬪姩 set锛?                       |
| ------------------ | ------------------------------------------------------------ | ----------------------------------------------------- |
| **鏌ヨ鏁堢巼**       | **O(1)**锛氱洿鎺ヨ蛋鏁版嵁搴撲富閿储寮?                              | **O(n)**锛氬叏琛ㄦ壂鎻忥紝鏁版嵁閲忓ぇ鏃舵瀬鎱?                   |
| **鍐呭瓨鍗犵敤**       | 鍙姞杞?**1 鏉¤褰?*                                          | 鍔犺浇 **鍏ㄩ儴璁板綍**锛屽鏄?OOM                           |
| **SQL 璇彞**       | `SELECT ... WHERE id = ?` 鈫?`UPDATE ... SET ... WHERE id = ?` | `SELECT * FROM category` 鈫?鍙兘涓€娆?`UPDATE`          |
| **瀛楁鏇存柊鐏垫椿鎬?* | **鍏ㄥ瓧娈佃鐩?*锛堝彧瑕佸墠绔紶浜嗛潪 null 灏辨洿鏂帮級 鑻ユ兂閮ㄥ垎鏇存柊闇€棰濆澶勭悊 | **纭紪鐮佸彧鏇存柊 `categoryName`**锛屾兂鍔犳柊瀛楁蹇呴』鏀逛唬鐮?|
| **浠ｇ爜绠€娲佸害**     | **鏋佺畝**锛孞PA 鑷姩澶勭悊 merge                                 | 鍐楅暱锛屾墜鍔ㄩ亶鍘?+ 鎵嬪姩澶嶅埗瀛楁                         |
| **浜嬪姟/骞跺彂瀹夊叏**  | 鐩存帴鍦ㄥ悓涓€涓簨鍔″唴瀹屾垚鏌ユ壘+鏇存柊                              | 鍚屼笂锛堜絾鍥犲叏琛ㄥ姞杞藉彲鑳藉紩鍙戞洿澶ч攣绔炰簤锛?               |
| **鎵╁睍鎬?*         | 鏄撲簬閰嶅悎 `BeanUtils.copyProperties(category, savedCategory, "categoryId")` 瀹炵幇 **閮ㄥ垎鏇存柊** | 姣忔柊澧炲瓧娈甸兘瑕佹敼 `setXxx`                             |
| **鏄惁鎺ㄨ崘**       | **寮虹儓鎺ㄨ崘**锛堢敓浜х幆澧冩爣鍑嗗仛娉曪級                             | **涓嶆帹鑽?*锛屼粎閫傚悎鏋佸皬琛ㄦ垨瀛︿範婕旂ず                    |



##### update涓汉鐨勭枒闂細

**涓轰粈涔坄JPA`鍦ㄨ璁℃椂鍊欎笉鐩存帴灏哷update`鍜宍insert`鍒嗗紑锛?*

鍙互鐪嬬湅浠栫殑搴曞眰锛?

```java
if (entity.isNew()) {
    entityManager.persist(entity);   // INSERT
} else {
    entityManager.merge(entity);     // UPDATE (鎴?INSERT 濡傛灉 ID 涓嶅瓨鍦?
}
```

- isNew() 榛樿鍒ゆ柇锛?*@Id 鏄惁涓?null**锛堝彲閫氳繃 @Entity 鐨?org.hibernate.annotations.Entity 鑷畾涔夛級

- merge()

   浼氾細

  1. SELECT 鏌ュ嚭鏁版嵁搴撲腑鐨勫疄浣?
  2. 鎶婁紶杩涙潵瀵硅薄鐨勫瓧娈?**澶嶅埗杩囧幓**
  3. UPDATE 鍥炲幓

> 鎵€浠?save() **涓€瀹氫細瑙﹀彂涓€娆?SELECT**锛堥櫎闈炲湪鍚屼竴涓?EntityManager 浼氳瘽涓凡鍔犺浇锛?



**鎬荤粨涓轰粈涔堜笉鍒嗗紑鍐?*

| 浼樼偣               | 璇存槑                                      |
| ------------------ | ----------------------------------------- |
| **缁熶竴鎺ュ彛**       | 璋冪敤鑰呭彧鍏冲績鈥滀繚瀛樷€濓紝涓嶅叧蹇冩柊鏃?           |
| **鍑忓皯鏍锋澘浠ｇ爜**   | 鏃犻渶 `if (id == null) insert else update` |
| **绗﹀悎 JPA 瑙勮寖**  | 鍩轰簬 `persist` / `merge` 璁捐             |
| **鏄撲簬娴嬭瘯鍜岀淮鎶?* | 鎵€鏈?CRUD 璧板悓涓€濂楁祦绋?                   |



鎴栬€呮洿绠€娲佺殑鍐欐硶

```java
  @Override
    public Category updateCategory(Category category, Long categoryId) {
        
		Category savedCategory = CategoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        
         // 纭繚Id浼犲洖鏉ワ紝淇濇姢鏁版嵁
         savedCategory.setCategoryId(categoryId);
         // 璁㎎PA鑷鍒ゆ柇鏄疷pdate杩樻槸Insert
         savedCategory = CategoryRepository(category);
         return savedCategory;
    }

```



鍐嶆瀹屽杽涓婦elete

```java
    @Override
    public String deleteCategory(Long categoryId) {

        // 鏌ユ壘+鍒ゆ柇 鍩烘湰妯″紡
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        // 濡傛灉涓嶅瓨鍦紝杩斿洖404
        Category Category = optionalCategory
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(Category);
        return " Category with categoryId: " + categoryId + " deleted successfully";
    }
```



鏃х殑锛?

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



鎴栬€呯畝鍗曟ā寮忥細

```java
  @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
        return "Category deleted" + categoryId + "successfully!";

    }
```



##### 鍒犻櫎鐨勯棶棰橈紵

> **鈥滀负浠€涔?deleteCategory 鍙渶瑕佷紶 categoryId锛岃€?updateCategory 鍗磋鍦?category 瀵硅薄涓?setCategoryId(id)锛熲€?*

绛旀灏卞湪浜庯細**delete 鏄€滄寜 ID 鍒犻櫎鈥濓紝鑰?update 鏄€滄寜瀹炰綋淇濆瓨鈥?* 鈥斺€?涓よ€呯殑 **JPA 鏈哄埗瀹屽叏涓嶅悓**銆?



##### 鎬荤粨锛?

| 椤圭洰             | 鍒犻櫎                      | 鏇存柊                              |
| ---------------- | ------------------------- | --------------------------------- |
| 鏄惁闇€瑕佸疄浣?    | 涓嶉渶瑕?                   | 蹇呴』                              |
| 鏄惁闇€瑕?`setId` | 涓嶉渶瑕?                   | **蹇呴』**                          |
| 鎺ㄨ崘鏂规硶         | `deleteById(id)`          | `save(entity)` + `setId(id)`      |
| 搴曞眰 SQL         | `DELETE ... WHERE id = ?` | `UPDATE ... SET ... WHERE id = ?` |
| 鏄惁闇€瑕佸厛鏌?    | `existsById`锛堝彲閫夛級      | `findById`锛堝繀椤伙紝闃茶鐩栵級        |



### Experimenting Beyond

[09:08:28](https://www.youtube.com/watch?v=m559BxR30ls&t=32908s) 

瀹為獙灏忕粨锛?

瀹為獙涓€锛?

濡傛灉鎴戝湪鎴戠殑`model`瀹炰綋绫讳腑鍒犻櫎浜哷getter` ` setter`鏂规硶浼氬彂鐢熶粈涔堬紵



![image-20251102165009259](鏋勫缓鐢靛晢Spring-Boot.assets/image-20251102165009259.png)

杩欏彲浠ュ彂鐜帮紝灏唍ame鐨刞getter setter`鏂规硶鍙栨秷锛屼笉浼氭姤閿欙紝浣嗘槸褰撲綘鎻掑叆鏁版嵁鐨勬椂鍊欙紝浼氭樉绀篳NULL`



瀹為獙浜岋細

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

鍠勭敤hibernate锛屽綋鎴戜滑鎵ц鎻掑叆鎿嶄綔鎴栬€呭叾浠朇RUD鐨勬搷浣滄椂鍊欙細

鎴戜滑鍙互鍐嶇粓绔腑鐪嬪埌`JPA`鍒板簳鎵ц浜嗕粈涔堟牱鐨刞sql`璇彞锛宖or example:

<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251102165311542.png" alt="image-20251102165311542" style="zoom:50%;" />



瀹為獙涓夛細

<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251102165635803.png" alt="image-20251102165635803" style="zoom:50%;" />



<img src="鏋勫缓鐢靛晢Spring-Boot.assets/image-20251102165640486.png" alt="image-20251102165640486" style="zoom:50%;" />



杩欎簺閮借〃鏄庯細閫氳繃缁ф壙JPA,涓€浜涚被鍨嬫槸鍙嚜瀹氫箟鐨勶細渚嬪ID,

杩樻湁妗堜緥涓殑`category`宸茬粡瀹氫箟鎴愪负涓€涓疄浣撶被銆?







Lombok

鏃х殑浠ｇ爜锛?

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



鍔犲叆Lombok娉ㄨВ鍚?

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



鏉ヨ嚜Gork鐨勬€荤粨锛?

浣犵敤鐨勬槸 **Lombok**锛屼竴涓兘澶у箙鍑忓皯 Java 鏍锋澘浠ｇ爜鐨勫簱銆備笅闈㈡垜 **閫愯瑙ｉ噴浣犱唬鐮佷腑姣忎釜 Lombok 娉ㄨВ鐨勪綔鐢?*锛屽苟璇存槑 **瀹冧滑鑳屽悗鐢熸垚浜嗗摢浜涗唬鐮?*銆?

------

##### 瀹炰綋绫?

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

##### 涓€銆丂Entity(name = "CATEGORIES")

| 娉ㄨВ                | 浣滅敤                                                       |
| ------------------- | ---------------------------------------------------------- |
| @Entity             | 鏍囪杩欐槸涓€涓?**JPA 瀹炰綋绫?*锛屼細琚?Hibernate 鏄犲皠鍒版暟鎹簱琛?|
| name = "CATEGORIES" | 鏄惧紡鎸囧畾 **閫昏緫琛ㄥ悕** 涓?CATEGORIES                        |

> **榛樿琛屼负**锛氬鏋滀笉鍐?name锛岃〃鍚?= 绫诲悕 = Category 鈫?鏁版嵁搴撹〃鍚?category **浣犲啓 name = "CATEGORIES"** 鈫?鏁版嵁搴撹〃鍚嶅己鍒朵负 **CATEGORIES**锛堝ぇ鍐欙級

------

##### 浜屻€丂Data

> **鏈€寮哄ぇ銆佹渶甯哥敤鐨?Lombok 娉ㄨВ锛?*

###### @Data = 浠ヤ笅 6 涓敞瑙ｇ殑**鍚堥泦**锛?

| 娉ㄨВ                     | 鐢熸垚鐨勪唬鐮?                             |
| ------------------------ | --------------------------------------- |
| @Getter                  | 鎵€鏈夊瓧娈佃嚜鍔ㄧ敓鎴?getXxx()               |
| @Setter                  | 鎵€鏈夊瓧娈佃嚜鍔ㄧ敓鎴?setXxx()               |
| @ToString                | 鐢熸垚 toString()                         |
| @EqualsAndHashCode       | 鐢熸垚 equals() 鍜?hashCode()             |
| @RequiredArgsConstructor | 涓?**final 鎴?@NonNull 瀛楁**鐢熸垚鏋勯€犲櫒 |

> **娉ㄦ剰**锛氫綘娌℃湁 final 瀛楁锛屾墍浠?@RequiredArgsConstructor **涓嶄細鐢熸垚浠讳綍鏋勯€犲櫒**

------

###### @Data 瀹為檯涓轰綘鐢熸垚浜嗭細

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

##### 涓夈€丂NoArgsConstructor

###### 鐢熸垚 **鏃犲弬鏋勯€犲櫒**锛圝PA **蹇呴』瑕佹眰**锛侊級

```
public Category() {}
```

> **涓轰粈涔?JPA 闇€瑕佹棤鍙傛瀯閫犲櫒锛?* 鍥犱负 Hibernate 鍦ㄥ姞杞藉疄浣撴椂锛屽厛 new Category()锛屽啀閫氳繃鍙嶅皠 setXxx() 璧嬪€笺€?

------

##### 鍥涖€丂AllArgsConstructor

###### 鐢熸垚 **鍏ㄥ弬鏋勯€犲櫒**

```java
public Category(Long categoryId, String categoryName) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
}
```

> 甯哥敤浜?**娴嬭瘯銆丅uilder 妯″紡銆丏TO 杞崲** 绛夊満鏅
 
