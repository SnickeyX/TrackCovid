package edu.harvard.cs50.trackcovid;

public class Country {
    private String name;
    private int total_cases;
    private int total_recovered;
    private int total_deaths;

    Country(String name, int total_cases, int total_recovered, int total_deaths){
        this.name = name;
        this.total_cases = total_cases;
        this.total_recovered = total_recovered;
        this.total_deaths = total_deaths;
    }

    public String getName(){return name;}
    public int getTotal_cases(){return total_cases;}
    public int getTotal_recovered(){return total_recovered;}
    public int getTotal_deaths(){return total_deaths;}

}
