/**
 * Nome do arquivo: Employee.java
 * Autor: Francisco Aires
 * Data de criação: 21/04/2023
 * Descrição: Entidade Employee
 */

package com.example.cafedamanha.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;


@Entity // Indica que a classe é uma entidade JPA 
@Data //Anotação do projeto Lombok que gera automaticamente métodos getters, setters
public class Employee {
    @Id //Indica que o próximo atributo é a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.AUTO)  //Indica que o valor do atributo é gerado automaticamente pelo banco de dados. (Não usado em insert utilizando NativeQuery)
    
    //Atributos
    private Long id;
    
    private String code;
    private String name;
    private String cpf;
    private String foodOption;
    private String date;
    private Boolean verification;

    public Employee() {} //Contrutor vazio

    public Employee(String name, String cpf, String foodOption, String date, Boolean verification) { //Contrutor quando passa o atributo verification
        this.name = name;
        this.cpf = cpf;
        this.foodOption = foodOption;
        this.date = date;
        this.verification = verification;
    }

    public Employee(String name, String cpf, String foodOption, String date) { //Contrutor quando não passa o atributo verification
        this.name = name;
        this.cpf = cpf;
        this.foodOption = foodOption;
        this.date = date;
    }
}
