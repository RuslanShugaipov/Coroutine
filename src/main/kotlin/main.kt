import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val job = launch {
        try {
            repeat(3) { i ->
                delay(1000L)
                println("I'm sleeping $i ...")
            }

            delay(1000L)
            printSomething()

            delay(2000L)
            //Функции вызываются последовательно, поэтому сначала пройдет задержка
            //в 1 секунду у функции numA(), потом у numB(), только потом выводится
            //результат сложения
            println("\nSequential invocation:")
            val timeSequent = measureTimeMillis {
                val a: Float = numA()
                val b: Float = numB()
                println("a + b = ${a + b}")
            }
            println("Completed in $timeSequent ms")

            //Функции вызываются асинхронно, поэтому задержка в 1 секунду
            //у функций numA() и numB() завершится практически одновременно,
            //после чего выводится результат с применением функции await()
            println("\nAsynchronous invocation:")
            val timeAsync = measureTimeMillis {
                val a: Deferred<Float> = async { numA() }
                val b: Deferred<Float> = async { numB() }
                println("a + b = ${a.await() + b.await()}")
            }
            println("Completed in $timeAsync ms")
        } finally {
            delay(1000L)
            println("\nI'm running finally")
        }
    }
    delay(4000L)
    println("main: I'm tired of waiting!\n")
    delay(2000L)
    println("Hello,")
    job.join()
    println("\nmain: Now I can quit.")
}

suspend fun printSomething() {
    delay(1000L)
    println("World")
}

suspend fun numA(): Float {
    delay(1000L)
    return 0.5f
}

suspend fun numB(): Float {
    delay(1000L)
    return 0.5f
}