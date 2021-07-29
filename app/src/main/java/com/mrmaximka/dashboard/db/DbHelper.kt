package com.mrmaximka.dashboard.db

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class DbHelper (val  context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE BRANDS (\n" +
                "    ID_BRAND INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    NAME     TEXT     NOT NULL,\n" +
                "    ID_PRJ   INT (11) NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE CATEGORIES (\n" +
                "    ID_CAT INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    NAME   TEXT     NOT NULL,\n" +
                "    ID_PRJ INT (11) NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE EVENTS (\n" +
                "    DATETIME    TEXT     NOT NULL,\n" +
                "    ID_STAND    INT (11) NOT NULL,\n" +
                "    ID_GOOD     INT (11) NOT NULL,\n" +
                "    EVENT       TEXT     NOT NULL,\n" +
                "    DESCRIPTION TEXT     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE GOODS (\n" +
                "    ID_GOOD     INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    ARTICUL     VARCHAR (45) DEFAULT NULL,\n" +
                "    NAME        TEXT         NOT NULL,\n" +
                "    ID_CAT      INT (11)     NOT NULL,\n" +
                "    PICTURE     TEXT         NOT NULL,\n" +
                "    DESCRIPTION TEXT         NOT NULL,\n" +
                "    ID_BRAND    INT (11)     NOT NULL,\n" +
                "    ID_PRJ      INT (11)     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE LOCATION (\n" +
                "    ID_LOC      INT (11)     NOT NULL,\n" +
                "    NAME_LOC    TEXT         NOT NULL,\n" +
                "    CITY_LOC    TEXT         NOT NULL,\n" +
                "    ADDRESS_LOC TEXT         NOT NULL,\n" +
                "    GPS_LOC     TEXT         NOT NULL,\n" +
                "    DESC_LOC    VARCHAR (45) DEFAULT NULL,\n" +
                "    ID_PRJ      INT (11)     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE LOG_ALL_SENSORS (\n" +
                "    ID       INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NUMBER   TEXT    NOT NULL,\n" +
                "    STAND    TEXT,\n" +
                "    DATETIME TEXT    NOT NULL,\n" +
                "    SYNC     INTEGER DEFAULT 0\n" +
                ");")

        db?.execSQL("CREATE TABLE LOG_CONNECT (\n" +
                "    ID_STAND INT (11) NOT NULL,\n" +
                "    DATETIME TEXT     NOT NULL,\n" +
                "    EVENT    TEXT     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE LOG_SENSORS (\n" +
                "    ID       INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    ID_SENS  INT (11) NOT NULL,\n" +
                "    ID_GOOD  INT (11) NOT NULL,\n" +
                "    ID_STAND INT (11) NOT NULL,\n" +
                "    DATETIME TEXT     NOT NULL,\n" +
                "    EVENT    TEXT     NOT NULL,\n" +
                "    SYNC     INTEGER  DEFAULT 0\n" +
                ");")

        db?.execSQL("CREATE TABLE LOG_USERS (\n" +
                "    ID_USER  INT (11) NOT NULL,\n" +
                "    EVENT    TEXT     NOT NULL,\n" +
                "    DATETIME TEXT     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE LOG_VIDEO (\n" +
                "    ID        INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    ID_VIDEO  INT (11) NOT NULL,\n" +
                "    DURATION  INT (11) NOT NULL,\n" +
                "    ID_GOOD   INT (11) NOT NULL,\n" +
                "    ID_GOOD_2 INT (11) NOT NULL,\n" +
                "    ID_STAND  INT (11) NOT NULL,\n" +
                "    DATETIME  TEXT     NOT NULL,\n" +
                "    SYNC      INTEGER  DEFAULT 0\n" +
                ");")

        db?.execSQL("CREATE TABLE PROJECT (\n" +
                "    ID_PRJ   INT (11)     NOT NULL,\n" +
                "    NAME_PRJ TEXT         NOT NULL,\n" +
                "    DESC_PRJ VARCHAR (45) DEFAULT NULL,\n" +
                "    EMAIL    VARCHAR (45) DEFAULT NULL,\n" +
                "    COMPANY  VARCHAR (50) DEFAULT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE SENSOR_TYPES (\n" +
                "    ID_TYPE INT (11) NOT NULL,\n" +
                "    NAME    TEXT     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE SENSORS (\n" +
                "    ID_SENS  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    ID_STAND TEXT    NOT NULL,\n" +
                "    SERIAL   TEXT    NOT NULL,\n" +
                "    ID_TYPE  TEXT    NOT NULL,\n" +
                "    ID_GOOD  TEXT    NOT NULL,\n" +
                "    ACTIVE   TEXT    NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE STAND (\n" +
                "    ID_STAND     INT (11) NOT NULL,\n" +
                "    PASSWORD     TEXT,\n" +
                "    SERIAL       INT (11) NOT NULL,\n" +
                "    ID_PRJ       INT (11) DEFAULT NULL,\n" +
                "    ID_LOC       INT (11) DEFAULT NULL,\n" +
                "    NAME         TEXT     NOT NULL,\n" +
                "    DESCRIPTION  TEXT,\n" +
                "    STAND_TYPE   TEXT     NOT NULL,\n" +
                "    GMT_TIME     TEXT,\n" +
                "    ACTIVE       INT (11) NOT NULL,\n" +
                "    DATE_INSTALL DATETIME DEFAULT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE STAND_PARAMETRS (\n" +
                "    ID             INT (11) NOT NULL,\n" +
                "    ID_STAND       INT (11) NOT NULL\n" +
                "                            DEFAULT '0',\n" +
                "    DELAY          INT (11) DEFAULT NULL,\n" +
                "    WAIT_TIME      INT (11) DEFAULT NULL,\n" +
                "    QUEUE          INT (11) DEFAULT NULL,\n" +
                "    BREAK          INT (11) DEFAULT NULL,\n" +
                "    REPLAY         INT (11) DEFAULT NULL,\n" +
                "    STOP_PLAY_ONE  INT (11) DEFAULT NULL,\n" +
                "    STOP_PLAY_TWO  INT (11) DEFAULT NULL,\n" +
                "    BREAK_PLAY_TWO INT (11) DEFAULT NULL,\n" +
                "    SENSITIVITY    INT (11) DEFAULT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE STAND_TYPES (\n" +
                "    ID_TYPE INT (11) NOT NULL,\n" +
                "    NAME    TEXT     NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE STAND_VIDEO (\n" +
                "    ID        INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    ID_STAND  INT (11) NOT NULL,\n" +
                "    ID_GOOD   INT (11) NOT NULL,\n" +
                "    ID_GOOD_2 INT (11),\n" +
                "    ID_VIDEO  INT (11) NOT NULL\n" +
                ");")

        db?.execSQL("CREATE TABLE VIDEOS (\n" +
                "    ID_VIDEO INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    FILENAME TEXT     NOT NULL,\n" +
                "    PATH     TEXT     NOT NULL,\n" +
                "    ID_PRJ   INT (11) NOT NULL\n" +
                ");")

        db?.execSQL("INSERT INTO STAND (\n" +
                "                      DATE_INSTALL,\n" +
                "                      ACTIVE,\n" +
                "                      GMT_TIME,\n" +
                "                      STAND_TYPE,\n" +
                "                      DESCRIPTION,\n" +
                "                      NAME,\n" +
                "                      ID_LOC,\n" +
                "                      ID_PRJ,\n" +
                "                      SERIAL,\n" +
                "                      PASSWORD,\n" +
                "                      ID_STAND\n" +
                "                  )\n" +
                "                  VALUES (\n" +
                "                      NULL,\n" +
                "                      1,\n" +
                "                      '+3',\n" +
                "                      1,\n" +
                "                      'Описание',\n" +
                "                      'Имя стенда',\n" +
                "                      1,\n" +
                "                      1,\n" +
                "                      1,\n" +
                "                      1234,\n" +
                "                      1\n" +
                "                  );")

        db?.execSQL("INSERT INTO STAND_PARAMETRS (\n" +
                "                                SENSITIVITY,\n" +
                "                                BREAK_PLAY_TWO,\n" +
                "                                STOP_PLAY_TWO,\n" +
                "                                STOP_PLAY_ONE,\n" +
                "                                REPLAY,\n" +
                "                                BREAK,\n" +
                "                                QUEUE,\n" +
                "                                WAIT_TIME,\n" +
                "                                DELAY,\n" +
                "                                ID_STAND,\n" +
                "                                ID\n" +
                "                            )\n" +
                "                            VALUES (\n" +
                "                                50,\n" +
                "                                1,\n" +
                "                                25,\n" +
                "                                0,\n" +
                "                                1,\n" +
                "                                1,\n" +
                "                                NULL,\n" +
                "                                4000,\n" +
                "                                0,\n" +
                "                                1,\n" +
                "                                1\n" +
                "                            );")

        addElements(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }


    private fun addElements(db: SQLiteDatabase?) {
        db?.execSQL("INSERT INTO GOODS (\n" +
                "                      ID_PRJ,\n" +
                "                      ID_BRAND,\n" +
                "                      DESCRIPTION,\n" +
                "                      PICTURE,\n" +
                "                      ID_CAT,\n" +
                "                      NAME,\n" +
                "                      ARTICUL,\n" +
                "                      ID_GOOD\n" +
                "                  )\n" +
                "                  VALUES (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      '-',\n" +
                "                      '',\n" +
                "                      0,\n" +
                "                      'Нет товара',\n" +
                "                      'None',\n" +
                "                      0\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      2,\n" +
                "                      1,\n" +
                "                      'gut pic',\n" +
                "                      '',\n" +
                "                      0,\n" +
                "                      123,\n" +
                "                      'None',\n" +
                "                      1\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      'SuperNova1',\n" +
                "                      'None',\n" +
                "                      8\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      'https://shishabook.ru/wp-content/uploads/2018/09/%D0%A2%D0%B0%D0%B1%D0%B0%D0%BA-MustHave.jpg',\n" +
                "                      10,\n" +
                "                      'MustHave',\n" +
                "                      'None',\n" +
                "                      11\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      9,\n" +
                "                      0,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      0,\n" +
                "                      '',\n" +
                "                      'None',\n" +
                "                      12\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      11,\n" +
                "                      10,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      14,\n" +
                "                      'SWMPPT 10',\n" +
                "                      'None',\n" +
                "                      13\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      'dsv',\n" +
                "                      'None',\n" +
                "                      14\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      321,\n" +
                "                      'None',\n" +
                "                      16\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      323123213,\n" +
                "                      'None',\n" +
                "                      17\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      111,\n" +
                "                      'None',\n" +
                "                      18\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      2222,\n" +
                "                      'None',\n" +
                "                      19\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      10,\n" +
                "                      31232131,\n" +
                "                      'None',\n" +
                "                      20\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      321,\n" +
                "                      'None',\n" +
                "                      21\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      321,\n" +
                "                      'None',\n" +
                "                      22\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      10,\n" +
                "                      32131231,\n" +
                "                      'None',\n" +
                "                      23\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      10,\n" +
                "                      32313213321213213,\n" +
                "                      'None',\n" +
                "                      24\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      8,\n" +
                "                      12331232323,\n" +
                "                      'None',\n" +
                "                      25\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      'UserImages/anypics.ru-1284.jpg',\n" +
                "                      8,\n" +
                "                      444444,\n" +
                "                      'None',\n" +
                "                      26\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '/UserImages/image.jpg',\n" +
                "                      10,\n" +
                "                      321313333,\n" +
                "                      'None',\n" +
                "                      27\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      10,\n" +
                "                      999,\n" +
                "                      'None',\n" +
                "                      28\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '/UserImages/5_2.jpg',\n" +
                "                      10,\n" +
                "                      888,\n" +
                "                      'None',\n" +
                "                      29\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.64877400 1574005314Crysis 3.jpg',\n" +
                "                      8,\n" +
                "                      321312,\n" +
                "                      'None',\n" +
                "                      30\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.73902700 1574005344wallpaper-1908623.jpg',\n" +
                "                      8,\n" +
                "                      'dsadasdadssada',\n" +
                "                      'None',\n" +
                "                      31\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      10,\n" +
                "                      'Ох',\n" +
                "                      'None',\n" +
                "                      32\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      7,\n" +
                "                      8,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      10,\n" +
                "                      999,\n" +
                "                      'None',\n" +
                "                      33\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      13,\n" +
                "                      11,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.97341500 1575033666DCD710C2-KS.jpg',\n" +
                "                      15,\n" +
                "                      'DEWALT DCD710C2',\n" +
                "                      'DCD710C2-KS',\n" +
                "                      35\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      13,\n" +
                "                      11,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.87124800 1575034139DCD991P2-QW.jpg',\n" +
                "                      15,\n" +
                "                      'DEWALT XRP DCD991P2',\n" +
                "                      'DCD991P2-QW',\n" +
                "                      36\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      13,\n" +
                "                      11,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.48574500 1575034280DCD791P2-QW.jpg',\n" +
                "                      15,\n" +
                "                      'DEWALT XRP DCD791P2',\n" +
                "                      'DCD791P2-QW',\n" +
                "                      37\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      14,\n" +
                "                      12,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.64980900 1575379102печать.png',\n" +
                "                      16,\n" +
                "                      'Отвертка',\n" +
                "                      123,\n" +
                "                      39\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      14,\n" +
                "                      13,\n" +
                "                      '',\n" +
                "                      '/UserImages/0.25380000 1575379121Подпись.png',\n" +
                "                      16,\n" +
                "                      'Подпись',\n" +
                "                      111,\n" +
                "                      40\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      16,\n" +
                "                      14,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      17,\n" +
                "                      'Шуруповерт 1',\n" +
                "                      'SDAS123',\n" +
                "                      41\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      16,\n" +
                "                      14,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      17,\n" +
                "                      'Шуруповерт 2',\n" +
                "                      'DSADS1676',\n" +
                "                      42\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      'TEST',\n" +
                "                      '123asd',\n" +
                "                      43\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      44\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      123,\n" +
                "                      'dsa',\n" +
                "                      45\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      'DCD770',\n" +
                "                      1,\n" +
                "                      46\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      'DCD771',\n" +
                "                      2,\n" +
                "                      47\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      'DCD777',\n" +
                "                      3,\n" +
                "                      48\n" +
                "                  ),\n" +
                "                  (\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      0,\n" +
                "                      'DCD791',\n" +
                "                      4,\n" +
                "                      49\n" +
                "                  );\n")

        db?.execSQL("INSERT INTO SENSORS (\n" +
                "                        ACTIVE,\n" +
                "                        ID_GOOD,\n" +
                "                        ID_TYPE,\n" +
                "                        SERIAL,\n" +
                "                        ID_STAND,\n" +
                "                        ID_SENS\n" +
                "                    )\n" +
                "                    VALUES (\n" +
                "                        1,\n" +
                "                        49,\n" +
                "                        1,\n" +
                "                        'ac:23:3f:a2:7a:c9',\n" +
                "                        1,\n" +
                "                        19\n" +
                "                    ),\n" +
                "                    (\n" +
                "                        1,\n" +
                "                        48,\n" +
                "                        1,\n" +
                "                        'ac:23:3f:a2:7a:cb',\n" +
                "                        1,\n" +
                "                        20\n" +
                "                    ),\n" +
                "                    (\n" +
                "                        1,\n" +
                "                        47,\n" +
                "                        1,\n" +
                "                        'ac:23:3f:a2:7a:c3',\n" +
                "                        1,\n" +
                "                        21\n" +
                "                    ),\n" +
                "                    (\n" +
                "                        1,\n" +
                "                        46,\n" +
                "                        1,\n" +
                "                        'ac:23:3f:a2:7a:c7',\n" +
                "                        1,\n" +
                "                        22\n" +
                "                    );\n")

        db?.execSQL("INSERT INTO LOG_ALL_SENSORS (\n" +
                "                                SYNC,\n" +
                "                                DATETIME,\n" +
                "                                STAND,\n" +
                "                                NUMBER,\n" +
                "                                ID\n" +
                "                            )\n" +
                "                            VALUES (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:46.436960',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:cb',\n" +
                "                                6\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:46.531826',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:cb',\n" +
                "                                7\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:46.635785',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:cb',\n" +
                "                                8\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:48.089873',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:c3',\n" +
                "                                9\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:48.896256',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:c3',\n" +
                "                                10\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:49.602006',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:c3',\n" +
                "                                11\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:49.797010',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:c3',\n" +
                "                                12\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                0,\n" +
                "                                '2020-01-25 20:48:51.670437',\n" +
                "                                1,\n" +
                "                                'ac:23:3f:a2:7a:c7',\n" +
                "                                13\n" +
                "                            );\n")

        db?.execSQL("INSERT INTO STAND_VIDEO (\n" +
                "                            ID_VIDEO,\n" +
                "                            ID_GOOD_2,\n" +
                "                            ID_GOOD,\n" +
                "                            ID_STAND,\n" +
                "                            ID\n" +
                "                        )\n" +
                "                        VALUES (\n" +
                "                            26,\n" +
                "                            0,\n" +
                "                            46,\n" +
                "                            1,\n" +
                "                            47\n" +
                "                        ),\n" +
                "                        (\n" +
                "                            25,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            1,\n" +
                "                            48\n" +
                "                        ),\n" +
                "                        (\n" +
                "                            27,\n" +
                "                            0,\n" +
                "                            47,\n" +
                "                            1,\n" +
                "                            50\n" +
                "                        );\n")

        db?.execSQL("INSERT INTO VIDEOS (\n" +
                "                       ID_PRJ,\n" +
                "                       PATH,\n" +
                "                       FILENAME,\n" +
                "                       ID_VIDEO\n" +
                "                   )\n" +
                "                   VALUES (\n" +
                "                       18,\n" +
                "                       '/UserVideos/18video0.39887100 1579828918.mp4',\n" +
                "                       'intro',\n" +
                "                       25\n" +
                "                   ),\n" +
                "                   (\n" +
                "                       18,\n" +
                "                       '/UserVideos/18video0.53045700 1579965314.mp4',\n" +
                "                       1,\n" +
                "                       26\n" +
                "                   ),\n" +
                "                   (\n" +
                "                       18,\n" +
                "                       '/UserVideos/18video0.87677300 1579965323.mp4',\n" +
                "                       2,\n" +
                "                       27\n" +
                "                   );\n")


    }

    companion object {
        const val ASSETS_PATH = "databases"
        const val DATABASE_NAME = "iotech"
        const val DATABASE_VERSION = 1
    }
}