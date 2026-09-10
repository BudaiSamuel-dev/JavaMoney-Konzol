import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.classfile.constantpool.PackageEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class MoneyProgram {

    private final static File FILE = new File("/home/budaisamuel/Developer/Sources/money-data.txt");
    private final static File FILE_OUT = new File("/home/budaisamuel/Developer/Sources/money-kiadások.txt");

    public static void main(String[] args) {
        ArrayList<Penzmozgas> lista = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(FILE);

            while(fileScanner.hasNextLine()){
                String sor = fileScanner.nextLine();
                if(!sor.trim().isEmpty()){
                    String[] datas = sor.split("\\|");
                    String datum = datas[0].trim().strip();
                    char tipus = datas[1].trim().strip().charAt(0);
                    String megnevezes = datas[2].trim().strip();
                    int osszeg = Integer.parseInt(datas[3].trim().strip());

                    String fizetesMod = null;
                    if (datas.length > 4 && tipus == 'K'){
                        fizetesMod = datas[4].trim().strip();
                    }

                    Penzmozgas penzmozgas = new Penzmozgas(datum, tipus, megnevezes, osszeg);
                    if (fizetesMod != null){
                        penzmozgas.setFizetesiMod(fizetesMod);
                    }

                    lista.add(penzmozgas);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("A fájl nem található");
            System.exit(0);
        }

        System.out.printf("3. feladat\nÖsszesen %d tétel adatakerült beolvasásra.\n", lista.size());

        int bevetelSzam = 0;
        int kiadasSzam = 0;

        int bevetel = 0;
        int kiadas = 0;

        for (Penzmozgas penzmozgas : lista) {
            if (penzmozgas.getTipus() == 'B'){
                bevetelSzam++;
                bevetel += penzmozgas.getOsszeg();
            }else {
                kiadasSzam++;
                kiadas += penzmozgas.getOsszeg();
            }
        }

        System.out.printf("4. feladat\nBevételek száma: %d db\nKiadaások száma: %d db\n", bevetelSzam, kiadasSzam);
        System.out.printf("5. feladat\nA bevétel %d Ft, a kiadás %d Ft 2024. április hónap elejétől.\n",bevetel, kiadas);

        int osszegASzamlan = 75000;
        for (Penzmozgas penzmozgas : lista){
            int ev = Integer.parseInt(penzmozgas.getDatum().split("\\.")[0]);
            int honap = Integer.parseInt(penzmozgas.getDatum().split("\\.")[1]);
            int nap = Integer.parseInt(penzmozgas.getDatum().split("\\.")[2]);

            if (ev == 2024 && honap < 6){
                if (penzmozgas.getTipus() == 'B'){
                    osszegASzamlan += penzmozgas.getOsszeg();
                }else if(penzmozgas.getTipus() == 'K' && penzmozgas.getFizetesiMod().equalsIgnoreCase("BK")){
                    osszegASzamlan -= penzmozgas.getOsszeg();
                }
            }
        }

        System.out.printf("6. feladat\nBankszámlaegyenleg 2024. június 1-én: %d Ft", osszegASzamlan);

        try {
            FileWriter fileWriter = new FileWriter(FILE_OUT);

            ArrayList<Penzmozgas> kiadasok = new ArrayList<>();
            for (Penzmozgas penzmozgas : lista){
                if (penzmozgas.getTipus() == 'K'){
                    kiadasok.add(penzmozgas);
                }
            }

            //Kiadások sortolása
            //kiadasok.sort();

            for(Penzmozgas penzmozgas : kiadasok){
                String sor = String.format("%s -> %s (%d Ft)\n", penzmozgas.getDatum(), penzmozgas.getMegnevezes(), penzmozgas.getOsszeg());
                fileWriter.write(sor);
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
