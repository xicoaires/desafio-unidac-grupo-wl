/**
 * Nome do arquivo: EmployeeRepository.java
 * Autor: Francisco Aires
 * Data de criação: 21/04/2023
 * Descrição: Repository da entidade Employee
 */

package com.example.cafedamanha.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cafedamanha.entity.Employee;

//interface do Spring Data JPA e contém métodos para lidar com operações de CRUD (create, read, update, delete) em uma entidade.
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
