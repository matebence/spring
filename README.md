## Unit tests with plain JUnit & Mockito

### Annotations explained

- JUnit < 5
    - **@RunWith(MockitoJRunner.class)**
- JUnit >= 5
    - **@ExtendWith(MockitoExtension.class)**

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

### Depencies with Spring

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
    @EnabledForJreRange(min= JRE.JAVA_8)
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