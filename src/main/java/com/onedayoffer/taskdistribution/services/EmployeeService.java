package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable String sortDirection) {
        List<Employee> employees;

        if (sortDirection != null && !sortDirection.isEmpty()) {
            Sort.Direction direction = Sort.Direction.valueOf(sortDirection.toUpperCase());
            employees = employeeRepository.findAllAndSort(Sort.by(direction, "fio"));
        } else {
            employees = employeeRepository.findAll();
        }
        Type listType = new TypeToken<List<EmployeeDTO>>() {}.getType();
        List<EmployeeDTO> employeeDTOs = modelMapper.map(employees, listType);
        return employeeDTOs;
    }

    @Transactional
    public EmployeeDTO getOneEmployee(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Type type = new TypeToken<EmployeeDTO>() {}.getType();
            EmployeeDTO employeeDTO = modelMapper.map(employee.get(), type);
            return employeeDTO;
        }
        return  null;
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        List<Task> tasks = taskRepository.findAllByEmployeeId(id);

        Type listType = new TypeToken<List<TaskDTO>>() {}.getType();
        List<TaskDTO> taskDTOS = modelMapper.map(tasks, listType);
        return taskDTOS;
    }

    @Transactional
    public void changeTaskStatus(Integer taskId, TaskStatus status) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        task.setStatus(status);
        taskRepository.save(task);

    }

    @Transactional
    public void postNewTask(Integer employeeId, TaskDTO newTask) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EntityNotFoundException::new);
        Type type = new TypeToken<Task>() {}.getType();
        Task task = modelMapper.map(newTask, type);
        task.setEmployee(employee);
        task.setStatus(newTask.getStatus());
        taskRepository.save(task);
    }
}
