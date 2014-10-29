#java.io

Относительно сложная тема к которой я уже третий раз пытаюсь подступиться. Более менее хорошо и полно она описана в книге "Thinking in Java" глава 16. Кстати это предпоследняя глава - то есть Эккель решил оставить её на тот момент, когда читатель хорошо ознакомиться со всем остальным. В то же время с io вы сталкиваетесь уже на первом занятии, т.к. вашим програмам требуется взаимодействовать с внешним миром. 

(здесь ещё небольшое вступление)

#java.io.InputStream

*Иерархия классов java.io отсносящикся к InputStream, отдельно выделен класс не из java.io но всё равно наследующийся от InputStream (таких много, это просто единичный пример) и отдельно выделены классы-декораторы.*

![hierarchy](https://leto41g.storage.yandex.net/rdisk/ac650f6acc90924ea61c14fe61d84a1d/mpfs/oVuHhReuvOjokimgr1GANfWhO8BEmBlQ-DlTuBp7Dqx3wO_PPe7afTq5Tuf_uuixuK6xvoMAO3a9ghCBCH-QOA==?uid=0&filename=2014-10-26%2019-27-19%20%D0%A1%D0%BA%D1%80%D0%B8%D0%BD%D1%88%D0%BE%D1%82%20%D1%8D%D0%BA%D1%80%D0%B0%D0%BD%D0%B0.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&rtoken=78a3a5fa4002442bb4eec09a53f76ca9&rtimestamp=544d1801&force_default=no)

InputStream - абстрактный класс, предназначенный для ввода байтов из различных источников таких, как:

* Массив байтов - ByteArrayInputStream
* Строка - StringBufferInputStream
* файл - FileInputStream
* канал (pipe) - PipedInputStream предназначен для общения между 2мя потоками в вашей программе
* System.in - стандартный ввод (из консоли) имеет тип InputStream 
* сеть - множество классов из java.net реализуют java.io.InputStream например SocketInputStream

Вполне естественно удивление - зачем нужны вводы из массивов и строк? То есть ввод из внешнего мира вполне очевиден, но зачем вводить то что итак внутри программы? Дело в том, что классы из пакета `java.io` кроме того что вводят данные, также и предназначены для их обработки. То есть допустим вами была получена строка и вы хотите её распарсить - вы оборачиваете её в StringBufferInputStream и далее во всякие PushBackInputStream о которых речь пойдёт далее. 

Итак что мы имеем: есть абстрактный класс InputStream - к нему мы прибегнем в том случае, если получаем поток из вне (например параметр для функции). Если мы хотим прочитать внешние (или не внешние) данные то мы используем потомок InputStream. Потомка выбираем изходя из типа источника данных. Наиболее частый случай - файл:

```java
InputStream is = new FileInputStream("file_name"); // всего есть 3 конструктора для этого класс
```

Отбросим пока что тему обработки исключений (допустим все действия происходят в try в котором все нужные типы обрабатываются). Посмотрим какими мы располагаем методами в InputStream:

```java
public abstract int read() throws IOException;
public int read(byte[] buffer, int offset, int length) throws IOException;
public int read(byte[] buffer) throws IOException;
```
Ещё вам пригодится `close` и например `skip` может быть. Первый читает один байт из потока и возвращает от 0 до 255 если байт ыл считан, либо -1 если файл закончился, либо исключение. Второй метод немного автоматизирует эту операцию и записывает байты в массив `buffer` начиная с позиции offset и до length. Возвращает `-1` если файл закончен. Третий метод это `read(buffer, 0, buffer.length)`. С ним уже более менее жить можно, хотя в коде мы не так часто манипулируем байтами. 

##Декораторы

Среди потомков InputStream можно заметить FilterInputStream - абстрактный класс от которого наследуются декораторы для потоков. В конструкторе они не принимают источник данных, заместо этого они принимают потомка от InputStream. Они могут служить оберткой над прямыми наследниками и также оберткой над оберткой. Хотя в данном случае не знаю имеет ли это место. ~~В библиотеке java декораторы немного чудны - они добавляют свои методы, хотя в классических примерах они переопределяют поведение в методах но не добавляют новых.~~

1. `PushbackInputStream` - класс предоставляет односимвольный буфер. То есть вы можете как будто вернуться в прежнее положение вызвав метод `unread`. Полезно для разных парсеров когда вы выбираете подходящую конструкцию перебором по первым символам. Вы делаете предположение и читаете до тех пор пока предположение либо не окажется 100% верным, либо делаете `unread` и снова подбираете вариант.
2. `BufferedInputStream` - никаких новых методов, в конструктор вы можете передать InputStream и размер буфера (по умолчанию `defaultBufferSize = 8192`). Делает достаточно простую вещь: при считывании заполняет свой буфер и возвращает то что вы запросили, при следующем считывании возвращает из буфера, а не из потока и так до тех пор пока буфер не закончится. В нём нет особого смысла если вы считываете сразу много. И нет смысла, если устанавливаете размер буфера слишком малым (тут не могу точно сказать что такое **слишком**). Дело в том что все операционные системы в системе ввода вывода сами буферизуют данные и если вы поставите сверху буфер меньшего размера - то не будет никакой выгоды. 
3. LineNumberInputStream - считает строки и может перейти к заданной строке. Сейчас (и уже давно) считается устаревшим.
4. DataInputStream - работает в паре с DataOutputStream. Сначала вы записываете в файл через DataOutputStream примитивы (через writeShort, writeDouble и т.д.), затем вы их считываете классом DataInputStream (readInt, readShort...). Важно учесть что это можно делать для того, чтобы потом считывать программой, а не предоставлять людям. Можно конечно, но байтовый float человек особо не разберет. 

Есть ещё StreamTokenizer, который наследник Object, но тоже оборачивается вокруг InputStream. Связка InputStream + StreamTokenizer считается устаревшей, сейчас актуален Reader но о нём позже.

###Архивирование

Также от `FilterInputStream ` наследуется `InflaterInputStream` предназначенный для чтения заархивированных файлов. От него наследуется классы с конкретным алгоритмом декодирования: 

* CheckedInputStream - не архивированный файл, просто проверка checksum
* ZipInputStrem - очевидно читает zip
* GZIPInputStrem - gzip
* JARInputStream - jar

Для собственных нужд вам скорее всего хватит GZIP. Zip - предназначен для множества файлов.

###GZIP архиватор
В качестве примера приведу программу, которая при получении файла с расширением **gz** разархивирует его, в ином случае архивирует. 

```java


import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Main {
    //пример приемлимого использования throws
    public static void copy(InputStream input, OutputStream output) throws IOException{
        byte[] buffer = new byte[8096];
        int readen = 0; // сколько считано байт
        while ((readen = input.read(buffer)) != -1) //немного громоздко
            output.write(buffer, 0, readen);
    }

    public static void main(String[] args) {
	    if(args.length < 1){
            System.out.println("no file given");
            System.exit(1);
        }
        final String filename = args[0];
        try{
            if(filename.endsWith(".gz")){ //если на вход подали архив
                final String outputName = filename.substring(0, filename.length() - 3);
                try(InputStream input = new GZIPInputStream(new FileInputStream(filename));
                    OutputStream output = new FileOutputStream(outputName)){
                    copy(input, output);
                    //лаконичнее будет сделать вот так:
                    //Files.copy(input, Paths.get(outputName));
                }
            }
            else{
                final String outputName = filename + ".gz";
                try(InputStream input = new FileInputStream(filename);
                    OutputStream output = new GZIPOutputStream(new FileOutputStream(outputName))){
                    copy(input, output);
                }
            }
        }
        catch (FileNotFoundException e){       //бесполезные кэтчи
            System.out.println("No such file: " + filename);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
```
Текстовый файл размером 42 kb был сжат до 6. Очень даже неплохо, правда с другими форматами таких результатов не добъёшься. 

### JAR архивы
Про zip архивирование мне здесь не хочется рассказывать, в принципе про jar тоже. Ведь не часто возникает необходимость в программе читать другугю скомпилированную программу. Но вот про сам jar думаю самое время упомянуть. 

JAR - Java ARchive - формат для хранения сжатого байт-кода. java не единственный язык, где применяется такой подход. Хотя у вас могут полезть мысли что в ваших паскалях и плюсах вы получаете exe - дело в том, что это компилируемые языки - результатом их деятельности получаются код исполняемый процессором (ну в зависимости от оси они там ещё чем-то оборачивается - не знаю).




#java.io.OutputStream

OutputStream - предназначен для вывода.

Классы с  OutputStream в основном имеют аналог с InputStream (FileOutputStream etc). Здесь мы также можем выводить в файл, в канал, в массив байтов. В строку нельзя (можно но нет прямого класса), для этого скорее подойдёт Reader и Writer. 

Из методов есть метод `write` (также записать один байт либо массив байтов). `close` и `flush`


#Reader и Writer

*Основной причиной появления иерархий классов Reader и Writer стала интернационализация. Старая библиотека ввода/вывода поддерживала только 8-битовые символы и зачастую неверно обращалась с 16-битовыми символами Юникода. Именно благодаря символам Юникода возможна интернационализация программ (простейший тип Java char (символ) также основан на Юникоде), поэтому новые классы отвечают за их правильное использование в операциях ввода/вывода. Вдобавок новые средства спроектированы так, что работают быстрее старых классов.*

Если вы собираетесь работать с файлом как с текстом (на первое время это более частая задача с которой вы будете встречаться), то должны воспользовать классами Reader и Writer. Хоть в их названиян не присутствует слова Stream - они работают по тому принципу. В комментариях стандартной библиотеки они также упомянаются как stream.

Что характерно эти два класса появились в Java 1.1. И до неё читать текст через InputStream считалось нормальным и с тех пор остались классы LineNumberInputStream и StreamTokenizer принимает InputStream. Но сейчас это считается плохой практикой классы методы и конструкторы помечены как `deprecated` (устаревшие). Если вам нужен построчный ввод\вывод то вы определенно работаете с потоком символов, а не байтов.

Возможна ситуация, когда библиотека возвращает вам InputStream, но вы желаете работать с данными как с текстом. Для этого случая есть два *адаптера*: **InputStreamReader** (InputStream -> Reader), также **OutputStreamWriter**. Например вы желаете прочитать запакованный в архив текстовый файл. Среди иерархии `Reader` нет аналога для `FilterInputStream`, т.к. архивация происходит на уровне байтов а не символов UTF. Зато вы можете FileInputStream завернуть в `GZIPInputStream` и уже завернуть в `InputStreamReader`. 

#Reader

Итак мы настроены прочитать текстовый файл. Посмотрим каким мы располагаем методами.
```java
    /**
     *... аналогично как для InputStream
     * @return     The character read, as an integer in the range 0 to 65535
     *             (<tt>0x00-0xffff</tt>), or -1 if the end of the stream has
     *             been reached
     */
public int read() throws IOException;
public int read(char cbuf[]) throws IOException;
public int read(char cbuf[], int off, int len) throws IOException;
```

|Предназначение|Ввод(byte)|Ввод(char)|Вывод(byte)|Вывод(char)|
|--------------|----------|----------|-----------|-----------|
|Базовый класс |InputStream|Reader   |OutputStream|Writer    |
|Файл          |FileInputStream|FileReader|FileOutputStream|FileWriter    |
|Строка        |StringBufferInputStream|StringReader| - |StringWriter    |
|массив (byte/char)|ByteArrayInputStream|CharArrayReader   |ByteArrayOutputStream|CharArrayWriter    |
|pipe          |PipedInputStream|PipedReader   |PipedOutputStream|PipedWriter    |

##StreamTokenizer и Scanner

Оборачивается над InputStream либо Reader (предпочтительнее). Выполняет достаточно популярную задачу, особенно для студентов, особенно решающих олимпиадные задачи. Считывает токены (int, float...) один за другим. http://acm.timus.ru/help.aspx?topic=java&locale=ru - пример использования. **TODO** 

Scanner более удобный в основном предназначен для задачи когда вы примерно знаете структуру файла. StreamTokenizer работает быстрее (что на самом деле не очень важно) и в целом напоминает парсер структурированного файла. Метод `nextToken` возвращает int значение которого представляет тип токена (было бы удобнее если бы это был enum). Тип может быть `TT_EOL` (конец линии), `TT_NUMBER` (число), `TT_WORD` (слово), `TT_NOTHING` (ничего - значение по умолчанию). Если это число то его можно прочитать через публичное поле `public double nval;` (дабл как самый ёмкий тип) если слово то через `public String sval;`. В общем плохой в плане дизайна класс. Может быть удобно в конкретной ситуации но в целом плохой класс.

`java.util.Scanner` по-моему очень удобен и подойдёт вам во многих задачах. С другой стороны он не вписывается в общую иерархию потоков. В конструктор Scanner-у можно напрямую передать объект типа `File` или `String`. Дальше для чтения можно пользоваться *регулярными выражениями* (долгая тема). При этом внутри уже есть заготовки для разных регулярных выражений (шаблонов по которым происходит поиск в тексте). Например есть `boolean hasNext(Pattern pattern)`, который говорит есть далее выражение подходящее под шаблон и есть готовые шаблоны `boolean hasNextBoolean()` и `boolean hasNextByte()` и тд, можно задать разрядность `boolean hasNextByte(int radix)`. Можно прочитать `byte nextByte()` `double nextDouble()`, `String nextLine()`. Есть ещё куча полезных методов типа `skip`.

Для примера решим [задачу с тимуса](http://acm.timus.ru/problem.aspx?space=1&num=1100). Сказать по правде я её немного не понял - неужели это простая сортировка? Ну что ж сделаем класс, который сортируется по второму полю 

Для начала скину решение со StreamTokenizer (оно очень плохое по-моему) - оно прошло проверку на тимусе (там требуется чтобы ввод и вывод были стандартными). Импорты я пропускаю. 

```java

public class Main {
	//реализуем Comparable чтобы можно было сравнивать в стандартных алгоритмах
	static class TableNode implements Comparable<TableNode>{
		private final int id, m; // так эти поля названы в условии задачи
		public TableNode(int _id, int _m){
			id = _id;
			m = _m;
		}

		@Override
		public int compareTo(TableNode o) {
			return Integer.compare(o.m, m);
			//здесь я поставил аргументы наоборот т.к. нужна сортивка по убыванию
		}

		@Override
		public String toString() {
			return id + " " + m;
		}
	}
	static StreamTokenizer scanner;
	static int nextInt() throws IOException{
		scanner.nextToken();
		return (int)scanner.nval;
	}

    public static void main(String[] args) throws IOException{
		List<TableNode> table;
		scanner = new StreamTokenizer(System.in);
		final int n = nextInt();
		// <> означает что параметр будет такой же какой указан в типе переменной
		table = new ArrayList<>(n);
		for (int i = 0; i < n; ++i)
			table.add(new TableNode(nextInt(), nextInt()));
		Collections.sort(table);
			for(TableNode node: table)
				System.out.println(node);
    }
}
```

Далее решение со `Scanner` (которое я сначала писал для файлов) оно достаточно лаконичное, выразительное, но не прошло из-за времени - там стояло ограничение на секунду. Сначала я подумал, что это такая загвоздка задачи, что речь идет не тупо про сортировку, но видимо именно так.

```java
public class Main {
	//реализуем Comparable чтобы можно было сравнивать в стандартных алгоритмах
	static class TableNode implements Comparable<TableNode>{
		private final int id, m; // так эти поля названы в условии задачи
		public TableNode(int _id, int _m){
			id = _id;
			m = _m;
		}

		@Override public int compareTo(TableNode o) {
			return Integer.compare(o.m, m);
			//здесь я поставил аргументы наоборот т.к. нужна сортивка по убыванию
		}

		@Override public String toString() {
		// так проще с выводом
			return id + " " + m;
		}
	}

    public static void main(String[] args) throws IOException{
		List<TableNode> table;
		try(Scanner scanner = new Scanner(new File("input.txt"))){
			final int n = scanner.nextInt();
			// <> означает что параметр будет такой же какой указан в типе переменной
			table = new ArrayList<>(n);
			for (int i = 0; i < n; ++i)
				table.add(new TableNode(scanner.nextInt(), scanner.nextInt()));
		}
		Collections.sort(table);
		try(PrintWriter out = new PrintWriter(new FileWriter("output.txt"))){
			for(TableNode node: table)
				out.println(node);
		}
    }
}
```


#RandomAccessFile

Класс для работы с файлами произвольного доступа. Никак не вписывается в иерархии упомянутые выше - наслудется от Object. Позволяет произвольно ездить по файлу в любое положение и оттуда читать\записывать. В конструкторе имеет второй аргумент, который указывает что следует открыть файл на чтение `"r"` либо на чтение и запись `"rw"`. Рекомендую по возможности использовать другие классы. Здесь подробно на нём останавливаться не буду. 


#Java 8 Stream

В джава 8 добавили много вещей из функционального мира. В частности начиная с лиспа (то есть очень давно) появились обобщённые операции над списками. `map` для применения функции к каждому элементу списка (отображение на список), `filter` для отображения булефвой функции на список, чтобы отфильтровать; `fold` или `reduce` для применения бинарной функции, что-то вроде `map` с накоплением. На основе `fold` можно просуммировать весь список, перемножить или отсортировать. Кажется раньше это называли потоками, но я нигде этого сам не видел. И подобно тому как C# назвал классы связанные с этими списковыми функциональными комбинаторами LINQ  так и разработчики java решили сделать отдельное слово и назвали stream. 

Всё это написано здесь чтобы не сбились с толку когда гуглили `java bla bla bla stream` и наткнулись на статью с фичами java 8 вместо операций ввода вывода. Неудачное они выбрали слово вообщем.

Ну и в конце небольшой пример что это представляет
```java
List<String> myList =
    Arrays.asList("a1", "a2", "b1", "c2", "c1");

myList
    .stream() //приводим список к типу stream
    .filter(s -> s.startsWith("c")) //фильтруем: внутри лямбда функция принимающая элемент списка и возвращающая
                                    //boolean  и если он false то элемент удаяется из списка
    .map(String::toUpperCase)  //подаётся функция (приводящая символы строки к верхнему регистру) и примняется 
                               //каждому элементу списка
    .sorted() //сортируется
    .forEach(System.out::println); //выводит построчно каждый элементы (почти map)
```
Взял отсюда http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/ там посложнее дальше идут вещи. Ещё в stream делается упор на паралельность операций.

#Задания (ещё не все)

## Zip архиватор
Почитать [вот здесь](http://wikijava.org.ua/index.php?title=%D0%93%D0%BB%D0%B0%D0%B2%D0%B0_16_Thinking_in_Java_4th_edition#.D0.9C.D0.BD.D0.BE.D0.B3.D0.BE.D1.84.D0.B0.D0.B9.D0.BB.D0.BE.D0.B2.D1.8B.D0.B5_.D0.B0.D1.80.D1.85.D0.B8.D0.B2.D1.8B_ZIP) и сделать zip архиватор. Это должно быть просто, т.к. пример достаточно подробный. Но сложно потому что сложновато со всем разобраться. Допустим программе на вход подаётся папка первым параметром и название выходного файла вторым параметром. 
