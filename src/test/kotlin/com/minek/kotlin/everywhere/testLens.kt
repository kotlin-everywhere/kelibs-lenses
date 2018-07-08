package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.lenses.from
import com.minek.kotlin.everywhere.lenses.map
import com.minek.kotlin.everywhere.lenses.plus
import com.minek.kotlin.everywhere.lenses.set
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
}