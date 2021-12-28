package cu.wilb3r.codechallengetm.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class BaseConverter<T> {
    @TypeConverter
    fun listToString(someObjects: List<T>?): String? {
        if (someObjects == null) {
            return null
        }
        val listType = object : TypeToken<List<T>>() {}.type
        return Gson().toJson(someObjects, listType)
    }

    @TypeConverter
    fun stringToList(data: String?): List<T>? {
        if (data == null) {
            return null
        }
        val listType = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(data, listType)
    }

}