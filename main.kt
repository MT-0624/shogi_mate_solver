fun main(){
    val b = Board()

    b.readSfen("ln7/2k6/1ppp+P4/p8/9/9/9/9/K8 b BGN2rb3g4s2n3l13p 1")

    b.showAll()

    val mt = MateTree()
    // 第一引数のboolはどちらが詰ましに行くか
    // depth　調べる手数　
    println("探索中です")
    val ans = mt.mate(true, b, 7)

    if (ans == -1){
        println("詰みません")
    }
    else{
        println(ans.toString() + "手で詰みます")
    }
}