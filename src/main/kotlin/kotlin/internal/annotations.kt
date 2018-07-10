package kotlin.internal

/*
참고 - https://discuss.kotlinlang.org/t/how-prevent-type-inference/8140
참고 - https://youtrack.jetbrains.com/issue/KT-13198
KT-13198 이슈가 해결되면 Workaround 가 필요 없다.
*/
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class Exact
