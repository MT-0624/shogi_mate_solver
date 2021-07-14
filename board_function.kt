import java.lang.Exception
import java.lang.Math.E
import java.lang.Math.abs

class BoardFunc() {
    //2点が隣同士か判定する
    fun isNextAB(rowA: Int, colA: Int, rowB: Int, colB: Int): Boolean {
        return rowA in (-1..1 + rowB) && colA in (-1..1 + colB)
    }

    //動きの配列を独自形式のString形式に変換する
    fun txtMove(move: Array<Int>): String {
        val txt = move[0].toString() + move[1].toString() + move[2].toString() + move[3].toString()
        return "m" + txt + if (move[4] == 1) "+" else ""
    }

    //置く動きの配列を独自形式のString形式に変換する
    //持ち駒の番号に２桁の駒はない
    fun txtPut(move: Array<Int>): String {
        return "p" + move[0].toString() + move[1].toString() + move[2].toString()
    }


    //合法手を探す
    //turn_mover じゃない方の王が取れるかどうかのチェックをする

    //forCheckにtrueを入れると詰将棋用になる
    //turnがtrueのときはどう考えても王手じゃなさげな手を切る
    //turnがfalseのときはどう考えても王手回避できてない手を切る
    //何れにせよisCheckは必要
    fun available(turn_mover: Boolean, b: Array<Array<Int>>, forCheck: Boolean = false): MutableList<Array<Int>> {
        //駒1と駒2が同じ陣営であれば1 敵なら-1　空があるなら0
        fun turnEqual(pieceA: Int, pieceB: Int): Int {
            val x: Int = if (pieceA > 0) 1 else if (pieceA < 0) -1 else 0
            val y: Int = if (pieceB > 0) 1 else if (pieceB < 0) -1 else 0

            if (x == 0 || y == 0) {
                return 0
            } else {
                if (x == y) {
                    return 1
                } else {
                    return -1
                }
            }
        }
        //動き
        val name = Name()
        //動きリストは(行番号,列番号,終点行番号,終点列番号,成るか否か0,1)で構成される
        val moves: MutableList<Array<Int>> = mutableListOf()

        for (rw in 0..8) {
            for (cl in 0..8) {
                val p: Int = b[rw][cl]

                if (p == 0) {
                    continue
                } else if ((p > 0) != turn_mover) {
                    continue
                } else {
                    //自分の手番のコマでなければ動かせないのでcontinue
                    val pNumber: Int = abs(p)
                    val move: Array<Array<Int>>
                    //飛び道具ではない駒
                    if (pNumber != 2 && pNumber != 5 && pNumber != 6 && pNumber != 12 && pNumber != 13) {
                        //駒番号に合う駒の動きを取得する
                        move = when (pNumber) {
                            1 -> name.pawn
                            3 -> name.knight
                            4 -> name.silver
                            in 7..13 -> name.gold
                            14 -> name.king
                            else -> throw Exception("%sは処理できません".format())
                        }

                        var up: Int
                        var right: Int
                        //後手なら駒の移動を全て逆さにする
                        val bias: Int = if (turn_mover) 1 else -1
                        var endRow: Int
                        var endCol: Int
                        //とあるマスのとある駒の合法な動きを取得し、movesに追加する
                        for (m in move) {

                            up = m[0] * bias
                            right = m[1] * bias
                            endRow = rw + up
                            endCol = cl + right

                            //移動が将棋盤をはみ出ていればcontinue
                            if (!(endRow in 0..8 && endCol in 0..8)) {
                                continue
                            }

                            //移動先に味方の駒があればcontinue
                            if (turnEqual(p, b[endRow][endCol]) == 1) {
                                continue
                            }


                            //ここまでくればでとりあえず動かせる駒ではある
                            //歩　桂　を成らなくてはならない　かどうかの判定はこの後

                            //歩
                            if (pNumber == 1) {
                                //先手
                                if (turn_mover) {
                                    //成れるなら
                                    if (endRow in 0..2) {
                                        //最上段なら強制で成る
                                        if (endRow == 0) {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            continue
                                        } else {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                            continue
                                        }
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    }
                                }
                                //後手
                                else {
                                    if (endRow in 6..8) {
                                        if (endRow == 8) {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            continue
                                        } else {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                            continue
                                        }
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    }
                                }
                            }
                            //桂で移動先が上2段か下2段なら成しか追加しない
                            if (pNumber == 3) {
                                //先手
                                if (turn_mover) {
                                    //成れるなら
                                    if (endRow in 0..2) {
                                        //最上2段なら強制で成る
                                        if (endRow == 0 || endRow == 1) {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            continue
                                        } else {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                            continue
                                        }
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    }
                                }
                                //後手
                                else {
                                    if (endRow in 6..8) {
                                        if (endRow == 8 || endRow == 7) {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            continue
                                        } else {
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                            moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                            continue
                                        }
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    }
                                }
                            }
                            //銀
                            if (pNumber == 4) {
                                //先手
                                if (turn_mover) {
                                    //成れるなら
                                    if (endRow in 0..2 || rw == 2) {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    }
                                }
                                //後手
                                else {
                                    if (endRow in 6..8 || rw == 6) {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        continue
                                    }
                                }
                            }

                            //金 玉
                            if (pNumber == 14 || pNumber in 7..11) {
                                moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                continue
                            }

                        }
                    }
                    //飛び道具の場合の処理
                    else {
                        var endRow: Int
                        var endCol: Int
                        var promotion = 0//0成れない,1成れる,2成らないといけない
                        //香車
                        if (pNumber == 2) {
                            val bias = if (turn_mover) 1 else -1
                            for (i in 1..8) {
                                endRow = rw + (i * -1 * bias)
                                endCol = cl
                                //はみ出ていればbreak
                                if (8 < endRow || endRow < 0) {
                                    break
                                }
                                //移動先のマス目の状況を考える
                                //移動先の成りのルールを考える


                                //敵陣最上段最下段なら2 そうでなくて敵陣なら1　さもなくば0
                                if (endRow == 0 || endRow == 8) {
                                    promotion = 2
                                } else {
                                    //そうでなく敵陣なら1
                                    if (turn_mover) {
                                        if (endRow == 1 || endRow == 2) {
                                            promotion = 1
                                        }
                                    } else {
                                        if (endRow == 7 || endRow == 8) {
                                            promotion = 1
                                        }
                                    }
                                }

                                //移動先に味方の駒があればcontinue
                                val te = turnEqual(p, b[endRow][endCol])
                                if (te == 1) {
                                    break
                                }

                                //移動
                                when (promotion) {
                                    0 -> moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                    1 -> {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                    }
                                    else -> moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                }
                                //敵駒なら

                                if (te == -1) {
                                    break
                                }
                            }

                        }
                        //飛 竜
                        if (pNumber == 5 || pNumber == 12) {
                            //a は方向が上　右　下　左　と回る
                            for (a in 0..3) {
                                //飛車の動き
                                for (i in 1..8) {
                                    endRow = rw + (i * name.rookR[a])
                                    endCol = cl + (i * name.rookC[a])
                                    //はみ出ていればbreak
                                    if (8 < endRow || endRow < 0 || 8 < endCol || endCol < 0) {
                                        break
                                    }
                                    //移動先のマス目の状況を考える
                                    //移動先の成りのルールを考える

                                    //敵陣最上段最下段なら2 そうでなくて敵陣なら1　さもなくば0

                                    //そうでなく敵陣なら1
                                    if (pNumber != 12) {
                                        if (turn_mover) {
                                            if (endRow in 0..2 || rw in 0..2) {
                                                promotion = 1
                                            }
                                        } else {
                                            if (endRow in 6..8 || rw in 6..8) {
                                                promotion = 1
                                            }
                                        }
                                    }

                                    //移動先に味方の駒があればbreak
                                    val te = turnEqual(p, b[endRow][endCol])
                                    if (te == 1) {
                                        break
                                    }

                                    //移動
                                    if (promotion == 0 || pNumber == 12) {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                    }

                                    //移動先に敵の駒があればbreak
                                    if (te == -1) {
                                        break
                                    }
                                }
                            }
                            //龍なら斜め1マスそれぞれ4方向の動きも追加
                            if (pNumber == 12) {
                                for (i in 0..3) {
                                    endRow = rw + name.bishopR[i]
                                    endCol = cl + name.bishopC[i]
                                    //移動先がはみ出ていない&味方駒がいないなら移動
                                    if (endRow in 0..8 && endCol in 0..8 && turnEqual(p, b[endRow][endCol]) != 1) {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                    }
                                }
                            }
                        }
                        //角　馬
                        if (pNumber == 6 || pNumber == 13) {
                            //a は方向が上　右　下　左　と回る
                            for (a in 0..3) {
                                for (i in 1..8) {
                                    endRow = rw + (i * name.bishopR[a])
                                    endCol = cl + (i * name.bishopC[a])
                                    //移動先がはみ出ていればbreak
                                    if (!(endRow in 0..8 && endCol in 0..8)) {
                                        break
                                    }
                                    //移動先のマス目の状況を考える
                                    //移動先の成りのルールを考える

                                    //敵陣最上段最下段なら2 そうでなくて敵陣なら1　さもなくば0

                                    //そうでなく敵陣なら1
                                    if (turn_mover) {
                                        if (endRow in 0..2 || rw in 0..2) {
                                            promotion = 1
                                        }
                                    } else {
                                        if (endRow in 6..8 || rw in 6..8) {
                                            promotion = 1
                                        }
                                    }

                                    //移動先に味方の駒があればbreak
                                    val te = turnEqual(p, b[endRow][endCol])
                                    if (te == 1) {
                                        break
                                    }

                                    //移動
                                    if (promotion == 0 || pNumber == 13) {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                    } else {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 1))
                                    }

                                    if (te == -1) {
                                        break
                                    }

                                }
                            }
                            //龍なら斜め1マスそれぞれ4方向の動きも追加
                            if (pNumber == 13) {
                                for (i in 0..3) {
                                    endRow = rw + name.rookR[i]
                                    endCol = cl + name.rookC[i]
                                    //移動先がはみ出ていない&味方駒がいないなら移動
                                    if (endRow in 0..8 && endCol in 0..8 && (turnEqual(p, b[endRow][endCol]) != 1)) {
                                        moves.add(arrayOf(rw, cl, endRow, endCol, 0))
                                    }
                                }
                            }
                        }

                    }


                }


            }

        }

        return moves
    }

    fun availablePut(
        turn_mover: Boolean,
        hand: Array<Int>,
        b: Array<Array<Int>>,
        forCheck: Boolean = false
    ): MutableList<Array<Int>> {
        //
        //動きリストは(行番号,列番号,終点行番号,終点列番号,成るか否か0,1)で構成される
        val moves: MutableList<Array<Int>> = mutableListOf()

        for (rw in 0..8) {
            for (cl in 0..8) {
                val p: Int = b[rw][cl]

                //空白でなければ無視
                if (p != 0) {
                    continue
                }
                //持ち駒リスト
                for (h in 0..6) {
                    //持ち駒にない駒を打とうとしているならcontinue
                    if (hand[h] == 0) {
                        continue
                    }

                    //歩の場合の2歩とロックのチェック可能なら動きリストに追加
                    if (h == 0) {
                        val check: Int = if (turn_mover) 0 else 8
                        //最上段ならcontinue
                        if (rw == check) {
                            continue
                        }


                        //同じ行に味方の歩があればcontinue
                        var nif: Boolean = false
                        val pawn: Int = if (turn_mover) 1 else -1

                        for (i in 0..8) {
                            if (b[i][cl] == pawn) {
                                nif = true
                                break
                            }
                        }
                        if (nif) {
                            continue
                        }

                        //打てる歩なので追加
                        moves.add(arrayOf(rw, cl, pawn))
                        continue
                    }
                    //桂馬
                    if (h == 1) {

                        //上から2段ならcontinue
                        if (turn_mover) {
                            if (rw < 2) {
                                continue
                            }
                        } else {
                            if (rw > 5) {
                                continue
                            }
                        }
                        //打てる桂馬
                        moves.add(arrayOf(rw, cl, if (turn_mover) 2 else -2))
                        continue
                    }
                    //香車
                    if (h == 2) {
                        val check: Int = if (turn_mover) 0 else 7
                        //最上段ならcontinue
                        if (rw == check) {
                            continue
                        }
                        moves.add(arrayOf(rw, cl, if (turn_mover) 3 else -3))
                        continue
                    }

                    moves.add(arrayOf(rw, cl, if (turn_mover) (h + 1) else (h + 1) * -1))
                }
            }
        }
        return moves
    }


//    //論理チェック　二歩　恒久的に身動きの取れない不成駒　
//    fun logicalCheck(turn: Boolean, b: Array<Array<Int>>): Boolean {
//
//    }
//
//    //その局面に王手を回避する術がないか　上二つに依存
//    fun isMate(turn: Boolean, b: Array<Array<Int>>): Boolean {
//
//    }
    //先手玉の王手ならtrue 後手玉ならfalse

    fun isCheck(turn: Boolean, b: Array<Array<Int>>): Boolean {
        //与えられた駒が敵か味方か空白か判別する
        fun type(piece: Int): Int {
            //空白は0 continueフラグに使う
            if (piece == 0) {
                return 0
            }
            val me = if (turn) 1 else -1
            val opp = if (piece > 0) 1 else -1

            return if (me == opp) 1 else -1
        }

        var kIdxRw: Int = -1
        var kIdxCl: Int = -1

        var grid: Int

        var oppRw: Int
        var oppCl: Int


        //敵駒かどうかのフラグ
        var type: Int

        val forward: Int = if (turn) -1 else 1
        val back: Int = if (turn) 1 else -1

        val king: Int = if (turn) 14 else -14
        var find = false
        //チェック対象の王を探す
        for (i in 0..8) {
            for (j in 0..8) {
                if (b[i][j] == king) {
                    kIdxRw = i
                    kIdxCl = j
                    find = true
                    break
                }
            }
            if (find) {
                break
            }
        }
        if (!(find)) {
            throw Exception("王が見つかりません")
        }


        //右上方向の探索 角馬竜　1周目に限り銀もあり
        //先手玉のチェックなら左下方向に走査する
        //以後のコメントは後手玉の王手チェックの場合と仮定してコメントする

        //探索する敵駒の行
        //列
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppRw += back
            oppCl += forward
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]

            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {

                //絶対値に変換して駒番号にする
                grid = kotlin.math.abs(grid)
                //そこの敵駒が何かを判定　王手になる駒ならtrueを返し終了
                //王の1マス隣の時だけ飛び道具以外の駒も判定に入る(この場合馬角以外の竜(12)銀(4)も王手になる)
                //詳細な駒番号はpiece_name.ktを参照
                if (i == 1) {
                    if (grid == 4 || grid == 6 || grid == 12 || grid == 13 || grid == 14) {
                        return true
                    } else {
                        break
                    }
                } else
                //馬角のみ
                {
                    if (grid == 6 || grid == 13) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }


        //上方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppRw += back
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]
            type = type(grid)

            if (type == -1) {

                grid = kotlin.math.abs(grid)

                if (i == 1) {
                    if (grid == 5 || grid >= 7) {
                        return true
                    } else {
                        break
                    }
                } else
                //馬角のみ
                {
                    if (grid == 5 || grid == 12) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }

        //左上方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppRw += back
            oppCl += back
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }
            grid = b[oppRw][oppCl]
            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {

                grid = kotlin.math.abs(grid)
                if (i == 1) {
                    if (grid == 4 || grid == 6 || grid == 12 || grid == 13 || grid == 14) {
                        return true
                    } else {
                        break
                    }
                } else
                //馬角のみ
                {
                    if (grid == 6 || grid == 13) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }

        //左方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppCl += back
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]

            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {
                grid = kotlin.math.abs(grid)
                if (i == 1) {
                    if (grid == 5 || grid >= 7) {
                        return true
                    } else {
                        break
                    }
                } else {
                    if (grid == 5 || grid == 12) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }

        //左下方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppRw += forward
            oppCl += back
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]

            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {

                grid = kotlin.math.abs(grid)
                if (i == 1) {
                    if (grid == 4 || grid >= 6) {
                        return true
                    } else {
                        break
                    }
                } else
                //馬角のみ
                {
                    if (grid == 6 || grid == 13) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }

        //下方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppRw += forward
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]

            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {
                grid = kotlin.math.abs(grid)
                if (i == 1) {
                    if (grid != 3 && grid != 6) {
                        return true
                    } else {
                        break
                    }
                } else {
                    if (grid == 2 || grid == 5 || grid == 12) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }

        //右下方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppRw += forward
            oppCl += forward
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]

            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {

                grid = kotlin.math.abs(grid)

                if (i == 1) {
                    if (grid == 4 || grid >= 6) {
                        return true
                    } else {
                        break
                    }
                } else {
                    if (grid == 6 || grid == 13 || grid == 14) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }
        //右方向
        oppRw = kIdxRw
        oppCl = kIdxCl
        for (i in 1..8) {
            oppCl += forward
            //盤をはみ出る場所を参照しないか
            if (!(oppRw in 0..8 && oppCl in 0..8)) {
                break
            }

            grid = b[oppRw][oppCl]

            type = type(grid)

            //_typeが敵駒(-1)なら判定を行う
            if (type == -1) {

                grid = kotlin.math.abs(grid)

                if (i == 1) {
                    if (grid == 5 || grid >= 7) {
                        return true
                    } else {
                        break
                    }
                } else {
                    if (grid == 5 || grid == 12) {
                        return true
                    } else {
                        break
                    }
                }

            }
            //味方
            else if (type == 1) {
                break
            }
            //空白
            else {
                continue
            }
        }


        //桂馬チェック
        //左桂馬
        oppRw = kIdxRw + forward + forward
        oppCl = kIdxCl + back

        if (oppRw in 0..8 && oppCl in 0..8) {
            grid = b[oppRw][oppCl]
            type = type(grid)
            if (type == -1) {
                grid = kotlin.math.abs(grid)
                if (grid == 3) {
                    return true
                }
            }
        }

        //右桂馬
        oppCl = kIdxCl + forward
        if (oppRw in 0..8 && oppCl in 0..8) {
            grid = b[oppRw][oppCl]
            type = type(grid)
            if (type == -1) {
                grid = kotlin.math.abs(grid)
                if (grid == 3) {
                    return true
                }
            }
        }
        //全探索終了
        //王手はかかっていない
        return false

    }
}

//詰将棋の指し手の手順を保管する木構造
class MateTree() {

    private val bf = BoardFunc()

    fun addNode(key: String, obj: Array<Int>) {
        hash[key] = obj
    }

    fun getNode(str: String): Array<Int>? {
        return hash[str]
    }

    //側は最善種だけなので一意に決まるはず

    //  attackHashの1ノードに対する応手が格納されている
    var hash: MutableMap<String, Array<Int>> = mutableMapOf()

    fun mate(turn: Boolean, b: Board, depth: Int, parent: String = "/"): Int {

        //println(parent)

        //指定の深さまで来たら終了
        if (depth == -1) {
            return -1
        }

        val hand = if (turn) b.handW else b.handB
        val put = bf.availablePut(turn, hand, b.board)
        val move = bf.available(turn, b.board)


        //この変数が判定後も０なら
        var nodeNum: Int = 0
        //戻り値を評価する
        var actions = mutableListOf<Array<Int>>()
        var returns = mutableListOf<Int>()
        if (0 in returns) {
            throw Exception("0がリターンされました")
        }


        //先手なら王手がかかる打ち手だけ選ぶ
        //後手なら王手のかからない手を選ぶ

        for (i in put) {
            val boardCopy: Board = deepCopy(b)
            boardCopy.put(turn, abs(i[2]), i[0], i[1])
            if (bf.isCheck(false, boardCopy.board) == turn) {
                nodeNum++
                //攻撃側の手なら自玉の王手も確かめる
                if (turn && bf.isCheck(true, boardCopy.board)) {
                    continue
                }
                i[2] = abs(i[2])
                actions.add(i)
                returns.add(mate(!(turn), boardCopy, depth - 1, parent + bf.txtPut(i) + "/"))
            }
        }
        //王手がかかる指し手だけ選ぶ
        for (i in move) {
            val boardCopy: Board = deepCopy(b)
            val pro = i[4] == 1

            boardCopy.move(i[0], i[1], i[2], i[3], pro)
            //攻撃側なら王手をかけているか　受け側なら王手を回避しているか
            if (bf.isCheck(false, boardCopy.board) == turn) {
                nodeNum++
                //println(parent + "p" + bf.txtPut(i) + "/")
                //攻撃側の手なら自玉の王手も確かめる
                if (turn && bf.isCheck(true, boardCopy.board)) {
                    continue
                }
                actions.add(i)
                returns.add(mate(!(turn), boardCopy, depth - 1, parent + bf.txtMove(i) + "/"))
            }
        }
        //攻め側は王手でさえあればどうでもいい　受け側の正しい応手が必要

        //候補手がゼロ個の場合
        //後手なら受けなし詰み
        //先手なら王手なしによる不詰
        if (nodeNum == 0) {
            if (turn) {
                return -1
            } else {
                return 0
            }
        } else {
            //候補手のある後手
            if (!(turn)) {
                if (depth == 0){
                    return -1
                }
                var maxMate = 0
                var maxInd = -1
                for (ind in 0..returns.size - 1) {
                    val r = returns[ind]
                    if (r < 0) {
                        addNode(parent, actions[ind])
                        return -1
                    } else {
                        if (maxMate < r) {
                            maxMate = r
                            maxInd = ind
                        }
                    }
                }
                addNode(parent, actions[maxInd])
                return maxMate + 1
            }
            //候補手のある先手
            else {
                var minMate = -1

                for (ind in 0..returns.size - 1) {
                    val r = returns[ind]
                    if (r > -1) {
                        if (minMate < r) {
                            minMate = r
                        }
                    } else {
                        continue
                    }
                }

                return if (minMate == -1) -1 else minMate + 1
            }
        }

        return 0
    }

}


//つみの有無　木構造の作成
//
fun makeMate(turn: Boolean, b: Board, depth: Int) {
    val bf = BoardFunc()
    val tree = MateTree()


}

fun main() {

}


