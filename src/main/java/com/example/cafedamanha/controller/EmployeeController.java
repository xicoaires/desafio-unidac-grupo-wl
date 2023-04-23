/**
 * Nome do arquivo: EmployeeController.java
 * Autor: Francisco Aires
 * Data de criação: 21/04/2023
 * Descrição: Classe Java que implementa um controlador REST para gerenciar as operações CRUD em objetos Employee. 
 */

package com.example.cafedamanha.controller;
import com.example.cafedamanha.entity.Employee;
import com.example.cafedamanha.service.EmployeeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @CrossOrigin(origins = "https://desafio-unidac-angular.netlify.app/")
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    // Retorna todos os  todos os registros
    @GetMapping
    public List<Employee> getAllEmployees() { 
        return employeeService.getAllEmployees();
    }

    // Retorna um registro específico com o ID 
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);

    }

    // Adiciona um novo registro
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
            var newEmployee = employeeService.addEmployee(employee);
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    //Atualiza um registro existente com um ID
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        if (employee.getId() == null){ // Caso não passe o id
            employee.setId(id);
        }
        return new ResponseEntity<>(employeeService.updateEmployeeNative(employee), HttpStatus.OK);
    }

    //Exclui um registro com um ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        var employee = employeeService.getEmployeeById(id);

        if (employee == null){
            throw new IllegalArgumentException("Colaborador não existente");
        }

        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
