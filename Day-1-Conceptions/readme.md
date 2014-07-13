Концепции языка Java
==================

Источники: http://compscicenter.ru/sites/default/files/materials/java2012_intro.pdf http://www.helloworld.ru/texts/comp/lang/java/java/03.htm 

Java c 1991 года - можно считать язык относительно молодым, хотя разработчики старались его сделать как можно консервативнее.
До 2002 года нумерация была 1.0, 1.1 ... 1.4 В 2004 появилась Java 5.0 нумерация изменилась. В Java 5 вошло много изменений - в нашем курсе мы будем в основном ориентироваться на Java 5, хотя на работе вы можете встретить наследумый код который продолжает работать на более ранних версиях.

Виртуальная машина Java
-----------------

Java является высокоуровневым языком. В этом понятии нет ничего нового. Контрпример низкоуровневые языки - ассемблеры. Когда вы пишете на ассемблере вам приходиться постоянно помнить об архитектуре машины (в основном процессора) под которую вы пишете. Ближе к середине прошлого века разработчикам захотелось сделать языки более ориентированные на решение конкретных задач, нежели перемещению байтов между оперативной памятью и регистрами. С этим понятием связано другое - кроссплатформенность. То есть возможность создавать программы под разные платформы не меняя исходный файл. Так появлялись алгол фортран и т.д.

Java является интерпритируемым языком. Контрпример компилируемые языки - си, паскаль. В компилируемом языке после описания программы на заданном языке вам необходимо запустить специальную программу-сборщик (компилятор). Она проверит ваш код на наличие синтаксических ошибок, попытается составить необходимое ей представление (изначально ваш код - одна огромная строка, что во многих случаях неудобно обрабатывать). Дальше компилятор попытается представить программу в виде процессорных команд для целевой архитектуры (зачастую ваша целевая архитектура и архитектура пк за которым вы работаете будут совпадать и скорее всего это будет x86-64). Вот вы получили необходимую программу, например это будет exe файл под windows. Теперь вы сможете её запустить также как запускали компилятор и и команда одна за другой будут исполняться непосредственно процессором.

У интерпритируемых языков в простейшем случае нам достаточно подать наш исходный код на вход программе интепретатору. Она будет считывать программу построчно (ну то есть по  одной функции\выражению оператору) и стараться выполнять сразу же. Не знаю точно, но наверное очень мало интерпретаторов так работает - это очень не эффективно. 
Подход который используется в java - наш исходный код мы подаём компилятору javac (который в составе jdk). Только теперь компилируются не команды процессора, а байт-код промежуточное представление для интерпретатора. Выглядят они одинаково если открыть файл блокнотом, но байт-код не исполняется процессором. Полученный файл мы можем открыть на любом устройстве (со многими оговорками) с помощью утилиты java (которая в составе jre). Утилита начнёт испольнять команды одна за другой.

Получая универсальность мы жертвуем производительностью. В среднем на одну команду байт-кода нам требуется 15-20 процессорных команд. Поэтому на java зачастую сетуют, что она медленная. Начиная с java 1.3 в jre используется Hotspot. Запуская программу на java собирается статистика по использованию участков кода - те участки которые требовательны к ресурсам и запускаются часто, проходятся jit (Just-In-Time) компилятором. Он берет байт-код java и транслирует его в байт-код процессора на котором работает. Получается всего в 2 раза медленне программ на С/C++. Во многих случаях скорость не главный показатель - иначе бы все программирвоали на ассемблере.

Следует отличать jre (java runtime environment) от jdk (java development kit). Первое необходимо чтобы запускать программы на java. Второе чтобы их собирать. Вам пригодится и то и другое. 

###Существуют следующие редакции поставки jdk:

1. Java SE (Standart Edition) - java для настольных приложений (в освном будем использовать её).
2. Java ME (Micro Edition) - для встраиваемых приложений. Можно найти на старых телефонах игры на java.
3. Java EE (Enterprise Edition) - как SE только с набором дополнительных библиотек. 
4. Java Card - урезанная SE для банковских карт (звучит странно да).

Существуют разные реализации java от разных команий Oracle, IBM, HP. Самая популярная от Oracle (которая унаследовала её от Sun), OpenJDK (открытая реализация). Отдельно есть Dalvic для андроид, которая работает немного иначе стандартных реализаций.

Управление памятью
------------------

В java вы будете иметь дело с автоматическим управлением памятью. Изначально языки создавались с ручным управлением - так естественннее. В С/C++ вам придётся выделять память и в нужном месте освобождать её. Если память не освободить и программа закроется, то ничего страшного по сути не произойдёт - операционная система проследит за тем чтобы пометить всю память после вашей программы как свободную. Если программа работает долго и память не высвобождается (это называется утечки памяти) то вскоре её перестанет хватать и ничем хорошим это не закончится. По возможности программист старается освобождать не используемую память. Но тут возникают две ошибки - возможно память нельзя назвать неиспользуемой и где-нибудь мы ей воспользуемся (постараемся прочитать - и получим мусор либо постараемся присвоить) и мы можем случайно второй раз освободить память (удалить объект). Вообще говоря это undefined behavior (неопределенное поведение) но скорей всего вы увидите окошко segmentation fault и ваша программа рухнет.

Есть механизмы чтобы облегчить жизнь c++ программистам - полуавтоматическое управление памятью на основе подсчета ссылок. Конечно мозг в таком случае отключать не приходится - всё равно необходимо знать в какой момент освобождать память (и вообще не всегда можно пользоваться подсчетом ссылок), но зато вы уменьшаете риск всяких непредвиденных ситуаций вроде segmentation fault. 

Итак в java вам не придется думать об управлении памятью. Сборщик мусора (garbage collector) сам найдёт неиспользуемые объекты и удалит. Это одна из причин по которой java выбран в качестве языка для обучения программированию. Ну тут вообще есть два мнения: первый язык должен быть наиболее простым, чтобы объяснить основы, разные алгоритмы и чтобы это не превратилось в скучную рутину. Либо наиболее строгим, чтобы потом программиста уже было не удивить (ведь вам скорей всего придется ещё столкнуться с С++ ассемблером). Мы склонялись скорее ко второму мнению когда остановились на джава (см. ниже строгая статическая типизация), но с управлением памятью вы так и не будете иметь дело. Проблема в том что из популярных языков с ручным управлением памяти есть C/C++. С простой и хороший язык, всмысле хорошо подходит под определенный круг задач. Но на нём нет многих высокоуровневых конструкций и вообще вам затруднительно будет писать какие-либо наглядные приложения, также большая проблема что если вы будете искать что-то про си то результаты поиска будут перекрываться с++. С++ язык с большим количеством разных особенностей, язык очень гибкий и позволяет программисту очень многое, что в конечном итоге приносит ему вред. Чтобы пользоваться им в компаниях вводят свои стандарты которые ограничивают программиста (можете почитать например **поискать** google code guide). Также под линуксом и виндоус есть свои особенности компиляторов для обоих языков. Таким образом (суммируя) если студент спросит преподавателя "где ошибка?" - вопрос скорей всего останется без ответа, а преподаватель узнает для себя какую-нибудь новую особенность языка. Вообще говоря здесь в хорошем свете предстаёт паскаль. Правда он вам наверное уже успел\успеет наскучить.

Насчёт самого сборщика мусора - идея совсем не нова. Уже в начале 60х появился язык Lisp в котором необходим сборщик мусора (возможно он появился и ранее). Основной недостаток сборки мусора в уменьшении производительности. Вообще говоря большая часть работы вашей программы будет уходить на работу с памятью. Это как бы дорого обходиться процессору обратиться в оперативную память (на жесткий диск обратить гораздо дольше). Итак в языке с ручным управлением памятью вы запрашиваете операционную системы вам выделить память и запрашиваете её освободить **(подразобраться вот тут. может я и ошибаюсь?)** это идёт относительно долго но быстрее тут никак не получиться. В полуавтоматической организации управления памятью у вас будут накладные расходы на десяток процессорных операций - пустяк по сравнению с обращением к памяти. Если у вас сборщик мусора, то изначально у вас есть большая выделенная область памяти затем она заканчивается и запускается сборка мусора. Вся ваша программа в этот момент останавливается сборщик находит все используемые ("живые") объекты - выделяет новую большую область в оперативной памяти равную нынешней либо большую если нынешней не хватает. Перетаскивает живые объекты в новую область и освобождает старую область. Естественно это на порядок дольше чем ручное управление памятью. Это ещё одна причина по которой java называют медленной. Сейчас конечо алгоритмы стали лучше (я описал простейший) но от потери производительности никуда не уйдёшь.

Такое решение также не подходит для систем реального времени. Это очень узкая задача с которой вы возможно так и не столкнётесь. Там требуется от программы выполнение операций за фиксированное время (пускай и долгое, но одинаковые операции выполняются за одинаковое время). Время работы сборщика мусора, как и время в которое начнётся сборка есть случайная величина.

Язык java
-----------
Также следует отличать виртуальную машину java (jre), которая исполняет программы на своём байт-коде, от языка java. Это важно потому что есть множество языков JRuby, Jython, Groovy, Clojure (lisp для java). Среди них в java сообществе набрал некоторую популярность scala (остальные не имеют особого интереса) - похож на java но перенял много идей из функциональных языков вроде ML, Haskell; имеет много новых выразительных конструкций, перегрузку операторов (которой нет в java). Также возможно использовать язык java под другими платформами например .net от майкрософт. Хотя язык java в отрыве от своей платформы не представляет особого интереса. 

Java является структурным языком. Говорят, что когда-то были и другие языки. Вроде был раньше Basic где все писалось подряд и были безусловные переходы (GOTO) заместо процедур. И ещё в нём строки нумеравались 10, 20, 30... Ещё есть ассемблер - тоже не структурный код. Дейкстра в то время писал про то, что от GOTO следует избавиться **уточнить где писал?**. 

Java является императивным языком. Контрпример - функциональные языки. В императивных вы имеете дело с изменением состаяния вашей программы. Вы можете вспомнить как в школе на уроках математики вы упрощали выражение, но оно не менялось а оставалось тем же - его можно вернуть в исходное состаяние. В системе уравнений вы находите корни - значения переменных при которых уравнение истинно. При это никакого изменения состояния нет. Иногда вы выполняете преобразования при которых теряется часть корней, но вы выполняете их с оговоркой, что чтобы восстановить новое выражение до предыдущего требуется дополнить каким-то ещё выражением (*как-то так*). То есть в таком случае вы не преобразуете выражаение а вводите новое (которое является частью предыдущего), нахождение корней которого поможет вам найти корни предыдущего **привести пример**. В java если вы пишете (псевдоязык как пример): 

    x = 5
    
Не означает, что в любом месте в программы вы сможете подставить 5 заместо х и программа останется незимененной. Это означает, что в текущем состоянии программы когда интерпретатор доходит до этой строки становится верным, что х = 5. На следующих строках состояние меняется и это утверждение может стать неверным.

    x = x + 5
    
Такое утверждение также корректно для императивных языков программирования: оно означает взять `х` из предыдущего состояния, прибавить к нему пять, записать получившееся значение как `х` и сохранить состояние. Но будь это математическая запись утверждение было бы просто некорректным (`0 = 5` *может где-нибудь в других математиках такое допустимо и даже имеет смысл?*). Вообще с моей стороны некорректно сравнивать математическую нотацию в которой знак `=` обозначает эквивалентность, равенство и в Java `=` обозначает опертор присваивания. Когда-то в языках чтобы не путать делали оператор `:=` или `<-` таким образом видно, что одно другому вообще говоря не равно, и также можно видеть что присваивание работает справа налево. Но в 80х начал набирать популярность язык си, который изменил тенденцию и теперь во многих языках символ равенства обозначает присваивания. В этом нет большого  преступления - программисты понимают что творится в коде, а знак = выглядит компактнее и вроде приятнее.

Здесь я немного залезаю в детали синтаксиса языка. Сейчас вам это не важно - просто хочу сказать что есть ещё языки поддерживающие функциональную парадигму. Где равенство обозначает равенство и программы составляются через композиции функций. Полностью программу составить в функциональном стиле нельзя - нам так или иначе придется взаимодействовать с внешней средой, читать файлы, реагировать на ввод, это потребует изменять состояние программы. Впервые такой подход использовался в лиспе (очень давно), хотя на нём можно полностью программировать императивно, язык не вносит ограничений (как например хаскель, где программиста принуждают). Также язык обладает некоторой лаконичностью и вообще вам стоит с ним ознакомиться. Лучше всего почитать **SICP** на русском.

###Типизация

По поводу сильной\слабой типизации:

> Therefore: I give the following general definitions for strong and weak typing, at least when used as absolutes: 
> + Strong typing: A type system that I like and feel comfortable with
> + Weak typing: A type system that worries me, or makes me feel uncomfortable

<div align="right" style="width:100%;">
[*вычитал в одном блоге (англ.)*](http://blogs.perl.org/users/ovid/2010/08/what-to-know-before-debating-type-systems.html)
</div>

Вот небольшой список языков в зависимости от типизации [википедия в качестве пруфа](http://en.wikipedia.org/wiki/Comparison_of_type_systems). На самом деле это спорно таким образом разделять языки. Java иногда может быть динамической. С# может быть статически типизирован, динамически (даже есть ключевое слово dynamic), и может быть вообще небезопасным как C.

|                           | static (статическая) | dynamic (динамическая)| (None) Безтиповая |
|:--------------------------|:--------------------:|:---------------------:|:-----------------:|
|**safe (безопасная)**      |Haskell, Pascal, Java, C#| Lisp, SmallTalk, Python, Ruby | Assembly |
|**unsafe (небезопасная)**  | C, C++ | Objective-C | Brainfuck |

###ООП

