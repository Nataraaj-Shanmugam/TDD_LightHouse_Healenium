package functional.utilities;

import functional.genericKeywords.GenericKeywords;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for providing data from Google Sheets.
 */
public class DataProviderUtility {


    static HashMap<String, HashMap<String, HashMap<String, Integer>>> eachSheetHeaderData = new HashMap<>();
    static HashMap<String, HashMap<String, List<List<Object>>>> eachSheetTestData = new HashMap<>();

    /**
     * Retrieves data from a specified Google Sheet and organizes it into header and test data.
     *
     * @param sheetId The ID of the Google Sheet.
     * @param sheetName The name of the specific sheet within the Google Sheet.
     * @return An array containing header data and test data.
     */
    public static Object[] getData(String sheetId, String sheetName){
        if(!eachSheetHeaderData.containsKey(sheetId) || !eachSheetHeaderData.get(sheetId).containsKey(sheetName)){
            List<List<Object>> testData = GoogleSheet.getData(sheetId, sheetName);
            setHeaderData(testData.get(0), sheetId,sheetName, !eachSheetHeaderData.containsKey(sheetId));
            eachSheetTestData.put(sheetId, new HashMap<String, List<List<Object>>>() {{
                put(sheetName, testData.subList(1, testData.size()));
            }});
        }
        return new Object[]{eachSheetHeaderData.get(sheetId).get(sheetName), eachSheetTestData.get(sheetId).get(sheetName)};
    }

    /**
     * Sets the header data for a given sheet.
     *
     * @param testData List of objects representing header data.
     * @param sheetId The ID of the Google Sheet.
     * @param sheetName The name of the specific sheet within the Google Sheet.
     * @param isNewSheet Boolean indicating if it's a new sheet.
     */
    private static void setHeaderData(List<Object> testData, String sheetId, String sheetName, boolean isNewSheet){
        HashMap<String, Integer> map = new HashMap<>(testData.size());
        for(int i = 0; i < testData.size();i++)
            map.put((String)testData.get(i), i );
        if(isNewSheet)
            eachSheetHeaderData.put(sheetId, new HashMap<>());
        eachSheetHeaderData.get(sheetId).put(sheetName, map);
    }

    /**
     * Retrieves browser details from a Google Sheet.
     *
     * @return A HashSet containing lists of strings representing browser details.
     */
    public static HashSet<List<String>> getBrowserDetails(){
        GenericKeywords genericKeywords = new GenericKeywords();
        return GoogleSheet.getData(genericKeywords.getPropertyValue("sheetId"), genericKeywords.getPropertyValue("browserDetails"))
                .stream()
                .skip(1)
                .map(each -> each.stream()
                        .map(obj -> obj != null ? obj.toString() : "")
                        .collect(Collectors.toList()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Nested static class for interacting with Google Sheets.
     * Provides functionalities to manipulate and retrieve data from Google Sheets.
     */
    protected static class GoogleSheet {


        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

        /**
         * The directory path for storing tokens.
         */
        private static final String TOKENS_DIRECTORY_PATH = "tokens";

        /**
         * The file path for the credentials JSON file.
         */
        private static final String CREDENTIALS_FILE_PATH = "./Credentials.json";

        /**
         * Retrieves credentials for Google Sheets API access.
         *
         * @param HTTP_TRANSPORT The network HTTP Transport.
         * @return Credential object for accessing Google Sheets.
         * @throws IOException If the credentials file is not found.
         */
        private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
                throws IOException {

            InputStream in = DataProviderUtility.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            List<String> scopes = new ArrayList<>();
            scopes.add(SheetsScopes.SPREADSHEETS_READONLY);
            return new AuthorizationCodeInstalledApp(
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in)), scopes)
                            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                            .setAccessType("offline")
                            .build()
                    , receiver).authorize("user");
        }

        /**
         * Creates and returns a Google Sheets service.
         *
         * @return An instance of the Sheets service.
         */
        private static Object getService() {
            try {
                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName("Read Google Sheet").build();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * Retrieves a Sheets service for manipulating spreadsheet data.
         *
         * @return A Sheets service for data operations.
         */
        private static Sheets getSheetService(){
            return (Sheets) getService();
        }

        /**
         * Retrieves a Sheets.Spreadsheets.Values service for reading and writing values to sheets.
         *
         * @return A Sheets.Spreadsheets.Values service.
         */
        private static Sheets.Spreadsheets.Values getSheetData() {
            return getSheetService().spreadsheets().values();
        }

        /**
         * Retrieves data from a specified range in a Google Sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param sheetName The name of the specific sheet within the Google Sheet.
         * @param startCell The starting cell of the range.
         * @param endCell The ending cell of the range.
         * @return A list of lists containing the data from the specified range.
         */
        public static List<List<Object>> getData(String sheetId, String sheetName, String startCell, String endCell) {
            try {
                String range = (startCell.trim().isEmpty() && endCell.trim().isEmpty()) ?
                        sheetName :
                        endCell.trim().isEmpty() ? sheetName + "!" + startCell :
                                sheetName + "!" + startCell + ":" + endCell;

                List<List<Object>> values = getSheetData()
                        .get(sheetId, range)
                        .setValueRenderOption("UNFORMATTED_VALUE")
                        .setDateTimeRenderOption("FORMATTED_STRING")
                        .setMajorDimension("ROWS")
                        .execute().getValues();

                int maxColumns = values.get(0).size();

                values.forEach(row -> {
                    while (row.size() < maxColumns) {
                        row.add("");
                    }
                });
                return values;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Creates a new sheet within a Google Sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param sheetName The name of the sheet to be created.
         */
        public static void createSheet(String sheetId, String sheetName) {
            try {
                getSheetService().spreadsheets().batchUpdate(sheetId,
                        new BatchUpdateSpreadsheetRequest().setRequests(Collections.singletonList(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(sheetName)))))
                ).execute().getSpreadsheetId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Creates multiple new sheets within a Google Sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param sheetNames A list of names for the new sheets to be created.
         */
        public static void createSheets(String sheetId, List<String> sheetNames) {
            try {
                ArrayList<Request> requests = new ArrayList<>();
                for (String sheetName : sheetNames)
                    requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(sheetName))));
                getSheetService().spreadsheets().batchUpdate(sheetId,
                        new BatchUpdateSpreadsheetRequest().setRequests(requests)
                ).execute().getSpreadsheetId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Clears a specified range in a Google Sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param range The range to be cleared in A1 notation.
         */
        public static void clearSheetRange(String sheetId, String range) {
            try {
                BatchClearValuesRequest request = new BatchClearValuesRequest();
                request.setRanges(Collections.singletonList(range));
                getSheetService().spreadsheets().values().batchClear(sheetId, request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Updates a specified range in a Google Sheet with the provided data.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param range The range to be updated in A1 notation.
         * @param dataToUpdate A list of lists containing the data to update in the specified range.
         */
        public static void updateSheet(String sheetId, String range, List<List<Object>> dataToUpdate) {
            try {
                BatchUpdateValuesRequest requestBody = new BatchUpdateValuesRequest();
                requestBody.setValueInputOption("USER_ENTERED");
                requestBody.setData(Collections.singletonList(new ValueRange().setRange(range).setValues(dataToUpdate)));
                getSheetService().spreadsheets().values().batchUpdate(sheetId, requestBody).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Clears and updates a specified range in a Google Sheet with the provided data.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param range The range to be cleared and updated in A1 notation.
         * @param dataToUpdate A list of lists containing the data to update in the specified range.
         */
        public static void clearAndUpdateSheet(String sheetId, String range, List<List<Object>> dataToUpdate) {
            clearSheetRange(sheetId,range);
            updateSheet(sheetId, range, dataToUpdate);
        }

        /**
         * Retrieves data from a specified cell in a Google Sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param sheetName The name of the specific sheet within the Google Sheet.
         * @param cell The cell to retrieve data from in A1 notation.
         * @return A list of lists containing the data from the specified cell.
         */
        public static List<List<Object>> getData(String sheetId, String sheetName, String cell) {
            return getData(sheetId, sheetName, cell, "");
        }

        /**
         * Retrieves all data from a specified sheet in a Google Sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @param sheetName The name of the specific sheet within the Google Sheet.
         * @return A list of lists containing all the data from the specified sheet.
         */
        public static List<List<Object>> getData(String sheetId, String sheetName) {
            return getData(sheetId, sheetName, "", "");
        }

        /**
         * Retrieves the names of all sheets within a Google Sheet, excluding the 'Master' sheet.
         *
         * @param sheetId The ID of the Google Sheet.
         * @return A HashSet containing the names of all sheets within the Google Sheet.
         */
        public static HashSet<String> getAllSheetName(String sheetId) {
            HashSet<String> sheetNames = new HashSet<>();
            try {
                for (Sheet sheet : getSheetService().spreadsheets().get(sheetId).execute().getSheets())
                    sheetNames.add(sheet.getProperties().getTitle());
                sheetNames.remove("Master");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sheetNames;
        }
    }
}
