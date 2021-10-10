package com.parlonsdev.resources;

import com.parlonsdev.dto.StudentDto;
import com.parlonsdev.entities.Student;
import com.parlonsdev.exception.ResourceNotFoundException;
import com.parlonsdev.message.Message;
import com.parlonsdev.repository.StudentRepository;
import com.parlonsdev.repository.TrainingRepository;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class StudentResource {

    private final StudentRepository studentRepository;
    private final TrainingRepository trainingRepository;

	public StudentResource(StudentRepository studentRepository, TrainingRepository trainingRepository) {
		this.studentRepository = studentRepository;
		this.trainingRepository = trainingRepository;
	}

	/*
     * GETTING ALL STUDENTS
     * URL : http://127.0.0.1:8080/trainings/detail/{trainingId}/students
	 */
    @GetMapping(path = "/trainings/detail/{trainingId}/students")
    public ResponseEntity<?> getAllStudents(@PathVariable Long trainingId, Pageable pageable){
        Page<Student> students = studentRepository.findByTrainingId(trainingId, pageable);
        if (students.isEmpty())
            return new ResponseEntity<>(new Message("List of students is empty!"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    
    /*
     * GETTING A SINGLE STUDENT BY ID
     * URL : http://127.0.0.1:8080/trainings/detail/{trainingId}/students/{studentId}
     */
    @GetMapping(path="/trainings/detail/{trainingId}/students/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long trainingId, @PathVariable Long studentId){
    	if(!trainingRepository.existsById(trainingId))
    		return new ResponseEntity<>(new Message("Training does not exist : "+trainingId+"!"), HttpStatus.NOT_FOUND);
    	if(!studentRepository.existsById(studentId))
    		return new ResponseEntity<>(new Message("Student does not exist : "+studentId+"!"), HttpStatus.NOT_FOUND);
    	
    	Student student = studentRepository.findById(studentId).get();
    	return new ResponseEntity<>(student, HttpStatus.OK);
    }
    
    /*
     * DELETE A STUDENT BY ID FROM DATABASE
     * URL : http://127.0.0.1:8080/trainings/detail/{trainingId}/students/{studentId}
     */
    @DeleteMapping(path="/trainings/detail/{trainingId}/students/{studentId}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long trainingId, @PathVariable Long studentId){
    	if(!trainingRepository.existsById(trainingId))
    		return new ResponseEntity<>(new Message("Training does not exist : "+trainingId+" !"), HttpStatus.NOT_FOUND);
    	if(!studentRepository.existsById(studentId))
    		return new ResponseEntity<>(new Message("Student does not exist : "+studentId+" !"), HttpStatus.NOT_FOUND);
    	
    	Student student = studentRepository.findById(studentId).get();
    	studentRepository.delete(student);
    	return new ResponseEntity<>(new Message("Student has been deleted successfully : "+studentId+" !"), HttpStatus.OK);
    }
    
    /*
     * POST A NEW STUDENT FROM DATABASE
     * IF WE USE THE POST METHOD IN THE POSTMAN, THEN WE IGNORE THE TRAINING PROPERTY
     * URL : http://127.0.0.1:8080/trainings/detail/{trainingId}/students
     */
    @PostMapping(path="/trainings/detail/{trainingId}/students")
    public ResponseEntity<?> saveStudentByIdTraining(@Valid @RequestBody StudentDto dto, @PathVariable Long trainingId){
    	if(!trainingRepository.existsById(trainingId))
    		return new ResponseEntity<>(new Message("Training does not exist : "+trainingId+"!"), HttpStatus.NOT_FOUND);
    	if(dto.getFirstName().isBlank() || dto.getLastName().isBlank() || dto.getEmail().isBlank() || dto.getPhone().isBlank())
    		return new ResponseEntity<>(new Message("This field is required!"), HttpStatus.BAD_REQUEST);
    	if(studentRepository.existsByEmail(dto.getEmail()))
    		return new ResponseEntity<>(new Message("Student email is already exist!"), HttpStatus.BAD_REQUEST);
    	if(studentRepository.existsByPhone(dto.getPhone()))
    		return new ResponseEntity<>(new Message("Student phone number is already exist!"), HttpStatus.BAD_REQUEST);
    	
    	Student student = new Student(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getPhone(), dto.getDob(), 
    			                       dto.getImageUrl(), dto.getAddress(), dto.getGender(), dto.isRegistered(), dto.getTraining());
    	 trainingRepository.findById(trainingId).map(training -> {
             student.setTraining(training);
             return studentRepository.save(student);
         });
    	return new ResponseEntity<>(new Message("Student ("+student.getFirstName()+" "+student.getLastName()+"), has been added successfully!"), HttpStatus.OK);
    }
    
    /*
     * UPDATE A PRODUCT BY ID FROM DATABASE
     * IF WE USE THE EDIT METHOD IN THE POSTMAN, THEN WE IGNORE THE TRAINING PROPERTY
     * URL : http://127.0.0.1:8080/trainings/detail/{trainingId}/students/{studentId}
     */
    @PutMapping(path="/trainings/detail/{trainingId}/students/{studentId}")
    public ResponseEntity<?> updateStudentByIdt(@PathVariable (value = "studentId") Long studentId,
                                                @PathVariable (value = "trainingId") Long trainingId,
                                                @Valid @RequestBody StudentDto dto) {
			if(!trainingRepository.existsById(trainingId)) {
			    throw new ResourceNotFoundException("Training id = " + trainingId + " not found");
			}
			if(!studentRepository.existsById(studentId)) {
			    throw new ResourceNotFoundException("Student id = " + studentId + " not found");
			}
			if(dto.getFirstName().isBlank() || dto.getLastName().isBlank() || dto.getEmail().isBlank() || dto.getPhone().isBlank())
    		   return new ResponseEntity<>(new Message("This field is required!"), HttpStatus.BAD_REQUEST);
    	    if(studentRepository.existsByEmail(dto.getEmail()) && studentRepository.findByEmailContaining(dto.getEmail()).get().getId() != studentId)
    		   return new ResponseEntity<>(new Message("Student email is already exist!"), HttpStatus.BAD_REQUEST);
    	    if(studentRepository.existsByPhone(dto.getPhone()) && studentRepository.findByPhoneContaining(dto.getPhone()).get().getId() != studentId)
    		   return new ResponseEntity<>(new Message("Student phone number is already exist!"), HttpStatus.BAD_REQUEST);
			
			studentRepository.findById(studentId).map(student -> {
			student.setFirstName(dto.getFirstName());
			student.setLastName(dto.getLastName());
			student.setEmail(dto.getEmail());
			student.setPhone(dto.getPhone());
			student.setDob(dto.getDob());
			student.setGender(dto.getGender());
			student.setAddress(dto.getAddress());
			student.setImageUrl(dto.getImageUrl());
			student.setRegistered(dto.isRegistered());
			return studentRepository.save(student);
			}).get();
			return new ResponseEntity<>(new Message("Student has been updated successfully : "+studentId+"!"), HttpStatus.OK);
     }

}
