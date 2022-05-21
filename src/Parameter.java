public class Parameter {

    public enum Datatype{
        INT, DOUBLE, YEAR;

        public String getValueFromString(String string){
            try {
                switch (this) {
                    case INT -> {
                        return String.valueOf(Integer.parseInt(string));
                    }
                    case DOUBLE -> {
                        return String.valueOf(Double.parseDouble(string));
                    }
                    case YEAR -> {
                        int year = Integer.parseInt(string);
                        year -= 1900;
                        if (year >= 0 && year < 256)
                            return String.valueOf(year);
                    }
                }
            }catch(NumberFormatException e){
                throw new IllegalArgumentException();
            }
            throw new RuntimeException("unimplemented code");
        }
    }

    private String name;
    private Datatype type;


    public Parameter(String name, Datatype type){
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public String getValueFromString(String string){
        try {
            return type.getValueFromString(string);
        } catch(IllegalArgumentException e){
            throw new IllegalParameterException();
        }
    }

}
