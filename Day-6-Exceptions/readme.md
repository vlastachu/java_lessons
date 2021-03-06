Полезные статьи: [Oracle Tutorial](http://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html) и [хабр](http://habrahabr.ru/company/golovachcourses/blog/223821) [продолжение](http://habrahabr.ru/company/golovachcourses/blog/225585/)

#Exception

Exception (**исключение**) - событие, проявляющееся во время исполнения программы, которое прерывает нормальный поток исполнения операций.

Когда ошибка проявляется внутри метода, метод создаёт объект и отдаёт его системе исполнения. Объект называется *exception object* содержит информацию об ошибке, включая тип ошибки и состояние программы на момент возникновения ошибки. Создание объекта исключения и передача его системе исполнения назыается пробрасывание исключения (throwing an exception).

Когда система исполнения (runtime system) получает исключение - она пытается найти что-то, что могло бы обработать исключение. Она ищет это *что-то* в списке методов, которые были вызваны ранее. То есть на момент вызова исключения мы находимся в методе1, который был вызван методом2, который был вызван методом main, тогда система будет искать обработчик сначала в нашем методе1 и так выше. Такой список методов называется **callstack**.

![иллюстрация]()

Если система исполнения находит подходящий обработчик исключения, то это называется *поймать исключение* (**catch** the exception).

Почему все так устроено? Допустим вы пишете некий метод, принимающий на вход список и допустим рандомно перетасовывающий элементы в нём (что-то вроде карточной игры). Когда, вы принимаете объект извне вы знаете, что он может оказаться равным null. Но зная это что вы предпримете? Наверное правильнее было бы ничего не делать. Но что если это ломает логику программы? В таком случае правльнее бросить NullPointerException и пускай сверху с ним разберуться. То есть с ошибкой мы неможем разобраться прямо в месте её возникновения, т.к. мы не знаем как наш метод используется. В методах вызывающих наш мы сможем обработать ошибку, т.к. обладаем достаточным контекстом ошибки. 

Пример конечно надуманный null почти всегда можно обработать в коде без всяких исключений.

#Создание исключений

Чтобы вызвать исключение нам сначало необходимо создать объект-исключение. Он создаётся точно также как и другие объекты в куче с помощью оператора **new**. У всех стандартных исключений существует как минимум два конструктора: без аргументов и с аргументом строкой: объясняет причину исключения.

Далее нам потребуется непосредственно передать исключение системе обработки: это осуществляется с помощью ключевого слова **throw**.

```java
Exception e = new NullPointerException("list = null; can't shuffle");
throw e; //здесь дальнейшее исполнение метода прерывается
```
Чаще всего исключение создаётся в том же месте в котором и пробрасывается:
```java
throw new NullPointerException("list = null; can't shuffle");
```

Во время вызова исключения происходит *безусловный переход* что-то вроде **return**, но при этом метод не возвращает то значение, которое предусмотрено (по сути он никакого значения не возвращает).

Начиная со слова `throw` появляется некоторая магия вокруг исключений: в качестве аргумента для `throw` мы передаём объект, но не каждый объект может стать претендентом на роль исключения. Чтобы объект можно было *пробросить* он (его класс) должен наследовать класс **Throwable** (странно что не интерфейс). Почему это называют магией? Потому что это как-то *не по правилам* чтоли. Если ключевое слово принимает объект, то наверное любой объект. Дальше будет ещё больше магии.


#Перехват исключений: Конструкция try catch

Ранее уже упоминалось, что при пробросе исключения система ищет подходящий обработчик. Исключения обрабатывает блок **try**

```java
try{
    dangerous1();
    dangerous2();
    dangerous3();
}
```
Точнее сказать он их не обрабатывает а перехватывает. То есть если в одном из выражений (не обязательно методов это может быть и просто `1/0;`)  возникнет исключение, то оно перейдёт к обработчик. Просто так блок `try` не пишется за ним обязательно должен следовать `catch` либо `finally`, либо оба сразу.

##catch

```java
private static void dangerous(){
		throw new RuntimeException();
}

public static void main(String[] args){
	try{
		dangerous();
	}
	catch (Exception e){
		e.printStackTrace();
	}
}

>>  java.lang.RuntimeException
	at com.company.Main.dangerous(Main.java:158)
	at com.company.Main.main(Main.java:163)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:601)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:120)
```
Если бы мы не обработали это исключение нас бы ожидал примерно такой же вывод. Зато теперь обработав исключение мы можем работать дальше. Зачастую в случае ловли исключения у нас не остаётся вариантов как продолжать работать. Иногда можно уведомить пользователя например о том что не удаётся открыть файл, мол выбери другой.

Ключевое слово **catch** предназначено для того, чтобы поймать и обработать исключение. `catch` в качестве аргумента принимает также `Throwable` и использует его как аргумент функции. Если никакого исключения не было то блок `catch` не вызовется. Сходство `catch` с функцией в том что к нему будет применен полиморфизм.

Во-первых в этом примере вы можете заметить восходящее преобразование: от RuntimeException до Exception (оба потомки Throwable). Exception является предком для RuntimeException - поэтому преобразование допустимо.

Во-вторых допустимо несколько веток для `catch` (допустим Throwable1 и 2 и 3 унаследованы от Throwable):

```java
try{
    throw new Throwable1();
    throw new Throwable2();
    throw new Throwable3();
}
catch(Throwable3 t){}//допустимо несколько (включая 0) блоков catch
catch(Throwable2 t){}
catch(Throwable1 t){} // <- вызовется этот
```

Что тут происходит? Сначала пробрасывается исключение типа `Throwable1`. Значит остальное исполнение блока `try` прекращается и начинается поиск подходящего обработчика. Cначала находим обработчик для `Throwable3`, но 1 и 3 находятся на разных ветках наследования, поэтому восходящее преобразование недопустимо. Далее ищем другой обработчик - 2 тоже не подходит. и останавливаемся на 3. 

Что будет если убрать строку `catch(Throwable1 t){}`? Обработчик так и не будет найден и исключение проброситься на уровень выше. Возможно во всей программе не будет обработчика тогда программа приостановится.

Что будет если добавить строку `catch(Throwable t){}`? Если мы её добавим в конце то всё относительно хорошо:
```java
try{
    //опасный код
}
catch(Throwable3 t){}
catch(Throwable2 t){}
catch(Throwable1 t){} 
catch(Throwable t){}
```
В итоге возможно 3й 2й или 1й обработчики в зависимости от бросаемого исключения. И если бросается какое-то другое исключение то обязательно исполнится последний обработчик. Если мы поставим его в начало:
```java
try{
    //опасный код
}
catch(Throwable t){}
catch(Throwable3 t){}
catch(Throwable2 t){}
catch(Throwable1 t){} 
```
 То исполнится только первый обработчик. То есть остальные 3 ниже **никогда** не исполнятся. Ваша IDE их вам скорее всего подчеркнет, т.к. они бесполезны. Это происходит потому что поиск подходящего обработчика осуществляется последовательно сверху вниз. А `Throwable` является наиболее общим типом для всех исключений.

##Оператор instanceof

Вас возможно задело то что во время исполнения `catch` может проверить, к какому типу относится объект. Такую магию можно провернуть и в остальном коде не относящемся к исключениям. 

Дело в том что информация о классе объекта, о всей иерархии классов хранится в памяти во время исполнения (в отличии от например от C++ где с этим посложнее). И более того вся эта информация может бывть предоставлена программисту (гуглите reflection).

В более менее простом случае, когда мы откуда то получаем ссылку общего типа и нам необходимо узнать частный то мы можем воспользоваться оператором `instanceof`. По сути `instanceof` является таким же бинарным булевым оператором как и `==` или `<=` и т.д. Только слева он принимает объект, а справа класс. Ну и соответственно проверяет есть ли класс в списке предков класса данного объекта. Важно, что он делает это не на основании информации о типе переменной (иначе бы это можно было вычислить на этапе компиляции), а во время исполнении просматривает реальный класс объекта.

```java
Throwable t = new RuntimeException();
//...
if(t instanceof Exception){ //true
	Exception e = (Exception)t; // выполняем небезопасное преобразование, 
	//но для себя можем быть уверены, что оно не вызовет ошибок
	//...
}
if(t instanceof Error){ //false
	Error e = (Error)t;
	//...
}
if(t instanceof Object){ //всегда истина за исключением тех случаев когда t = null
	//для null instanceof всегда возращает false
}
```

##Пример

Небольшой пример использования `try catch`. Допустим у нас имеется `Map<String, Map<String, List<String>>>`. Выглядит безумно, но можно допустить, что мы парсим какой-нибудь структурированый текстовый формат (например json или xml) в мапы и списки. Конечно сейчас есть библиотеки, чтобы парсить более типобезопасно.

Итак в нашем файле (или не файле) допустим хранится информация посылаемая от клиента к серверу - запрос к ячейкам базы данных, слова для поиска. Мы почти уверены, что там есть поле "request" и поле "keywords" и в нем интересующая нас информация. Правда есть все же вероятность, что текст немного поврежден. Или клиент балуется и пытается послать что-нибудь другое. 

```java
//псевдокод конечно же
try{
	Map<String, Map<String, List<String>>> req = (Map<String, Map<String, List<String>>>)client.parseRequest();//ошибка преобразования
	for(String keyword: req.get("request").get("keywords")){ //3 NullPointerException
		//TODO
	}
}catch(RuntimeException e){
	Log.warning("it's impossible but wrong request: " + client.getRequest());
	// а ещё здесь мы должны послать клиенту ответ, что запрос неверный
}
```

Иногда это удобно - сделать обработчик всех критических ситуаций в одном месте. Правда это в то же время пример плохого кода, т.к. почти всегда потомков RuntimeException можно отловить вполне стандартными средствами. То есть RuntimeException это например деление на 0, обращение к null, выход за границы массива, ошибка преобразования. Почти всегда такие ошибки можно отловить if. И в этом плане `try catch` представляется нам синтаксическим сахаром, который освобождает нас от необходимости нагромождать `if`.

Почему все же не стоит здесь пользоваться `try catch`? Дело в том что проброс исключения - операция весьма **дорогостоящая**. Ввиду того, что необходимо сохранить весь стек вызовов и состояние программы на момент исключения. Иногда это не столь критично, зачастую ясность кода важнее производительности (т.к. поддержка кода может обойтись дороже чем железо) и может быть такое, что нам нужны гарантии от определенного участка кода. 

##Checked и Unchecked Exceptions

Начнём паралельно знакомиться с java.io.

io - обозначет input/output (ввод/вывод). Предоставляет базовые средства для общения с внешним миром. `System.out` также относится к io (точнее использует его). Самое обыденный пример ввода/вывода это работа с файлами. А самая простая обработка файла это побайтовое чтение и побайтовая запись. Дальше мы можем обернуть эти байты в более высококоуровневые конструкции (что и сделано в других классах в java.io). Напишем (лучше в IDE) следующую строку:

```java
import java.io.FileInputStream;
//... main
FileInputStream in = new FileInputStream("input.txt");
```
На первый взгляд всё выглядит достаточно безобидно. У класса `FileInputStream` есть конструктор принимающий строку (кстати если вы оставите текстовый курсор на конструкторе и нажмете `CTRL+B` в IDEA то перейдете к опеределению конструктора и увидете предупреждение `throws FileNotFoundException` в коде - вернемся к этому позже). Создаём объект и присваиваем переменной подходящего типа. Казалось бы всё в порядке, но при компиляции (ну или в IDE ещё до компиляции) видим ошибку: `java: unreported exception java.io.FileNotFoundException; must be caught or declared to be thrown`.

До этого ранее вы встречались с исключениями, которые обсуждались выше. Забыли проинициализировать переменную и все такое. Но все эти исключения (скорее всего) были потомками `RuntimeException`. Такие исключения называются **unchecked**. Это нигде не отражено в коде разделение на checked/unchecked является магией (то есть просто знать надо). Unchecked (непроверяемые) исключения заключаются в том что мы не обязаны их обрабатывать. Такое иссключение вполне естественно. Более странными выглядят **checked** исключения с которыми вы ранее не сталкивались. Их мы обязаны проверять (всмысле обрабатывать). 

В данном случае мы должны поймать `FileNotFoundException`, либо его предка, тогда компилятор разрешит компилировать программу. 

```java
try{
	FileInputStream in = new FileInputStream("input.txt");
} catch(FileNotFoundException e){

}
```

В некотором смысле проверяемые исключения представляют больший интерес, чем обычные. Они в основном связаны с работой с внешними ресурсами файлы, сеть и т.д. То есть непроверяемые исключения - ошибки программиста (пускай сложные и не очевидные, но все равно можно обойтись без обработчика). Проверяемые исключения - независящие от программиста обстоятельства. Заранее в коде нельзя гарантировать, что файл по указанному пути существует и если существует нельзя сказать, что у него есть необходимый доступ. 

##ключевое слово `throws` 

Выше уже упомяналось, что зачастую мы не можем обработать исключение в том контексте в котором мы его *бросаем*. Как тогда поступить с checked исключениями? 

В определении метода мы можем написать слово `throws`. Например конструктор `FileInputStream`:
```java
    public FileInputStream(String name) throws FileNotFoundException {
        this(name != null ? new File(name) : null);
    }
```

Далее конструктор `FileInputStream(File file)`. И в итоге это сводится к нативному методу:

```java
    private native void open(String name) throws FileNotFoundException;
```

У `throws` есть 2 задачи: убрать необходимость обрабатывать исключение (и соответственно перекладываем эту ответственность на уровень выше) и описательная задача (документирование). Например в случае unchecked исключений написать `throws NullPointerException` - будет предупреждением для программиста, но в целом это будет равносильно написанию того же самого в комментарии к методу. Пример из класса `Integer`:

```java
    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s,10);
    }
```
Хоть в определении метода и содержится предупреждение - оно не накладывает никаких обязательств.

#Ключевое слово `finally`

После блока `try` и после всех `catch` может быть слово `finally`. `catch` представляет код, который будет исполнен, если произойдёт исключение, в то время как `finally` код будет выполнен в любом случае. Это считай равносильно тому как мы просто оставим код после блоков `try catch`. Но в случае кодом внутри `finally` мы получаем гарании того что код будет исполнен (конечно мы все равно не застрахованы от исключений внутри `finally`). Код в finally будет исполнен даже если catch не перехватил нужное исключение (или если вообще нет блоков `catch`).

Примером хорошего использования блока `finally` может быть работа с внешними ресурсами. Когда мы получаем доступ к внешнему ресурсу, то по окончанию работы мы *обязаны* вернуть его системе. Иными словами *закрыть* ресурс. Потому что:

* Если вы что-то записывали в файл, то (не замечая для себя) вы использовали буферезированный ввод вывод. Когда вы закончили работу с файлом, то часть данных осталась в буфере. Чтобы принудительно вывести буфер в файл его следует либо закрыть, либо ещё есть метод `flush`.
* У системы может быть предел используемых ресурсов. То есть постоянно открывая (и не закрывая) файлы система вам может отказать.

Хотя когда ваша программа закрывается все ресурсы высвобождаются, но все равно стоит помнить про первый пункт. Итак нам нужны *гарантии* того, что мы закроем файлы после их использования. 

```java
//код побайтово копирует из одного файла в другой
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("input.txt");
            out = new FileOutputStream("output.txt");
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null)
                in.close();
            if (out != null) 
                out.close();
        }
```

Есть мнение, что блок `finally` в целом полезнее, чем `catch`. Так как если мы поймали какое-то исключение конкретного типа, то мы не знаем что с ним делать кроме как уведомить пользователя, либо записать в логах для отладки. В то время как задача для `finally` с высвобождением ресурсов более насущная.

Чтобы убедиться в гарантиях `finally` можете посмотреть следующий код (результат функции - 4):

```java
int fun(){
	try{
		return 2;
	} finally{
		return 4;
	}
}
```

##try c ресурсами
Задача использования ресурсов встречается довольно часто, а подводных камней в ней много. Поэтому в java 7 появилась конструкция, которая облегчает жизнь. 

```
try (OutputStream stream = openOutputStream()) {
    // что-то делаем со stream
}
```
От вас по-прежнему потребуют написать блоки `catch` если там есть checked exception но об освобождении ресурса позаботится среда исполнения. В общем идея не так уж и сложна: инициализируете и возможно объявляете в скобках переменную ресурсного типа по окончанию блока она освободится как положено.

##иерархия исключений

![exceptions](https://thenewcircle.com/static/bookshelf/java_fundamentals_tutorial/images/ExceptionClassHierarchy.png)

Здесь представлены некоторые примеры исключений. Вкратце: RuntimeException - unchecked чаще всего ошибка программиста, если видим потомка RuntimeException значит можно что-то устранить в коде чтобы все было в порядке. Exception (хоть и является прдком RuntimeException, но стоит рассматривать как разные сущености) checked - может возникнуть независимо от программиста, но в строго определенных местах, обязательно подлежит обработке. Error unchecked - то что не зависит от нас, но в отличии от Exception может появиться где угодно. Не подлежит обязательной обработке, но иногда в особых случаях можно перестраховаться. Throwable checked - не видел его потомков кроме Error и Exception, возможно вы захотите построить свою иерархию исключений и тогда Throwable может пригодиться.

#java.io
Работа с внешними ресурсами в java представлена в виде потоков (stream). Ранее вы уже работали с потоками через `in` `out` и ещё есть `err` (тоже что и `out` только предназначен для вывода ошибок). Потоки подразумевают под собой абстракцию работающую с устройством ввода или вывода данных. Поток можно представить как последовательность байт - мы можем прочитать один, если не достигли конца файла. Или записать один байт. Ниже изображена немного страшная картинка иерархии потоков (Stream классы работают исключительно с байтами).

![io streams](http://www.kumanov.com/docs/prog/Teach%20Yourself%20Cafe%20in%2021%20Days/fb-5.gif)

Выше мы уже познакомились с вводом/выводом байтов. Часто вам понадобится работать с символами (char), которые представлены в размере более 1 байта (хотя не всегда). В java символы кодируются в UTF16, занимают 2 байта. То есть если вы собираетесь работать с файлом как с текстом, то `FileInputStream` вам скорее всего не подойдёт. Для этих целей существуют `FileReader` и `FileWriter` Это другая ветка в иерархии классов java.io

![иерархия reader writer](http://flylib.com/books/2/33/1/html/2/files/09fig04.gif)

На низшем уровне работа с устройством ввода вывода все равно осуществляется через байты, но Reader'ы все же удобнее, особенно если речь заходит о локазлизации приложения. Кстати вот тут http://tutorials.jenkov.com/java-io/overview.html (внизу страницы) есть неплохая таблица для классов java.io которую мне лень было переписать. 

**Недоделаная часть**
В java.io используется паттерн декоратор. Например DataInputStream принимает в конструкторе другой поток ввода например FileInputStream тем самым дополняя его своими методами.

DataInputStream - чтобы выводить разные примитивы в бинарном виде

#Задания
Напрямую заданий к обработке исключений придумать сложно. Разве что тесты которые спрашивают как код поведет себя в этом случае как в том. Можно ещё придумать задание, что вот мол сделайте свой класс исключения, пускай оно будет checked и обработайте его в main. Ну здесь ничего сложного - простое наследование. Если хотите попрактикуйтесь - можете задавать свои вопросы по возникающим проблемам.           
Более менее требующая обработок исключений задача - это работа с файлами.
Кстати насчет корректного закрытия файла http://stackoverflow.com/questions/156508/closing-a-java-fileinputstream здесь рекомендуют воспользоваться IOUtils. Она находится в apache.commons это набор классов дополняющий стандартную библиотеку java. Вам когда-нибудь придется воспользоваться ей если вы будете программировать в дальнейшем на java, но думаю это перебор для домашних заданий. 
Пока что можно воспользоваться 
```java
FileInputStream fis = null;
try {
    fis = new FileInputStream(file);
    //... process ...
}
catch (IOException e) {
   // print e.message
}
finally {
    try {
        if (fis != null)
            fis.close();
    }
    catch (IOException e){
       // print e.message
    }
}
```

либо try с ресурсами
```java
try(FileInputStream fis = new FileInputStream(file)){
    //... process ...
}
catch(IOException e){
   //print e.message
}
```
можете почитать о проблеме на хабре http://habrahabr.ru/post/178405/



##1. Нахождение одинаковых файлов

На вход программе подаётся корневой каталог (папка в которой не так уж много, но достаточно файлов)

На выходе от неё ожидаем список повторов. Чтобы упростить задачу допускается следующий вид вывода
```
С:/.../file1 equal to С:/.../file2
...
С:/.../file1 equal to С:/.../file3
```
То есть не так умно как могло бы быть:
```
С:/.../file1 equal to: С:/.../file2; С:/.../file3;
```
Но там уж как хотите, должно быть это не так уж сложно доделать.

Здесь мы сталкиваемся с проблемой, что хранить файлы в оперативной памяти мы не можем - она запросто иссякнет. Поэтому нам нужно хранить хэш. В java есть средства чтобы кодировать MD5 или SHA (MD5 признан устаревшим), но это немного долго, а во-вторых они предназначены для криптографических целей - как можно меньше коллизий. Нам бы это тоже пригодилось, если бы мы проверяли действительно много файлов. Но для скромной домашней работы хватит и `Arrays.hashCode`:

```java
        int result = 1;
        for (byte element : a)
            result = 31 * result + element;
```
Вот так оно просто делает хеш. Основные свойства хеша: чтобы у одинаковых файлов он 100% совпадал и у разных файлов в большинстве случаев отличался. Если хеш совпадает, то мы проверяем файл побайтово и тогда уверенно можем сказать, что они совпадают. 

Весь файл кодировать в хеш не стоит. Допустим хватит 512 байт, плюс в начале массива байт запишем размер файла (придётся int перевести в byte через >> смещение, а затем преобразование `(byte)`) на случай если начало файлов совпадает. Разумеется при полной проверке такого делать не стоит.

Хранить информацию о файлах рекомендую в Map<int, List<File>> (инт - хеш) перед каждым складыванием проверять есть ли уже такой хеш. Если есть, то открыть 2 BufferedInputStream в каждом выполнять считывание по байту до конца одного из файлов. Кстати тут интересный момент - что делать, если хеш совпадает, а файлы нет. Из-за Map отображает хеш в список файлов (совпадающие файлы туда записывать не рекомендую, либо сделайте список из списка) и при совпадении проверьте каждый файл с совпавшим.

О классе `File` и о том как проходится по списку директорий и файлов данного пути написано в конце предыдущей лекции.

##2. Судоку
```
    Problem statement                Solution

    .  .  4 | 8  .  . | .  1  7      9  3  4 | 8  2  5 | 6  1  7
            |         |                      |         |
    6  7  . | 9  .  . | .  .  .      6  7  2 | 9  1  4 | 8  5  3
            |         |                      |         |
    5  .  8 | .  3  . | .  .  4      5  1  8 | 6  3  7 | 9  2  4
    --------+---------+--------      --------+---------+--------
    3  .  . | 7  4  . | 1  .  .      3  2  5 | 7  4  8 | 1  6  9
            |         |                      |         |
    .  6  9 | .  .  . | 7  8  .      4  6  9 | 1  5  3 | 7  8  2
            |         |                      |         |
    .  .  1 | .  6  9 | .  .  5      7  8  1 | 2  6  9 | 4  3  5
    --------+---------+--------      --------+---------+--------
    1  .  . | .  8  . | 3  .  6      1  9  7 | 5  8  2 | 3  4  6
            |         |                      |         |
    .  .  . | .  .  6 | .  9  1      8  5  3 | 4  7  6 | 2  9  1
            |         |                      |         |
    2  4  . | .  .  1 | 5  .  .      2  4  6 | 3  9  1 | 5  7  8
```
[Таже задача но в файле](https://github.com/vlastachu/java_lessons/blob/master/Day-6-Exceptions/sudoku.txt)

Можете почитать эффективные методы [решения судоку](http://en.wikipedia.org/wiki/Sudoku_solving_algorithms)
Я останавлюсь на простом переборе: backtracing. 

Для начала нужно прочитать файл '.' обозначает пустое поле, цифра - занятое. Остальные символы в том числе и пеереносы строки должны игнорироваться. Когда массив 9х9 заполниться - остальные символы в файле также должны игнорироваться. 

Название файла программа получает в первом аргументе в командной строке. Второй аргумент название файла вывода (по умолчанию output.txt). В выходном файле должны быть числа через пробел и с двойным переводом строк в конце линии. В остальном декорации не обязательны.

Игровое поле можно представить в виде двумерного массива. И тогда должна быть функция проверяющая корректность при установке значения в поле. Можно ещё сделать объект который будет содержать список списка интов (ряды) и тоже самое для строк и ещё 9 множеств для квадратов. В таком случае данные дважды дублируются (что немного опасно в плане расхождения игрового поля в рядах и строках и ещё опаснее если присутствует многопоточность).

Наверное лучше всего сделать класс содержащий двумерный массив из int (или byte). 0 будет обозначать пустое поле. При установке значений поля значение может не установится и вернуться false свидетельствующий о недопустимости операции.  

Или не вводить дополнительных классов - просто двумерный массив. На каждом шаге рекурсивного вызова можно создавать множество с числами от 1 до 9 и вычитывать (remove) занятые числа в строке, колонке и квадрате. оставшиеся перебирать. Важно что в переборе с возвратом в основном будут случаи неудачи (когда не осталось допустимых чисел) тогда вы например возвращаете null, а в функции выше получая null ставите следующее допустимое число, при этом в рекуррентный вызов передаёте не массив а его копию. Чтобы возможно было восстановить предудщее состояние массива. Хотя можно и не засорять память храня только изначальный массив, а чтобы восстановить состояние просто затирать ячейки после текущей значениями из изначального массива (я использовал массив на каждом вызове и у меня работало достаточно быстро на медленном ноуте и оперативы вполне себе хватило :-))

Тонкий момент: в java.util.Arrays нет методы для копирования двухмерного массива. Вот такую функцию можно сделать для частного случая:

```java
	public static byte[][] deepCopy(byte[][] original) {
		if (original == null) {
			return null;
		}

		final byte[][] result = new byte[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}
```


##3. Кроссворд

Творческое задание.
Особых алгоритмов как составлять кроссворды я не находил. Думаю люди обычно вырисовывают примерно симметричную фигуру для слов, затем ищут в словарях подходящие слова (было бы удобно если они отсортированы по длине) и вписывают их. затем приделывают вопросы к словам. Здесь задача поставлена другим образом. Есть  файл содержащий слова из них требуется составить кроссворд. Для хранения кроссворда воспользоваться достаточно большим двумерным массивом char (первое слово положить где-нибудь посередине), обернутым в какой-нибудь класс. Так чтобы при складывании слова запоминать его координаты, ориентацию (горизонтальное\вертикальное bool), и длину. 

Думаю стоит сделать полный перебор возможных размещений слов. Сначала все горизонтальные, затем все вертикальные, при этом отсекая все невозможные варианты - например горизонтальное персекается с горизонтальным, либо линией выше или ниже. Перебор также делать с возвратом, результат каждого рекурсивного вызова записывать в список (то есть функция возвращает список) и из списка выбрать тот вариант, который даёт наибольшее количество пересечений (считать их в каком-нибудь поле класса). Конечно можно и как-нибудь оптимальнее. К тому же как критерий можно вводить дополнительные метрики, мол наименьшиий размер, симметричность фигуры.


##4. Фильтр bmp изображения

Пример работы фильтра (да выглядит так как будто произошёл какойто рандом. наверное интереснее будет если обменяете значения каналов или инверсите их).

![example](https://leto35h.storage.yandex.net/rdisk/a4f0eeb8127944fe27af0b6a8aa9c59b/mpfs/TwBHoIAhFTOU5QiZbUMRpGMJCvhIoGsdA9gqNH3GhBLnEdJ2s3EaMDdNxNZNnxgcopgtws5VQlAur2J7wguSIw==?uid=0&filename=lal.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&rtoken=cee3ac87d08e9862a6e372d7a87f3e39&rtimestamp=5444ce56&force_default=no)

[Описание формата](https://ru.wikipedia.org/wiki/BMP)

bmp выбран как один из самых простых и в тоже время он уже достаточно запутан и есть много разных случаев. Рассмотрим только случай 24битного изображения (не уверен но думаю он более распространенный и ещё в нём пэинт сохраняет). По байту на цвет. 

На википедии написано где какая информация расположена. Здесь первая загвоздка: байты в основном (на провереных изображениях) хранятся в формате big-endian. То есть биты слева направо, а байты в WORDе справа налево. Чтобы никто не застрял в начале приведу такую вам функцию: 
```java
	static int byteToInt(byte[] a, int offset, int length){
		int res = 0;
		for (int i = 0; i < length; ++i){
                     res = res | ((a[offset + i]&0xff) << 8*i);
		}
		return res;
	}
```
Используется следующим образом: `int height = byteToInt(bmp, 0x16, 4);`. Из полей информации нам пригодятся: сдвиг для пикселей (0x0A), ширина (0x12). Ну и это впринципе всё. Можно проверить количество бит на пиксель. Можно проверить формат байтов. Ещё я слышал, что не всегда на один цвет одинаковое количество бит.

Как хранятся 24битные пиксели: с начала положения для пикселей идут друг за другом по байту синий, зелёный, красный, и так до конца ряда - в конце пустые байты, так чтобы количество байт в ряду было кратно 4 (то есть их может и не быть). Кстати ширина указывается в пикселях так что домножайте на 3.

Небольшое пояснение про `a[offset + i]&0xff` -  в java нет беззнаковых типов. Допусти у нас есть байт -70 = 0хВА мы хотим его сдвинуть и поместить после него следующий байт. 0xBA << 1 = 0xBA00 (в идеале), но в джава << является операцией для инта и сначала приводится к инту. Казалось бы должны быть теже биты - но нет. значение тоже самое -70, но кодируется оно иначе: 0хFFFFFFBA. Понятно что нам нужны только последние два знака поэтому выполняем "побитовое и" после которого первые F обращаются в 0. 
