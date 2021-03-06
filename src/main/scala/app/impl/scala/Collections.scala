package app.impl.scala

import app.NumberInterface
import app.impl.Generic
import org.junit.Test

import scala.collection.immutable.HashMap


/**
  * Some examples about how to use collections in scala
  */
class Collections extends Generic with NumberInterface {


  /**
    * Slice function will get the collection and it will return a new collection
    * With the elements specify in te from to
    * shall print
    *
    * 6
    * 7
    * 8
    * 9
    */
  @Test def slice(): Unit = {
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    list.slice(4, 8) foreach (e => {
      println(list(e))
    })
  }

  /**
    * Drop function will drop from the collection the number of items we specify,
    * and it will return a new collection
    *
    */
  @Test def drop(): Unit = {
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val newList = list.drop(5)
    newList foreach (e => {
      println(newList.get[Int](e))
    })
  }

  implicit class listUtils[V](list: List[V]) {
    def get[T](value: T): V = list.filter(v => v.equals(value)).head
  }

  /**
    * To replace an element in a collection we can just map to pass a function where we specify
    * from which value to which we want to replace
    */
  @Test def replace(): Unit = {
    val a1 = new A(1)
    val a2 = new A(2)
    val a3 = new A(3)
    val a4 = new A(4)

    val a20 = new A(20)
    val a40 = new A(40)

    var list = List(a1, a2, a3, a4)
    list = list.map(a2 => a20)
    list.foreach(a => println(a.a))
  }

  class A(val a: Int) {}


  /**
    * How to create and iterate a Map collection
    */
  @Test def iterateMap(): Unit = {
    println(defaultImpl())
    val numberMap = HashMap[String, Int]("1" -> 1, "2" -> 2, "3" -> 3)

    val sumMap = numberMap
      .map(entry => {
        println(numberMap(entry._1))
        entry._2
      })
      .filter(n => isHigherThan1(n))
      .sum
    println(sumMap)
  }

  /**
    * How to revert a map from key/value to value/key using scan
    */
  @Test def revertMap(): Unit = {
    val numberMap = HashMap[Int, Int](1 -> 2, 3 -> 4, 5 -> 6)
    val revertedMap = numberMap
      .map(entry => Map[Int, Int](entry._2 -> entry._1))
      .scan(HashMap())((map1, map2) => map1 ++ map2).last
    println(revertedMap)
  }


  /**
    * We a map with int/list pair and we revert as key for every item in the collection,
    * and we set the value as the current key
    */
  @Test def revertMapList(): Unit = {
    val listMap = HashMap[Int, List[Int]](1 -> List(3, 4), 4 -> List(5, 6))
    val revertedMap = listMap
      .flatMap(map => map._2
        .map(entry => Map[Int, Int](entry -> map._1))
        .scan(HashMap())((map1, map2) => map1 ++ map2))
      .scan(HashMap())((m, m1) => m ++ m1).last
    println(revertedMap)
  }

  /**
    * Iterate over a collection emit the items create a new string items and return a new collection
    */
  @Test def intToStringList(): Unit = {
    val list = List(1, 2, 3).toStream
      .map(entry => String.valueOf(entry))
      .toList
    print(list)
  }

  /**
    * Iterate over a collection, emit the items, create a new one through the previous values,
    * and return a new collection with this new values
    */
  @Test def intToNewIntList(): Unit = {
    val list = List(1, 2, 3)
      .flatMap(entry => List(entry * 100))
    print(list)
  }

  /**
    * How to create merge to list
    */
  @Test def appendList(): Unit = {
    val list = List(1, 2, 3)
    val list2 = List(4, 5, 6)
    val totalList = list ++ list2
    print(totalList)
  }

  /**
    * Add a value on list
    */
  @Test def addValue(): Unit = {
    val list = List(1, 2, 3)
    var list1 = list.::(4)
    list1 = 5 +: list1
    print(list1)
  }


  /**
    * Scan operator allow us in an iterator get the previous emitted item and the new one and do an operation.
    * We need to provide an initial value for the scan, and as second argument a bi function where we priovide
    * the previous item emitted and the new one
    */
  @Test def scanIntegers(): Unit = {
    print(List(1, 2, 3).scan(0)(sumIntegerFunction)
    )
  }

  def sumIntegerFunction: (Int, Int) => Int = {
    (previousItem, newItem) => previousItem + newItem
  }

  /**
    * In this case we define as initial value an empty list, then in the function mergeList we merge the previous list
    * and the new one. Finally we just return the last item emitted.
    */
  @Test def scanList(): Unit = {
    val newList = List(1, 2, 3)
      .map(n => List(n * 100))
      .scan(List())(mergeList).last
    println(newList)
  }

  /**
    * This exmaple compare two lists and return a new list with the only the values that exist in both collections
    */
  @Test def listEqualThanList(): Unit = {
    val listA = List(1, 3, 5)
    val listEqualThanA = List(1, 4, 6, 5, 3)
      .map(n => listA
        .filter(n1 => n1 == n))
      .scan(List())((l, l1) => l ++ l1)
      .last
    println(listEqualThanA)
  }

  @Test def sumValuesAsKey(): Unit = {
    val mapList = Map[String, List[Int]]("1" -> List(1, 3, 4, 5), "2" -> List(3, 5, 7, 8), "3" -> List(1, 2, 4, 5))
    val revertedMap = mapList
      .map(entry => Map[Int, String](entry._2.sum -> entry._1))
      .scan(HashMap())((m, m1) => m ++ m1).last
    println(revertedMap)
  }

  @Test def createListThroughArray(): Unit = {
    val list = Array[Int](1, 2, 3, 4, 5).toList
    println(list)
  }

  @Test def createMapThroughMap(): Unit = {
    val map = Map[Int, Int](1 -> 2, 2 -> 3, 3 -> 4)
    val map1 = Map[Int, Int]() ++ map
    println(map1)
  }

  @Test def concateniations(): Unit = {
    val list = List(1, 2, 3)
    print(list ::: List(4, 5, 6))
  }

  @Test def tranversable(): Unit = {
    println(List(1, 2, 3, 4, 5)
      .takeWhile(n => n < 3))

    println(List(1, 2, 3, 4, 5)
      .map(n => n * 100)
      .filter(n => n < 400))

    println(List(1, 2, 3, 4, 5)
      .map(n => n * 100)
      .filterNot(n => n < 400)
      .last)

    //Forall
    println(List(1, 2, 3, 4, 5)
      .map(n => n * 100)
      .forall(n => n < 400))

    println(List(1, 2, 3, 4, 5)
      .map(n => n * 100)
      .forall(n => n > 1))

    //Find
    println(List(1, 2, 3, 4, 5)
      .find(n => n == 3))

  }

  def mergeList(prevResult: List[Int], currentItem: List[Int]): List[Int] = {
    prevResult ++ currentItem
  }


  override def isHigherThan1(num: Int): Boolean = {
    num > 1
  }


  /**
    * Format style for foreach loop + plus use of interpolation
    */
  @Test def foreach(): Unit = {
    List(1, 2, 3, 4, 5) foreach { element =>
      println(element)
    }

    Map(1 -> "one", 2 -> "two", 3 -> "three") foreach { entry =>
      println(s"key:${entry._1} --> val:${entry._2}")
    }
  }

  @Test def range(): Unit = {
    val x = 0
    val list = List(1, 2, 3, 4, 5)
    x until list.size foreach (e => {
      println(list.apply(e))
    })
  }

}



