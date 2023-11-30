package com.fansipan.callcolor.calltheme.utils.data

enum class Amp(val code: Int, val timeArr: LongArray, val ampArr: IntArray) {
    MID_PULSE(
        1,
        longArrayOf(100, 300, 100, 300, 100, 300, 100, 300),
        intArrayOf(0, 30, 0, 70, 0, 30, 0, 70)
    ),
    FAST(
        2,
        longArrayOf(20, 250, 20, 250, 20, 250, 20, 250),
        intArrayOf(0, 35, 0, 35, 0, 35, 0, 35)
    ),
    CALMING(
        3,
        longArrayOf(300, 800, 300, 800, 300, 800, 300, 800),
        intArrayOf(0, 35, 0, 35, 0, 35, 0, 35)
    ),
    WATER_FALL(
        4,
        longArrayOf(300, 700, 10, 700, 10, 700, 10, 700),
        intArrayOf(0, 20, 0, 50, 0, 50, 0, 20)
    ),
    CONTINUOUS(
        5,
        longArrayOf(200, 700, 5, 700, 5, 700, 5, 700),
        intArrayOf(0, 20, 0, 20, 0, 20, 0, 20)
    ),
    GUST(
        6,
        longArrayOf(50, 3000),
        intArrayOf(0, 20)
    ),
    SLEEP(
        7,
        longArrayOf(5, 800, 5, 800, 5, 800),
        intArrayOf(0, 25, 0, 25, 0, 25)
    ),
    STRONG_PULSE(
        8,
        longArrayOf(10, 600, 10, 600, 10, 600),
        intArrayOf(0, 10, 0, 15, 0, 15)
    ),
    EARTH_QUAKE(
        9,
        longArrayOf(50, 700),
        intArrayOf(0, 35)
    ),
    HEART_BEAT(
        10,
        longArrayOf(10, 2000),
        intArrayOf(0, 35)
    ),
    BLAST(
        11,
        longArrayOf(10, 300, 5, 80, 5, 80, 5, 80, 5, 80, 5, 80, 5, 80),
        intArrayOf(0, 30, 0, 35, 0, 35, 0, 35, 0, 35, 0, 35, 0, 35)
    ),
    MEDITATIVE(
        12,
        longArrayOf(500, 2500),
        intArrayOf(0, 35)
    ),
    EXTREME(
        13,
        longArrayOf(25, 90, 25, 90, 25, 90, 25, 90, 25, 90, 25, 90),
        intArrayOf(0, 35, 0, 35, 0, 35, 0, 35, 0, 35, 0, 35)
    ),
    VOLCANO(
        14,
        longArrayOf(25, 90, 25, 90, 25, 90, 25, 90, 25, 100, 25, 100, 10, 100, 10, 1000),
        intArrayOf(0, 35, 0, 35, 0, 35, 0, 35, 0, 30, 0, 30, 0, 30, 0, 25)
    ),
    BOMB(
        15,
        longArrayOf(25, 90, 25, 90, 25, 90, 25, 90, 25, 90, 25, 600),
        intArrayOf(0, 35, 0, 35, 0, 35, 0, 35, 0, 35, 0, 30)
    ),
    IN_TEST(
        16,
        longArrayOf(200, 500, 200, 500, 200, 500, 200, 500),
        intArrayOf(0, 40, 0, 40, 0, 40, 0, 40)
    ),
    SWEDISH(
        17,
        longArrayOf(272, 500, 272, 550, 272, 650, 272, 750),
        intArrayOf(0, 40, 0, 41, 0, 42, 0, 43)
    ),
    HOT_STONE(
        18,
        longArrayOf(250, 400, 250, 500, 250, 400, 250, 500),
        intArrayOf(0, 40, 0, 40, 0, 40, 0, 40)
    ),
    Aromatherapy(
        19,
        longArrayOf(230, 483, 230, 600, 230, 483, 230, 600),
        intArrayOf(0, 40, 0, 40, 0, 40, 0, 40)
    ),
    Deep_Tissue(
        20,
        longArrayOf(200, 800, 200, 800, 200, 800, 200, 800),
        intArrayOf(0, 50, 0, 50, 0, 50, 0, 50)
    ),
    Sports(
        21,
        longArrayOf(150, 555, 150, 555, 150, 555, 150, 555),
        intArrayOf(0, 55, 0, 55, 0, 55, 0, 55)
    ),
    Trigger_Point(
        22,
        longArrayOf(241, 600, 241, 300, 241, 600, 241, 500),
        intArrayOf(0, 40, 0, 60, 0, 40, 0, 60)
    ),
    Reflexology(
        23,
        longArrayOf(400, 500, 400, 500, 400, 500, 400, 500),
        intArrayOf(0, 60, 0, 40, 0, 60, 0, 40)
    ),
    Shiatsu(
        24,
        longArrayOf(300, 500, 300, 500, 300, 500, 300, 500),
        intArrayOf(0, 60, 0, 60, 0, 60, 0, 60)
    ),
    Thai(
        25,
        longArrayOf(321, 846, 321, 846, 321, 846, 321, 846),
        intArrayOf(0, 54, 0, 54, 0, 54, 0, 54)
    ),
    Prenatal(
        26,
        longArrayOf(126, 784, 126, 784, 126, 784, 126, 784),
        intArrayOf(0, 88, 0, 88, 0, 88, 0, 88)
    ),
    Couples(
        27,
        longArrayOf(111, 1000, 111, 1000, 111, 1000, 111, 1000),
        intArrayOf(0, 60, 0, 70, 0, 80, 0, 90)
    ),
    Chair(
        28,
        longArrayOf(200, 300, 200, 300, 200, 300, 200, 300),
        intArrayOf(0, 40, 0, 40, 0, 40, 0, 40)
    ),
    Lymphatic(
        29,
        longArrayOf(500, 500, 500, 500, 500, 500, 500, 500),
        intArrayOf(0, 40, 0, 40, 0, 40, 0, 40)
    ),
    Cranial_Sacral(
        30,
        longArrayOf(300, 600, 300, 600, 300, 600, 300, 600),
        intArrayOf(0, 44, 0, 44, 0, 44, 0, 44)
    ),
    Abhyanga_Oil(
        31,
        longArrayOf(200, 600, 200, 600, 200, 600, 200, 600),
        intArrayOf(0, 44, 0, 55, 0, 66, 0, 22)
    ),
    Myofascial(
        32,
        longArrayOf(111, 666, 111, 666, 111, 666, 111, 666),
        intArrayOf(0, 46, 0, 46, 0, 46, 0, 46)
    ),
    mindfulness(
        33,
        longArrayOf(120, 1200, 120, 1200, 120, 1200, 120, 1200),
        intArrayOf(0, 50, 0, 50, 0, 50, 0, 50)
    ),
    spiritual(
        34,
        longArrayOf(120, 1200, 120, 1200, 120, 1200, 120, 1200),
        intArrayOf(0, 55, 0, 55, 0, 55, 0, 55)
    ),
    focused(
        35,
        longArrayOf(120, 1200, 120, 1200, 120, 1200, 120, 1200),
        intArrayOf(0, 60, 0, 60, 0, 60, 0, 60)
    ),
    movement(
        36,
        longArrayOf(120, 1200, 120, 1200, 120, 1200, 120, 1200),
        intArrayOf(0, 65, 0, 65, 0, 65, 0, 65)
    ),
    mantra(
        37,
        longArrayOf(120, 1200, 120, 1200, 120, 1200, 120, 1200),
        intArrayOf(0, 70, 0, 70, 0, 70, 0, 70)
    ),
    transcendental(
        38,
        longArrayOf(110, 1100, 110, 1100, 110, 1100, 110, 1100),
        intArrayOf(0, 70, 0, 70, 0, 70, 0, 70)
    ),
    progressive(
        39,
        longArrayOf(110, 1100, 110, 1100, 110, 1100, 110, 1100),
        intArrayOf(0, 80, 0, 80, 0, 80, 0, 80)
    ),
    loving_kindness(
        40,
        longArrayOf(200, 1300, 200, 1300, 200, 1300, 200, 1300),
        intArrayOf(0, 80, 0, 60, 0, 80, 0, 60)
    ),
    visualization(
        41,
        longArrayOf(200, 1300, 200, 1300, 200, 1300, 200, 1300),
        intArrayOf(0, 60, 0, 80, 0, 80, 0, 60)
    ),
    Skillful(
        42,
        longArrayOf(155, 1500, 155, 1500, 155, 1500, 155, 1500),
        intArrayOf(0, 70, 0, 75, 0, 75, 0, 70)
    ),
    Resting(
        43,
        longArrayOf(155, 1200, 155, 1700, 155, 1700, 155, 1200),
        intArrayOf(0, 75, 0, 85, 0, 85, 0, 75)
    ),
    Reflection(
        44,
        longArrayOf(250, 1700, 250, 1300, 250, 1300, 250, 1700),
        intArrayOf(0, 70, 0, 90, 0, 90, 0, 70)
    ),
    Body(
        45,
        longArrayOf(500, 1700, 500, 1300, 500, 1300, 500, 1700),
        intArrayOf(0, 90, 0, 75, 0, 75, 0, 90)
    ),
    Slowing(
        46,
        longArrayOf(600, 1700, 600, 1700, 600, 1700, 600, 1700),
        intArrayOf(0, 90, 0, 75, 0, 55, 0, 75)
    ),
    Lowering_Blood(
        47,
        longArrayOf(600, 1700, 600, 1700, 600, 1700, 600, 1700),
        intArrayOf(0, 40, 0, 35, 0, 25, 0, 40)
    ),
    Slowing_Breathing(
        48,
        longArrayOf(400, 1500, 400, 1500, 400, 1500, 400, 1500),
        intArrayOf(0, 40, 0, 35, 0, 45, 0, 40)
    ),
    Improving(
        49,
        longArrayOf(300, 1200, 300, 1200, 300, 1200, 300, 1200),
        intArrayOf(0, 50, 0, 50, 0, 45, 0, 50)
    ),
    Controlling(
        50,
        longArrayOf(150, 600, 150, 600, 150, 600, 150, 600),
        intArrayOf(0, 50, 0, 30, 0, 50, 0, 30)
    ),
    Reducing(
        51,
        longArrayOf(50, 322, 50, 322, 50, 322, 50, 322),
        intArrayOf(0, 30, 0, 30, 0, 30, 0, 30)
    ),
    Increasing(
        52,
        longArrayOf(70, 455, 70, 455, 70, 455, 70, 455),
        intArrayOf(0, 30, 0, 45, 0, 60, 0, 75)
    ),
    Reducing_Muscle(
        53,
        longArrayOf(88, 333, 88, 333, 88, 333, 88, 333),
        intArrayOf(0, 30, 0, 45, 0, 30, 0, 105)
    ),
    Thinking(
        54,
        longArrayOf(99, 350, 99, 350, 99, 350, 99, 350),
        intArrayOf(0, 45, 0, 30, 0, 45, 0, 88)
    ),
    Finding_Humor(
        55,
        longArrayOf(50, 200, 50, 200, 50, 200, 50, 200),
        intArrayOf(0, 20, 0, 25, 0, 20, 0, 38)
    ),
    Managing(
        56,
        longArrayOf(66, 222, 66, 111, 66, 333, 66, 222),
        intArrayOf(0, 40, 0, 40, 0, 40, 0, 40)
    ),
    Exercising(
        57,
        longArrayOf(99, 500, 99, 500, 99, 500, 99, 500),
        intArrayOf(0, 40, 0, 50, 0, 40, 0, 50)
    ),
    Spending(
        58,
        longArrayOf(100, 200, 200, 300, 300, 400, 400, 500),
        intArrayOf(0, 40, 0, 50, 0, 60, 0, 70)
    ),
    Reaching(
        59,
        longArrayOf(500, 400, 400, 300, 300, 200, 200, 100),
        intArrayOf(0, 70, 0, 60, 0, 50, 0, 40)
    ),
    Eating(
        60,
        longArrayOf(300, 150, 300, 150, 300, 300, 150, 300),
        intArrayOf(0, 70, 0, 70, 0, 70, 0, 70)
    ),

    ;

    companion object {
        fun getItem(code: Int) = values().find { it.code == code } ?: MID_PULSE
        fun getAllAmp() = values()
    }

}