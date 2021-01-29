import com.company.Cell.Cell
import kotlin.random.Random

class Player(var money:Double, var coordinate:Int,var whichPlayer:Int) {
    private var listOfCards:MutableList<Cell> = ArrayList<Cell>()
    var imprisoned=0
    var currentThrow = 0
    var bankrupt=false
    private fun throwCubes(): Int {
        return Random.nextInt(1, 7) + Random.nextInt(1, 7)
    }

    fun currentPlayerMakesMove(field: Field){
        println("Ход игрока $whichPlayer")
        println("------------------------------------------------------------------------------------------")
        if (money<=0) {
            bankrupt=true
            println("Извините, но вы банкрот, вы проиграли!")
            return
        }
        if (imprisoned>0){
            println("Вы еще сидите в тюрьме")
            imprisoned--
            return
        }
        println(this.toString())
        println("Если хотите посмотреть свою недвижимость введите 'Y' или 'y' иначе введите что угодно!")
        val input= readLine().toString()
        if (input=="Y"||input=="y")
            if (listOfCards.isEmpty())
                println("Вы еще ничего не купили")
            else for (i in listOfCards)
                println(i.toString())
        println("Нажмите любую клавишу чтобы кинуть кубики")
        readLine()
        currentThrow = throwCubes()
        var oldCoordinate=this.coordinate
        this.coordinate=(this.coordinate+currentThrow)%field.size
        println("Игрок кидает кубики на сумму $currentThrow и оказывается на клетке ${this.coordinate}")
        onCell(field.field.first{cell -> cell.coordinate==this.coordinate })
        if (oldCoordinate>this.coordinate){
            println("Вы прошли круг, вы получаете 2 млн рублей")
        }
        println("------------------------------------------------------------------------------------------")
    }

    private fun onCell(cell: Cell) {
        when (cell) {
            is Shop -> isShop(cell)
            is Railway -> isRailway(cell)
            is Treasury -> isTreasury(cell)
            is Basic -> isBasic(cell)
            is Penalty->isPenalty(cell)
        }
    }

    private fun isShop(cell: Shop) {
        when (cell.owner) {
            null -> {
                println("Игрок $whichPlayer может купить ${cell.name} за ${cell.shopCost} тысяч рублей")
                println("Если хотите купить клетку введите 'Y' или 'y' иначе введите что угодно!")
                var input = readLine().toString()
                if ((input=="Y"||input=="y")&&this.money>=cell.shopCost){
                    this.money-=cell.shopCost
                    this.listOfCards.add(cell)
                    cell.owner=this
                    println("Поздравляю, вы купили ${cell.name} на клетке ${cell.coordinate} за ${cell.shopCost} тысяч рублей")
                }
                else println("Увы, но вы не купили этот магазин")
            }
            this -> {
                println("Игрок $whichPlayer может улучшить ${cell.name} за ${cell.shopImprovement} тысяч рублей")
                println("Если хотите улучшить магазин введите 'да' иначе введите что угодно!")
                var input = readLine().toString()
                if ((input=="Y"||input=="y")&&this.money>=cell.shopImprovement&&cell.lvl<6){
                    this.money-=cell.shopImprovement
                    cell.raiseCostByLvl()
                    println("Поздравляю, вы улучшили ${cell.name} на клетке ${cell.coordinate} за ${cell.shopImprovement} тысяч рублей и теперь там стоит ${cell.lvl} домов")
                }
                else println("Не удалось улучшить магазин")
            }
            else -> {
                println("Игрок $whichPlayer должен заплатить игроку ${cell.owner!!.whichPlayer} ${cell.shopCompensation} тысяч рублей за нахождение на ${cell.name}")
                this.money-=cell.shopCompensation
                cell.owner!!.money+=cell.shopCompensation
            }
        }
    }
    private fun isRailway(cell: Railway) {
        when (cell.owner) {
            null -> {
                println("Игрок $whichPlayer может купить ${cell.name} за ${cell.cost} тысяч рублей")
                println("Если хотите купить клетку введите 'Y' иначе введите что угодно!")
                var input = readLine().toString()
                if ((input=="Y"||input=="y")&&this.money>=cell.cost){
                    this.money-=cell.cost
                    this.listOfCards.add(cell)
                    cell.owner=this
                    println("Поздравляю, вы купили ${cell.name} на клетке ${cell.coordinate} за ${cell.cost} тысяч рублей")
                }
                else println("Увы, но вы не купили это клетку")
            }
            this -> {
                println("Игрок $whichPlayer стоит на своей клетке")
            }
            else -> {
                if (cell.utility) {
                    var counter = 0
                    cell.owner!!.listOfCards.forEach { x -> if (x is Railway && x.utility) counter++ }
                    println("У игрока ${cell.owner!!.whichPlayer} присутствует $counter жкх карт")
                    if (counter==1) cell.payment=10.0*currentThrow*4
                    else if (counter==2) cell.payment=10.0*10*currentThrow
                    println("Игрок $whichPlayer должен заплатить игроку ${cell.owner!!.whichPlayer} ${cell.payment} тысяч рублей за нахождение на ${cell.name}")
                    this.money -= cell.payment
                    cell.owner!!.money += cell.payment
                } else {
                    var counter = 0
                    cell.owner!!.listOfCards.forEach { x: Cell -> if (x is Railway) counter++ }
                    println("У игрока ${cell.owner!!.whichPlayer} присутствует $counter карточки железных дорог")
                    println("Игрок $whichPlayer должен заплатить игроку ${cell.owner!!.whichPlayer} ${cell.payment * counter} тысяч рублей за нахождение на клетке ${cell.name}")
                    this.money -= cell.payment * counter
                    cell.owner!!.money += cell.payment * counter
                }
            }
        }
    }
    private fun isTreasury(cell: Treasury){
        val result=cell.list[Random.nextInt(0,cell.list.size)]
        println("Вы испытываете удачу в лотерее и получаете $result тысяч рублей")
        this.money+=result
    }
    private fun isBasic(cell:Basic){
        when(cell.which){
            1-> {
                println("Вы на сейчас на старте")
            }
            2-> println("Вы просто посетили тюрьму")
            3-> println("Вы отдыхаете на парковке")
            4-> {
                println("Ого, кажется вас сажают в тюрьму на 2 хода")
                this.coordinate=10
                imprisoned=2
            }
        }
    }
    private fun isPenalty(cell: Penalty){
        println("Вы попали на штрафную клетку ${cell.name} и должны заплатить ${cell.payment}")
        this.money-=cell.payment
    }
    override fun toString(): String {
        return "Игрок $whichPlayer имеет баланс $money тысяч рублей и стоит на клетке $coordinate"
    }

}