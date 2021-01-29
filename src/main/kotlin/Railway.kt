import com.company.Cell.Cell

class Railway(coordinate:Int,var cost:Double,var payment:Double,name:String,val utility:Boolean):Cell(coordinate,name) {
    var owner:Player?=null
    override fun Display() {

    }
}