# Применение паттерна Clock

Выполнил: студент группы М34371, Хлытин Григорий.

### Задание

Необходимо реализовать интерфейс `EventsStatistic`, который считает события, происходящие в системе. Реализация должна
хранить статистику ровно за последний час и подсчитывать, сколько событий каждого типа произошло в минуту. Интерфейс
`EventsStatistic`:

```java
public interface EventsStatistic {
    void incEvent(String name);

    double getEventStatisticByName(String name);

    Map<String, Double> getAllEventStatistic();

    void printStatistic();
}
```

+ `incEvent(String name)` - инкрементит число событий name;
+ `getEventStatisticByName(String name)` - выдает rpm (request per minute) события name за последний час;
+ `getAllEventStatistic()` - выдает rpm всех произошедших событий за прошедший час;
+ `printStatistic()` - выводит в консоль rpm всех произошедших событий;

Реализацию EventsStatistic необходимо покрыть тестами, используя паттерн Clock, рассмотренный на лекции. Тесты не должны
использовать sleep'ы и должны выполняться быстро.
