package rafael.flores.ejercicio81;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // LinearLayout linearLayout = (LinearLayout)findViewById(R.id.info);

    private List<String> listaTextoArchivo=new ArrayList<String>();
    private ListView lista;
    private  String datos[];

    int REQUEST_CODE=200;

    private String carpeta="/archivos";
    private String file_path="";
    private File archivoSolucion;
    private File archivoPangrama;
    //private static final String FILE_NAME="Solucion.txt";
    private EditText ediTextFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificarPermisos();
        lista=findViewById(R.id.listViewTextoArchivo);
        this.file_path=(Environment.getExternalStorageDirectory()+this.carpeta);


        File localFile =new File(this.file_path);
        if(!localFile.exists()){
            localFile.mkdirs();
            Toast.makeText(this,"PATH:"+file_path,
                    Toast.LENGTH_LONG).show();

        }
        this.archivoSolucion =new File(localFile, "solucion.txt");
        this.archivoPangrama =new File(localFile, "pangrama.txt");

        try {
            this.archivoPangrama.createNewFile();
            this.archivoSolucion.createNewFile();


        }catch (IOException e){
            e.printStackTrace();
        }

        if(archivoPangrama.exists()){
            try {
                leerPangramas();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
public void verificarPermisos(){
        int PermisosSms= ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(PermisosSms!= PackageManager.PERMISSION_GRANTED){
         requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    REQUEST_CODE=200;
     PermisosSms= ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE);
    if(PermisosSms!= PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
    }
}
    public void mandarArchivoResultados(View view) throws IOException {
        leerPangramas();
        FileWriter fichero = null;
        PrintWriter pw = null;
        if(Integer.parseInt(listaTextoArchivo.get(0))<=listaTextoArchivo.size()){
            try {
                fichero = new FileWriter(archivoSolucion);
                pw = new PrintWriter(archivoSolucion);
                String[] pan;

                for(int i=1;i<=Integer.parseInt(listaTextoArchivo.get(0));i++){
                    String ca=listaTextoArchivo.get(i);
                    pan=esPangarama(listaTextoArchivo.get(i));


                    pw.printf("%-130s%-2s%-4s\n",pan[0],pan[1],pan[2]);

                }


                pw.flush();



                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(null!=fichero)
                        fichero.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Toast.makeText(this,"ARCHIVO GUARDADO EN:"+file_path,
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"NUMERO MAYOR",
                    Toast.LENGTH_LONG).show();
        }



    }
    public void leerPangramas() throws IOException {
        listaTextoArchivo.clear();
        String contenido="";
        String linea;
        FileInputStream fin=null;
        try {
            fin=new FileInputStream(archivoPangrama);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader archivoPan=new InputStreamReader(fin);
        BufferedReader reader=new BufferedReader(archivoPan);

            while((linea=reader.readLine())!=null){
                listaTextoArchivo.add(linea.split("\n")[0]);
            }
            archivoPan.close();

            datos=listaTextoArchivo.toArray(new String[listaTextoArchivo.size()]);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,datos);
            lista.setAdapter(adapter);

        Log.e("leerPangramas",contenido);
    }

    private String[]esPangarama(String cadena)
    {
        String [] respuesta=new String[3];
        String original=cadena;
        cadena=cadena.toUpperCase();
        cadena=cadena.replace(",","");
        cadena=cadena.replace(".","");
        cadena=cadena.replace(";","");
        cadena=cadena.replace("´","");
        cadena=cadena.replace(":","");
        cadena=cadena.replace("!","");
        cadena=cadena.replace("¡","");
        cadena=cadena.replace(" ","");
        int total=0;
        String texto= Normalizer.normalize(cadena, Normalizer.Form.NFD);
        texto=texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        String alfabeto="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String aux;
        for (int i=0; i<alfabeto.length(); i++){
            aux="versiyaconte";
            for(int j=0;j<texto.length();j++){

                if(Character.toString(texto.charAt(j)).equals(Character.toString(alfabeto.charAt(i))))
                {
                    if(aux.equals("versiyaconte")){
                        total++;
                        aux=Character.toString(alfabeto.charAt(i));
                    }
                }
            }


        }
        if(alfabeto.length()==total) {
            respuesta[0] = original;
            respuesta[1] = String.valueOf(cadena.length());
            respuesta[2] = "SI";
            return respuesta;
        }else {

            respuesta[0]=original;
            respuesta[1]=String.valueOf(cadena.length());
            respuesta[2]="NO";
            return respuesta;
        }

    }



    }


