package alararestaurant.service;

import alararestaurant.domain.dtos.EmployeeSeedDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Gson gson;
    private final PositionRepository positionRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    private final static String EMPLOYEE_FILE ="src/main/resources/files/employees.json";

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, Gson gson, PositionRepository positionRepository, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.employeeRepository = employeeRepository;
        this.gson = gson;
        this.positionRepository = positionRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean employeesAreImported() {

        return this.employeeRepository.count()>0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {

        return this.fileUtil.readFile(EMPLOYEE_FILE);
    }

    @Override
    public String importEmployees(String employees) throws IOException {
        StringBuilder importResult = new StringBuilder();
        EmployeeSeedDto[] employeeSeedDtos = this.gson.fromJson(new FileReader(EMPLOYEE_FILE),EmployeeSeedDto[].class);

        for (EmployeeSeedDto employeeSeedDto : employeeSeedDtos) {
            if(this.validationUtil.isValid(employeeSeedDto)){
                Position position =
                        this.positionRepository.findByName(employeeSeedDto.getPosition())
                                .orElse(new Position(employeeSeedDto.getPosition()));
                Employee employee = this.modelMapper.map(employeeSeedDto, Employee.class);
                employee.setPosition(position);
                System.out.println();
                this.employeeRepository.saveAndFlush(employee);
                importResult.append("Successfully imported Employee " + employee.getName()).append(System.lineSeparator());
            }else {
                importResult.append("Invalid data").append(System.lineSeparator());
            }
        }
        return importResult.toString().trim();
    }
}
