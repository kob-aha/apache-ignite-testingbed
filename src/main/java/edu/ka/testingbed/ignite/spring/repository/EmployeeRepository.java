package edu.ka.testingbed.ignite.spring.repository;

import edu.ka.testingbed.ignite.model.EmployeeDTO;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

@RepositoryConfig(cacheName = "cache")
public interface EmployeeRepository extends IgniteRepository<EmployeeDTO, Integer> {

    EmployeeDTO getEmployeeDTOById(Integer id);
}
