## Unit tests with plain JUnit & Mockito

### Setting up MySQL Container

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

```bash
docker run -d -p 3306:3306 --name mysql-docker-test-container -e MYSQL_ROOT_PASSWORD=test -e MYSQL_DATABASE=test -e MYSQL_USER=test -e MYSQL_PASSWORD=test mysql/mysql-server:latest
```

### Annotations explained

**Without Spring**

- JUnit < 5
    - **@RunWith(MockitoJRunner.class)**
- JUnit >= 5
    - **@ExtendWith(MockitoExtension.class)**

**With Spring**

- JUnit < 5
    - **@RunWith(SpringRunner.class)**
- JUnit >= 5
    - **@ExtendWith(SpringExtension.class)**

```java
public class MockitoExtension
extends java.lang.Object
implements BeforeEachCallback, AfterEachCallback, ParameterResolver{..}
```

```java
public class SpringExtension
extends Object
implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver{..}
```

- Service test
	- **@SpringBootTest(webEnvironment = WebEnvironment.NONE)** - annotation loads the full application context so that we can able to test various components
- Controller test (integration)
	- **@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)** - With RANDOM_PORT we instruct Spring to start the embedded servlet container on the random port.
 	- **@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)** - With DEFINED_PORT we instruct Spring to start the embedded servlet container on the pre-defined port. By default, that's port 8080, but we can configure this port with the server. port property.  
- Controller test (unit)
	- **@SpringBootTest(webEnvironment = WebEnvironment.MOCK)**
	- **@WebMvcTest** - annotation loads only the specified controller and its dependencies only without loading the entire application
	- **@JsonTest** - for testing the JSON marshalling and unmarshalling
	- **@RestClientTests** - for testing REST clients
- Repository test
	- **@DataJpaTest** - for testing the repository layer
- **@ActiveProfiles("test")** - activates specific profile
- **@ContextConfiguration/@Import** - test a Component, such as a third party library wrapper or load some specific beans
- **@TestPropertySource** - with this annotation, we can define configuration sources that have higher precedence than any other source used in the project

### Default values explained

- List are **empty** list
- Objects are **null**
- Primitiv are **0** or **false** 

### Stubs, Mock & Fakes

|Type               |What is it for?                      |How to do it in Mockito						|
|-------------------|-------------------------------------|---------------------------------------------|
|Fake               |No tests                             |`mock()`					                    |
|Stub               |Test data                            |`mock(), when().thenReturn()`		    	|
|Mock               |Test behaviour                       |`mock(), verify().myMethod()`				|

### Additional capabilities provided by Spring

What should we Mock??
- **final methods** - its OK, but for this we have to use Mockito inline (in early Mockito there was no support for static and final methods, because of that we used PowerMock wiich is no longer need because of Mockito inline)
- **private methods** - not recommended, but there are utils for this like **ReflectionTestUtils**

### Depencies with Spring

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Depencies without Spring

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.3.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.3.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.platform</groupId>
      <artifactId>junit-platform-suite-engine</artifactId>
      <version>1.9.3</version>
      <scope>test</scope>
    </dependency>
</dependencies>
```

### Code Sample

```java
public interface Sort {

    int[] start(int[] input);
}
```

```java
public class Merge implements Sort {

    @Override
    public int[] start(int[] input) {
        System.out.println("Merge Sort");

        int low = 0;
        int high = input.length - 1;

        mergeSort(input, low, high);

        return input;
    }

    void merge(int[] array, int p, int q, int r) {
        int n1 = q - p + 1;
        int n2 = r - q;

        int[] L = new int[n1];
        int[] M = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = array[p + i];
        for (int j = 0; j < n2; j++)
            M[j] = array[q + 1 + j];

        int i, j, k;
        i = 0;
        j = 0;
        k = p;

        while (i < n1 && j < n2) {
            if (L[i] <= M[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = M[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = M[j];
            j++;
            k++;
        }
    }

    void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }
}

```


```java
public class Quick implements Sort {

    @Override
    public int[] start(int[] input) {
        System.out.println("Quick Sort");

        int low = 0;
        int high = input.length - 1;

        quickSort(input, low, high);

        return input;
    }

    public Integer partition(int[] numbers, int low, int high) {
        int pivot = numbers[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (numbers[j] <= pivot) {
                i++;

                int temp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = temp;
            }
        }

        int temp = numbers[i + 1];
        numbers[i + 1] = numbers[high];
        numbers[high] = temp;

        return (i + 1);
    }

    public void quickSort(int[] numbers, int low, int high) {
        if (low < high) {
            int pi = partition(numbers, low, high);
            quickSort(numbers, low, pi - 1);
            quickSort(numbers, pi + 1, high);
        }
    }
}
```

```java
public class Table {

    private int[] numbers = new int[5];

    private Sort sort;

    public Table() {
    }

    public Table(Sort sort) {
        this.sort = sort;
    }

    public void addNumbers(int[] input) {
        for (int i = 0; i < input.length; i++) {
            numbers[i] = input[i];
        }
    }

    public void clearNumbers() {
        numbers = new int[numbers.length];
    }

    public void sort() {
        numbers = sort.start(numbers);
    }

    public void print() {
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }
    }

    public int[] get() {
        return this.numbers;
    }
}
```

```java
public class Application {

    public static void main(String[] args) {
        Sort quick = new Quick();
        Sort merge = new Merge();

        Table table = new Table(quick);
        table.addNumbers(new int[]{5, 4, 3, 2, 1});
        table.sort();
        table.print();
        table.get();
    }
}
```

### Testing - without Mockito

```java
public class MergeSortStub implements Sort {

    @Override
    public int[] start(int[] input) {
        return new int[]{1, 2, 3, 4, 5};
    }
}
```

```java
public class QuickSortStub implements Sort {

    @Override
    public int[] start(int[] input) {
        return new int[]{1, 2, 3, 4, 5};
    }
}
```

```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SortTest {

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Before all");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("After all");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("Before each");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("After each");
    }

    @Test
    public void firstTest() {
        System.out.println("#1 No failure means 'TEST PASSED'");
    }

    @Test
    @Disabled
    public void secondTest() {
        System.out.println("#2 No failure means 'TEST PASSED'");
    }

    @Test
    public void whenMergeSortIsCalled_thenStubShouldBeUsed() {
        // given
        Sort mergeStub = new MergeSortStub();
        Table table = new Table(mergeStub);

        // when
        table.addNumbers(new int[]{5, 4, 3, 2, 1});
        table.sort();

        // then
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, table.get());
    }

    @Test
    @EnabledOnOs({OS.WINDOWS, OS.LINUX})
    public void whenNumbersAreNull_NullPointerExceptionShouldBeThrown() {
        // given
        Sort mergeStub = new MergeSortStub();
        Table table = new Table(mergeStub);

        // when
        Executable executable = () -> table.addNumbers(null);

        // then
        assertThrows(NullPointerException.class, executable);
    }
}
```

### Testing - with Mockito

```java
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SortTestWIthMockito {

    @Test
    @DisplayName("when merge sort is called then stub should be used")
    public void whenMergeSortIsCalled_thenStubShouldBeUsed() {
        // given
        Sort mergeStub = mock(Merge.class);
        when(mergeStub.start(new int[]{5, 4, 3, 2, 1})).thenReturn(new int[]{1, 2, 3, 4, 5});
        Table table = new Table(mergeStub);

        // when
        table.addNumbers(new int[]{5, 4, 3, 2, 1});
        table.sort();

        // then
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, table.get());
    }
}
```

### Testing - with Mockito annotations

```java
@ExtendWith(MockitoExtension.class)
class SortTestWIthMockitoAnnotations {

    @Mock
    private Merge mergeStub;

    @InjectMocks
    private Table table;

    @Test
    @EnabledForJreRange(min= JRE.JAVA_11)
    public void whenMergeSortIsCalled_thenStubShouldBeUsed() {
        // given
        when(mergeStub.start(new int[]{5, 4, 3, 2, 1})).thenReturn(new int[]{1, 2, 3, 4, 5});

        // when
        table.addNumbers(new int[]{5, 4, 3, 2, 1});
        table.sort();

        // then
        verify(mergeStub, times(1)).start(new int[]{5, 4, 3, 2, 1});
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, table.get());
    }
}
```

```java
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TableTest {

    @Spy
    private Table table;

    @Test
    @Order(22)
    public void whenMergeSortIsCalled_thenArgumentShouldBeUsed() {
        // given
        ArgumentCaptor<int[]> valueCapture = ArgumentCaptor.forClass(int[].class);
        doNothing().when(table).addNumbers(valueCapture.capture());

        // when
        table.addNumbers(new int[]{5, 4, 3, 2, 1});

        // then
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, valueCapture.getValue());
    }

    @Test
    @Order(3)
    public void whenMergeSortIsCalled_thenInvocationShouldBeUsed() {
        // given
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            // then
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, (int[]) arg0);
            return null;
        }).when(table).addNumbers(any());

        // when
        table.addNumbers(new int[]{5, 4, 3, 2, 1});
    }

    @Test
    @Order(2)
    @RepeatedTest(10)
    @EnableIfSystemEnvironmentVariable(name="ENV_VAR_NAME", matches="ENV_VAR_VALUE ")
    public void whenMergeSortIsCalled_thenRealMethodShouldBeUsed() {
        // given
        doCallRealMethod().when(table).addNumbers(any());

        // when
        table.addNumbers(new int[]{5, 4, 3, 2, 1});

        // then
        verify(table, times(1)).addNumbers(new int[]{5, 4, 3, 2, 1});
    }
}
```

```java
@Suite
@SelectClasses({SortTestWIthMockito.class, SortTestWIthMockitoAnnotations.class})
public class IntegrationTest {
}
```

### Example useage of REST Assured

```java
public class ApiTests {

    @Test
    public void getCategories(){
        String endpoint = "http://localhost:8888/api_testing/category/read.php";
        var response = given().when().get(endpoint).then();
        response.log().body();
    }

    @Test
    public void getProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        given().
            queryParam("id", 2).
        when().
            get(endpoint).
        then().
            assertThat().
                statusCode(200).
                body("id", equalTo("2")).
                body("name", equalTo("Cross-Back Training Tank")).
                body("description", equalTo("The most awesome phone of 2013!")).
                body("price", equalTo("299.00")).
                body("category_id", equalTo("2")).
                body("category_name", equalTo("Active Wear - Women"));
    }

    @Test
    public void createProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        String body = """
                {
                "name": "Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price": 12,
                "category_id": 3
                }
                """;
        var response = given().body(body).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        String body = """
                {
                "id": 19,
                "name": "Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price": 15,
                "category_id": 3
                }
                """;
        var response = given().body(body).when().put(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = """
                {
                "id": 19
                }
                """;
        var response = given().body(body).when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSerializedProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Product product = new Product(
               "Water Bottle",
               "Blue water bottle. Holds 64 ounces",
               12,
               3
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSweatband()
    {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        String body = """
                    {             
                        "name" : "Sweatband",           
                        "description" : "White sweatband. One size fits all.",                
                        "price" : 5,               
                        "category_id" : 3          
                    }
                      """;
        var response = given().body(body).when().post(endpoint).then();
    }

    @Test
    public void updateSweatBand()
    {
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        String body = """
                    {   
                        "id": 26,          
                        "price" : 6                      
                    }
                   """;
        var response = given().body(body).put(endpoint).then();
        response.log().body();
    }

    @Test
    public void getSweatband() {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response =
                given().
                    queryParam("id", 26).
                when().
                    get(endpoint).
                then();
        response.log().body();
    }

    @Test
    public void deleteSweatband(){
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = """
                {         
                    "id" : 26       
                }
                """;
        var response = given().body(body).when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void getProducts(){
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        given().
        when().
            get(endpoint).
        then().
            log().
                headers().
                assertThat().
                    statusCode(200).
                    header("Content-Type", equalTo("application/json; charset=UTF-8")).
                    body("records.size()", greaterThan(0)).
                    body("records.id", everyItem(notNullValue())).
                    body("records.name", everyItem(notNullValue())).
                    body("records.description", everyItem(notNullValue())).
                    body("records.price", everyItem(notNullValue())).
                    body("records.category_id", everyItem(notNullValue())).
                    body("records.category_name", everyItem(notNullValue())).
                    body("records.id[0]", equalTo("25"));
    }

    @Test
    public void getDeserializedProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        Product expectedProduct = new Product(
                2,
                "Cross-Back Training Tank",
                "The most awesome phone of 2013!",
                299.00,
                2,
                "Active Wear - Women"
        );

        Product actualProduct =
            given().
                queryParam("id", "2").
            when().
                get(endpoint).
                    as(Product.class);

        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }

    @Test
    public void getMultiVitamins(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        given().
            queryParam("id", 18).
        when().
            get(endpoint).
        then().
            assertThat().
                statusCode(200).
                header("Content-Type", equalTo("application/json")).
                body("id", equalTo("18")).
                body("name", equalTo("Multi-Vitamin (90 capsules)")).
                body("description", equalTo("A daily dose of our Multi-Vitamins fulfills a day’s nutritional needs " +
                        "for over 12 vitamins and minerals.")).
                body("price", equalTo("10.00")).
                body("category_id", equalTo("4")).
                body("category_name", equalTo("Supplements"));
    }
}
```
