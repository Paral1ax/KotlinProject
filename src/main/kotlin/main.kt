import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    val field=Field()
    rulesOfTheGame()
    println("Введите количество игроков")
    var count: Int
    do {
        count= readLine()?.toInt() ?: -1
    }while (count==-1)
    val array=ArrayList<Player>()
    for (i:Int in 1..count){
        array.add(Player(12000.0,0,i))
    }
    while (array.size>1){
        for (i in array){
            if (i.bankrupt) array.remove(i)
            else i.currentPlayerMakesMove(field)
            if (array.size==1) break
        }
    }
    println("Победил игрок ${array.first().whichPlayer}")
}
fun rulesOfTheGame(){
    println("ВСЕ ИГРОКИ НАЧИНАЮТ С КЛЕТКИ 0\n" +
            "У КАЖДОГО ИГРОКА В НАЧАЛЕ НА СЧЕТУ 12 МЛН РУБЛЕЙ\n" +
            "ИГРОВОЕ ПОЛЕ ЗАНИМАЕТ 40 КЛЕТОК\n" +
            "ВСЕ ИГРОКИ БРОСАЮТ КУБИКИ ПО ОЧЕРЕДИ НАЧИНАЯ С ПЕРВОГО ИГРОКА\n" +
            "ИГРОКИ МОГУТ ПОКУПАТЬ НЕДВИЖИМОСТЬ НА ЕЩЕ НЕ КУПЛЕННЫХ КЛЕТКАХ МАГАЗИНОВ\n" +
            "КОГДА У ИГРОКА НА СЧЕТЕ ОСТАЕТСЯ МЕНЬШЕ ИЛИ РОВНО 0 ТЫСЯЧ РУБЛЕЙ ИГРОК ВЫБЫВАЕТ ИЗ ИГРЫ\n" +
            "УДАЧНОЙ ИГРЫ" +
            "\n///////////////////////////////////////////////")
}
