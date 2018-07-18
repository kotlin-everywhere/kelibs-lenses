package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.lenses.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestLens {
    @Test
    fun testSet() {
        data class Person(val name: String, val age: Int)
        data class People(val john: Person, val tom: Person)

        val people = People(Person("John", 20), Person("tom", 21))

        val byCopy = people.copy(tom = people.tom.copy(age = 22))

        // set tom's age to 22
        val bySet1 = People::tom from people set people.tom.copy(age = 22)
        assertEquals(byCopy, bySet1)

        val bySet2 = People::tom + Person::age from people set 22
        assertEquals(byCopy, bySet2)
    }

    @Test
    fun testMap() {
        data class Person(val name: String, val age: Int)
        data class People(val john: Person, val tom: Person)

        val people = People(Person("John", 20), Person("tom", 21))

        val byCopy = people.copy(tom = people.tom.copy(age = 22))

        // map tom's age to 22
        val byMap1 = People::tom from people map { it.copy(age = 22) }
        assertEquals(byCopy, byMap1)

        val byMap2 = People::tom + Person::age from people map { 22 }
        assertEquals(byCopy, byMap2)
    }

    private interface HasName {
        val name: String
    }

    @Test
    fun testReflectCopy() {
        data class Person(override val name: String, val age: Int) : HasName

        val john = Person("john", 21)
        val copyJohn = john.copy(age = 22)
        val reflectCopyJohn = john.reflectCopy("age", 22)
        assertEquals(reflectCopyJohn, copyJohn)

        val tom: HasName = Person("Tom", 21)
        val reflectCopyTom = tom.reflectCopy(HasName::name, "Tim")
        val copyTom = (tom as Person).copy(name = "Tim")
        assertEquals(reflectCopyTom, copyTom)

        val jane = Person("Jane", 21)
        val copyJane = jane.copy(age = 22)
        val reflectCopyJane = jane.reflectCopy(jane::age, 22)
        assertEquals(reflectCopyJane, copyJane)
    }
}