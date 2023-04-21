/**
 * Nome do arquivo: EmployeeService.java
 * Autor: Francisco Aires
 * Data de criação: 21/04/2023
 * Descrição: Classe de serviços da entidade Employee
 */

package com.example.cafedamanha.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.cafedamanha.entity.Employee;
import com.example.cafedamanha.repository.EmployeeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    //Retorna uma lista com todos os resgistros cadastrados utilizando framework
    public List<Employee> getAllEmployeesFramework() {
        return employeeRepository.findAll();
    }

    //Retorna um registro específico pelo seu ID
    public Employee getEmployeeById(Long id) {

            Query query = entityManager.createNativeQuery("SELECT * FROM employee WHERE id = ?", Employee.class);
            query.setParameter(1, id);
            List<Employee> result = query.getResultList();
            if (!result.isEmpty()) {
                return result.get(0);
            } else {
                return null;
            }
    }

    // Retorna uma lista com todos os resgistros cadastrados
    public List<Employee> getAllEmployees() {
        Query query = entityManager.createNativeQuery("SELECT * FROM employee", Employee.class);
        List<Employee> result = query.getResultList();
        return result;
    }

    //Retorna um registo específico pelo seu código.
    public Employee getEmployeeByCode(String code) {
        Query query = entityManager.createNativeQuery("SELECT * FROM employee WHERE code = ?", Employee.class);
        query.setParameter(1, code);
        List<Employee> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    //Adiciona um novo registro no banco de dados.
    @Transactional
    public Employee addEmployee(Employee employee) {

        UUID uuid = UUID.randomUUID();
        employee.setCode(uuid.toString());
        Query query = entityManager
                .createNativeQuery("INSERT INTO employee (name, cpf, food_option, date, verification, code) " +
                        "VALUES (?, ?, ?, ?, ?,?)");

        query.setParameter(1, employee.getName());
        query.setParameter(2, employee.getCpf());
        query.setParameter(3, employee.getFoodOption());
        query.setParameter(4, employee.getDate());
        query.setParameter(5, employee.getVerification());
        query.setParameter(6, employee.getCode());

        query.executeUpdate();

        return getEmployeeByCode(employee.getCode());
    }

    //Atualiza um regsitoro existente no banco de dados usando o framework.
    @Transactional
    public Employee updateEmployee(Employee employee) {
        // Utilizando com o framework
        Employee existingEmployee = employeeRepository.findById(employee.getId()).orElseThrow();
        existingEmployee.setName(employee.getName());
        existingEmployee.setCpf(employee.getCpf());
        existingEmployee.setFoodOption(employee.getFoodOption());
        existingEmployee.setDate(employee.getDate());
        existingEmployee.setVerification(employee.getVerification());
        return employeeRepository.save(existingEmployee);
    }

    
    //Atualiza um registro existente no banco de dados usando a abordagem NativeQuery.
    @Transactional
    public Employee updateEmployeeNative(Employee employee) {

        Query query = entityManager.createNativeQuery("UPDATE employee SET name = ?, cpf = ?, food_option = ?, " +
                "date = ?, verification = ? WHERE id = ?");
        query.setParameter(1, employee.getName());
        query.setParameter(2, employee.getCpf());
        query.setParameter(3, employee.getFoodOption());
        query.setParameter(4, employee.getDate());
        query.setParameter(5, employee.getVerification());
        query.setParameter(6, employee.getId());
        query.executeUpdate();

        return this.getEmployeeById(employee.getId());
    }

    //Deleta um registro existente do banco de dados
    @Transactional
    public boolean deleteEmployeeById(Long id) {
        Query query = entityManager.createNativeQuery("DELETE FROM employee WHERE id = ?");
        query.setParameter(1, id);        
        return query.executeUpdate() > 0;
    }
}
