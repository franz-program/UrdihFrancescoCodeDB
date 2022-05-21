import java.util.List;
import java.util.Scanner;

public class StoredProcedure {

    private String description;
    private String spName;
    private List<Parameter> parameters;
    private String[] resultColumnsNames;

    public StoredProcedure(String description, String spName, List<Parameter> parameters, String[] resultColumnsNames) {
        this.description = description;
        this.spName = spName;
        this.parameters = parameters;
        this.resultColumnsNames = resultColumnsNames;
    }

    public String getCallingSyntax(Scanner scanner) {

        String callingSyntax = this.spName + "(";

        for (Parameter parameter : parameters) {
            System.out.printf("Insert %s: ", parameter.getName());
            callingSyntax += parameter.getValueFromString(scanner.next()) + ",";
        }

        if (callingSyntax.endsWith(","))
            callingSyntax = callingSyntax.substring(0, callingSyntax.length() - 1);

        callingSyntax += ")";

        return callingSyntax;
    }

    public String getDescription(){
        return this.description;
    }

    public String[] getColumnsNames(){
        return this.resultColumnsNames;
    }


}
