class Name {
    val piece_w = arrayOf(" ", "P", "L", "N", "S", "R", "B", "G", "P+", "L+", "N+", "S+", "R+", "B+", "K")
    val piece_b = arrayOf(" ", "p", "l", "n", "s", "r", "b", "g", "p+", "l+", "n+", "s+", "r+", "b+", "k")

    //0:" "
    // 1, "p"
    // 2, "l"
    // 3, "n"
    // 4, "s"
    // 5, "r"
    // 6, "b"
    // 7, "g"
    // 8, "p+"
    // 9, "l+"
    // 10, "n+"
    // 11, "s+"
    // 12, "r+"
    // 13, "b+"
    // 14, "k"

    val hand_w = arrayOf(" ", "P", "L", "N", "S", "R", "B", "G")
    val hand_b = arrayOf(" ", "p", "l", "n", "s", "r", "b", "g")

    //飛び道具でない駒の動き方
    val pawn = arrayOf(arrayOf(-1, 0))
    val knight = arrayOf(arrayOf(-2, 1), arrayOf(-2, -1))
    val silver = arrayOf(arrayOf(1, -1), arrayOf(1, 1), arrayOf(-1, 0), arrayOf(-1, -1), arrayOf(-1, 1))
    val gold = arrayOf(arrayOf(0, 1), arrayOf(0, -1), arrayOf(1, 0), arrayOf(-1, 0), arrayOf(-1, -1), arrayOf(-1, 1))
    val king = arrayOf(
        arrayOf(-1, -1), arrayOf(-1, 0), arrayOf(-1, 1)
        , arrayOf(0, 1), arrayOf(0, -1)
        , arrayOf(1, -1), arrayOf(1, 0), arrayOf(1, 1)
    )


    //飛び道具どうしよ…
    //香車はfor文で敵ごまがあればその場所も追加してbreak
    val bishopR: Array<Int> = arrayOf(-1, 1, 1, -1)
    val bishopC: Array<Int> = arrayOf(1, 1, -1, -1)

    val rookR: Array<Int> = arrayOf(-1, 0, 1, 0)
    val rookC: Array<Int> = arrayOf(0, 1, 0, -1)

    //香車はi 1->8{i*-1,0}
    //角はi 1->8{i*-1,i*-1}{i,i}{i,i*-1}{i*-1,i}
    //飛車はi 1->8{i*-1,0}{0,i}{0,i*-1}{i,0}

}

fun main() {

}







