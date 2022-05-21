import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //LINK YT: https://youtu.be/19DRMN9ykRg

    private static List<StoredProcedure> storedProcedures = new ArrayList<>();

    private static String username = "root";
    private static String password = "password";
    private static String dbName = "gestionemonumenti";
    private static String dbIp = "127.0.0.1";
    private static String dbPortN = "3306";

    public static void main(String[] args) {
        istantiateStoredProcedures();
        Scanner scanner = new Scanner(System.in);

        StoredProcedure chosenStoredProcedure = letUserChooseAction(scanner);
        String[] columnsNames = chosenStoredProcedure.getColumnsNames();
        String callingSyntax;

        try {
            callingSyntax = chosenStoredProcedure.getCallingSyntax(scanner);
        } catch (IllegalParameterException e) {
            System.out.println("Data inserted wrongly, cannot proceed");
            return;
        }

        List<Object[]> rows;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + dbIp + ":" + dbPortN
                + "/" + dbName, username, password)) {
            rows = getRowsOfSP(connection, callingSyntax, chosenStoredProcedure.getColumnsNames());
        } catch (SQLException e) {
            System.out.printf("%s happened, cannot proceed", e.toString());
            return;
        }

        new ResultTable(columnsNames, rows, new int[]{700, 200}).setVisible(true);

    }

    private static StoredProcedure letUserChooseAction(Scanner scanner) {
        int chosenActionIndex;
        while (true) {
            printChoices();
            chosenActionIndex = scanner.nextInt();
            if (chosenActionIndex < 1 || chosenActionIndex > storedProcedures.size())
                System.out.println("Select a VALID action");
            else
                break;
        }
        return storedProcedures.get(chosenActionIndex - 1);
    }

    private static void printChoices() {
        System.out.println("Select an action: ");
        int i = 0;
        for (StoredProcedure storedProcedure : storedProcedures)
            System.out.printf("%d) %s%n", (i++) + 1, storedProcedure.getDescription());

        return;
    }

    private static List<Object[]> getRowsOfSP(Connection connection, String callingSyntax, String[] columnsNames) throws SQLException {
        List<Object[]> rows = new ArrayList<>();

        CallableStatement callableStatement = connection.prepareCall(String.format("{ call %s}", callingSyntax));
        callableStatement.execute();

        ResultSet resultSet = callableStatement.getResultSet();

        while (resultSet.next()) {
            Object[] row = new Object[columnsNames.length];
            int i = 0;
            for (String columnName : columnsNames)
                row[i++] = resultSet.getString(columnName);
            rows.add(row);
        }

        return rows;

    }

    private static void istantiateStoredProcedures() {
        istantiateEstimateYearlyFundSP();
        istantiateAssociationsSearcherSP();
        istantiatePresidentsSearcherSP();
        istantiateMonumentSearcherSP();
        return;
    }

    private static void istantiateEstimateYearlyFundSP() {
        String description = "Get estimate of funds for the associations";
        String name = "processEstimateOfYearlyFund";
        List<Parameter> parameters = new ArrayList<>();
        String[] columnsNames = new String[]{"codice", "stimaFinanziamento"};
        storedProcedures.add(new StoredProcedure(description, name, parameters, columnsNames));
        return;
    }

    private static void istantiateAssociationsSearcherSP() {
        String description = "Get the associations which received a value of funds counting each year in a range";
        String name = "getFundedAssociationsInRange";
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("min value", Parameter.Datatype.INT));
        parameters.add(new Parameter("max value", Parameter.Datatype.INT));
        String[] columnsNames = new String[]{"codice", "cittàSede", "provinciaSede", "totalFund"};
        storedProcedures.add(new StoredProcedure(description, name, parameters, columnsNames));
        return;
    }

    private static void istantiatePresidentsSearcherSP() {
        String description = "Get the presidents of associations with a minimum assets value";
        String name = "getPresidentsWithAssetsStartingFrom";
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("min value", Parameter.Datatype.INT));
        String[] columnsNames = new String[]{"nome", "cognome", "codiceFiscale", "codiceAssociazione", "capitale"};
        storedProcedures.add(new StoredProcedure(description, name, parameters, columnsNames));
        return;
    }

    private static void istantiateMonumentSearcherSP() {
        String description = "Get the monuments around you filtering construction year";
        String name = "getMonumentsAroundPoint";
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("your latitude", Parameter.Datatype.DOUBLE));
        parameters.add(new Parameter("your longitude", Parameter.Datatype.DOUBLE));
        parameters.add(new Parameter("maximum km of distance", Parameter.Datatype.DOUBLE));
        parameters.add(new Parameter("from year", Parameter.Datatype.INT));
        parameters.add(new Parameter("to year", Parameter.Datatype.INT));
        String[] columnsNames = new String[]{"nome", "annoCreazione", "distance", "descrizione", "nomeArtista", "cognomeArtista", "dataDiNascita", "dataDiMorte"};
        storedProcedures.add(new StoredProcedure(description, name, parameters, columnsNames));
        return;
    }

}
