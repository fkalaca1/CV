package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.net.HttpURLConnection;

import javax.xml.transform.Result;

import static android.app.DownloadManager.STATUS_RUNNING;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class KnjigePoznanika extends IntentService {
    String idKorisnika;
    ResultReceiver receiver;
    public int STATUS_FINISH = 1;
    public int STATUS_START = 0;
    public int STATUS_ERROR = 2;

    ArrayList<Knjiga> rezultat = new ArrayList<>();

    public KnjigePoznanika(){
        super(null);
    }
    public KnjigePoznanika(String name) {
        super(name);
    }
    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        idKorisnika = intent.getStringExtra("idkorisnika");
        receiver = intent.getParcelableExtra("resultReceiver");
        Bundle bundle = new Bundle();
        receiver.send(STATUS_START, Bundle.EMPTY);
        try{
            String query = null;
            query = URLEncoder.encode(idKorisnika, "utf-8");
            String url1 = "https://www.googleapis.com/books/v1/users/"+query+"/bookshelves";
            //https://www.googleapis.com/books/v1/users/113529346842306589642/bookshelves
            //String url1 = "https://www.googleapis.com/books/v1/users/101891275874213662915/bookshelves/1001/volumes";
            URL url = new URL(url1);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responceCode = httpConnection.getResponseCode();
            ArrayList<String> novi = new ArrayList<>();
            if(responceCode == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(httpConnection.getInputStream());
                String rezultat_in = convertStreamToString(in);
                JSONObject jo_prvi = new JSONObject(rezultat_in);

                JSONArray jo_items;
                if (jo_prvi.has("items")) jo_items = jo_prvi.getJSONArray("items");
                else jo_items = new JSONArray();

                for (int i = 0; i < jo_items.length(); i++){
                    JSONObject jo_bookshelf = jo_items.getJSONObject(i);

                    String knjiga_id;
                    if (jo_bookshelf.has("id")) knjiga_id = jo_bookshelf.getString("id");
                    else knjiga_id = "Greska";
                    novi.add(knjiga_id);
                }
            }
            for(int j=0; j<novi.size();j++){
                String url2 = "https://www.googleapis.com/books/v1/users/"+query+"/bookshelves/" + novi.get(j)+  "/volumes";
                url = new URL(url2);
                connection = url.openConnection();
                httpConnection = (HttpURLConnection) connection;
                responceCode = httpConnection.getResponseCode();
                if(responceCode == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(httpConnection.getInputStream());
                    String rezultat_in = convertStreamToString(in);
                    JSONObject jo_prvi = new JSONObject(rezultat_in);

                    JSONArray jo_items;
                    if (jo_prvi.has("items")) jo_items = jo_prvi.getJSONArray("items");
                    else jo_items = new JSONArray();

                    for (int i = 0; i < jo_items.length(); i++){
                        JSONObject jo_knjiga = jo_items.getJSONObject(i);

                        String knjiga_id;
                        if (jo_knjiga.has("id")) knjiga_id = jo_knjiga.getString("id");
                        else knjiga_id = "Greska";

                        JSONObject volumeInfo;
                        if (jo_knjiga.has("volumeInfo"))
                            volumeInfo = jo_knjiga.getJSONObject("volumeInfo");
                        else volumeInfo = new JSONObject();

                        String knjiga_naziv;
                        if (volumeInfo.has("title")) knjiga_naziv = volumeInfo.getString("title");
                        else knjiga_naziv = "Greska";

                        JSONArray jo_autori = new JSONArray();
                        if (volumeInfo.has("authors")) jo_autori = volumeInfo.getJSONArray("authors");
                        else jo_autori = new JSONArray();
                        ArrayList<Autor> knjiga_Autori = new ArrayList<>();
                        for (int is = 0; is < jo_autori.length(); is++) {
                            Autor x_autor = new Autor(jo_autori.get(is).toString(), knjiga_id);
                            knjiga_Autori.add(x_autor);
                        }

                        String knjiga_opis;
                        if (volumeInfo.has("description"))
                            knjiga_opis = volumeInfo.getString("description");
                        else knjiga_opis = "Greska";

                        String knjiga_datumObjavljivanja;
                        if (volumeInfo.has("publishedDate"))
                            knjiga_datumObjavljivanja = volumeInfo.getString("publishedDate");
                        else knjiga_datumObjavljivanja = "Greska";

                        URL knjiga_slika = null;
                        if(volumeInfo.has("imageLinks") && volumeInfo.getJSONObject("imageLinks").has("thumbnail")) {
                            String tmp1= volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                            knjiga_slika=new URL(tmp1);
                        }
                        else knjiga_slika=null;

                        int knjiga_brojStrinica;
                        if (volumeInfo.has("pageCount"))
                            knjiga_brojStrinica = volumeInfo.getInt("pageCount");
                        else knjiga_brojStrinica = 0;

                        Knjiga y_knjiga = new Knjiga(knjiga_id, knjiga_naziv, knjiga_Autori, knjiga_opis,
                                knjiga_datumObjavljivanja, knjiga_slika, knjiga_brojStrinica);
                        rezultat.add(y_knjiga);
                    }

                }
            }
        }catch(Exception e){
            receiver.send(STATUS_ERROR,bundle);
        }
        bundle.putParcelableArrayList("result",rezultat);
        receiver.send(STATUS_FINISH, bundle);

    }

    public String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            while((line = reader.readLine() ) != null){
                sb.append(line + "\n" );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
