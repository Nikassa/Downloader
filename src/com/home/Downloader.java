package com.home;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Downloader {

    private static Set<Links> scanSaving() throws IOException, ParseException {

        List<String> lines = new ArrayList<>();

        try {
            lines = Files.lines(Paths.get(Statics.LIST_PATH)).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("can't find file list.txt");
            e.printStackTrace();
        }

        Set<Links> links = new HashSet<>();

        for (String t : lines) {
            String[] linksArray = t.split(";");

            Date lastDownloadDate = null;
            if (!linksArray[4].equals("null")) {
                lastDownloadDate = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH).parse(linksArray[4]);
            }
            links.add(new Links(Integer.parseInt(linksArray[0]), linksArray[1], linksArray[2], linksArray[3], lastDownloadDate, Integer.parseInt(linksArray[5])));
        }

        return links;
    }

    private static void addNewLink(Set<Links> lines, String line) throws IOException {

        try {
            String[] linesArray = line.substring(4).split(";");
            lines.add(new Links(Integer.parseInt(linesArray[0]), linesArray[1], linesArray[2], linesArray[3], null, Integer.parseInt(linesArray[4])));

            savingList(lines);
            System.out.println("added successfully");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("can't add this string");
        }
    }

    private static void startDownload(Set<Links> lines, String line) throws IOException, ParseException {

        //по одному файлу или по нескольким
        Set<Links> tempLines = new HashSet<>();

        if (line.startsWith("start all")) {

            for (Links t : lines) {
                if (t.getiActive() == 1) {
                    tempLines.add(t);
                }
            }
        } else {

            try {
                int tempID = Integer.parseInt(line.substring(6));

                for (Links t : lines) {
                    if (t.getId().equals(tempID)) {
                        tempLines.add(t);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("wrong type id");
            }

        }

        //скачивание файла
        for (Links t : tempLines) {
            try (BufferedInputStream in = new BufferedInputStream(new URL(t.getFileLink()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(Statics.DOWNLOAD_FOLDER_PATH + t.getName() + t.getFileExtension())) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println("downloaded successfully");

            } catch (IOException e) {
                System.out.println("downloading error for link : " + t.getFileLink() );
                System.out.println(e);
            }
        }

        //записываем дату последнего скачивания
        for (Links t1 : lines) {
            for (Links t2 : tempLines) {
                if (t1.getId().equals(t2.getId())) {
                    t1.setLastDownload(new Date());
                }
            }
        }

        savingList(lines);
    }

    private static void savingList(Set<Links> lines){

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(Statics.LIST_PATH));

            for (Links t: lines) {
                bufferedWriter.append(t.toString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("saving error");
        }

    }


    public static void main(String[] args) throws IOException, ParseException {

        //при старте сканируется файл, содержащий базу адресов
        Set<Links> lines = scanSaving();

        //выдает список команд, которые можно ввести в консоль
        System.out.println("Cписок команд, которые можно ввести в консоль: \"list\" - вывести сохраненный список, \n" +
                "\"add <id>;<название>;<ссылка>;<.расширение>;<активность>\", \"start <id>\", \"start all\", \"exit\" - окончание работы.");
        //реализовать(?) "back start" - работа в фоне, "back stop", "back set <минуты>",

        //выполнение команд из консоли
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (!(line = bufferedReader.readLine()).equals("exit")) {

            if (line.equals("list")) {
                for (Links t : lines) {
                    System.out.println(t.toWrite());
                }
            } else if (line.startsWith("add ")) {
                addNewLink(lines, line);
            } else if (line.startsWith("start ")){
                startDownload(lines, line);
            }

        }

        System.out.println("closing");
    }

}