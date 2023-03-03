package com.example.googlemapscompose.domain.business


data class Coordinate(
    val longitude: Double,
    val latitude: Double
)

class PolylineUtilities {

    /**
     * Encodes a polyline using Google's polyline algorithm
     * (See http://code.google.com/apis/maps/documentation/polylinealgorithm.html for more information).
     *
     * code derived from : https://gist.github.com/signed0/2031157
     *
     * @param (x,y)-Coordinates
     * @return polyline-string
     */
    fun encode(coords: List<Coordinate>): String {
        val result: MutableList<String> = mutableListOf()

        var prevLat = 0
        var prevLong = 0

        for ((long,lat) in coords) {
            println("AAAAAAA Routes: $long, $lat")
            val iLat = (lat * 1e5).toInt()
            val iLong = (long * 1e5).toInt()

            val deltaLat = encodeValue(iLat - prevLat)
            val deltaLong = encodeValue(iLong - prevLong)

            prevLat = iLat
            prevLong = iLong

            result.add(deltaLat)
            result.add(deltaLong)
        }

        return result.joinToString("")
    }

    private fun encodeValue(value: Int): String {
        // Step 2 & 4
        var actualValue = if (value < 0) (value shl 1).inv() else (value shl 1)

        // Step 5-8
        val chunks: List<Int> = splitIntoChunks(actualValue)

        // Step 9-10
        return chunks.map { (it + 63).toChar() }.joinToString("")
    }

    private fun splitIntoChunks(toEncode: Int): List<Int> {
        // Step 5-8
        val chunks = mutableListOf<Int>()
        var value = toEncode
        while(value >= 32) {
            chunks.add((value and 31) or (0x20))
            value = value shr 5
        }
        chunks.add(value)
        return chunks
    }

    /**
     * Decodes a polyline that has been encoded using Google's algorithm
     * (http://code.google.com/apis/maps/documentation/polylinealgorithm.html)
     *
     * code derived from : https://gist.github.com/signed0/2031157
     *
     * @param polyline-string
     * @return (long,lat)-Coordinates
     */
    fun decode(polyline: String): List<Coordinate> {
        val coordinateChunks: MutableList<MutableList<Int>> = mutableListOf()
        coordinateChunks.add(mutableListOf())

        for (char in polyline.toCharArray()) {
            // convert each character to decimal from ascii
            var value = char.toInt() - 63

            // values that have a chunk following have an extra 1 on the left
            val isLastOfChunk = (value and 0x20) == 0
            value = value and (0x1F)

            coordinateChunks.last().add(value)

            if (isLastOfChunk)
                coordinateChunks.add(mutableListOf())
        }

        coordinateChunks.removeAt(coordinateChunks.lastIndex)

        var coordinates: MutableList<Double> = mutableListOf()

        for (coordinateChunk in coordinateChunks) {
            var coordinate = coordinateChunk.mapIndexed { i, chunk -> chunk shl (i * 5) }.reduce { i, j -> i or j }

            // there is a 1 on the right if the coordinate is negative
            if (coordinate and 0x1 > 0)
                coordinate = (coordinate).inv()

            coordinate = coordinate shr 1
            coordinates.add((coordinate).toDouble() / 100000.0)
        }

        val points: MutableList<Coordinate> = mutableListOf()
        var previousX = 0.0
        var previousY = 0.0

        for(i in 0 until coordinates.size step 2) {
            if(coordinates[i] == 0.0 && coordinates[i+1] == 0.0)
                continue

            previousX += coordinates[i + 1]
            previousY += coordinates[i]

            points.add(Coordinate(round(previousX, 5), round(previousY, 5)))
        }
        return points
    }

    private fun round(value: Double, precision: Int) =
        (value * Math.pow(10.0,precision.toDouble())).toInt().toDouble() / Math.pow(10.0,precision.toDouble())

    /**
     * https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
     */
    fun simplify(points: List<Coordinate>, epsilon: Double): List<Coordinate> {
        // Find the point with the maximum distance
        var dmax = 0.0
        var index = 0
        var end = points.size

        for(i in 1..(end - 2)) {
            var d = perpendicularDistance(points[i], points[0], points[end-1])
            if ( d > dmax ) {
                index = i
                dmax = d
            }
        }
        // If max distance is greater than epsilon, recursively simplify
        return if (dmax > epsilon) {
            // Recursive call
            val recResults1: List<Coordinate> = simplify(points.subList(0,index+1), epsilon)
            val recResults2: List<Coordinate> = simplify(points.subList(index,end), epsilon)

            // Build the result list
            listOf(recResults1.subList(0,recResults1.lastIndex), recResults2).flatMap { it.toList() }
        } else {
            listOf(points[0], points[end - 1])
        }
    }

    private fun perpendicularDistance(pt: Coordinate, lineFrom: Coordinate, lineTo: Coordinate): Double =
        Math.abs((lineTo.longitude - lineFrom.longitude)*(lineFrom.latitude - pt.latitude) - (lineFrom.longitude- pt.longitude)*(lineTo.latitude - lineFrom.latitude))/
                Math.sqrt(Math.pow(lineTo.longitude - lineFrom.longitude, 2.0) + Math.pow(lineTo.latitude - lineFrom.latitude, 2.0))

    /**
     * how to use it
     */

    fun main(args: Array<String>) {
        //val polyline = "smdeH_mwbB@cAM_AMo@Ca@Ic@OYE]G]GYE_@G_@I]G_@Ci@I_@Ic@OYI_@Ia@K_@Ga@M[M[MWKYEa@E]A[@[SKKYIYKSI]K]KWI_@KYMUIUGWK[MSMQIWKWGa@EYG_@E[A[M[I]E]MQE[IWMSG[Kc@GYGg@G]Ka@Kc@Ia@Ic@Ig@Em@Ic@Ca@Ia@I[Gc@Ic@Ic@Ge@Ge@K]Em@Kg@Gs@Mi@Gk@Mm@Og@Gk@Om@Gk@Mi@Q_@Q]S]U_@Ua@W[]WWQQSOOKUIUQWS_@Q_@S[K_@O]Oa@Eq@UYQ_@Qe@UUOWQMG[QIM]KUB_@^YTQJa@Hi@He@Fe@Fe@He@H_@F]L_@Hc@De@F]H_@J_@Bc@Jc@F_@H_@D_@?_@Aa@@]D_@F_@PWF[D]D[D[FU@a@@UBa@?a@?[D]@c@JYTe@B_@Do@Lm@Ns@De@Dm@Ls@Hg@Ji@Jk@Ha@Hi@Pi@Ee@I_@G]Ga@G_@G[Ia@Ka@I]O]Y[YYYUW_@[UY[WYWYSWUUSUWWWSUUU[YWYYW[WYYUQHQKSMU]WUS_@UWYQUOQa@[a@Y[]_@Y]Y[WWQ?SOYSYW_@UUOUAQSIm@SSQ[U[U]WWYWS]UWWSSYQWUUQQQMQSQBUKYO[G[Ka@M]Ma@C_@Ma@I_@M]ESIUCUBYAS?SBS@WEOO@[?[KSW@WASEUAUGYCYEWBUNs@Ig@IQHOFWHYHWJYI]WMa@Ok@Sg@Y]O[MUMUG]MYU]QYQe@Oe@WUM]UUOe@Se@Oa@Qg@Qo@S_@O_@QUMYM_@Mc@a@q@I[Qc@So@Kc@Qa@Se@S]O_@Qi@Ke@[i@Q_@MUMYGYQ?QBKVUMYG_@@WIYKUOSQMQSIS[SYOKQ[Ec@K]M]IYCg@Cu@Eo@GY@_@@c@E]G[KYQUU[Sc@Sg@Oq@Si@Se@Q_@O_@MUKUM[MOQUO[Sa@Oc@Qe@M_@QYQc@Sc@Si@Se@M_@Qa@Qe@Ug@Sc@Sg@Ug@Ue@Qe@Ua@Oa@Se@Q]Qe@Oc@Si@Oe@Ua@Qc@Qa@M[Qa@KYKYKWKYQYOSSSSQO[O_@Se@Oa@U]]c@Y_@Mc@SYQYYc@Ee@UYSM[g@Ua@Ma@SQOWKiAQa@@[WWQc@S[M]Si@M_@Qc@WOOc@Cm@Q[Wg@Y_@Wo@Mg@Qe@M]Oc@Yi@UUMc@Qi@Wi@G_@"
        val polyline = "mukmExvvnUZPLJLLBBFJFLHPBPBL@N?L?J@l@?p@?j@"
        val decoded = decode(polyline)
        val simplified = simplify(decoded, 0.0001) // 0.0001 ~ 7-11m
        val encoded = encode(simplified)
        println("#pts original: ${decoded.size} - simplified: ${simplified.size}")
        println("simplified polyline = $encoded")
    }

}