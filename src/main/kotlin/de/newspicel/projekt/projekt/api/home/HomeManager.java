package de.newspicel.projekt.projekt.api.home;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HomeManager {

    private Path path;
    private Path file;
    private Gson gson;

    public HomeManager(JavaPlugin javaPlugin) {
        path = Paths.get("plugins", "Project");
        file = Paths.get("plugins", "Project", "homes.data");
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        init();
    }

    private void init() {
        if (!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Files.exists(file)) {
            try {
                Files.createFile(file);
                save(new Homes(new ArrayList<>()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void addHome(Home home){
        Homes read = read();
        read.getHomes().add(home);
        save(read);
    }

    public void delHome(String uuid, String name){
        Homes read = read();
        read.getHomes().removeIf(home -> home.getName().equalsIgnoreCase(name) && home.getOwnerUUID().equalsIgnoreCase(uuid));
        save(read);
    }

    public Homes getFromPlayerAndName(String uuid, String name){
        ArrayList<Home> collect = (ArrayList<Home>) getFromPlayer(uuid).getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        return new Homes(collect);
    }

    public Homes getFromPlayer(String uuid){
        ArrayList<Home> collect = (ArrayList<Home>) read().getHomes().stream().filter(home -> home.getOwnerUUID().equalsIgnoreCase(uuid)).collect(Collectors.toList());
        return new Homes(collect);
    }

    public boolean isExist(String uuid, String name){
        return !getFromPlayerAndName(uuid, name).getHomes().isEmpty();
    }

    public ArrayList<String> toStringArray(String uuid){
        ArrayList<String> names = new ArrayList<>();
        getFromPlayer(uuid).getHomes().forEach(home -> names.add(home.getName()));
        return names;
    }

    public void save(Homes homes) {
        try(Writer writer = new FileWriter(file.toFile())) {
            BufferedWriter bw = new BufferedWriter(writer);
            gson.toJson(homes, bw);
            bw.flush();
            writer.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Homes read(){
        try {
            Homes homes = gson.fromJson(new FileReader(file.toFile()), Homes.class);
            if (homes == null){
                homes = new Homes(new ArrayList<>());
            }
            return homes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
