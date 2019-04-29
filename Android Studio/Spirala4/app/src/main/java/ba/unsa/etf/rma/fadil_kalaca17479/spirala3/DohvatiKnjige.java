package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DohvatiKnjige extends AsyncTask <String, Integer, Void> {

    ArrayList<Knjiga> rezultat = new ArrayList<>();
    private IDohvatiKnjigeDone pozivatelj;

    public DohvatiKnjige(IDohvatiKnjigeDone p){
        pozivatelj = p;
    }
    @Override
    protected Void doInBackground(String... strings) {
      for(int t = 0; t< strings.length; t++) {
          String query = null;
          try {
              query = URLEncoder.encode(strings[t], "utf-8");
              String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=5";
              URL url = new URL(url1);
              URLConnection connection = url.openConnection();
              HttpURLConnection httpConnection = (HttpURLConnection) connection;
              int responceCode = httpConnection.getResponseCode();
              if (responceCode == HttpURLConnection.HTTP_OK) {
                  InputStream in = new BufferedInputStream(httpConnection.getInputStream());
                  String rezultat_in = convertStreamToString(in);
                  JSONObject jo_prvi = new JSONObject(rezultat_in);
                  JSONArray jo_items;
                  if (jo_prvi.has("items")) jo_items = jo_prvi.getJSONArray("items");
                  else jo_items = new JSONArray();

                  for (int i = 0; i < jo_items.length(); i++) {
                      JSONObject jo_knjiga = jo_items.getJSONObject(i);
                      //_____ID
                      String knjiga_id;
                      if (jo_knjiga.has("id")) knjiga_id = jo_knjiga.getString("id");
                      else knjiga_id = "Greska";
                      //_____INFORMACIJE KNJIGE volumeInfo
                      JSONObject volumeInfo;
                      if (jo_knjiga.has("volumeInfo"))
                          volumeInfo = jo_knjiga.getJSONObject("volumeInfo");
                      else volumeInfo = new JSONObject();

                      //_____NAZIV
                      String knjiga_naziv;
                      if (volumeInfo.has("title")) knjiga_naziv = volumeInfo.getString("title");
                      else knjiga_naziv = "Greska";
                      //_____AUTORI
                      JSONArray jo_autori = new JSONArray();
                      if (volumeInfo.has("authors")) jo_autori = volumeInfo.getJSONArray("authors");
                      else jo_autori = new JSONArray();
                      ArrayList<Autor> knjiga_Autori = new ArrayList<>();
                      for (int is = 0; is < jo_autori.length(); is++) {
                          Autor x_autor = new Autor(jo_autori.get(is).toString(), knjiga_id);
                          knjiga_Autori.add(x_autor);
                      }

                      //_____OPIS
                      String knjiga_opis;
                      if (volumeInfo.has("description"))
                          knjiga_opis = volumeInfo.getString("description");
                      else knjiga_opis = "Greska";

                      //_____DATUM
                      String knjiga_datumObjavljivanja;
                      if (volumeInfo.has("publishedDate"))
                          knjiga_datumObjavljivanja = volumeInfo.getString("publishedDate");
                      else knjiga_datumObjavljivanja = "Greska";

                      //_____URL URADI KASNIJE
                      URL knjiga_slika = null;
                      if(volumeInfo.has("imageLinks") && volumeInfo.getJSONObject("imageLinks").has("thumbnail")) {
                          String tmp1= volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                          knjiga_slika=new URL(tmp1);
                      }
                      else knjiga_slika=null;


                      //_____BrojStranica
                      int knjiga_brojStrinica;
                      if (volumeInfo.has("pageCount"))
                          knjiga_brojStrinica = volumeInfo.getInt("pageCount");
                      else knjiga_brojStrinica = 0;

                      //KRAJ
                      Knjiga y_knjiga = new Knjiga(knjiga_id, knjiga_naziv, knjiga_Autori, knjiga_opis,
                              knjiga_datumObjavljivanja, knjiga_slika, knjiga_brojStrinica);
                      rezultat.add(y_knjiga);

                  }
              }
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
        return null;
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

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(rezultat);
    }
    public interface IDohvatiKnjigeDone{
        public void onDohvatiDone(ArrayList<Knjiga> rezultat);
    }
}
