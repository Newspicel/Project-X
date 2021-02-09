package de.newspicel.projekt.projekt.api.home

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

class HomeManager {
    private val path: Path = Paths.get("plugins", "Project")
    private val file: Path = Paths.get("plugins", "Project", "homes.data")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    private fun init() {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (!Files.exists(file)) {
            try {
                Files.createFile(file)
                save(Homes(ArrayList()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun addHome(home: Home?) {
        val read = read()
        read?.homes?.add(home)
        save(read)
    }

    fun delHome(uuid: String?, name: String?) {
        val read = read()
        read?.homes?.removeIf { home: Home? -> home?.name.equals(name, ignoreCase = true) && home?.ownerUUID.equals(uuid, ignoreCase = true) }
        save(read)
    }

    fun getFromPlayerAndName(uuid: String?, name: String?): Homes {
        val collect = getFromPlayer(uuid).homes?.stream()?.filter { home: Home? -> home?.name.equals(name, ignoreCase = true) }?.collect(Collectors.toList()) as ArrayList<Home?>
        return Homes(collect)
    }

    fun getFromPlayer(uuid: String?): Homes {
        val collect = read()?.homes?.stream()?.filter { home: Home? -> home?.ownerUUID.equals(uuid, ignoreCase = true) }?.collect(Collectors.toList()) as ArrayList<Home?>
        return Homes(collect)
    }

    fun isExist(uuid: String?, name: String?): Boolean {
        return !getFromPlayerAndName(uuid, name).homes?.isEmpty()!!
    }

    fun toStringArray(uuid: String?): ArrayList<String?> {
        val names = ArrayList<String?>()
        getFromPlayer(uuid).homes?.forEach(Consumer { home: Home? -> names.add(home?.name) })
        return names
    }

    private fun save(homes: Homes?) {
        try {
            FileWriter(file.toFile()).use { writer ->
                val bw = BufferedWriter(writer)
                gson.toJson(homes, bw)
                bw.flush()
                writer.flush()
                bw.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun read(): Homes? {
        try {
            var homes = gson.fromJson(FileReader(file.toFile()), Homes::class.java)
            if (homes == null) {
                homes = Homes(ArrayList())
            }
            return homes
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    init {
        init()
    }
}
