public class RunnableAction<N,E extends Comparable> {

    String name, description;
    ParameterType[] parameters;

    // Getters

    public String getName() {

        return name;

    }

    public String getDescription() {

        return description;

    }

    public ParameterType[] getParameters() {

        return parameters;

    }

    // Constructor

    public RunnableAction(String name, String description, ParameterType[] parameters) {

        this.name = name;
        this.description = description;
        this.parameters = parameters;

    }

}
    
