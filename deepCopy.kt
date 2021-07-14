
//Boardクラスオブジェクトを深くコピーする
fun deepCopy(board: Board):Board{
    val cp = Board()

    //盤面コピー
    for (i in 0..8){
        for (j in 0..8){
            cp.board[i][j] = board.board[i][j]
        }
    }

    //先手駒コピー
    for (i in 0..6){
        cp.handW[i] = board.handW[i]
    }
    //後手駒コピー
    for (i in 0..6){
        cp.handB[i] = board.handB[i]
    }


    return cp
}