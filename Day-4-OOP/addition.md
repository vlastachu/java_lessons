##Объекты
```java
Object object = new Object();
//Здесь мы: 1. инициализируем область в памяти под объект класса Object.
//2. Вызываем конструктор без параметров.
//3. Присваиваем его новой переменной с типом Object и названием object.  
System.out.println(object.toString());
//обращаемся к методу toString() (на самом деле он бы и так вызвался если бы мы передали объект)
```

```java
int a[] = {1, 2, 3, 4, 5};
//массив хранит поле в котором указывается количество элементов - обратимся к нему:
System.out.println(a.length); // => 5 
//никаких особых методов у массива не определено кроме тех же что у Object
```

##Организация памяти

1. Регистры. 
2. Кеш.
3. Стек.  
4. Куча. 
5. Постоянная память.

Перенные-объекты являются ссылками:

```java
int a = 5, b;
b = a;
System.out.println("a = " + a + "; b = " + b + ";"); //=> a = 5; b = 5;
a = 9;
System.out.println("a = " + a + "; b = " + b + ";"); //=> a = 9; b = 5;

int[] c = new int[]{1, 2, 3};
int[] d = c;
//видимо так исторически сложилось что стандартный вывод массива выглядит не так как хотелось бы
//c.toString() вернет нам что-то вроде "[I@3343c8b3" первый символ говорит о том что это массив
//второй символ говорит о значениях массива и дальше по-видимому указатель в памяти
//хотя нам это совсем не нужно. Поэтому воспользуемся import java.util.Arrays;
System.out.println("c = " + Arrays.toString(c) + "; d = " + Arrays.toString(d) + ";");
//=> c = "[1, 2, 3]"; d = "[1, 2, 3]";
c[0] = 5;
System.out.println("c = " + Arrays.toString(c) + "; d = " + Arrays.toString(d) + ";");
//=> c = "[5, 2, 3]"; d = "[5, 2, 3]";
c = new int[]{4, 5, 6};
System.out.println("c = " + Arrays.toString(c) + "; d = " + Arrays.toString(d) + ";");
//=> c = [4, 5, 6]; d = [5, 2, 3];
```

##Операторы эквивалентности для ссылок
```java
int[] c = new int[]{1, 2, 3};
int[] d = c;
if(c == d) // true
    System.out.println("c is equal d");
int[] d = new int[]{1, 2, 3};
if(c == d) // false
    System.out.println("c is equal d");
```

##Общая форма
```java
[private|public] class <имя класса> [extends <name>] [implements <name1, name2>]{
    //определение полей
    //final поля можно сразу инициализировать
    [private|public|protected] [static] [final] <тип> <имя> [= значение];
    //определение методов
    [private|public|protected] [static] <тип> <имя>(<аргументы через запятую>){
        //код
    }
    //определение конструкторов
    [private|public|protected] <имя класса>(<аргументы через запятую>){
        //код
    }
}
```


###пример cube


```java
public class Block{
    public int width, height, depth;
}
```


```java
Block cube = new Block();
cube.width = 3;
cube.height = 3;
cube.depth = 3;
System.out.printl("width is " + cube.width);
```


```java
public class Block{
    public int width, height, depth;
    //конструктор без параметров инициализирует поля нулями
    public Block(){
        width = 0;
        height = 0;
        depth = 0;
    }
    //конструктор задающей каждую сторону
    public Block(int w, int h, int d){
        width = w;
        height = h;
        depth = d;
    }
    //добавим для разнообразия метод возвращающий объём куба
    public int getValue(){
        return width*height*depth;
    }
}
```

##static

```java
 class StaticTest {
  static int і = 42;
 }
 ```
 
 ```java
 StaticTest st1 = new StaticTest();
 StaticTest st2 = new StaticTest();
 st1.i = 1; //в обоих объектах общее поле равно 1
 StaticTest.i++; // = 2
```


```java
class Incrementable {
    static void increment () { StaticTest.i++; }
}
```


```java
   Incrementable sf = new Incrementable();
   sf.increment();
```


```java
  Incrementable.increment();
```


##final (инициализация)

```java
if(i > 0 && i < 7){ //why 7?
```

```java
final int firstDayOfWeek = 0;
final int daysInWeek = 7;
if(i > firstDayOfWeek && i < daysInWeek){
```

```java
public class Stack{
    protected final int[] values;
    protected final int size;
    protected int pointer;
    public Stack(int size){
        this.size = size;
        values = new int[size];
        pointer = 0;
    }

    public Stack(){
        size = 100;
        values = new int[size];
        pointer = 0;
    }

    //very unsafe
    public Stack push(int value){
        values[pointer++] = value;
        return this;
    }

    public int pop(){
        return values[--pointer];
    }
}
```

##порядок инициализации

```java
class Person{
    String firstName = "John";
    public Person(){
        firstName = "";
    }
    String secondName = "Smith";
```

###static

Тоже самое можно проделать и для статических полей.

```java
class Person{
    static int count = 0;
    static Person lastPerson;
    static{
        lastPerson = null;
    }
}
```
#Наследование
```java
class Manager extends Person{

}
```


##Конструктор по умолчанию



```java
class Person{
    String firstName, secondName;
    Person(String name, String lastName){
        firstName = name;
        secondName = lastname;
    }
    
    Person(){
        this("", "");
    }
}
```

```
class A{
    A(){
        System.out.println("A");
    }
}

class B extends A{
    B(){
        System.out.println("B");
    }
}
```


```
class A{
    A(){
        System.out.println("A");
    }
    A(int i){
        System.out.println("A" + i);
    }
}

class B extends A{
    B(){
        super(1);
        System.out.println("B");
    }
}
```

##Пример иерархии классов.

```java
Class Mesh{
    float up, down, right, left;
    void draw(){}
    void onCollision(Mesh mesh){}
}

Class Engine{
    private Mesh[] meshes;
    private Player player;
    Engine(){
        //инициализируем графическую систему
        //подписываемся на нажатия клавиш
        //читаем файл хранящий положение всех объектов на карте
        //инициализируем массив с мешами записываем туда все нужные объекты и игрока
    }
    void onDraw(){
        for(int i = 0; i < meshes.length; i++)
            meshes.draw()
    }
    void onKeyPress(int keyCode){
        if(keyCode  == Keys.UP)
            player.move(Direcion.UP);
        for(int i = 0; i < meshes.length; i++)
            for(int i = 0; i < meshes.length; i++)
                //нужно проверить все коллизии
    }
}

class Player extends Mesh{
    @override
    void onCollision(Wall wall){
        //каким-нибудь образом отменяем последнее движение в сторону стены
    }
    
    @override
    void onCollision(Trap trap){
        //смерть
    }
    
    void Move(Direction dir){}
}

class Wall extends Mesh{}

class Trap extends Mesh{}

```
