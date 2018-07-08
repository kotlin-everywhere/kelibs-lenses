package com.minek.kotlin.everywhere.lenses

import kotlin.reflect.*
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions

data class MergedProperty<T : Any, U : Any, V>(private val a: KProperty1<T, U>, private val b: KProperty1<U, V>) : KProperty1<T, V> {
    override fun invoke(p1: T): V = b(a(p1))

    override val annotations: List<Annotation>
        get() = error("not implemented")
    override val getter: KProperty1.Getter<T, V>
        get() = error("not implemented")
    override val isAbstract: Boolean
        get() = error("not implemented")
    override val isConst: Boolean
        get() = error("not implemented")
    override val isFinal: Boolean
        get() = error("not implemented")
    override val isLateinit: Boolean
        get() = error("not implemented")
    override val isOpen: Boolean
        get() = error("not implemented")
    override val name: String
        get() = error("not implemented")
    override val parameters: List<KParameter>
        get() = error("not implemented")
    override val returnType: KType
        get() = error("not implemented")
    override val typeParameters: List<KTypeParameter>
        get() = error("not implemented")
    override val visibility: KVisibility?
        get() = error("not implemented")

    override fun call(vararg args: Any?): V = error("not implemented")

    override fun callBy(args: Map<KParameter, Any?>): V = error("not implemented")

    override fun get(receiver: T): V = error("not implemented")

    override fun getDelegate(receiver: T): Any? = error("not implemented")

    fun set(from: T, value: V): T {
        return set(from, a, set(a(from), b, value))
    }
}

private fun <T : Any, R> set(from: T, property: KProperty1<T, R>, value: R): T {
    return when (property) {
        is MergedProperty<T, *, R> -> property.set(from, value)
        else -> {
            val copy = from::class.memberFunctions.first { it.name == "copy" }
            val parameter = copy.parameters.first { it.name == property.name }
            @Suppress("UNCHECKED_CAST")
            copy.callBy(mapOf(copy.instanceParameter!! to from, parameter to value)) as T
        }
    }
}

data class Lens<T : Any, U>(val property: KProperty1<T, U>, val from: T)

infix fun <T : Any, U> Lens<T, U>.set(value: U): T = set(from, property, value)
infix fun <T : Any, U> Lens<T, U>.map(mapper: (U) -> U): T = set(mapper(property(from)))

infix operator fun <T : Any, U : Any, V> KProperty1<T, U>.plus(property: KProperty1<U, V>): KProperty1<T, V> = MergedProperty(this, property)

infix fun <T : Any, R> KProperty1<T, R>.from(from: T): Lens<T, R> = Lens(this, from)