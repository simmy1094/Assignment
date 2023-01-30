import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String amountToConvert = "10000000";
        String convertId = "2791";
        String uri = "https://pro-api.coinmarketcap.com/v2/tools/price-conversion";
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("amount", amountToConvert));
        parameters.add(new BasicNameValuePair("id", "3541"));
        parameters.add(new BasicNameValuePair("convert_id", convertId));

        try {
            System.out.println("Currency ---> Guatemalan Quetzal");
            System.out.println("Amount ---> " + amountToConvert);
            System.out.println("---> Converting to GBP ---->");

            String result = makeAPICall(uri, parameters);
            BigDecimal gbpAmount = getAmount(result, convertId);
            System.out.println("===> GBP Amount is " + gbpAmount);

            String convertSymbol = "DOGE";
            List<NameValuePair> parameters2 = new ArrayList<>();
            parameters2.add(new BasicNameValuePair("amount", String.valueOf(gbpAmount)));
            parameters2.add(new BasicNameValuePair("id", "2791"));
            parameters2.add(new BasicNameValuePair("convert", convertSymbol));

            String result2 = makeAPICall(uri, parameters2);
            BigDecimal dogeCoin = getAmount(result2, convertSymbol);

            System.out.println("---> Converting to DOGE COIN cryptocurrency ---->");
            System.out.println("===> dogeCoin is " + dogeCoin);
        } catch (IOException e) {
            System.out.println("Error: can't access content - " + e);
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e);
        }
    }

    private static String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String responseContent;

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        String apiKey = "d1521a5c-a2ea-4c3c-8900-7ea581c784fe";
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        try (CloseableHttpResponse response = client.execute(request)) {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }

        return responseContent;
    }

    private static BigDecimal getAmount(String responseContent, String convertId) {
        JSONObject obj = new JSONObject(responseContent);
        JSONObject dataObj = obj.getJSONObject("data");
        JSONObject quoteObj = dataObj.getJSONObject("quote");
        JSONObject convertIdObj = quoteObj.getJSONObject(convertId);
        return convertIdObj.getBigDecimal("price");
    }
}