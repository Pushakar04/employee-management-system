package com.ems.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ems.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("SELECT e FROM Employee e WHERE LOWER(e.employeeName) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<Employee> searchByName(@Param("name") String name, Pageable pageable);

	@Query("""
			SELECT e
			FROM Employee e
			WHERE LOWER(e.department.departmentName)
			LIKE LOWER(CONCAT('%', :deptName, '%'))
			""")
	Page<Employee> searchByDepartment(@Param("deptName") String deptName, Pageable pageable);

}
