import com.company.Cell.Cell

class Shop(coordinate: Int,shopName:String,var shopCost:Double,var shopCompensation:Double,var shopImprovement:Double) : Cell(coordinate,shopName) {
    var lvl=1
    var owner: Player? =null
    var hotels=0
    val mean="SH"
    fun raiseCostByLvl(){
        lvl+=1
        when(lvl){
            0->shopCompensation*=1
            1->shopCompensation*=5
            2->shopCompensation*=3
            3->shopCompensation*=2.5
            4->shopCompensation*=1.4
            5->shopCompensation*=1.3
        }
    }
    override fun Display() {

    }
}