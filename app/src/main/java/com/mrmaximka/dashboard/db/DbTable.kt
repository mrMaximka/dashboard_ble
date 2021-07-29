package com.mrmaximka.dashboard.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mrmaximka.dashboard.model.*
import java.lang.Exception


class DbTable {

    private val standID: Int = 1

    fun loadStand(db: SQLiteDatabase): StandSettings {
        var cursor: Cursor = db.query("STAND",
            null, "ID_STAND=$standID", null, null, null, null)

        val list = StandSettings()
        cursor.moveToFirst()
        val name = cursor.getColumnIndex("NAME")
        val timezone = cursor.getColumnIndex("GMT_TIME")
        val description = cursor.getColumnIndex("DESCRIPTION")

            list.name = cursor.getString(name)
            list.timezone = cursor.getString(timezone)
            list.description = cursor.getString(description)
        cursor.close()

        cursor = db.query("STAND_PARAMETRS",
            null, "ID_STAND=$standID", null, null, null, null)

        cursor.moveToFirst()
        val delay = cursor.getColumnIndex("DELAY")
        val waitTime = cursor.getColumnIndex("WAIT_TIME")
        val stop = cursor.getColumnIndex("BREAK")
        val replay = cursor.getColumnIndex("REPLAY")
        val stopPlayOne = cursor.getColumnIndex("STOP_PLAY_ONE")
        val stopPlayTwo = cursor.getColumnIndex("STOP_PLAY_TWO")
        val breakTwo = cursor.getColumnIndex("BREAK_PLAY_TWO")
        val sensitivity = cursor.getColumnIndex("SENSITIVITY")

        list.delay = cursor.getInt(delay)
        list.wait_time = cursor.getInt(waitTime)
        list.stop = cursor.getInt(stop)
        list.replay = cursor.getInt(replay)
        list.stopPlayOne = cursor.getInt(stopPlayOne)
        list.stopPlayTwo = cursor.getInt(stopPlayTwo)
        list.breakTwo = cursor.getInt(breakTwo)
        list.sensitivity = cursor.getInt(sensitivity)
        cursor.close()

        return list
    }

    fun loadActive(db: SQLiteDatabase): ArrayList<ActiveSensors> {
        val cursor: Cursor = db.query("SENSORS",
            null, "ID_STAND=$standID", null, null, null, null)

        val list: ArrayList<ActiveSensors> = ArrayList()

        cursor.moveToFirst()
        val idSens = cursor.getColumnIndex("ID_SENS")
        val serial = cursor.getColumnIndex("SERIAL")
        val idGood = cursor.getColumnIndex("ID_GOOD")
        try {
            do{
                val activeSensors = ActiveSensors()
                activeSensors.idSens = cursor.getInt(idSens)
                activeSensors.serial = cursor.getString(serial)
                val request = getActiveName(db, cursor.getString(idGood))
                activeSensors.name = request.name
                activeSensors.articul = request.articul

                list.add(activeSensors)
            }while (cursor.moveToNext())
        }catch (e: Exception){
            Log.d("MMV", e.message)
        }

        cursor.close()
        return list
    }

    fun loadScan(db: SQLiteDatabase): ArrayList<ScanSensors> {
        val cursor: Cursor = db.query("LOG_ALL_SENSORS",
            null, null, null, null, null, null)

        val list: ArrayList<ScanSensors> = ArrayList()
        val map: HashMap<String, String> = HashMap()

        cursor.moveToFirst()
        val number = cursor.getColumnIndex("NUMBER")
        val date = cursor.getColumnIndex("DATETIME")
        try {
            do{
                val nameStr = cursor.getString(number)
                val dateStr = cursor.getString(date)

                map[nameStr] = dateStr
            }while (cursor.moveToNext())

            for ((key, value) in map.entries) {
                val scanSensors = ScanSensors()
                scanSensors.name = key
                scanSensors.date = value
                list.add(scanSensors)
            }
        }catch (e: Exception){
            Log.d("MMV", e.message)
        }

        cursor.close()
        return list
    }

    fun loadScripts(db: SQLiteDatabase): ArrayList<ScriptList> {
        val cursor: Cursor = db.query("STAND_VIDEO",
            null, "ID_STAND=$standID", null, null, null, null)

        val list: ArrayList<ScriptList> = ArrayList()

        cursor.moveToFirst()
        val idScript = cursor.getColumnIndex("ID")
        val idGood = cursor.getColumnIndex("ID_GOOD")
        val idGood2 = cursor.getColumnIndex("ID_GOOD_2")
        val idVideo = cursor.getColumnIndex("ID_VIDEO")
        try {
            do{
                val scriptList = ScriptList()
                scriptList.idScript = cursor.getInt(idScript)
                val nameGood1 = getActiveName(db, cursor.getString(idGood))
                val nameGood2 = getActiveName(db, cursor.getString(idGood2))
                scriptList.good1 = nameGood1.name
                scriptList.good2 = nameGood2.name
                scriptList.video = getVideoName(db, cursor.getInt(idVideo))

                list.add(scriptList)
            }while (cursor.moveToNext())

        }catch (e: Exception){
            Log.d("MMV", e.message)
        }

        cursor.close()
        return list
    }

    fun loadBrands(db: SQLiteDatabase): ArrayList<BrandModel> {
        val cursor: Cursor = db.query("BRANDS",
            null, null, null, null, null, null)

        val list: ArrayList<BrandModel> = ArrayList()
        cursor.moveToFirst()

        val idBrand = cursor.getColumnIndex("ID_BRAND")
        val name = cursor.getColumnIndex("NAME")
        try {
            do{
                val brandModel = BrandModel()
                brandModel.idBrand = cursor.getInt(idBrand)
                brandModel.name = cursor.getString(name)

                list.add(brandModel)
            }while (cursor.moveToNext())
        }catch (e: Exception){
            Log.d("MMV", e.message)
        }

        cursor.close()
        return list
    }

    fun loadCategories(db: SQLiteDatabase): ArrayList<CategoryModel> {
        val cursor: Cursor = db.query("CATEGORIES",
            null, null, null, null, null, null)

        val list: ArrayList<CategoryModel> = ArrayList()
        cursor.moveToFirst()

        val idCat = cursor.getColumnIndex("ID_CAT")
        val name = cursor.getColumnIndex("NAME")
        try {
            do{
                val categoriesModel = CategoryModel()
                categoriesModel.idCategory = cursor.getInt(idCat)
                categoriesModel.name = cursor.getString(name)

                list.add(categoriesModel)
            }while (cursor.moveToNext())
        }catch (e: Exception){
            Log.d("MMV", e.message)
        }

        cursor.close()
        return list
    }

    fun loadProducts(db: SQLiteDatabase): ArrayList<ProductModel> {
        val cursor: Cursor = db.query("GOODS",
            null, null, null, null, null, null)

        val list: ArrayList<ProductModel> = ArrayList()
        cursor.moveToFirst()

        val idGood = cursor.getColumnIndex("ID_GOOD")
        val idBrand = cursor.getColumnIndex("ID_BRAND")
        val idCat = cursor.getColumnIndex("ID_CAT")
        val name = cursor.getColumnIndex("NAME")
        val articul = cursor.getColumnIndex("ARTICUL")
        try {
            do{
                val productModel = ProductModel()
                productModel.name = cursor.getString(name)
                productModel.articul = cursor.getString(articul)
                productModel.idGood = cursor.getInt(idGood)
                productModel.brand = getBrandName(db, cursor.getInt(idBrand))
                productModel.category = getCategoryName(db, cursor.getInt(idCat))

                list.add(productModel)
            }while (cursor.moveToNext())

        }catch (e: Exception){
            Log.d("MMV", e.message)
        }

        cursor.close()
        return list
    }

    fun updateStand(
        database: SQLiteDatabase,
        name: String,
        timezone: String,
        description: String
    ) {
        database.execSQL("UPDATE STAND\n" +
                "       SET NAME = '" + name + "',\n" +
                "       DESCRIPTION = '" + timezone + "',\n" +
                "       GMT_TIME = '" + description + "'\n" +
                " WHERE ID_STAND = $standID;")
    }

    fun updateMove(
        database: SQLiteDatabase,
        etStandVideoDelay: Int,
        etStandWaitTime: Int,
        cbStop: Int,
        cbReplay: Int,
        etStandPlayOne: Int,
        etStandPlayTwo: Int,
        cbBreakTwo: Int,
        etStandSensitivity: Int
    ) {
        database.execSQL("UPDATE STAND_PARAMETRS\n" +
                "       SET DELAY = '$etStandVideoDelay',\n" +
                "       WAIT_TIME = '$etStandWaitTime',\n" +
                "       BREAK = '$cbStop',\n" +
                "       REPLAY = '$cbReplay',\n" +
                "       STOP_PLAY_ONE = '$etStandPlayOne',\n" +
                "       STOP_PLAY_TWO = '$etStandPlayTwo',\n" +
                "       BREAK_PLAY_TWO = '$cbBreakTwo',\n" +
                "       SENSITIVITY = '$etStandSensitivity'\n" +
                " WHERE ID = $standID;")

    }

    fun delSensor(database: SQLiteDatabase, idSens: Int?) {
        database.execSQL("DELETE FROM SENSORS\n" +
                "      WHERE ID_SENS = $idSens;")
    }

    fun delScript(database: SQLiteDatabase, idScript: Int?) {
        database.execSQL("DELETE FROM STAND_VIDEO\n" +
                "      WHERE ID = $idScript;")
    }

    fun delBrand(database: SQLiteDatabase, idBrand: Int?) {
        database.execSQL("DELETE FROM BRANDS\n" +
                "      WHERE ID_BRAND = $idBrand;")
    }

    fun delCategory(database: SQLiteDatabase, idCat: Int?) {
        database.execSQL("DELETE FROM CATEGORIES\n" +
                "      WHERE ID_CAT = $idCat;")
    }

    fun delProduct(database: SQLiteDatabase, idGood: Int?) {
        database.execSQL("DELETE FROM GOODS\n" +
                "      WHERE ID_GOOD = $idGood;")
    }

    fun editSensor(
        database: SQLiteDatabase,
        idSens: Int?,
        idGood: Int?
    ) {
        database.execSQL("UPDATE SENSORS\n" +
                "   SET ID_GOOD = '$idGood'\n" +
                " WHERE ID_SENS = $idSens;")
    }

    fun editScript(
        database: SQLiteDatabase,
        idGood1: Int?,
        idGood2: Int?,
        idScript: Int?,
        idVideo: Int?
    ) {
        database.execSQL("UPDATE STAND_VIDEO\n" +
                "   SET ID_GOOD = '$idGood1',\n" +
                "       ID_GOOD_2 = '$idGood2',\n" +
                "       ID_VIDEO = '$idVideo'\n" +
                " WHERE ID = $idScript;")
    }

    fun editProduct(
        database: SQLiteDatabase,
        idGood: Int,
        name: String,
        articul: String,
        idCat: Int?,
        idBrand: Int?

        ) {
        database.execSQL("UPDATE GOODS\n" +
                "   SET ARTICUL = '$articul',\n" +
                "       NAME = '$name',\n" +
                "       ID_CAT = '$idCat',\n" +
                "       ID_BRAND = '$idBrand'\n" +
                " WHERE ID_GOOD = $idGood;")
    }

    fun editBrand(
        database: SQLiteDatabase,
        name: String,
        idBrand: Int?
    ) {
        database.execSQL("UPDATE BRANDS\n" +
                "   SET NAME = '$name'\n" +
                " WHERE ID_BRAND = $idBrand;")
    }

    fun editCategory(
        database: SQLiteDatabase,
        name: String,
        idCat: Int?
    ) {
        database.execSQL("UPDATE CATEGORIES\n" +
                "   SET NAME = '$name'\n" +
                " WHERE ID_CAT = $idCat;")
    }

    fun addSensor(database: SQLiteDatabase, name: String) {

        database.execSQL("INSERT INTO SENSORS (\n" +
                "                        ACTIVE,\n" +
                "                        ID_GOOD,\n" +
                "                        ID_TYPE,\n" +
                "                        SERIAL,\n" +
                "                        ID_STAND\n" +
                "                    )\n" +
                "                    VALUES (\n" +
                "                        1,\n" +
                "                        0,\n" +
                "                        1,\n" +
                "                        '$name',\n" +
                "                        1\n" +
                "                    );")

    }

    fun addScript(
        database: SQLiteDatabase,
        idGood1: Int?,
        idGood2: Int?,
        idVideo: Int?
    ) {
        database.execSQL("INSERT INTO STAND_VIDEO (\n" +
                "                            ID_VIDEO,\n" +
                "                            ID_GOOD_2,\n" +
                "                            ID_GOOD,\n" +
                "                            ID_STAND\n" +
                "                        )\n" +
                "                        VALUES (\n" +
                "                            $idVideo,\n" +
                "                            $idGood2,\n" +
                "                            $idGood1,\n" +
                "                            $standID\n" +
                "                        );\n")
    }

    fun addProduct(
        database: SQLiteDatabase,
        name: String,
        articul: String,
        idCat: Int?,
        idBrand: Int?

        ) {
        database.execSQL("INSERT INTO GOODS (\n" +
                "                      ID_PRJ,\n" +
                "                      ID_BRAND,\n" +
                "                      DESCRIPTION,\n" +
                "                      PICTURE,\n" +
                "                      ID_CAT,\n" +
                "                      NAME,\n" +
                "                      ARTICUL\n" +
                "                  )\n" +
                "                  VALUES (\n" +
                "                      1,\n" +
                "                      $idBrand,\n" +
                "                      '',\n" +
                "                      '',\n" +
                "                      $idCat,\n" +
                "                      '$name',\n" +
                "                      '$articul'\n" +
                "                  );\n")
    }

    fun addBrand(
        database: SQLiteDatabase,
        name: String
    ) {
        database.execSQL("INSERT INTO BRANDS (\n" +
                "                       ID_PRJ,\n" +
                "                       NAME\n" +
                "                   )\n" +
                "                   VALUES (\n" +
                "                       1,\n" +
                "                       '$name'\n" +
                "                   );")
    }

    fun addCategory(
        database: SQLiteDatabase,
        name: String
    ) {
        database.execSQL("INSERT INTO CATEGORIES (\n" +
                "                       ID_PRJ,\n" +
                "                       NAME\n" +
                "                   )\n" +
                "                   VALUES (\n" +
                "                       1,\n" +
                "                       '$name'\n" +
                "                   );")
    }

    fun addVideo(
        database: SQLiteDatabase,
        path: String,
        name: String
    ) {
        database.execSQL("INSERT INTO VIDEOS (\n" +
                "                       ID_PRJ,\n" +
                "                       PATH,\n" +
                "                       FILENAME\n" +
                "                   )\n" +
                "                   VALUES (\n" +
                "                       $standID,\n" +
                "                       '$path',\n" +
                "                       '$name'\n" +
                "                   );")
    }

    fun addLog(
        database: SQLiteDatabase,
        mac: String,
        currentDate: String
    ) {
        Log.d("MMV", "Writing")
        database.execSQL("INSERT INTO LOG_ALL_SENSORS (\n" +
                "                                SYNC,\n" +
                "                                DATETIME,\n" +
                "                                STAND,\n" +
                "                                NUMBER\n" +
                "                            )\n" +
                "                            VALUES (\n" +
                "                                0,\n" +
                "                                '$currentDate',\n" +
                "                                1,\n" +
                "                                '$mac'\n" +
                "                            );")
    }


    fun getGoodsList(db: SQLiteDatabase): GoodsList {

        val cursor: Cursor = db.query("GOODS",
            null, "NAME!=0", null, null, null, null)

        val map: HashMap<String, String> = HashMap()
        val mapId: HashMap<String, Int> = HashMap()

        cursor.moveToFirst()
        val name = cursor.getColumnIndex("NAME")
        val articul = cursor.getColumnIndex("ARTICUL")
        val _id = cursor.getColumnIndex("ID_GOOD")

        val request = GoodsList()
        do{
            val key = cursor.getString(name)
            val value = cursor.getString(articul)
            val idGood = cursor.getInt(_id)

            map[key] = value
            mapId[key] = idGood
        }while (cursor.moveToNext())

        request.map = map
        request.mapId = mapId

        cursor.close()
        return request
    }

    fun getSensorsGoods(db: SQLiteDatabase): HashMap<Int, String> {

        val cursor: Cursor = db.query("SENSORS",
            null, "ID_STAND=$standID", null, null, null, null)

        val map: HashMap<Int, String> = HashMap()
        cursor.moveToFirst()
        val idGood = cursor.getColumnIndex("ID_GOOD")

        do{
            val key = cursor.getInt(idGood)
            val request = getActiveName(db, cursor.getString(idGood))
            val value = request.name

            map[key] = value

        }while (cursor.moveToNext())
        map[0] = "Нет товара"
        cursor.close()

        return map
    }

    fun getScriptVideos(db: SQLiteDatabase): HashMap<Int, String> {

        val cursor: Cursor = db.query("VIDEOS",
            null, null, null, null, null, null)

        val map: HashMap<Int, String> = HashMap()
        cursor.moveToFirst()
        val idVideo = cursor.getColumnIndex("ID_VIDEO")

        do{
            val key = cursor.getInt(idVideo)
            val value = getVideoName(db, cursor.getInt(idVideo))

            map[key] = value

        }while (cursor.moveToNext())

        cursor.close()
        map[0] = "Нет видео"
        return map
    }

    fun getSensitivity(db: SQLiteDatabase): Int {

        val cursor: Cursor = db.query("STAND_PARAMETRS",
            null, "ID_STAND=1", null, null, null, null)

        cursor.moveToFirst()
        val sens = cursor.getColumnIndex("SENSITIVITY")
        val sensitivity = cursor.getInt(sens)
        cursor.close()
        return sensitivity
    }

    private fun getBrandName(db: SQLiteDatabase, idBrand: Int): String {

        return if (idBrand == 0){
            "null"
        }else{
            val cursor: Cursor
            try {

                cursor = db.query("BRANDS",
                    null, "ID_BRAND=$idBrand", null, null, null, null)
            }catch (e: Exception){
                e.message
                return "null"
            }

            cursor.moveToFirst()
            val name: Int
            val brandName: String
            try {
                name = cursor.getColumnIndex("NAME")
                brandName = cursor.getString(name)
            }catch (e: Exception){
                e.message
                return "null"
            }
            cursor.close()

            brandName
        }
    }

    private fun getCategoryName(db: SQLiteDatabase, idCategory: Int): String {

        return if (idCategory == 0){
            "null"
        }else{
            val cursor: Cursor
            try {
                cursor = db.query("CATEGORIES",
                    null, "ID_CAT=$idCategory", null, null, null, null)
            }catch (e: Exception){
                e.message
                return "null"
            }

            cursor.moveToFirst()
            val name: Int
            val catName: String
            try {
                name = cursor.getColumnIndex("NAME")
                catName = cursor.getString(name)
            }catch (e: Exception){
                e.message
                return "null"
            }

            cursor.close()

            catName
        }
    }

    private fun getActiveName(db: SQLiteDatabase, string: String): ActiveRequest {

        val request = ActiveRequest()
        if (string == "0"){
            request.name = "null"
            request.articul = "null"
            return request
        }
        val cursor: Cursor = db.query("GOODS",
            null, "ID_GOOD=$string", null, null, null, null)

        cursor.moveToFirst()
        val name = cursor.getColumnIndex("NAME")
        val articul = cursor.getColumnIndex("ARTICUL")
        request.name = cursor.getString(name)
        request.articul = cursor.getString(articul)
        cursor.close()

        return request
    }

    private fun getVideoName(db: SQLiteDatabase, idVideo: Int): String {

        return if (idVideo == 0){
            "null"
        }else{
            val cursor: Cursor = db.query("VIDEOS",
                null, "ID_VIDEO=$idVideo", null, null, null, null)

            cursor.moveToFirst()
            val name = cursor.getColumnIndex("FILENAME")
            val videoName = cursor.getString(name)
            cursor.close()

            videoName
        }
    }
}